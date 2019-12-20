package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSeriesinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaLogItemStateHistMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.apply.ApplyCommonService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class ApplyCommonServiceImpl implements ApplyCommonService {

    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;
    @Autowired
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiParStateinstMapper aeaHiParStateinstMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaLogItemStateHistMapper aeaLogItemStateHistMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ActStoAppinstMapper actStoAppinstMapper;
    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;
    @Autowired
    private BpmProcessService bpmProcessService;


    @Override
    public Map<String, Boolean> filterProcessStartConditions(String[] stateIds, ApplyType applyType) {
        if (stateIds == null || stateIds.length < 1) {
            return null;
        }
        Map<String, Boolean> stateinsts = new HashMap<>(stateIds.length);
        if (applyType == ApplyType.UNIT) {
            List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);
            stateList.forEach(s -> {
                if (Status.ON.equals(s.getIsProcStartCond())) {
                    stateinsts.put(s.getParStateId(), true);
                }
            });
        } else if (applyType == ApplyType.SERIES) {
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            stateList.forEach(s -> {
                if (Status.ON.equals(s.getIsProcStartCond())) {
                    stateinsts.put(s.getItemStateId(), true);
                }
            });
        } else {
            return null;
        }
        return stateinsts;
    }

    @Override
    public void setInformCommit(String[] stateIds, ApplyType applyType, AeaHiIteminst iteminst) throws Exception {
        if (stateIds == null || stateIds.length < 1) {
            return;
        }

        if (applyType == ApplyType.UNIT) {
            List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaParState state : stateList) {
                //判断事项是否为告知承诺制
                if ("1".equals(state.getIsInformCommit())) {
                    iteminst.setIteminstType("p");
                    aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
                    break;
                }
            }

        } else if (applyType == ApplyType.SERIES) {
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaItemState state : stateList) {
                //判断事项是否为告知承诺制
                if ("1".equals(state.getIsInformCommit())) {
                    iteminst.setIteminstType("p");
                    aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
                    break;
                }
            }
        }
    }

    /**
     * 根据申报实例id清除所有关联的实例化数据
     *
     * @param applyinstId 申报实例id
     */
    @Override
    public void clearHistoryInst(String applyinstId) throws Exception {
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        Assert.notNull(aeaHiApplyinst, "无法找到历史申报实例, applyinstId: " + applyinstId);

        List<String> deletingMatinstIds = null;
        List<String> deletingInoutinstIds = null;
        List<String> deletingItemStateHistIds = null;
        List<String> deletingIteminstIds;
        List<String> deletingItemStateinstIds = null;
        List<String> deletingStageStateinstIds = null;

        deletingIteminstIds = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId,null)
                .stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deletingIteminstIds)) {
            List<AeaHiItemInoutinst> aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByIteminstIds(deletingIteminstIds.toArray(new String[0]));
            deletingInoutinstIds = aeaHiItemInoutinsts.stream().map(AeaHiItemInoutinst::getInoutinstId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deletingInoutinstIds)) {
                // 找到要删除的纸质件和复印件， 电子件不处理
                deletingMatinstIds = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(aeaHiItemInoutinsts.stream().map(AeaHiItemInoutinst::getMatinstId).toArray(String[]::new))
                        .stream().filter(matinst -> (matinst.getRealPaperCount() != null || matinst.getRealCopyCount() != null))
                        .map(AeaHiItemMatinst::getMatinstId).collect(Collectors.toList());
            }

            deletingItemStateHistIds = aeaLogItemStateHistMapper.listAeaLogItemStateHistByIteminstIds(deletingIteminstIds)
                    .stream().map(AeaLogItemStateHist::getStateHistId).collect(Collectors.toList());
            deletingItemStateinstIds = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null)
                    .stream().map(AeaHiItemStateinst::getItemStateinstId).collect(Collectors.toList());
        }

        if (ApplyType.UNIT.getValue().equals(aeaHiApplyinst.getIsSeriesApprove())) { // 并联
            deletingStageStateinstIds = aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null)
                    .stream().map(AeaHiParStateinst::getStageStateinstId).collect(Collectors.toList());
        }

        // 删除事项实例和材料实例关联的 inoutinst 实例(aea_hi_item_inoutinst)
        if (CollectionUtils.isNotEmpty(deletingInoutinstIds)) {
            aeaHiItemInoutinstMapper.batchDeleteAeaHiItemInoutinst(deletingInoutinstIds.toArray(new String[0]));
        }
        // 删除纸质件、复印件相关的材料实例(aea_hi_item_matinst)
        if (CollectionUtils.isNotEmpty(deletingMatinstIds)) {
            aeaHiItemMatinstMapper.deleteAeaHiItemMatinsts(deletingMatinstIds.toArray(new String[0]));
        }
        // 删除事项实例日志(aea_log_item_state_hist)和事项实例 (aea_hi_iteminst)
        if (CollectionUtils.isNotEmpty(deletingItemStateHistIds)) {
            aeaLogItemStateHistMapper.batchDeleteAeaLogItemStateHist(deletingItemStateHistIds);
        }
        if (CollectionUtils.isNotEmpty(deletingIteminstIds)) {
            aeaHiIteminstMapper.batchDeleteAeaHiIteminst(deletingIteminstIds.toArray(new String[0]));
        }
        // 删除事项情形实例(aea_hi_item_stateinst)
        if (CollectionUtils.isNotEmpty(deletingItemStateinstIds)) {
            aeaHiItemStateinstMapper.batchDeleteAeaHiItemStateinst(deletingItemStateinstIds);
        }
        if (CollectionUtils.isNotEmpty(deletingStageStateinstIds)) {
            aeaHiParStateinstMapper.batchDeleteAeaHiParStateinst(deletingStageStateinstIds.toArray(new String[0]));
        }
    }

    @Override
    public void bindApplyinstProj(String projInfoId, String applyinstId, String currentUserId) throws Exception {
        AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
        aeaApplyinstProj.setApplyinstId(applyinstId);
        aeaApplyinstProj.setProjInfoId(projInfoId);
        List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(aeaApplyinstProj);
        // 如果之前已经绑定过，则略过
        if (CollectionUtils.isEmpty(aeaApplyinstProjs)) {
            aeaApplyinstProj.setApplyinstProjId(UuidUtil.generateUuid());
            aeaApplyinstProj.setCreater(currentUserId);
            aeaApplyinstProj.setCreateTime(new Date());
            aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);
        }
    }

    @Override
    public void bindThemeAndProject(String[] projInfoIds, String themeVerId) {

        AeaParThemeVer themeVer = aeaParThemeVerMapper.selectOneById(themeVerId);
        if (themeVer == null) {
            return;
        }
        String themeId = themeVer.getThemeId();
        AeaParTheme aeaParTheme = aeaParThemeMapper.selectOneById(themeId);
        if (projInfoIds != null && projInfoIds.length > 0) {
            for (String projInfoId : projInfoIds) {
                AeaProjInfo projInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
                // 主线的才绑定
                if (Status.ON.equals(aeaParTheme.getIsMainline()) || StringUtils.isBlank(projInfo.getThemeId())) {
                    projInfo.setThemeId(themeId);
                }
                projInfo.setThemeVerId(themeVerId);
                projInfo.setModifyTime(new Date());
                projInfo.setModifier(SecurityContext.getCurrentUserId());
                aeaProjInfoMapper.updateAeaProjInfo(projInfo);
            }
        }
    }

    @Override
    public void updateApplyProcessVariable(AeaHiApplyinst updateApplyinst) throws Exception {
        if(updateApplyinst==null)
            throw new RuntimeException("更新流程变量失败：更新的对象为空！");

        String applyinstId = updateApplyinst.getApplyinstId();
        ActStoAppinst actStoAppinst = new ActStoAppinst();
        actStoAppinst.setMasterRecordId(applyinstId);
        List <ActStoAppinst> appinstList = actStoAppinstMapper.listActStoAppinst(actStoAppinst);

        if(appinstList!=null&&appinstList.size()>0){
            ActStoAppinst masterAppinst = appinstList.get(0);

            String masterProcinstId = masterAppinst.getProcinstId();
            //加上流程是否挂起判断
            boolean masterIsSuspended = false;
            if (bpmProcessService.isProcessSuspended(masterProcinstId)) {
                bpmProcessService.activateProcessInstanceById(masterProcinstId);
                masterIsSuspended = true;
            }
            runtimeService.setVariable(masterProcinstId,"form",updateApplyinst);
            //如果原来流程是挂起的，则继续将流程挂起
            if(masterIsSuspended){
                bpmProcessService.suspendProcessInstanceById(masterProcinstId);
            }

            ActStoAppinstSubflow subflow = new ActStoAppinstSubflow();
            subflow.setAppinstId(masterAppinst.getAppinstId());
            List<ActStoAppinstSubflow> subflowList = actStoAppinstSubflowMapper.listActStoAppinstSubflow(subflow);
            if(subflowList!=null&&subflowList.size()>0){
                for(ActStoAppinstSubflow stoAppinstSubflow:subflowList){
                    String subflowProcinstId = stoAppinstSubflow.getSubflowProcinstId();

                    //加上流程是否挂起判断
                    boolean subFlowIsSuspended = false;
                    if (bpmProcessService.isProcessSuspended(subflowProcinstId)) {
                        bpmProcessService.activateProcessInstanceById(subflowProcinstId);
                        subFlowIsSuspended = true;
                    }

                    runtimeService.setVariable(subflowProcinstId,"form",updateApplyinst);

                    //如果原来流程是挂起的，则继续将流程挂起
                    if(subFlowIsSuspended){
                        bpmProcessService.suspendProcessInstanceById(subflowProcinstId);
                    }
                }
            }
        }

    }
}
