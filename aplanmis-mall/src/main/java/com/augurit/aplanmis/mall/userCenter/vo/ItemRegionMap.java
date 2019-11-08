package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("事项与区划的键值")
public class ItemRegionMap {
    @ApiModelProperty(value = "标准事项版本ID")
    private String baseItemVerId;
    @ApiModelProperty(value = "实施事项版本ID")
    private String itemVerId;
    @ApiModelProperty(value = "区划ID")
    private String regionId;
}
