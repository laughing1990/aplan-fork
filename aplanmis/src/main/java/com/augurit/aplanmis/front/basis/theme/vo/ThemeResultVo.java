package com.augurit.aplanmis.front.basis.theme.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("查询主题resultVo")
public class ThemeResultVo {

    @ApiModelProperty(value = "主线: main; 辅线: help; 审查; check", required = true, dataType = "string")
    private String key;

    @ApiModelProperty(value = "阶段列表", required = true, dataType = "java.util.List")
    private List<StageVo> stages;

    public ThemeResultVo(String key, List<StageVo> stages) {
        this.key = key;
        this.stages = stages;
    }
}
