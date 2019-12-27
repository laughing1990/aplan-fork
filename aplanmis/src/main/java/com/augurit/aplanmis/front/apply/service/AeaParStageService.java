package com.augurit.aplanmis.front.apply.service;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.receive.constant.ReceiveConstant;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.ApplyinstIdVo;
import com.augurit.aplanmis.front.apply.vo.ForminstVo;
import com.augurit.aplanmis.front.apply.vo.ParallelItemApplyinstVo;
import com.augurit.aplanmis.front.apply.vo.ParallelItemStateVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.receipt.SaveReceiptsVo;
import com.augurit.aplanmis.front.apply.vo.stash.ParallelStashResultVo;
import com.augurit.aplanmis.front.apply.vo.stash.ParallelUnstashVo;
import com.augurit.aplanmis.front.apply.vo.stash.StashVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 并联申报service
 * author:sam
 */
@Service
@Transactional
@Slf4j
public class AeaParStageService extends RestApplyService {
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaSeriesService aeaSeriesService;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;

    /**
     * 打印回执
     * @return 申报实例id
     */
    public ApplyinstIdVo printReceipts(StageApplyDataVo stageApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        List<AeaHiApplyinst> aeaHiApplyinsts = instantiateStageAndSeries(stageApplyDataVo, currentWindowId);
        Assert.isTrue(aeaHiApplyinsts.size() > 0, "实例化申报实例失败");

        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        for (AeaHiApplyinst ahi : aeaHiApplyinsts) {
            // 启动并挂起流程
            doStartApplyAndSuspend(ahi);

            // 确认申报实例的最终状态
            confirmAeaHiApplyinst(ahi, stageApplyDataVo, "2");

            applyinstIdVo.addApplyinstId(ahi);
        }

        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromStageApplyDataVo(stageApplyDataVo, applyinstIdVo, new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()}));

        return applyinstIdVo;
    }

    /**
     * 发起申报
     * @return 申报实例id
     */
    public ApplyinstIdVo startApply(StageApplyDataVo stageApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        List<AeaHiApplyinst> aeaHiApplyinsts = instantiateStageAndSeries(stageApplyDataVo, currentWindowId);
        Assert.isTrue(aeaHiApplyinsts.size() > 0, "实例化申报实例失败");

        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        for (AeaHiApplyinst ahi : aeaHiApplyinsts) {
            // 启动流程
            doStartApply(ahi, stageApplyDataVo.getComments(), currentWindowId);

            // 确认申报实例的最终状态
            confirmAeaHiApplyinst(ahi, stageApplyDataVo, "0");

            applyinstIdVo.addApplyinstId(ahi);
        }

        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromStageApplyDataVo(stageApplyDataVo, applyinstIdVo, new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()}));

        return applyinstIdVo;
    }

    public List<AeaHiApplyinst> instantiateStageAndSeries(StageApplyDataVo stageApplyDataVo, String currentWindowId) throws Exception {
        List<AeaHiApplyinst> aeaHiApplyinsts = new ArrayList<>();

        String parallelApplyinstId = null;
        // 并联实例化
        if (CollectionUtils.isNotEmpty(stageApplyDataVo.getItemVerIds())) {
            AeaHiApplyinst aeaHiApplyinst = instantiate(stageApplyDataVo, currentWindowId);
            Assert.notNull(aeaHiApplyinst, "并联申报实例化失败");
            Assert.notEmpty(aeaHiApplyinst.getIteminstIds(), "并联申报事项实例化失败");
            parallelApplyinstId = aeaHiApplyinst.getApplyinstId();
            aeaHiApplyinsts.add(aeaHiApplyinst);
        }

        // 并行事项实例化
        if (CollectionUtils.isNotEmpty(stageApplyDataVo.getPropulsionItemVerIds())) {
            List<SeriesApplyDataVo> seriesApplyDataVos = StageApplyDataVo.toSeriesApplyDataVosWhenPropulsionApply(stageApplyDataVo, parallelApplyinstId);
            // 为并行事项绑定之前已经生成的申报实例id
            aeaSeriesService.bindApplyinstId(parallelApplyinstId, seriesApplyDataVos);
            for (SeriesApplyDataVo seriesApplyDataVo : seriesApplyDataVos) {
                AeaHiApplyinst seriesAeaHiApplyinst = aeaSeriesService.instantiate(seriesApplyDataVo, currentWindowId);
                Assert.hasText(seriesAeaHiApplyinst.getIteminstId(), "并行申报事项实例化失败");
                aeaHiApplyinsts.add(seriesAeaHiApplyinst);
            }
        }
        return aeaHiApplyinsts;
    }

    // 不予受理
    public ApplyinstIdVo inadmissible(StageApplyDataVo stageApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();

        AeaHiApplyinst aeaHiApplyinst = instantiate(stageApplyDataVo, currentWindowId);
        applyinstIdVo.addApplyinstId(aeaHiApplyinst);

        // 启动并结束流程
        doInadmissible(aeaHiApplyinst, stageApplyDataVo.getComments());

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, stageApplyDataVo, "0");

        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromStageApplyDataVo(stageApplyDataVo, applyinstIdVo, new String[]{ReceiveConstant.ReceiveTypeEnum.REJECT_TYPE.getCode()}));

        return applyinstIdVo;
    }

    private AeaHiApplyinst instantiate(StageApplyDataVo stageApplyDataVo, String currentWindowId) throws Exception {
        // 参数校验
        validate(stageApplyDataVo);

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataVo.getStageId());
        Assert.notNull(aeaParStage, "无法找到要申报的阶段");
        stageApplyDataVo.setThemeVerId(aeaParStage.getThemeVerId());

        // 获取申报实例，如果不存在，则创建
        AeaHiApplyinst aeaHiApplyinst = createAeaHiApplyinstIfNotExists(stageApplyDataVo, stageApplyDataVo.getParallelApplyinstId());
        aeaHiApplyinst.setAppId(aeaParStage.getAppId());

        // 获取申报日志记录，如果不存在，则创建
        String appinstId = createAeaLogApplyStateHistIfNotExists(aeaHiApplyinst, currentWindowId);
        aeaHiApplyinst.setAppinstId(appinstId);

        // 如果需要的话，清除历史数据
        clearHistoryDataIfNecessary(aeaHiApplyinst);

        // 获取并联申报实例，如果不存在，则创建
        AeaHiParStageinst aeaHiParStageinst = createAeaHiParStageinstIfNotExists(appinstId, aeaHiApplyinst, stageApplyDataVo);

        // 创建事项实例 和 事项日志实例
        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(stageApplyDataVo.getThemeVerId(), aeaHiParStageinst.getStageinstId(), stageApplyDataVo.getItemVerIds(), stageApplyDataVo.getBranchOrgMap(), null, appinstId);
        aeaHiApplyinst.setIteminstIds(aeaHiIteminsts.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toSet()));

        // 创建情形实例
        aeaHiParStateinstService.batchInsertAeaHiParStateinst(aeaHiApplyinst.getApplyinstId(), aeaHiParStageinst.getStageinstId(), stageApplyDataVo.getStateIds(), SecurityContext.getCurrentUserName());

        // 简单合并申报的情况下，可能存在事项自己的情形列表
        saveItemStateBySimpleMerge(stageApplyDataVo.getStageId(), stageApplyDataVo.getParallelItemStateIds(), stageApplyDataVo.getItemVerIds(), aeaHiApplyinst.getApplyinstId(), aeaHiParStageinst.getStageinstId());

        // 创建输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(stageApplyDataVo.getMatinstsIds(), aeaHiApplyinst.getApplyinstId(), SecurityContext.getCurrentUserName());

        // 创建前后置事项输出材料关联
        this.createPre_PostItemMat(stageApplyDataVo.getMatinstsIds(), stageApplyDataVo.getStageId(), aeaHiIteminsts);

        String projInfoId = stageApplyDataVo.getProjInfoIds()[0];

        // 保存申报实例与项目关联
        applyCommonService.bindApplyinstProj(projInfoId, aeaHiApplyinst.getApplyinstId(), SecurityContext.getCurrentUserId());

        // 保存申报主体
        saveApplySubject(aeaHiApplyinst, stageApplyDataVo);

        // 项目绑定主题
        applyCommonService.bindThemeAndProject(stageApplyDataVo.getProjInfoIds(), aeaParStage.getThemeVerId());

        // 设置流程审批相关信息
        fillApproveInfo(aeaHiApplyinst, stageApplyDataVo, aeaHiIteminsts);

        return aeaHiApplyinst;
    }

    protected void validate(StageApplyDataVo stageApplyDataVo) {
        super.validate(stageApplyDataVo);

        Assert.hasText(stageApplyDataVo.getStageId(), "阶段参数不能为空");
    }

    private AeaHiParStageinst createAeaHiParStageinstIfNotExists(String appinstId, AeaHiApplyinst aeaHiApplyinst, StageApplyDataVo stageApplyDataVo) throws Exception {
        AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
        if (aeaHiParStageinst == null) {
            aeaHiParStageinst = aeaHiParStageinstService.createAeaHiParStageinst(aeaHiApplyinst.getApplyinstId(), stageApplyDataVo.getStageId(), stageApplyDataVo.getThemeVerId(), appinstId, null);
        }
        return aeaHiParStageinst;
    }

    private void fillApproveInfo(AeaHiApplyinst aeaHiApplyinst, StageApplyDataVo stageApplyDataVo, List<AeaHiIteminst> aeaHiIteminsts) {
        Map<String, Boolean> approveOrgMap = new HashMap<>();
        Map<String, Boolean> isBranchHandle = new HashMap<>();
        Map<String, Boolean> iteminstMap = new HashMap<>();//初始化选中情形绑定的事项
        aeaHiIteminsts.forEach(aeaHiIteminst -> {
            AeaItemBasic aeaItemBasic;
            try {
                aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(aeaHiIteminst.getItemVerId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("根据itemVerId查询aeaItemBasic错误, itemVerId: {}", aeaHiIteminst.getItemVerId());
                return;
            }
            // 分局承办设置 根据itemVerId查map， 有则说明是分局承办，否则市局承办
            String approveOrgCode = opuOmOrgMapper.getOrg(aeaHiIteminst.getApproveOrgId()).getOrgCode();
            if (StringUtils.isNotBlank(BusinessUtil.getOrgIdFromBranchOrgMap(stageApplyDataVo.getBranchOrgMap(), aeaHiIteminst.getItemVerId()))) {
                approveOrgMap.put(approveOrgCode, true);
                isBranchHandle.put(aeaItemBasic.getItemCategoryMark(), true);
                iteminstMap.put(aeaItemBasic.getItemCategoryMark(), true);
            } else {
                approveOrgMap.put(approveOrgCode, false);
                isBranchHandle.put(aeaItemBasic.getItemCategoryMark(), false);
                iteminstMap.put(aeaItemBasic.getItemCategoryMark(), false);
            }
        });
        aeaHiApplyinst.setIsBranchHandle(isBranchHandle);
        aeaHiApplyinst.setApprovalOrgCode(approveOrgMap);
        aeaHiApplyinst.setIteminsts(iteminstMap);

        // 用于流程启动情形
        aeaHiApplyinst.setStateinsts(applyCommonService.filterProcessStartConditions(stageApplyDataVo.getStateIds(), ApplyType.UNIT));
    }

    /**
     * 简单合并时即多事项直接合并办理，判断并联事项下是否有选择情形，保存对应的情形实例
     *
     * @param itemVerIds  并联申报事项
     * @param applyinstId 并联申报实例id
     * @param stageinstId 阶段实例id
     */
    private void saveItemStateBySimpleMerge(String stageId, List<ParallelItemStateVo> parallelItemStateIds, List<String> itemVerIds, String applyinstId, String stageinstId) throws Exception {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        // 多事项直接合并办理 handWay=0 时才处理
        if (aeaParStage == null || "1".equals(aeaParStage.getHandWay())) {
            return;
        }
        if (parallelItemStateIds != null && parallelItemStateIds.size() > 0) {
            Map<String, List<String>> parallelItemStateIdMap = new HashMap<>();
            parallelItemStateIds.forEach(p -> parallelItemStateIdMap.put(p.getItemVerId(), p.getStateIds()));

            List<AeaHiIteminst> iteminsts = new ArrayList<>(aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId));

            itemVerIds.forEach(itemVerId -> {
                if (CollectionUtils.isNotEmpty(parallelItemStateIdMap.get(itemVerId))) {
                    String[] itemStateIds = parallelItemStateIdMap.get(itemVerId).toArray(new String[0]);

                    try {
                        for (AeaHiIteminst iteminst : iteminsts) {
                            if (itemVerId.equals(iteminst.getItemVerId())) {
                                //判断事项是否为告知承诺制
                                applyCommonService.setInformCommit(itemStateIds, ApplyType.SERIES, iteminst);
                                break;
                            }
                        }

                        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(applyinstId, null, stageinstId, itemStateIds, SecurityContext.getCurrentUserName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("简单合并申报保存事项的情形实例时失败, itemVerIds: {}", itemVerIds);
                        throw new RuntimeException("简单合并申报保存事项的情形实例时失败", e);
                    }
                }
            });
        }
    }

    private void createPre_PostItemMat(String[] matinstIds, String stageId, List<AeaHiIteminst> aeaHiIteminsts) throws Exception {
        for (AeaHiIteminst iteminst : aeaHiIteminsts) {
            List<AeaItemMat> mats = aeaItemMatService.getOfficeMatsByStageItemVerIds(stageId, new String[]{iteminst.getItemVerId()});
            if (mats.size() > 0) {
                boolean flag = false;
                List<AeaHiItemMatinst> matinsts = aeaHiItemMatinstService.getMatinstListByStageIteminstId(iteminst.getIteminstId());
                for (AeaHiItemMatinst matinst : matinsts) {
                    for (AeaItemMat mat : mats) {
                        if (matinst.getMatId().equals(mat.getMatId())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }

                if (!flag) {
                    for (AeaItemMat mat : mats) {
                        for (String matinstId : matinstIds) {
                            boolean check = aeaHiItemMatinstService.matinstbeLong2MatId(matinstId, mat.getMatId());
                            if (check) {
                                AeaHiItemInoutinst inoutinst = new AeaHiItemInoutinst();
                                inoutinst.setInoutinstId(UUID.randomUUID().toString());
                                inoutinst.setIteminstId(iteminst.getIteminstId());
                                inoutinst.setItemInoutId(mat.getInoutId());
                                inoutinst.setMatinstId(matinstId);
                                inoutinst.setCreater(SecurityContext.getCurrentUserName());
                                inoutinst.setCreateTime(new Date());
                                inoutinst.setIsCollected("1");
                                inoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                                aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(inoutinst);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 暂存
     */
    public ParallelStashResultVo stash(StashVo.ParallelStashVo parallelStashVo) throws Exception {
        ParallelStashResultVo parallelStashResultVo = new ParallelStashResultVo();
        String applySubject = parallelStashVo.getApplySubject();
        String linkmanInfoId = parallelStashVo.getLinkmanInfoId();
        String projInfoId = parallelStashVo.getProjInfoId();
        String themeVerId = parallelStashVo.getThemeVerId();
        String stageId = parallelStashVo.getStageId();
        List<String> itemVerIds = parallelStashVo.getItemVerIds();
        String branchOrgMap = parallelStashVo.getBranchOrgMap();

        Assert.hasText(applySubject, "applySubject is null");
        Assert.hasText(linkmanInfoId, "linkmanInfoId is null");
        Assert.hasText(projInfoId, "projInfoId is null");

        AeaHiApplyinst aeaHiApplyinst;
        AeaHiParStageinst aeaHiParStageinst = null;

        String parallelApplyinstId = parallelStashVo.getApplyinstId();
        // 申报实例不为空时，先删除之前的所有实例化数据
        if (StringUtils.isNotBlank(parallelApplyinstId)) {
            aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(parallelApplyinstId);
            if (!"2".equals(aeaHiApplyinst.getIsTemporarySubmit())) {
                aeaHiApplyinst.setIsTemporarySubmit(Status.ON);
            }
            applyCommonService.clearHistoryInst(parallelApplyinstId);

            aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(parallelApplyinstId);
        } else {
            aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(ApplySource.WIN.getValue()
                    , applySubject, linkmanInfoId
                    , ApplyType.UNIT.getValue(), branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), Status.ON,null);
            parallelApplyinstId = aeaHiApplyinst.getApplyinstId();
        }
        aeaHiApplyinst.setIsGreenWay(parallelStashVo.getIsGreenWay());
        aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);

        applyCommonService.bindApplyinstProj(projInfoId, parallelApplyinstId, SecurityContext.getCurrentUserId());
        if (aeaHiParStageinst == null) {
            aeaHiParStageinst = stashStage(parallelApplyinstId, stageId, themeVerId);
        }
        String stageinstId = aeaHiParStageinst.getStageinstId();

        if (StringUtils.isNotBlank(themeVerId)) {
            Assert.hasText(stageId, "stageId is null.");
            applyCommonService.bindThemeAndProject(new String[]{projInfoId}, themeVerId);

            // 并联事项实例 和  事项日志实例
            if (itemVerIds != null && itemVerIds.size() > 0) {
                stashItems(themeVerId, stageinstId, aeaHiParStageinst.getAppinstId(), itemVerIds, branchOrgMap);
            }

            String[] stateIds = parallelStashVo.getStateIds();
            if (stateIds != null && stateIds.length > 0) {
                aeaHiParStateinstService.batchInsertAeaHiParStateinst(parallelApplyinstId, stageinstId, stateIds, SecurityContext.getCurrentUserName());
            }

            // 简单合并申报的情况下，可能存在事项自己的情形列表
            saveItemStateBySimpleMerge(stageId, parallelStashVo.getParallelItemStateIds(), itemVerIds, parallelApplyinstId, stageinstId);

            aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(parallelStashVo.getMatinstsIds(), parallelApplyinstId, SecurityContext.getCurrentUserName());
        }
        parallelStashResultVo.setApplyinstId(parallelApplyinstId);

        // 并行事项暂存
        List<String> propulsionItemVerIds = parallelStashVo.getPropulsionItemVerIds();
        if (propulsionItemVerIds != null && propulsionItemVerIds.size() > 0) {
            List<ParallelItemApplyinstVo> newSeriesApplyinstIds = new ArrayList<>();
            List<ParallelItemApplyinstVo> seriesApplyinstIds = parallelStashVo.getSeriesApplyinstIds();
            List<ParallelItemStateVo> parallelItemStateIds = parallelStashVo.getParallelItemStateIds();
            Map<String, String> itemApplyinstMap = new HashMap<>();
            if (seriesApplyinstIds != null && seriesApplyinstIds.size() > 0) {
                itemApplyinstMap = seriesApplyinstIds.stream().collect(Collectors.toMap(ParallelItemApplyinstVo::getItemVerId, ParallelItemApplyinstVo::getSeriesApplyinstId));
            }
            Map<String, List<String>> itemStateMap = new HashMap<>();
            if (parallelItemStateIds != null && parallelItemStateIds.size() > 0) {
                itemStateMap = parallelItemStateIds.stream().collect(Collectors.toMap(ParallelItemStateVo::getItemVerId, ParallelItemStateVo::getStateIds));
            }
            for (String propulsionItemVerId : propulsionItemVerIds) {
                String[] propulsionStateIds = itemStateMap.get(propulsionItemVerId) == null ? null : itemStateMap.get(propulsionItemVerId).toArray(new String[0]);
                String propulsionBranchOrgMap = BusinessUtil.getOrgIdFromBranchOrgMap(parallelStashVo.getPropulsionBranchOrgMap(), propulsionItemVerId);
                if (StringUtils.isNotBlank(propulsionBranchOrgMap)) {
                    propulsionBranchOrgMap = "[{\"itemVerId\":\"" + propulsionItemVerId + "\", \"branchOrg\": \"" + propulsionBranchOrgMap + "\"}]";
                }
                String seriesApplyinstId = itemApplyinstMap.get(propulsionItemVerId);
                String newSeriesApplyinstId = aeaSeriesService.stash(parallelStashVo.toSeriesStashVo(propulsionItemVerId, propulsionStateIds, propulsionBranchOrgMap, seriesApplyinstId, parallelApplyinstId));
                newSeriesApplyinstIds.add(new ParallelItemApplyinstVo(propulsionItemVerId, newSeriesApplyinstId));
            }
            parallelStashResultVo.setSeriesApplyinstIds(newSeriesApplyinstIds);
        }
        return parallelStashResultVo;
    }

    private List<AeaHiIteminst> stashItems(String themeVerId, String stageinstId, String appinstId, List<String> itemVerIds, String branchOrgMap) throws Exception {
        return aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(themeVerId, stageinstId, itemVerIds, branchOrgMap, null, appinstId);
    }

    private AeaHiParStageinst stashStage(String applyinstId, String stageId, String themeVerId) throws Exception {
        // 预先生成流程模板实例ID
        String appinstId = UUID.randomUUID().toString();
        return aeaHiParStageinstService.createAeaHiParStageinst(applyinstId, stageId, themeVerId, appinstId, null);
    }

    /**
     * 暂存回显
     *
     * @param applyinstId 申报实例id
     */
    public ParallelUnstashVo unstash(String applyinstId) throws Exception {
        ParallelUnstashVo parallelUnstashVo = new ParallelUnstashVo();

        unstashCommonProperties(parallelUnstashVo, applyinstId);

        AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
        parallelUnstashVo.setStageId(aeaHiParStageinst.getStageId());

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiParStageinst.getStageId());
        parallelUnstashVo.setThemeVerId(aeaParStage.getThemeVerId());

        parallelUnstashVo.setStateIds(aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null)
                .stream().map(AeaHiParStateinst::getExecStateId).collect(Collectors.toSet()));

        parallelUnstashVo.setParallelItemStateIds(aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrStageinstId(null, aeaHiParStageinst.getStageinstId())
                .stream().map(AeaHiItemStateinst::getExecStateId).collect(Collectors.toSet()));

        List<AeaApplyinstForminst> aeaApplyinstForminsts = aeaApplyinstForminstMapper.listAeaApplyinstForminstByApplyinstId(applyinstId);
        parallelUnstashVo.getForminstVos().addAll(aeaApplyinstForminsts.stream()
                .map(ForminstVo::from).collect(Collectors.toList()));

        Map<String, String> branchOrg = new HashMap<>();
        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        aeaHiIteminsts.forEach(iteminst -> {
            branchOrg.put(iteminst.getItemVerId(), iteminst.getApproveOrgId());
            try {
                AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(iteminst.getItemVerId());
                // 如果是实施是事项， 对应的把标准事项也放进去
                if (Status.OFF.equals(aeaItemBasic.getIsCatalog())) {
                    AeaItemBasic catalogItem = aeaItemBasicService.getCatalogItemByCarryOutItemId(aeaItemBasic.getItemId(),null);
                    if (catalogItem != null) {
                        branchOrg.put(catalogItem.getItemVerId(), iteminst.getApproveOrgId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //ignore
            }
        });
        parallelUnstashVo.setBranchOrg(branchOrg);
        parallelUnstashVo.setSmsInfoId(Optional.ofNullable(aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId)).orElse(new AeaHiSmsInfo()).getId());
        return parallelUnstashVo;
    }
}
