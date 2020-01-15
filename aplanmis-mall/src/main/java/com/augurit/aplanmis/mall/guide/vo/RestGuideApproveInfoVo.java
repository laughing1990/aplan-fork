package com.augurit.aplanmis.mall.guide.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("部门确认意见VO")
public class RestGuideApproveInfoVo {
    @ApiModelProperty("部门确认意见并联事项")
    private List<RestGuideApproveItemInfoVo> guideParallelItemList;
    @ApiModelProperty("部门确认意见并行推进事项")
    private List<RestGuideApproveItemInfoVo> guideCoreItemList;
    @ApiModelProperty("牵头部门意见")
    private String leaderComment;
    @ApiModelProperty("阶段序号")
    private String stageNo;
}
