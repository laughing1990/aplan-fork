package com.augurit.aplanmis.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MatCorrectinstDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "correctId", value = "补正实例ID")
    private String correctId;

    @ApiModelProperty(name = "correctState", value = "补正实例状态")
    private String correctState;

    @ApiModelProperty(name = "correctDesc", value = "补正备注")
    private String correctDesc;

    @ApiModelProperty(name = "matCorrectDtosJson", value = "补正材料清单")
    private String matCorrectDtosJson;

}
