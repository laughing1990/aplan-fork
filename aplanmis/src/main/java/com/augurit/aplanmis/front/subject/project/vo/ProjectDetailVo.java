package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDetailVo {
    @ApiModelProperty(value = "项目id")
    private String projInfoId;
    
    @ApiModelProperty(value = "项目名称")
    private String projName;
    
    @ApiModelProperty(value = "地方编码")
    private String localCode;
    
    @ApiModelProperty(value = "工程编码")
    private String gcbm;
    
    @ApiModelProperty(value = "总投资")
    private Double investSum;
    
    @ApiModelProperty(value = "项目类型")
    private String projType;
    
    @ApiModelProperty(value = "建设性质")
    private String projNature;
    
    @ApiModelProperty(value = "项目开工时间")
    private String nstartTime;
    
    @ApiModelProperty(value = "项目建成时间")
    private String endTime;
    
    @ApiModelProperty(value = "核准文件文号")
    private String foreignApproveNum;
    
    @ApiModelProperty(value = "建设面积（㎡）")
    private String buildAreaSum;
    
    @ApiModelProperty(value = "用地面积")
    private Double xmYdmj;
    
    @ApiModelProperty(value = "项目地址")
    private String projAddr;
    
    @ApiModelProperty(value = "建筑内容")
    private String scaleContent;
    
    @ApiModelProperty(value = "备注")
    private String foreignRemark;

    @ApiModelProperty("项目级别")
    private String projLevel;
    @ApiModelProperty("项目大类")
    private String mainClass;
    @ApiModelProperty("国家统一代码")
    private String centralCode;
    @ApiModelProperty("是否技改项目")
    private String isJgxm;
    @ApiModelProperty("紧急程度")
    private String projUrgency;
    @ApiModelProperty("创建用户名")
    private String projCreateUserCode;
    @ApiModelProperty("创建人姓名")
    private String projCreateUser;
    @ApiModelProperty("创建人联系电话")
    private String projCreateMobile;
    @ApiModelProperty("目前进展情况")
    private String currentProgress;
    @ApiModelProperty("问题及沟通情况")
    private String projQuestion;
    @ApiModelProperty("建设地点详情")
    private String areaDetailCode;
    @ApiModelProperty("建设地点")
    private String projectAddress;
    @ApiModelProperty("国标行业")
    private String industry;
    @ApiModelProperty("新增用地面积")
    private Double xzydmj;
    @ApiModelProperty("农用地面积")
    private Double nydmj;
    @ApiModelProperty("项目资本金")
    private Double xmzbj;
    @ApiModelProperty("资金来源")
    private String financialSource;
    @ApiModelProperty("财政资金来源")
    private String czzjly;
    @ApiModelProperty("行政分区")
    private String district;
    @ApiModelProperty("是否外资项目")
    private String isForeign;
    @ApiModelProperty("是否涉及国家安全")
    private String foreignInvolveSecurity;
    @ApiModelProperty("投资方式")
    private String foreignInvestmentWay;
    @ApiModelProperty("适用产业政策条目类型")
    private String foreignPolicyType;
    @ApiModelProperty("适用产业政策条目")
    private String foreignPolicyItem;
    @ApiModelProperty("折合美元")
    private Double foreignTotalDollar;
    @ApiModelProperty("使用的汇率")
    private Double foreignTotalRate;
    @ApiModelProperty("项目资本金")
    private Double foreignCapital;
    @ApiModelProperty("项目资本金折合美元")
    @Size(max = 14)
    private Double foreignCapitalDollar;
    @ApiModelProperty("项目资本金使用的汇率")
    @Size(max = 14)
    private Double foreignCapitalRate;
    @ApiModelProperty("投资者名称")
    private String investor;
    @ApiModelProperty("注册国别地区")
    private String investorCountry;
    @ApiModelProperty("出资额")
    private Double investorCapital;
    @ApiModelProperty("出资比例%")
    private Double investorCapitalPercent;
    @ApiModelProperty("出资方式")
    private String investorCapitalType;
    @ApiModelProperty("是否涉及新增固定资产投资")
    private String foreignIsAddInvestment;
    @ApiModelProperty("土地获取方式")
    private String foreignLandWay;
    @ApiModelProperty("总用地面积")
    private Double foreignLandArea;
    @ApiModelProperty("总建筑面积")
    private Double foreignBuildingArea;
    @ApiModelProperty("是否新增设备")
    private String foreignIsAddEquipment;
    @ApiModelProperty("拟进口设备数量及金额")
    private String foreignEquipmentNum;
    @ApiModelProperty("项目单位是否筹建中")
    private String foreignIsHaveDept;
    @ApiModelProperty("项目单位注册地址")
    private String foreignDeptAddress;
    @ApiModelProperty("项目单位性质")
    private String foreignDeptNature;
    @ApiModelProperty("项目单位中、外方各股东及持股比例")
    private String foreignIsSame;
    @ApiModelProperty("中方股比")
    private Double foreignChinaPercent;
    @ApiModelProperty("外方股比")
    private Double foreignForeignPercent;
    @ApiModelProperty("主要经营范围")
    private String foreignManagement;
    @ApiModelProperty("联系电话")
    private String foreignTel;
    @ApiModelProperty("传真")
    private String foreignFax;
    @ApiModelProperty("通讯地址")
    private String foreignAddress;
    @ApiModelProperty("项目所在地")
    private String abroadProjectAddress;
    @ApiModelProperty("中方投资额")
    private Double abroadChineseInvestment;
    @ApiModelProperty("投资目的国")
    private String areaDetial;
    @ApiModelProperty("所属行业")
    private String theIndustry;
    @ApiModelProperty("项目类型")
    private String foreignProjectType;
    @ApiModelProperty("一般性变更")
    private String foreignChangeReason;
    @ApiModelProperty("安全审查决定文号")
    private String foreignSecurityNum;
    @ApiModelProperty("项目创建时间")
    private String projectCreateDate;
    @ApiModelProperty("项目所属状态")
    private String projectStatus;
    @ApiModelProperty("产业大类")
    private String industryMainClass;
    @ApiModelProperty("产业小类")
    private String industrySubClass;
    @ApiModelProperty("建筑类型")
    private String buildType;
    @ApiModelProperty("投资类型")
    private String investType;
    @ApiModelProperty("土地来源")
    private String landSource;
    @ApiModelProperty("用地性质")
    private String landNature;
    @ApiModelProperty("建筑性质")
    private String buildNature;
    @ApiModelProperty("逻辑删除标记：0表示正常记录，1表示已删除记录")
    private String isDeleted;
    @ApiModelProperty("工程分类，来自于数据字典，包括：民用建筑、工业建筑等23类")
    private String projCategory;
    @ApiModelProperty("项目状态，0表示正常（已确认），1表示新建未确认，2表示审核中，3表示已暂停，4表示已终止")
    private String projState;
    @ApiModelProperty("是否含有详细项目信息，0表示没有详细信息（适用于ROOT项目和子项目），1表示含有详细信息（适用于发改项目）")
    private String haveDetail;
    @ApiModelProperty("项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目")
    private String projFlag;
    @ApiModelProperty("项目空间信息id")
    private String geoObjectid;
    @ApiModelProperty("项目规模")
    private String projScale;
    @ApiModelProperty("项目规模内容")
    private String projScaleContent;
    @ApiModelProperty("是否为财政资金（资金来源）：1 是，0 不是")
    private String isFinancialFund;
    @ApiModelProperty("财政资金比例：当 IS_FINANCIAL_FUND = 1，必填")
    private String financialFundProportion;
    @ApiModelProperty("是否为社会资金（资金来源）：1 是，0 不是")
    private String isSocialFund;
    @ApiModelProperty("社会资金比例：当 IS_SOCIAL_FUND = 1，必填")
    private String socialFundProportion;
    @ApiModelProperty("是否为无效项目：1 是， 0 否")
    private String isInvalidProj;
    @ApiModelProperty("是否为采购项目：1 是，0 否（投资审批项目）")
    private String isPurchaseProj;

    @ApiModelProperty(value = "东至", dataType = "string", notes = "如: XXX路")
    private String eastScope;
    @ApiModelProperty(value = "南至", dataType = "string", notes = "如: XXX路")
    private String southScope;
    @ApiModelProperty(value = "西至", dataType = "string", notes = "如: XXX路")
    private String westScope;
    @ApiModelProperty(value = "北至", dataType = "string", notes = "如: XXX路")
    private String northScope;

    @ApiModelProperty(value = "立项部门", dataType = "string")
    private String projectDept;
    @ApiModelProperty(value = "项目类型", dataType = "string", notes = "就是指主题")
    private String projApplyType;
    @ApiModelProperty("行政区划")
    private String regionalism;
    @ApiModelProperty(value = "行政区划名称", dataType = "string")
    private String regionName;

    @ApiModelProperty(value = "地上面积", dataType = "string", notes = "单位: 平方米")
    private String aboveGround;
    @ApiModelProperty(value = "地下面积", dataType = "string", notes = "单位: 平方米")
    private String underGround;

    @ApiModelProperty(value = "土地是否带设计方案，0不是，1是", dataType = "string")
    private String isDesignSolution;
    @ApiModelProperty(value = "是否完成区域评估，0不是，1是", dataType = "string")
    private String isAreaEstimate;
    @ApiModelProperty(value = "国标行业代码发布年代，如：2017", dataType = "string")
    private String gbCodeYear;

    @ApiModelProperty(value = "申报主体类型")
    private String applySubjectType;
    @ApiModelProperty(value = "归属主题id")
    private String themeId;
    @ApiModelProperty(value = "主题版本id", dataType = "string")
    private String themeVerId;
    @ApiModelProperty(value = "主题类型")
    private String themeType;
    @ApiModelProperty(value = "个人")
    private ProjectApplySubjectApplicantVo personalApplicant;
    @ApiModelProperty(value = "建设单位")
    private List<ProjectApplySubjectEnterpriseVo> buildUnits;
    @ApiModelProperty(value = "经办单位")
    private List<ProjectApplySubjectEnterpriseVo> agentUnits;
    @ApiModelProperty(value = "非企业")
    private List<ProjectApplySubjectOthersVo> otherUnits;

    public static ProjectDetailVo from(AeaProjInfo aeaProjInfo) {
        ProjectDetailVo vo = new ProjectDetailVo();
        vo.setProjInfoId(aeaProjInfo.getProjInfoId());
        vo.setProjName(aeaProjInfo.getProjName());
        vo.setLocalCode(aeaProjInfo.getLocalCode());
        vo.setGcbm(aeaProjInfo.getGcbm());
        vo.setInvestSum(aeaProjInfo.getInvestSum());
        vo.setProjType(aeaProjInfo.getProjType());
        vo.setProjNature(aeaProjInfo.getProjNature());
        vo.setNstartTime(aeaProjInfo.getNstartTime());
        vo.setEndTime(aeaProjInfo.getEndTime());
        vo.setForeignApproveNum(aeaProjInfo.getForeignApproveNum());
        vo.setBuildAreaSum(aeaProjInfo.getBuildAreaSum() != null ? String.valueOf(aeaProjInfo.getBuildAreaSum()) : "");
        vo.setXmYdmj(aeaProjInfo.getXmYdmj());
        vo.setProjAddr(aeaProjInfo.getProjAddr());
        vo.setScaleContent(aeaProjInfo.getScaleContent());
        vo.setForeignRemark(aeaProjInfo.getForeignRemark());
        vo.setProjLevel(aeaProjInfo.getProjLevel());
        vo.setMainClass(aeaProjInfo.getMainClass());
        vo.setCentralCode(aeaProjInfo.getCentralCode());
        vo.setIsJgxm(aeaProjInfo.getIsJgxm());
        vo.setProjUrgency(aeaProjInfo.getProjUrgency());
        vo.setProjCreateUserCode(aeaProjInfo.getProjCreateUserCode());
        vo.setProjCreateUser(aeaProjInfo.getProjCreateUser());
        vo.setProjCreateMobile(aeaProjInfo.getProjCreateMobile());
        vo.setCurrentProgress(aeaProjInfo.getCurrentProgress());
        vo.setProjQuestion(aeaProjInfo.getProjQuestion());
        vo.setAreaDetailCode(aeaProjInfo.getAreaDetailCode());
        vo.setProjectAddress(aeaProjInfo.getProjectAddress());
        vo.setIndustry(aeaProjInfo.getIndustry());
        vo.setXzydmj(aeaProjInfo.getXzydmj());
        vo.setNydmj(aeaProjInfo.getNydmj());
        vo.setXmzbj(aeaProjInfo.getXmzbj());
        vo.setFinancialSource(aeaProjInfo.getFinancialSource());
        vo.setCzzjly(aeaProjInfo.getCzzjly());
        // 行政区划
        vo.setRegionalism(aeaProjInfo.getRegionalism());
        vo.setDistrict(aeaProjInfo.getDistrict());
        vo.setIsForeign(aeaProjInfo.getIsForeign());
        vo.setForeignInvolveSecurity(aeaProjInfo.getForeignInvolveSecurity());
        vo.setForeignInvestmentWay(aeaProjInfo.getForeignInvestmentWay());
        vo.setForeignPolicyType(aeaProjInfo.getForeignPolicyType());
        vo.setForeignPolicyItem(aeaProjInfo.getForeignPolicyItem());
        vo.setForeignTotalDollar(aeaProjInfo.getForeignTotalDollar());
        vo.setForeignTotalRate(aeaProjInfo.getForeignTotalRate());
        vo.setForeignCapital(aeaProjInfo.getForeignCapital());
        vo.setForeignCapitalDollar(aeaProjInfo.getForeignCapitalDollar());
        vo.setForeignCapitalRate(aeaProjInfo.getForeignCapitalRate());
        vo.setInvestor(aeaProjInfo.getInvestor());
        vo.setInvestorCountry(aeaProjInfo.getInvestorCountry());
        vo.setInvestorCapital(aeaProjInfo.getInvestorCapital());
        vo.setInvestorCapitalPercent(aeaProjInfo.getInvestorCapitalPercent());
        vo.setInvestorCapitalType(aeaProjInfo.getInvestorCapitalType());
        vo.setForeignIsAddInvestment(aeaProjInfo.getForeignIsAddInvestment());
        vo.setForeignLandWay(aeaProjInfo.getForeignLandWay());
        vo.setForeignLandArea(aeaProjInfo.getForeignLandArea());
        vo.setForeignBuildingArea(aeaProjInfo.getForeignBuildingArea());
        vo.setForeignIsAddEquipment(aeaProjInfo.getForeignIsAddEquipment());
        vo.setForeignEquipmentNum(aeaProjInfo.getForeignEquipmentNum());
        vo.setForeignIsHaveDept(aeaProjInfo.getForeignIsHaveDept());
        vo.setForeignDeptAddress(aeaProjInfo.getForeignDeptAddress());
        vo.setForeignDeptNature(aeaProjInfo.getForeignDeptNature());
        vo.setForeignIsSame(aeaProjInfo.getForeignIsSame());
        vo.setForeignChinaPercent(aeaProjInfo.getForeignChinaPercent());
        vo.setForeignForeignPercent(aeaProjInfo.getForeignForeignPercent());
        vo.setForeignManagement(aeaProjInfo.getForeignManagement());
        vo.setForeignTel(aeaProjInfo.getForeignTel());
        vo.setForeignFax(aeaProjInfo.getForeignFax());
        vo.setForeignAddress(aeaProjInfo.getForeignAddress());
        vo.setAbroadProjectAddress(aeaProjInfo.getAbroadProjectAddress());
        vo.setAbroadChineseInvestment(aeaProjInfo.getAbroadChineseInvestment());
        vo.setAreaDetial(aeaProjInfo.getAreaDetial());
        vo.setTheIndustry(aeaProjInfo.getTheIndustry());
        vo.setForeignProjectType(aeaProjInfo.getForeignProjectType());
        vo.setForeignChangeReason(aeaProjInfo.getForeignChangeReason());
        vo.setForeignSecurityNum(aeaProjInfo.getForeignSecurityNum());
        vo.setProjectCreateDate(aeaProjInfo.getProjectCreateDate());
        vo.setProjectStatus(aeaProjInfo.getProjectStatus());
        vo.setIndustryMainClass(aeaProjInfo.getIndustryMainClass());
        vo.setIndustrySubClass(aeaProjInfo.getIndustrySubClass());
        vo.setBuildType(aeaProjInfo.getBuildType());
        vo.setInvestType(aeaProjInfo.getInvestType());
        vo.setLandSource(aeaProjInfo.getLandSource());
        vo.setLandNature(aeaProjInfo.getLandNature());
        vo.setBuildNature(aeaProjInfo.getBuildNature());
        vo.setIsDeleted(aeaProjInfo.getIsDeleted());
        vo.setProjCategory(aeaProjInfo.getProjCategory());
        vo.setProjState(aeaProjInfo.getProjState());
        vo.setHaveDetail(aeaProjInfo.getHaveDetail());
        vo.setProjFlag(aeaProjInfo.getProjFlag());
        vo.setGeoObjectid(aeaProjInfo.getGeoObjectid());
        vo.setProjScale(aeaProjInfo.getProjScale());
        vo.setProjScaleContent(aeaProjInfo.getProjScaleContent());
        vo.setIsFinancialFund(aeaProjInfo.getIsFinancialFund());
        vo.setFinancialFundProportion(aeaProjInfo.getFinancialFundProportion());
        vo.setIsSocialFund(aeaProjInfo.getIsSocialFund());
        vo.setSocialFundProportion(aeaProjInfo.getSocialFundProportion());
        vo.setIsInvalidProj(aeaProjInfo.getIsInvalidProj());
        vo.setIsPurchaseProj(aeaProjInfo.getIsPurchaseProj());

        vo.setIsDesignSolution(aeaProjInfo.getIsDesignSolution());
        vo.setIsAreaEstimate(aeaProjInfo.getIsAreaEstimate());
        vo.setGbCodeYear(aeaProjInfo.getGbCodeYear());

        vo.setEastScope(aeaProjInfo.getEastScope());
        vo.setSouthScope(aeaProjInfo.getSouthScope());
        vo.setWestScope(aeaProjInfo.getWestScope());
        vo.setNorthScope(aeaProjInfo.getNorthScope());
        vo.setProjectDept(aeaProjInfo.getProjectDept());
        vo.setAboveGround(aeaProjInfo.getAboveGround());
        vo.setUnderGround(aeaProjInfo.getUnderGround());

        vo.setBuildUnits(new ArrayList<>());
        vo.setAgentUnits(new ArrayList<>());
        vo.setOtherUnits(new ArrayList<>());
        return vo;
    }
}
