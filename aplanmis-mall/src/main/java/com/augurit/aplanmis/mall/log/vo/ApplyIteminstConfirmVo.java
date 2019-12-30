package com.augurit.aplanmis.mall.log.vo;

import com.augurit.aplanmis.common.domain.AeaItemInout;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("并联审批事项确认返回结果VO")
public class ApplyIteminstConfirmVo {
    @ApiModelProperty(value = "主题ID")
    private String themeId;
    @ApiModelProperty(value = "主题版本ID")
    private String themeVerId;
    @ApiModelProperty(value = "主题名称")
    private String themeName;
    @ApiModelProperty(value = "旧主题ID")
    private String oldThemeId;
    @ApiModelProperty(value = "旧主题版本ID")
    private String oldThemeVerId;
    @ApiModelProperty(value = "旧主题名称")
    private String oldThemeName;
    @ApiModelProperty(value = "阶段ID")
    private String stageId;
    @ApiModelProperty(value = "阶段名称")
    private String stageName;
    @ApiModelProperty(value = "阶段实例ID")
    private String stageinstId;
    @ApiModelProperty(value = "并联审批事项列表")
    private List<IteminstConfirmVo> coreIteminstList;
    @ApiModelProperty(value = "并行推进事项列表")
    private List<IteminstConfirmVo> parallelIteminstList;
    @ApiModelProperty(value = "部门意见")
    private String deptComments;
    @ApiModelProperty(value = "是否变更主题  1是 0否")
    private String isThemeChange;

}
