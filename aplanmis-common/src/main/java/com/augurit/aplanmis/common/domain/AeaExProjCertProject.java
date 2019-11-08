package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                        import org.springframework.format.annotation.DateTimeFormat;
/**
* 建设工程规划许可证-模型
*/
public class AeaExProjCertProject implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String certProjectId; // (主键)
        private String projInfoId; // (项目ID)
        private String certProjectCode; // (建设工程规划许可证编号)
        private String publishOrgCode; // (核发机关组织机构代码)
        private String publishOrgName; // (核发机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date publishTime; // (核发日期)
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
    public String getCertProjectId(){
        return certProjectId;
    }
    public void setCertProjectId( String certProjectId ) {
        this.certProjectId = certProjectId == null ? null : certProjectId.trim();
    }
    public String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public String getCertProjectCode(){
        return certProjectCode;
    }
    public void setCertProjectCode( String certProjectCode ) {
        this.certProjectCode = certProjectCode == null ? null : certProjectCode.trim();
    }
    public String getPublishOrgCode(){
        return publishOrgCode;
    }
    public void setPublishOrgCode( String publishOrgCode ) {
        this.publishOrgCode = publishOrgCode == null ? null : publishOrgCode.trim();
    }
    public String getPublishOrgName(){
        return publishOrgName;
    }
    public void setPublishOrgName( String publishOrgName ) {
        this.publishOrgName = publishOrgName == null ? null : publishOrgName.trim();
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
    //    return "AeaExProjCertProject";
    //}
}
