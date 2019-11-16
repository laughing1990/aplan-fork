package com.augurit.aplanmis.supermarket.main.vo.province;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ProvinceIndexData {
    @ApiModelProperty(value = "中介机构数量")
    private int agentUnitCount = 0;
    @ApiModelProperty(value = "中介服务事项数量")
    private int itemServiceCount = 0;
    @ApiModelProperty(value = "已完成的采购需求数量")
    private int finishedPurchaseCount = 0;
    @ApiModelProperty(value = "已发布的采购需求")
    private int publishedPurchaseCount = 0;
}
