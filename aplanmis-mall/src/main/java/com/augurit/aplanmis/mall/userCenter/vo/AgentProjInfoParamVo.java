package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("代办申请表单参数")
public class AgentProjInfoParamVo {

    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "委托代办信息 1立项用地规划许可阶段 2 工程建设许可阶段 3施工许可阶段 4竣工验收阶段  多选时逗号拼接")
    private String agentStageState;

    @ApiModelProperty(value = "代办项目信息")
    private AgentProjInfoParamVo agentProjInfoParamVo;

}