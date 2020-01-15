package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("申报实例与表单实例关联")
public class AeaApplyinstForminst implements Serializable {

    private static final long serialVersionUID = 1L;

    private String applyinstForminstId;

    @ApiModelProperty(value = "申报实例id")
    private String applyinstId;

    @ApiModelProperty(value = "表单实例id")
    private String forminstId;

    private String creater;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    private String modifier;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifierTime;

    // 扩展字段

    @ApiModelProperty(value = "表单定义id")
    private String stoFormId;

    @ApiModelProperty(value = "表单记录主键")
    private String formPrimaryKey;

}
