package com.augurit.aplanmis.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StageApplyStatusDto {

    @ApiModelProperty(value = "主题版本id", dataType = "string")
    private String themeVerId;

    @ApiModelProperty(value = "阶段id", dataType = "string")
    private String stageId;

    @ApiModelProperty(value = "阶段名称", dataType = "string")
    private String stageName;

    @ApiModelProperty(value = "阶段实例id", dataType = "string")
    private String stageinstId;

    @ApiModelProperty(value = "申报实例id", dataType = "string")
    private String applyinstId;

    @ApiModelProperty(value = "租户id", dataType = "string")
    private String rootOrgId;

    @ApiModelProperty(value = "申报状态名称", dataType = "string")
    private String applyStateName;

    @ApiModelProperty(value = "申报状态", dataType = "string")
    private String applyStateValue;

    @ApiModelProperty(value = "业务状态: 通过、申报失败、申报中", dataType = "string", notes = "由阶段定义中的lcbsxlx字段根据事项是否核心事项计算得出")
    private String businessStatusName;

    @ApiModelProperty(value = "1表示主线，2表示辅线 3技术审查", dataType = "string")
    private String isNode;

    @ApiModelProperty(value = "关联主线ID", dataType = "string")
    private String parentId;
}
