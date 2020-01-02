package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门辅导申请参数")
public class AeaGuideItItemVo {

    @ApiModelProperty("智能引导事项版本ID")
    private String itItemVerId;
    @ApiModelProperty("智能引导选择")
    private String isITSel;
    @ApiModelProperty("申请人选择")
    private String isApplySel;
}
