package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = " 事项与总表单关联表")
public class AeaItemOneform implements Serializable{

    private static final long serialVersionUID = 1L;

    private String itemOneformId; // (主键ID)
    private String itemVerId; // (事项版本ID)
    private String oneformId; // (一张表单ID)
    private String linkName; // (链接名称)
    private Double sortNo; // (排序字段)
    private String isActive; // (是否启用，0表示禁用，1表示启用)

    private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // (修改时间)

    @ApiModelProperty(value = "扩展字段：总表名称")
    private String oneformName;
}
