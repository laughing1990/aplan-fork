package com.augurit.aplanmis.front.apply.service;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.instance.RestTimeruleinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.ApplyInstantiateResult;
import com.augurit.aplanmis.front.apply.vo.ApplyinstIdVo;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
import com.augurit.aplanmis.front.apply.vo.PropulsionItemStateVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import com.augurit.aplanmis.front.receive.service.ReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 并联申报service
 * author:sam
 */
@Service
@Transactional
@Slf4j
public class AeaParStageService {
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaBpmProcessService aeaBpmProcessService;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
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
    private ReceiveService receiveService;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private RestTimeruleinstService restTimeruleinstService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private AplanmisEventPublisher publisher;

    /**
     * 保存实例、启动流程（停留在收件节点）
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     */
    public ApplyinstIdVo stagingApply(StageApplyDataVo stageApplyDataVo) throws Exception {
        //创建事项、情形、申请实例，并启动流程
        List<ApplyInstantiateResult> results = this.instantiateStageApply(stageApplyDataVo, false);

        for (int i = 0; i < results.size(); i++) {
            String procinstId = results.get(i).getProcInstId();
            //挂起流程
            bpmProcessService.suspendProcessInstanceById(procinstId);
        }
        return covertResults(results);
    }

    /**
     * 发起申报（流转到部门审批节点）
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     * @throws Exception
     */
    public ApplyinstIdVo stageApply(StageApplyDataVo stageApplyDataVo) throws Exception {
//        if (StringUtils.isBlank(stageApplyDataVo.getComments()))
//            throw new InvalidParameterException("申报意见参数为空！");

        List<Task> tasks = new ArrayList();
        //String[] applyinstIds = new String[0];
        List<ApplyInstantiateResult> results = new ArrayList<>();

        //已经先创建实例、启动流程，下一个节点是部门审批
        String[] ids = ArrayUtils.addAll(stageApplyDataVo.getApplyinstIds(), stageApplyDataVo.getParallelApplyinstId());
        if (ids.length > 0 && "2".equals(stageApplyDataVo.getIsJustApplyinst())) {//说明已经实例化了申报，事项，材料等
            //applyinstIds = stageApplyDataVo.getApplyinstIds();
            for (String applyinstId : stageApplyDataVo.getApplyinstIds()) {
                ActStoAppinst actStoAppinst = new ActStoAppinst();
                actStoAppinst.setMasterRecordId(applyinstId);
                actStoAppinst.setFlowMode("proc");
                List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(actStoAppinst);

                if (appinsts.size() > 0) {
                    actStoAppinst = appinsts.get(0);
                    tasks.addAll(taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list());
                    bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());//激活流程

                    ApplyInstantiateResult result = new ApplyInstantiateResult();
                    result.setProcInstId(actStoAppinst.getProcinstId());
                    result.setAppinstId(actStoAppinst.getAppinstId());
                    result.setApplyinstId(applyinstId);
                    results.add(result);
                }
            }
        } else {
            //直接发起申报
            results = this.instantiateStageApply(stageApplyDataVo, false);

            //applyinstIds = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                String procinstId = results.get(i).getProcInstId();
                if (procinstId != null) {
                    tasks.addAll(taskService.createTaskQuery().processInstanceId(procinstId).list());
                }
                // applyinstIds[i] = results.get(i).getApplyinstId();
            }
        }

        //循环推动流程流向部门审批节点，包括了并联审批和并行推进事项
        for (Task task : tasks) {
            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), stageApplyDataVo.getComments());
            //推动流程流转，这里固定窗机流程的部门审批节点的id必须为“bumenshenpi”，否则报错
            taskService.complete(task.getId(), new String[]{"bumenshenpi"}, (Map) null);
        }

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        String currentOrgId = SecurityContext.getCurrentOrgId();

        //循环更新事项状态和申请状态，收件》受理
        if (results != null && results.size() > 0) {
            for (ApplyInstantiateResult result : results) {
                String procinstId = result.getProcInstId();
                List<Task> currTasks = null;
                if (procinstId != null)
                    currTasks = taskService.createTaskQuery().processInstanceId(procinstId).list();

                //更改事项实例状态和新增历史记录
                List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(result.getApplyinstId());
                for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), currTasks.get(0).getId(), result.getAppinstId(), ItemStatus.ACCEPT_DEAL.getValue(), currentOrgId);
                }

                //更改申请实例状态和新增历史记录
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(result.getApplyinstId(), currTasks.get(0).getId(), result.getAppinstId(), ApplyState.ACCEPT_DEAL.getValue(), opuWinId);
                /*//发起阶段申报受理事件
                try {
                    publisher.publishEvent(new ApplyAcceptAplanmisEvent(this, stageApplyDataVo.getStageId(), stageApplyDataVo.getProjInfoIds(), SecurityContext.getCurrentUserId(),result.getApplyinstId()));
                }catch (Exception e){
                    //发起事件失败
                    e.printStackTrace();
                }*/
            }
        }
        return covertResults(results);
    }

    private ApplyinstIdVo covertResults(List<ApplyInstantiateResult> results) {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        if (results.size() == 0) return applyinstIdVo;
        String[] applyinstIds = results.size() > 0 ? results.stream().filter(result -> ApplyType.SERIES.getValue().equals(result.getIsSeriesApprove())).map(ApplyInstantiateResult::getApplyinstId).toArray(String[]::new) : new String[]{};
        String[] parrallelApplyinstIds = results.size() > 0 ? results.stream().filter(result -> ApplyType.UNIT.getValue().equals(result.getIsSeriesApprove())).map(ApplyInstantiateResult::getApplyinstId).toArray(String[]::new) : new String[]{};
        applyinstIdVo.setParallelApplyinstId(parrallelApplyinstIds.length > 0 ? parrallelApplyinstIds[0] : null);
        applyinstIdVo.setApplyinstIds(applyinstIds);
        return applyinstIdVo;
    }

    /**
     * 不予受理
     *
     * @param stageApplayDataVo 并联阶段申报参数实体
     */
    public ApplyinstIdVo inadmissible(StageApplyDataVo stageApplayDataVo) throws Exception {
        List<ApplyInstantiateResult> results = this.instantiateStageApply(stageApplayDataVo, true);
        String[] applyinstIds = new String[results.size()];

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        String opuOrgId = SecurityContext.getCurrentOrgId();

        for (int i = 0; i < results.size(); i++) {
            applyinstIds[i] = results.get(i).getApplyinstId();
            String procinstId = results.get(i).getProcInstId();
            String applyinstId = results.get(i).getApplyinstId();
            String appinstId = results.get(i).getAppinstId();

            String taskId = null;
            //推动流程转向结束
            List<Task> list = taskService.createTaskQuery().processInstanceId(procinstId).list();
            if (list != null && list.size() > 0) {
                Task task = list.get(0);
                taskId = task.getId();

                //收件意见
                bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), stageApplayDataVo.getComments());
                //推动流程流转
                taskService.complete(task.getId(), new String[]{"jieshu"}, (Map) null);
            }

            //更改事项实例状态和新增历史记录，收件》不予受理
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskId, appinstId, ItemStatus.OUT_SCOPE.getValue(), opuOrgId);
            }

            //更改申请实例状态和新增历史记录，收件》不予受理
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.OUT_SCOPE.getValue(), opuWinId);
        }

        // 保存回执
        String[] receiptTypes = new String[]{"3"};
        receiveService.saveReceive(applyinstIds, receiptTypes, SecurityContext.getCurrentUserName(), stageApplayDataVo.getComments());

        return covertResults(results);
    }

    /**
     * 意见征求
     *
     * @throws Exception
     */
    public void solicit() throws Exception {
        //do something
    }

    /**
     * 并联申报实例化方法
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     * @param isInadmissible   是否不予受理
     * @throws Exception
     */
    private List<ApplyInstantiateResult> instantiateStageApply(StageApplyDataVo stageApplyDataVo, boolean isInadmissible) throws Exception {
        //参数非空校验
        if (stageApplyDataVo == null)
            throw new InvalidParameterException("参数对象为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getApplySource()))
            throw new InvalidParameterException("申报来源参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getApplySubject()))
            throw new InvalidParameterException("申报主体参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getLinkmanInfoId()) && StringUtils.isBlank(stageApplyDataVo.getApplyLinkmanId()))
            throw new InvalidParameterException("联系人ID参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getAppId()))
            throw new InvalidParameterException("流程模板ID参数为空！");
        if (stageApplyDataVo.getProjInfoIds() == null || stageApplyDataVo.getProjInfoIds().length == 0)
            throw new InvalidParameterException("项目ID参数为空！");

        List<ApplyInstantiateResult> result = new ArrayList<>();

        //获取参数
        List<String> itemVerIds = stageApplyDataVo.getItemVerIds();
        String applySource = stageApplyDataVo.getApplySource();
        String applySubject = stageApplyDataVo.getApplySubject();
        String linkmanInfoId = stageApplyDataVo.getLinkmanInfoId();
        String stageId = stageApplyDataVo.getStageId();
        String themeVerId = stageApplyDataVo.getThemeVerId();
        String branchOrgMap = stageApplyDataVo.getBranchOrgMap();//是否分局承办，允许为空
        String[] stateIds = stageApplyDataVo.getStateIds();
        String[] matinstsIds = stageApplyDataVo.getMatinstsIds();
        String appId = stageApplyDataVo.getAppId();
        String[] projInfoIds = stageApplyDataVo.getProjInfoIds();
        String applyLinkmanId = stageApplyDataVo.getApplyLinkmanId();
        List<BuildProjUnitVo> buildProjUnitMap = stageApplyDataVo.getBuildProjUnitMap();
        String[] handleUnitIds = stageApplyDataVo.getHandleUnitIds();
        List<String> propulsionItemVerIds = stageApplyDataVo.getPropulsionItemVerIds();//并行推进事项IDs，允许为空
        String propulsionBranchOrgMap = stageApplyDataVo.getPropulsionBranchOrgMap();//并行推进事项分局承办，允许为空
        String isJustApplyinst = stageApplyDataVo.getIsJustApplyinst();//是否仅实例化了申报实例
        //if("1".equals(isJustApplyinst) && StringUtils.isBlank(stageApplyDataVo.getParallelApplyinstId())) throw new InvalidParameterException("缺少并联申报实例ID！");
        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID
        AeaHiApplyinst aeaHiApplyinst = null;
        //1、实例化申请实例
        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        if (StringUtils.isNotBlank(stageApplyDataVo.getParallelApplyinstId()) && "1".equals(isJustApplyinst)) {//说明之前仅实例化了申报实例
            aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(stageApplyDataVo.getParallelApplyinstId());
            if (aeaHiApplyinst != null && StringUtils.isNotBlank(aeaHiApplyinst.getApplyinstState())) {
                aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(aeaHiApplyinst.getApplyinstId(), null, appinstId, null, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);
            }
        } else {
            aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applySource, applySubject, linkmanInfoId, "0", branchOrgMap, null, appinstId, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);
        }

        if (aeaHiApplyinst == null)
            throw new RuntimeException("实例化申请实例失败！");

        ApplyInstantiateResult stageResult = new ApplyInstantiateResult();
        List<String> applyinstIds = new ArrayList<>();

        String applyinstId = aeaHiApplyinst.getApplyinstId();//获取申报实例ID

        aeaHiApplyinst.setProjInfoId(projInfoIds[0]);

        //2、实例化并联实例
        AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.createAeaHiParStageinst(applyinstId, stageId, themeVerId, appinstId, null);

        //3、实例化事项----此处已经做了事项实例表中的分局承办字段，
//        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.batchInsertAeaHiIteminst(themeVerId, aeaHiParStageinst.getStageinstId(), itemVerIds, branchOrgMap);
        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(themeVerId, aeaHiParStageinst.getStageinstId(), itemVerIds, branchOrgMap, null, appinstId);

        //4、情形实例
        aeaHiParStateinstService.batchInsertAeaHiParStateinst(applyinstId, aeaHiParStageinst.getStageinstId(), stateIds, SecurityContext.getCurrentUserName());

        // 简单合并申报的情况下，可能存在事项自己的情形列表
        saveItemStateBySimpleMerge(stageApplyDataVo, itemVerIds, applyinstId, aeaHiParStageinst.getStageinstId());

        //5、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, applyinstId, SecurityContext.getCurrentUserName());

        //创建前后置事项输出材料关联
        this.createPre_PostItemMat(matinstsIds, stageId, aeaHiIteminsts);

        Map<String, Boolean> approveOrgMap = new HashMap<>();
        Map<String, Boolean> isBranchHandle = new HashMap<>();
        Map<String, Boolean> iteminstMap = new HashMap();//初始化选中情形绑定的事项
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
            if (StringUtils.isNotBlank(BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, aeaHiIteminst.getItemVerId()))) {
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

        //把所有情形丢到变量里，用于流程启动情形
        if(stateIds!=null&&stateIds.length>0){
            Map<String,Boolean> stateinsts = new HashMap();
            for(String stateId:stateIds){
                stateinsts.put(stateId,true);
            }
            if(stateinsts.size()>0)
                aeaHiApplyinst.setStateinsts(stateinsts);
        }

        //6、启动主流程
        BpmProcessInstance bpmProcessInstance = aeaBpmProcessService.startFlow(appId, appinstId, aeaHiApplyinst);

        if (bpmProcessInstance == null) {
            throw new RuntimeException("并联申报流程实例化失败！阶段ID为：" + stageId);
        }

        /*// 流程启动事件
        if (publisher.eventEnabled(AplanmisEventType.APPLY_START)) {
            publisher.publishEvent(new ApplyStartAplanmisEvent(this));
        }*/

        //新增时限规则实例
        restTimeruleinstService.createTimeruleinstByProcinst(appId, bpmProcessInstance.getProcessInstance().getId(), bpmProcessInstance.getProcessInstance().getProcessDefinitionKey());

        //流程发起后，更新初始事项历史的taskId
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(bpmProcessInstance.getProcessInstance().getId()).list();//查询出流程第一个节点
        aeaHiIteminsts.forEach(aeaHiIteminst -> {
            AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), appinstId);
            logItemStateHist.setTaskinstId(tasks.get(0).getId());
            aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);
        });

        //流程发起后，更新初始申请历史的taskId
        if (tasks != null && tasks.size() > 0) {
            AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(applyinstId, appinstId);
            applyStateHist.setTaskinstId(tasks.get(0).getId());
            aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);
        }


        //7、申报主体
        this.insertApplySubject(applySubject, applyinstId, projInfoIds, applyLinkmanId, linkmanInfoId, buildProjUnitMap, handleUnitIds);

        // 项目与主题绑定
        bindThemeAndProject(stageApplyDataVo.getProjInfoIds(), themeVerId);

        //8、并行推进事项
        if (!isInadmissible && propulsionItemVerIds != null && propulsionItemVerIds.size() > 0) {
            int index = 1;
            List<PropulsionItemStateVo> propulsionItemStateIds = stageApplyDataVo.getPropulsionItemStateIds();
            Map<String, List<String>> propulsionItemStateIdMap = new HashMap<>();
            propulsionItemStateIds.forEach(p -> propulsionItemStateIdMap.put(p.getItemVerId(), p.getStateIds()));
            //每个并行推进的事项发起一个单项申报
            for (String itemVerId : propulsionItemVerIds) {
                String propulsionAppinstId = UUID.randomUUID().toString();//预先生成并行推进单项模板实例ID

                //实例化串联申请实例
                AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applySource, applySubject, linkmanInfoId, "1", branchOrgMap, null, propulsionAppinstId, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);

                if (aeaHiApplyinst == null)
                    throw new RuntimeException("实例化并行推进事项申请实例失败！");

                String seriesApplyinstId = seriesApplyinst.getApplyinstId();//并行推进事项申请实例ID

                applyinstIds.add(seriesApplyinstId);

                AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);

                //1、保存单项实例
                AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, propulsionAppinstId, "1", stageId);

                //2、事项实例
//                AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminst(aeaHiSeriesinst.getSeriesinstId(), itemVerId, propulsionBranchOrgMap);
                AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, propulsionBranchOrgMap, null, propulsionAppinstId);

                // 分局承办设置 根据itemVerId查map， 有则说明是分局承办，否则市局承办
                Map<String, Boolean> approveOrgItem = new HashMap<>();
                Map<String, Boolean> isBranchHandleItem = new HashMap<>();
                String approveOrgCode = opuOmOrgMapper.getOrg(aeaHiIteminst.getApproveOrgId()).getOrgCode();
                if (StringUtils.isNotBlank(BusinessUtil.getOrgIdFromBranchOrgMap(propulsionBranchOrgMap, itemVerId))) {
                    approveOrgItem.put(approveOrgCode, true);
                    isBranchHandleItem.put(aeaItemBasic.getItemCategoryMark(), true);
                } else {
                    approveOrgItem.put(approveOrgCode, false);
                    isBranchHandleItem.put(aeaItemBasic.getItemCategoryMark(), false);
                }
                seriesApplyinst.setIsBranchHandle(isBranchHandleItem);
                seriesApplyinst.setApprovalOrgCode(approveOrgItem);
                seriesApplyinst.setProjInfoId(projInfoIds[0]);

                //把所有情形丢到变量里，用于流程启动情形
                if(propulsionItemStateIds!=null&&propulsionItemStateIds.size()>0){
                    Map<String,Boolean> stateinsts = new HashMap();
                    for(String stateId:stateIds){
                        stateinsts.put(stateId,true);
                    }
                    if(stateinsts.size()>0)
                        seriesApplyinst.setStateinsts(stateinsts);
                }

                //4、情形实例
                if (CollectionUtils.isNotEmpty(propulsionItemStateIdMap.get(itemVerId))) {
                    String[] itemStateIds = propulsionItemStateIdMap.get(itemVerId).toArray(new String[0]);
                    aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, itemStateIds, SecurityContext.getCurrentUserName());
                }

                //5、材料输入输出实例
                aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, seriesApplyinstId, SecurityContext.getCurrentUserName());

                //6、启动主流程
                BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(aeaItemBasic.getAppId(), propulsionAppinstId, seriesApplyinst);

                if (processInstance == null || processInstance.getProcessInstance() == null) {
                    throw new RuntimeException("并行推进事项主流程实例化失败！事项版本ID为：" + itemVerId);
                }

               /* // 流程启动事件
                if (publisher.eventEnabled(AplanmisEventType.APPLY_START)) {
                    publisher.publishEvent(new ApplyStartAplanmisEvent(this));
                }*/

                //新增时限规则实例
                restTimeruleinstService.createTimeruleinstByProcinst(aeaItemBasic.getAppId(), processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());

                //查询出流程第一个节点
                List<Task> seriesTasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();
                //流程发起后，更新初始事项历史的taskId
                AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), propulsionAppinstId);
                logItemStateHist.setTaskinstId(seriesTasks.get(0).getId());
                aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

                //流程发起后，更新初始申请历史的taskId
                AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(seriesApplyinstId, propulsionAppinstId);
                applyStateHist.setTaskinstId(seriesTasks.get(0).getId());
                aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);

                ApplyInstantiateResult seriesResult = new ApplyInstantiateResult();
                seriesResult.setApplyinstId(seriesApplyinstId);
                seriesResult.setAppinstId(processInstance.getActStoAppinst().getAppinstId());
                seriesResult.setProcInstId(processInstance.getProcessInstance().getId());
                seriesResult.setIsSeriesApprove(ApplyType.SERIES.getValue());
                result.add(seriesResult);

                //7、申报主体
                this.insertApplySubject(applySubject, seriesApplyinstId, projInfoIds, applyLinkmanId, linkmanInfoId, buildProjUnitMap, handleUnitIds);

                index++;
            }

        }

        stageResult.setProcInstId(bpmProcessInstance.getProcessInstance().getId());
        stageResult.setAppinstId(bpmProcessInstance.getActStoAppinst().getAppinstId());
        stageResult.setApplyinstId(applyinstId);
        stageResult.setIsSeriesApprove(ApplyType.UNIT.getValue());
        result.add(stageResult);

        applyinstIds.add(applyinstId);

        //保存申请实例与项目之间的关系 aea_applyinst_proj
        for (String id : applyinstIds) {
            AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
            aeaApplyinstProj.setApplyinstId(id);
            aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
            aeaApplyinstProj.setProjInfoId(projInfoIds[0]);//4.0版本已废弃了多项目申报，所以只有一个值
            aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
            aeaApplyinstProj.setCreateTime(new Date());
            aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);
        }

        return result;
    }

    /**
     * 简单合并时即多事项直接合并办理，判断并联事项下是否有选择情形，保存对应的情形实例
     *
     * @param stageApplyDataVo 申报vo
     * @param itemVerIds       并联申报事项
     * @param applyinstId      并联申报实例id
     * @param stageinstId      阶段实例id
     */
    private void saveItemStateBySimpleMerge(StageApplyDataVo stageApplyDataVo, List<String> itemVerIds, String applyinstId, String stageinstId) {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataVo.getStageId());
        // 多事项直接合并办理 handWay=0 时才处理
        if (aeaParStage == null || "1".equals(aeaParStage.getHandWay())) {
            return;
        }
        if (stageApplyDataVo.getParallelItemStateIds() != null && stageApplyDataVo.getParallelItemStateIds().size() > 0) {
            Map<String, List<String>> parallelItemStateIdMap = new HashMap<>();
            stageApplyDataVo.getParallelItemStateIds().forEach(p -> parallelItemStateIdMap.put(p.getItemVerId(), p.getStateIds()));
            itemVerIds.forEach(itemVerId -> {
                if (CollectionUtils.isNotEmpty(parallelItemStateIdMap.get(itemVerId))) {
                    String[] itemStateIds = parallelItemStateIdMap.get(itemVerId).toArray(new String[0]);
                    try {
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

    private void bindThemeAndProject(String[] projInfoIds, String themeVerId) throws Exception {

        AeaParThemeVer themeVer = aeaParThemeVerMapper.selectOneById(themeVerId);
        if (themeVer == null) {
            return;
        }
        String themeId = themeVer.getThemeId();
        if (projInfoIds != null && projInfoIds.length > 0) {
            for (String projInfoId : projInfoIds) {
                AeaProjInfo projInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
                projInfo.setThemeId(themeId);
                projInfo.setThemeVerId(themeVerId);
                projInfo.setModifyTime(new Date());
                projInfo.setModifier(SecurityContext.getCurrentUserId());
                aeaProjInfoMapper.updateAeaProjInfo(projInfo);
            }
        }
    }

    private void insertApplySubject(String applySubject, String applyinstId, String[] projInfoIds, String applyLinkmanId, String linkmanInfoId, List<BuildProjUnitVo> buildProjUnits, String[] handleUnitIds) {
        if ("0".equals(applySubject)) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(applyinstId, projInfoIds, applyLinkmanId, linkmanInfoId);
        } else {
            //建设单位
            if (CollectionUtils.isNotEmpty(buildProjUnits)) {
                Map<String, List<String>> puMap = new HashMap<>(buildProjUnits.size());
                buildProjUnits.forEach(pu -> puMap.put(pu.getProjectInfoId(), pu.getUnitIds()));
                aeaUnitInfoService.insertApplyOwnerUnitProj(applyinstId, puMap);
            }
            //经办单位
            if (handleUnitIds != null && handleUnitIds.length > 0) {
                aeaUnitInfoService.insertApplyNonOwnerUnitProj(applyinstId, projInfoIds, handleUnitIds);
            }
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
}
