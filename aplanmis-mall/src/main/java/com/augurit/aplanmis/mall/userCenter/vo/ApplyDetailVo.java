package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("申请详情")
public class ApplyDetailVo {

    @ApiModelProperty("项目信息")
    private AeaProjInfo aeaProjInfo;
    @ApiModelProperty("事项实例列表(并联专用)")
    private List<AeaHiIteminst> aeaHiIteminstList;
    @ApiModelProperty("情形列表")
    private List<Map<String,String>> stateList;
    @ApiModelProperty("材料列表")
    private List<MatinstVo> matList;
    @ApiModelProperty("办件结果实例")
    private AeaHiSmsInfo AeaHiSmsInfo;
    @ApiModelProperty("1:单项 0:并联")
    private String isSeriesApprove;
    @ApiModelProperty("角色0:个人,1:委托人,2:单位")
    private String role;
    @ApiModelProperty("事项名称（单项）")
    private String iteminstName;//事项名称（单项）
    @ApiModelProperty("审批部门（单项）")
    private String approveOrgName;//审批部门（单项）
    @ApiModelProperty("【主题】+阶段名称(并联)")
    private String themeStageName;//【主题】+阶段名称(并联)
    @ApiModelProperty("主题ID")
    private String themeId;//主题ID
    @ApiModelProperty("主题版本ID")
    private String themeVerId;//主题版本ID
    @ApiModelProperty("阶段ID")
    private String stageId;//阶段ID
    @ApiModelProperty("阶段实例ID(并联)")
    private String stageinstId;//阶段实例ID(并联)

    @ApiModelProperty("单位信息")
    private  AeaUnitInfo aeaUnitInfo;
    @ApiModelProperty("联系人信息")
    private AeaLinkmanInfo aeaLinkmanInfo;
    @ApiModelProperty("建设单位信息")
    private List<AeaUnitInfoVo> aeaUnitInfos;
    @ApiModelProperty("申报流水号")
    private String applyinstCode;
    @ApiModelProperty("是否启动情形 0否 1是")
    private String isNeedState;
    @ApiModelProperty("联系人列表")
    private List<AeaLinkmanInfo> aeaLinkmanInfoList;
}
