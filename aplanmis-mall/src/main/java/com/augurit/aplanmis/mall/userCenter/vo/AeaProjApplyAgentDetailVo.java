package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("代办详情信息VO")
public class AeaProjApplyAgentDetailVo {
    @ApiModelProperty("项目/工程信息")
    private AeaProjInfo projInfo;
    @ApiModelProperty("单位信息")
    private UserInfoVo userInfoVo;
    @ApiModelProperty("委托代办阶段")
    private String agentStageState;
    @ApiModelProperty("代办状态")
    private String agentApplyState;
    @ApiModelProperty("代办协议")
    private List<AgentAgreementVo> agentAgreement;
}
