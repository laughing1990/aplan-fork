package com.augurit.aplanmis.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 网厅材料补全/补正实体
 */
@Data
@ApiModel("网厅材料补全/补正实体")
public class MatCorrectAndSuppleDto {
    @ApiModelProperty(name = "isSeriesApprove", value = "串联审批")
    private String isSeriesApprove;//是否串联审批  1 串联  0并联
    @ApiModelProperty(name = "isCorrect", value = "是否补正(1:补正0：补齐)")
    private String isCorrect;//是否补正
    @ApiModelProperty(name = "applyinstId", value = "申请实例ID")
    private String applyinstId;//申请实例ID
    @ApiModelProperty(name = "applyinstCode", value = "申报流水号")
    private String applyinstCode;//申报流水号
    @ApiModelProperty(name = "projName", value = "项目名称")
    private String projName;//项目名称
    @ApiModelProperty(name = "localCode", value = "项目代码")
    private String localCode;//项目代码
    @ApiModelProperty(name = "gcbm", value = "工程代码")
    private String gcbm;//工程代码
    @ApiModelProperty(name = "stageName", value = "阶段名称")
    private String stageName;//阶段名称
    @ApiModelProperty(name = "iteminstName", value = "事项实例名称")
    private String iteminstName;//事项实例名称
    @ApiModelProperty(name = "itemStageName", value = "事项或阶段名称")
    private String itemStageName;//事项或阶段名称
    @ApiModelProperty(name = "themeName", value = "主题名称")
    private String themeName;//主题名称
    @ApiModelProperty(name = "applyinstCorrectAndSuppleId", value = "材料补齐补正ID")
    private String applyinstCorrectAndSuppleId;//材料补正ID

}
