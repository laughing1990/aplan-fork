package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaParentProjMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.service.window.AeaProjWindowService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestAeaProjAgentService {
    @Autowired
    private AeaProjApplyAgentService aeaProjApplyAgentService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;
    @Autowired
    private AeaProjWindowService aeaProjWindowService;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    public AeaProjApplyAgent saveProjInfoAndInitProjApplyAgent(AgentProjInfoParamVo agentProjInfoParamVo, LoginInfoVo loginInfo) throws Exception {
        String unitInfoId=agentProjInfoParamVo.getAeaUnitProjLinkmanVo().getUnitInfoId();
        String agentStageState=agentProjInfoParamVo.getAgentStageState();
        ProjAgentParamVo projAgentParamVo = agentProjInfoParamVo.getProjAgentParamVo();
        AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo = agentProjInfoParamVo.getAeaUnitProjLinkmanVo();
        AeaProjInfo aeaProjInfo=AgentProjInfoParamVo.format(projAgentParamVo);
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        saveOrUpdateLinkmanTypes(aeaUnitProjLinkmanVo.getLeaderName(),aeaUnitProjLinkmanVo.getLeaderMobilePhone(),unitInfoId,aeaProjInfo.getProjInfoId(),"1");//todo 暂时用1表示负责人 2表示经办人
        saveOrUpdateLinkmanTypes(aeaUnitProjLinkmanVo.getOperatorName(),aeaUnitProjLinkmanVo.getOperatorMobilePhone(),unitInfoId,aeaProjInfo.getProjInfoId(),"2");
        AeaProjApplyAgent aeaProjApplyAgent=initAeaProjApplyAgent(loginInfo,agentStageState,aeaProjInfo.getProjInfoId());
        aeaProjApplyAgentService.saveAeaProjApplyAgent(aeaProjApplyAgent);
        return aeaProjApplyAgent;
    }

    private void saveOrUpdateLinkmanTypes(String linkmanName,String linkmanMobilePhone,String unitInfoId,String projInfoId,String type) throws Exception {
        AeaLinkmanInfo query=new AeaLinkmanInfo();
        query.setLinkmanName(linkmanName);
        query.setLinkmanMobilePhone(linkmanMobilePhone);
        List<AeaLinkmanInfo> linkmans = aeaLinkmanInfoService.listAeaLinkmanInfo(query);
        AeaLinkmanInfo aeaLinkmanInfo=new AeaLinkmanInfo();
        if(linkmans.size()>0){
            aeaLinkmanInfo=linkmans.get(0);
        }else{
            aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
            aeaLinkmanInfo.setLinkmanName(linkmanName);
            aeaLinkmanInfo.setLinkmanMobilePhone(linkmanMobilePhone);
            aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaLinkmanInfo.setIsActive(Status.ON);
            aeaLinkmanInfo.setIsDeleted(Status.OFF);
            aeaLinkmanInfoService.insertLinkmanInfo(aeaLinkmanInfo);
        }
        AeaUnitProj aeaUnitProj=new AeaUnitProj();
        List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, "7");
        if(unitProjs.size()==0){
            aeaUnitProj.setProjInfoId(projInfoId);
            aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setIsOwner("1");
            aeaUnitProj.setUnitInfoId(unitInfoId);
            aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
            aeaUnitProj.setIsDeleted(Status.OFF);
            aeaUnitProj.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            aeaUnitProj.setUnitType("7");
            aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
        }else{
            aeaUnitProj=unitProjs.get(0);
        }

        List<AeaUnitProjLinkman> aeaUnitProjLinkmans = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(aeaUnitProj.getUnitProjId(), aeaLinkmanInfo.getLinkmanInfoId(), type);
        if(aeaUnitProjLinkmans.size()==0){
            AeaUnitProjLinkman aeaUnitProjLinkman=new AeaUnitProjLinkman();
            aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
            aeaUnitProjLinkman.setUnitProjId(aeaUnitProj.getUnitProjId());
            aeaUnitProjLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            aeaUnitProjLinkman.setLinkmanType(type);
            aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitProjLinkman.setCreateTime(new Date());
            aeaUnitProjLinkman.setIsDeleted(Status.OFF);
            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
        }
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
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
       // UserInfoVo userInfoVo = restApplyService.getApplyObject(request, projInfoId);
        AeaProjApplyAgentDetailVo vo=new AeaProjApplyAgentDetailVo();
        AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo=new AeaUnitProjLinkmanVo();
        if(StringUtils.isNotBlank(loginVo.getUnitId())){
            AeaUnitInfo unitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(loginVo.getUnitId());
            aeaUnitProjLinkmanVo.setApplicant(unitInfo!=null?unitInfo.getApplicant():null);
            aeaUnitProjLinkmanVo.setEmail(unitInfo!=null?unitInfo.getEmail():null);
            aeaUnitProjLinkmanVo.setUnitInfoId(unitInfo!=null?unitInfo.getUnitInfoId():null);
            aeaUnitProjLinkmanVo.setApplicantDetailSite(unitInfo!=null?unitInfo.getApplicantDetailSite():null);
            aeaUnitProjLinkmanVo.setUnitNature(unitInfo!=null?unitInfo.getUnitNature():null);
        }
        List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, loginVo.getUnitId(), "7");
        if(unitProjs.size()>0){
            List<AeaUnitProjLinkman> aeaUnitProjLinkmans = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unitProjs.get(0).getUnitProjId(),null,null);
            if(aeaUnitProjLinkmans.size()>0){
                List<AeaUnitProjLinkman> list = aeaUnitProjLinkmans.stream().filter(v -> ("1".equals(v.getLinkmanType()) || "2".equals(v.getLinkmanType()))).collect(Collectors.toList());
                if(list.size()>0){
                    for (AeaUnitProjLinkman aeaUnitProjLinkman:list){
                        AeaLinkmanInfo linkman = aeaLinkmanInfoService.getOneById(aeaUnitProjLinkman.getLinkmanInfoId());
                        if("1".equals(aeaUnitProjLinkman.getLinkmanType())){
                            aeaUnitProjLinkmanVo.setLeaderName(linkman.getLinkmanName());
                            aeaUnitProjLinkmanVo.setLeaderMobilePhone(linkman.getLinkmanMobilePhone());
                            aeaUnitProjLinkmanVo.setLeaderDuty("负责人");
                        }else if("2".equals(aeaUnitProjLinkman.getLinkmanType())){
                            aeaUnitProjLinkmanVo.setOperatorName(linkman.getLinkmanName());
                            aeaUnitProjLinkmanVo.setOperatorMobilePhone(linkman.getLinkmanMobilePhone());
                            aeaUnitProjLinkmanVo.setOperatorDuty("经办人");
                        }
                    }
                }
            }
        }
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
        vo.setProjAgentParamVo(AeaProjApplyAgentDetailVo.formatAeaProjApplyAgentDetailVo(projInfo));
       // vo.setUserInfoVo(userInfoVo);
        vo.setAeaUnitProjLinkmanVo(aeaUnitProjLinkmanVo);
        return vo;
    }

    public void submitAgentAgreement(String applyAgentId) throws Exception {
        AeaProjApplyAgent aeaProjApplyAgent =new AeaProjApplyAgent();
        aeaProjApplyAgent.setApplyAgentId(applyAgentId);
        aeaProjApplyAgent.setAgentApplyState(AgencyState.SIGN_COMPLETED.getValue());//申请人签订完成
        aeaProjApplyAgentService.updateAeaProjApplyAgent(aeaProjApplyAgent);
    }


}
