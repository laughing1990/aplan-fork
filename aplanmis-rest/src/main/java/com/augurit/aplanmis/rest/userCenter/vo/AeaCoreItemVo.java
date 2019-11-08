package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("并联审批返回结果-并行事项VO")
public class AeaCoreItemVo {
    @ApiModelProperty(value = "事项名称")
    private String itemName;
    @ApiModelProperty(value = "审批部门")
    private String approveOrgName;
    @ApiModelProperty(value = "事项状态")
    private String iteminstState;
    @ApiModelProperty(value = "申报流水号")
    private String applyinstCode;
}
