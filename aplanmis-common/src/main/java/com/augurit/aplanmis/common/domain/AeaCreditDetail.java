package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 信用管理-信用汇总子表（字段列表）-模型
*/
@Data
public class AeaCreditDetail implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String detailId; // (主键)
    private String summaryId; // (所属汇总ID)
    private String cnColumnName; // (字段中文名)
    private String enColumnName; // (字段英文名)
    private String columnValue; // (字段值)
    private String columnDataType; // (数据类型，s表示字符串，n表示数字，d表示日期)
    
    private String isSync; // (是否同步，0表示手工录入，1表示自动同步)
    private String syncSource; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date syncTime; // (同步时间)
    private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (更新时间)
    private String rootOrgId; // (根组织ID)

    private String keyword; //搜索条件
}
