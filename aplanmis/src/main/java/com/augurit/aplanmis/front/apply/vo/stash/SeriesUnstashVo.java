package com.augurit.aplanmis.front.apply.vo.stash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@ApiModel("单项回显vo")
@Getter
@Setter
public class SeriesUnstashVo extends UnStashVo {

    @ApiModelProperty(value = "审批部门")
    private String approveOrgId;

    @ApiModelProperty(value = "情形id")
    private Set<String> stateIds;

    public SeriesUnstashVo() {
        stateIds = new HashSet<>();
    }
}
