package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.mapper.AeaLogItemStateHistMapper;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AeaLogItemStateHistServiceImpl implements AeaLogItemStateHistService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaLogItemStateHistServiceImpl.class);

    @Autowired
    private AeaLogItemStateHistMapper aeaLogItemStateHistMapper;

    @Override
    public void insertAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist) {
        LOGGER.debug("新增事项状态变更记录");
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLogItemStateHistMapper.insertAeaLogItemStateHist(aeaLogItemStateHist);
    }

    @Override
    public void updateAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist) {
        LOGGER.debug("修改事项状态变更记录");
        aeaLogItemStateHistMapper.updateAeaLogItemStateHist(aeaLogItemStateHist);
    }

    @Override
    public void batchInsertTriggerAeaLogItemStateHist(List<AeaLogItemStateHist> aeaLogItemStateHistList) {
        LOGGER.debug("批量事项状态变更记录");
        if (aeaLogItemStateHistList != null && aeaLogItemStateHistList.size() > 0) {

            aeaLogItemStateHistMapper.batchInsertAeaLogItemStateHist(aeaLogItemStateHistList);
        }
    }

    @Override
    public void insertTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String oldState, String newState, String opuOrgId) {
        AeaLogItemStateHist aeaLogItemStateHist = this.constructTriggerAeaLogItemStateHist(iteminstId, taskinstId, appinstId, oldState, newState, opuOrgId);
        LOGGER.debug("保存状态变更记录，事项实例ID为：{}，变更状态为", iteminstId, newState);
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLogItemStateHistMapper.insertAeaLogItemStateHist(aeaLogItemStateHist);
    }

    @Override
    public AeaLogItemStateHist constructTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String oldState, String newState, String opuOrgId) {
        AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
        aeaLogItemStateHist.setStateHistId(UUID.randomUUID().toString());
        aeaLogItemStateHist.setIteminstId(iteminstId);
        aeaLogItemStateHist.setTaskinstId(taskinstId);
        aeaLogItemStateHist.setAppinstId(appinstId);
        aeaLogItemStateHist.setIsFlowTrigger(Status.ON);
        aeaLogItemStateHist.setTriggerTime(new Date());
        aeaLogItemStateHist.setOldState(oldState);
        aeaLogItemStateHist.setNewState(newState);
        aeaLogItemStateHist.setOpsOrgId(opuOrgId);
        aeaLogItemStateHist.setOpsUserId(SecurityContext.getCurrentUserId());
        aeaLogItemStateHist.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        return aeaLogItemStateHist;
    }

    @Override
    public void insertOpsAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuOrgId) {
        this.insertOpsLinkBusAeaLogItemStateHist(iteminstId, opsUserOpinion, opsAction, opsMemo, oldState, newState, opuOrgId, null, null, null);
    }

    @Override
    public void insertOpsLinkBusAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuOrgId, String busTableName, String busPkName, String busRecordId) {
        AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
        aeaLogItemStateHist.setStateHistId(UUID.randomUUID().toString());
        aeaLogItemStateHist.setIteminstId(iteminstId);
        aeaLogItemStateHist.setIsFlowTrigger(Status.OFF);
        aeaLogItemStateHist.setOldState(oldState);
        aeaLogItemStateHist.setNewState(newState);
        aeaLogItemStateHist.setOpsOrgId(opuOrgId);
        aeaLogItemStateHist.setOpsUserId(SecurityContext.getCurrentUserId());
        aeaLogItemStateHist.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        Date now = new Date();
        aeaLogItemStateHist.setTriggerTime(now);
        aeaLogItemStateHist.setOpsFillTime(now);
        aeaLogItemStateHist.setOpsUserOpinion(opsUserOpinion);
        aeaLogItemStateHist.setOpsAction(opsAction);
        aeaLogItemStateHist.setOpsMemo(opsMemo);
        aeaLogItemStateHist.setBusTableName(busTableName);
        aeaLogItemStateHist.setBusPkName(busPkName);
        aeaLogItemStateHist.setBusRecordId(busRecordId);
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        LOGGER.debug("保存状态变更记录，事项实例ID为：{}，变更状态为", iteminstId, newState);
        aeaLogItemStateHistMapper.insertAeaLogItemStateHist(aeaLogItemStateHist);
    }

    @Override
    public AeaLogItemStateHist getInitStateAeaLogItemStateHist(String iteminstId, String appinstId) {
        LOGGER.debug("获取事项初始状态记录");
        return aeaLogItemStateHistMapper.getInitStateAeaLogItemStateHist(iteminstId, appinstId);
    }

    @Override
    public PageInfo<AeaLogItemStateHist> listAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist, Page page) {
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaLogItemStateHist> list = aeaLogItemStateHistMapper.listAeaLogItemStateHist(aeaLogItemStateHist);
        LOGGER.debug("成功执行分页查询！！");
        return new PageInfo<AeaLogItemStateHist>(list);
    }

    @Override
    public AeaLogItemStateHist getAeaLogItemStateHistById(String id) {
        Assert.notNull(id, "id is null");
        LOGGER.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaLogItemStateHistMapper.getAeaLogItemStateHistById(id);
    }

    @Override
    public List<AeaLogItemStateHist> findAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist) {
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLogItemStateHist> list = aeaLogItemStateHistMapper.listAeaLogItemStateHist(aeaLogItemStateHist);
        LOGGER.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaLogItemStateHist getAeaLogItemStateHistByCorrectId(String correctId) throws Exception {
        Assert.notNull(correctId, "材料补正实例ID为空！");
        return aeaLogItemStateHistMapper.getAeaLogItemStateHistByCorrectId(correctId);
    }

    @Override
    public List<SupplyOrSpacialCommentVo> findSpacialAeaLogItemStateHist(String taskInstId, String rootOrgId) {
        Assert.notNull(taskInstId, "任务ID为空！");
        return aeaLogItemStateHistMapper.findSpacialAeaLogItemStateHist(taskInstId, rootOrgId);
    }

    @Override
    public List<SupplyOrSpacialCommentVo> findItemCorrectStateHist(String taskInstId, String rootOrgId) {
        Assert.notNull(taskInstId, "任务ID为空！");
        return aeaLogItemStateHistMapper.findItemCorrectStateHist(taskInstId, rootOrgId);
    }

    @Override
    public void batchDeleteAeaLogItemStateHist(List<String> ids) {
        if (ids.size() > 0) {
            aeaLogItemStateHistMapper.batchDeleteAeaLogItemStateHist(ids);
        }
    }

    @Override
    public List<AeaLogItemStateHist> findAeaLogItemStateHistByIteminstIds(String[] iteminstIds) {
        if (iteminstIds.length > 0) {
            return aeaLogItemStateHistMapper.listAeaLogItemStateHistByIteminstIds(Arrays.asList(iteminstIds));
        }
        return new ArrayList<>();
    }

    @Override
    public AeaLogItemStateHist getLastAeaLogItemStateHistByState(String iteminstId, String newState) throws Exception {
        Assert.notNull(iteminstId, "事项实例ID为空！");
        Assert.notNull(newState, "状态标志为空！");
        return aeaLogItemStateHistMapper.getLastAeaLogItemStateHistByState(iteminstId, newState, SecurityContext.getCurrentOrgId());
    }
}

