package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("并联申报中并行申报实例与事项映射vo")
public class ParallelItemApplyinstVo {

    @ApiModelProperty(value = "事项版本id", dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "单项申报实例id")
    private String seriesApplyinstId;

    public ParallelItemApplyinstVo(String itemVerId, String seriesApplyinstId) {
        this.itemVerId = itemVerId;
        this.seriesApplyinstId = seriesApplyinstId;
    }

    public ParallelItemApplyinstVo() {
    }
}
