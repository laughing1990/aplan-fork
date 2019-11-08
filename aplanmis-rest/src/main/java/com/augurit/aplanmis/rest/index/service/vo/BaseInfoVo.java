package com.augurit.aplanmis.rest.index.service.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单项申报基础信息VO")
public class BaseInfoVo {

    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("事项名称")
    private String itemName;
    @ApiModelProperty("是否需要情形 1是 0否")
    private String isNeedState;
    @ApiModelProperty("是否需要前置条件 1是 0否")
    private String isNeedFrontCond;

    @ApiModelProperty("项目信息")
    AeaProjInfo aeaProjInfo;
}
