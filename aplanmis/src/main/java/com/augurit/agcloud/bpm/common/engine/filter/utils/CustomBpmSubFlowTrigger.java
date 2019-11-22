package com.augurit.agcloud.bpm.common.engine.filter.utils;

import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 自定义业务逻辑类，实现子流程触发的接口
 * 在子流程触发前后，做相应的业务逻辑。
 */
@Component
public class CustomBpmSubFlowTrigger implements BpmSubFlowTrigger {

    /**
     * 子流程触发前 自定义业务逻辑执行接口
     * @param masterForm    业务主表单对象
     * @param delegateTask  当前要触发子流程的任务节点对象
     * @param params        启动子流程的初始化参数对象
     * @param appinstId     流程模板实例id
     * @param appTrigger    子流程配置对象
     * @return 返回true 则继续执行 触发子流程逻辑，false则结束触发子流程逻辑
     */
    @Override
    public boolean doBeforeTaskTrigger(Object masterForm, DelegateTask delegateTask,Map<String,Object> params, String appinstId, ActTplAppTrigger appTrigger) {
//        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
//        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration().getTaskService();
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
        AeaItemBasicMapper aeaItemBasicMapper = SpringContextHolder.getBean(AeaItemBasicMapper.class);

        setJointReviewTaskId(runtimeService, delegateTask.getId(), delegateTask.getProcessInstanceId(), delegateTask.getTaskDefinitionKey());

        //----------------处理并联申报时，部分事项已单项办理时，系统自动跳过事项节点相关逻辑 start---------------------
        AeaHiApplyinst applyinst = (AeaHiApplyinst) masterForm;
        String projInfoId = applyinst.getProjInfoId();

        if ("0".equals(applyinst.getIsSeriesApprove())) {
            List<String> isDoneItemVerIdList = aeaItemBasicMapper.getItemVerIdListByProjInfoId(projInfoId);
            if (appTrigger.getBusRecordId() != null && isDoneItemVerIdList.contains(appTrigger.getBusRecordId())) {//判定是否串联中申报过
                taskService.addComment(delegateTask.getId(), delegateTask.getProcessInstanceId(), "【已办事项：该事项已进行单项申报】");
                taskService.setVariable(delegateTask.getId(), "$BUS_SYS_AUTO_ITEMS_VER_ID", appTrigger.getBusRecordId());
                taskService.complete(delegateTask.getId());
                return false;
            }
        }
        //----------------处理并联申报时，部分事项已单项办理时，系统自动跳过事项节点相关逻辑 end---------------------
        try {
            String iteminstId = getIteminstId(appTrigger, delegateTask.getProcessInstanceId(), appinstId);
            params.put("$BRANCH_ORG_ITEMINST_ID", iteminstId);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    /**
     * 子流程触发后 自定义业务逻辑执行接口
     * @param masterForm        业务主表单对象
     * @param delegateTask      当前要触发子流程的任务节点对象
     * @param appinstId         流程模板实例id
     * @param appTrigger        子流程配置对象
     * @param processInstance   子流程实例对象
     */
    @Override
    public void doAfterTaskTrigger(Object masterForm, DelegateTask delegateTask, String appinstId,ActTplAppTrigger appTrigger, BpmProcessInstance processInstance) {
        try {
//            RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
            RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
            AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
            String iteminstId = getIteminstId(appTrigger, delegateTask.getProcessInstanceId(), appinstId);
            if (processInstance != null && StringUtils.isNotBlank(iteminstId)) {
                //更新事项实例表关联的流程实例ID
                AeaHiIteminst iteminst = new AeaHiIteminst();
                iteminst.setIteminstId(iteminstId);
                iteminst.setProcinstId(processInstance.getProcessInstance().getProcessInstanceId());
                aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
                runtimeService.setVariable(processInstance.getProcessInstance().getProcessInstanceId(), "$BUS_CURRENT_ITEMINST_ID", iteminstId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doBeforeExcutionTrigger(Object masterForm, DelegateExecution delegateExecution, Map<String, Object> params, String appinstId, ActTplAppTrigger appTrigger) {
        return false;
    }

    @Override
    public void doAfterExcutionTrigger(Object masterForm, DelegateExecution delegateExecution, String appinstId, ActTplAppTrigger appTrigger, BpmProcessInstance processInstance) {

    }


    /**
     * 设置联合审查阶段的taskId
     *
     * @param runtimeService
     * @param taskInstId
     * @param procInstId
     * @param taskDefinitionKey
     */
    private void setJointReviewTaskId(RuntimeService runtimeService, String taskInstId, String procInstId, String taskDefinitionKey) {
        if ("joint_review_task".equals(taskDefinitionKey)) {
            runtimeService.setVariable(procInstId, "$JOINT_REVIEW_TASK_ID", taskInstId);
        }
    }


    private String getIteminstId(ActTplAppTrigger appTrigger, String procInstId, String appinstId) throws Exception {
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);
        ActTplAppTriggerMapper actTplAppTriggerMapper = SpringContextHolder.getBean(ActTplAppTriggerMapper.class);
//        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
        AeaItemBasicMapper aeaItemBasicMapper = SpringContextHolder.getBean(AeaItemBasicMapper.class);

        String triggerId = appTrigger.getTriggerId();
        ActTplAppTrigger currTrigger = actTplAppTriggerMapper.getActTplAppTriggerById(triggerId);

        String iteminstId = null;
        AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");
        if (currTrigger != null && StringUtils.isNotBlank(currTrigger.getBusRecordId())) {

            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(currTrigger.getBusRecordId(),SecurityContext.getCurrentOrgId());
            if(aeaItemBasic==null)
                throw new RuntimeException("无法找到事项版本好为："+currTrigger.getBusRecordId()+"的事项信息，请检查子流程配置或确认事项是否被删除！");

            //先查询出所有的iteminst
            if (null != aeaHiApplyinst) {
                List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
                if(aeaHiIteminsts!=null&&aeaHiIteminsts.size()>0){
                    for(AeaHiIteminst aeaHiIteminst:aeaHiIteminsts){
                        if(aeaHiIteminst.getItemId().equals(aeaItemBasic.getItemId())){
                            iteminstId = aeaHiIteminst.getIteminstId();
                        }
                    }
                }
            }
        }
        return iteminstId;
    }
}
