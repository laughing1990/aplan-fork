package com.augurit.aplanmis.front.apply.vo.stash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@ApiModel("并联回显vo")
@Getter
@Setter
public class ParallelUnstashVo extends UnStashVo {

    @ApiModelProperty(value = "阶段所选情形")
    private Set<String> stateIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private Set<String> parallelItemStateIds;

    @ApiModelProperty(value = "并联事项与审批部门对应关系")
    private Map<String, String> branchOrg;

}
