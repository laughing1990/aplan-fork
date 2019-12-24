package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                            import org.springframework.format.annotation.DateTimeFormat;
/**
* 合同信息-模型

*/
public class AeaExProjContract implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private java.lang.String contractId; // (主键)
        private java.lang.String projInfoId; // (项目ID)
        private java.lang.String contractCode; // (合同编号)
        private java.lang.String contractType; // (合同类型)
            private java.lang.String contractMoeny; // (合同金额，单位：万元)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date contractSignTime; // (合同签订日期)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date contractConfirmTime; // (合同确认日期)
        private java.lang.String govOrgCode; // (合同确认的行政单位机构代码)
        private java.lang.String govOrgName; // (合同确认的行政单位机构名称)
        private java.lang.String govOrgAreaCode; // (合同确认的行政单位区域码)
        private java.lang.String contractMemo; // (备注)
        private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private java.lang.String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // (修改时间)
        private java.lang.String rootOrgId; // (所属根组织ID)
        //扩展字段
        private String formId;

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    private String contractNum;//'省级合同编号(广东三库一平台返回)',
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public java.lang.String getContractId(){
        return contractId;
    }
    public void setContractId( java.lang.String contractId ) {
        this.contractId = contractId == null ? null : contractId.trim();
    }
    public java.lang.String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( java.lang.String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public java.lang.String getContractCode(){
        return contractCode;
    }
    public void setContractCode( java.lang.String contractCode ) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }
    public java.lang.String getContractType(){
        return contractType;
    }
    public void setContractType( java.lang.String contractType ) {
        this.contractType = contractType == null ? null : contractType.trim();
    }
    public java.lang.String getContractMoeny(){
        return contractMoeny;
    }
    public void setContractMoeny( java.lang.String contractMoeny ) {
        this.contractMoeny = contractMoeny == null ? null : contractMoeny.trim();
    }
    public java.util.Date getContractSignTime() {
        return contractSignTime;
    }
    public void setContractSignTime( java.util.Date contractSignTime ){
        this.contractSignTime = contractSignTime;
    }

    public java.util.Date getContractConfirmTime() {
        return contractConfirmTime;
    }
    public void setContractConfirmTime( java.util.Date contractConfirmTime ){
        this.contractConfirmTime = contractConfirmTime;
    }

    public java.lang.String getGovOrgCode(){
        return govOrgCode;
    }
    public void setGovOrgCode( java.lang.String govOrgCode ) {
        this.govOrgCode = govOrgCode == null ? null : govOrgCode.trim();
    }
    public java.lang.String getGovOrgName(){
        return govOrgName;
    }
    public void setGovOrgName( java.lang.String govOrgName ) {
        this.govOrgName = govOrgName == null ? null : govOrgName.trim();
    }
    public java.lang.String getGovOrgAreaCode(){
        return govOrgAreaCode;
    }
    public void setGovOrgAreaCode( java.lang.String govOrgAreaCode ) {
        this.govOrgAreaCode = govOrgAreaCode == null ? null : govOrgAreaCode.trim();
    }
    public java.lang.String getContractMemo(){
        return contractMemo;
    }
    public void setContractMemo( java.lang.String contractMemo ) {
        this.contractMemo = contractMemo == null ? null : contractMemo.trim();
    }
    public java.lang.String getCreater(){
        return creater;
    }
    public void setCreater( java.lang.String creater ) {
        this.creater = creater == null ? null : creater.trim();
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime( java.util.Date createTime ){
        this.createTime = createTime;
    }

    public java.lang.String getModifier(){
        return modifier;
    }
    public void setModifier( java.lang.String modifier ) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
    public java.util.Date getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime( java.util.Date modifyTime ){
        this.modifyTime = modifyTime;
    }

    public java.lang.String getRootOrgId(){
        return rootOrgId;
    }
    public void setRootOrgId( java.lang.String rootOrgId ) {
        this.rootOrgId = rootOrgId == null ? null : rootOrgId.trim();
    }
    public java.lang.String getFormId(){
        return formId;
    }
    public void setFormId( java.lang.String formId ) {
        this.formId = formId == null ? null : formId.trim();
    }
    //public String getTableName()  {
    //    return "AeaExProjContract";
    //}
}
