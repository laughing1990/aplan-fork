package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门辅导事项参数")
public class AeaGuideItemVo {
    @ApiModelProperty("事项ID")
    private String itemId;
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("标准事项版本ID")
    private String baseItemVerId;

    @ApiModelProperty("申请人意见（申请人）")
    private String applySelOpinion;
    @ApiModelProperty("是否智能引导选择（申请人） 是 1 否0")
    private String isITSel;
    @ApiModelProperty("是否申请人选择（申请人） 是 1 否0")
    private String isApplySel;
}
