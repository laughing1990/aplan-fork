package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("实例化结果")
public class ApplyInstantiateResult {
    @ApiModelProperty(value = "流程实例", dataType = "string")
    private String procInstId;
    @ApiModelProperty(value = "模板实例", dataType = "string")
    private String appinstId;
    @ApiModelProperty(value = "申报实例", dataType = "string")
    private String applyinstId;
}