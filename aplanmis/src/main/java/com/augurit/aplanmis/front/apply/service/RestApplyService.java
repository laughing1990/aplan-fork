package com.augurit.aplanmis.front.apply.service;

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
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstForminstMapper;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiReceiveMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaLogApplyStateHistMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.apply.ApplyCommonService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.front.apply.vo.ApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.receipt.SaveReceiptsVo;
import com.augurit.aplanmis.front.apply.vo.stash.UnStashVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class RestApplyService {

    @Autowired
    protected AeaBpmProcessService aeaBpmProcessService;
    @Autowired
    protected ApplyCommonService applyCommonService;
    @Autowired
    protected ActStoAppinstService actStoAppinstService;
    @Autowired
    protected BpmProcessService bpmProcessService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected BpmTaskService bpmTaskService;
    @Autowired
    protected AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    protected AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    protected AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    protected AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    protected AeaLogApplyStateHistMapper aeaLogApplyStateHistMapper;
    @Autowired
    protected AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    protected AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    protected AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    protected AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    protected AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    protected AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    protected OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    protected AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    protected AeaParStageMapper aeaParStageMapper;
    @Autowired
    protected AeaApplyinstForminstMapper aeaApplyinstForminstMapper;
    @Autowired
    protected AeaApplyinstProjMapper aeaApplyinstProjMapper;

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;

    protected void validate(ApplyDataVo applyDataVo) {
        Assert.hasText(applyDataVo.getApplySource(), "申报来源参数为空!");

        Assert.hasText(applyDataVo.getApplySubject(), "申报主体参数为空！");

        Assert.isTrue(StringUtils.isNotBlank(applyDataVo.getLinkmanInfoId()) || StringUtils.isNotBlank(applyDataVo.getApplyLinkmanId()), "联系人ID参数为空！");

        Assert.isTrue(applyDataVo.getProjInfoIds().length > 0, "项目ID参数为空！");

    }

    protected AeaHiApplyinst createAeaHiApplyinstIfNotExists(ApplyDataVo applyDataVo, String applyinstId) throws Exception {
        AeaHiApplyinst aeaHiApplyinst;
        String parallelApplyinstId = null;
        if (applyDataVo instanceof SeriesApplyDataVo) {
            parallelApplyinstId = ((SeriesApplyDataVo) applyDataVo).getParallelApplyinstId();
        }
        if (StringUtils.isNotBlank(applyinstId)) {
            aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            // 与并联申报实例关联
            if (StringUtils.isNotBlank(parallelApplyinstId)) {
                aeaHiApplyinst.setParentApplyinstId(parallelApplyinstId);
            }
        } else {
            aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(applyDataVo.getApplySource(),
                    applyDataVo.getApplySubject(), applyDataVo.getLinkmanInfoId(), applyDataVo.getApplyType().getValue(),
                    applyDataVo.getBranchOrgMap(), ApplyState.RECEIVE_APPROVED_APPLY.getValue(), Status.OFF, parallelApplyinstId);
        }
        Assert.notNull(aeaHiApplyinst, "获取申报实例异常");
        return aeaHiApplyinst;
    }

    protected void clearHistoryDataIfNecessary(AeaHiApplyinst aeaHiApplyinst) throws Exception {
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

    protected String createAeaLogApplyStateHistIfNotExists(AeaHiApplyinst aeaHiApplyinst, String currentWindowId) {
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

    /**
     * 打印回执
     *
     * @param saveReceiptsVo 打印回执参数
     */
    public void clearHistoryReceiptsAndSaveAgain(SaveReceiptsVo saveReceiptsVo) throws Exception {
        if (CollectionUtils.isNotEmpty(saveReceiptsVo.getAlreadExistsApplyinstIds())) {
            // 先删除以前的 receiveType=1, 2 的回执实例
            List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listReceiveByApplyinstIdAndTypes(saveReceiptsVo.getAlreadExistsApplyinstIds().toArray(new String[0]), saveReceiptsVo.getReceiptTypes());
            if (CollectionUtils.isNotEmpty(aeaHiReceives)) {
                aeaHiReceiveMapper.deleteAeaHiReceives(aeaHiReceives.stream().map(AeaHiReceive::getReceiveId).collect(Collectors.toList()));
            }
        }

        // 检查收件人
        updateAeaSmsInfo(saveReceiptsVo.getSmsInfoId(), saveReceiptsVo.getCurrentApplyinstIds());

        //保存受理回执，物料回执
        receiveService.saveReceive(saveReceiptsVo.getCurrentApplyinstIds().toArray(new String[0]), saveReceiptsVo.getReceiptTypes(), SecurityContext.getCurrentUserName(), saveReceiptsVo.getComments());
    }

    /**
     * 目前一个申报实例对应一个领件人
     *
     * @param smsInfoId    领件人id
     * @param applyinstIds 申报实例id
     */
    private void updateAeaSmsInfo(String smsInfoId, Set<String> applyinstIds) throws Exception {
        if (CollectionUtils.isNotEmpty(applyinstIds)) {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoById(smsInfoId);
            for (String applyinstId : applyinstIds) {
                // 先判断是否存在申请号或者申请实例ID与传过来的不一致：因为数据库applyisnt_id不能为空，默认第一次是item_code
                if (aeaHiSmsInfo != null && StringUtils.isBlank(aeaHiSmsInfo.getApplyinstId())) {
                    aeaHiSmsInfo.setApplyinstId(applyinstId);
                    aeaHiSmsInfoMapper.updateAeaHiSmsInfo(aeaHiSmsInfo);
                    continue;
                }
                AeaHiSmsInfo aeaHiSmsInfoByApplyinstId = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId);
                if (aeaHiSmsInfoByApplyinstId == null) {
                    AeaHiSmsInfo newOne = new AeaHiSmsInfo();
                    BeanUtils.copyProperties(newOne, aeaHiSmsInfo);
                    newOne.setId(UuidUtil.generateUuid());
                    newOne.setApplyinstId(applyinstId);
                    newOne.setCreater(SecurityContext.getCurrentUserId());
                    newOne.setCreateTime(new Date());
                    newOne.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaHiSmsInfoMapper.insertAeaHiSmsInfo(newOne);
                }
            }
        }
    }

    protected void saveApplySubject(AeaHiApplyinst aeaHiApplyinst, ApplyDataVo applyDataVo) {
        // 申报主体为个人
        if ("0".equals(applyDataVo.getApplySubject())) {
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(aeaHiApplyinst.getApplyinstId(), applyDataVo.getProjInfoIds(), applyDataVo.getApplyLinkmanId(), applyDataVo.getLinkmanInfoId());
        } else {
            // 建设单位
            List<BuildProjUnitVo> buildProjUnits = applyDataVo.getBuildProjUnitMap();
            if (CollectionUtils.isNotEmpty(buildProjUnits)) {
                Map<String, List<String>> puMap = new HashMap<>(buildProjUnits.size());
                buildProjUnits.forEach(pu -> puMap.put(pu.getProjectInfoId(), pu.getUnitIds()));
                aeaUnitInfoService.insertApplyOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), puMap);
            }
            // 经办单位
            String[] handleUnitIds = applyDataVo.getHandleUnitIds();
            if (handleUnitIds != null && handleUnitIds.length > 0) {
                aeaUnitInfoService.insertApplyNonOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), applyDataVo.getProjInfoIds(), handleUnitIds);
            }
        }
    }

    protected void confirmAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst, ApplyDataVo applyDataVo, String isTemporarySubmit) throws Exception {
        if (StringUtils.isNotBlank(aeaHiApplyinst.getApplyinstId())) {
            AeaHiApplyinst newOne = new AeaHiApplyinst();
            newOne.setApplyinstId(aeaHiApplyinst.getApplyinstId());
            newOne.setProjInfoId(applyDataVo.getProjInfoIds()[0]);
            newOne.setIsGreenWay(StringUtils.isNotBlank(applyDataVo.getIsGreenWay()) ? applyDataVo.getIsGreenWay() : Status.OFF);
            newOne.setIsTemporarySubmit(isTemporarySubmit);
            aeaHiApplyinstService.updateAeaHiApplyinst(newOne);
        }
    }

    public String onlyInstApply(ApplyDataVo applyDataVo) throws Exception {
        String applySource = applyDataVo.getApplySource();
        String applySubject = applyDataVo.getApplySubject();
        String linkmanInfoId = applyDataVo.getLinkmanInfoId();
        String branchOrgMap = applyDataVo.getBranchOrgMap();//是否分局承办，允许为空
        AeaHiApplyinst applyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, applyDataVo.getApplyType().getValue(), branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), "0",null);
        return applyinst.getApplyinstId();
    }

    protected void doStartApply(AeaHiApplyinst aeaHiApplyinst, String comments, String currentWindowId) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getAppId(), "流程模板id不能为空");

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
            Set<String> iteminstIds = updateLogInstTaskId(aeaHiApplyinst, task);

            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), comments);
            //推动流程流转
            taskService.complete(task.getId(), new String[]{"bumenshouli"}, null);

            for (String iteminstId : iteminstIds) {
                // 更新事项状态
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminstId, task.getId(), appinstId, ItemStatus.ACCEPT_DEAL.getValue(), SecurityContext.getCurrentOrgId());
            }

            // 更新申请状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getApplyinstId(), task.getId(), appinstId, ApplyState.ACCEPT_DEAL.getValue(), currentWindowId);

        } else {
            throw new RuntimeException("流程流转失败！");
        }
    }

    protected void doStartApplyAndSuspend(AeaHiApplyinst aeaHiApplyinst) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getAppId(), "流程模板id不能为空");

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

    protected void doInadmissible(AeaHiApplyinst aeaHiApplyinst, String comments) throws Exception {
        Assert.hasText(aeaHiApplyinst.getAppinstId(), "流程模板实例id不能为空");
        Assert.hasText(aeaHiApplyinst.getIteminstId(), "事项实例id不能为空");

        String appinstId = aeaHiApplyinst.getAppinstId();

        ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
        String procInstId;
        if (actStoAppinst == null) {
            // 启动主流程
            BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(aeaHiApplyinst.getAppId(), appinstId, aeaHiApplyinst);
            Assert.isTrue(processInstance != null && processInstance.getProcessInstance() != null, "流程启动失败！");
            procInstId = processInstance.getProcessInstance().getId();
        } else {
            procInstId = actStoAppinst.getProcinstId();
        }
        //推动流程转向结束
        List<Task> list = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if (list != null && list.size() > 0) {
            Task task = list.get(0);
            try {
                bpmProcessService.activateProcessInstanceById(task.getProcessInstanceId());
            } catch (ProcessAlreadyActivateException e) {
                log.info(e.getMessage());
            }
            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), comments);
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

    protected Set<String> updateLogInstTaskId(AeaHiApplyinst aeaHiApplyinst, Task task) {
        Set<String> iteminstIds = new HashSet<>();
        if (ApplyType.SERIES.getValue().equals(aeaHiApplyinst.getIsSeriesApprove())) {
            iteminstIds.add(aeaHiApplyinst.getIteminstId());
        } else if (ApplyType.UNIT.getValue().equals(aeaHiApplyinst.getIsSeriesApprove())) {
            iteminstIds.addAll(aeaHiApplyinst.getIteminstIds());
        }
        Assert.notEmpty(iteminstIds, "更新事项日志实例状态时，找不到对应的事项实例");
        for (String iteminstId : iteminstIds) {
            // 流程发起后，更新初始事项历史的taskId
            AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(iteminstId, aeaHiApplyinst.getAppinstId());
            logItemStateHist.setTaskinstId(task.getId());
            aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);
        }

        // 流程发起后，更新初始申请历史的taskId
        AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(aeaHiApplyinst.getApplyinstId(), aeaHiApplyinst.getAppinstId());
        applyStateHist.setTaskinstId(task.getId());
        aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);

        return iteminstIds;
    }

    protected void unstashCommonProperties(UnStashVo unStashVo, String applyinstId) throws Exception {
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        unStashVo.setAeaHiApplyinst(aeaHiApplyinst);

        List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
        Assert.state(aeaApplyinstProjs.size() > 0, "根据申报实例找不到对应的项目信息, applyinstId: " + applyinstId);
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(aeaApplyinstProjs.get(0).getProjInfoId());
        unStashVo.setProjInfoId(aeaProjInfo.getProjInfoId());
        unStashVo.setThemeId(aeaProjInfo.getThemeId());
    }
}
