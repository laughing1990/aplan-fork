package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BuildProjUnitVo {

    @ApiModelProperty(value = "项目id", dataType = "string")
    private String projectInfoId;

    @ApiModelProperty(value = "建设单位id")
    private List<String> unitIds;
}
