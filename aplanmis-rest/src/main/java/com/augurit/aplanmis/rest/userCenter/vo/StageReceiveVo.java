package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("阶段回执VO")
public class StageReceiveVo {
    @ApiModelProperty("阶段名称")
    private String stateName;
    @ApiModelProperty("回执ID")
    private String receiveId;
}
