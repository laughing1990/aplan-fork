package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel("代办详情信息VO")
public class AeaProjApplyAgentDetailVo {
    @ApiModelProperty("项目/工程信息")
    private ProjAgentParamVo projAgentParamVo;
    @ApiModelProperty("单位信息")
    private AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo;
    @ApiModelProperty("委托代办阶段")
    private String agentStageState;
    @ApiModelProperty("代办状态")
    private String agentApplyState;
    @ApiModelProperty("代办协议")
    private List<AgentAgreementVo> agentAgreement;
    @ApiModelProperty("终止单")
    private List<AgentAgreementVo> agencyStopList;
    @ApiModelProperty("办结单")
    private List<AgentAgreementVo> agencyEndList;
    @ApiModelProperty("不予受理")
    private List<AgentAgreementVo> agencyOutScopeList;

    public static ProjAgentParamVo formatAeaProjApplyAgentDetailVo(AeaProjInfo aeaProjInfo){
        ProjAgentParamVo projAgentParamVo=new ProjAgentParamVo();
        BeanUtils.copyProperties(aeaProjInfo,projAgentParamVo);
        return projAgentParamVo;
    }
}
