package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApiModel("并联回显vo")
@Getter
@Setter
public class ParallelUnstashVo {

    @ApiModelProperty(value = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "主题id")
    private String themeId;

    @ApiModelProperty(value = "主题版本id")
    private String themeVerId;

    @ApiModelProperty(value = "阶段id")
    private String stageId;

    @ApiModelProperty(value = "阶段所选情形")
    private Set<String> stateIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private Set<String> parallelItemStateIds;

    @ApiModelProperty(value = "并联事项与审批部门对应关系")
    private Map<String, String> branchOrg;

    @ApiModelProperty(value = "已填表单列表")
    private List<ForminstVo> forminstVos;

    public ParallelUnstashVo() {
        forminstVos = new ArrayList<>();
    }
}
