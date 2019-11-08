package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 统计数据生成记录表-模型
*/
@Data
public class AeaAnaStatisticsRecord implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键 **/
    private String statisticsRecordId;

    /** 统计报表总表ID **/
    private String reportId;

    /** 报表名称 **/
    private String reportName;

    /** 统计数据开始日期（yyyy-MM-dd） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsStartDate;

    /** 统计数据结束日期（yyyy-MM-dd） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsEndDate;

    /** 生成来源。1表示程序生成，0表示人工生成 **/
    private String operateSource;

    /** 统计类型。s表示申报统计，b表示办件统计 **/
    private String statisticsType;

    /** 创建人 **/
    private String creater;

    /** 创建时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;

    /** 修改人 **/
    private String modifier;

    /** 修改时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date modifyTime;

    /** 所属顶级组织ID **/
    private String rootOrgId;

}
