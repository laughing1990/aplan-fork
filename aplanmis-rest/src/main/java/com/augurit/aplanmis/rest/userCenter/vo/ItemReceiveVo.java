package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("事项回执VO")
public class ItemReceiveVo {
    @ApiModelProperty("事项名称")
    private String itemName;
    @ApiModelProperty("回执ID")
    private String receiveId;
    @ApiModelProperty("是否并联审批")
    private String isSeriApprove;
}
