package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("统计各类总量vo")
public class YearCountVo {
    @ApiModelProperty(value = "接件总数", dataType="string")
    private String themtId;
    @ApiModelProperty(value = "接件总数", dataType="string")
    private String themtName;
    @ApiModelProperty(value = "阶段/辅线/并行推进事项ID", dataType="string")
    private String applyRecordId;
    @ApiModelProperty(value = "阶段/辅线/并行推进事项名称", dataType="string")
    private String applyRecordName;
    @ApiModelProperty(value = "接件总数", dataType="Long")
    private Long allApplyCount =0l;
    @ApiModelProperty(value = "补全待确认总数", dataType="Long")
    private Long allInSupplementCount;
    @ApiModelProperty(value = "补全已确认总数", dataType="Long")
    private Long allSupplementedCount =0l;
    @ApiModelProperty(value = "预受理总数", dataType="Long")
    private Long allPreAcceptanceCount =0l;
    @ApiModelProperty(value = "不予受理总数", dataType="Long")
    private Long allOutScopeCount =0l;
    @ApiModelProperty(value = "办结总数", dataType="Long")
    private Long allCompletedCount =0l;
    @ApiModelProperty(value = "逾期总数", dataType="Long")
    private Long allOverTimeCount =0l;

    @ApiModelProperty(value = "本年接件总数", dataType="Long")
    private Long thisYearApplyCount =0l;
    @ApiModelProperty(value = "本年补全待确认总数", dataType="Long")
    private Long thisYearInSupplementCount;
    @ApiModelProperty(value = "本年补全已确认总数", dataType="Long")
    private Long thisYearSupplementedCount =0l;
    @ApiModelProperty(value = "本年预受理总数", dataType="Long")
    private Long thisYearPreAcceptanceCount =0l;
    @ApiModelProperty(value = "本年不予受理总数", dataType="Long")
    private Long thisYearOutScopeCount =0l;
    @ApiModelProperty(value = "本年办结总数", dataType="Long")
    private Long thisYearCompletedCount =0l;
    @ApiModelProperty(value = "本年逾期总数", dataType="Long")
    private Long thisYearOverTimeCount =0l;

    @ApiModelProperty(value = "上一年接件总数", dataType="Long")
    private Long lastYearApplyCount =0l;
    @ApiModelProperty(value = "上一年补全待确认总数", dataType="Long")
    private Long lastYearInSupplementCount;
    @ApiModelProperty(value = "上一年补全已确认总数", dataType="Long")
    private Long lastYearSupplementedCount =0l;
    @ApiModelProperty(value = "上一年预受理总数", dataType="Long")
    private Long lastYearPreAcceptanceCount =0l;
    @ApiModelProperty(value = "上一年不予受理总数", dataType="Long")
    private Long lastYearOutScopeCount =0l;
    @ApiModelProperty(value = "上一年办结总数", dataType="Long")
    private Long lastYearCompletedCount =0l;
    @ApiModelProperty(value = "上一年逾期总数", dataType="Long")
    private Long lastYearOverTimeCount =0l;
    @ApiModelProperty(value = "是否并行推进事项。0表示否，1表示是", dataType="string")
    private String isParallel;
    @ApiModelProperty(value = "申请实例来源。net表示网上申报，win表示窗口申报", dataType="string")
    private String applyinstSource;
    @ApiModelProperty(value = "国标阶段", dataType="string")
    private String dybzspjdxh;
    @ApiModelProperty(value = "1表示主线，2表示辅线 3技术审查", dataType="string")
    private String isNode;
}
