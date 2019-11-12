package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单项审批返回结果vo")
public class SeriesApplyResultVo {
    //申报信息
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "申报流水号")
    private String applyinstCode;

    //项目信息
    @ApiModelProperty(value = "项目/工程名称")
    private String projName;
    @ApiModelProperty(value = "项目类型")
    private String projType;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty(value = "工程编码")
    private String gcbm;

    //事项信息
    @ApiModelProperty(value = "事项名称")
    private String itemName;
    @ApiModelProperty(value = "实施主体")
    private String approveOrgName;
    @ApiModelProperty(value = "事项实施编码")
    private String iteminstCode;
    @ApiModelProperty(value = "办件类型")
    private String itemProperty;
}
