package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaProjApplyAgentMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.common.vo.agency.AgencyDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
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

    public void saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        aeaProjApplyAgentMapper.insertAeaProjApplyAgent(aeaProjApplyAgent);
    }
    public void updateAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        aeaProjApplyAgentMapper.updateAeaProjApplyAgent(aeaProjApplyAgent);
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
        AeaProjApplyAgent aeaProjApplyAgent = aeaProjApplyAgentMapper.getAeaProjApplyAgentById(applyAgentId);
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
            AeaProjInfo projInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
            List<AeaUnitInfo> unitInfoList = aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, "1");
            vo.setAeaProjInfo(projInfo);
            vo.setUnitInfoList(unitInfoList);
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
        return aeaProjApplyAgent;
    }
}

