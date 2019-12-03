package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
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
import com.augurit.aplanmis.front.apply.vo.ApplyInstantiateResult;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
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
 * 单项申报service
 * author:sam
 */
@Service
@Transactional
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
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private ApplyCommonService applyCommonService;


    /**
     * 保存实例、启动流程（停留在收件节点）
     *
     * @param seriesApplyDataVo 单项申报参数实体
     */
    public String stagingApply(SeriesApplyDataVo seriesApplyDataVo) throws Exception {

        //创建事项、情形、申请实例，并启动流程
        ApplyInstantiateResult applyResult = this.instantiateSeriesApply(seriesApplyDataVo);
        seriesApplyDataVo.setApplyinstCode(applyResult.getApplyinstCode());
        //挂起流程
        bpmProcessService.suspendProcessInstanceById(applyResult.getProcInstId());

        return seriesApplyDataVo.getApplyinstId();
    }

    /**
     * 发起申报流转到部门审批节点）
     *
     * @param seriesApplyDataVo 单项申报参数实体
     */
    public String stageApplay(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        List<Task> tasks = new ArrayList<>();

        String appinstId = null;
        String applyinstId;
        List<AeaHiIteminst> aeaHiIteminsts;

        //直接发起申报
        if (StringUtils.isBlank(seriesApplyDataVo.getApplyinstId()) || Status.ON.equals(seriesApplyDataVo.getIsJustApplyinst())) {
            ApplyInstantiateResult applyResult = this.instantiateSeriesApply(seriesApplyDataVo);
            seriesApplyDataVo.setApplyinstCode(applyResult.getApplyinstCode());
            tasks.addAll(taskService.createTaskQuery().processInstanceId(applyResult.getProcInstId()).list());

            appinstId = applyResult.getAppinstId();
            applyinstId = applyResult.getApplyinstId();

            aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        } else {
            //实例和流程已经启动，下一节点是部门审批
            ActStoAppinst actStoAppinst = new ActStoAppinst();
            actStoAppinst.setMasterRecordId(seriesApplyDataVo.getApplyinstId());
            actStoAppinst.setFlowMode("proc");
            List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(actStoAppinst);

            if (appinsts.size() > 0) {
                actStoAppinst = appinsts.get(0);
                tasks.addAll(taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list());
                bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());//激活流程

                appinstId = actStoAppinst.getAppinstId();
            }

            aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(seriesApplyDataVo.getApplyinstId());
            if (aeaHiIteminsts == null || aeaHiIteminsts.size() == 0)
                throw new RuntimeException("当前申请实例关联事项实例为空！");

            applyinstId = seriesApplyDataVo.getApplyinstId();
        }

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        String currentOrgId = SecurityContext.getCurrentOrgId();

        if (tasks.size() > 0) {
            Task task = tasks.get(0);
            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), seriesApplyDataVo.getComments());
            //推动流程流转
            taskService.complete(task.getId(), new String[]{"bumenshenpi"}, null);

            //更新事项状态
            for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), task.getId(), appinstId, ItemStatus.ACCEPT_DEAL.getValue(), currentOrgId);
            }

            //更新申请状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, task.getId(), appinstId, ApplyState.ACCEPT_DEAL.getValue(), opuWinId);

        } else
            throw new InvalidParameterException("流程流转失败！");

        return seriesApplyDataVo.getApplyinstId();
    }

    /**
     * 不予受理
     *
     * @param seriesApplyDataVo 单项申报参数实体
     */
    public String inadmissible(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        ApplyInstantiateResult applyResult = this.instantiateSeriesApply(seriesApplyDataVo);
        seriesApplyDataVo.setApplyinstCode(applyResult.getApplyinstCode());
        String appinstId = applyResult.getAppinstId();
        String taskId = null;

        //推动流程转向结束
        List<Task> list = taskService.createTaskQuery().processInstanceId(applyResult.getProcInstId()).list();
        if (list != null && list.size() > 0) {
            Task task = list.get(0);
            taskId = task.getId();

            //收件意见
            bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), seriesApplyDataVo.getComments());
            //推动流程流转
            taskService.complete(task.getId(), new String[]{"jieshu"}, null);
        }

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        String currentOrgId = SecurityContext.getCurrentOrgId();

        //更改事项实例状态和新增历史记录为不予受理
        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(seriesApplyDataVo.getApplyinstId());
        for (AeaHiIteminst iteminst : aeaHiIteminsts) {
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskId, appinstId, ItemStatus.OUT_SCOPE.getValue(), currentOrgId);
        }

        //更改申请实例状态和新增历史记录
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(seriesApplyDataVo.getApplyinstId(), taskId, appinstId, ApplyState.OUT_SCOPE.getValue(), opuWinId);

        return seriesApplyDataVo.getApplyinstId();
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
//            aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinstId, comments, "材料补全", null, aeaHiApplyinst.getApplyinstState(), null);
        }
    }

    /**
     * 单项申报实例化方法
     */
    private ApplyInstantiateResult instantiateSeriesApply(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        //参数非空校验
        if (seriesApplyDataVo == null) {
            throw new InvalidParameterException("参数对象为空！");
        }
        if (StringUtils.isBlank(seriesApplyDataVo.getApplySource())) {
            throw new InvalidParameterException("申报来源参数为空！");
        }
        if (StringUtils.isBlank(seriesApplyDataVo.getApplySubject())) {
            throw new InvalidParameterException("申报主体参数为空！");
        }
        if (StringUtils.isBlank(seriesApplyDataVo.getLinkmanInfoId()) && StringUtils.isBlank(seriesApplyDataVo.getApplyLinkmanId())) {
            throw new InvalidParameterException("联系人ID参数为空！");
        }
        if (StringUtils.isBlank(seriesApplyDataVo.getAppId())) {
            throw new InvalidParameterException("流程模板ID参数为空！");
        }
        if (seriesApplyDataVo.getProjInfoIds() == null || seriesApplyDataVo.getProjInfoIds().length == 0) {
            throw new InvalidParameterException("项目ID参数为空！");
        }
        if (seriesApplyDataVo.getIsParallel() == null) {
            throw new InvalidParameterException("是否并行推进事项参数为空！");
        }
        if ("1".equals(seriesApplyDataVo.getIsParallel()) && seriesApplyDataVo.getStageId() == null) {
            throw new InvalidParameterException("当前为并行推进事项申报，申报阶段ID参数不能为空！");
        }

        ApplyInstantiateResult applyInstantiateResult = new ApplyInstantiateResult();

        String applySource = seriesApplyDataVo.getApplySource();
        String applySubject = seriesApplyDataVo.getApplySubject();
        String linkmanInfoId = seriesApplyDataVo.getLinkmanInfoId();
        String itemVerId = seriesApplyDataVo.getItemVerId();
        String branchOrgMap = seriesApplyDataVo.getBranchOrgMap();
        String[] matinstsIds = seriesApplyDataVo.getMatinstsIds();
        String appId = seriesApplyDataVo.getAppId();
        String[] projInfoIds = seriesApplyDataVo.getProjInfoIds();
        String applyLinkmanId = seriesApplyDataVo.getApplyLinkmanId();
        List<BuildProjUnitVo> buildProjUnits = seriesApplyDataVo.getBuildProjUnitMap();
        String[] handleUnitIds = seriesApplyDataVo.getHandleUnitIds();
        String[] stateIds = seriesApplyDataVo.getStateIds();
        AeaItemBasic itemBasicByItemVerId = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        AeaHiApplyinst seriesApplyinst;
        // 一张表单仅仅实例化了申报实例的情况
        if (Status.ON.equals(seriesApplyDataVo.getIsJustApplyinst())) {
            seriesApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(seriesApplyDataVo.getApplyinstId());
            if (seriesApplyinst == null) {
                throw new Exception("找不到申报实例 applyinstId: " + seriesApplyDataVo.getApplyinstId());
            }
            if (StringUtils.isNotBlank(seriesApplyinst.getApplyinstState())) {
                aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(seriesApplyinst.getApplyinstId(), null, appinstId, null, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);
            }
        } else {
            seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applySource, applySubject, linkmanInfoId, "1", branchOrgMap, null, appinstId, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);//实例化串联申请实例
        }

        String seriesApplyinstId = seriesApplyinst.getApplyinstId();//申报实例ID
        seriesApplyinst.setProjInfoId(projInfoIds[0]);

        seriesApplyDataVo.setApplyinstId(seriesApplyinstId);//回填申请实例ID


        AeaHiSeriesinst seriesInst = aeaHiSeriesinstService.getAeaHiSeriesinstByApplyinstId(seriesApplyinstId);
        AeaHiSeriesinst aeaHiSeriesinst;
        AeaHiIteminst aeaHiIteminst;
        if(seriesInst==null){
            //1、保存单项实例
            aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, appinstId, seriesApplyDataVo.getIsParallel(), seriesApplyDataVo.getStageId());
            //2、事项实例
            aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, branchOrgMap, null, appinstId);
        }else{
            aeaHiSeriesinst=seriesInst;
            aeaHiIteminst=aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(seriesApplyinstId).get(0);
        }


        if (StringUtils.isNotBlank(branchOrgMap)) {
            List<Map> mapList = JSONArray.parseArray(branchOrgMap, Map.class);
            mapList.forEach(map -> {
                OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(map.get("branchOrg").toString());
                Map<String, Boolean> approvalOrgCode = new HashMap<>();
                Map<String, Boolean> isBranchHandle = new HashMap<>();
                approvalOrgCode.put(opuOmOrg.getOrgCode(), true);
                isBranchHandle.put(itemBasicByItemVerId.getItemCategoryMark(), true);
                seriesApplyinst.setApprovalOrgCode(approvalOrgCode);
                seriesApplyinst.setIsBranchHandle(isBranchHandle);
            });
        } else {//市局承办
            Map<String, String> mapOrg = new HashMap<>();
            Map<String, Boolean> isBranchHandle = new HashMap<>();
            mapOrg.put("itemVerId", itemBasicByItemVerId.getItemVerId());
            mapOrg.put("branchOrg", aeaHiIteminst.getApproveOrgId());
            seriesApplyinst.setBranchOrg(mapOrg.toString());
            isBranchHandle.put(itemBasicByItemVerId.getItemCategoryMark(), false);
            seriesApplyinst.setIsBranchHandle(isBranchHandle);
        }

        // 用于流程启动情形
        seriesApplyinst.setStateinsts(applyCommonService.filterProcessStartConditions(stateIds, ApplyType.SERIES));

        //3、情形实例
        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());

        //4、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, seriesApplyinstId, SecurityContext.getCurrentUserName());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(appId, appinstId, seriesApplyinst);

        if (processInstance == null || processInstance.getProcessInstance() == null) {
            throw new RuntimeException("流程启动失败！");
        }

        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();
        //6.流程发起后，更新初始事项历史的taskId
        AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), appinstId);
        logItemStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

        //流程发起后，更新初始申请历史的taskId
        AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(seriesApplyinstId, appinstId);
        applyStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);

        //8.保存申请实例与项目之间的关系 aea_applyinst_proj
        AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
        aeaApplyinstProj.setApplyinstId(seriesApplyinst.getApplyinstId());
        aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
        aeaApplyinstProj.setProjInfoId(projInfoIds[0]);//4.0版本已废弃了多项目申报，所以只有一个值
        aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
        aeaApplyinstProj.setCreateTime(new Date());
        aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);

        //9、申报主体
        if ("0".equals(applySubject)) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(seriesApplyinstId, projInfoIds, applyLinkmanId, linkmanInfoId);
        } else {
            //判断是否存在申报关联关系

            //建设单位
            if (CollectionUtils.isNotEmpty(buildProjUnits)) {
                Map<String, List<String>> puMap = new HashMap<>(buildProjUnits.size());
                buildProjUnits.forEach(pu -> puMap.put(pu.getProjectInfoId(), pu.getUnitIds()));
                aeaUnitInfoService.insertApplyOwnerUnitProj(seriesApplyinstId, puMap);
            }
            //经办单位
            if (handleUnitIds != null && handleUnitIds.length > 0) {
                aeaUnitInfoService.insertApplyNonOwnerUnitProj(seriesApplyinstId, projInfoIds, handleUnitIds);
            }
        }

        //保存回执前需要更新aea_hi_sms数据，否则查询不到数据，回执无法保存
        /*if (isNeedSaveReceive) {
            receiveService.saveReceive(new String[]{seriesApplyinstId}, receiptTypes, SecurityContext.getCurrentUserName());
        }*/

        applyInstantiateResult.setAppinstId(appinstId);
        applyInstantiateResult.setApplyinstId(seriesApplyinstId);
        applyInstantiateResult.setApplyinstCode(seriesApplyinst.getApplyinstCode());
        applyInstantiateResult.setProcInstId(processInstance.getProcessInstance().getId());

        return applyInstantiateResult;
    }
}
