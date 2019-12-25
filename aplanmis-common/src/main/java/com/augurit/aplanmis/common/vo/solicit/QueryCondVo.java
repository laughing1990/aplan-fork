package com.augurit.aplanmis.common.vo.solicit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询条件vo", description = "查询条件vo")
public class QueryCondVo {
    @ApiModelProperty(value = "意见征求业务类型。来源于数据字典", required = true)
    private String busType;

    @ApiModelProperty(value = "当前登录人ID", required = true)
    private String userId;

    @ApiModelProperty(value = "当前根组织ID", required = true)
    private String rootOrgId;

    @ApiModelProperty(value = "审批类型 0 并联，1 单项 “” 所有")
    private String applyType;

    private String theme;

    private String keyword;

}
