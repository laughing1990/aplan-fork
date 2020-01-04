package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.service.window.AeaProjWindowService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @Autowired
    private RestApplyService restApplyService;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private RestApplyCommonService restApplyCommonService;

    public AeaProjApplyAgent saveProjInfoAndInitProjApplyAgent(AgentProjInfoParamVo agentProjInfoParamVo, LoginInfoVo loginInfo) throws Exception {
        String agentStageState=agentProjInfoParamVo.getAgentStageState();
        ProjAgentParamVo projAgentParamVo = agentProjInfoParamVo.getProjAgentParamVo();
        List<LinkmanTypeVo> types = agentProjInfoParamVo.getLinkmanTypeVos();
        if (types != null && types.size() > 0) {
            restApplyCommonService.saveOrUpdateLinkmanTypes(types);
        }
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

    public AeaProjApplyAgentDetailVo getProjInfoAndProjApplyAgent(String projInfoId, String applyAgentId, HttpServletRequest request) throws Exception {
        AeaProjApplyAgentDetailVo vo=new AeaProjApplyAgentDetailVo();
        UserInfoVo userInfoVo = restApplyService.getApplyObject(request, projInfoId);
        AeaProjInfo projInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if(StringUtils.isNotBlank(applyAgentId)){
            AeaProjApplyAgent applyAgent = aeaProjApplyAgentService.getAeaProjApplyAgentById(applyAgentId);
            vo.setAgentStageState(applyAgent.getAgentStageState());
            vo.setAgentApplyState(applyAgent.getAgentApplyState());
            List<AgentAgreementVo> list=new ArrayList<>();
            AgentAgreementVo agentAgreementVo=new AgentAgreementVo();
            agentAgreementVo.setAgentUserName(applyAgent.getAgentUserName());
            agentAgreementVo.setAgentUserMobile(applyAgent.getAgentUserMobile());
            List<BscAttForm> atts = bscAttMapper.listAttLinkAndDetailByTablePKRecordId("AEA_PROJ_APPLY_AGENT", "APPLY_AGENT_ID", applyAgentId, SecurityContext.getCurrentOrgId());
            agentAgreementVo.setAtts(atts);
            list.add(agentAgreementVo);
            vo.setAgentAgreement(list);
        }
        vo.setProjInfo(projInfo);
        vo.setUserInfoVo(userInfoVo);
        return vo;
    }

    public void submitAgentAgreement(String applyAgentId) throws Exception {
        AeaProjApplyAgent aeaProjApplyAgent =new AeaProjApplyAgent();
        aeaProjApplyAgent.setApplyAgentId(applyAgentId);
        aeaProjApplyAgent.setAgentApplyState(AgencyState.SIGN_COMPLETED.getValue());//申请人签订完成
        aeaProjApplyAgentService.updateAeaProjApplyAgent(aeaProjApplyAgent);
    }
}
