package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel("申报实例ID结果集")
public class ApplyinstIdVo {
    @ApiModelProperty(value = "并行申请实例ID列表")
    private String[] applyinstIds;
    @ApiModelProperty(value = "并联申请实例ID")
    private String parallelApplyinstId;
}