package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 效能督查列表对象
 */
public class EffectInspectForm implements Serializable {
    private String taskId;
    private String applyinstCode;
    private String projName;
    private String applyinstSource;
    private String linkMan;
    private String isSeriesApprove;
    private String taskName;
    private String iteminstStageName;
    private Date applyinstTime;
    private String applyinstTimeStr;
    private String approveUserName;
    private String procInstId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApplyinstCode() {
        return applyinstCode;
    }

    public void setApplyinstCode(String applyinstCode) {
        this.applyinstCode = applyinstCode;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getApplyinstSource() {
        return applyinstSource;
    }

    public void setApplyinstSource(String applyinstSource) {
        this.applyinstSource = applyinstSource;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getIsSeriesApprove() {
        return isSeriesApprove;
    }

    public void setIsSeriesApprove(String isSeriesApprove) {
        this.isSeriesApprove = isSeriesApprove;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getIteminstStageName() {
        return iteminstStageName;
    }

    public void setIteminstStageName(String iteminstStageName) {
        this.iteminstStageName = iteminstStageName;
    }

    public Date getApplyinstTime() {
        return applyinstTime;
    }

    public void setApplyinstTime(Date applyinstTime) {
        this.applyinstTime = applyinstTime;
    }

    public String getApplyinstTimeStr() {
        return applyinstTimeStr;
    }

    public void setApplyinstTimeStr(String applyinstTimeStr) {
        this.applyinstTimeStr = applyinstTimeStr;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
}
