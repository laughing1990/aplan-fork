package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "事项前置扩展表检测实体类")
public class AeaItemFrontPartform implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String frontPartformId;

    @ApiModelProperty("事项版本ID")
    private String itemVerId;

    @ApiModelProperty("前置事项版本ID")
    private String itemPartformId;

    @ApiModelProperty("排序字段")
    private Long sortNo;

    @ApiModelProperty("是否启用 1启用 0禁用")
    private String isActive;

    @ApiModelProperty("备注")
    private String itemPartformMemo;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    @ApiModelProperty("根组织id")
    private String rootOrgId;

    @ApiModelProperty("扩展字段：关键字查询")
    private String keyword;
}
