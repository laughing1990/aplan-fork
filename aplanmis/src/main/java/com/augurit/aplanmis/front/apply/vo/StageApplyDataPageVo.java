package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 并联申报参数实体
 */
@Data
@ApiModel("并联申报页面传值vo")
public class StageApplyDataPageVo {
    @ApiModelProperty(value = "并行申请实例ID列表")
    private String[] applyinstIds;

    @ApiModelProperty(value = "并联申请实例ID")
    private String parallelApplyinstId;

    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    @ApiModelProperty(value = "主题id")
    private String themeId;

    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win", dataType = "string")
    @NotEmpty(message = "applySource is null")
    private String applySource;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;

    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "并联申报事项版本ID", dataType = "java.util.List")
    private List<String> itemVerIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private List<ParallelItemStateVo> parallelItemStateIds;

    @ApiModelProperty(value = "分局承办，格式为", dataType = "string")
    private String branchOrgMap;

    @ApiModelProperty(value = "领件人信息对象id", required = true)
    @NotEmpty(message = "smsInfoVo is null")
    private String smsInfoId;

    @ApiModelProperty(value = "并行推进事项版本ID", dataType = "java.util.List")
    private List<String> propulsionItemVerIds;

    @ApiModelProperty(value = "并行推进事项分局承办", dataType = "string")
    private String propulsionBranchOrgMap;

    @ApiModelProperty(value = "项目ID集合", required = true)
    @NotNull(message = "projInfoIds is null")
    private String[] projInfoIds;

    @ApiModelProperty(value = "经办单位ID集合")
    private String[] handleUnitIds;

    @ApiModelProperty(value = "建设单位Map集合", dataType = "string")
    private List<BuildProjUnitVo> buildProjUnitMap;

    @ApiModelProperty(value = "并行事项情形Map集合")
    private List<PropulsionItemStateVo> propulsionItemStateIds;

    @ApiModelProperty(value = "材料实例ID集合")
    private String[] matinstsIds;

    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;

    @ApiModelProperty(value = "申请联系人ID", required = true)
    private String applyLinkmanId;

    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;

    @ApiModelProperty(value = "是否只实例化了申报实例 1是 0否", required = true)
    private String isJustApplyinst;

    @ApiModelProperty(value = "是否实例化过回执 1是 0否", required = true)
    private String isPrintReceive;


    public StageApplyDataVo toStageApplyDataVo(String appId, String themeVerId) {
        StageApplyDataVo stageApplyDataVo = new StageApplyDataVo();
        stageApplyDataVo.setApplyinstIds(this.applyinstIds);
        stageApplyDataVo.setParallelApplyinstId(this.parallelApplyinstId);
        stageApplyDataVo.setThemeVerId(themeVerId);
        stageApplyDataVo.setStageId(this.stageId);
        stageApplyDataVo.setApplySource(this.applySource);
        stageApplyDataVo.setApplySubject(this.applySubject);
        stageApplyDataVo.setLinkmanInfoId(this.linkmanInfoId);
        stageApplyDataVo.setAppId(appId);
        stageApplyDataVo.setItemVerIds(this.itemVerIds);
        stageApplyDataVo.setParallelItemStateIds(this.parallelItemStateIds);
        stageApplyDataVo.setBranchOrgMap(this.branchOrgMap);
        stageApplyDataVo.setPropulsionItemVerIds(this.propulsionItemVerIds);
        stageApplyDataVo.setPropulsionBranchOrgMap(this.propulsionBranchOrgMap);
        stageApplyDataVo.setProjInfoIds(this.projInfoIds);
        stageApplyDataVo.setHandleUnitIds(this.handleUnitIds);
        stageApplyDataVo.setIsJustApplyinst(this.isJustApplyinst);
        stageApplyDataVo.setIsPrintReceive(this.isPrintReceive);
        stageApplyDataVo.setBuildProjUnitMap(this.buildProjUnitMap);
        stageApplyDataVo.setPropulsionItemStateIds(this.propulsionItemStateIds);
        stageApplyDataVo.setMatinstsIds(this.matinstsIds);
        stageApplyDataVo.setComments(this.comments);
        stageApplyDataVo.setApplyLinkmanId(this.applyLinkmanId);
        stageApplyDataVo.setStateIds(this.stateIds);
        return stageApplyDataVo;
    }

}
