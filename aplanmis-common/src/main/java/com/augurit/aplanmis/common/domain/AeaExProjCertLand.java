package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                                    import org.springframework.format.annotation.DateTimeFormat;
/**
* 建设项目用地规划许可证-模型

*/
public class AeaExProjCertLand implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String certLandId; // (主键)
        private String projInfoId; // (项目ID)
        private String certLandCode; // (用地规划许可证编号)
        private String landNature; // (用地性质，来自于数据字典)
        private String landAreaValue; // (用地面积数值)
        private String landAreaUnit; // (用地面积单位)
        private String govOrgCode; // (发证机关机构代码)
        private String govOrgName; // (发证机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date publishTime; // (发证日期)
        private String certLandMemo; // (备注)
        private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // (修改时间)
        private String rootOrgId; // (所属根组织ID)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getCertLandId(){
        return certLandId;
    }
    public void setCertLandId( String certLandId ) {
        this.certLandId = certLandId == null ? null : certLandId.trim();
    }
    public String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public String getCertLandCode(){
        return certLandCode;
    }
    public void setCertLandCode( String certLandCode ) {
        this.certLandCode = certLandCode == null ? null : certLandCode.trim();
    }
    public String getLandNature(){
        return landNature;
    }
    public void setLandNature( String landNature ) {
        this.landNature = landNature == null ? null : landNature.trim();
    }
    public String getLandAreaValue(){
        return landAreaValue;
    }
    public void setLandAreaValue( String landAreaValue ) {
        this.landAreaValue = landAreaValue == null ? null : landAreaValue.trim();
    }
    public String getLandAreaUnit(){
        return landAreaUnit;
    }
    public void setLandAreaUnit( String landAreaUnit ) {
        this.landAreaUnit = landAreaUnit == null ? null : landAreaUnit.trim();
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
    public java.util.Date getPublishTime() {
        return publishTime;
    }
    public void setPublishTime( java.util.Date publishTime ){
        this.publishTime = publishTime;
    }

    public String getCertLandMemo(){
        return certLandMemo;
    }
    public void setCertLandMemo( String certLandMemo ) {
        this.certLandMemo = certLandMemo == null ? null : certLandMemo.trim();
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
    //    return "AeaExProjCertLand";
    //}
}
