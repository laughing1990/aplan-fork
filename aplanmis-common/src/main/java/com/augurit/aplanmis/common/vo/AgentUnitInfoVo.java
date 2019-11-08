package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/12
 */
@Data
@ApiModel("符合条件的中介机构")
public class AgentUnitInfoVo {
    @ApiModelProperty(value = "中介机构id")
    private String unitInfoId;//中介机构id
    @ApiModelProperty(value = "综合评价")
    private String compEvaluation;//综合评价
    @ApiModelProperty(value = "中介机构名称")
    private String agentUnitName;//中介机构名称

    @Data
    public static class Require{

        public Require(boolean noRequire) {
            this.noRequire = noRequire;
        }

        private boolean noRequire;

        private boolean finish;
    }
}
