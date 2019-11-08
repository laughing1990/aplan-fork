package com.augurit.aplanmis.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApplyinstCorrectinstDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "applyinstId", value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "taskinstId", value = "节点ID（由流程触发时必填）")
    private String taskinstId;

    @ApiModelProperty(name = "isFlowTrigger", value = "补全触发类型（1：表示在流程上触发，0：非流程触发）")
    private String isFlowTrigger;

    @ApiModelProperty(name = "correctDueDays", value = "补全期限")
    private Long correctDueDays;

    @ApiModelProperty(name = "correctDesc", value = "补全备注")
    private String correctDesc;

    @ApiModelProperty(name = "correctState", value = "补全实例状态")
    private String correctState;

    @ApiModelProperty(name = "matCorrectDtosJson", value = "补全材料清单")
    private String matCorrectDtosJson;
}
