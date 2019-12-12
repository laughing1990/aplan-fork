package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApiModel("单项回显vo")
@Getter
@Setter
public class SeriesUnstashVo {

    @ApiModelProperty(value = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "主题id")
    private String themeId;

    @ApiModelProperty(value = "主题版本id")
    private String themeVerId;

    @ApiModelProperty(value = "阶段id")
    private String stageId;

    @ApiModelProperty(value = "审批部门")
    private String approveOrgId;

    @ApiModelProperty(value = "情形id")
    private Set<String> stateIds;

    @ApiModelProperty(value = "已填表单列表")
    private List<ForminstVo> forminstVos;

    public SeriesUnstashVo() {
        forminstVos = new ArrayList<>();
        stateIds = new HashSet<>();
    }
}
