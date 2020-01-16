package com.augurit.aplanmis.dg_front.outerTaskEvent;


/**
 * 自定义外部流程只需继承该基础类，类前使用Component注解即可。
 * 例如：@Component("com.augurit.aplanmis.dg_front.approve.outerTaskEvent.OuterProcessPlanningBureauTriggerListener")
 */
public class BaseOuterListener implements IBaseOuterListener {


    /**
     * 回填AEA_HI_APPLY_DATA表中的taskId
     *
     * @param sblsh
     * @param iteminstId
     * @param taskId
     * @throws Exception
     */
    @Override
    public void setTaskIdToAeaHiApplyData(String sblsh, String iteminstId, String taskId) throws Exception {

    }

    @Override
    public void setTaskIdToAeaHiApplyData(String applyinstCode, String iteminstId, String taskId, String processInstanceId) throws Exception {

    }
}
