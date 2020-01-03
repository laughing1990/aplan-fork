package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 并联审批。阶段二级流程完成成功或办件监听器
 */
public class StageProcFinishSuccessOrFailExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        AeaHiParStageinstMapper aeaHiParStageinstMapper = SpringContextHolder.getBean(AeaHiParStageinstMapper.class);
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        AeaHiSmsInfoMapper aeaHiSmsInfoMapper = SpringContextHolder.getBean(AeaHiSmsInfoMapper.class);
        AeaLinkmanInfoMapper aeaLinkmanInfoMapper = SpringContextHolder.getBean(AeaLinkmanInfoMapper.class);
        AeaHiApplyinstService aeaHiApplyinstService = SpringContextHolder.getBean(AeaHiApplyinstService.class);
        AeaHiParStageinstService aeaHiParStageinstService = SpringContextHolder.getBean(AeaHiParStageinstService.class);
        AplanmisEventPublisher publisher = SpringContextHolder.getBean(AplanmisEventPublisher.class);
        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);

        String procInstId = delegateExecution.getProcessInstanceId();

        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);

        ActStoAppinstSubflow actStoAppinstSubflow = null;
        try {
            actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            AeaHiParStageinst parStageinst = new AeaHiParStageinst();
            parStageinst.setApplyinstId(aeaHiApplyinst.getApplyinstId());
            parStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiParStageinst> stageinstList = aeaHiParStageinstMapper.listAeaHiParStageinst(parStageinst);
            if(stageinstList!=null&&stageinstList.size()>0){
                AeaHiParStageinst currParStage = stageinstList.get(0);

                AeaHiIteminst hiIteminst = new AeaHiIteminst();
                hiIteminst.setStageinstId(currParStage.getStageinstId());
                hiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaHiIteminst> hiIteminstList = aeaHiIteminstMapper.listAeaHiIteminst(hiIteminst);

                boolean passFlag = false;
                if(hiIteminstList!=null&&hiIteminstList.size()>0){
                    for(AeaHiIteminst iteminst:hiIteminstList){
                        //办结（通过）、办结（容缺通过）
                        if(ItemStatus.AGREE.getValue().equals(iteminst.getIteminstState())
                                ||ItemStatus.AGREE_TOLERANCE.getValue().equals(iteminst.getIteminstState())){
                            passFlag = true;
                            break;
                        }
                    }
                }

                //当所有事项有一个通过即流向发证节点，否则流向办结节点
                if(passFlag){
                    String parentTaskId = actStoAppinstSubflow.getTriggerTaskinstId();
                    if (StringUtils.isNotBlank(parentTaskId)) {
                        Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
                        if (task != null) {
                            taskService.complete(parentTaskId, new String[]{"fazheng"}, (Map) null);
//                            aeaHiParStageinstService.updateBusinessStateByApplyinstId(aeaHiApplyinst.getApplyinstId(),);
//                            aeaHiParStageinstService.updateBusinessStateByApplyinstId(aeaHiApplyinst.getApplyinstId(),);
                        }
                    }
                }else{
                    String parentTaskId = actStoAppinstSubflow.getTriggerTaskinstId();
                    if (StringUtils.isNotBlank(parentTaskId)) {
                        Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
                        if (task != null) {
                            taskService.complete(parentTaskId, new String[]{"banjie"}, (Map) null);

                            //触发流程发送事件，用于窗口办结发送短信
                            /*BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                            Process process = (Process)bpmnModel.getProcesses().get(0);
                            FlowElement flowElement = process.getFlowElement("banjie");
                            if(flowElement!=null && flowElement instanceof UserTask){
                                UserTask userTask = (UserTask)flowElement;
                                String assigneeStr = userTask.getAssignee();

                                if("$INITIATOR".equals(assigneeStr)){
                                    assigneeStr = (String) runtimeService.getVariable(task.getProcessInstanceId(),"$INITIATOR");
                                }
                                if(StringUtils.isBlank(assigneeStr))
                                    assigneeStr = userTask.getAssigneeRange();

                                if(StringUtils.isNotBlank(assigneeStr)) {
                                    BpmTaskSendObject sendObject = new BpmTaskSendObject();
                                    sendObject.setTaskId(parentTaskId);
                                    BpmTaskSendConfig bpmTaskSendConfig = new BpmTaskSendConfig();
                                    bpmTaskSendConfig.setDestActId("banjie");
                                    bpmTaskSendConfig.setUserTask(true);
                                    bpmTaskSendConfig.setAssignees(assigneeStr);
                                    List<BpmTaskSendConfig> list = new ArrayList<>();
                                    list.add(bpmTaskSendConfig);
                                    sendObject.setSendConfigs(list);
                                    publisher.publishEvent(new BpmNodeSendAplanmisEvent(this, sendObject));
                                }
                            }*/
//                            aeaHiParStageinstService.updateBusinessStateByApplyinstId(aeaHiApplyinst.getApplyinstId(),);
//                            aeaHiParStageinstService.updateBusinessStateByApplyinstId(aeaHiApplyinst.getApplyinstId(),);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
