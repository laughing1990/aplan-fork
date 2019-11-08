package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 并联申报参数实体
 */
@Data
@ApiModel(value = "并联申报页面传值vo")
public class StageApplyDataPageVo {

    @ApiModelProperty(value = "申请实例ID集合")
    private String[] applyinstIds;

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "阶段ID", required = true)
    private String stageId;

    @ApiModelProperty(value = "主题id", required = true)
    private String themeId;

    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;

    @ApiModelProperty(value = "申报单位ID")
    private String unitInfoId;

    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "并联申报事项版本ID")
    private List<String> itemVerIds;

    @ApiModelProperty(value = "分局承办，格式为", dataType = "string")
    private String branchOrgMap;

    @ApiModelProperty(value = "领件人信息对象id", required = true)
    private String smsInfoId;

    @ApiModelProperty(value = "并行推进事项版本ID")
    private List<String> propulsionItemVerIds;

    @ApiModelProperty(value = "并行推进事项分局承办", required = true)
    private String propulsionBranchOrgMap;

    @ApiModelProperty(value = "项目ID集合", required = true)
    private String[] projInfoIds;

    @ApiModelProperty(value = "并行事项情形Map集合")
    private List<PropulsionItemStateVo> propulsionItemStateIds;

    @ApiModelProperty(value = "材料实例ID集合")
    private String[] matinstsIds;

    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形")
    private List<ParallelItemStateVo> parallelItemStateIds; // 简单合并申报，选择的并联事项情形


    public StageApplyDataVo toStageApplyDataVo(String appId, String themeVerId) {
        StageApplyDataVo stageApplyDataVo = new StageApplyDataVo();
        stageApplyDataVo.setApplyinstIds(this.applyinstIds);
        stageApplyDataVo.setApplyinstId(this.applyinstId);
        stageApplyDataVo.setThemeVerId(themeVerId);
        stageApplyDataVo.setStageId(this.stageId);
        stageApplyDataVo.setApplySource(this.applySource);
        stageApplyDataVo.setApplySubject(this.applySubject);
        stageApplyDataVo.setLinkmanInfoId(this.linkmanInfoId);
        stageApplyDataVo.setAppId(appId);
        stageApplyDataVo.setItemVerIds(this.itemVerIds);
        stageApplyDataVo.setPropulsionItemVerIds(this.propulsionItemVerIds);
        stageApplyDataVo.setProjInfoIds(this.projInfoIds);
        stageApplyDataVo.setPropulsionItemStateIds(this.propulsionItemStateIds);
        stageApplyDataVo.setMatinstsIds(this.matinstsIds);
        stageApplyDataVo.setUnitInfoId(this.unitInfoId);
        stageApplyDataVo.setThemeId(this.themeId);
        stageApplyDataVo.setStateIds(this.stateIds);
        stageApplyDataVo.setParallelItemStateIds(this.parallelItemStateIds);
        stageApplyDataVo.setSmsInfoId(this.smsInfoId);
        return stageApplyDataVo;
    }

}
