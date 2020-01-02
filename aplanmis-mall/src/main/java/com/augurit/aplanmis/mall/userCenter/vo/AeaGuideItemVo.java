package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门辅导申请参数")
public class AeaGuideItemVo {
    @ApiModelProperty("事项ID")
    private String itemId;
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("标准事项版本ID")
    private String baseItemVerId;
    @ApiModelProperty("申请人意见")
    private String applySelOpinion;
}
