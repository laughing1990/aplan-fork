package com.augurit.aplanmis.common.vo.solicit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询条件vo", description = "查询条件vo")
public class QueryCondVo {
    @ApiModelProperty(value = "意见征求业务类型。来源于数据字典")
    private String busType;

    @ApiModelProperty(value = "当前登录人ID")
    private String userId;

    @ApiModelProperty(value = "当前根组织ID")
    private String rootOrgId;

}
