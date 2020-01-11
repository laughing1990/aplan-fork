package com.augurit.aplanmis.front.third.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("物流下单成功vo")
public class LogisticsOrderResultVo {

    @ApiModelProperty(value = "订单号")
    protected String orderId;

    @ApiModelProperty(value = "运单号")
    protected String expressNum;
}
