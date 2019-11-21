//package com.augurit.agcloud.bpm.common.engine.filter.utils;
//
//import com.augurit.agcloud.bpm.common.constant.TrueFalseConstant;
//import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
//import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
//import com.augurit.agcloud.bpm.common.domain.ActTplAppFlowdef;
//import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
//import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
//import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
//import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
//import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
//import com.augurit.agcloud.bpm.common.mapper.ActTplAppFlowdefMapper;
//import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
//import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
//import com.augurit.agcloud.framework.security.SecurityContext;
//import com.augurit.agcloud.framework.util.StringUtils;
//import com.augurit.aplanmis.common.constants.DeletedStatus;
//import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
//import com.augurit.aplanmis.common.domain.AeaHiIteminst;
//import com.augurit.aplanmis.common.domain.AeaItemBasic;
//import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
//import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
//import com.augurit.aplanmis.common.mapper.AeaItemVerMapper;
//import com.augurit.aplanmis.common.service.instance.RestTimeruleinstService;
//import com.augurit.aplanmis.common.service.instance.RestTimeruleinstServiceImpl;
//import org.flowable.bpmn.model.*;
//import org.flowable.bpmn.model.Process;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.delegate.DelegateExecution;
//import org.flowable.engine.impl.util.CommandContextUtil;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.task.service.delegate.DelegateTask;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BpmSubFlowTriggerUtil {
//    /**
//     * 任务子流程触发方法
//     *
//     * @param delegateTask
//     * @param eventName
//     */
//    public static void triggerTaskSubFlows(DelegateTask delegateTask, String eventName) throws Exception {
//        ActTplAppTriggerMapper actTplAppTriggerMapper = SpringContextHolder.getBean(ActTplAppTriggerMapper.class);
//        ActTplAppFlowdefMapper actTplAppFlowdefMapper = SpringContextHolder.getBean(ActTplAppFlowdefMapper.class);
//        ActStoAppinstMapper actStoAppinstMapper = SpringContextHolder.getBean(ActStoAppinstMapper.class);
//        ActStoAppinstSubflowMapper actStoAppinstSubflowMapper = SpringContextHolder.getBean(ActStoAppinstSubflowMapper.class);
//        BpmProcessService bpmProcessService = SpringContextHolder.getBean(BpmProcessService.class);
//        RestTimeruleinstService restTimeruleinstService = (RestTimeruleinstServiceImpl)SpringContextHolder.getBean("restTimeruleinstServiceImpl");
//        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration().getRepositoryService();
//        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
//        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
//        AeaItemBasicMapper aeaItemBasicMapper = SpringContextHolder.getBean(AeaItemBasicMapper.class);
//        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
//
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(delegateTask.getProcessDefinitionId());
//        Process process = bpmnModel.getProcesses().get(0);
//        UserTask currUserTask = (UserTask) process.getFlowElement(delegateTask.getTaskDefinitionKey());
//
//        String appId = null;//模板ID
//        String appinstId = null;//模板实例ID
//        String parentSubflowId = null;//父子流程实例Id,当子流程再启动子流程时有值
//
//        String taskInstId = delegateTask.getId();
//        String procInstId = delegateTask.getProcessInstanceId();
//        String processDefinitionId = delegateTask.getProcessDefinitionId();
//        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
//        setJointReviewTaskId(runtimeService, taskInstId, procInstId, taskDefinitionKey);
//        Object masterForm = runtimeService.getVariable(procInstId, "form");
//
//        //-------------第一步，先获取当前流程的流程模板实例信息 start--------------
//        ActStoAppinst stoAppinst = new ActStoAppinst();
//        stoAppinst.setAppinstIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        stoAppinst.setProcinstId(delegateTask.getProcessInstanceId());
//        List<ActStoAppinst> parentAppinstList = actStoAppinstMapper.listActStoAppinst(stoAppinst);
//
//        //当前流程如果为子流程时
//        if (parentAppinstList == null || parentAppinstList.size() == 0) {
//            String parentTaskInstId = (String) runtimeService.getVariable(procInstId, "$SYS_DGZJ_PARENT_INST_ID");
//
//            ActStoAppinstSubflow appinstSubflow = new ActStoAppinstSubflow();
//            appinstSubflow.setTriggerTaskinstId(parentTaskInstId);
//            List<ActStoAppinstSubflow> appinstSubflowList = actStoAppinstSubflowMapper.listActStoAppinstSubflow(appinstSubflow);
//
//            //非第一级流程时，即子流程时
//            if (appinstSubflowList != null && appinstSubflowList.size() > 0) {
//                parentSubflowId = appinstSubflowList.get(0).getSubflowId();
//                appinstId = appinstSubflowList.get(0).getAppinstId();
//
//                ActStoAppinst currParentAppinst = actStoAppinstMapper.getActStoAppinstById(appinstId);
//                if (currParentAppinst == null) throw new RuntimeException("获取流程父流程模板实例失败！");
//                if (currParentAppinst != null)
//                    appId = currParentAppinst.getAppId();
//            }
//        } else {
//            //第一级流程时
//            appinstId = parentAppinstList.get(0).getAppinstId();
//            appId = parentAppinstList.get(0).getAppId();
//        }
//
//        if (StringUtils.isBlank(appinstId)) {
//            throw new RuntimeException("获取当前流程实例模板实例信息失败！");
//        }
//        if (StringUtils.isBlank(appId)) {
//            throw new RuntimeException("当前节点，无法获取到任务创建时启动子流程扫描的事件定义信息，请检查流程配置信息！");
//        }
//        //-------------第一步，先获取当前流程的流程模板实例信息 end----------------
//
//        //-------------第二步，解析当前流程定义的任务子流程扫描监听器信息 start----------------
//        if (currUserTask != null) {
//            //获取当前任务子流程扫描监听器
//            List<FlowableListener> taskListeners = currUserTask.getTaskListeners();
//            if (taskListeners != null && taskListeners.size() > 0) {
//                //循环扫描监听器
//                for (FlowableListener listener : taskListeners) {
//                    //匹配当前模板下是否配合有事件名称为【eventName】的子流程扫描，如果有才进行扫描
//                    if (listener.getSubFlow() /*&& listener.getAppIds().contains(appId) */ && eventName.equals(listener.getEvent())) {
//                        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
//                        if (processDefinition == null) {
//                            throw new RuntimeException("获取流程定义信息失败！");
//                        }
//
//                        ActTplAppFlowdef appFlowdefCondition = new ActTplAppFlowdef();
//                        appFlowdefCondition.setAppId(appId);
//                        appFlowdefCondition.setProcdefKey(processDefinition.getKey());
//                        List<ActTplAppFlowdef> appFlowdefList = actTplAppFlowdefMapper.listActTplAppFlowdef(appFlowdefCondition);
//
//                        if (appFlowdefList != null && appFlowdefList.size() > 0) {
//                            List<String> appFlowdefIds = new ArrayList<>();
//                            for (ActTplAppFlowdef appFlowdef : appFlowdefList) {
//                                appFlowdefIds.add(appFlowdef.getAppFlowdefId());
//                            }
//
//                            List<ActTplAppTrigger> appTriggerList = actTplAppTriggerMapper.getActTplAppTriggerByAppFlowDefIds(appFlowdefIds, appId);
//
//                            if (appTriggerList != null && appTriggerList.size() > 0) {
//                                for (ActTplAppTrigger appTrigger : appTriggerList) {
//                                    if (appId.equals(appTrigger.getTriggerAppId()) && eventName.equals(appTrigger.getTriggerEvent())
//                                            && taskDefinitionKey.equals(appTrigger.getTriggerElementId())) {
//
//                                        //----------------处理并联申报时，部分事项已单项办理时，系统自动跳过事项节点相关逻辑 start---------------------
//                                        AeaHiApplyinst applyinst = (AeaHiApplyinst) masterForm;
//                                        String projInfoId = applyinst.getProjInfoId();
//
//                                        if ("0".equals(applyinst.getIsSeriesApprove())) {
//                                            List<String> isDoneItemVerIdList = aeaItemBasicMapper.getItemVerIdListByProjInfoId(projInfoId);
//                                            if (appTrigger.getBusRecordId() != null && isDoneItemVerIdList.contains(appTrigger.getBusRecordId())) {//判定是否串联中申报过
//                                                taskService.addComment(taskInstId, procInstId, "【已办事项：该事项已进行单项申报】");
//                                                taskService.setVariable(taskInstId, "$BUS_SYS_AUTO_ITEMS_VER_ID", appTrigger.getBusRecordId());
//                                                taskService.complete(taskInstId);
//                                                return;
//                                            }
//                                        }
//                                        //----------------处理并联申报时，部分事项已单项办理时，系统自动跳过事项节点相关逻辑 end---------------------
//
//                                        String iteminstId = BpmSubFlowTriggerUtil.getIteminstId(appTrigger, procInstId, appinstId);
//
//                                        //当配置了事项子流程关联事项，如果无法找到与之匹配的申报事项实例，则跳过触发子流程
//                                        if((StringUtils.isNotBlank(appTrigger.getBusRecordId())&&iteminstId!=null)
//                                                ||StringUtils.isBlank(appTrigger.getBusRecordId())){
//                                            Map<String, Object> params = new HashMap<>();
//                                            params.put("$SYS_DGZJ_PARENT_INST_ID", taskInstId);
//                                            if (masterForm != null) {
//                                                params.put("form", masterForm);
//                                            }
//                                            params.put("$BRANCH_ORG_ITEMINST_ID", iteminstId);
//                                            BpmProcessInstance processInstance = bpmProcessService.startSubFlowProcessInstanceByTplAppFlowdefId(appTrigger.getTriggerAppFlowdefId(), appinstId, appTrigger.getTriggerId(), taskInstId, null, parentSubflowId, params, SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserName());
//                                            if (processInstance != null && StringUtils.isNotBlank(iteminstId)) {
//                                                //更新事项实例表关联的流程实例ID
//                                                AeaHiIteminst iteminst = new AeaHiIteminst();
//                                                iteminst.setIteminstId(iteminstId);
//                                                iteminst.setProcinstId(processInstance.getProcessInstance().getProcessInstanceId());
//                                                aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
//                                                runtimeService.setVariable(processInstance.getProcessInstance().getProcessInstanceId(), "$BUS_CURRENT_ITEMINST_ID", iteminstId);
//
//                                                //新增时限规则实例
////                                                restTimeruleinstService.createTimeruleinstByProcinst(appId, processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());
//                                            }
//                                        }
//
//                                        /*Map<String, Object> params = new HashMap<>();
//                                        params.put("$SYS_DGZJ_PARENT_INST_ID", taskInstId);
//                                        if (masterForm != null) {
//                                            params.put("form", masterForm);
//                                        }
//                                        params.put("$BRANCH_ORG_ITEMINST_ID", iteminstId);
//                                        BpmProcessInstance processInstance = bpmProcessService.startSubFlowProcessInstanceByTplAppFlowdefId(appTrigger.getTriggerAppFlowdefId(), appinstId, appTrigger.getTriggerId(), taskInstId, null, parentSubflowId, params, SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserName());
//                                        if (processInstance != null && StringUtils.isNotBlank(iteminstId)) {
//                                            //更新事项实例表关联的流程实例ID
//                                            AeaHiIteminst iteminst = new AeaHiIteminst();
//                                            iteminst.setIteminstId(iteminstId);
//                                            iteminst.setProcinstId(processInstance.getProcessInstance().getProcessInstanceId());
//                                            aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
//                                            runtimeService.setVariable(processInstance.getProcessInstance().getProcessInstanceId(), "$BUS_CURRENT_ITEMINST_ID", iteminstId);
//                                        }
//                                        //新增时限规则实例
////                                        restTimeruleinstService.createTimeruleinstByProcinst(appId, processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());
//*/
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        //-------------第二步，解析当前流程定义的任务子流程扫描监听器信息 end----------------
//    }
//
//    /**
//     * 设置联合审查阶段的taskId
//     *
//     * @param runtimeService
//     * @param taskInstId
//     * @param procInstId
//     * @param taskDefinitionKey
//     */
//    private static void setJointReviewTaskId(RuntimeService runtimeService, String taskInstId, String procInstId, String taskDefinitionKey) {
//        if ("joint_review_task".equals(taskDefinitionKey)) {
//            runtimeService.setVariable(procInstId, "$JOINT_REVIEW_TASK_ID", taskInstId);
//        }
//    }
//
//    /**
//     * 节点子流程触发方法
//     *
//     * @param delegateExecution
//     * @param eventName         事件类型
//     */
//    public static void triggerExecutionSubFlows(DelegateExecution delegateExecution, String eventName) throws Exception {
//        ActTplAppTriggerMapper actTplAppTriggerMapper = SpringContextHolder.getBean(ActTplAppTriggerMapper.class);
//        ActTplAppFlowdefMapper actTplAppFlowdefMapper = SpringContextHolder.getBean(ActTplAppFlowdefMapper.class);
//        ActStoAppinstMapper actStoAppinstMapper = SpringContextHolder.getBean(ActStoAppinstMapper.class);
//        BpmProcessService bpmProcessService = SpringContextHolder.getBean(BpmProcessService.class);
//        ActStoAppinstSubflowMapper actStoAppinstSubflowMapper = SpringContextHolder.getBean(ActStoAppinstSubflowMapper.class);
//        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration().getRepositoryService();
//        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
//        RestTimeruleinstService restTimeruleinstService = SpringContextHolder.getBean(RestTimeruleinstService.class);
//        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
//
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(delegateExecution.getProcessDefinitionId());
//        Process process = bpmnModel.getProcesses().get(0);
//        FlowElement currFlowElement = process.getFlowElement(delegateExecution.getCurrentActivityId());
//        //模板ID
//        String appId = null;
//        //模板实例ID
//        String appinstId = null;
//        //父子流程实例Id,当子流程再启动子流程时有值
//        String parentSubflowId = null;
//
//        String procInstId = delegateExecution.getProcessInstanceId();
//        String processDefinitionId = delegateExecution.getProcessDefinitionId();
//        String activityId = delegateExecution.getCurrentActivityId();
//        String currElementInstId = delegateExecution.getId();
//
//        //-------------第一步，先获取当前流程的流程模板实例信息 start--------------
//        ActStoAppinst stoAppinst = new ActStoAppinst();
//        stoAppinst.setAppId(appId);
//        stoAppinst.setAppinstIsDeleted(TrueFalseConstant.FALSE_STRING);
//        stoAppinst.setProcinstId(delegateExecution.getProcessInstanceId());
//        List<ActStoAppinst> parentAppinstList = actStoAppinstMapper.listActStoAppinst(stoAppinst);
//
//        //当前流程如果为子流程时
//        if (parentAppinstList == null || parentAppinstList.size() == 0) {
//            String parentElementInstId = (String) runtimeService.getVariable(procInstId, "$SYS_DGZJ_PARENT_INST_ID");
//
//            ActStoAppinstSubflow appinstSubflow = new ActStoAppinstSubflow();
//            appinstSubflow.setTriggerElementInstId(parentElementInstId);
//            List<ActStoAppinstSubflow> appinstSubflowList = actStoAppinstSubflowMapper.listActStoAppinstSubflow(appinstSubflow);
//
//            //非第一级流程时，即子流程时
//            if (appinstSubflowList != null && appinstSubflowList.size() > 0) {
//                parentSubflowId = appinstSubflowList.get(0).getSubflowId();
//                appinstId = appinstSubflowList.get(0).getAppinstId();
//
//                ActStoAppinst currParentAppinst = actStoAppinstMapper.getActStoAppinstById(appinstId);
//                if (currParentAppinst == null) throw new RuntimeException("获取流程父流程模板实例失败！");
//                if (currParentAppinst != null)
//                    appId = currParentAppinst.getAppId();
//            }
//        } else {
//            //第一级流程时
//            appinstId = parentAppinstList.get(0).getAppinstId();
//            appId = parentAppinstList.get(0).getAppId();
//        }
//        if (StringUtils.isBlank(appId)) {
//            throw new RuntimeException("当前节点，无法获取到任务创建时启动子流程扫描的事件定义信息，请检查流程配置信息！");
//        }
//        if (StringUtils.isBlank(appinstId)) {
//            throw new RuntimeException("获取当前流程实例模板实例信息失败！");
//        }
//        //-------------第一步，先获取当前流程的流程模板实例信息 end--------------
//
//        //-------------第二步，解析当前流程定义的任务子流程扫描监听器信息 start----------------
//        if (currFlowElement != null) {
//            //获取当前节点监听器
//            List<FlowableListener> executionListeners = currFlowElement.getExecutionListeners();
//            if (executionListeners != null && executionListeners.size() > 0) {
//                //循环扫描监听器
//                for (FlowableListener listener : executionListeners) {
//                    //匹配当前模板下是否配合有事件名称为【eventName】的子流程扫描，如果有才进行扫描
//                    if (listener.getSubFlow() /*&& listener.getAppIds().contains(appId)*/ && eventName.equals(listener.getEvent())) {
//                        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
//                        if (processDefinition == null) {
//                            throw new RuntimeException("获取流程定义信息失败！");
//                        }
//
//                        ActTplAppFlowdef appFlowdefCondition = new ActTplAppFlowdef();
//                        appFlowdefCondition.setAppId(appId);
//                        appFlowdefCondition.setProcdefKey(processDefinition.getKey());
//                        List<ActTplAppFlowdef> appFlowdefList = actTplAppFlowdefMapper.listActTplAppFlowdef(appFlowdefCondition);
//
//                        if (appFlowdefList != null && appFlowdefList.size() > 0) {
//                            List<String> appFlowdefIds = new ArrayList<>();
//                            for (ActTplAppFlowdef appFlowdef : appFlowdefList) {
//                                appFlowdefIds.add(appFlowdef.getAppFlowdefId());
//                            }
//
//                            List<ActTplAppTrigger> appTriggerList = actTplAppTriggerMapper.getActTplAppTriggerByAppFlowDefIds(appFlowdefIds, appId);
//
//                            if (appTriggerList != null && appTriggerList.size() > 0) {
//                                for (ActTplAppTrigger appTrigger : appTriggerList) {
//                                    //if("1".equals(appTrigger.getIsOuterFlow())){
//                                    if (appId.equals(appTrigger.getTriggerAppId()) && eventName.equals(appTrigger.getTriggerEvent()) && activityId.equals(appTrigger.getTriggerElementId())) {
//
//                                        String iteminstId = BpmSubFlowTriggerUtil.getIteminstId(appTrigger, procInstId, appinstId);
//
//                                        Map<String, Object> params = new HashMap<>();
//                                        params.put("$SYS_DGZJ_PARENT_INST_ID", currElementInstId);
//                                        BpmProcessInstance processInstance = bpmProcessService.startSubFlowProcessInstanceByTplAppFlowdefId(appTrigger.getTriggerAppFlowdefId(), appinstId, appTrigger.getTriggerId(), null, currElementInstId, parentSubflowId, null, SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserName());
//                                        if (processInstance != null && iteminstId != null) {
//                                            //更新事项实例表关联的流程实例ID
//                                            AeaHiIteminst iteminst = new AeaHiIteminst();
//                                            iteminst.setIteminstId(iteminstId);
//                                            iteminst.setProcinstId(processInstance.getProcessInstance().getProcessInstanceId());
//                                            aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
//                                            runtimeService.setVariable(processInstance.getProcessInstance().getProcessInstanceId(), "$BUS_CURRENT_ITEMINST_ID", iteminstId);
//                                            //新增时限规则实例
////                                            restTimeruleinstService.createTimeruleinstByProcinst(appId, processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());
//
//                                        }
//                                    }
//                                    //}
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        //-------------第二步，解析当前流程定义的任务子流程扫描监听器信息 end----------------
//    }
//
//    private static String getIteminstId(ActTplAppTrigger appTrigger, String procInstId, String appinstId) throws Exception {
//        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
//        ActTplAppTriggerMapper actTplAppTriggerMapper = SpringContextHolder.getBean(ActTplAppTriggerMapper.class);
//        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
//        AeaItemVerMapper aeaItemVerMapper = SpringContextHolder.getBean(AeaItemVerMapper.class);
//        AeaItemBasicMapper aeaItemBasicMapper = SpringContextHolder.getBean(AeaItemBasicMapper.class);
//
//        String triggerId = appTrigger.getTriggerId();
//        ActTplAppTrigger currTrigger = actTplAppTriggerMapper.getActTplAppTriggerById(triggerId);
//
//        String iteminstId = null;
////        AeaHiIteminst currTaskIteminst = null;
//        AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");
//        if (currTrigger != null && StringUtils.isNotBlank(currTrigger.getBusRecordId())) {
//
//            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(currTrigger.getBusRecordId(),SecurityContext.getCurrentOrgId());
//            if(aeaItemBasic==null)
//                throw new RuntimeException("无法找到事项版本好为："+currTrigger.getBusRecordId()+"的事项信息，请检查子流程配置或确认事项是否被删除！");
//
//            //先查询出所有的iteminst
//            if (null != aeaHiApplyinst) {
//                List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
//                if(aeaHiIteminsts!=null&&aeaHiIteminsts.size()>0){
//                    for(AeaHiIteminst aeaHiIteminst:aeaHiIteminsts){
//                        if(aeaHiIteminst.getItemId().equals(aeaItemBasic.getItemId())){
//                            iteminstId = aeaHiIteminst.getIteminstId();
//                        }
//                    }
//                }
//            }
//
//
//            /*Map<String,AeaHiIteminst> iteminstMap = new HashMap<>();
//            Map<String,String> itemVerMap = new HashMap<>();
//
//            //先查询出所有的iteminst
//            if (null != aeaHiApplyinst) {
//                List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
//                if (aeaHiIteminsts.size() > 0) {
//                    List<String> itemIds = new ArrayList<>();
//                    for(AeaHiIteminst iteminst:aeaHiIteminsts){
//                        itemIds.add(iteminst.getItemId());
//                        iteminstMap.put(iteminst.getItemId(),iteminst);
//                    }
//                    List<AeaItemVer> itemVers = aeaItemVerMapper.getAllShiShiItemVerIdsByParentItemIds(itemIds);
//                    List<AeaItemVer> currItemVerList = aeaItemVerMapper.getAeaItemVersByItemIds(itemIds);//
//
//                    if(currItemVerList!=null&&currItemVerList.size()>0)
//                        itemVers.addAll(currItemVerList);
//
//                    if(itemVers!=null&&itemVers.size()>0){
//                        for(AeaItemVer itemVer:itemVers){
//                            itemVerMap.put(itemVer.getItemVerId(),itemVer.getItemId());
//                        }
//                    }
//                }
//            }
//
//            String triggerItemId = itemVerMap.get(currTrigger.getBusRecordId());
//
//            //当前事项实例
//            AeaHiIteminst currIteminst = null;
//
//            //如果trigger配置的是实施事项版本ID时，直接可以在事项实例里面找到当前触发的事项实例
//            currIteminst = iteminstMap.get(triggerItemId);
//
//            //如果trigger配置的是标准事项版本ID时，需要遍历查找对应的实施事项实例
//            if(currIteminst==null) {
//                List<String> shishiItemIds = aeaItemVerMapper.getShiShiItemByBiaozhunItemId(triggerItemId);
//
//                if (shishiItemIds != null && shishiItemIds.size() > 0) {
//                    for (String shishiItemId : shishiItemIds) {
//                        AeaHiIteminst hiIteminst = iteminstMap.get(shishiItemId);
//                        if (hiIteminst != null) {
//                            currIteminst = hiIteminst;
//                            break;
//                        }
//                    }
//                }
//            }
//
//            //当能找到事项实例ID时，才返回，否则报错
//            if (currIteminst!=null) {
//                iteminstId = currIteminst.getIteminstId();
//                currTaskIteminst = currIteminst;
//                //更新事项实例的子流程审批appinst id
//                currTaskIteminst.setInnerAppinstId(appinstId);
//                aeaHiIteminstMapper.updateAeaHiIteminst(currTaskIteminst);
//            } else {
//                throw new RuntimeException("获取事项实例失败，请检查！");
//            }*/
//        }
//        return iteminstId;
//    }
//}
