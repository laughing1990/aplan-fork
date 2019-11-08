package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("现场登记，并联申报--> 不收件或不受理回执")
public class RefusedRecepitVo {
    @ApiModelProperty(name = "receiveId", value = "回执id，不用传")
    private String receiveId;
    @ApiModelProperty(name = "themeVerId", value = "主题版本id， 用于并联申报不受理")
    private String themeVerId;
    @ApiModelProperty(name = "stageId", value = "阶段id， 用于并联申报不受理")
    private String stageId;
    @ApiModelProperty(name = "stageinstState", value = "阶段实例状态， 用于并联申报不受理")
    private String stageinstState;
    @ApiModelProperty(name = "itemVerId", value = "事项版本id， 用于现场登记不收件")
    private String itemVerId;
    @ApiModelProperty(name = "applyinstSource", value = "申报来源，net表示网上申报，win表示窗口申报", allowableValues = "net, win")
    private String applyinstSource;
    @ApiModelProperty(name = "isSeriesApprove", value = "是否串联审批，0表示并联审批，1表示串联审批", allowableValues = "0, 1")
    private String isSeriesApprove;
    @ApiModelProperty(name = "projInfoId", value = "项目id")
    private String projInfoId;
    @ApiModelProperty(name = "projName", value = "项目名称")
    private String projName;
    @ApiModelProperty(name = "linkmanInfoId", value = "联系人id")
    private String linkmanInfoId;
    @ApiModelProperty(name = "iteminstState", value = "事项状态")
    private String iteminstState;
    @ApiModelProperty(name = "receiptType", value = "回执类型")
    private String receiptType;

    @ApiModelProperty(name = "receiveCertNo", value = "领取人证件号码")
    private String receiveCertNo;
    @ApiModelProperty(name = "receiveUserName", value = "领取人姓名")
    private String receiveUserName;
    @ApiModelProperty(name = "receiveUserMobile", value = "领证人手机号码")
    private String receiveUserMobile;
    @ApiModelProperty(name = "receiveMode", value = "领取方式")
    private String receiveMode;

    @ApiModelProperty(name = "serviceAddress", value = "收件地址")
    private String serviceAddress;
    @ApiModelProperty(name = "issueUserMobile", value = "领证人手机号码")
    private String issueUserMobile;
    @ApiModelProperty(name = "receiveMemo", value = "备注")
    private String receiveMemo;
    @ApiModelProperty(name = "selectTable", value = "选择的材料编码")
    private String selectTable;

}
