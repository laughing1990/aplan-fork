package com.augurit.aplanmis.common.service.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("回执信息实体vo")
public class AeaHiReceiveVo {

    /* AEA_HI_RECEIVE字段 start */

    @ApiModelProperty(value = "回执ID")
    private java.lang.String receiveId;

    @ApiModelProperty(value = "申请实例ID")
    private java.lang.String applyinstId;

    @ApiModelProperty(value = "输出实例ID")
    private java.lang.String outinstId;

    @ApiModelProperty(value = "领取人姓名")
    private java.lang.String receiveUserName;

    @ApiModelProperty(value = "领取人证件号码")
    private java.lang.String receiveCertNo;

    @ApiModelProperty(value = "领取时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date receiveTime;

    @ApiModelProperty(value = "领取登记部门所在的行政区域代码")
    private java.lang.String approveDeptRegion;

    @ApiModelProperty(value = "分发至下级部门的行政区域代码")
    private java.lang.String subDeptRegion;

    @ApiModelProperty(value = "备注")
    private java.lang.String receiveMemo;

    @ApiModelProperty(value = "创建人")
    private java.lang.String creater;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人")
    private java.lang.String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "领证人手机号码")
    private java.lang.String receiveUserMobile;

    @ApiModelProperty(value = "收件地址")
    private java.lang.String serviceAddress;

    @ApiModelProperty(value = "回执类型")
    private String receiptType;

    @ApiModelProperty(value = "文书号")
    private String documentNum;

    @ApiModelProperty(value = "文书名称")
    private String documentName;

    @ApiModelProperty(value = "公司名称或法人名")
    private String applicant;//公司名称或法人名

    @ApiModelProperty(value = "申报编码")
    private String applyinstCode;

    @ApiModelProperty(value = "申报来源，net表示网上申报，win表示窗口申报")
    private String applyinstSource;

    @ApiModelProperty(value = "是否串联审批，0表示并联审批，1表示串联审批")
    private String isSeriesApprove;

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(value = "项目名称")
    private String projName;

    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;

    @ApiModelProperty(value = "实例状态")
    private String iteminstState;

    @ApiModelProperty(value = "所属主题版本ID")
    private String themeVerId;

    @ApiModelProperty(value = "所属阶段定义ID")
    private String stageId;

    @ApiModelProperty(value = "状态")
    private String stageinstState;

    @ApiModelProperty(value = "开普返回的回执文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;

    @ApiModelProperty(value = "回执类型名称-查询数据字典获得")
    private String receiveTypeName;

    /* AEA_HI_RECEIVE字段 end */

    @ApiModelProperty(value = "事项实例（办件）名称")
    private String iteminstName;

    @ApiModelProperty(value = "打印时间")
    private String printTime;

    @ApiModelProperty(value = "承诺办结时限数字")
    private Double dueNum;

    @ApiModelProperty(value = "承诺办结时限单位")
    private String dueUnit;

    @ApiModelProperty(value = "事项名称")
    private String itemName;

    @ApiModelProperty(value = "时限")
    private String timeLimit;

    @ApiModelProperty(value = "所有材料")
    private List<AeaHiItemMatinst> allMatList;

    @ApiModelProperty(value = "纸质材料")
    private List<AeaHiItemMatinst> paperMatList;

    @ApiModelProperty(value = "电子材料")
    private List<AeaHiItemMatinst> attrMatList;

    @ApiModelProperty(value = "收件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;

    @ApiModelProperty(value = "窗口收件人")
    private String tellerName;

    @ApiModelProperty(value = "接收人单位")
    private String receiveOrgName;


    @ApiModelProperty(value = "经办企业证件号")
    private String agentIdCard;

    @ApiModelProperty(value = "经办企业")
    private String agentName;

    @ApiModelProperty(value = "经办人手机号")
    private String agentLinkmanTel;

    @ApiModelProperty(value = "经办人姓名")
    private String agentLinkmanName;

    @ApiModelProperty(value = "经办人身份证号")
    private String agentLinkmanIDCard;

    @ApiModelProperty(value = "建设单位证件号")
    private String applicantIDCard;

    @ApiModelProperty(value = "部门简称")
    private String orgShortName;

    @ApiModelProperty(value = "窗口人员签字")
    private String winName;


    @ApiModelProperty(value = "领取方式")
    private String receiveMode;

    @ApiModelProperty(value = "领证人")
    private String issueUserName;

    @ApiModelProperty(value = "领证人手机号码")
    private String issueUserMobile;


    @ApiModelProperty(value = "寄件人姓名")
    private String senderName;

    @ApiModelProperty(value = "寄件人手机号码")
    private String senderPhone;

    @ApiModelProperty(value = "寄件人省份")
    private String senderProvince;

    @ApiModelProperty(value = "寄件人城市")
    private String senderCity;

    @ApiModelProperty(value = "寄件人县区")
    private String senderCounty;

    @ApiModelProperty(value = "寄件人地址")
    private String senderAddr;


    @ApiModelProperty(value = "收件人名字")
    private String addresseeName;

    @ApiModelProperty(value = "收件人号码")
    private String addresseePhone;

    @ApiModelProperty(value = "收件人身份证")
    private String addresseeIdcard;

    @ApiModelProperty(value = "法人")
    private String idrepresentative;

    @ApiModelProperty(value = "法人证件号")
    private String idno;

    @ApiModelProperty(value = "法人联系方式")
    private String idmobile;

    @ApiModelProperty(value = "经办人身份证号")
    private String applicantDetailSite;

}
