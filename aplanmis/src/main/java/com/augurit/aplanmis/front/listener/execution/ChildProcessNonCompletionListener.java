package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Gateway;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;


/**
 * 部门子流程不通过“办结”节点直接“不通过”办结
 */
public class ChildProcessNonCompletionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {

        String procInstId = delegateExecution.getProcessInstanceId();

        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);

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

            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            String iteminstId = (String) runtimeService.getVariable(procInstId, "$BUS_CURRENT_ITEMINST_ID");
            AeaHiIteminst aeaHiIteminst = null;
            if (StringUtils.isNotBlank(iteminstId)) {
                aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
                if (aeaHiIteminst == null) return;
//                aeaHiIteminst.setIteminstState(ItemStatus.OUT_SCOPE.getValue());//不予受理
//                aeaHiIteminstMapper.updateAeaHiIteminst(aeaHiIteminst);

                // 不予受理，保存历史记录
                AeaHiIteminstService aeaHiIteminstService = SpringContextHolder.getBean(AeaHiIteminstService.class);
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminstId, task.getId(), aeaHiApplyinst.getApplyinstId(), ItemStatus.OUT_SCOPE.getValue(), SecurityContext.getCurrentOrgId());
            }

            //单项申报：部门子流程“不通过”办结时，将一级流程流向办结；并联申报：部门子流程“不通过”办结，将二级子流程往下个节点流转。
            //判断该事项分局承办
            Boolean flag = aeaHiApplyinst.getIsBranchHandle().get(aeaHiIteminst.getIteminstCode());

            if ("1".equals(aeaHiApplyinst.getIsSeriesApprove())) {
                FlowElement flowElement = delegateExecution.getCurrentFlowElement();
                if (!(flowElement instanceof Gateway)) {
                    if (flag!=null&&flag) {
                        taskService.complete(task.getId());
                    } else {
                        taskService.complete(task.getId(), new String[]{"banjie"}, null);
                    }
                }
            } else if ("0".equals(aeaHiApplyinst.getIsSeriesApprove())) {
                taskService.complete(task.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
