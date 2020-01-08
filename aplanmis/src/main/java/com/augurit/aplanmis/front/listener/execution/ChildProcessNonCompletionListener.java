/*
package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;

import java.util.ArrayList;
import java.util.List;


*/
/**
 * 部门子流程不通过“办结”节点直接“不通过”办结
 *//*

public class ChildProcessNonCompletionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {

        String procInstId = delegateExecution.getProcessInstanceId();

        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);
        AplanmisEventPublisher publisher = SpringContextHolder.getBean(AplanmisEventPublisher.class);
        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration().getRepositoryService();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
