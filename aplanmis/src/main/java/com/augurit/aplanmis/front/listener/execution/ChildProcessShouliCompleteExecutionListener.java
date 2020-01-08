package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门受理子流程完成事件（含通过和不通过）
 */
public class ChildProcessShouliCompleteExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String procInstId = delegateExecution.getProcessInstanceId();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        HistoryService historyService = CommandContextUtil.getProcessEngineConfiguration().getHistoryService();
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);
//        AplanmisEventPublisher publisher = SpringContextHolder.getBean(AplanmisEventPublisher.class);

//        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration().getRepositoryService();
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();

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

            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            HistoricVariableInstance iteminstIdObj = historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId).variableName("$BUS_CURRENT_ITEMINST_ID").singleResult();
            String iteminstId = (String) iteminstIdObj.getValue();
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            if (aeaHiIteminst != null) {

                if ("1".equals(aeaHiApplyinst.getIsSeriesApprove())) {//单项申报时,推送到综窗受理节点
                    taskService.complete(task.getId(), new String[]{"zongchuangqueren"}, null);
                } else {//并联申报时，直接往下推
                    taskService.complete(task.getId());
                }

                //触发流程发送事件，用于综窗受理发送短信
                /*BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                Process process = (Process)bpmnModel.getProcesses().get(0);
                FlowElement flowElement = process.getFlowElement("zongchuangshouli");
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
                        bpmTaskSendConfig.setDestActId("zongchuangshouli");
                        bpmTaskSendConfig.setUserTask(true);
                        bpmTaskSendConfig.setAssignees(assigneeStr);
                        List<BpmTaskSendConfig> list = new ArrayList<>();
                        list.add(bpmTaskSendConfig);
                        sendObject.setSendConfigs(list);
                        publisher.publishEvent(new BpmNodeSendAplanmisEvent(this, sendObject));
                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
