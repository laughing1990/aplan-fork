package com.augurit.aplanmis.front.approve.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 申请事项信息
 * 申请项目，申请人单位，申报流水号，事项名称，申请时间，前置条件，情形
 */
@Data
@ApiModel("申请事项信息")
public class AeaHiItemInfoVo {

    @ApiModelProperty(value = "申请项目", required = true, dataType = "string")
    private String projName;

    @ApiModelProperty(value = "申请人单位", required = true, dataType = "string")
    private String applyUnit;

    @ApiModelProperty(value = "申请人证件号", required = true, dataType = "string")
    private String applyIDCard;

    @ApiModelProperty(value = "经办人单位", required = true, dataType = "string")
    private String agency;

    @ApiModelProperty(value = "经办人证件号", required = true, dataType = "string")
    private String agencyIDCard;

    @ApiModelProperty(value = "联系人", required = true, dataType = "string")
    private String contact;

    @ApiModelProperty(value = "联系人证件号", required = true, dataType = "string")
    private String contactIdNo;

    @ApiModelProperty(value = "联系人手机", required = true, dataType = "string")
    private String contactMobile;

    @ApiModelProperty(value = "联系人邮箱", required = true, dataType = "string")
    private String contactEmail;

    @ApiModelProperty(value = "经办联系人", required = true, dataType = "string")
    private String agencyContact;

    @ApiModelProperty(value = "经办联系人证件号", required = true, dataType = "string")
    private String agencyContactIdNo;

    @ApiModelProperty(value = "经办联系人手机", required = true, dataType = "string")
    private String agencyContactMobile;

    @ApiModelProperty(value = "经办联系人邮箱", required = true, dataType = "string")
    private String agencyContactEmail;

    @ApiModelProperty(value = "申报流水号", required = true, dataType = "string")
    private String applyNo;

    @ApiModelProperty(value = "事项名称", required = true, dataType = "string")
    private String itemName;

    @ApiModelProperty(value = "申请时间", required = true, dataType = "date")
    private Date applyDate;

    @ApiModelProperty(value = "事项定义ID，用来查找情形和激活条件(前置条件)", required = true, dataType = "string")
    private String itemId;

    @ApiModelProperty(value = "事项编码", required = true, dataType = "string")
    private String iteminstCode;
    @ApiModelProperty(value = "事项实例ID", required = true, dataType = "string")

    //itemInstId和projInfoId用来展示需索材料
    private String itemInstId;

    @ApiModelProperty(value = "登记时间", required = true, dataType = "string")
    private String projInfoId;

    @ApiModelProperty(value = "项目代码", required = true, dataType = "string")
    private String projCode;

    @ApiModelProperty(value = "办件类型", required = true, dataType = "string")
    private String itemProperty;

    @ApiModelProperty(value = "是否串联审批，0表示并联审批，1表示串联审批", required = true, dataType = "string", allowableValues = "0,1")
    private String isSeries;

    @ApiModelProperty(value = "实施编码", required = true, dataType = "string")
    private String ssCode;

    @ApiModelProperty(value = "分局承办单位", required = true, dataType = "string")
    private String branchOrgName;

    @ApiModelProperty(value = "单项办件类型", required = true, dataType = "string")
    private String itemVerId;
}
