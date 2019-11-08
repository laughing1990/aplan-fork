package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:18</li>
 * </ul>
 */
public class AeaApplyinstProj implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String applyinstProjId; // ()
    private String applyinstId; // ()
    private String projInfoId; // ()
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()

    private String localCode;//项目编码
    private String projName;//项目/工程名称
    private String gcbm;//工程编码


    // ----------------------------------------------------- Constructors
// ----------------------------------------------------- Methods
    public String getApplyinstProjId() {
        return applyinstProjId;
    }

    public void setApplyinstProjId(String applyinstProjId) {
        this.applyinstProjId = applyinstProjId == null ? null : applyinstProjId.trim();
    }

    public String getApplyinstId() {
        return applyinstId;
    }

    public void setApplyinstId(String applyinstId) {
        this.applyinstId = applyinstId == null ? null : applyinstId.trim();
    }

    public String getProjInfoId() {
        return projInfoId;
    }

    public void setProjInfoId(String projInfoId) {
        this.projInfoId = projInfoId == null ? null : projInfoId.trim();
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

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getGcbm() {
        return gcbm;
    }

    public void setGcbm(String gcbm) {
        this.gcbm = gcbm;
    }

    //public String getTableName()  {
    //    return "AeaApplyinstProj";
    //}
}
