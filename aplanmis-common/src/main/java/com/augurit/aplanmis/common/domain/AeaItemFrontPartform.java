package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = " 事项的前置检查事项")
public class AeaItemFrontPartform implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frontPartformId; // (ID)
    private String itemVerId; // (事项版本ID)
    private String itemPartformId; // (前置事项版本ID)
    private Long sortNo; // (排序字段)
    private String isActive; // ()

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // ()
    private String rootOrgId; // (根组织id)
}
