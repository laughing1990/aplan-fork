package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.service.window.AeaProjWindowService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaProjApplyAgentDetailVo;
import com.augurit.aplanmis.mall.userCenter.vo.AgentProjInfoParamVo;
import com.augurit.aplanmis.mall.userCenter.vo.ProjAgentParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RestAeaProjAgentService {
    @Autowired
    private AeaProjApplyAgentService aeaProjApplyAgentService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaProjWindowService aeaProjWindowService;

    public AeaProjApplyAgent saveProjInfoAndInitProjApplyAgent(AgentProjInfoParamVo agentProjInfoParamVo, LoginInfoVo loginInfo) throws Exception {
        String agentStageState=agentProjInfoParamVo.getAgentStageState();
        ProjAgentParamVo projAgentParamVo = agentProjInfoParamVo.getProjAgentParamVo();
        AeaProjInfo aeaProjInfo=AgentProjInfoParamVo.format(projAgentParamVo);
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        AeaProjApplyAgent aeaProjApplyAgent=initAeaProjApplyAgent(loginInfo,agentStageState,aeaProjInfo.getProjInfoId());
        aeaProjApplyAgentService.saveAeaProjApplyAgent(aeaProjApplyAgent);
        return aeaProjApplyAgent;
    }

    private AeaProjApplyAgent initAeaProjApplyAgent(LoginInfoVo loginInfo,String agentStageState,String projInfoId) throws Exception {
        AeaProjApplyAgent aeaProjApplyAgent=new AeaProjApplyAgent();
        aeaProjApplyAgent.setApplyAgentId(UUID.randomUUID().toString());
        aeaProjApplyAgent.setAgentCode(CommonTools.createDateNo());
        aeaProjApplyAgent.setUnitInfoId(loginInfo.getUnitId());
        aeaProjApplyAgent.setProjInfoId(projInfoId);
        aeaProjApplyAgent.setAgentUserId(loginInfo.getUserId());
        aeaProjApplyAgent.setAgentStageState(agentStageState);
        aeaProjApplyAgent.setAgentApplyState(AgencyState.SIGNING.getValue());
        List<AeaServiceWindow> windows = aeaProjWindowService.listAeaServiceWindowByProjInfoId(projInfoId);
        if(windows.size()==0) throw new Exception("该项目无代办窗口");
        aeaProjApplyAgent.setWindowId(windows.get(0).getWindowId());
        aeaProjApplyAgent.setStartTime(new Date());
        aeaProjApplyAgent.setCreater(SecurityContext.getCurrentUserName());
        aeaProjApplyAgent.setCreateTime(new Date());
        aeaProjApplyAgent.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaProjApplyAgent;
    }

    public AeaProjApplyAgentDetailVo getProjInfoAndProjApplyAgent(String projInfoId) {
        AeaProjApplyAgentDetailVo vo=new AeaProjApplyAgentDetailVo();
        return vo;
    }
}
