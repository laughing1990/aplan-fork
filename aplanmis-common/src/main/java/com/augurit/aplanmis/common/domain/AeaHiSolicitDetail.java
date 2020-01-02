package com.augurit.aplanmis.common.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 征求意见详情表-模型，注意：如果增删改字段要同步修改SolicitDetailVo值对象类
 */
public class AeaHiSolicitDetail implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String solicitDetailId; // (主键)
    private String solicitId; // (征求意见主表ID)
    private String detailOrgId; // (征求意见部门ID)
    private String detailOrgName; // (征求意见部门名称)
    private String itemId; // (事项ID【当SOLICIT_TYPE=i】)
    private String itemVerId; // (事项版本ID【当SOLICIT_TYPE=i】)
    @Size(max = 10)
    private Long detailDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)
    @Size(max = 10)
    private Long detailRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    private String detailDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailStartTime; // (意见征求开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailEndTime; // (意见征求结束时间)
    private String detailState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)
    private String isDeleted; // (是否删除：0表示未删除；1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建日期)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (更新时间)

    private String itemName;//非表字段，事项名称
    // ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getSolicitDetailId() {
        return solicitDetailId;
    }

    public void setSolicitDetailId(String solicitDetailId) {
        this.solicitDetailId = solicitDetailId == null ? null : solicitDetailId.trim();
    }

    public String getSolicitId() {
        return solicitId;
    }

    public void setSolicitId(String solicitId) {
        this.solicitId = solicitId == null ? null : solicitId.trim();
    }

    public String getDetailOrgId() {
        return detailOrgId;
    }

    public void setDetailOrgId(String detailOrgId) {
        this.detailOrgId = detailOrgId == null ? null : detailOrgId.trim();
    }

    public String getDetailOrgName() {
        return detailOrgName;
    }

    public void setDetailOrgName(String detailOrgName) {
        this.detailOrgName = detailOrgName == null ? null : detailOrgName.trim();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId == null ? null : itemId.trim();
    }

    public String getItemVerId() {
        return itemVerId;
    }

    public void setItemVerId(String itemVerId) {
        this.itemVerId = itemVerId == null ? null : itemVerId.trim();
    }

    public Long getDetailDueDays() {
        return detailDueDays;
    }

    public void setDetailDueDays(Long detailDueDays) {
        this.detailDueDays = detailDueDays;
    }

    public Long getDetailRealDays() {
        return detailRealDays;
    }

    public void setDetailRealDays(Long detailRealDays) {
        this.detailRealDays = detailRealDays;
    }

    public String getDetailDaysUnit() {
        return detailDaysUnit;
    }

    public void setDetailDaysUnit(String detailDaysUnit) {
        this.detailDaysUnit = detailDaysUnit == null ? null : detailDaysUnit.trim();
    }

    public java.util.Date getDetailStartTime() {
        return detailStartTime;
    }

    public void setDetailStartTime(java.util.Date detailStartTime) {
        this.detailStartTime = detailStartTime;
    }

    public java.util.Date getDetailEndTime() {
        return detailEndTime;
    }

    public void setDetailEndTime(java.util.Date detailEndTime) {
        this.detailEndTime = detailEndTime;
    }

    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState == null ? null : detailState.trim();
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

    public String getModifiear() {
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    //public String getTableName()  {
    //    return "AeaHiSolicitDetail";
    //}
}
