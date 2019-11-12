package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("事项信息VO")
public class AeaItemBasicVo {
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("事项名称")
    private String itemName;
    @ApiModelProperty("事项实例状态")
    private String iteminstState;
    @ApiModelProperty("事项实例历时")
    private Double iteminstRunTime;
    @ApiModelProperty(value = "承诺办结时限数字")
    private Double dueNum;
    @ApiModelProperty("事项实例开始时间")
    private Date iteminstStartTime;
    @ApiModelProperty("事项实例结束时间")
    private Date iteminstEndTime;
    @ApiModelProperty("实施主体")
    private String orgName;
    @ApiModelProperty("事项状态类型 0已办结 1办理中 2未办理")
    private String itemStateType;
}
