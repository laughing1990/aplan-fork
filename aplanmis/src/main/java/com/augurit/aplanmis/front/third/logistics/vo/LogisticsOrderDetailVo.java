package com.augurit.aplanmis.front.third.logistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("物流跟踪信息")
public class LogisticsOrderDetailVo {

    @ApiModelProperty(value = "接收时间")
    private String acceptTime;

    @ApiModelProperty(value = "接收地址")
    private String acceptAddress;

    @ApiModelProperty(value = "备注")
    private String remark;

    public LogisticsOrderDetailVo() {
    }

    public LogisticsOrderDetailVo(String acceptTime, String acceptAddress, String remark) {
        this.acceptTime = acceptTime;
        this.acceptAddress = acceptAddress;
        this.remark = remark;
    }
}
