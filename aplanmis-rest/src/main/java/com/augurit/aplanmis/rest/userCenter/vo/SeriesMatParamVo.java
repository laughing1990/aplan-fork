package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单项获取材料参数实体vo")
public class SeriesMatParamVo {

    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    @ApiModelProperty(value = "情形ID数组", required = false)
    private String[] stateIds;
}
