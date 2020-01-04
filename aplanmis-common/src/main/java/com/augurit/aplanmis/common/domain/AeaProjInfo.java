package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 项目库信息-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Data
@ApiModel("项目基础信息类")
public class AeaProjInfo implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键ID")
    private java.lang.String projInfoId; // (主键)
    @ApiModelProperty("地方编码")
    private java.lang.String localCode; // (地方编码)
    @ApiModelProperty("项目名称")
    private java.lang.String projName; // (项目名称)
    @ApiModelProperty("项目类型")
    private java.lang.String projType; // (立项类型，来自于数据字典，包括：审批、核准、备案)
    @ApiModelProperty("项目级别")
    private java.lang.String projLevel; // (项目级别)
    @ApiModelProperty("项目大类")
    private java.lang.String mainClass; // (项目大类)
    @ApiModelProperty("国家统一代码")
    private java.lang.String centralCode; // (国家统一代码)
    @ApiModelProperty("是否技改项目")
    private java.lang.String isJgxm; // (是否技改项目)
    @ApiModelProperty("紧急程度")
    private java.lang.String projUrgency; // (紧急程度)
    @ApiModelProperty("项目位置")
    private java.lang.String projAddr; // (项目位置)
    @ApiModelProperty("创建用户名")
    private java.lang.String projCreateUserCode; // (创建用户名)
    @ApiModelProperty("创建人姓名")
    private java.lang.String projCreateUser; // (创建人姓名)
    @ApiModelProperty("创建人联系电话")
    private java.lang.String projCreateMobile; // (创建人联系电话)
    @ApiModelProperty("建筑面积(m2)")
    private java.lang.Double buildAreaSum; // (建筑面积(m2))
    @ApiModelProperty("目前进展情况")
    private java.lang.String currentProgress; // (目前进展情况)
    @ApiModelProperty("问题及沟通情况")
    private java.lang.String projQuestion; // (问题及沟通情况)
    @ApiModelProperty("建设性质")
    private java.lang.String projNature; // (建设性质)
    @ApiModelProperty("拟开工时间")
    private java.lang.String nstartTime; // (拟开工时间)
    @ApiModelProperty("拟建成时间")
    private java.lang.String endTime; // (拟建成时间)
    @ApiModelProperty("总投资")
    private java.lang.Double investSum; // (总投资(万元))
    @ApiModelProperty("建设地点")
    private java.lang.String projectAddress; // (建设地点)
    @ApiModelProperty("建设地点详情")
    private java.lang.String areaDetailCode; // (建设地点详情)
    @ApiModelProperty("国标行业")
    private java.lang.String industry; // (国标行业)
    @ApiModelProperty("建设规模及内容")
    private java.lang.String scaleContent; // (建设规模及内容)
    @ApiModelProperty("用地面积")
    private java.lang.Double xmYdmj; // (用地面积（平方米）)
    @ApiModelProperty("新增用地面积")
    private java.lang.Double xzydmj; // (新增用地面积)
    @ApiModelProperty("农用地面积")
    private java.lang.Double nydmj; // (农用地面积)
    @ApiModelProperty("项目资本金")
    private java.lang.Double xmzbj; // (项目资本金)
    @ApiModelProperty("资金来源")
    private java.lang.String financialSource; // (资金来源)
    @ApiModelProperty("财政资金来源")
    private java.lang.String czzjly; // (财政资金来源)
    @ApiModelProperty("行政分区")
    private java.lang.String district; // (行政分区)
    @ApiModelProperty("是否外资项目")
    private java.lang.String isForeign; // (是否外资项目)
    @ApiModelProperty("是否涉及国家安全")
    private java.lang.String foreignInvolveSecurity; // (是否涉及国家安全)
    @ApiModelProperty("投资方式")
    private java.lang.String foreignInvestmentWay; // (投资方式)
    @ApiModelProperty("适用产业政策条目类型")
    private java.lang.String foreignPolicyType; // (适用产业政策条目类型)
    @ApiModelProperty("适用产业政策条目")
    private java.lang.String foreignPolicyItem; // (适用产业政策条目)
    @ApiModelProperty("折合美元")
    private java.lang.Double foreignTotalDollar; // (折合美元(万元))
    @ApiModelProperty("使用的汇率")
    private java.lang.Double foreignTotalRate; // (使用的汇率（人民币/美元）)
    @ApiModelProperty("项目资本金")
    private java.lang.Double foreignCapital; // (项目资本金)
    @ApiModelProperty("项目资本金折合美元")
    private java.lang.Double foreignCapitalDollar; // (项目资本金折合美元)
    @ApiModelProperty("项目资本金使用的汇率")
    private java.lang.Double foreignCapitalRate; // (项目资本金使用的汇率)
    @ApiModelProperty("投资者名称")
    private java.lang.String investor; // (投资者名称)
    @ApiModelProperty("注册国别地区")
    private java.lang.String investorCountry; // (注册国别地区)
    @ApiModelProperty("出资额")
    private java.lang.Double investorCapital; // (出资额(万元))
    @ApiModelProperty("出资比例%")
    private java.lang.Double investorCapitalPercent; // (出资比例%)
    @ApiModelProperty("出资方式")
    private java.lang.String investorCapitalType; // (出资方式)
    @ApiModelProperty("是否涉及新增固定资产投资")
    private java.lang.String foreignIsAddInvestment; // (是否涉及新增固定资产投资)
    @ApiModelProperty("土地获取方式")
    private java.lang.String foreignLandWay; // (土地获取方式)
    @ApiModelProperty("总用地面积")
    private java.lang.Double foreignLandArea; // (总用地面积(平方米))
    @ApiModelProperty("总建筑面积")
    private java.lang.Double foreignBuildingArea; // (总建筑面积(平方米))
    @ApiModelProperty("是否新增设备")
    private java.lang.String foreignIsAddEquipment; // (是否新增设备)
    @ApiModelProperty("拟进口设备数量及金额")
    private java.lang.String foreignEquipmentNum; // (其中：拟进口设备数量及金额)
    @ApiModelProperty("项目单位是否筹建中")
    private java.lang.String foreignIsHaveDept; // (项目单位是否筹建中)
    @ApiModelProperty("项目单位注册地址")
    private java.lang.String foreignDeptAddress; // (项目单位注册地址)
    @ApiModelProperty("项目单位性质")
    private java.lang.String foreignDeptNature; // (项目单位性质)
    @ApiModelProperty("项目单位中、外方各股东及持股比例")
    private java.lang.String foreignIsSame; // (项目单位中、外方各股东及持股比例)
    @ApiModelProperty("中方股比")
    private java.lang.Double foreignChinaPercent; // (中方股比)
    @ApiModelProperty("外方股比")
    private java.lang.Double foreignForeignPercent; // (外方股比)
    @ApiModelProperty("主要经营范围")
    private java.lang.String foreignManagement; // (主要经营范围)
    @ApiModelProperty("联系电话")
    private java.lang.String foreignTel; // (联系电话)
    @ApiModelProperty("传真")
    private java.lang.String foreignFax; // (传真)
    @ApiModelProperty("通讯地址")
    private java.lang.String foreignAddress; // (通讯地址)
    @ApiModelProperty("备注")
    private java.lang.String foreignRemark; // (备注)
    @ApiModelProperty("项目所在地")
    private java.lang.String abroadProjectAddress; // (项目所在地)
    @ApiModelProperty("中方投资额")
    private java.lang.Double abroadChineseInvestment; // (中方投资额(万元))
    @ApiModelProperty("投资目的国")
    private java.lang.String areaDetial; // (投资目的国)
    @ApiModelProperty("所属行业")
    private java.lang.String theIndustry; // (所属行业)
    @ApiModelProperty("项目类型")
    private java.lang.String foreignProjectType; // (项目类型)
    @ApiModelProperty("核准文件文号")
    private java.lang.String foreignApproveNum; // (核准文件文号)
    @ApiModelProperty("一般性变更")
    private java.lang.String foreignChangeReason; // (一般性变更（请说明具体事项及变更原因）)
    @ApiModelProperty("安全审查决定文号")
    private java.lang.String foreignSecurityNum; // (安全审查决定文号)
    @ApiModelProperty("项目创建人")
    private java.lang.String projectCreateUser; // (项目创建人)
    @ApiModelProperty("项目创建时间")
    private java.lang.String projectCreateDate; // (项目创建时间)
    @ApiModelProperty("项目所属状态")
    private java.lang.String projectStatus; // (项目所属状态)
    @ApiModelProperty("产业大类")
    private java.lang.String industryMainClass; // (产业大类)
    @ApiModelProperty("产业小类")
    private java.lang.String industrySubClass; // (产业小类)
    @ApiModelProperty("建筑类型")
    private java.lang.String buildType; // (建筑类型)
    @ApiModelProperty("投资类型")
    private java.lang.String investType; // (投资类型)
    @ApiModelProperty("土地来源")
    private java.lang.String landSource; // (土地来源)
    @ApiModelProperty("用地性质")
    private java.lang.String landNature; // (用地性质)
    @ApiModelProperty("建筑性质")
    private java.lang.String buildNature; // (建筑性质)
    @ApiModelProperty("逻辑删除标记：0表示正常记录，1表示已删除记录")
    private java.lang.String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    @ApiModelProperty("所属主题定义ID")
    private java.lang.String themeId; // (所属主题定义ID（项目类型），包括：)
    @ApiModelProperty(value = "主题版本id", dataType = "string")
    private String themeVerId;
    @ApiModelProperty("创建人")
    private java.lang.String creater; // (创建人)
    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @ApiModelProperty("修改人")
    private java.lang.String modifier; // (修改人)
    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    @ApiModelProperty("工程分类，来自于数据字典，包括：民用建筑、工业建筑等23类")
    private java.lang.String projCategory; // (工程分类，来自于数据字典，包括：民用建筑、工业建筑等23类)
    @ApiModelProperty("项目状态，0表示正常（已确认），1表示新建未确认，2表示审核中，3表示已暂停，4表示已终止")
    private java.lang.String projState; // (项目状态，0表示正常（已确认），1表示新建未确认，2表示审核中，3表示已暂停，4表示已终止)
    @ApiModelProperty("是否含有详细项目信息，0表示没有详细信息（适用于ROOT项目和子项目），1表示含有详细信息（适用于发改项目）")
    private java.lang.String haveDetail; // (是否含有详细项目信息，0表示没有详细信息（适用于ROOT项目和子项目），1表示含有详细信息（适用于发改项目）)
    @ApiModelProperty("项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目")
    private java.lang.String projFlag; // (项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目)
    @ApiModelProperty("项目空间信息id")
    private java.lang.String geoObjectid; // (项目空间信息id,多个用","隔开)
    @ApiModelProperty("项目规模")
    private java.lang.String projScale;
    @ApiModelProperty("项目规模内容")
    private java.lang.String projScaleContent;
    @ApiModelProperty("是否为财政资金（资金来源）：1 是，0 不是")
    private java.lang.String isFinancialFund; // (是否为财政资金（资金来源）：1 是，0 不是)
    @ApiModelProperty("财政资金比例：当 IS_FINANCIAL_FUND = 1，必填")
    private java.lang.String financialFundProportion; // (财政资金比例：当 IS_FINANCIAL_FUND = 1，必填)
    @ApiModelProperty("是否为社会资金（资金来源）：1 是，0 不是")
    private java.lang.String isSocialFund; // (是否为社会资金（资金来源）：1 是，0 不是)
    @ApiModelProperty("社会资金比例：当 IS_SOCIAL_FUND = 1，必填")
    private java.lang.String socialFundProportion; // (社会资金比例：当 IS_SOCIAL_FUND = 1，必填)
    @ApiModelProperty("是否为无效项目：1 是， 0 否")
    private java.lang.String isInvalidProj; // (是否为无效项目：1 是， 0 否)
    @ApiModelProperty("是否为采购项目：1 是，0 否（投资审批项目）")
    private java.lang.String isPurchaseProj; // (是否为采购项目：1 是，0 否（投资审批项目）)
    @ApiModelProperty("工程编码")
    private java.lang.String gcbm; // (工程编码)
    private String rootOrgId;//根组织ID

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
    @ApiModelProperty(value = "行政区划", dataType = "string")
    private java.lang.String regionalism;

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

    @ApiModelProperty("项目用途")
    private String projFunction; // (项目用途，来自于数据字典)
    @ApiModelProperty("建设规模类型，来自于数据字典，7特大型工程、8大型工程、9中型工程、10小型工程")
    private String scaleType; // (建设规模类型，来自于数据字典，7特大型工程、8大型工程、9中型工程、10小型工程)
    @ApiModelProperty("立项文号")
    private String projNum; // (立项文号)
    @ApiModelProperty("立项批准机关")
    private String approvalDept; // (立项批准机关)
    @ApiModelProperty("立项批准时间")
    private java.util.Date approvalTime; // (立项批准时间)
    @ApiModelProperty("省级项目编号，三库一平台返回")
    private String provinceProjCode; // (省级项目编号，三库一平台返回)
    @ApiModelProperty("是否工程总承包（EPC）模式")
    private String isEpc; // (是否工程总承包（EPC）模式)

    @ApiModelProperty("地上层数")
    private java.lang.Integer aboveFloor;//("地上层数")
    @ApiModelProperty("地下层数")
    private java.lang.Integer underFloor;//("地下层数")
    @ApiModelProperty("地上建筑高度（米）")
    private java.lang.Double aboveHeight; // (地上建筑高度（米）)
    @ApiModelProperty("地下建筑深度（米）")
    private java.lang.Double underDepth; // (地下建筑深度（米）)
    @ApiModelProperty("长度（米）")
    private java.lang.Double length; // (长度（米）)
    @ApiModelProperty("垮度（米）")
    private java.lang.Double span; // (垮度（米）)

    //非表字段
    @ApiModelProperty("关键字")
    private String keyword;
    @ApiModelProperty("父ID")
    private String parentProjId; // 父ID
    @ApiModelProperty("序列")
    private String projSeq; // 序列
    @ApiModelProperty("是否子项目")
    private String isChildProj;//是否子项目 0表示否，1表示是
    @ApiModelProperty("所属主题类型")
    private String themeType;
    @ApiModelProperty(value = "行政区划名称", dataType = "string")
    private String regionName;

    @ApiModelProperty(value = "单位名称", dataType = "string")
    private String applicant;
    @ApiModelProperty(value = "单位证件号", dataType = "string")
    private String idCard;
//    @ApiModelProperty("建设单位列表")
//    private List<AeaUnitInfo> buildUnitList;//是否子项目 0表示否，1表示是
//    @ApiModelProperty("代办单位列表")
//    private List<AeaUnitInfo> agencyUnitList;//是否子项目 0表示否，1表示是
    @ApiModelProperty("建设性质名称（非表字段）")
    private java.lang.String projNatureStr; // (建设性质名称（非表字段）)
    @ApiModelProperty("主题名称（非表字段）")
    private java.lang.String themeName; // (建设性质名称（非表字段）)
    @ApiModelProperty("项目建设单位列表")
    private List<AeaUnitInfoVo> aeaUnitInfos;

    private String stageFlag;// 广东模式的阶段标志 1:工程规划阶段，2：施工许可阶段
    //扩展字段
    private String formId;
    @ApiModelProperty("是否需要生成本地编码，1:是")
    private String isNeedGeneCode;

    @ApiModelProperty("是否代办项目")
    private String isAgentProj;
    @ApiModelProperty("代办状态")
    private String projAgentState;
    @ApiModelProperty("代办申请ID")
    private String applyAgentId;

}
