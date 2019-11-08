package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("单项申报参数实体vo")
public class SeriesApplyDataVo {
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "申请流水号")
    private String applyinstCode;
    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource;
    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "模板ID", required = true)
    private String appId;
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    @ApiModelProperty(value = "分局承办；并行推进事项分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
    private String branchOrgMap;
    @ApiModelProperty(value = "项目ID集合", required = true)
    private String[] projInfoIds;
    @ApiModelProperty(value = "经办单位ID集合", required = true)
    private String[] handleUnitIds;
    @ApiModelProperty(value = "建设单位Map集合，key为projInfoId,格式为[{projInfoId1:[unitId1,unitId1]}]", required = true)
    private List<BuildProjUnitVo> buildProjUnitMap;
    @ApiModelProperty(value = "材料实例ID集合", required = true)
    private String[] matinstsIds;
    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;
    @ApiModelProperty(value = "申请联系人ID", required = true)
    private String applyLinkmanId;
    @ApiModelProperty(value = "情形ID集合", required = true)
    private String[] stateIds;
    //是否并行推行：0表示否，1表示是
    private String isParallel;
    //并行推进阶段ID
    private String stageId;

}
