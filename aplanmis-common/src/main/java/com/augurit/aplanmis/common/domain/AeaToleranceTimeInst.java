package com.augurit.aplanmis.common.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
/**
* 事项容缺时限实例
*/
public class AeaToleranceTimeInst implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String toleranceTimeInstId; // (主键)
        private String iteminstId; // (事项实例id)
        private String timeruleId; // (时限计算规则ID)
    @Size(max=22)
    private Double timeLimit; // (时限数)
        private String timeruleUnit; // (【计算规则时间单位】ND：自然日，WD：工作日，NH：小时（自然日），WH：小时（工作日）)
    @Size(max=22)
    private Double useLimitTime; // (已用时)
    @Size(max=22)
    private Double remainingTime; // (剩余用时)
    @Size(max=22)
    private Double overdueTime; // (逾期用时)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date overdueDate; // (逾期日期)
        private String orgId; // (顶级组织ID)
        private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // ()
        private String modifier; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date modifyTime; // ()
        private String instState; // (实例状态：1 正常，2 预警，3 逾期)
        private String isCompleted; // (容缺补正是否完成：1 是，0 否)
// ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getToleranceTimeInstId(){
        return toleranceTimeInstId;
    }
    public void setToleranceTimeInstId( String toleranceTimeInstId ) {
        this.toleranceTimeInstId = toleranceTimeInstId == null ? null : toleranceTimeInstId.trim();
    }
    public String getIteminstId(){
        return iteminstId;
    }
    public void setIteminstId( String iteminstId ) {
        this.iteminstId = iteminstId == null ? null : iteminstId.trim();
    }
    public String getTimeruleId(){
        return timeruleId;
    }
    public void setTimeruleId( String timeruleId ) {
        this.timeruleId = timeruleId == null ? null : timeruleId.trim();
    }
    public Double getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit( Double timeLimit ){
        this.timeLimit = timeLimit;
    }

    public String getTimeruleUnit(){
        return timeruleUnit;
    }
    public void setTimeruleUnit( String timeruleUnit ) {
        this.timeruleUnit = timeruleUnit == null ? null : timeruleUnit.trim();
    }
    public Double getUseLimitTime() {
        return useLimitTime;
    }
    public void setUseLimitTime( Double useLimitTime ){
        this.useLimitTime = useLimitTime;
    }

    public Double getRemainingTime() {
        return remainingTime;
    }
    public void setRemainingTime( Double remainingTime ){
        this.remainingTime = remainingTime;
    }

    public Double getOverdueTime() {
        return overdueTime;
    }
    public void setOverdueTime( Double overdueTime ){
        this.overdueTime = overdueTime;
    }

    public java.util.Date getOverdueDate() {
        return overdueDate;
    }
    public void setOverdueDate( java.util.Date overdueDate ){
        this.overdueDate = overdueDate;
    }

    public String getOrgId(){
        return orgId;
    }
    public void setOrgId( String orgId ) {
        this.orgId = orgId == null ? null : orgId.trim();
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

    public String getInstState(){
        return instState;
    }
    public void setInstState( String instState ) {
        this.instState = instState == null ? null : instState.trim();
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }
}
