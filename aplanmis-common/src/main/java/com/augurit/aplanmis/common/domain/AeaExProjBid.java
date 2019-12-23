package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 招投标信息-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-10-31 15:56:12</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-10-31 15:56:12</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
public class AeaExProjBid implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String bidId; // (主键)
    private String projInfoId; // (项目ID)
    private String winBidNoticeCode; // (中标通知书编号)
    private String bidSectionName; // (标段名称)
    private String bidSectionAddr; // (标段地址)
    private String unitAddr; // (单位地址)
    private String bidType; // (招标类型，来自于数据字典)
    private String bidMode; // (招标方式，来自于数据字典)
    private String bidSectionMoney; // (标段金额，单位：万元)
    private String bidSectionArea; // (表单面积，单位：平方米)
    private String bidSectionSpan; // (跨度，单位：米)
    private String bidSectionLength; // (长度，单位：里)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private java.util.Date winBidTime; // (中标日期)
    private String winBidMoney; // (实际中标金额，单位：万元)
    private String bidMemo; // (备注)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)

    public String getTenderNum() {
        return tenderNum;
    }

    public void setTenderNum(String tenderNum) {
        this.tenderNum = tenderNum;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (所属根组织ID)


    //扩展字段
    private String tenderNum;  //''省级中标通知书编号(广东三库一平台返回)',

    private String formId;

    // ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId == null ? null : bidId.trim();
    }

    public String getProjInfoId() {
        return projInfoId;
    }

    public void setProjInfoId(String projInfoId) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }

    public String getWinBidNoticeCode() {
        return winBidNoticeCode;
    }

    public void setWinBidNoticeCode(String winBidNoticeCode) {
        this.winBidNoticeCode = winBidNoticeCode == null ? null : winBidNoticeCode.trim();
    }

    public String getBidSectionName() {
        return bidSectionName;
    }

    public void setBidSectionName(String bidSectionName) {
        this.bidSectionName = bidSectionName == null ? null : bidSectionName.trim();
    }

    public String getBidSectionAddr() {
        return bidSectionAddr;
    }

    public void setBidSectionAddr(String bidSectionAddr) {
        this.bidSectionAddr = bidSectionAddr == null ? null : bidSectionAddr.trim();
    }

    public String getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr == null ? null : unitAddr.trim();
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType == null ? null : bidType.trim();
    }

    public String getBidMode() {
        return bidMode;
    }

    public void setBidMode(String bidMode) {
        this.bidMode = bidMode == null ? null : bidMode.trim();
    }

    public String getBidSectionMoney() {
        return bidSectionMoney;
    }

    public void setBidSectionMoney(String bidSectionMoney) {
        this.bidSectionMoney = bidSectionMoney == null ? null : bidSectionMoney.trim();
    }

    public String getBidSectionArea() {
        return bidSectionArea;
    }

    public void setBidSectionArea(String bidSectionArea) {
        this.bidSectionArea = bidSectionArea == null ? null : bidSectionArea.trim();
    }

    public String getBidSectionSpan() {
        return bidSectionSpan;
    }

    public void setBidSectionSpan(String bidSectionSpan) {
        this.bidSectionSpan = bidSectionSpan == null ? null : bidSectionSpan.trim();
    }

    public String getBidSectionLength() {
        return bidSectionLength;
    }

    public void setBidSectionLength(String bidSectionLength) {
        this.bidSectionLength = bidSectionLength == null ? null : bidSectionLength.trim();
    }

    public java.util.Date getWinBidTime() {
        return winBidTime;
    }

    public void setWinBidTime(java.util.Date winBidTime) {
        this.winBidTime = winBidTime;
    }

    public String getWinBidMoney() {
        return winBidMoney;
    }

    public void setWinBidMoney(String winBidMoney) {
        this.winBidMoney = winBidMoney == null ? null : winBidMoney.trim();
    }

    public String getBidMemo() {
        return bidMemo;
    }

    public void setBidMemo(String bidMemo) {
        this.bidMemo = bidMemo == null ? null : bidMemo.trim();
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
    //    return "AeaExProjBid";
    //}
}
