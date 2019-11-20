package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("申报统计数目：已申报，已撤件，草稿箱，材料补全，材料补正")
public class StatisticsNumVo {
    @ApiModelProperty(value = "材料补全数目")
    private long matCompletionNum;//材料补全
    @ApiModelProperty(value = "已申报数目")
    private long applyNum;//已申报
    @ApiModelProperty(value = "已撤件数目")
    private long withdrawalNum;//已撤件
    @ApiModelProperty(value = "草稿箱数目")
    private long draftNum;//草稿箱
    @ApiModelProperty(value = "材料补正数目")
    private long supplyNum;//材料补正
    @ApiModelProperty(value = "已审批数目")
    private long approvalNum;//已审批
    @ApiModelProperty(value = "撤回申报列表数目")
    private long withdrawNum;//已审批
    @ApiModelProperty(value = "我的材料库")
    private long myMatNum;//我的材料库
}
