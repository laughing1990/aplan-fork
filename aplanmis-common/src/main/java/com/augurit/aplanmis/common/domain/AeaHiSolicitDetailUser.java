package com.augurit.aplanmis.common.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 征求意见详情用户任务表-模型
 */
public class AeaHiSolicitDetailUser implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String detailTaskId; // (主键)
    private String solicitDetailId; // (征求意见详情ID)
    private String userId; // (用户ID)
    private String userConclusion; // (办理结论，0表示不通过或不同意，1表示通过或同意，2表示不涉及)
    private String userOpinion; // (填写意见)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime; // (签收时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillTime; // (填写意见时间)
    private String taskAction; // (任务动作，0表示正常办理，1表示转交给同一委办局的其他人员，2表示添加同一委办局的其他人员进来)
    private String parentId; // (父ID【当TASK_ACTION=1或2时必填】)
    private String isDeleted; // (是否删除：0表示未删除；1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建日期)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (更新时间)

    // ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getDetailTaskId() {
        return detailTaskId;
    }

    public void setDetailTaskId(String detailTaskId) {
        this.detailTaskId = detailTaskId == null ? null : detailTaskId.trim();
    }

    public String getSolicitDetailId() {
        return solicitDetailId;
    }

    public void setSolicitDetailId(String solicitDetailId) {
        this.solicitDetailId = solicitDetailId == null ? null : solicitDetailId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserConclusion() {
        return userConclusion;
    }

    public void setUserConclusion(String userConclusion) {
        this.userConclusion = userConclusion == null ? null : userConclusion.trim();
    }

    public String getUserOpinion() {
        return userOpinion;
    }

    public void setUserOpinion(String userOpinion) {
        this.userOpinion = userOpinion == null ? null : userOpinion.trim();
    }

    public java.util.Date getSignTime() {
        return signTime;
    }

    public void setSignTime(java.util.Date signTime) {
        this.signTime = signTime;
    }

    public java.util.Date getFillTime() {
        return fillTime;
    }

    public void setFillTime(java.util.Date fillTime) {
        this.fillTime = fillTime;
    }

    public String getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(String taskAction) {
        this.taskAction = taskAction == null ? null : taskAction.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public java.util.Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    //public String getTableName()  {
    //    return "AeaHiSolicitDetailUser";
    //}
}
