package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("并行事项情形vo")
public class PropulsionItemStateVo {

    @ApiModelProperty(value = "事项版本id", dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "情形id")
    private List<String> stateIds;
}
