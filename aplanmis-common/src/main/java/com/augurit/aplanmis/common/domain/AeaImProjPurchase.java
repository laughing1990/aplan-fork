package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 项目发布-模型[New]
 */
@Data
@ApiModel("采购需求表实体")
public class AeaImProjPurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @FiledNameIs(filedValue = "主键ID")
    @ApiModelProperty(value = "主键ID")
    private String projPurchaseId; // ()
    @ApiModelProperty(value = "服务项目ID")
    @FiledNameIs(filedValue = "服务项目ID")
    private String projInfoId; // (服务项目ID)
    @ApiModelProperty(value = "业主单位ID")

    @FiledNameIs(filedValue = "业主单位ID")
    private String publishUnitInfoId; // (业主单位ID)
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
    @FiledNameIs(filedValue = "是否删除：1 已删除，0 未删除")
    private String isDelete;
    @FiledNameIs(filedValue = "是否启用：1 已启用，0 未启用")
    private String isActive;
    @FiledNameIs(filedValue = "创建人")
    private String creater;
    @FiledNameIs(filedValue = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    @FiledNameIs(filedValue = "修改人")
    private String modifier;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FiledNameIs(filedValue = "修改时间")
    private java.util.Date modifyTime;
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
    @FiledNameIs(filedValue = "是否为阶段：1 是，0 否")
    private String isStage;
    @FiledNameIs(filedValue = "阶段ID")
    private String stageId;
    @FiledNameIs(filedValue = "阶段下的事项ID")
    private String stageItemVerId;
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
    @FiledNameIs(filedValue = "是否为投资审批项目：1 是，0 否")
    private String isApproveProj;
    @ApiModelProperty(value = "关联的审批流水号")
    @FiledNameIs(filedValue = "关联的审批流水号")
    private String applyinstCode;
    @ApiModelProperty(value = "是否现场见证：1 是， 0 否")
    @FiledNameIs(filedValue = "是否现场见证：1 是， 0 否")
    private String isLiveWitness;
    @ApiModelProperty(value = "见证者1姓名")
    @FiledNameIs(filedValue = "见证者1姓名")
    private String witnessName1;
    @ApiModelProperty(value = "见证者2姓名")
    @FiledNameIs(filedValue = "见证者2姓名")
    private String witnessName2;
    @ApiModelProperty(value = "见证者3姓名")
    @FiledNameIs(filedValue = "见证者3姓名")
    private String witnessName3;
    @ApiModelProperty(value = "见证者1电话")
    @FiledNameIs(filedValue = "见证者1电话")
    private String witnessPhone1;
    @ApiModelProperty(value = "见证者2电话")
    @FiledNameIs(filedValue = "见证者2电话")
    private String witnessPhone2;
    @ApiModelProperty(value = "见证者3电话")
    @FiledNameIs(filedValue = "见证者3电话")
    private String witnessPhone3;
    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    @FiledNameIs(filedValue = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm;
    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    @FiledNameIs(filedValue = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    @FiledNameIs(filedValue = "审核时间")
    private java.util.Date auditTime;
    @ApiModelProperty(value = "审核意见")
    @FiledNameIs(filedValue = "审核意见")
    private String auditOpinion;
    @ApiModelProperty(value = "业主投诉电话")
    @FiledNameIs(filedValue = "业主投诉电话")
    private String ownerComplaintPhone;
    @ApiModelProperty(value = "中介机构要求信息ID")
    @FiledNameIs(filedValue = "中介机构要求信息ID")
    private String unitRequireId;
    @ApiModelProperty(value = "批文文件")
    @FiledNameIs(filedValue = "批文文件")
    private String officialRemarkFile;
    @ApiModelProperty(value = "要求说明文件")
    @FiledNameIs(filedValue = "要求说明文件")
    private String requireExplainFile;
    @ApiModelProperty(value = "服务选择条件 1 多个服务具备其一，0 多个服务都具备")
    @FiledNameIs(filedValue = "服务选择条件")
    private String selectCondition;
    @ApiModelProperty(value = "是否确认金额，1 是 0 否")
    @FiledNameIs(filedValue = "是否确认金额，1 是 0 否")
    private String isDefineAmount;
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date expirationDate;
    @ApiModelProperty(value = "业主委托人信息ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "选取中介时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date choiceImunitTime;
    @ApiModelProperty(value = "服务开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date serviceStartTime;
    @ApiModelProperty(value = "服务结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date serviceEndTime;

    @ApiModelProperty(value = "最小下浮率%")
    private String baseRate;

    @ApiModelProperty(value = "最大下浮率%")
    private String highestRate;

    @ApiModelProperty(value = "报价方式,0 金额 1 下浮率")
    private String quoteType;

    @ApiModelProperty(value = "是否有企业回避：1 是，0 否")
    private String isAvoid;

    @ApiModelProperty(value = "【当IS_AVOID=1时必填】回避原因")
    private String avoidReason;

    //拓展地段-采购项目信息
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "事项名称，中介事项")
    private String itemName;
    private String itemBasicId;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty("采购项目规模")
    private String projScale;
    @ApiModelProperty("采购项目规模内容")
    private java.lang.String projScaleContent;

    private String priority;//要求资质
    private String realPrice;//中介竞价价格
    @ApiModelProperty(value = "业主名称")
    private String applicant;//业主名称
    @ApiModelProperty(value = "服务名称")
    private String serviceName;
    private String serviceId;
    private String purchaseImgUrl;

    //申请实例ID
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "部门名称")
    private String orgName;//部门名称
    @ApiModelProperty(value = "涉及的行政事项")
    private String parentItemName;// 涉及的行政事项
    @ApiModelProperty(value = "投资审批项目名称")
    private String parentProjName;// 投资审批项目名称
    @ApiModelProperty(value = "投资审批项目编码")
    private String parentLocalCode;// 投资审批项目编码
    @ApiModelProperty(value = "是否为财政资金（资金来源）：1 是，0 不是")
    private String isFinancialFund;
    @ApiModelProperty(value = "财政资金比例")
    private String financialFundProportion;
    @ApiModelProperty(value = "是否为社会资金（资金来源）：1 是，0 不是")
    private String isSocialFund; // (是否为社会资金（资金来源）：1 是，0 不是)
    @ApiModelProperty(value = "社会资金比例")
    private String socialFundProportion; // (社会资金比例：当 IS_SOCIAL_FUND = 1，必填)

    private String selectedEndTime;//选取结束时间
    private String selectedApplicant;//中选机构名称
    private String selectedUnitInfoId;//中选机构id
    private String selectedAppAddr;//中选机构地址
    private String financialSource;//资金来源
    private List<AeaImUnitBidding> unitBiddingList;//中介机构列表
    private String isUploadContract;//是否上传合同：1 是，0 否
    private String isOwnerUpload;//是否业主上传： 1 是， 0 否（中介机构上传）
    private String isConfirm;//是否已确认：1 是， 0 否
    private String isUploadResult;//是否上传服务结果：1 是，0 否
    private String proType;//菜单按钮编号
    private String contractId;//合同Id
    private AeaImContract contract;//合同实体
    private String serviceResultId;//服务结果Id
    private AeaImServiceResult serviceResult;//服务结果实体
    private String unitBiddingId;
    private String serviceEvaluationId;
    private String rootOrgId;//根组织ID
//    private String realPrice;//服务金额
//    private String serviceItemName;//中介服务事项名称

    //单位要求字段
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


    /*查询状态数组*/
    private String[] auditFlags;
    /*查询关键字*/
    private String keyword;

    private java.util.Date lastBiddingTime;// 最后竞价时间

    private String avoidUnitInfos;// 回避机构

    private List<BscAttForm> officialRemarkBscAttForms;
    private List<BscAttForm> requireExplainBscAttForms;

    private String taskId;
    private String processinstId;
    private String isOwnFile;
    private String operateDescribe;
    private String operateAction;

    public AeaImProjPurchase() {
        this.isDelete = "0";
        this.isActive = "1";
    }

    public AeaImProjPurchase(String auditFlag, String rootOrgId) {
        this.auditFlag = auditFlag;
        this.rootOrgId = rootOrgId;
        this.isDelete = "0";
        this.isActive = "1";
    }


    /**
     * 竞价类型：1 随机中标，2 自主选择 3 竞价选取
     */
    public enum BiddingType {
        随机中标("1"),
        自主选择("2"),
        竞价选取("3");

        private String type;

        BiddingType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}

