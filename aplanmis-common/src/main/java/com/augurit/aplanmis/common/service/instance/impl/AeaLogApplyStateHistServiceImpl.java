package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.mapper.AeaLogApplyStateHistMapper;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AeaLogApplyStateHistServiceImpl implements AeaLogApplyStateHistService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaLogApplyStateHistServiceImpl.class);

    @Autowired
    private AeaLogApplyStateHistMapper aeaLogApplyStateHistMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String rootOrgId;

    @Override
    public void insertAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist) {
        LOGGER.debug("新增申请状态变更记录");
        aeaLogApplyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLogApplyStateHistMapper.insertAeaLogApplyStateHist(aeaLogApplyStateHist);
    }

    @Override
    public void updateAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist) {
        LOGGER.debug("修改申请状态变更记录");
        aeaLogApplyStateHistMapper.updateAeaLogApplyStateHist(aeaLogApplyStateHist);
    }

    @Override
    public void insertTriggerAeaLogApplyStateHist(String applyinstId, String taskinstId, String appinstId, String oldState, String newState, String opuWindowId) {
        AeaLogApplyStateHist aeaLogApplyStateHist = new AeaLogApplyStateHist();
        aeaLogApplyStateHist.setStateHistId(UUID.randomUUID().toString());
        aeaLogApplyStateHist.setApplyinstId(applyinstId);
        aeaLogApplyStateHist.setTaskinstId(taskinstId);
        aeaLogApplyStateHist.setAppinstId(appinstId);
        aeaLogApplyStateHist.setIsFlowTrigger(Status.ON);
        aeaLogApplyStateHist.setTriggerTime(new Date());
        aeaLogApplyStateHist.setOldState(oldState);
        aeaLogApplyStateHist.setNewState(newState);
        aeaLogApplyStateHist.setOpsWindowId(opuWindowId);
        aeaLogApplyStateHist.setOpsUserId(SecurityContext.getCurrentUserId());
        aeaLogApplyStateHist.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        LOGGER.debug("保存状态变更记录，申请ID为：{}，变更状态为", applyinstId, newState);
        aeaLogApplyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLogApplyStateHistMapper.insertAeaLogApplyStateHist(aeaLogApplyStateHist);
    }

    @Override
    public void insertOpsAeaLogApplyStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuWindowId) {
        this.insertOpsLinkBusAeaLogApplyStateHist(applyinstId, opsUserOpinion, opsAction, opsMemo, oldState, newState,opuWindowId, null, null, null);
    }

    @Override
    public void insertOpsLinkBusAeaLogApplyStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuWindowId, String busTableName, String busPkName, String busRecordId) {
        AeaLogApplyStateHist aeaLogApplyStateHist = new AeaLogApplyStateHist();
        aeaLogApplyStateHist.setStateHistId(UUID.randomUUID().toString());
        aeaLogApplyStateHist.setApplyinstId(applyinstId);
        aeaLogApplyStateHist.setIsFlowTrigger(Status.OFF);
        aeaLogApplyStateHist.setOldState(oldState);
        aeaLogApplyStateHist.setNewState(newState);
        aeaLogApplyStateHist.setOpsWindowId(opuWindowId);
        aeaLogApplyStateHist.setOpsUserId(SecurityContext.getCurrentUserId());
        aeaLogApplyStateHist.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        Date now = new Date();
        aeaLogApplyStateHist.setOpsFillTime(now);
        aeaLogApplyStateHist.setTriggerTime(now);
        aeaLogApplyStateHist.setOpsUserOpinion(opsUserOpinion);
        aeaLogApplyStateHist.setOpsAction(opsAction);
        aeaLogApplyStateHist.setOpsMemo(opsMemo);
        aeaLogApplyStateHist.setBusTableName(busTableName);
        aeaLogApplyStateHist.setBusPkName(busPkName);
        aeaLogApplyStateHist.setBusRecordId(busRecordId);
        LOGGER.debug("保存状态变更记录，申请ID为：{}，变更状态为", applyinstId, newState);
        this.insertAeaLogApplyStateHist(aeaLogApplyStateHist);
    }

    @Override
    public PageInfo<AeaLogApplyStateHist> listAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist, Page page) {
        aeaLogApplyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaLogApplyStateHist> list = aeaLogApplyStateHistMapper.listAeaLogApplyStateHist(aeaLogApplyStateHist);
        LOGGER.debug("成功执行分页查询！！");
        return new PageInfo<AeaLogApplyStateHist>(list);
    }

    @Override
    public AeaLogApplyStateHist getAeaLogApplyStateHistById(String id) {
        Assert.notNull(id, "id is null");
        LOGGER.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaLogApplyStateHistMapper.getAeaLogApplyStateHistById(id);
    }

    @Override
    public List<AeaLogApplyStateHist> findAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist) {
        //由于定时器扫描计算流程已用时不用登录，用SecurityContext获取会报错
        if (StringUtils.isBlank(rootOrgId))
            rootOrgId = SecurityContext.getCurrentOrgId();
        aeaLogApplyStateHist.setRootOrgId(rootOrgId);
        List<AeaLogApplyStateHist> list = aeaLogApplyStateHistMapper.listAeaLogApplyStateHist(aeaLogApplyStateHist);
        LOGGER.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaLogApplyStateHist getInitStateAeaLogApplyStateHist(String applyinstId, String appinstId) {
        return aeaLogApplyStateHistMapper.getInitStateAeaLogApplyStateHist(applyinstId, appinstId);
    }

    @Override
    public AeaLogApplyStateHist getAeaLogApplyStateHistByApplyinstCorrectId(String applyinstCorrectId) throws Exception {
        Assert.notNull(applyinstCorrectId, "材料补全实例ID为空！");
        return aeaLogApplyStateHistMapper.getAeaLogApplyStateHistByApplyinstCorrectId(applyinstCorrectId);
    }


    @Override
    public AeaLogApplyStateHist getAeaLogApplyStateHistListByApplyinstCorrectId(String applyinstCorrectId) throws Exception {
        Assert.notNull(applyinstCorrectId, "材料补全实例ID为空！");
        return aeaLogApplyStateHistMapper.getAeaLogApplyStateHistListByApplyinstCorrectId(applyinstCorrectId);
    }

    @Override
    public AeaLogApplyStateHist getProjFirstApplyStageLog(String projInfoId, String standardSortNo) {
        LOGGER.debug("获取项目第一次申报某个阶段的预受理时间");
        AeaLogApplyStateHist logApplyStateHist = aeaLogApplyStateHistMapper.getFirstApplyAeaProjStageByProjInfoIdAndStandardSortNo(projInfoId, standardSortNo);
        return logApplyStateHist;
    }

    @Override
    public  List<SupplyOrSpacialCommentVo> findApplyinstCorrectStateHist(String applyinstId, String taskInstId, String rootOrgId){
        Assert.notNull(taskInstId, "任务ID为空！");
        return aeaLogApplyStateHistMapper.findApplyinstCorrectStateHist(applyinstId,taskInstId,rootOrgId);
    }
}

