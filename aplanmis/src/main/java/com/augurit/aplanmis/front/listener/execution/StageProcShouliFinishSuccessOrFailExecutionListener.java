package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.front.apply.service.AeaParStageService;
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
public class StageProcShouliFinishSuccessOrFailExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        AeaHiParStageinstMapper aeaHiParStageinstMapper = SpringContextHolder.getBean(AeaHiParStageinstMapper.class);
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        AeaParStageMapper aeaParStageMapper = SpringContextHolder.getBean(AeaParStageMapper.class);
        AplanmisEventPublisher publisher = SpringContextHolder.getBean(AplanmisEventPublisher.class);
        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
        ActStoAppinstService actStoAppinstService = SpringContextHolder.getBean(ActStoAppinstService.class);

        String procInstId = delegateExecution.getProcessInstanceId();

        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);

        ActStoAppinstSubflow actStoAppinstSubflow = null;
        try {
            actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            //=======根据事项受理情况，维护主流程事项变量，当不予受理或者不受理的事项，则在流程变量里移除该事项的记录，然后在部门审批时，不需要进不受理或者不予受理的审批事项 start====
            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(actStoAppinstSubflow.getAppinstId());
            String masterProcinstId = actStoAppinst.getProcinstId();
            AeaHiApplyinst masterApplyinst = (AeaHiApplyinst) runtimeService.getVariable(masterProcinstId, "form");
            Map<String, Boolean> iteminsts = masterApplyinst.getIteminsts();
            //=======根据事项受理情况，维护主流程事项变量，当不予受理或者不受理的事项，则在流程变量里移除该事项的记录，然后在部门审批时，不需要进不受理或者不予受理的审批事项 end======

            AeaHiParStageinst parStageinst = new AeaHiParStageinst();
            parStageinst.setApplyinstId(aeaHiApplyinst.getApplyinstId());
            parStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiParStageinst> stageinstList = aeaHiParStageinstMapper.listAeaHiParStageinst(parStageinst);
            if(stageinstList!=null&&stageinstList.size()>0){
                AeaHiParStageinst currParStage = stageinstList.get(0);

                AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(currParStage.getStageId());
                String stageNum = aeaParStage.getDybzspjdxh();//阶段号，对应1、2、3、4阶段

                AeaHiIteminst hiIteminst = new AeaHiIteminst();
                hiIteminst.setStageinstId(currParStage.getStageinstId());
                hiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaHiIteminst> hiIteminstList = aeaHiIteminstMapper.listAeaHiIteminst(hiIteminst);

                boolean passFlag = false;
                int itemAcceptCount = 0;//事项受理数
                if(hiIteminstList!=null&&hiIteminstList.size()>0){
                    for(AeaHiIteminst iteminst:hiIteminstList){
                        //办结（通过）、办结（容缺通过）
                        if(ItemStatus.DEPARTMENT_DEAL_START.getValue().equals(iteminst.getIteminstState())){
                            passFlag = true;
                            itemAcceptCount++;
                        }

                        //不予受理、不受理的事项，不需要进入事项部门审批
                        if(iteminsts!=null&&iteminsts.size()>0&&(ItemStatus.REFUSE_DEAL.getValue().equals(iteminst.getIteminstState())
                                ||ItemStatus.OUT_SCOPE.getValue().equals(iteminst.getIteminstState()))){
                            iteminsts.remove(iteminst.getItemCategoryMark());
                        }
                    }

                    //当为4阶段的时候，事项受理一否权否
                    if("4".equals(stageNum)
                            &&itemAcceptCount!=hiIteminstList.size()){
                        passFlag = false;
                    }

                    //重新维护好主流程主变量
                    masterApplyinst.setIteminsts(iteminsts);
                    runtimeService.setVariable(masterProcinstId, "form",masterApplyinst);
                }

                //当所有事项有一个通过即流向综窗确认节点，否则流向结束节点
                if(passFlag){
                    String parentTaskId = actStoAppinstSubflow.getTriggerTaskinstId();
                    if (StringUtils.isNotBlank(parentTaskId)) {
                        Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
                        if (task != null) {
                            taskService.complete(parentTaskId, new String[]{"zongchuangqueren"}, (Map) null);

                            //触发流程发送事件，用于综窗确认发送短信
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
                                    bpmTaskSendConfig.setDestActId("zongchuangqueren");
                                    bpmTaskSendConfig.setUserTask(true);
                                    bpmTaskSendConfig.setAssignees(assigneeStr);
                                    List<BpmTaskSendConfig> list = new ArrayList<>();
                                    list.add(bpmTaskSendConfig);
                                    sendObject.setSendConfigs(list);
                                    publisher.publishEvent(new BpmNodeSendAplanmisEvent(this, sendObject));
                                }
                            }
                        }
                    }
                }else{
                    String parentTaskId = actStoAppinstSubflow.getTriggerTaskinstId();
                    if (StringUtils.isNotBlank(parentTaskId)) {
                        Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
                        if (task != null) {
                            taskService.complete(parentTaskId, new String[]{"jieshu"}, (Map) null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
