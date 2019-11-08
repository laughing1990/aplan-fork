package com.augurit.aplanmis.supermarket.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目发布采购需求vo
 */
@Data
@ApiModel("项目发布采购需求vo")
public class ImServiceItemPurchaseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务事项ID")
    private String serviceItemId;

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(value = "项目代码")
    private String localCode;

    @ApiModelProperty(value = "采购需求名称")
    private String purchaseName;

    @ApiModelProperty(value = "采购项目编码")
    private String purchaseCode;

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "申请流水号")
    private String applyinstCode;

    @ApiModelProperty(value = "业主单位ID")
    private String unitInfoId;

    @ApiModelProperty(value = "业主单位名称")
    private String applicant;

    @ApiModelProperty(value = "业主联系人ID")
    private String linkmanInfoId;

    @ApiModelProperty(value = "业主联系人姓名")
    private String linkmanName;

    @ApiModelProperty(value = "业主联系人电话")
    private String linkmanMobilePhone;

    @ApiModelProperty(value = "竞价类型：1 随机中标，2 直接选取，3 网上竞价")
    private String biddingType;

    @ApiModelProperty(value = "是否确认金额，1 是 0 暂不作评估")
    private String isDefineAmount;

    @ApiModelProperty(value = "选取中介时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date choiceImunitTime;

    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    @ApiModelProperty(value = "最高价格（万元）")
    private String highestPrice;

    @ApiModelProperty(value = "最低价格（万元）")
    private String basePrice;

    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm;

    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding;

    @ApiModelProperty(value = "服务选择条件：1 多个服务具备其一，0 多个服务都具备")
    private String selectCondition;

    @ApiModelProperty(value = "项目规模")
    private String projScale;

    @ApiModelProperty(value = "项目规模内容")
    private String projScaleContent;

    @ApiModelProperty(value = "申报主体")
    private String applySubject;

    @ApiModelProperty(value = "服务内容")
    private String serviceContent;
}
