package com.augurit.aplanmis.common.domain;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
/**
* 项目资金来源构成比例-模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-10-31 09:28:01</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-31 09:28:01</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
public class AeaExProjMoney implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String moneyId; // (主键)
        private String projInfoId; // (项目ID)
        private String govFinance; // (各级政府财政资金投资)
        private String stateEnterprice; // (国有企业资金投资)
        private String stateInvestment; // (国家融资)
        private String internationalInvestment; // (国际组织或外国政府资金)
        private String collectiveInvestment; // (集体经济组织投资)
        private String foreignInvestment; // (外商（国）投资)
        private String hkInvestment; // (港澳台投资)
        private String privateInvestment; // (私（民）营投资)
        private String otherInvestment; // (其他资金来源)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date govOrgConfirmTime; // (建设行业主管部门确认时间)
        private String govOrgCode; // (建设行业主管部门行政单位组织代码)
        private String govOrgName; // (建设行业主管部门行政单位名称)
        private String govAreaCode; // (建设行业主管部门行政单位区域码)
        private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // (修改时间)
        private String rootOrgId; // (所属根组织ID)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getMoneyId(){
        return moneyId;
    }
    public void setMoneyId( String moneyId ) {
        this.moneyId = moneyId == null ? null : moneyId.trim();
    }
    public String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public String getGovFinance(){
        return govFinance;
    }
    public void setGovFinance( String govFinance ) {
        this.govFinance = govFinance == null ? null : govFinance.trim();
    }
    public String getStateEnterprice(){
        return stateEnterprice;
    }
    public void setStateEnterprice( String stateEnterprice ) {
        this.stateEnterprice = stateEnterprice == null ? null : stateEnterprice.trim();
    }
    public String getStateInvestment(){
        return stateInvestment;
    }
    public void setStateInvestment( String stateInvestment ) {
        this.stateInvestment = stateInvestment == null ? null : stateInvestment.trim();
    }
    public String getInternationalInvestment(){
        return internationalInvestment;
    }
    public void setInternationalInvestment( String internationalInvestment ) {
        this.internationalInvestment = internationalInvestment == null ? null : internationalInvestment.trim();
    }
    public String getCollectiveInvestment(){
        return collectiveInvestment;
    }
    public void setCollectiveInvestment( String collectiveInvestment ) {
        this.collectiveInvestment = collectiveInvestment == null ? null : collectiveInvestment.trim();
    }
    public String getForeignInvestment(){
        return foreignInvestment;
    }
    public void setForeignInvestment( String foreignInvestment ) {
        this.foreignInvestment = foreignInvestment == null ? null : foreignInvestment.trim();
    }
    public String getHkInvestment(){
        return hkInvestment;
    }
    public void setHkInvestment( String hkInvestment ) {
        this.hkInvestment = hkInvestment == null ? null : hkInvestment.trim();
    }
    public String getPrivateInvestment(){
        return privateInvestment;
    }
    public void setPrivateInvestment( String privateInvestment ) {
        this.privateInvestment = privateInvestment == null ? null : privateInvestment.trim();
    }
    public String getOtherInvestment(){
        return otherInvestment;
    }
    public void setOtherInvestment( String otherInvestment ) {
        this.otherInvestment = otherInvestment == null ? null : otherInvestment.trim();
    }
    public java.util.Date getGovOrgConfirmTime() {
        return govOrgConfirmTime;
    }
    public void setGovOrgConfirmTime( java.util.Date govOrgConfirmTime ){
        this.govOrgConfirmTime = govOrgConfirmTime;
    }

    public String getGovOrgCode(){
        return govOrgCode;
    }
    public void setGovOrgCode( String govOrgCode ) {
        this.govOrgCode = govOrgCode == null ? null : govOrgCode.trim();
    }
    public String getGovOrgName(){
        return govOrgName;
    }
    public void setGovOrgName( String govOrgName ) {
        this.govOrgName = govOrgName == null ? null : govOrgName.trim();
    }
    public String getGovAreaCode(){
        return govAreaCode;
    }
    public void setGovAreaCode( String govAreaCode ) {
        this.govAreaCode = govAreaCode == null ? null : govAreaCode.trim();
    }
    public String getCreater(){
        return creater;
    }
    public void setCreater( String creater ) {
        this.creater = creater == null ? null : creater.trim();
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime( java.util.Date createTime ){
        this.createTime = createTime;
    }

    public String getModifier(){
        return modifier;
    }
    public void setModifier( String modifier ) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
    public java.util.Date getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime( java.util.Date modifyTime ){
        this.modifyTime = modifyTime;
    }

    public String getRootOrgId(){
        return rootOrgId;
    }
    public void setRootOrgId( String rootOrgId ) {
        this.rootOrgId = rootOrgId == null ? null : rootOrgId.trim();
    }
    //public String getTableName()  {
    //    return "AeaExProjMoney";
    //}
}
