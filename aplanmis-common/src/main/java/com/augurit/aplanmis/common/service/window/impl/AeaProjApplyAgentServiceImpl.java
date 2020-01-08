package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.common.vo.agency.AeaUnitProjLinkmanVo;
import com.augurit.aplanmis.common.vo.agency.AgencyDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
* 项目代办申请表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaProjApplyAgentServiceImpl implements AeaProjApplyAgentService {

    private static Logger logger = LoggerFactory.getLogger(AeaProjApplyAgentServiceImpl.class);

    @Autowired
    private AeaProjApplyAgentMapper aeaProjApplyAgentMapper;

    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaServiceWindowUserMapper aeaServiceWindowUserMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private FileUtilsService fileUtilsService;

    public void saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        if(aeaProjApplyAgent != null){
            aeaProjApplyAgent.setApplyAgentId(UUID.randomUUID().toString());
            aeaProjApplyAgent.setCreater(SecurityContext.getCurrentUserName());
            aeaProjApplyAgent.setCreateTime(new Date());
            aeaProjApplyAgentMapper.insertAeaProjApplyAgent(aeaProjApplyAgent);
        }
    }
    public void updateAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        if(aeaProjApplyAgent != null){
            String agreementCode = aeaProjApplyAgent.getAgreementCode();
            if(StringUtils.isBlank(agreementCode)){
                throw new Exception("协议编号不能为空。");
            }
            //校验编号唯一性
            AeaProjApplyAgent agent = aeaProjApplyAgentMapper.getAeaProjApplyAgentByAgreementCode(agreementCode);
            if(agent != null && !agent.getApplyAgentId().equals(aeaProjApplyAgent.getApplyAgentId())){
                throw new Exception("该协议编号已被使用。");
            }
            aeaProjApplyAgent.setModifier(SecurityContext.getCurrentUserName());
            aeaProjApplyAgent.setModifyTime(new Date());
            aeaProjApplyAgentMapper.updateAeaProjApplyAgent(aeaProjApplyAgent);
        }
    }
    public void deleteAeaProjApplyAgentById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaProjApplyAgentMapper.deleteAeaProjApplyAgent(id);
    }
    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgent(aeaProjApplyAgent);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaProjApplyAgent>(list);
    }
    public AeaProjApplyAgent getAeaProjApplyAgentById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaProjApplyAgentMapper.getAeaProjApplyAgentById(id);
    }
    public List<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgent(aeaProjApplyAgent);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgentByConditional(AeaProjApplyAgent aeaProjApplyAgent, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgentByConditional(aeaProjApplyAgent);
        logger.debug("成功执行查询list！！");
        return new PageInfo<>(list);
    }

    @Override
    public AgencyDetailVo getAgencyDetail(String applyAgentId) throws Exception{
        Assert.notNull(applyAgentId,"代办申请ID参数不能为空。");
        AgencyDetailVo vo = null;
        AeaProjApplyAgent aeaProjApplyAgent = aeaProjApplyAgentMapper.getAgencyAgreementDetail(applyAgentId);
        if(aeaProjApplyAgent != null){
            vo = new AgencyDetailVo();
            OpuOmUser currentUser = SecurityContext.getCurrentUser();
            if(currentUser == null){
                throw new RuntimeException("当前登录账号不存在。");
            }
            String userId = currentUser.getUserId();
            OpuOmUserInfo userInfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(userId);
            String currentUserMobile = userInfo != null?userInfo.getUserMobile():null;
            aeaProjApplyAgent.setCurrentUserId(userId);
            aeaProjApplyAgent.setCurrentUserName(currentUser.getUserName());
            aeaProjApplyAgent.setCurrentUserMobile(currentUserMobile);

            String projInfoId = aeaProjApplyAgent.getProjInfoId();
            String unitInfoId = aeaProjApplyAgent.getUnitInfoId();
            String agentApplyState = aeaProjApplyAgent.getAgentApplyState();
            AeaProjInfo projInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
            AeaUnitInfo unitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
            //项目单位信息
            AeaUnitProjLinkmanVo aeaUnitProjLinkmanVo = new AeaUnitProjLinkmanVo();
            aeaUnitProjLinkmanVo.setUnitInfoId(unitInfoId);
            aeaUnitProjLinkmanVo.setApplicant(unitInfo!=null?unitInfo.getApplicant():null);
            aeaUnitProjLinkmanVo.setEmail(unitInfo!=null?unitInfo.getEmail():null);
            aeaUnitProjLinkmanVo.setApplicantDetailSite(unitInfo!=null?unitInfo.getApplicantDetailSite():null);
            aeaUnitProjLinkmanVo.setUnitNature(unitInfo!=null?unitInfo.getUnitNature():null);
            List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, "7");
            if(unitProjs.size()>0){
                List<AeaUnitProjLinkman> aeaUnitProjLinkmans = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unitProjs.get(0).getUnitProjId(),null,null);
                if(aeaUnitProjLinkmans.size()>0){
                    List<AeaUnitProjLinkman> list = aeaUnitProjLinkmans.stream().filter(v -> ("1".equals(v.getLinkmanType()) || "0".equals(v.getLinkmanType()))).collect(Collectors.toList());
                    if(list.size()>0){
                        for (AeaUnitProjLinkman linkman:list){
                            if("0".equals(linkman.getLinkmanType())){
                                aeaUnitProjLinkmanVo.setOperatorName(linkman.getLinkmanName());
                                aeaUnitProjLinkmanVo.setOperatorMobilePhone(linkman.getLinkmanMobilePhone());
                                aeaUnitProjLinkmanVo.setOperatorDuty(linkman.getLinkmanDuty());
                            }else if("1".equals(linkman.getLinkmanType())){
                                aeaUnitProjLinkmanVo.setLeaderName(linkman.getLinkmanName());
                                aeaUnitProjLinkmanVo.setLeaderMobilePhone(linkman.getLinkmanMobilePhone());
                                aeaUnitProjLinkmanVo.setLeaderDuty(linkman.getLinkmanDuty());
                            }
                        }
                    }
                }
            }
            //已签章的文件
            if("3".equals(agentApplyState)||"4".equals(agentApplyState)){
                List<BscAttForm> bscAttForms = bscAttService.listAttLinkAndDetailByTablePKRecordId("AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE", aeaProjApplyAgent.getAgreementCode(), SecurityContext.getCurrentOrgId());
                if(bscAttForms != null && bscAttForms.size() > 0){
                    BscAttForm bscAttForm = bscAttForms.get(0);
                    aeaProjApplyAgent.setAgreementFileName(bscAttForm.getAttName());
                    aeaProjApplyAgent.setDetailId(bscAttForm.getDetailId());
                }
            }
            this.setAgentStageName(aeaProjApplyAgent);
            vo.setAeaProjInfo(projInfo);
            vo.setAeaUnitProjLinkmanVo(aeaUnitProjLinkmanVo);
            vo.setAeaProjApplyAgent(aeaProjApplyAgent);
        }
        return vo;
    }


    @Override
    public List<AeaProjApplyAgent> listAeaProjApplyAgentByProjInfoId(String projInfoId) throws Exception {
        AeaProjApplyAgent aeaProjApplyAgent=new AeaProjApplyAgent();
        aeaProjApplyAgent.setProjInfoId(projInfoId);
        return aeaProjApplyAgentMapper.listAeaProjApplyAgent(aeaProjApplyAgent);
    }

    @Override
    public AeaProjApplyAgent getAgencyAgreementDetail(String applyAgentId) throws Exception {
        AeaProjApplyAgent aeaProjApplyAgent = aeaProjApplyAgentMapper.getAgencyAgreementDetail(applyAgentId);
        this.setAgentStageName(aeaProjApplyAgent);
        return aeaProjApplyAgent;
    }

    @Override
    public boolean checkAgreementCodeUnique(String agreementCode) {
        AeaProjApplyAgent agent = aeaProjApplyAgentMapper.getAeaProjApplyAgentByAgreementCode(agreementCode);
        return agent == null;
    }

    @Override
    public List<AeaServiceWindowUser> getCurrAgencyWinUserList() {
        List<AeaServiceWindowUser> result = null;
        //查出当前登录账户所在的代办中心
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserIdAndRootOrgId(SecurityContext.getCurrentUserId(), currentOrgId);
        if(windows != null){
            List<String> winIds = new ArrayList<>();
            for(AeaServiceWindowUser win:windows){
                winIds.add(win.getWindowId());
            }
            result = aeaServiceWindowUserMapper.listAeaServiceWindowUserByWindowIdsAndRootOrgId(winIds.toArray(new String[winIds.size()]),currentOrgId);
        }
        return result;
    }

    /**
     * 删除代办协议
     * @param agreementCode
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteAgreementFile(String agreementCode) throws Exception{
        List<BscAttForm> bscAttForms = bscAttService.listAttLinkAndDetailByTablePKRecordId("AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE", agreementCode, SecurityContext.getCurrentOrgId());
        if(bscAttForms != null && bscAttForms.size() > 0){
            BscAttForm bscAttForm = bscAttForms.get(0);
            return fileUtilsService.deleteAttachment(bscAttForm.getDetailId());
        }
        return false;
    }

    private void setAgentStageName(AeaProjApplyAgent aeaProjApplyAgent){
        if(aeaProjApplyAgent != null){
            String[] name = {"","立项用地规划许可阶段","工程建设许可阶段","施工许可阶段","竣工验收阶段"};
            String stageState = aeaProjApplyAgent.getAgentStageState();
            String[] split = stageState.split(",");
            StringBuilder sb = new StringBuilder();
            for(String stage:split){
                sb.append("，").append(name[Integer.valueOf(stage)]);
            }
            String stageName = sb.toString().substring(1);
            aeaProjApplyAgent.setAgentStageName(stageName);
        }
    }
}

