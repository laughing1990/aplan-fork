package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                                    import org.springframework.format.annotation.DateTimeFormat;
/**
* 建设项目选址意见书-模型

*/
public class AeaExProjSite implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String siteId; // (主键)
        private String projInfoId; // (项目ID)
        private String siteCode; // (建设项目选址意见书批文号)
        private String landAreaValue; // (拟用地面积数值)
        private String landAreaUnit; // (拟用地面积单位)
        private String constructionSize; // (拟建设规模)
        private String govOrgCode; // (核发机关组织机构代码)
        private String govOrgName; // (核发机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date publishTime; // (核发日期)
        private String siteMemo; // (备注)
        private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // (修改时间)
        private String rootOrgId; // (所属根组织ID)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getSiteId(){
        return siteId;
    }
    public void setSiteId( String siteId ) {
        this.siteId = siteId == null ? null : siteId.trim();
    }
    public String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public String getSiteCode(){
        return siteCode;
    }
    public void setSiteCode( String siteCode ) {
        this.siteCode = siteCode == null ? null : siteCode.trim();
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
    public String getConstructionSize(){
        return constructionSize;
    }
    public void setConstructionSize( String constructionSize ) {
        this.constructionSize = constructionSize == null ? null : constructionSize.trim();
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

    public String getSiteMemo(){
        return siteMemo;
    }
    public void setSiteMemo( String siteMemo ) {
        this.siteMemo = siteMemo == null ? null : siteMemo.trim();
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
    //    return "AeaExProjSite";
    //}
}
