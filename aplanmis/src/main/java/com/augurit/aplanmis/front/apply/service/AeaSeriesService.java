package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.receive.constant.ReceiveConstant;
import com.augurit.aplanmis.front.apply.vo.ForminstVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.receipt.SaveReceiptsVo;
import com.augurit.aplanmis.front.apply.vo.stash.SeriesUnstashVo;
import com.augurit.aplanmis.front.apply.vo.stash.StashVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 单项申报service
 * author:sam
 */
@Service
@Transactional
@Slf4j
public class AeaSeriesService extends RestApplyService {
    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    /**
     * 打印回执
     * @return 申报实例id
     */
    public String printReceipts(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaHiApplyinst aeaHiApplyinst = instantiate(seriesApplyDataVo, currentWindowId);
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "单项打印回执事项实例化失败");

        // 启动并挂起流程
        doStartApplyAndSuspend(aeaHiApplyinst);

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "2");

        // 先删除以前的 receiveType=1, 2 的回执实例
        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromSeriesApplyDataVo(seriesApplyDataVo, aeaHiApplyinst.getApplyinstId(), new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()}));

        return aeaHiApplyinst.getApplyinstId();
    }

    /**
     * 发起申报
     * @return 申报实例id
     */
    public String startApply(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        // 实例化
        AeaHiApplyinst aeaHiApplyinst = instantiate(seriesApplyDataVo, currentWindowId);
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "单项申报事项实例化失败");

        // 启动流程
        doStartApply(aeaHiApplyinst, seriesApplyDataVo.getComments(), currentWindowId);

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "0");

        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromSeriesApplyDataVo(seriesApplyDataVo, aeaHiApplyinst.getApplyinstId(), new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()}));

        return aeaHiApplyinst.getApplyinstId();
    }

    // 不予受理
    public String inadmissible(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaHiApplyinst aeaHiApplyinst = instantiate(seriesApplyDataVo, currentWindowId);
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "单项不予受理事项实例化失败");

        // 启动并结束流程
        doInadmissible(aeaHiApplyinst, seriesApplyDataVo.getComments());

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "0");

        // 打印不受理回执
        clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo.fromSeriesApplyDataVo(seriesApplyDataVo, aeaHiApplyinst.getApplyinstId(), new String[]{ReceiveConstant.ReceiveTypeEnum.REJECT_TYPE.getCode()}));

        return aeaHiApplyinst.getApplyinstId();
    }

    public AeaHiApplyinst instantiate(SeriesApplyDataVo seriesApplyDataVo, String currentWindowId) throws Exception {
        // 参数校验
        validate(seriesApplyDataVo);

        // 获取申报实例，如果不存在，则创建
        AeaHiApplyinst aeaHiApplyinst = createAeaHiApplyinstIfNotExists(seriesApplyDataVo, seriesApplyDataVo.getApplyinstId());

        // 如果需要的话，清除历史数据
        clearHistoryDataIfNecessary(aeaHiApplyinst);

        // 获取申报日志记录，如果不存在，则创建
        String appinstId = createAeaLogApplyStateHistIfNotExists(aeaHiApplyinst, currentWindowId);
        aeaHiApplyinst.setAppinstId(appinstId);

        // 获取单项申报实例，如果不存在，则创建
        AeaHiSeriesinst aeaHiSeriesinst = createAeaHiSeriesinstIfNotExists(appinstId, aeaHiApplyinst, seriesApplyDataVo);

        // 创建事项实例 和 事项日志实例
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), seriesApplyDataVo.getItemVerId(), seriesApplyDataVo.getBranchOrgMap(), null, appinstId);
        aeaHiApplyinst.setIteminstId(aeaHiIteminst.getIteminstId());

        // 创建情形实例
        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(aeaHiApplyinst.getApplyinstId(), aeaHiSeriesinst.getSeriesinstId(), null, seriesApplyDataVo.getStateIds(), SecurityContext.getCurrentUserName());

        // 创建输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(seriesApplyDataVo.getMatinstsIds(), aeaHiApplyinst.getApplyinstId(), SecurityContext.getCurrentUserName());

        // 判断事项是否为告知承诺制
        applyCommonService.setInformCommit(seriesApplyDataVo.getStateIds(), ApplyType.SERIES, aeaHiIteminst);

        String projInfoId = seriesApplyDataVo.getProjInfoIds()[0];

        // 保存申报实例与项目关联
        applyCommonService.bindApplyinstProj(projInfoId, aeaHiApplyinst.getApplyinstId(), SecurityContext.getCurrentUserId());

        // 保存申报主体
        saveApplySubject(aeaHiApplyinst, seriesApplyDataVo);

        // 项目绑定主题
        if (StringUtils.isNotBlank(seriesApplyDataVo.getStageId())) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(seriesApplyDataVo.getStageId());
            if (aeaParStage != null) {
                applyCommonService.bindThemeAndProject(seriesApplyDataVo.getProjInfoIds(), aeaParStage.getThemeVerId());
            }
        }

        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        aeaHiApplyinst.setAppId(aeaItemBasic.getAppId());

        // 设置流程审批相关信息
        fillApproveInfo(aeaHiApplyinst, seriesApplyDataVo, aeaItemBasic, aeaHiIteminst);

        return aeaHiApplyinst;
    }

    protected void validate(SeriesApplyDataVo seriesApplyDataVo) {
        super.validate(seriesApplyDataVo);

        Assert.hasText(seriesApplyDataVo.getItemVerId(), "事项版本参数为空");

        Assert.hasText(seriesApplyDataVo.getIsParallel(), "是否并行推进事项参数为空！");

        if (Status.ON.equals(seriesApplyDataVo.getIsParallel())) {
            Assert.hasText(seriesApplyDataVo.getStageId(), "当前为并行推进事项申报，申报阶段ID参数不能为空！");
        }
    }

    private AeaHiSeriesinst createAeaHiSeriesinstIfNotExists(String appinstId, AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
        if (aeaHiSeriesinst == null) {
            aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(aeaHiApplyinst.getApplyinstId(), appinstId, seriesApplyDataVo.getIsParallel(), seriesApplyDataVo.getStageId());
        }
        Assert.notNull(aeaHiApplyinst, "获取单项申报实例异常");
        return aeaHiSeriesinst;
    }

    private void fillApproveInfo(AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo, AeaItemBasic aeaItemBasic, AeaHiIteminst aeaHiIteminst) {
        if (StringUtils.isNotBlank(seriesApplyDataVo.getBranchOrgMap())) {
            List<Map> mapList = JSONArray.parseArray(seriesApplyDataVo.getBranchOrgMap(), Map.class);
            mapList.forEach(map -> {
                OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(map.get("branchOrg").toString());
                Map<String, Boolean> approvalOrgCode = new HashMap<>();
                Map<String, Boolean> isBranchHandle = new HashMap<>();
                approvalOrgCode.put(opuOmOrg.getOrgCode(), true);
                isBranchHandle.put(aeaItemBasic.getItemCategoryMark(), true);
                aeaHiApplyinst.setApprovalOrgCode(approvalOrgCode);
                aeaHiApplyinst.setIsBranchHandle(isBranchHandle);
            });
        } else {//市局承办
            Map<String, String> mapOrg = new HashMap<>();
            Map<String, Boolean> isBranchHandle = new HashMap<>();
            mapOrg.put("itemVerId", aeaItemBasic.getItemVerId());
            mapOrg.put("branchOrg", aeaHiIteminst.getApproveOrgId());
            aeaHiApplyinst.setBranchOrg(mapOrg.toString());
            isBranchHandle.put(aeaItemBasic.getItemCategoryMark(), false);
            aeaHiApplyinst.setIsBranchHandle(isBranchHandle);
        }
        // 用于流程启动情形
        aeaHiApplyinst.setStateinsts(applyCommonService.filterProcessStartConditions(seriesApplyDataVo.getStateIds(), ApplyType.SERIES));
    }

    /**
     * 材料补全
     *
     * @param applyinstId 申请实例ID
     * @param comments    意见
     * @param flag        start：开始补全，end:补全结束
     */
    public void materialCompletion(String applyinstId, String comments, String flag) throws Exception {
        if (StringUtils.isBlank(applyinstId)) throw new InvalidParameterException("申请实例ID为空！");
        if (StringUtils.isBlank(flag)) throw new InvalidParameterException("材料补全标志为空！");
        ActStoAppinst actStoAppinst = new ActStoAppinst();
        actStoAppinst.setMasterRecordId(applyinstId);
        actStoAppinst.setFlowMode("proc");
        List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(actStoAppinst);

        if (appinsts.size() > 0) {
            actStoAppinst = appinsts.get(0);
            String iteminstId;
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if ("start".equals(flag)) {
                bpmProcessService.suspendProcessInstanceById(actStoAppinst.getProcinstId());  //挂起流程
                //更改事项实例状态和新增历史记录，保存审批部门补全意见 comments
                for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                    iteminstId = iteminst.getIteminstId();
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminstId, comments, "材料补全", null, ItemStatus.CORRECT_MATERIAL_START.getValue(), null);
                }
                aeaHiApplyinst.setApplyinstState(ApplyState.IN_THE_SUPPLEMENT.getValue());
            } else {
                bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());//激活流程
                //更改事项实例状态和新增历史记录，保存窗口收件补全意见 comments
                for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                    iteminstId = iteminst.getIteminstId();
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminstId, comments, "材料补全", null, ItemStatus.CORRECT_MATERIAL_END.getValue(), null);
                }
                aeaHiApplyinst.setApplyinstState(ApplyState.SUPPLEMENTARY.getValue());
            }
            //更改申请实例状态和新增历史记录
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinstId, comments, "材料补全", null, aeaHiApplyinst.getApplyinstState(), null);
        }
    }

    /**
     * 单项申报 暂存
     */
    public String stash(StashVo.SeriesStashVo seriesStashVo) throws Exception {
        Assert.hasText(seriesStashVo.getApplySubject(), "applySubject is null");
        Assert.hasText(seriesStashVo.getLinkmanInfoId(), "linkmanInfoId is null");
        Assert.hasText(seriesStashVo.getProjInfoId(), "projInfoId is null");

        AeaHiApplyinst aeaHiApplyinst;
        AeaHiSeriesinst aeaHiSeriesinst = null;

        String applyinstId = seriesStashVo.getApplyinstId();

        // 申报实例不为空时，先删除之前的所有实例化数据
        if (StringUtils.isNotBlank(applyinstId)) {
            applyCommonService.clearHistoryInst(applyinstId);

            aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (!"2".equals(aeaHiApplyinst.getIsTemporarySubmit())) {
                aeaHiApplyinst.setIsTemporarySubmit(Status.ON);
            }
            aeaHiSeriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstByApplyinstId(applyinstId);
        } else {
            aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(ApplySource.WIN.getValue()
                    , seriesStashVo.getApplySubject(), seriesStashVo.getLinkmanInfoId(), ApplyType.SERIES.getValue(), seriesStashVo.getBranchOrgMap()
                    , ApplyState.RECEIVE_APPROVED_APPLY.getValue(), Status.ON, seriesStashVo.getParallelApplyinstId());
            applyinstId = aeaHiApplyinst.getApplyinstId();
        }
        aeaHiApplyinst.setIsGreenWay(seriesStashVo.getIsGreenWay());
        aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);

        applyCommonService.bindApplyinstProj(seriesStashVo.getProjInfoId(), applyinstId, SecurityContext.getCurrentUserId());
        if (aeaHiSeriesinst == null) {
            // 预先生成流程模板实例ID
            String appinstId = UUID.randomUUID().toString();
            String isParallel = StringUtils.isNotBlank(seriesStashVo.getIsParallel()) ? seriesStashVo.getIsParallel() : Status.OFF;
            aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(applyinstId, appinstId, isParallel, seriesStashVo.getStageId());
        }

        if (StringUtils.isNotBlank(seriesStashVo.getStageId()) && StringUtils.isNotBlank(seriesStashVo.getItemVerId())) {
            aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), seriesStashVo.getItemVerId(), seriesStashVo.getBranchOrgMap(), null, aeaHiSeriesinst.getAppinstId());

            String[] stateIds = seriesStashVo.getStateIds();
            if (stateIds != null && stateIds.length > 0) {
                aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(applyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());
            }
            aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(seriesStashVo.getMatinstsIds(), applyinstId, SecurityContext.getCurrentUserName());
        }
        return aeaHiApplyinst.getApplyinstId();
    }

    /**
     * 暂存回显
     * @param applyinstId 申报实例id
     */
    public SeriesUnstashVo unstash(String applyinstId) throws Exception {
        SeriesUnstashVo seriesUnstashVo = new SeriesUnstashVo();

        unstashCommonProperties(seriesUnstashVo, applyinstId);

        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstByApplyinstId(applyinstId);
        seriesUnstashVo.setStageId(aeaHiSeriesinst.getStageId());

        if (StringUtils.isNotBlank(aeaHiSeriesinst.getStageId())) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiSeriesinst.getStageId());
            seriesUnstashVo.setThemeVerId(aeaParStage.getThemeVerId());
        }

        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (CollectionUtils.isNotEmpty(aeaHiIteminsts)) {
            AeaHiIteminst aeaHiIteminst = aeaHiIteminsts.get(0);
            seriesUnstashVo.setApproveOrgId(aeaHiIteminst.getApproveOrgId());
        }

        seriesUnstashVo.setStateIds(aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null)
                .stream().map(AeaHiItemStateinst::getExecStateId).collect(Collectors.toSet()));

        List<AeaApplyinstForminst> aeaApplyinstForminsts = aeaApplyinstForminstMapper.listAeaApplyinstForminstByApplyinstId(applyinstId);
        seriesUnstashVo.getForminstVos().addAll(aeaApplyinstForminsts.stream()
                .map(ForminstVo::from).collect(Collectors.toList()));
        seriesUnstashVo.setSmsInfoId(Optional.ofNullable(aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId)).orElse(new AeaHiSmsInfo()).getId());
        return seriesUnstashVo;
    }

    /**
     * 并联申报时，将并行申报与申报实例关联
     *
     * @param parallelApplyinstId 并联申报实例id
     * @param seriesApplyDataVos  并行申报参数vo
     */
    public void bindApplyinstId(String parallelApplyinstId, List<SeriesApplyDataVo> seriesApplyDataVos) throws Exception {
        Assert.notEmpty(seriesApplyDataVos, "并行申报参数不能为空");
        if (StringUtils.isNotBlank(parallelApplyinstId)) {
            Set<String> seriesApplyinstIds = aeaHiApplyinstService.getSeriesAeaHiApplyinstListByParentApplyinstId(parallelApplyinstId, null)
                    .stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(seriesApplyinstIds)) {
                return;
            }
            Map<String, String> applyinstItemVerIdMap = aeaHiApplyinstMapper.listSeriesAeaHiIteminstByApplyinstIds(seriesApplyinstIds)
                    .stream().collect(Collectors.toMap(AeaHiApplyinst::getItemVerId, AeaHiApplyinst::getApplyinstId));
            for (SeriesApplyDataVo vo : seriesApplyDataVos) {
                vo.setApplyinstId(applyinstItemVerIdMap.get(vo.getItemVerId()));
            }
        }
    }
}
