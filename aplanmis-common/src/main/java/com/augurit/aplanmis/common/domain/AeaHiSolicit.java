package com.augurit.aplanmis.common.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 征求意见主表-模型
 */
public class AeaHiSolicit implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String solicitId; // (主键)
    private String solicitType; // (征求意见类型：i表示按事项征求，d表示按部门征求)
    private String solicitOrgId; // (按组织征求配置ID【当SOLICIT_TYPE=d】)
    private String solicitItemId; // (按事项征求配置ID【当SOLICIT_TYPE=i】)
    private String applyinstId; // (发起征求的申报ID)
    private String procinstId; // (发起征求的流程实例ID)
    private String hiTaskinstId; // (发起征求的历史任务实例ID)
    private String solicitCode; // (意见征求流水号)
    private String solicitTopic; // (意见征求主题)
    private String solicitContent; // (征求意见内容)
    @Size(max = 10)
    private Long solicitDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)
    @Size(max = 10)
    private Long solicitRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    private String solicitDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitStartTime; // (意见征求开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitEndTime; // (意见征求结束时间)
    private String isCalcTimerule; // (是否纳入计时)
    private String solicitTimeruleId; // (意见征求计算策略表ID)
    private String conclusionFlag; // (办结结论标志位，0表示不通过，1表示通过)
    private String conclusionDesc; // (办结结论描述)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date conclusionTime; // (填写结论时间)
    private String conclusionUserId; // (填写结论用户ID)
    private String conclusionUserName; // (填写结论用户姓名)
    private String initiatorOrgId; // (意见征求发起组织ID)
    private String initiatorOrgName; // (意见征求发起组织名称)
    private String initiatorUserId; // (意见征求发起用户ID)
    private String initiatorUserName; // (意见征求发起用户姓名)
    @Size(max = 10)
    private Long countLimit; // (办理次数限制，0表示无限制，1或以上表示限制次数)
    private String solicitState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)

    // ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getSolicitId() {
        return solicitId;
    }

    public void setSolicitId(String solicitId) {
        this.solicitId = solicitId == null ? null : solicitId.trim();
    }

    public String getSolicitType() {
        return solicitType;
    }

    public void setSolicitType(String solicitType) {
        this.solicitType = solicitType == null ? null : solicitType.trim();
    }

    public String getSolicitOrgId() {
        return solicitOrgId;
    }

    public void setSolicitOrgId(String solicitOrgId) {
        this.solicitOrgId = solicitOrgId == null ? null : solicitOrgId.trim();
    }

    public String getSolicitItemId() {
        return solicitItemId;
    }

    public void setSolicitItemId(String solicitItemId) {
        this.solicitItemId = solicitItemId == null ? null : solicitItemId.trim();
    }

    public String getApplyinstId() {
        return applyinstId;
    }

    public void setApplyinstId(String applyinstId) {
        this.applyinstId = applyinstId == null ? null : applyinstId.trim();
    }

    public String getProcinstId() {
        return procinstId;
    }

    public void setProcinstId(String procinstId) {
        this.procinstId = procinstId == null ? null : procinstId.trim();
    }

    public String getHiTaskinstId() {
        return hiTaskinstId;
    }

    public void setHiTaskinstId(String hiTaskinstId) {
        this.hiTaskinstId = hiTaskinstId == null ? null : hiTaskinstId.trim();
    }

    public String getSolicitCode() {
        return solicitCode;
    }

    public void setSolicitCode(String solicitCode) {
        this.solicitCode = solicitCode == null ? null : solicitCode.trim();
    }

    public String getSolicitTopic() {
        return solicitTopic;
    }

    public void setSolicitTopic(String solicitTopic) {
        this.solicitTopic = solicitTopic == null ? null : solicitTopic.trim();
    }

    public String getSolicitContent() {
        return solicitContent;
    }

    public void setSolicitContent(String solicitContent) {
        this.solicitContent = solicitContent == null ? null : solicitContent.trim();
    }

    public Long getSolicitDueDays() {
        return solicitDueDays;
    }

    public void setSolicitDueDays(Long solicitDueDays) {
        this.solicitDueDays = solicitDueDays;
    }

    public Long getSolicitRealDays() {
        return solicitRealDays;
    }

    public void setSolicitRealDays(Long solicitRealDays) {
        this.solicitRealDays = solicitRealDays;
    }

    public String getSolicitDaysUnit() {
        return solicitDaysUnit;
    }

    public void setSolicitDaysUnit(String solicitDaysUnit) {
        this.solicitDaysUnit = solicitDaysUnit == null ? null : solicitDaysUnit.trim();
    }

    public java.util.Date getSolicitStartTime() {
        return solicitStartTime;
    }

    public void setSolicitStartTime(java.util.Date solicitStartTime) {
        this.solicitStartTime = solicitStartTime;
    }

    public java.util.Date getSolicitEndTime() {
        return solicitEndTime;
    }

    public void setSolicitEndTime(java.util.Date solicitEndTime) {
        this.solicitEndTime = solicitEndTime;
    }

    public String getIsCalcTimerule() {
        return isCalcTimerule;
    }

    public void setIsCalcTimerule(String isCalcTimerule) {
        this.isCalcTimerule = isCalcTimerule == null ? null : isCalcTimerule.trim();
    }

    public String getSolicitTimeruleId() {
        return solicitTimeruleId;
    }

    public void setSolicitTimeruleId(String solicitTimeruleId) {
        this.solicitTimeruleId = solicitTimeruleId == null ? null : solicitTimeruleId.trim();
    }

    public String getConclusionFlag() {
        return conclusionFlag;
    }

    public void setConclusionFlag(String conclusionFlag) {
        this.conclusionFlag = conclusionFlag == null ? null : conclusionFlag.trim();
    }

    public String getConclusionDesc() {
        return conclusionDesc;
    }

    public void setConclusionDesc(String conclusionDesc) {
        this.conclusionDesc = conclusionDesc == null ? null : conclusionDesc.trim();
    }

    public java.util.Date getConclusionTime() {
        return conclusionTime;
    }

    public void setConclusionTime(java.util.Date conclusionTime) {
        this.conclusionTime = conclusionTime;
    }

    public String getConclusionUserId() {
        return conclusionUserId;
    }

    public void setConclusionUserId(String conclusionUserId) {
        this.conclusionUserId = conclusionUserId == null ? null : conclusionUserId.trim();
    }

    public String getConclusionUserName() {
        return conclusionUserName;
    }

    public void setConclusionUserName(String conclusionUserName) {
        this.conclusionUserName = conclusionUserName == null ? null : conclusionUserName.trim();
    }

    public String getInitiatorOrgId() {
        return initiatorOrgId;
    }

    public void setInitiatorOrgId(String initiatorOrgId) {
        this.initiatorOrgId = initiatorOrgId == null ? null : initiatorOrgId.trim();
    }

    public String getInitiatorOrgName() {
        return initiatorOrgName;
    }

    public void setInitiatorOrgName(String initiatorOrgName) {
        this.initiatorOrgName = initiatorOrgName == null ? null : initiatorOrgName.trim();
    }

    public String getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(String initiatorUserId) {
        this.initiatorUserId = initiatorUserId == null ? null : initiatorUserId.trim();
    }

    public String getInitiatorUserName() {
        return initiatorUserName;
    }

    public void setInitiatorUserName(String initiatorUserName) {
        this.initiatorUserName = initiatorUserName == null ? null : initiatorUserName.trim();
    }

    public Long getCountLimit() {
        return countLimit;
    }

    public void setCountLimit(Long countLimit) {
        this.countLimit = countLimit;
    }

    public String getSolicitState() {
        return solicitState;
    }

    public void setSolicitState(String solicitState) {
        this.solicitState = solicitState == null ? null : solicitState.trim();
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

    public String getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(String rootOrgId) {
        this.rootOrgId = rootOrgId == null ? null : rootOrgId.trim();
    }
    //public String getTableName()  {
    //    return "AeaHiSolicit";
    //}
}
