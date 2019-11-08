package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("简单合并申报时, 选择的情形")
public class ParallelItemStateVo {

    @ApiModelProperty(value = "事项版本id", dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "情形id")
    private List<String> stateIds;
}