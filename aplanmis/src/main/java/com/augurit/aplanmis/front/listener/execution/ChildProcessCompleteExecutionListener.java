package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
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
 * 事项审批子流程完成父节点监听器
 */
public class ChildProcessCompleteExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String procInstId = delegateExecution.getProcessInstanceId();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        HistoryService historyService = CommandContextUtil.getProcessEngineConfiguration().getHistoryService();
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);
        AplanmisEventPublisher publisher = SpringContextHolder.getBean(AplanmisEventPublisher.class);

        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration().getRepositoryService();
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

                if ("1".equals(aeaHiApplyinst.getIsSeriesApprove())) {
                    taskService.complete(task.getId(), new String[]{"banjie"}, null);
                } else {
                    //适应二级流程某个节点不通过时，直接跳过下面的节点，跳转到结束节点；要求不通过默认流向的节点为非人工节点，否则报错
                    //taskService.complete(task.getId());

                    BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                    Process process = bpmnModel.getProcesses().get(0);

                    //获取当前任务定义信息
                    UserTask currTaskElement = (UserTask) process.getFlowElement(task.getTaskDefinitionKey());

                    //解析当前节点的流向集合
                    List<SequenceFlow> outgoingFlows = currTaskElement.getOutgoingFlows();
                    if (outgoingFlows != null && outgoingFlows.size() > 0) {
                        //当只有一个流向时，直接往下流转
                        if (outgoingFlows.size() == 1) {
                            taskService.complete(task.getId());
                        } else {
                            List<String> taskFlowElementActIds = new ArrayList<>();//非办结节点的节点ID集合
                            String notPassActId = "jieshu";//必须有一个节点ID为jieshu的，要不会报错

                            for (SequenceFlow flow : outgoingFlows) {
                                FlowElement flowElement = flow.getTargetFlowElement();

                                if (!flowElement.getId().equals(notPassActId) && !flowElement.getId().startsWith("endEvent")) {
                                    taskFlowElementActIds.add(flowElement.getId());
                                } else {
                                    notPassActId = flowElement.getId();
                                }
                            }
                            //当事项为通过时，流向非办结节点，如果为不通过时，流向ID为“jieshu”的节点（前提是，需要有结束节点）
                            if (ItemStatus.AGREE.getValue().equals(aeaHiIteminst.getIteminstState())
                                    ||ItemStatus.AGREE_TOLERANCE.getValue().equals(aeaHiIteminst.getIteminstState())) {
                                taskService.complete(task.getId(), (String[]) taskFlowElementActIds.toArray(), null);
                            } else {
                                taskService.complete(task.getId(), new String[]{notPassActId}, null);
                            }
                        }
                    } else {
                        throw new RuntimeException("当前任务没有下个流向，请检查流程定义！");
                    }
                }

                //触发流程发送事件，用于窗口办结发送短信
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
