package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaHiApplyinstConstants;
import com.augurit.aplanmis.common.constants.AeaHiIteminstConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.ItemCompletedEvent;
import com.augurit.aplanmis.common.event.vo.IteminstEventVo;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import com.augurit.aplanmis.common.service.dic.IteminstCodeBscRuleCodeStrategy;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class AeaHiIteminstServiceImpl implements AeaHiIteminstService {

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AplanmisEventPublisher publisher;

    @Autowired
    private IteminstCodeBscRuleCodeStrategy iteminstCodeBscRuleCodeStrategy;

    @Override
    public List<AeaHiIteminst> getAeaHiIteminstListByStageinstId(String stageinstId) throws Exception {
        if (StringUtils.isBlank(stageinstId)) throw new InvalidParameterException("参数stageinstId为空！");
        return aeaHiIteminstMapper.getAeaHiIteminstListByStageinstId(stageinstId);
    }

    @Override
    public List<AeaHiIteminst> getAeaHiIteminstListByApplyinstId(String applyinstId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) throw new InvalidParameterException("参数applyinstId为空！");
        return aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId,"0");
    }

    @Override
    public List<AeaHiIteminst> getAeaHiIteminstListByApplyinstIds(List<String> applyinstIds, String isSeriesApprove) throws Exception {
        if (applyinstIds == null || applyinstIds.size() == 0) throw new InvalidParameterException("参数applyinstIds为空！");
        return aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstIds(applyinstIds, isSeriesApprove);
    }

    @Override
    public List<AeaHiIteminst> getAeaHiIteminstListByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception {
        List<AeaHiIteminst> list = getAeaHiIteminstListByApplyinstId(applyinstId);
        if (StringUtils.isNotBlank(iteminstId) && list.size() > 0) {
            return list.stream().filter(aeaHiIteminst -> iteminstId.equals(aeaHiIteminst.getIteminstId()))
                    .collect(Collectors.toList()); //将Stream转化为List
        }
        return list;
    }

    @Override
    public AeaHiIteminst insertAeaHiIteminst(String seriesinstId, String itemVerId, String branchOrgMap) throws Exception {
        if (StringUtils.isBlank(seriesinstId)) throw new InvalidParameterException("参数seriesinstId为空！");
        if (StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (aeaItemBasic != null) {
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, itemVerId);
            return insertAeaHiIteminst(seriesinstId, aeaItemBasic, orgId);
        }
        return null;
    }

    @Override
    public void updateAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception {
        if (StringUtils.isBlank(aeaHiIteminst.getIteminstId())) throw new Exception("缺少参数！");
        aeaHiIteminstMapper.updateAeaHiIteminst(aeaHiIteminst);
    }

    @Override
    public AeaHiIteminst insertAeaHiIteminstAndTriggerAeaLogItemStateHist(String seriesinstId, String itemVerId, String branchOrgMap, String taskinstId, String appinstId) throws Exception {
        AeaHiIteminst aeaHiIteminst = this.insertAeaHiIteminst(seriesinstId, itemVerId, branchOrgMap);
        if (aeaHiIteminst != null) {
            aeaLogItemStateHistService.insertTriggerAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), taskinstId, appinstId, null, ItemStatus.RECEIVE_APPLY.getValue(), aeaHiIteminst.getApproveOrgId());
        }
        return aeaHiIteminst;
    }

    @Override
    public AeaHiIteminst insertAeaHiIteminst(String themeVerId, String stageinstId, String itemVerId, String branchOrgMap) throws Exception {
        if (StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        if (StringUtils.isBlank(stageinstId)) throw new InvalidParameterException("参数stageinstId为空！");
        if (StringUtils.isBlank(themeVerId)) throw new InvalidParameterException("参数themeVerId为空！");
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (aeaItemBasic != null) {
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, itemVerId);
            return insertAeaHiIteminst(themeVerId, stageinstId, aeaItemBasic, orgId);
        }
        return null;
    }

    @Override
    public AeaHiIteminst insertAeaHiIteminstAndTriggerAeaLogItemStateHist(String themeVerId, String stageinstId, String itemVerId, String branchOrgMap, String taskinstId, String appinstId) throws Exception {
        AeaHiIteminst aeaHiIteminst = this.insertAeaHiIteminst(themeVerId, stageinstId, itemVerId, branchOrgMap);
        if (aeaHiIteminst != null) {
            aeaLogItemStateHistService.insertTriggerAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), taskinstId, appinstId, null, ItemStatus.RECEIVE_APPLY.getValue(), aeaHiIteminst.getApproveOrgId());
        }
        return aeaHiIteminst;
    }

    @Override
    public List<AeaHiIteminst> batchInsertAeaHiIteminst(String seriesinstId, List<String> itemVerIds, String branchOrgMap) throws Exception {
        if (StringUtils.isBlank(seriesinstId)) throw new InvalidParameterException("参数seriesinstId为空！");
        if (itemVerIds.size() <= 0) throw new InvalidParameterException("参数itemVerIds为空！");
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(itemVerIds);
        List<AeaHiIteminst> iteminstList = new ArrayList<>();
        for (AeaItemBasic aeaItemBasic : list) {
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, aeaItemBasic.getItemVerId());
            AeaHiIteminst aeaHiIteminst = insertAeaHiIteminst(seriesinstId, aeaItemBasic, orgId);
            iteminstList.add(aeaHiIteminst);
        }
        return iteminstList;
    }

    @Override
    public List<AeaHiIteminst> batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(String seriesinstId, List<String> itemVerIds, String branchOrgMap, String taskinstId, String appinstId) throws Exception {
        List<AeaHiIteminst> aeaHiIteminstList = this.batchInsertAeaHiIteminst(seriesinstId, itemVerIds, branchOrgMap);
        List<AeaLogItemStateHist> aeaLogItemStateHistList = new ArrayList<>();
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.constructTriggerAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), taskinstId, appinstId, null, ItemStatus.RECEIVE_APPLY.getValue(), aeaHiIteminst.getApproveOrgId());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaLogItemStateHistList.add(aeaLogItemStateHist);
        }
        aeaLogItemStateHistService.batchInsertTriggerAeaLogItemStateHist(aeaLogItemStateHistList);
        return aeaHiIteminstList;
    }

    @Override
    public List<AeaHiIteminst> batchInsertAeaHiIteminst(String themeVerId, String stageinstId, List<String> itemVerIds, String branchOrgMap) throws Exception {

        if (StringUtils.isBlank(themeVerId)) throw new InvalidParameterException("参数themeVerId为空！");
        if (StringUtils.isBlank(stageinstId)) throw new InvalidParameterException("参数stageinstId为空！");
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(itemVerIds);
        List<AeaHiIteminst> iteminstList = new ArrayList<>();
        for (AeaItemBasic aeaItemBasic : list) {
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, aeaItemBasic.getItemVerId());
            AeaHiIteminst aeaHiIteminst = insertAeaHiIteminst(themeVerId, stageinstId, aeaItemBasic, orgId);
            iteminstList.add(aeaHiIteminst);
        }
        return iteminstList;
    }

    @Override
    public List<AeaHiIteminst> batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(String themeVerId, String stageinstId, List<String> itemVerIds, String branchOrgMap, String taskinstId, String appinstId) throws Exception {
        List<AeaHiIteminst> aeaHiIteminstList = this.batchInsertAeaHiIteminst(themeVerId, stageinstId, itemVerIds, branchOrgMap);
        List<AeaLogItemStateHist> aeaLogItemStateHistList = new ArrayList<>();
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.constructTriggerAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), taskinstId, appinstId, null, ItemStatus.RECEIVE_APPLY.getValue(), aeaHiIteminst.getApproveOrgId());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaLogItemStateHistList.add(aeaLogItemStateHist);
        }
        aeaLogItemStateHistService.batchInsertTriggerAeaLogItemStateHist(aeaLogItemStateHistList);
        return aeaHiIteminstList;
    }

    private AeaHiIteminst insertAeaHiIteminst(String seriesinstId, AeaItemBasic aeaItemBasic, String orgId) throws Exception {
        if (StringUtils.isBlank(seriesinstId)) throw new InvalidParameterException("参数seriesinstId为空！");
        AeaHiIteminst iteminst = createIteminst(null, null, seriesinstId, aeaItemBasic, AeaHiApplyinstConstants.SERIESINST_APPLY, orgId);
        return iteminst;
    }

    private AeaHiIteminst insertAeaHiIteminst(String themeVerId, String stageinstId, AeaItemBasic aeaItemBasic, String orgId) throws Exception {
        if (StringUtils.isBlank(themeVerId)) throw new InvalidParameterException("参数themeinstId为空！");
        if (StringUtils.isBlank(stageinstId)) throw new InvalidParameterException("参数stageinstId为空！");
        AeaHiIteminst iteminst = createIteminst(themeVerId, stageinstId, null, aeaItemBasic, AeaHiApplyinstConstants.STAGEINST_APPLY, orgId);
        return iteminst;
    }

    private AeaHiIteminst createIteminst(String themeVerId, String stageinstId, String seriesinstId, AeaItemBasic aeaItemBasic, String isSeriesApprove, String orgId) throws Exception {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaHiIteminst aeaHiIteminst = new AeaHiIteminst();
        aeaHiIteminst.setIteminstId(UUID.randomUUID().toString());
        aeaHiIteminst.setItemId(aeaItemBasic.getItemId());
        aeaHiIteminst.setItemVerId(aeaItemBasic.getItemVerId());
        aeaHiIteminst.setApproveOrgId(StringUtils.isNotBlank(orgId) ? orgId : aeaItemBasic.getOrgId());
        aeaHiIteminst.setIsSeriesApprove(isSeriesApprove);
        aeaHiIteminst.setIteminstCode(iteminstCodeBscRuleCodeStrategy.generateCode(aeaItemBasic.getItemCode(), aeaItemBasic.getItemCode(), null, rootOrgId));
        aeaHiIteminst.setIteminstName(aeaItemBasic.getItemName());
        aeaHiIteminst.setIteminstState(ItemStatus.RECEIVE_APPLY.getValue());
        if (AeaHiApplyinstConstants.STAGEINST_APPLY.equals(isSeriesApprove)) {
            aeaHiIteminst.setStageinstId(stageinstId);//  并联事项实例
            aeaHiIteminst.setThemeVerId(themeVerId);
        } else {
            aeaHiIteminst.setSeriesinstId(seriesinstId);//  串联事项实例
        }
        aeaHiIteminst.setIsToleranceAccept(AeaHiIteminstConstants.IS_TOLERANCE_ACCEPT_FLASE);
        Date date = new Date();
        aeaHiIteminst.setStartTime(date);
        aeaHiIteminst.setCreateTime(date);
        aeaHiIteminst.setRegisterTime(date);
        aeaHiIteminst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiIteminst.setRootOrgId(rootOrgId);
        aeaHiIteminstMapper.insertAeaHiIteminst(aeaHiIteminst); //    新增事项实例
        return aeaHiIteminst;
    }

    @Override
    public AeaHiIteminst saveAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception {
        if (StringUtils.isBlank(aeaHiIteminst.getIteminstId())) {
            aeaHiIteminst.setIteminstId(UUID.randomUUID().toString());
            aeaHiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiIteminstMapper.insertAeaHiIteminst(aeaHiIteminst);
        } else {
            aeaHiIteminstMapper.updateAeaHiIteminst(aeaHiIteminst);
        }
        return aeaHiIteminst;
    }

    @Override
    public void deleteAeaHiIteminst(String id) throws Exception {
        if (StringUtils.isBlank(id)) throw new InvalidParameterException("参数id为空！");
        aeaHiIteminstMapper.deleteAeaHiIteminst(id);
    }

    @Override
    public List<AeaHiIteminst> listAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception {
        aeaHiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiIteminstMapper.listAeaHiIteminst(aeaHiIteminst);
    }

    @Override
    public AeaHiIteminst getAeaHiIteminstById(String id) throws Exception {
        if (StringUtils.isBlank(id)) return null;
        return aeaHiIteminstMapper.getAeaHiIteminstById(id);
    }

    @Override
    public PageInfo<AeaHiIteminst> listAeaHiIteminst(AeaHiIteminst aeaHiIteminst, int pageNum, int pageSize) throws Exception {
        aeaHiIteminst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(pageNum, pageSize);
        List<AeaHiIteminst> list = aeaHiIteminstMapper.listAeaHiIteminst(aeaHiIteminst);
        return new PageInfo<>(list);
    }

    @Override
    public void updateAeaHiIteminstStateAndBusinessState(String iteminstId, String iteminstState) throws Exception {
        if (StringUtils.isBlank(iteminstId)) throw new InvalidParameterException("参数iteminstId为空！");
        AeaHiIteminst updateParam = new AeaHiIteminst();
        updateParam.setIteminstId(iteminstId);
        updateParam.setIteminstState(iteminstState);
        if (iteminstState.equals(ItemStatus.AGREE.getValue()) || iteminstState.equals(ItemStatus.AGREE_TOLERANCE.getValue())
                || iteminstState.equals(ItemStatus.DISAGREE.getValue()) || iteminstState.equals(ItemStatus.OUT_SCOPE.getValue())
                || iteminstState.equals(ItemStatus.REFUSE_DEAL.getValue())) {
            updateParam.setEndTime(new Date());
        }
        if (ItemStatus.ACCEPT_DEAL.getValue().equals(iteminstState)) {
            updateParam.setAcceptTime(new Date());
        }
        updateParam.setModifier(SecurityContext.getCurrentUserName());
        updateParam.setModifyTime(new Date());
        aeaHiIteminstMapper.updateAeaHiIteminst(updateParam);
        if (iteminstState.equals(ItemStatus.AGREE.getValue()) || iteminstState.equals(ItemStatus.AGREE_TOLERANCE.getValue())) {
            //事项办结通过时发布事件，计算阶段办结状态
            publisher.publishEvent(new ItemCompletedEvent(this, iteminstId, SecurityContext.getCurrentOrgId(), SecurityContext.getCurrentUserId()));
        }
        publisher.conditionalPublishEvent4Iteminst(new IteminstEventVo(iteminstId, iteminstState));
    }

    @Override
    public void updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String iteminstState, String opuOrgId) throws Exception {
        AeaHiIteminst iteminst = saveAndReturnAeaHiIteminstState(iteminstId, iteminstState);
        aeaLogItemStateHistService.insertTriggerAeaLogItemStateHist(iteminstId, taskinstId, appinstId, iteminst.getIteminstState(), iteminstState, opuOrgId);
    }

    @Override
    public void updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState, String opuOrgId) throws Exception {
        AeaHiIteminst iteminst = saveAndReturnAeaHiIteminstState(iteminstId, iteminstState);
        aeaLogItemStateHistService.insertOpsAeaLogItemStateHist(iteminstId, opsUserOpinion, opsAction, opsMemo, iteminst.getIteminstState(), iteminstState, opuOrgId);
    }

    @Override
    public void updateAeaHiIteminstStateAndInsertOpsLinkBusAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState, String opuOrgId, String busTableName, String busPkName, String busRecordId) throws Exception {
        AeaHiIteminst iteminst = saveAndReturnAeaHiIteminstState(iteminstId, iteminstState);
        aeaLogItemStateHistService.insertOpsLinkBusAeaLogItemStateHist(iteminstId, opsUserOpinion, opsAction, opsMemo, iteminst.getIteminstState(), iteminstState, opuOrgId, busTableName, busPkName, busRecordId);
    }

    @Override
    public Set<AeaHiIteminst> getAeaHiIteminstByProjInfoIdAndStageId(String projInfoId, String stageId, String isOptionItem, String rootOrgId) throws Exception {
        if (StringUtils.isBlank(projInfoId)) throw new InvalidParameterException("参数projInfoId为空！");
        Set<AeaHiIteminst> list = new HashSet<>();
        List<AeaItemBasic> itemBasicList = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, isOptionItem, null, rootOrgId);
        if (itemBasicList.size() == 0) return list;
        List<AeaHiIteminst> iteminsts = aeaHiIteminstMapper.getSeriesAeaHiIteminstByProjInfoId(projInfoId);
        if (iteminsts.size() == 0) return list;
        for (AeaItemBasic aeaItemBasic : itemBasicList) {
            for (AeaHiIteminst aeaHiIteminst : iteminsts) {
                if (aeaItemBasic.getItemVerId().equals(aeaHiIteminst.getItemVerId())) {
                    aeaHiIteminst.setDueNum(aeaItemBasic.getDueNum());
                    list.add(aeaHiIteminst);
                }
            }
        }
        return list;
    }

    @Override
    public void updateAeaHiIteminstIsToleranceAccept(String iteminstId, String isToleranceAccept) throws Exception {
        if (StringUtils.isBlank(iteminstId))
            throw new InvalidParameterException("事项实例ID为空！");
        if (StringUtils.isBlank(isToleranceAccept))
            throw new InvalidParameterException("是否为容缺受理参数为空！");

        AeaHiIteminst iteminst = new AeaHiIteminst();
        iteminst.setIteminstId(iteminstId);
        iteminst.setIsToleranceAccept(isToleranceAccept);
        iteminst.setModifier(SecurityContext.getCurrentUserName());
        iteminst.setModifyTime(new Date());
        aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
    }

    @Override
    public void updateAeaHiIteminstToleranceTime(String iteminstId, double toleranceTime, String timeruleId) throws Exception {
        if (StringUtils.isBlank(iteminstId))
            throw new InvalidParameterException("事项实例ID为空！");
        if (StringUtils.isBlank(timeruleId))
            throw new InvalidParameterException("容缺时限规则id为空！");
        if (toleranceTime < 0)
            throw new InvalidParameterException("容缺时限值必须大于等于0！");

        AeaHiIteminst iteminst = new AeaHiIteminst();
        iteminst.setIteminstId(iteminstId);
        iteminst.setToleranceTime(toleranceTime);
        iteminst.setTimeruleId(timeruleId);
        iteminst.setModifier(SecurityContext.getCurrentUserName());
        iteminst.setModifyTime(new Date());
        aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
    }

    private AeaHiIteminst saveAndReturnAeaHiIteminstState(String iteminstId, String iteminstState) throws Exception {
        if (StringUtils.isBlank(iteminstId)) throw new InvalidParameterException("参数iteminstId为空！");
        AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        if (iteminst != null) {
            updateAeaHiIteminstStateAndBusinessState(iteminstId, iteminstState);
        } else {
            throw new Exception("根据【" + iteminstId + "】找不到该事项实例");
        }
        return iteminst;
    }

    @Override
    public long countApproveProjInfoListByUnitOrLinkman(String unitInfoId, String userInfoId, String isAll) {
        return aeaHiIteminstMapper.countApproveProjInfoListByUnitOrLinkman(unitInfoId, userInfoId, isAll);
    }

    @Override
    public Map<String, HandleStatus> queryItemStatusByStageIdAndProjInfoId(List<String> stageIds, String projInfoId, String rootOrgId) {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        Assert.isTrue(StringUtils.isNotBlank(rootOrgId), "rootOrgId is null");

        List<AeaHiIteminst> allIteminstList = new ArrayList<>();

        // 查询阶段下的事项实例
        List<AeaHiIteminst> stageIteminsts = aeaHiIteminstMapper.listAeaHiIteminstByProjInfoIdAndStageIds(stageIds, projInfoId, rootOrgId);

        // 查询并行申报事项实例
        List<AeaHiIteminst> seriesIteminsts = aeaHiIteminstMapper.listSeriesAeaHiIteminstByProjInfoId(projInfoId, rootOrgId);

        allIteminstList.addAll(stageIteminsts);
        allIteminstList.addAll(seriesIteminsts);

        Map<String, HandleStatus> resultMap = new HashMap<>();
        allIteminstList.forEach(iteminst -> {
            // 事项办理状态 0:未办 1: 已办 2: 办理中
            HandleStatus status;
            String iteminstState = iteminst.getIteminstState();
            if (iteminstState.equals(ItemStatus.AGREE.getValue())) {
                status = HandleStatus.FINISHED;
            } else if (iteminstState.equals(ItemStatus.AGREE_TOLERANCE.getValue())) {
                status = HandleStatus.TOLERENCE_PASS;
            } else if (ItemStatus.isHandling(iteminstState)) {
                status = HandleStatus.HANDLING;
            } else {
                status = HandleStatus.UN_FINISHED;
            }
            resultMap.put(iteminst.getItemId(), status);
        });
        // 把对应的标准事项也放进去
        Map<String, HandleStatus> catalogMap = new HashMap<>(resultMap.size());
        for (Map.Entry<String, HandleStatus> entry : resultMap.entrySet()) {
            AeaItemBasic catalogItem = aeaItemBasicService.getCatalogItemByCarryOutItemId(entry.getKey(), null);
            if (catalogItem != null) {
                catalogMap.put(catalogItem.getItemId(), entry.getValue());
            }
        }
        resultMap.putAll(catalogMap);
        return resultMap;
    }

    @Override
    public int countTotalItemByStates(String[] states, String rootOrgId) throws Exception {
        return aeaHiIteminstMapper.countTotalItemByStates(states, rootOrgId);
    }

    @Override
    public int countCurrentMonthCountItemByStates(String[] states, String rootOrgId) throws Exception {
        return aeaHiIteminstMapper.countCurrentMonthCountItemByStates(states, rootOrgId);
    }

    @Override
    public void updateAeaHiIteminstStateAndInsertTriggerAeaLogApplyinstStateHist(String iteminstId, String taskinstId, String appinstId, String iteminstState) throws Exception {
        AeaHiIteminst iteminst = saveAndReturnAeaHiIteminstState(iteminstId, iteminstState);
        aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(iteminstId, taskinstId, appinstId, iteminst.getIteminstState(), iteminstState, iteminst.getApproveOrgId());
    }


    @Override
    public void updateAeaHiIteminstStateAndInsertOpsAeaLogApplyinstStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState) throws Exception {
        AeaHiIteminst iteminst = saveAndReturnAeaHiIteminstState(iteminstId, iteminstState);
        aeaLogApplyStateHistService.insertOpsAeaLogApplyStateHist(iteminstId, opsUserOpinion, opsAction, opsMemo, iteminst.getIteminstState(), iteminstState, iteminst.getApproveOrgId());
    }

    @Override
    public void batchDeleteAeaHiIteminstAndBatchDelAeaLogItemStateHist(String[] iteminstIds) {
        if (iteminstIds.length > 0) {
            aeaHiIteminstMapper.batchDeleteAeaHiIteminst(iteminstIds);
            List<AeaLogItemStateHist> logs = aeaLogItemStateHistService.findAeaLogItemStateHistByIteminstIds(iteminstIds);
            if (logs.size() > 0) {
                List<String> ids = logs.stream().map(AeaLogItemStateHist::getStateHistId).collect(Collectors.toList());
                aeaLogItemStateHistService.batchDeleteAeaLogItemStateHist(ids);
            }
        }
    }

}
