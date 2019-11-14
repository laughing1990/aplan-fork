package com.augurit.aplanmis.front.supermarket.vo;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import com.augurit.aplanmis.common.domain.AeaImMajorQual;
import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@ApiModel("中介事项申报参数实体vo")
public class AgentItemApplyData {
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource;
    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "模板ID", hidden = true)
    private String appId;
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    //    @ApiModelProperty(value = "分局承办；并行推进事项分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
//    private String branchOrgMap;
    @ApiModelProperty(value = "审批项目ID", required = true)
    private String projInfoId;
//    @ApiModelProperty(value = "经办单位ID集合", hidden = true)
//    private String[] handleUnitIds;

    @ApiModelProperty(value = "建设单位Map集合，key为projInfoId,格式为[{projInfoId1:[unitId1,unitId1]}]", required = true)
    private List<BuildProjUnitVo> buildProjUnitMap;
    @ApiModelProperty(value = "材料实例ID集合", required = true)
    private String[] matinstsIds;
    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;
    @ApiModelProperty(value = "申请联系人ID,", notes = "当申报主体为个人时：必输")
    private String applyLinkmanId;

//    @ApiModelProperty(value = "情形ID集合", hidden = true)
//    private String[] stateIds;
//    @ApiModelProperty(value = "是否并行推行：0表示否，1表示是,", hidden = true)
//    private String isParallel;
//    @ApiModelProperty(value = "并行推进阶段ID", hidden = true)
//    private String stageId;

    //采购项目信息
    @ApiModelProperty(value = "采购需求ID", hidden = true)
    private String projPurchaseId;

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
    private String memo;
    @ApiModelProperty(value = "最高价格（万元）")
    private String highestPrice;

    @NotBlank(message = "服务时间说明不能为空")
    @ApiModelProperty(value = "服务时间说明", required = true)
    private String serviceTimeExplain;

    @NotBlank(message = "服务内容不能为空")
    @ApiModelProperty(value = "服务内容", required = true)
    private String serviceContent;

    @NotBlank(message = "业主联系人不能为空")
    @ApiModelProperty(value = "业主联系人,格式：姓名[证件号]", required = true)
    private String contacts;

    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话", required = true)
    private String moblie;
    @ApiModelProperty(value = "金额说明")
    private String amountExplain;

    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否", required = true)
    private String isApproveProj;

    @ApiModelProperty(value = "关联的审批流水号", hidden = true)
    private String applyinstCode;

    @ApiModelProperty(value = "是否现场见证：1 是， 0 否", hidden = true)
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

    @NotNull(message = "机构要求不能为空")
    @Valid
    @ApiModelProperty(value = "机构要求", required = true)
    private AeaImUnitRequire aeaImUnitRequire;

    @ApiModelProperty(value = "专业要求")
    private List<AeaImMajorQual> aeaImMajorQuals;

    @FiledNameIs(filedValue = "服务选择条件：1 多个服务具备其一，0 多个服务都具备")
    @ApiModelProperty(value = "服务选择条件：1 多个服务具备其一，0 多个服务都具备")
    private String selectCondition;//

    @ApiModelProperty(value = "所选中介机构id", notes = "当选取中介方式为直接选取时，必输")
    private String agentUnitInfoId;

    @ApiModelProperty(value = "截止日期 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;


    @ApiModelProperty(value = "选取中介时间 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date choiceImunitTime;

    @ApiModelProperty(value = "业主委托人信息ID")
    private String ownerlinkmanInfoId;

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

    //采购项目信息
    @NotBlank(message = "采购项目编码不能为空")
    @ApiModelProperty(value = "采购项目地方编码", required = true)
    @FiledNameIs(filedValue = "采购项目地方编码")
    private String localCode;

    @NotBlank(message = "采购项目名称不能为空")
    @ApiModelProperty(value = "采购项目名称", required = true)
    @FiledNameIs(filedValue = "采购项目名称")
    private String projName;

    @ApiModelProperty(value = "是否为采购项目", notes = "1 是，0 否（投资审批项目）")
    private String isPurchaseProj = "1";

    @NotBlank(message = "采购项目规模不能为空")
    @ApiModelProperty(value = "采购项目规模", required = true)
    @FiledNameIs(filedValue = "采购项目规模")
    private String projScale;

    @NotBlank(message = "采购项目规模内容不能为空")
    @ApiModelProperty(value = "采购项目规模内容", required = true)
    @FiledNameIs(filedValue = "采购项目规模内容")
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
    private String isSocialFund;

    @ApiModelProperty(value = "社会资金比例", notes = "当 IS_SOCIAL_FUND = 1，必填")
    @FiledNameIs(filedValue = "社会资金比例：当 IS_SOCIAL_FUND = 1，必填")
    private String socialFundProportion;

    @ApiModelProperty(value = "批文文件")
    private String officialRemarkFile;
    @ApiModelProperty(value = "要求说明文件")
    private String requireExplainFile;


    /**
     * 生成申报信息-common
     *
     * @return
     */
    public ImItemApplyData createItemApplyData() {
        ImItemApplyData applyData = new ImItemApplyData();
        BeanUtils.copyProperties(this, applyData);
        if (!"0".equals(this.applySubject)) {
            applyData.setConstructionUnitId(this.buildProjUnitMap.get(0).getUnitIds().get(0));
        }
        applyData.setCreater(SecurityContext.getCurrentUserName());
        applyData.setRootOrgId(SecurityContext.getCurrentOrgId());
        return applyData;
    }

    /**
     * 生成采购信息-common
     *
     * @param applyinstId
     * @param applyinstCode
     * @return
     */
    public ImPurchaseData createPurchaseData(String applyinstId, String applyinstCode) {
        ImPurchaseData purchaseData = new ImPurchaseData();
        BeanUtils.copyProperties(this, purchaseData);
        purchaseData.setProjPurchaseId(UUID.randomUUID().toString());
        purchaseData.setApproveProjInfoId(this.projInfoId);
        purchaseData.setApplyinstId(applyinstId);
        purchaseData.setApplyinstCode(applyinstCode);
        purchaseData.setCreater(SecurityContext.getCurrentUserName());
        purchaseData.setRootOrgId(SecurityContext.getCurrentOrgId());
        if ("0".equals(this.applySubject)) {
            purchaseData.setPublishLinkmanInfoId(applyLinkmanId);

        } else {
            purchaseData.setPublishUnitInfoId(this.buildProjUnitMap.get(0).getUnitIds().get(0));
        }
        AeaImUnitRequire aeaImUnitRequire = purchaseData.getAeaImUnitRequire();
        if (null != aeaImUnitRequire) {
            String isQualRequire = aeaImUnitRequire.getIsQualRequire();
            String isRecordRequire = aeaImUnitRequire.getIsRecordRequire();
            String isRegisterRequire = aeaImUnitRequire.getIsRegisterRequire();
            if (StringUtils.isNotBlank(isQualRequire) || isQualRequire.equals("true")) {
                aeaImUnitRequire.setIsQualRequire("1");
            } else {
                aeaImUnitRequire.setIsQualRequire("0");
            }
            if (StringUtils.isNotBlank(isRecordRequire) || isRecordRequire.equals("true")) {
                aeaImUnitRequire.setIsRecordRequire("1");
            } else {
                aeaImUnitRequire.setIsRecordRequire("0");
            }
            if (StringUtils.isNotBlank(isRegisterRequire) || isRegisterRequire.equals("true")) {
                aeaImUnitRequire.setIsRegisterRequire("1");
            } else {
                aeaImUnitRequire.setIsRegisterRequire("0");
            }
        }
        return purchaseData;
    }

    public AeaProjInfo createAeaProjInfo() {
        AeaProjInfo projInfo = new AeaProjInfo();
        BeanUtils.copyProperties(this, projInfo);
        projInfo.setProjInfoId(UUID.randomUUID().toString());
        projInfo.setCreateTime(new Date());
        projInfo.setGcbm(projInfo.getLocalCode());
        projInfo.setCreater(SecurityContext.getCurrentUserName());
        projInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        return projInfo;
    }
}
