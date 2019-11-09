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
    private String basePrice; // (最低价格（万元）)
    @ApiModelProperty(value = "采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时")
    @FiledNameIs(filedValue = "采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时")
    private String auditFlag; // 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
    @ApiModelProperty(value = "竞价天数")
    @FiledNameIs(filedValue = "竞价天数")
    private String bidNum; // (竞价天数)
    @ApiModelProperty(value = "竞价时间单位")
    @FiledNameIs(filedValue = "竞价时间单位")
    private String bjType; // (竞价时间单位)
    @FiledNameIs(filedValue = "是否删除：1 已删除，0 未删除")
    private String isDelete; // (是否删除：1 已删除，0 未删除)
    @FiledNameIs(filedValue = "是否启用：1 已启用，0 未启用")
    private String isActive; // (是否启用：1 已启用，0 未启用)
    @FiledNameIs(filedValue = "创建人")
    private String creater; // ()
    @FiledNameIs(filedValue = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // ()
    @FiledNameIs(filedValue = "修改人")
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FiledNameIs(filedValue = "修改时间")
    private java.util.Date modifyTime; // ()
    @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
    @FiledNameIs(filedValue = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
    private String biddingType;
    @FiledNameIs(filedValue = "备注")
    private String memo; // (备注)
    @ApiModelProperty(value = "最高价格（万元）")
    @FiledNameIs(filedValue = "最高价格（万元）")
    private String highestPrice; // (最高价格（万元）)
    @ApiModelProperty(value = "服务时间说明")
    @FiledNameIs(filedValue = "服务时间说明")
    private String serviceTimeExplain; // (服务时间说明)
    @ApiModelProperty(value = "服务内容")
    @FiledNameIs(filedValue = "服务内容")
    private String serviceContent; // (服务内容)
    @ApiModelProperty(value = "业主联系人")
    @FiledNameIs(filedValue = "业主联系人")
    private String contacts; // (业主联系人)
    @ApiModelProperty(value = "联系电话")
    @FiledNameIs(filedValue = "联系电话")
    private String moblie; // (联系电话)
    @FiledNameIs(filedValue = "是否为阶段：1 是，0 否")
    private String isStage; // (是否为阶段：1 是，0 否)
    @FiledNameIs(filedValue = "阶段ID")
    private String stageId; // (阶段ID)
    @FiledNameIs(filedValue = "阶段下的事项ID")
    private String stageItemVerId; // (阶段下的事项ID)
    @ApiModelProperty(value = "发布时间")
    @FiledNameIs(filedValue = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date publishTime; // (发布时间)
    @ApiModelProperty(value = "金额说明")
    @FiledNameIs(filedValue = "金额说明")
    private String amountExplain; // (金额说明)
    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire; // (资质备案说明)
    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否")
    @FiledNameIs(filedValue = "是否为投资审批项目：1 是，0 否")
    private String isApproveProj; // (是否为投资审批项目：1 是，0 否)
    @ApiModelProperty(value = "关联的审批流水号")
    @FiledNameIs(filedValue = "关联的审批流水号")
    private String applyinstCode; // (关联的审批流水号)
    @ApiModelProperty(value = "是否现场见证：1 是， 0 否")
    @FiledNameIs(filedValue = "是否现场见证：1 是， 0 否")
    private String isLiveWitness; // (是否现场见证：1 是， 0 否)
    @ApiModelProperty(value = "见证者1姓名")
    @FiledNameIs(filedValue = "见证者1姓名")
    private String witnessName1; // (见证者1姓名)
    @ApiModelProperty(value = "见证者2姓名")
    @FiledNameIs(filedValue = "见证者2姓名")
    private String witnessName2; // (见证者2姓名)
    @ApiModelProperty(value = "见证者3姓名")
    @FiledNameIs(filedValue = "见证者3姓名")
    private String witnessName3; // (见证者3姓名)
    @ApiModelProperty(value = "见证者1电话")
    @FiledNameIs(filedValue = "见证者1电话")
    private String witnessPhone1; // (见证者1电话)
    @ApiModelProperty(value = "见证者2电话")
    @FiledNameIs(filedValue = "见证者2电话")
    private String witnessPhone2; // (见证者2电话)
    @ApiModelProperty(value = "见证者3电话")
    @FiledNameIs(filedValue = "见证者3电话")
    private String witnessPhone3; // (见证者3电话)
    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    @FiledNameIs(filedValue = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm; // (是否公示中选机构： 1 是， 0 否)
    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    @FiledNameIs(filedValue = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding; // (是否公示中标公告：1 是， 0 否)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    @FiledNameIs(filedValue = "审核时间")
    private java.util.Date auditTime; // (审核时间)
    @ApiModelProperty(value = "审核意见")
    @FiledNameIs(filedValue = "审核意见")
    private String auditOpinion; // (审核意见)
    @ApiModelProperty(value = "业主投诉电话")
    @FiledNameIs(filedValue = "业主投诉电话")
    private String ownerComplaintPhone; // (业主投诉电话)
    @ApiModelProperty(value = "中介机构要求信息ID")
    @FiledNameIs(filedValue = "中介机构要求信息ID")
    private String unitRequireId; // 中介机构要求信息ID
    @ApiModelProperty(value = "批文文件")
    @FiledNameIs(filedValue = "批文文件")
    private String officialRemarkFile; // 批文文件
    @ApiModelProperty(value = "要求说明文件")
    @FiledNameIs(filedValue = "要求说明文件")
    private String requireExplainFile; // 要求说明文件
    @ApiModelProperty(value = "服务选择条件 1 多个服务具备其一，0 多个服务都具备")
    @FiledNameIs(filedValue = "服务选择条件")
    private String selectCondition;//服务选择条件：1 多个服务具备其一，0 多个服务都具备
    @ApiModelProperty(value = "是否确认金额，1 是 0 否")
    @FiledNameIs(filedValue = "是否确认金额，1 是 0 否")
    private String isDefineAmount;//是否确认金额，1 是 0 否
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date expirationDate;//截止日期
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

    //拓展地段
    @ApiModelProperty(value = "项目名称")
    private String projName;//项目名称
    @ApiModelProperty(value = "事项名称，中介事项")
    private String itemName;//事项名称，中介事项
    @ApiModelProperty(value = "项目代码")
    private String localCode;//项目代码
    private String priority;//要求资质
    private String realPrice;//中介竞价价格
    @ApiModelProperty(value = "业主名称")
    private String applicant;//业主名称
    @ApiModelProperty(value = "服务名称")
    private String serviceName;//服务名称
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
    private String isFinancialFund; // (是否为财政资金（资金来源）：1 是，0 不是)
    @ApiModelProperty(value = "财政资金比例")
    private String financialFundProportion; // (财政资金比例：当 IS_FINANCIAL_FUND = 1，必填)
    @ApiModelProperty(value = "是否为社会资金（资金来源）：1 是，0 不是")
    private String isSocialFund; // (是否为社会资金（资金来源）：1 是，0 不是)
    @ApiModelProperty(value = "社会资金比例")
    private String socialFundProportion; // (社会资金比例：当 IS_SOCIAL_FUND = 1，必填)

    private String selectedEndTime;//选取结束时间
    private String selectedApplicant;//中选机构名称
    private String selectedUnitInfoId;//中选机构id
    private String selectedAppAddr;//中选机构地址
    private String projScale;//项目规模
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

