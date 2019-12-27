package com.augurit.aplanmis.front.apply.vo;

import com.augurit.aplanmis.common.constants.ApplyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("单项申报参数实体vo")
public class SeriesApplyDataVo extends ApplyDataVo {

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "并联申报实例id", notes = "并联申报时， 并行申报需要与并联申报关联", hidden = true)
    private String parallelApplyinstId;

    @ApiModelProperty(value = "申请流水号")
    private String applyinstCode;

    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;

    @ApiModelProperty(value = "是否并行推行", notes = "0表示否，1表示是")
    private String isParallel;

    public SeriesApplyDataVo() {
        applyType = ApplyType.SERIES;
    }
}
