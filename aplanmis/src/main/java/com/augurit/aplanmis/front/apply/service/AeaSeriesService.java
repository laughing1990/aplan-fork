package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.exception.ProcessAlreadyActivateException;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstForminstMapper;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaLogApplyStateHistMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.apply.ApplyCommonService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
import com.augurit.aplanmis.front.apply.vo.ForminstVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.stash.SeriesUnstashVo;
import com.augurit.aplanmis.front.apply.vo.stash.StashVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 单项申报service
 * author:sam
 */
@Service
@Transactional
@Slf4j
public class AeaSeriesService {
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaBpmProcessService aeaBpmProcessService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private ApplyCommonService applyCommonService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaApplyinstForminstMapper aeaApplyinstForminstMapper;
    @Autowired
    private AeaLogApplyStateHistMapper aeaLogApplyStateHistMapper;

    /**
     * 打印回执
     * @return 申报实例id
     */
    public String printReceipts(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaHiApplyinst aeaHiApplyinst = instantiate(seriesApplyDataVo, currentWindowId);

        // 启动并挂起流程
        doStartApplyAndSuspend(aeaHiApplyinst);

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "2");

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

        // 启动流程
        doStartApply(aeaHiApplyinst, seriesApplyDataVo, currentWindowId);

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "0");

        return aeaHiApplyinst.getApplyinstId();
    }

    // 不予受理
    public String inadmissible(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String currentWindowId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaHiApplyinst aeaHiApplyinst = instantiate(seriesApplyDataVo, currentWindowId);

        // 启动并结束流程
        doInadmissible(aeaHiApplyinst, seriesApplyDataVo);

        // 确认申报实例的最终状态
        confirmAeaHiApplyinst(aeaHiApplyinst, seriesApplyDataVo, "0");

        return aeaHiApplyinst.getApplyinstId();
    }

    private AeaHiApplyinst instantiate(SeriesApplyDataVo seriesApplyDataVo, String currentWindowId) throws Exception {
        // 参数校验
        validate(seriesApplyDataVo);

        // 获取申报实例，如果不存在，则创建
        AeaHiApplyinst aeaHiApplyinst = createAeaHiApplyinstIfNotExists(seriesApplyDataVo);

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

    private void validate(SeriesApplyDataVo seriesApplyDataVo) {
        Assert.hasText(seriesApplyDataVo.getApplySource(), "申报来源参数为空!");

        Assert.hasText(seriesApplyDataVo.getApplySubject(), "申报主体参数为空！");

        Assert.isTrue(StringUtils.isNotBlank(seriesApplyDataVo.getLinkmanInfoId()) || StringUtils.isNotBlank(seriesApplyDataVo.getApplyLinkmanId()), "联系人ID参数为空！");

        Assert.hasText(seriesApplyDataVo.getItemVerId(), "事项版本参数为空");

        Assert.isTrue(seriesApplyDataVo.getProjInfoIds().length > 0, "项目ID参数为空！");

        Assert.hasText(seriesApplyDataVo.getIsParallel(), "是否并行推进事项参数为空！");

        if (Status.ON.equals(seriesApplyDataVo.getIsParallel())) {
            Assert.hasText(seriesApplyDataVo.getStageId(), "当前为并行推进事项申报，申报阶段ID参数不能为空！");
        }
    }

    private AeaHiApplyinst createAeaHiApplyinstIfNotExists(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        AeaHiApplyinst aeaHiApplyinst;
        if (StringUtils.isNotBlank(seriesApplyDataVo.getApplyinstId())) {
            aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(seriesApplyDataVo.getApplyinstId());
        } else {
            aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(seriesApplyDataVo.getApplySource(),
                    seriesApplyDataVo.getApplySubject(), seriesApplyDataVo.getLinkmanInfoId(), ApplyType.SERIES.getValue(),
                    seriesApplyDataVo.getBranchOrgMap(), ApplyState.RECEIVE_APPROVED_APPLY.getValue(), Status.OFF, null);
        }
        Assert.notNull(aeaHiApplyinst, "获取申报实例异常");
        return aeaHiApplyinst;
    }

    private void clearHistoryDataIfNecessary(AeaHiApplyinst aeaHiApplyinst) throws Exception {
        // 之前已经打印过回执 或者 暂存过
        if (alreadyPrintedBefore(aeaHiApplyinst.getIsTemporarySubmit())
                || alreadyStashedBefore(aeaHiApplyinst.getIsTemporarySubmit())) {
            applyCommonService.clearHistoryInst(aeaHiApplyinst.getApplyinstId());
        }
    }

    private boolean alreadyStashedBefore(String isTemporarySubmit) {
        return Status.ON.equals(isTemporarySubmit);
    }

    private boolean alreadyPrintedBefore(String isTemporarySubmit) {
        return "2".equals(isTemporarySubmit);
    }

    private String createAeaLogApplyStateHistIfNotExists(AeaHiApplyinst aeaHiApplyinst, String currentWindowId) {
        String appinstId;
        AeaLogApplyStateHist aeaLogApplyStateHist = new AeaLogApplyStateHist();
        aeaLogApplyStateHist.setApplyinstId(aeaHiApplyinst.getApplyinstId());
        List<AeaLogApplyStateHist> aeaLogApplyStateHists = aeaLogApplyStateHistMapper.listAeaLogApplyStateHist(aeaLogApplyStateHist);
        if (CollectionUtils.isEmpty(aeaLogApplyStateHists)) {
            appinstId = UuidUtil.generateUuid();
            aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(aeaHiApplyinst.getApplyinstId(), null, appinstId, null,
                    ApplyState.RECEIVE_APPROVED_APPLY.getValue(), currentWindowId);
        } else {
            appinstId = aeaLogApplyStateHists.get(0).getAppinstId();
        }
        Assert.hasText(appinstId, "获取流程模板实例id异常");
        return appinstId;
    }

    private AeaHiSeriesinst createAeaHiSeriesinstIfNotExists(String appinstId, AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
        if (aeaHiSeriesinst == null) {
            aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(aeaHiApplyinst.getApplyinstId(), appinstId, seriesApplyDataVo.getIsParallel(), seriesApplyDataVo.getStageId());
        }
        Assert.notNull(aeaHiApplyinst, "获取单项申报实例异常");
        return aeaHiSeriesinst;
    }

    private void saveApplySubject(AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo) {
        // 申报主体为个人
        if ("0".equals(seriesApplyDataVo.getApplySubject())) {
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(aeaHiApplyinst.getApplyinstId(), seriesApplyDataVo.getProjInfoIds(), seriesApplyDataVo.getApplyLinkmanId(), seriesApplyDataVo.getLinkmanInfoId());
        } else {
            // 建设单位
            List<BuildProjUnitVo> buildProjUnits = seriesApplyDataVo.getBuildProjUnitMap();
            if (CollectionUtils.isNotEmpty(buildProjUnits)) {
                Map<String, List<String>> puMap = new HashMap<>(buildProjUnits.size());
                buildProjUnits.forEach(pu -> puMap.put(pu.getProjectInfoId(), pu.getUnitIds()));
                aeaUnitInfoService.insertApplyOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), puMap);
            }
            // 经办单位
            String[] handleUnitIds = seriesApplyDataVo.getHandleUnitIds();
            if (handleUnitIds != null && handleUnitIds.length > 0) {
                aeaUnitInfoService.insertApplyNonOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), seriesApplyDataVo.getProjInfoIds(), handleUnitIds);
            }
        }
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

    private void doStartApply(AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo, String currentWindowId) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getAppId(), "流程模板id不能为空");
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "事项实例id不能为空");

        String appinstId = aeaHiApplyinst.getAppinstId();
        String appId = aeaHiApplyinst.getAppId();
        String procInstId;
        // 如果之前已经打印过回执，这时候流程是挂起状态
        if ("2".equals(aeaHiApplyinst.getIsTemporarySubmit())) {
            applyCommonService.updateApplyProcessVariable(aeaHiApplyinst);
            // 实例和流程已经启动，下一节点是部门审批
            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
            Assert.notNull(actStoAppinst, "找不到流程模板实例: appinstId: " + appinstId);
            procInstId = actStoAppinst.getProcinstId();
            bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());//激活流程
        } else {
            // 启动主流程
            BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(appId, appinstId, aeaHiApplyinst);
            Assert.isTrue(processInstance != null && processInstance.getProcessInstance() != null, "流程启动失败！");
            procInstId = processInstance.getProcessInstance().getId();
        }
        // 查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if (tasks.size() > 0) {
            Task task = tasks.get(0);
            updateLogInstTaskId(aeaHiApplyinst, task);

            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), seriesApplyDataVo.getComments());
            //推动流程流转
            taskService.complete(task.getId(), new String[]{"bumenshenpi"}, null);

            // 更新事项状态
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getIteminstId(), task.getId(), appinstId, ItemStatus.ACCEPT_DEAL.getValue(), SecurityContext.getCurrentOrgId());

            // 更新申请状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getApplyinstId(), task.getId(), appinstId, ApplyState.ACCEPT_DEAL.getValue(), currentWindowId);

        } else {
            throw new RuntimeException("流程流转失败！");
        }
    }

    private void doStartApplyAndSuspend(AeaHiApplyinst aeaHiApplyinst) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getAppId(), "流程模板id不能为空");
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "事项实例id不能为空");

        String appinstId = aeaHiApplyinst.getAppinstId();
        String appId = aeaHiApplyinst.getAppId();

        String procInstId;
        // 如果之前已经打印过回执，这时候流程是挂起状态
        if ("2".equals(aeaHiApplyinst.getIsTemporarySubmit())) {
            applyCommonService.updateApplyProcessVariable(aeaHiApplyinst);
            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(aeaHiApplyinst.getAppinstId());
            procInstId = actStoAppinst.getProcinstId();
        } else {
            // 启动主流程
            BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(appId, appinstId, aeaHiApplyinst);
            Assert.isTrue(processInstance != null && processInstance.getProcessInstance() != null, "流程启动失败！");
            procInstId = processInstance.getProcessInstance().getId();
            bpmProcessService.suspendProcessInstanceById(procInstId);
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if (tasks.size() > 0) {
            //6.流程发起后，更新初始事项历史的taskId
            updateLogInstTaskId(aeaHiApplyinst, tasks.get(0));
        }
    }

    private void updateLogInstTaskId(AeaHiApplyinst aeaHiApplyinst, Task task) {
        // 流程发起后，更新初始事项历史的taskId
        AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiApplyinst.getIteminstId(), aeaHiApplyinst.getAppinstId());
        logItemStateHist.setTaskinstId(task.getId());
        aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

        // 流程发起后，更新初始申请历史的taskId
        AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(aeaHiApplyinst.getApplyinstId(), aeaHiApplyinst.getAppinstId());
        applyStateHist.setTaskinstId(task.getId());
        aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);
    }

    private void confirmAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo, String isTemporarySubmit) throws Exception {
        aeaHiApplyinst.setProjInfoId(seriesApplyDataVo.getProjInfoIds()[0]);
        aeaHiApplyinst.setIsGreenWay(StringUtils.isNotBlank(seriesApplyDataVo.getIsGreenWay()) ? seriesApplyDataVo.getIsGreenWay() : Status.OFF);
        aeaHiApplyinst.setIsTemporarySubmit(isTemporarySubmit);
        aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);
    }

    private void doInadmissible(AeaHiApplyinst aeaHiApplyinst, SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "事项实例id不能为空");

        String appinstId = aeaHiApplyinst.getAppinstId();

        ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
        //推动流程转向结束
        List<Task> list = taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list();
        if (list != null && list.size() > 0) {
            Task task = list.get(0);
            try {
                bpmProcessService.activateProcessInstanceById(task.getProcessInstanceId());
            } catch (ProcessAlreadyActivateException e) {
                log.info(e.getMessage());
            }
            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), seriesApplyDataVo.getComments());
            //推动流程流转
            taskService.complete(task.getId(), new String[]{"jieshu"}, null);

            String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
            String currentOrgId = SecurityContext.getCurrentOrgId();

            // 更改 事项实例状态 和 新增事项日志记录为 不予受理
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getIteminstId(), task.getId(), appinstId, ItemStatus.OUT_SCOPE.getValue(), currentOrgId);
            // 更改 申报实例状态 和 新增申报日志实例记录
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getApplyinstId(), task.getId(), appinstId, ApplyState.OUT_SCOPE.getValue(), opuWinId);
        }
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
        String applySubject = seriesStashVo.getApplySubject();
        String linkmanInfoId = seriesStashVo.getLinkmanInfoId();
        String projInfoId = seriesStashVo.getProjInfoId();
        String branchOrgMap = seriesStashVo.getBranchOrgMap();
        String itemVerId = seriesStashVo.getItemVerId();

        Assert.hasText(applySubject, "applySubject is null");
        Assert.hasText(linkmanInfoId, "linkmanInfoId is null");
        Assert.hasText(projInfoId, "projInfoId is null");

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
                    , applySubject, linkmanInfoId, ApplyType.SERIES.getValue(), branchOrgMap
                    , ApplyState.RECEIVE_APPROVED_APPLY.getValue(), Status.ON,null);
            applyinstId = aeaHiApplyinst.getApplyinstId();
        }
        aeaHiApplyinst.setIsGreenWay(seriesStashVo.getIsGreenWay());
        aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);

        applyCommonService.bindApplyinstProj(projInfoId, applyinstId, SecurityContext.getCurrentUserId());
        if (aeaHiSeriesinst == null) {
            // 预先生成流程模板实例ID
            String appinstId = UUID.randomUUID().toString();
            String isParallel = StringUtils.isNotBlank(seriesStashVo.getIsParallel()) ? seriesStashVo.getIsParallel() : Status.OFF;
            aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(applyinstId, appinstId, isParallel, seriesStashVo.getStageId());
        }

        if (StringUtils.isNotBlank(seriesStashVo.getStageId()) && StringUtils.isNotBlank(seriesStashVo.getItemVerId())) {
            aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, branchOrgMap, null, aeaHiSeriesinst.getAppinstId());

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

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        seriesUnstashVo.setAeaHiApplyinst(aeaHiApplyinst);

        List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
        Assert.state(aeaApplyinstProjs.size() > 0, "根据申报实例找不到对应的项目信息, applyinstId: " + applyinstId);
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(aeaApplyinstProjs.get(0).getProjInfoId());
        seriesUnstashVo.setProjInfoId(aeaProjInfo.getProjInfoId());
        seriesUnstashVo.setThemeId(aeaProjInfo.getThemeId());

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
        return seriesUnstashVo;
    }
}
