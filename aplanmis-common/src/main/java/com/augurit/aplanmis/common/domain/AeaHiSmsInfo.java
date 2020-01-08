package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Getter
@Setter
public class AeaHiSmsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "领取模式：1 窗口取证  0 邮政快递")
    private String receiveMode;

    @ApiModelProperty(value = "领取方式", notes = "1：一次领取，0：多次领取")
    private String receiveType;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "运单号")
    private String expressNum;

    @ApiModelProperty(value = "收件人名字")
    private String addresseeName;

    @ApiModelProperty(value = "收件人号码")
    private String addresseePhone;

    @ApiModelProperty(value = "收件人身份证")
    private String addresseeIdcard;

    @ApiModelProperty(value = "收件人所在地址")
    private String addresseeAddr;

    @ApiModelProperty(value = "收件人所在省")
    private String addresseeProvince;

    @ApiModelProperty(value = "收件人所在城市")
    private String addresseeCity;

    @ApiModelProperty(value = "收件人所在区县")
    private String addresseeCounty;

    @ApiModelProperty(value = "寄件人名字")
    private String senderName;

    @ApiModelProperty(value = "寄件人电话")
    private String senderPhone;

    @ApiModelProperty(value = "寄件人地址")
    private String senderAddr;

    @ApiModelProperty(value = "寄件人所在省")
    private String senderProvince;

    @ApiModelProperty(value = "寄件人所在城市")
    private String senderCity;

    @ApiModelProperty(value = "寄件人所在区县")
    private String senderCounty;

    @ApiModelProperty(value = "快递类型：1 普通快递  2 上门取件")
    private String smsType;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "发证日期/快递日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date issueTime;

    @ApiModelProperty(value = "根组织ID")
    private String rootOrgId;

    // 扩展字段

    @ApiModelProperty(value = "是否委托人")
    private String isConsigner;
}
