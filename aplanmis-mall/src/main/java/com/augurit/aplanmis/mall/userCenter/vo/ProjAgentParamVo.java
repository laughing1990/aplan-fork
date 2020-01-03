package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel("代办项目信息参数")
public class ProjAgentParamVo {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键ID")
    private java.lang.String projInfoId;
    @ApiModelProperty("地方编码")
    private java.lang.String localCode;
    @ApiModelProperty("项目名称")
    private java.lang.String projName;
    @ApiModelProperty("土地来源")
    private java.lang.String landSource;
    @ApiModelProperty("资金来源")
    private java.lang.String financialSource;
    @ApiModelProperty("投资总额")
    private java.lang.Double investSum;
    @ApiModelProperty("重点项目级别")
    private java.lang.String projLevel;
    @ApiModelProperty("项目当前进度")
    private java.lang.String currentProgress;
    @ApiModelProperty("项目简介")
    private java.lang.String scaleContent;
    @ApiModelProperty("项目地址")
    private java.lang.String projAddr;


    @ApiModelProperty("建设地点")
    private java.lang.String projectAddress;
    @ApiModelProperty("行政区划")
    private java.lang.String regionalism;




}
