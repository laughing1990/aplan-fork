package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门辅导申请参数")
public class AeaGuideItemVo {
    @ApiModelProperty("事项ID")
    private String itemId;
    @ApiModelProperty("标准事项版本ID")
    private String baseItemVerId;
    @ApiModelProperty("智能引导事项版本ID")
    private String itItemVerId;
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("智能引导选择")
    private String isITSel;
    @ApiModelProperty("申请人选择")
    private String isApplySel;
    @ApiModelProperty("申请人意见")
    private String applySelOpinion;
}
