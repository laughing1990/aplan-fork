package com.augurit.aplanmis.front.third.logistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogisticsOrderVo {

    @ApiModelProperty(value = "收件人名字")
    protected String addresseeName;

    @ApiModelProperty(value = "收件人号码")
    protected String addresseePhone;

    @ApiModelProperty(value = "收件人身份证")
    protected String addresseeIdcard;

    @ApiModelProperty(value = "收件人所在地址")
    protected String addresseeAddr;

    @ApiModelProperty(value = "收件人所在省")
    protected String addresseeProvince;

    @ApiModelProperty(value = "收件人所在城市")
    protected String addresseeCity;

    @ApiModelProperty(value = "收件人所在区县")
    protected String addresseeCounty;

    @ApiModelProperty(value = "寄件人名字")
    protected String senderName;

    @ApiModelProperty(value = "寄件人电话")
    protected String senderPhone;

    @ApiModelProperty(value = "寄件人地址")
    protected String senderAddr;

    @ApiModelProperty(value = "寄件人所在省")
    protected String senderProvince;

    @ApiModelProperty(value = "寄件人所在城市")
    protected String senderCity;

    @ApiModelProperty(value = "寄件人所在区县")
    protected String senderCounty;
}
