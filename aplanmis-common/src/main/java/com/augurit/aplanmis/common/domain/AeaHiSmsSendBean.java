package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/8/5/005 14:07
 */
@Data
public class AeaHiSmsSendBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "申请实例id")
    private String applyinstId; // ()
    @ApiModelProperty(value = "窗口人员id")
    private String windowUserId; // ()
    @ApiModelProperty(value = "窗口人员名称")
    private String windowUserName; // ()
    @ApiModelProperty(value = "窗口处理时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date windowHandleTime; // ()
    @ApiModelProperty(value = "领取模式：1 窗口取证  0 邮政快递")
    private String receiveMode; // (领取模式：1 窗口取证  0 邮政快递)
    @ApiModelProperty(value = "订单号")
    private String orderId; // (订单号)
    @ApiModelProperty(value = "运单号")
    private String expressNum; // (运单号)
    @ApiModelProperty(value = "收件人名字")
    private String addresseeName; // (收件人名字)
    @ApiModelProperty(value = "收件人号码")
    private String addresseePhone; // (收件人号码)
    @ApiModelProperty(value = "收件人身份证")
    private String addresseeIdcard; // (收件人身份证)
    @ApiModelProperty(value = "收件人所在地址")
    private String addresseeAddr; // (收件人所在地址)
    @ApiModelProperty(value = "收件人所在省")
    private String addresseeProvince; // (收件人所在省)
    @ApiModelProperty(value = "收件人所在城市")
    private String addresseeCity; // (收件人所在城市)
    @ApiModelProperty(value = "收件人所在区县")
    private String addresseeCounty; // (收件人所在区县)
    @ApiModelProperty(value = "寄件人名字")
    private String senderName; // (寄件人名字)
    @ApiModelProperty(value = "寄件人电话")
    private String senderPhone; // (寄件人电话)
    @ApiModelProperty(value = "寄件人地址")
    private String senderAddr; // (寄件人地址)
    @ApiModelProperty(value = "寄件人所在省")
    private String senderProvince; // (寄件人所在省)
    @ApiModelProperty(value = "寄件人所在城市")
    private String senderCity; // (寄件人所在城市)
    @ApiModelProperty(value = "寄件人所在区县")
    private String senderCounty; // (寄件人所在区县)
    @ApiModelProperty(value = "快递类型：1 普通快递  2 上门取件")
    private String smsType; // (快递类型：1 普通快递  2 上门取件)
    @ApiModelProperty(value = "发证日期/快递日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date issueTime; // (发证日期/快递日期)
    @ApiModelProperty(value = "创建人")
    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime; // ()
    @ApiModelProperty(value = "是否委托人")
    private String isConsigner ;//是否委托人
    @ApiModelProperty(value = "委托书附件id")
    private String consignerAttId;//委托书附件id
    private String rootOrgId;//根组织ID
}
