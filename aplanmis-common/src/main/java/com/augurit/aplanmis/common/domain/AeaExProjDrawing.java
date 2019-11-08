package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
                            import org.springframework.format.annotation.DateTimeFormat;
/**
* 施工图审查信息-模型
<ul>

*/
public class AeaExProjDrawing implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private java.lang.String drawingId; // (主键)
        private java.lang.String projInfoId; // (项目ID)
        private java.lang.String provinceProjCode; // (省级项目编号)
        private java.lang.String drawingQuabookCode; // (施工图审查合格书编号)
        private java.lang.String inverstmentMoeny; // (投资额，单位：万元)
        private java.lang.String approveDrawingArea; // (图审面积，单位：平方米)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date approveStartTime; // (开始审查日期)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date approveEndTime; // (审查完成日期)
        private java.lang.String isOncePass; // (一次审查是否通过，0表示否，1表示是)
        private java.lang.String oncePassAgainstCount; // (一次审查时违反强条数)
        private java.lang.String oncePassAgainstItem; // (一次审查时违反的强条条目)
        private java.lang.String approveOpinion; // (施工图审查意见（终审）)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date approveConfirmTime; // (审图确认时间)
        private java.lang.String govOrgCode; // (审图确认的行政单位机构代码)
        private java.lang.String govOrgName; // (审图确认的行政单位名称)
        private java.lang.String govOrgAreaCode; // (审图确认的行政单位区域码)
        private java.lang.String drawingMemo; // (备注)
        private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private java.lang.String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // (修改时间)
        private java.lang.String rootOrgId; // (所属根组织ID)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public java.lang.String getDrawingId(){
        return drawingId;
    }
    public void setDrawingId( java.lang.String drawingId ) {
        this.drawingId = drawingId == null ? null : drawingId.trim();
    }
    public java.lang.String getProjInfoId(){
        return projInfoId;
    }
    public void setProjInfoId( java.lang.String projInfoId ) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
    }
    public java.lang.String getProvinceProjCode(){
        return provinceProjCode;
    }
    public void setProvinceProjCode( java.lang.String provinceProjCode ) {
        this.provinceProjCode = provinceProjCode == null ? null : provinceProjCode.trim();
    }
    public java.lang.String getDrawingQuabookCode(){
        return drawingQuabookCode;
    }
    public void setDrawingQuabookCode( java.lang.String drawingQuabookCode ) {
        this.drawingQuabookCode = drawingQuabookCode == null ? null : drawingQuabookCode.trim();
    }
    public java.lang.String getInverstmentMoeny(){
        return inverstmentMoeny;
    }
    public void setInverstmentMoeny( java.lang.String inverstmentMoeny ) {
        this.inverstmentMoeny = inverstmentMoeny == null ? null : inverstmentMoeny.trim();
    }
    public java.lang.String getApproveDrawingArea(){
        return approveDrawingArea;
    }
    public void setApproveDrawingArea( java.lang.String approveDrawingArea ) {
        this.approveDrawingArea = approveDrawingArea == null ? null : approveDrawingArea.trim();
    }
    public java.util.Date getApproveStartTime() {
        return approveStartTime;
    }
    public void setApproveStartTime( java.util.Date approveStartTime ){
        this.approveStartTime = approveStartTime;
    }

    public java.util.Date getApproveEndTime() {
        return approveEndTime;
    }
    public void setApproveEndTime( java.util.Date approveEndTime ){
        this.approveEndTime = approveEndTime;
    }

    public java.lang.String getIsOncePass(){
        return isOncePass;
    }
    public void setIsOncePass( java.lang.String isOncePass ) {
        this.isOncePass = isOncePass == null ? null : isOncePass.trim();
    }
    public java.lang.String getOncePassAgainstCount(){
        return oncePassAgainstCount;
    }
    public void setOncePassAgainstCount( java.lang.String oncePassAgainstCount ) {
        this.oncePassAgainstCount = oncePassAgainstCount == null ? null : oncePassAgainstCount.trim();
    }
    public java.lang.String getOncePassAgainstItem(){
        return oncePassAgainstItem;
    }
    public void setOncePassAgainstItem( java.lang.String oncePassAgainstItem ) {
        this.oncePassAgainstItem = oncePassAgainstItem == null ? null : oncePassAgainstItem.trim();
    }
    public java.lang.String getApproveOpinion(){
        return approveOpinion;
    }
    public void setApproveOpinion( java.lang.String approveOpinion ) {
        this.approveOpinion = approveOpinion == null ? null : approveOpinion.trim();
    }
    public java.util.Date getApproveConfirmTime() {
        return approveConfirmTime;
    }
    public void setApproveConfirmTime( java.util.Date approveConfirmTime ){
        this.approveConfirmTime = approveConfirmTime;
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
    public java.lang.String getDrawingMemo(){
        return drawingMemo;
    }
    public void setDrawingMemo( java.lang.String drawingMemo ) {
        this.drawingMemo = drawingMemo == null ? null : drawingMemo.trim();
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
    //public String getTableName()  {
    //    return "AeaExProjDrawing";
    //}
}
