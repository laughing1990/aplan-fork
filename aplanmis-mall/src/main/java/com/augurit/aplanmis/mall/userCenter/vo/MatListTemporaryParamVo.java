package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("暂存材料数据传值vo")
public class MatListTemporaryParamVo {
    @ApiModelProperty(value = "材料实例ID集合")
    private String applyinstId;
    @ApiModelProperty(value = "材料实例ID集合")
    private String[] matinstsIds;
}
