package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 并联申报参数实体
 */
@Data
@ApiModel("并联申报vo")
public class StageApplyDataVo {
    @ApiModelProperty(value = "并行申请实例ID列表")
    private String[] applyinstIds;

    @ApiModelProperty(value = "并联申请实例ID")
    private String parallelApplyinstId;

    @ApiModelProperty(value = "主题版本ID")
    private String themeVerId;

    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win", dataType = "string")
    private String applySource;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;

    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "主题ID", required = true)
    private String appId;

    @ApiModelProperty(value = "并联申报事项版本ID", required = true, dataType = "java.util.List")
    private List<String> itemVerIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private List<ParallelItemStateVo> parallelItemStateIds;

    @ApiModelProperty(value = "分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
    private String branchOrgMap;

    @ApiModelProperty(value = "并行推进事项版本ID", required = true)
    private List<String> propulsionItemVerIds;

    @ApiModelProperty(value = "并行推进事项分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true, example = "[{itemVerId=2, branchOrg=1}, {itemVerId=11, branchOrg=22}]")
    private String propulsionBranchOrgMap;

    @ApiModelProperty(value = "项目ID集合", required = true)
    private String[] projInfoIds;

    @ApiModelProperty(value = "经办单位ID集合", required = true)
    private String[] handleUnitIds;

    @ApiModelProperty(value = "建设单位Map集合，key为projInfoId,格式为[{projInfoId1:[unitId1,unitId1]}]", required = true)
    private List<BuildProjUnitVo> buildProjUnitMap;

    @ApiModelProperty(value = "并行事项情形Map集合，key为itemVerId,格式为[{itemVerId:[stateId1,stateId2]}]", required = true)
    private List<PropulsionItemStateVo> propulsionItemStateIds;

    @ApiModelProperty(value = "材料实例ID集合", required = true)
    private String[] matinstsIds;

    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;

    @ApiModelProperty(value = "申请联系人ID", required = true)
    private String applyLinkmanId;

    @ApiModelProperty(value = "情形ID集合", required = true)
    private String[] stateIds;

    @ApiModelProperty(value = "是否只实例化了申报实例", required = true, notes = "0: 初始化, 1: 一张表单暂存后仅仅生成了申报实例, 2: 申报/打印回执/不受理后")
    private String isJustApplyinst;

    @ApiModelProperty(value = "是否实例化过回执 1是 0否", required = true)
    private String isPrintReceive;

    @ApiModelProperty(value = "是否绿色通道", notes = "1: 是, 0: 否")
    private String isGreenWay;
}
