package com.augurit.aplanmis.dg_front.outerTaskEvent;


public interface IBaseOuterListener {
    public void setTaskIdToAeaHiApplyData(String applyinstCode, String iteminstId, String taskId) throws Exception;

    public void setTaskIdToAeaHiApplyData(String applyinstCode, String iteminstId, String taskId, String processInstanceId) throws Exception;
}
