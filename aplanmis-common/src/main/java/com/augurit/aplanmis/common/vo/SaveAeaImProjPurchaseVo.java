package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import com.augurit.aplanmis.common.domain.AeaImMajorQual;
import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author tiantian
 * @date 2019/6/11
 */
@Data
@ApiModel("新增采购需求")
public class SaveAeaImProjPurchaseVo {

    @ApiModelProperty(value = "采购需求ID")
    private String projPurchaseId;

    @ApiModelProperty(value = "服务项目ID")
    private String projInfoId;

    @NotBlank(message = "服务类型和中介服务事项关联ID不能为空")
    @ApiModelProperty(value = "服务类型和中介服务事项关联ID，多个用,隔开", required = true)
    private String serviceItemId;

    @NotBlank(message = "服务类型ID不能为空")
    @ApiModelProperty(value = "服务类型ID", required = true)
    private String serviceId;

    @ApiModelProperty(value = "最低价格（万元）", required = true)
    private String basePrice;

    @ApiModelProperty(value = "竞价天数")
    private String bidNum;
    @ApiModelProperty(value = "竞价时间单位")
    private String bjType;

    @NotBlank(message = "竞价类型不能为空")
    @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择 3 竞价选取", required = true)
    private String biddingType;
    @ApiModelProperty(value = "备注")
    private String memo; // (备注)
    @ApiModelProperty(value = "最高价格（万元）")
    private String highestPrice;

    @NotBlank(message = "服务时间说明不能为空")
    @ApiModelProperty(value = "服务时间说明", required = true)
    private String serviceTimeExplain;

    @NotBlank(message = "服务内容不能为空")
    @ApiModelProperty(value = "服务内容", required = true)
    private String serviceContent;

    @NotBlank(message = "业主联系人不能为空")
    @ApiModelProperty(value = "业主联系人", required = true)
    private String contacts;

    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话", required = true)
    private String moblie;
    @ApiModelProperty(value = "金额说明")
    private String amountExplain;

    @NotBlank(message = "是否为投资审批项目不能为空")
    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否", required = true)

    private String isApproveProj;
    @ApiModelProperty(value = "关联的审批流水号")
    private String applyinstCode;
    @ApiModelProperty(value = "是否现场见证：1 是， 0 否")
    private String isLiveWitness = "0";
    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm;
    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding;

    @NotBlank(message = "业主投诉电话不能为空")
    @ApiModelProperty(value = "业主投诉电话", required = true)
    private String ownerComplaintPhone;

    @ApiModelProperty(value = "是否确认金额，1 是 0 否")
    private String isDefineAmount;

    @NotNull(message = "采购项目信息不能为空")
    @Valid
    @ApiModelProperty(value = "采购项目", required = true)
    private SaveAeaProjInfoVo saveAeaProjInfoVo;

    @NotNull(message = "机构要求不能为空")
    @Valid
    @ApiModelProperty(value = "机构要求")
    private AeaImUnitRequire aeaImUnitRequire;

    @ApiModelProperty(value = "专业要求")
    private List<AeaImMajorQual> aeaImMajorQuals;

    @FiledNameIs(filedValue = "服务选择条件")
    private String selectCondition;//服务选择条件：1 多个服务具备其一，0 多个服务都具备

    @ApiModelProperty(value = "所选中介机构id")
    private String agentUnitInfoId;

    @ApiModelProperty(value = "截止日期 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date expirationDate;//截止日期


    @ApiModelProperty(value = "选取中介时间 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date choiceImunitTime;

    @ApiModelProperty(value = "业主委托人信息ID")
    private String linkmanInfoId;

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

    @ApiModelProperty(value = "【当IS_AVOID=1时必填】回避单位ID，多个,隔开")
    private String avoidUnitInfoIds;
    @ApiModelProperty(value = "批文文件")
    private String officialRemarkFile;
    @ApiModelProperty(value = "要求说明文件")
    private String requireExplainFile;

    //事项信息
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource = "net";
    @ApiModelProperty(value = "材料实例ID集合", required = true)
    private String[] matinstsIds;
    private String creater;
    private String rootOrgId;

    public ImPurchaseData createPurchaseData(String applyinstId, String applyinstCode) {
        ImPurchaseData purchaseData = new ImPurchaseData();
        BeanUtils.copyProperties(this, purchaseData);
        purchaseData.setApproveProjInfoId(this.projInfoId);
        purchaseData.setProjPurchaseId(UUID.randomUUID().toString());

        return purchaseData;
    }

    public ImItemApplyData createItemApplyData() {
        ImItemApplyData applyData = new ImItemApplyData();
        BeanUtils.copyProperties(this, applyData);
        return applyData;
    }

    //创建采购项目信息
    public AeaProjInfo createAeaProjInfo() {
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        BeanUtils.copyProperties(this.saveAeaProjInfoVo, aeaProjInfo);
        aeaProjInfo.setRootOrgId(this.rootOrgId);
        aeaProjInfo.setCreater(this.creater);
        return aeaProjInfo;
    }


    @Data
    @ApiModel("采购项目信息")
    public static class SaveAeaProjInfoVo {

        @ApiModelProperty(value = "审批项目编码")
        @FiledNameIs(filedValue = "审批项目编码")
        private String approvalCode;

        @ApiModelProperty(value = "审批项目id")
        @FiledNameIs(filedValue = "审批项目id")
        private String parentProjId;

        @NotBlank(message = "项目编码不能为空")
        @ApiModelProperty(value = "地方编码", required = true)
        @FiledNameIs(filedValue = "地方编码")
        private String localCode;

        @NotBlank(message = "项目名称不能为空")
        @ApiModelProperty(value = "项目名称", required = true)
        @FiledNameIs(filedValue = "项目名称")
        private String projName;

        @ApiModelProperty(value = "是否为采购项目", notes = "1 是，0 否（投资审批项目）")
        private String isPurchaseProj;

        @NotBlank(message = "项目规模不能为空")
        @ApiModelProperty(value = "项目规模", required = true)
        @FiledNameIs(filedValue = "项目规模")
        private String projScale; // (项目规模)

        @NotBlank(message = "项目规模内容不能为空")
        @ApiModelProperty(value = "项目规模内容", required = true)
        @FiledNameIs(filedValue = "项目规模内容")
        private String projScaleContent;

        @NotBlank(message = "是否为财政资金不能为空")
        @ApiModelProperty(value = "是否为财政资金（资金来源)", notes = "1 是，0 不是", required = true)
        @FiledNameIs(filedValue = "是否为财政资金（资金来源）：1 是，0 不是")
        private String isFinancialFund;

        @ApiModelProperty(value = "财政资金比例", notes = "当 IS_FINANCIAL_FUND = 1，必填")
        @FiledNameIs(filedValue = "财政资金比例：当 IS_FINANCIAL_FUND = 1，必填")
        private String financialFundProportion;

        @NotBlank(message = "是否为社会资金不能为空")
        @ApiModelProperty(value = "是否为社会资金（资金来源）", notes = "1 是，0 不是")
        @FiledNameIs(filedValue = "是否为社会资金（资金来源）：1 是，0 不是")
        private String isSocialFund; // (是否为社会资金（资金来源）：1 是，0 不是)

        @ApiModelProperty(value = "社会资金比例", notes = "当 IS_SOCIAL_FUND = 1，必填")
        @FiledNameIs(filedValue = "社会资金比例：当 IS_SOCIAL_FUND = 1，必填")
        private String socialFundProportion;

    }
}
