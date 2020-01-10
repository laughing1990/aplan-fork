package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Getter
@Setter
public class AeaHiSmsSendBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;

    @ApiModelProperty(value = "窗口人员id")
    protected String windowUserId;

    @ApiModelProperty(value = "窗口人员名称")
    protected String windowUserName;

    @ApiModelProperty(value = "窗口处理时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    protected java.util.Date windowHandleTime;

    @ApiModelProperty(value = "领取模式：1 窗口取证  0 邮政快递")
    protected String receiveMode;

    @ApiModelProperty(value = "订单号")
    protected String orderId;

    @ApiModelProperty(value = "运单号")
    protected String expressNum;

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

    @ApiModelProperty(value = "快递类型：1 普通快递  2 上门取件")
    protected String smsType;

    @ApiModelProperty(value = "发证日期/快递日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    protected java.util.Date issueTime;

    @ApiModelProperty(value = "创建人")
    protected String creater;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    protected java.util.Date createTime;

    @ApiModelProperty(value = "是否委托人")
    protected String isConsigner;

    @ApiModelProperty(value = "委托书附件id")
    protected String consignerAttId;

    protected String rootOrgId;
}
