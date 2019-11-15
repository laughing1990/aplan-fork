package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = " 事项与扩展表单关联表")
public class AeaItemPartform implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String itemPartformId;

    @ApiModelProperty("主键")
    private String partformName;

    @ApiModelProperty("事项版本ID")
    private String itemVerId;

    @ApiModelProperty("是否智能表单  1智能表单  0 开发表单")
    private String isSmartForm;

    @ApiModelProperty("表单定义ID")
    private String stoFormId;

    @ApiModelProperty("排序字段")
    private String sortNo;

    @ApiModelProperty("是否启动EL表达式，0表示禁用，1表示启用")
    private String useEl;

    @ApiModelProperty("EL表达式内容")
    private String elContent;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    //======拓展字段======

    private String formName;

    private String formCode;

    private String formProperty;

    private String formOrgId;

    private String orgName;

    private String formTmnId;

    private String formTmnName;

    private String isInnerForm;

    private String keyword;

    private String formUrl;
}
