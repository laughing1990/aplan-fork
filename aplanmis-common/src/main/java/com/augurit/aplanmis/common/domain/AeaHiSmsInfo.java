package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:22</li>
 * </ul>
 */
@Data
public class AeaHiSmsInfo implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String id; // ()
    private String applyinstId; // (申请实例ID)
    private String receiveMode; // (领取模式：1 窗口取证  0 邮政快递)
    private String orderId; // (订单号)
    private String expressNum; // (运单号)
    private String addresseeName; // (收件人名字)
    private String addresseePhone; // (收件人号码)
    private String addresseeIdcard; // (收件人身份证)
    private String addresseeAddr; // (收件人所在地址)
    private String addresseeProvince; // (收件人所在省)
    private String addresseeCity; // (收件人所在城市)
    private String addresseeCounty; // (收件人所在区县)
    private String senderName; // (寄件人名字)
    private String senderPhone; // (寄件人电话)
    private String senderAddr; // (寄件人地址)
    private String senderProvince; // (寄件人所在省)
    private String senderCity; // (寄件人所在城市)
    private String senderCounty; // (寄件人所在区县)
    private String smsType; // (快递类型：1 普通快递  2 上门取件)
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date issueTime; // (发证日期/快递日期)
    private String rootOrgId;//根组织ID

    //额外字段
    private String isConsigner;//是否委托人
}
