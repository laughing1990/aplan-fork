package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
/**
* -模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-08-03 10:37:41</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:37:41</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class AeaHiSmsSendItem  extends AeaHiSmsSendBean implements Serializable{

private static final long serialVersionUID = 1L;
        private String sendItemId; // ()
        private String sendItemCode; // ()
        private String isOnceSend; // ()
        private String sendApplyId; // ()
//        private String applyinstId; // ()
        private String iteminstId; // ()
        private String inoutinstId; // ()
       /* private String windowUserId; // ()
        private String windowUserName; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date windowHandleTime; // ()
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
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date issueTime; // (发证日期/快递日期)
        private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // ()
    private String rootOrgId;//根组织ID
    private String isConsigner ;//是否委托人
    private String consignerAttId;//委托书附件id*/
    //额外字段
    @ApiModelProperty(value = "是否出件")
    private boolean hadSmsSend = false;//是否出件
    @ApiModelProperty(value = "事项实例名称")
    private String iteminstName;//事项实例名称
}
