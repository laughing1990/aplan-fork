package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;


@Data
@ApiModel("代办申请表单参数")
public class AgentProjInfoParamVo {

    @ApiModelProperty(value = "当前企业用户的人员设置")
    private AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo;

    @ApiModelProperty(value = "委托代办信息 1立项用地规划许可阶段 2 工程建设许可阶段 3施工许可阶段 4竣工验收阶段  多选时逗号拼接")
    private String agentStageState;

    @ApiModelProperty(value = "代办项目信息")
    private ProjAgentParamVo projAgentParamVo;


    public static AeaProjInfo format(ProjAgentParamVo projAgentParamVo){
        AeaProjInfo aeaProjInfo=new AeaProjInfo();
        BeanUtils.copyProperties(projAgentParamVo,aeaProjInfo);
        return aeaProjInfo;
    }

}