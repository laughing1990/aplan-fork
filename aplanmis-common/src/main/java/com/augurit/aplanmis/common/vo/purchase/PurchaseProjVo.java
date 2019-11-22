package com.augurit.aplanmis.common.vo.purchase;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value = "审批页采购项目信息")
public class PurchaseProjVo {
    @ApiModelProperty(value = "主键ID")
    private String projPurchaseId; // ()
    @ApiModelProperty(value = "服务项目ID")
    @FiledNameIs(filedValue = "服务项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "业主单位ID")

    @FiledNameIs(filedValue = "业主单位ID")
    private String publishUnitInfoId;
    @FiledNameIs(filedValue = "个人申报时采购人ID")
    private String publishLinkmanInfoId;

    @ApiModelProperty(value = "服务类型和中介服务事项关联ID")
    @FiledNameIs(filedValue = "服务类型和中介服务事项关联ID")
    private String serviceItemId;
    @ApiModelProperty(value = "最低价格（万元）")
    @FiledNameIs(filedValue = "最低价格（万元）")
    private String basePrice;
    @ApiModelProperty(value = "采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时")
    @FiledNameIs(filedValue = "采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时")
    private String auditFlag;
    @ApiModelProperty(value = "竞价天数")
    @FiledNameIs(filedValue = "竞价天数")
    private String bidNum;
    @ApiModelProperty(value = "竞价时间单位")
    @FiledNameIs(filedValue = "竞价时间单位")
    private String bjType;
    @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
    @FiledNameIs(filedValue = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
    private String biddingType;
    @FiledNameIs(filedValue = "备注")
    private String memo; // (备注)
    @ApiModelProperty(value = "最高价格（万元）")
    @FiledNameIs(filedValue = "最高价格（万元）")
    private String highestPrice;
    @ApiModelProperty(value = "服务时间说明")
    @FiledNameIs(filedValue = "服务时间说明")
    private String serviceTimeExplain;
    @ApiModelProperty(value = "服务内容")
    @FiledNameIs(filedValue = "服务内容")
    private String serviceContent;
    @ApiModelProperty(value = "业主联系人")
    @FiledNameIs(filedValue = "业主联系人")
    private String contacts;
    @ApiModelProperty(value = "联系电话")
    @FiledNameIs(filedValue = "联系电话")
    private String moblie;
    @ApiModelProperty(value = "发布时间")
    @FiledNameIs(filedValue = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date publishTime;
    @ApiModelProperty(value = "金额说明")
    @FiledNameIs(filedValue = "金额说明")
    private String amountExplain;
    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire;
    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否")
    private String isApproveProj;
    @ApiModelProperty(value = "关联的审批流水号")
    private String applyinstCode;
    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm;
    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding;
    @ApiModelProperty(value = "业主单位委托人")
    private String linkmanInfoId;
    //单位要求字段
    @ApiModelProperty(value = "中介机构要求信息ID")
    private String unitRequireId;
    @ApiModelProperty(value = "是否需要资质要求：1 需要，0 不需要")
    private String isQualRequire;

    @ApiModelProperty(value = "资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项")
    private String qualRequireType;
    private String qualRequire;
    @ApiModelProperty(value = "资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）")
    private String qualRequireExplain;

    @ApiModelProperty(value = "是否仅承诺服务：1 是，0 否")
    private String promiseService;
    @ApiModelProperty(value = "其他要求说明")
    private String otherRequireExplain;

    @ApiModelProperty(value = "是否需要执业/职业人员要求：1 需要，0 不需要")
    private String isRegisterRequire;
    @ApiModelProperty(value = "执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerTotal;
    @ApiModelProperty(value = "执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerRequire;

    @ApiModelProperty(value = "是否需要备案要求：1 需要，0 不需要")
    private String isRecordRequire;
    @ApiModelProperty(value = "备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）")
    private String recordRequireExplain;

    //采购项目信息
    @ApiModelProperty(value = "项目名称")
    private String projName;//项目名称
    private String projScale;//项目规模
    private String projScaleContent;
    @ApiModelProperty(value = "项目代码")
    private String localCode;//项目代码
    @ApiModelProperty(value = "是否为财政资金（资金来源）：1 是，0 不是")
    private String isFinancialFund;
    @ApiModelProperty(value = "财政资金比例")
    private String financialFundProportion;
    @ApiModelProperty(value = "是否为社会资金（资金来源）：1 是，0 不是")
    private String isSocialFund;
    @ApiModelProperty(value = "社会资金比例")
    private String socialFundProportion;
    //中介服务信息
    @ApiModelProperty(value = "服务名称")
    private String serviceName;


}