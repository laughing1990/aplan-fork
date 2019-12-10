package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("并行事项及其对应的申报实例ID")
public class PropulsionItemApplyinstIdVo {
    @ApiModelProperty(value = "事项版本id", required = false, dataType = "string")
    private String itemVerId;
    @ApiModelProperty(value = "申报实例ID", required = false, dataType = "string")
    private String seriesApplyinstId;
    @ApiModelProperty(value = "单项实例ID", required = false, dataType = "string")
    private String seriesinstId;
}
