package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmDestTaskConfig;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.SolicitBusTypeEnum;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.service.solicit.AeaHiSolicitService;
import com.augurit.aplanmis.front.constant.SolicitConstant;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 联合评审或一次征询子流程完成父节点监听器
 */
public class SolicitFinishChildProcessExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String procInstId = delegateExecution.getProcessInstanceId();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        AeaHiSolicitService aeaHiSolicitService = SpringContextHolder.getBean(AeaHiSolicitService.class);
        BpmTaskService bpmTaskService = SpringContextHolder.getBean(BpmTaskService.class);

        try {
            ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
            if (actStoAppinstSubflow == null) {
                return;
            }
            String parentTaskId = actStoAppinstSubflow.getTriggerTaskinstId();
            if (parentTaskId == null) {
                return;
            }
            Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
            if (task == null) {
                return;
            }

            String hiTaskinstId = task.getId();

            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            AeaHiSolicit aeaHiSolicit = null;

            AeaHiSolicit solicit = new AeaHiSolicit();
            solicit.setApplyinstId(aeaHiApplyinst.getApplyinstId());
            List<AeaHiSolicit> solicitList = aeaHiSolicitService.listAeaHiSolicit(solicit);
            if (solicitList != null && solicitList.size() > 0) {
                Collections.sort(solicitList, new Comparator<AeaHiSolicit>() {
                    @Override
                    public int compare(AeaHiSolicit o1, AeaHiSolicit o2) {
                        return o2.getCreateTime().compareTo(o1.getCreateTime());
                    }
                });

                aeaHiSolicit = solicitList.get(0);
            }

            if (aeaHiSolicit == null)
                throw new RuntimeException("当前申报获取不到联合评审或一次征询记录，请确认监听器使用是否正确！");

            //直接根据汇总结论，决定流程流向
            if (SolicitBusTypeEnum.YCZX.getValue().equals(aeaHiSolicit.getBusType()) &&
                    SolicitConstant.SOLICIT_CONCLUSION_FLAG_TG.equals(aeaHiSolicit.getConclusionFlag())) {
                //一次征询需要汇总结论是通过的，才会结束当前节点，并往下流转
                // 注意：基于目前只有一个流向的情况，如果多于一个流向则需要按照实际需求优化此处代码
                taskService.complete(hiTaskinstId);
            } else if (SolicitBusTypeEnum.YJZQ.getValue().equals(aeaHiSolicit.getBusType())) {
                //意见征求默认往下推送，此处后续根据实际需求进行优化
                taskService.complete(hiTaskinstId);
            } else if (SolicitBusTypeEnum.LHPS.getValue().equals(aeaHiSolicit.getBusType())) {
                List<BpmDestTaskConfig> bpmDestTaskConfig = bpmTaskService.getBpmDestTaskConfigByCurrTaskId(hiTaskinstId);
                for (int i = 0, len = bpmDestTaskConfig.size(); i < len; i++) {
                    String destActId = bpmDestTaskConfig.get(i).getDestActId();
                    //如果联合评审结果时通过，则流向形式审查节点
                    if (SolicitConstant.SOLICIT_CONCLUSION_FLAG_TG.equals(aeaHiSolicit.getConclusionFlag()) &&
                            StringUtils.isNotBlank(destActId) && (!destActId.startsWith("endEvent") || !destActId.equals("jieshu"))) {
                        taskService.complete(hiTaskinstId, new String[]{destActId}, (Map) null);
                        return;
                    }
                    //如果不通过，则直接流向结束节点
                    if (SolicitConstant.SOLICIT_CONCLUSION_FLAG_BTG.equals(aeaHiSolicit.getConclusionFlag()) &&
                            StringUtils.isNotBlank(destActId) && (destActId.startsWith("endEvent") || destActId.equals("jieshu"))) {
                        taskService.complete(hiTaskinstId, new String[]{destActId}, (Map) null);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
