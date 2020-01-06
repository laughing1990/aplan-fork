package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@ApiModel("拆分子工程申请vo")
public class SplitProjInfoParamVo {
    @ApiModelProperty(value = "项目ID")
    private String parentProjInfoId;

    @ApiModelProperty(value = "所处阶段 1 工程建设许可阶段  2 施工许可阶段")
    private String stageNo;
    @ApiModelProperty(value = "阶段ID")
    private String stageId;
    @ApiModelProperty(value = "前阶段项目ID")
    private String frontStageProjInfoId;
    @ApiModelProperty(value = "前阶段关联工程代码")
    private String frontStageGcbm;
    @ApiModelProperty(value = "单项工程名称")
    private String projName;
    @ApiModelProperty(value = "工程范围")
    private String foreignManagement;
    @ApiModelProperty(value = "单项工程投资额")
    private Double investSum;
    @ApiModelProperty(value = "单项工程建设规模")
    private String scaleContent;
    @ApiModelProperty(value = "用地面积")
    private Double xmYdmj;
    @ApiModelProperty(value = "建筑面积(㎡)")
    private Double buildAreaSum;

    @ApiModelProperty(value = "申请单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "申请人ID")
    private String linkmanInfoId;


    public static AeaProjInfo formatProjInfo(AeaProjInfo projInfo, SplitProjInfoParamVo splitProjInfoParamVo) {
        AeaProjInfo aeaProjInfo=new AeaProjInfo();
        //继承父项目信息
        aeaProjInfo.setLocalCode(projInfo.getLocalCode());
        aeaProjInfo.setThemeId(projInfo.getThemeId());
        aeaProjInfo.setThemeVerId(projInfo.getThemeVerId());
        aeaProjInfo.setRegionalism(projInfo.getRegionalism());
        aeaProjInfo.setDistrict(projInfo.getDistrict());
        aeaProjInfo.setIsDeleted(Status.OFF);
        aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaProjInfo.setCreateTime(new Date());
        aeaProjInfo.setCreater(SecurityContext.getCurrentUserName());
        aeaProjInfo.setProjType(projInfo.getProjType());
        aeaProjInfo.setProjectAddress(projInfo.getProjectAddress());
        aeaProjInfo.setFinancialSource(projInfo.getFinancialSource());
        aeaProjInfo.setIsForeign(projInfo.getIsForeign());
        aeaProjInfo.setInvestType(projInfo.getInvestType());
        aeaProjInfo.setLandSource(projInfo.getLandSource());
        aeaProjInfo.setProjCategory(projInfo.getProjCategory());
        aeaProjInfo.setGbCodeYear(projInfo.getGbCodeYear());
        aeaProjInfo.setIndustry(projInfo.getIndustry());

        //工程信息
        aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
        aeaProjInfo.setProjFlag("c");
        aeaProjInfo.setProjName(splitProjInfoParamVo.getProjName());
        aeaProjInfo.setForeignManagement(splitProjInfoParamVo.getForeignManagement());
        aeaProjInfo.setInvestSum(splitProjInfoParamVo.getInvestSum());
        aeaProjInfo.setScaleContent(splitProjInfoParamVo.getScaleContent());
        aeaProjInfo.setXmYdmj(splitProjInfoParamVo.getXmYdmj());
        aeaProjInfo.setBuildAreaSum(splitProjInfoParamVo.getBuildAreaSum());
        return aeaProjInfo;
    }
}
