package com.augurit.aplanmis.common.service.process.impl;

import com.augurit.agcloud.bpm.common.constant.TaskSendMsgConstant;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.service.ActTplAppFlowdefService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AeaBpmProcessServiceImpl implements AeaBpmProcessService {
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private ActTplAppFlowdefService actTplAppFlowdefService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 根据流程模板ID和申请实例，启动流程
     * @param appId 流程模板ID 必须
     * @param appinstId 模板实例ID 必须
     * @param aeaHiApplyinst 申请实例对象 必须
     * @return
     * @throws Exception
     */
    @Override
    public BpmProcessInstance startFlow(String appId,String appinstId, AeaHiApplyinst aeaHiApplyinst) throws Exception {
        if (StringUtils.isBlank(appId)) {
            throw new InvalidParameterException(appId);
        }
        if (aeaHiApplyinst == null)
            throw new InvalidParameterException("申请实例对象为空：{}",aeaHiApplyinst);

        Map<String,Object> variables = new HashMap<>();
        variables.put("form",aeaHiApplyinst);

        //暂时忽略了概要标题、概要说明和其他概要信息的获取

        BpmProcessInstance bpmProcessInstance = bpmProcessService.startMasterProcessInstanceByTplAppId(appId,aeaHiApplyinst.getApplyinstId(),variables,
                SecurityContext.getCurrentOrgId(),SecurityContext.getCurrentUserName(),null,null,null ,appinstId);
        return bpmProcessInstance;
    }

    /**
     * 根据流程当前节点，获取下一个节点信息，并动态设置下一个节点的办理人
     * @param currentTask 当前节点对象
     * @param assignees 办理人列表
     */
    @Override
    public void fillNextTaskAssignee(Task currentTask, List<String> assignees) {
        if (currentTask != null) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);

            //获取当前任务定义信息
            UserTask currTaskElement = (UserTask) process.getFlowElement(currentTask.getTaskDefinitionKey());

            List<SequenceFlow> outGoingFlowList = currTaskElement.getOutgoingFlows();
            if (outGoingFlowList != null && outGoingFlowList.size() > 0) {
                for (SequenceFlow flow : outGoingFlowList) {
                    SequenceFlow sequenceFlow = flow;
                    FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                    //人工节点
                    if (flowElement instanceof UserTask) {
                        UserTask userTask = (UserTask) flowElement;
                        String destActId = userTask.getId();
                        //是否多工作项
                        if(userTask.getEnableMultitask()){//多工作项
                            if (assignees != null && assignees.size() > 0) {
                                Map<String, Object> vars = new HashMap<>();
                                vars.put(UserTaskActivityBehavior.MULTI_TASK_ASSIGNEES_VAR_NAME_PREFIX + destActId, assignees);
                                runtimeService.setVariables(currentTask.getProcessInstanceId(), vars);
                            } else {
                                throw new RuntimeException(TaskSendMsgConstant.USER_TASK_MULTI_TASK);
                            }
                        }else{
                            //非多工作项
                            if (assignees != null && assignees.size() == 1) {
                                if(assignees.get(0) != null && !"".equals(assignees.get(0).trim()))
                                    runtimeService.setVariableLocal(currentTask.getProcessInstanceId(),
                                            UserTaskActivityBehavior.TASK_ASSIGNEE_VAR_NAME_PREFIX + destActId, assignees.get(0));
                            } else {
                                int count = 0;
                                if (assignees != null) {
                                    count = assignees.size();
                                }
                                throw new RuntimeException(TaskSendMsgConstant.USER_TASK_SIGLE_ASSIGNEE_ERROR + ":" + count);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public BpmProcessInstance startFlow(String appId, String appinstId, AeaHiApplyinst aeaHiApplyinst, String userName) throws Exception {
        if (StringUtils.isBlank(appId)) {
            throw new InvalidParameterException(appId);
        }
        if (aeaHiApplyinst == null)
            throw new InvalidParameterException("申请实例对象为空：{}", aeaHiApplyinst);

        Map<String, Object> variables = new HashMap<>();
        variables.put("form", aeaHiApplyinst);

        //暂时忽略了概要标题、概要说明和其他概要信息的获取

        BpmProcessInstance bpmProcessInstance = bpmProcessService.startMasterProcessInstanceByTplAppId(appId, aeaHiApplyinst.getApplyinstId(), variables,
                SecurityContext.getCurrentOrgId(), userName, null, null, null, appinstId);
        return bpmProcessInstance;
    }
}
