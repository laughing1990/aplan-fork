package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 信用管理-红黑名单管理-模型
*/
@Data
public class AeaCreditSummary implements Serializable{

    private static final long serialVersionUID = 1L;

    private String summaryId; // (主键)
    private String creditType; // (信用类型，bad表示失信，good表示守信，reg表示登记注册备案，cert表示资质认证许可)
    private String summaryType; // (对象类型，u表示单位，l表示联系人)
    private String creditUnitInfoId; // ()
    private String linkmanInfoId; // (联系人ID)
    private String cnTableName; // (表中文名)
    private String enTableName; // (表英文名)
    private String cnDeptName; // (所属单位中文名)
    private String enDeptName; // (所属单位英文名)
    private String isValid; // (是否有效，0表示无效，1表示有效)
    private String invalidReason; // (失效原因)

    private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (更新时间)
    private String isSync; // (是否同步，0表示手工录入，1表示自动同步)
    private String syncSource; // (同步来源)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date syncTime; // (同步时间)
    private String rootOrgId; // (根组织ID)

    private String creditUnitInfoName;
    private String linkmanInfoName;
    private String keyword;
}
