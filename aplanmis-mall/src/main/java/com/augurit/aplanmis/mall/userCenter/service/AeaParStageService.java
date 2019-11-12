package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaApplyinstUnitProjMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import com.google.common.collect.Lists;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * 并联申报service
 * author:sam
 */
@Service
@Transactional
public class AeaParStageService {
    private final Logger logger= LoggerFactory.getLogger(AeaParStageService.class);

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaBpmProcessService aeaBpmProcessService;
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
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private RestTimeruleinstService restTimeruleinstService;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private AeaServiceWindowUserService aeaServiceWindowUserService;
    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;

    /**
     * 保存实例、启动流程（停留在收件节点）
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     * @throws Exception
     */
    public String[] stagingApply(StageApplyDataVo stageApplyDataVo) throws Exception {

        //创建事项、情形、申请实例，并启动流程
        StageApplyInstantiateResult result = this.instantiateStageApply(stageApplyDataVo, false);
        for (String procinstId : result.getProcInstIds()) {
            //挂起流程
            bpmProcessService.suspendProcessInstanceById(procinstId);
        }
        return stageApplyDataVo.getApplyinstIds();
    }

    /**
     * 发起申报（流转到部门审批节点）
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     * @throws Exception
     */
    public ParallelApplyResultVo stageApply(StageApplyDataVo stageApplyDataVo) throws Exception {
        ParallelApplyResultVo resultVo=new ParallelApplyResultVo();
        List<Task> tasks = new ArrayList();

//        //已经先创建实例、启动流程，下一个节点是部门审批
//        if (stageApplyDataVo.getApplyinstIds().length > 0) {
//            for (String applyinstId : stageApplyDataVo.getApplyinstIds()) {
//
//                ActStoAppinst actStoAppinst = new ActStoAppinst();
//                actStoAppinst.setMasterRecordId(applyinstId);
//                actStoAppinst.setFlowMode("proc");
//                List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(actStoAppinst);
//
//                if (appinsts.size() > 0) {
//                    actStoAppinst = appinsts.get(0);
//                    tasks.addAll(taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list());
//                    bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());//激活流程
//                }
//            }
//        } else {}
//            //直接发起申报
            StageApplyInstantiateResult result = this.instantiateStageApply(stageApplyDataVo, false);
            for (String procinstId : result.getProcInstIds()) {
                if(procinstId!=null)
                    tasks.addAll(taskService.createTaskQuery().processInstanceId(procinstId).list());
            }
            resultVo=result.getParallelApplyResultVo();
            resultVo.setApplyinstIds(result.getApplyinstIds());

        //先动态获取当前申报项目所属区域的受理窗口人员，
        List<String> taskAssignees = getTaskAssigneeByProjInfo(stageApplyDataVo.getProjInfoIds()[0]);
        if(taskAssignees.size() == 0){
            throw new RuntimeException("申报失败！当前申报项目所属的区域，没有配置办理窗口或没有配置窗口人员，请联系管理员进行相关配置！");
        }
        //循环推动流程流向部门审批节点，包括了并联审批和并行推进事项
        for (Task task : tasks) {
            //推动流程流转，流转至预审
            //1、设置到流程中，动态填充预审节点的办理人
            aeaBpmProcessService.fillNextTaskAssignee(task,taskAssignees);
            //2、完成当前节点，推动流程往下流转
            taskService.complete(task.getId());
        }
        List<ApplyInstantiateResult> applyInstantiateResults=resultVo.getApplyInstantiateResults();
        for (ApplyInstantiateResult applyInstantiateResult:applyInstantiateResults){
            //更改事项实例状态和新增历史记录
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyInstantiateResult.getApplyinstId());
            for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), tasks.get(0).getId(), applyInstantiateResult.getAppinstId(), ItemStatus.RECEIVE_APPLY.getValue(),null);
            }
            //更改申请实例状态和新增历史记录
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyInstantiateResult.getApplyinstId(), tasks.get(0).getId(), applyInstantiateResult.getAppinstId(), ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),null);

        }
        return resultVo;
    }

    /**
     * 通过项目所属区域id，获取区域下的窗口人员列表
     * @param projInfoId 项目id
     * @return
     */
    private List<String> getTaskAssigneeByProjInfo(String projInfoId){
        List<String> assignees = new ArrayList<>();
        //获取项目id
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if(aeaProjInfo != null){

            List<OpuOmUser> list = aeaServiceWindowUserService.findWindowUserByRegionIdAndAllItemUser(aeaProjInfo.getRegionalism(), SecurityContext.getCurrentOrgId());
            list.forEach(a ->{
                assignees.add(a.getLoginName());
            });
        }
        return assignees;
    }

    /**
     * 并联申报实例化方法
     *
     * @param stageApplyDataVo 并联阶段申报参数实体
     * @param isInadmissible   是否不予受理
     * @throws Exception
     */
    private StageApplyInstantiateResult instantiateStageApply(StageApplyDataVo stageApplyDataVo, boolean isInadmissible) throws Exception {
        ParallelApplyResultVo parallelApplyResultVo=new ParallelApplyResultVo();
        List<ApplyInstantiateResult> applyInstantiateResults=new ArrayList<>();
        //参数非空校验
        if (stageApplyDataVo == null)
            throw new InvalidParameterException("参数对象为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getApplySource()))
            throw new InvalidParameterException("申报来源参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getApplySubject()))
            throw new InvalidParameterException("申报主体参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getLinkmanInfoId()))
            throw new InvalidParameterException("联系人ID参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getStageId()))
            throw new InvalidParameterException("申报阶段ID参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getThemeVerId()))
            throw new InvalidParameterException("主题版本ID参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getAppId()))
            throw new InvalidParameterException("流程模板ID参数为空！");
        if (stageApplyDataVo.getProjInfoIds() == null || stageApplyDataVo.getProjInfoIds().length == 0)
            throw new InvalidParameterException("项目ID参数为空！");
        if (StringUtils.isBlank(stageApplyDataVo.getSmsInfoId()))
            throw new InvalidParameterException("办证取件方式ID参数为空！");
        StageApplyInstantiateResult result = new StageApplyInstantiateResult();

        //获取参数
        List<String> itemVerIds = stageApplyDataVo.getItemVerIds();
        String applySource = stageApplyDataVo.getApplySource();
        String applySubject = stageApplyDataVo.getApplySubject();
        String linkmanInfoId = stageApplyDataVo.getLinkmanInfoId();
        String stageId = stageApplyDataVo.getStageId();
        String themeVerId = stageApplyDataVo.getThemeVerId();
        String[] stateIds = stageApplyDataVo.getStateIds();
        String[] matinstsIds = stageApplyDataVo.getMatinstsIds();
        String appId = stageApplyDataVo.getAppId();
        String[] projInfoIds = stageApplyDataVo.getProjInfoIds();
        String smsInfoId=stageApplyDataVo.getSmsInfoId();
        String applyInstId = stageApplyDataVo.getApplyinstId();
        String branchOrgMap = stageApplyDataVo.getBranchOrgMap();//是否分局承办，允许为空
        String propulsionBranchOrgMap = stageApplyDataVo.getPropulsionBranchOrgMap();//是否分局承办，允许为空
        List<String> propulsionItemVerIds = stageApplyDataVo.getPropulsionItemVerIds();//并行推进事项IDs，允许为空
        List<BuildProjUnitVo> buildProjUnitMap = stageApplyDataVo.getBuildProjUnitMap();
        List<String> unitProjIds = stageApplyDataVo.getProjUnitIds();
        String applyLinkmanId = stageApplyDataVo.getApplyLinkmanId();


        List<String> appinstIds = new ArrayList<>();
        List<String> applyinstIds = new ArrayList<>();
        List<String> procinstIds = new ArrayList<>();
        List<String> iteminstIds = new ArrayList<>();
        List<String> applyinstCodeList = new ArrayList<>();

        AeaProjInfo projInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoIds[0]);
        parallelApplyResultVo.setGcbm(projInfo.getGcbm());
        parallelApplyResultVo.setLocalCode(projInfo.getLocalCode());
        parallelApplyResultVo.setProjName(projInfo.getProjName());
        BscDicCodeItem projTypeDic = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.PROJECT_CLASS ,projInfo.getProjType(), SecurityContext.getCurrentOrgId());
        parallelApplyResultVo.setProjType(projTypeDic==null?"":projTypeDic.getItemName());
        List<String> parallelItemNames=new ArrayList<>();
        List<AeaParaItemVo> aeaParaItemVoList = new ArrayList<>();
        if(itemVerIds!=null && itemVerIds.size()>0){//若并联事项不选，则说明只进行并行事项的申报
            ApplyInstantiateResult stageResult=new ApplyInstantiateResult();
            parallelApplyResultVo.setIsCoreApply("0");
            //1、实例化申请实例
            //
            AeaHiApplyinst aeaHiApplyinst = null;
            if (StringUtils.isNotBlank(applyInstId)){//applyInstId不为空的情况:在调用一张表单时已经提前将申请实例化
                aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyInstId);
            }else {
                aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "0", branchOrgMap,ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue());
            }

            if (aeaHiApplyinst == null)
                throw new RuntimeException("实例化申请实例失败！");
            parallelApplyResultVo.setApplyinstCode(aeaHiApplyinst.getApplyinstCode());
            String applyinstId = aeaHiApplyinst.getApplyinstId();//获取申报实例ID

            aeaHiApplyinst.setProjInfoId(projInfoIds[0]);

            String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID

            //2、实例化并联实例
            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.createAeaHiParStageinst(applyinstId, stageId, themeVerId, appinstId, null);
            //3、实例化事项----此处已经做了事项实例表中的分局承办字段，
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(themeVerId,aeaHiParStageinst.getStageinstId(),itemVerIds,branchOrgMap,null,appinstId);

            aeaHiIteminsts.stream().forEach(aeaHiIteminst -> {
                AeaParaItemVo aeaParaItemVo = new AeaParaItemVo();
                aeaParaItemVo.setItemName(aeaHiIteminst.getIteminstName());
                OpuOmOrg approveOrg = opuOmOrgMapper.getOrg(aeaHiIteminst.getApproveOrgId());
                aeaParaItemVo.setApproveOrgName(approveOrg!=null?approveOrg.getOrgName():"");
                aeaParaItemVo.setIteminstState(aeaHiIteminst.getIteminstState());
                aeaParaItemVo.setIteminstCode(aeaHiIteminst.getIteminstCode());
                aeaParaItemVoList.add(aeaParaItemVo);
            });

            //4、情形实例
            aeaHiParStateinstService.batchInsertAeaHiParStateinst(applyinstId, aeaHiParStageinst.getStageinstId(), stateIds, SecurityContext.getCurrentUserName());

            // 简单合并申报的情况下，可能存在事项自己的情形列表
            saveItemStateBySimpleMerge(stageApplyDataVo, itemVerIds, applyinstId,aeaHiParStageinst.getStageinstId());
            //5、材料输入输出实例
            aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, applyinstId, SecurityContext.getCurrentUserName());

            Map<String, Boolean> approveOrgMap = new HashMap<>();
            Map<String, Boolean> isBranchHandle = new HashMap<>();
            Map<String, Boolean> iteminstMap = new HashMap();//初始化选中情形绑定的事项
            aeaHiIteminsts.forEach(aeaHiIteminst -> {
                AeaItemBasic aeaItemBasic;
                try {
                    aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(aeaHiIteminst.getItemVerId());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("根据itemVerId查询aeaItemBasic错误, itemVerId: {}", aeaHiIteminst.getItemVerId());
                    return;
                }

                parallelItemNames.add(aeaHiIteminst.getIteminstName());
                iteminstIds.add(aeaHiIteminst.getIteminstId());
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
            if (stateIds != null && stateIds.length > 0) {
                Map<String, Boolean> stateinsts = new HashMap();
                for (String stateId : stateIds) {
                    stateinsts.put(stateId, true);
                }
                if (stateinsts.size() > 0)
                    aeaHiApplyinst.setStateinsts(stateinsts);
            }

            //6、启动主流程
            BpmProcessInstance bpmProcessInstance = aeaBpmProcessService.startFlow(appId, appinstId, aeaHiApplyinst);

            if (bpmProcessInstance == null)
                throw new RuntimeException("并联申报流程实例化失败！阶段ID为：" + stageId);
            //新增时限规则
            restTimeruleinstService.createTimeruleinstByProcinst(appId,bpmProcessInstance.getProcessInstance().getId(),bpmProcessInstance.getProcessInstance().getProcessDefinitionKey());
            //流程发起后，更新初始事项历史的taskId
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(bpmProcessInstance.getProcessInstance().getId()).list();//查询出流程第一个节点
            aeaHiIteminsts.forEach(aeaHiIteminst -> {
                AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(),appinstId);
                logItemStateHist.setTaskinstId(tasks.get(0).getId());
                aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);
            });

            //7.1、单位本身的申报主体
            this.insertApplySubject(applySubject, applyinstId, projInfoIds, stageApplyDataVo.getUnitInfoId(), linkmanInfoId);

            //7.2、新增单位的申报主体
            if (unitProjIds!=null&&unitProjIds.size()>0)
            this.insertApplySubject(applySubject, applyinstId, projInfoIds, applyLinkmanId, linkmanInfoId, unitProjIds);

            //更新办件结果领取方式
            AeaHiSmsInfo aeaSmsInfo=new AeaHiSmsInfo();
            aeaSmsInfo.setId(smsInfoId);
            aeaSmsInfo.setApplyinstId(applyinstId);
            aeaHiSmsInfoService.updateAeaHiSmsInfo(aeaSmsInfo);

            appinstIds.add(bpmProcessInstance.getActStoAppinst().getAppinstId());
            procinstIds.add(bpmProcessInstance.getProcessInstance().getId());
            applyinstIds.add(applyinstId);
            applyinstCodeList.add(aeaHiApplyinst.getApplyinstCode());
            stageResult.setAppinstId(bpmProcessInstance.getActStoAppinst().getAppinstId());
            stageResult.setApplyinstId(applyinstId);
            stageResult.setProcInstId(bpmProcessInstance.getProcessInstance().getId());
            applyInstantiateResults.add(stageResult);
        }
        parallelApplyResultVo.setParaItemVoList(aeaParaItemVoList);
        parallelApplyResultVo.setParallelItemNames(parallelItemNames);


        //8、并行推进事项
        if (!isInadmissible && propulsionItemVerIds != null && propulsionItemVerIds.size() > 0) {
            List<AeaCoreItemVo> aeaCoreItemVos=new ArrayList<>();
            int index = 1;
            List<PropulsionItemStateVo> propulsionItemStateIds = stageApplyDataVo.getPropulsionItemStateIds();
            Map<String, List<String>> propulsionItemStateIdMap = new HashMap<>();
            propulsionItemStateIds.forEach(p -> propulsionItemStateIdMap.put(p.getItemVerId(), p.getStateIds()));
            //每个并行推进的事项发起一个单项申报
            for (String itemVerId : propulsionItemVerIds) {
                ApplyInstantiateResult seriesResult=new ApplyInstantiateResult();

                AeaCoreItemVo aeaCoreItemVo=new AeaCoreItemVo();
                //实例化串联申请实例
                AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "1", branchOrgMap,ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue());

                if (seriesApplyinst == null)
                    throw new RuntimeException("实例化并行推进事项申请实例失败！");
                aeaCoreItemVo.setApplyinstCode(seriesApplyinst.getApplyinstCode());
                String seriesApplyinstId = seriesApplyinst.getApplyinstId();//并行推进事项申请实例ID
                applyinstIds.add(seriesApplyinstId);
                applyinstCodeList.add(seriesApplyinst.getApplyinstCode());
                AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
                String propulsionAppinstId = UUID.randomUUID().toString();//预先生成并行推进单项模板实例ID

                //1、保存单项实例
                AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, propulsionAppinstId,"1",stageId);

                //2、事项实例
                AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, propulsionBranchOrgMap,null,propulsionAppinstId);
                iteminstIds.add(aeaHiIteminst.getIteminstId());
                aeaCoreItemVo.setIteminstCode(aeaHiIteminst.getIteminstCode());
                aeaCoreItemVo.setIteminstState(aeaHiIteminst.getIteminstState());
                aeaCoreItemVo.setItemName(aeaHiIteminst.getIteminstName());
                // 分局承办设置 根据itemVerId查map， 有则说明是分局承办，否则市局承办
                Map<String, Boolean> approveOrgItem = new HashMap<>();
                Map<String, Boolean> isBranchHandleItem = new HashMap<>();
                OpuOmOrg approveOrg = opuOmOrgMapper.getOrg(aeaHiIteminst.getApproveOrgId());
                aeaCoreItemVo.setApproveOrgName(approveOrg.getOrgName());
                String approveOrgCode = approveOrg.getOrgCode();
                if (StringUtils.isNotBlank(BusinessUtil.getOrgIdFromBranchOrgMap(propulsionBranchOrgMap, itemVerId))) {
                    approveOrgItem.put(approveOrgCode, true);
                    isBranchHandleItem.put(aeaHiIteminst.getIteminstCode(), true);
                } else {
                    approveOrgItem.put(approveOrgCode, false);
                    isBranchHandleItem.put(aeaHiIteminst.getIteminstCode(), false);
                }
                seriesApplyinst.setIsBranchHandle(isBranchHandleItem);
                seriesApplyinst.setApprovalOrgCode(approveOrgItem);
                seriesApplyinst.setProjInfoId(projInfoIds[0]);

                //4、情形实例
                if (CollectionUtils.isNotEmpty(propulsionItemStateIdMap.get(itemVerId))) {
                    String[] itemStateIds = propulsionItemStateIdMap.get(itemVerId).toArray(new String[0]);
                    aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null,itemStateIds, SecurityContext.getCurrentUserName());
                }

                //更新办件结果领取方式
                AeaHiSmsInfo seriesSms =aeaHiSmsInfoService.getAeaHiSmsInfoById(smsInfoId);
                seriesSms.setId(UUID.randomUUID().toString());
                seriesSms.setApplyinstId(seriesApplyinstId);
                aeaHiSmsInfoService.createAeaHiSmsInfo(seriesSms);

                //5、材料输入输出实例
                aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, seriesApplyinstId, SecurityContext.getCurrentUserName());

                //6、启动主流程
                BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(aeaItemBasic.getAppId(), propulsionAppinstId, seriesApplyinst);

                if (processInstance == null||processInstance.getProcessInstance()==null)
                    throw new RuntimeException("并行推进事项主流程实例化失败！事项版本ID为：" + itemVerId);
                //新增时限规则
                restTimeruleinstService.createTimeruleinstByProcinst(aeaItemBasic.getAppId(),processInstance.getProcessInstance().getId(),processInstance.getProcessInstance().getProcessDefinitionKey());
                //查询出流程第一个节点
                List<Task> seriesTasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();
                //流程发起后，更新初始事项历史的taskId
                AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(),propulsionAppinstId);
                logItemStateHist.setTaskinstId(seriesTasks.get(0).getId());
                aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

                appinstIds.add(processInstance.getActStoAppinst().getAppinstId());
                procinstIds.add(processInstance.getProcessInstance().getId());

              //7.1、单位本身的申报主体
                this.insertApplySubject(applySubject, seriesApplyinstId, projInfoIds, stageApplyDataVo.getUnitInfoId(), linkmanInfoId);

                //7.2、新增单位的申报主体
                if (unitProjIds!=null&&unitProjIds.size()>0)
                    this.insertApplySubject(applySubject, seriesApplyinstId, projInfoIds, applyLinkmanId, linkmanInfoId, unitProjIds);

                index++;
                aeaCoreItemVos.add(aeaCoreItemVo);
                seriesResult.setAppinstId(processInstance.getActStoAppinst().getAppinstId());
                seriesResult.setApplyinstId(seriesApplyinstId);
                seriesResult.setProcInstId(processInstance.getProcessInstance().getId());
                applyInstantiateResults.add(seriesResult);
            }
            parallelApplyResultVo.setCoreItemList(aeaCoreItemVos);
        }
        parallelApplyResultVo.setApplyInstantiateResults(applyInstantiateResults);

        //保存申请实例与项目之间的关系 aea_applyinst_proj
        for (String id:applyinstIds){
            AeaApplyinstProj aeaApplyinstProj=new AeaApplyinstProj();
            aeaApplyinstProj.setApplyinstId(id);
            aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
            aeaApplyinstProj.setProjInfoId(projInfoIds[0]);//4.0版本已废弃了多项目申报，所以只有一个值
            aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
            aeaApplyinstProj.setCreateTime(new Date());
            aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);
        }
        //绑定项目的主题
        AeaProjInfo param=new AeaProjInfo();
        param.setThemeId(stageApplyDataVo.getThemeId());
        param.setProjInfoId(projInfoIds[0]);
        aeaProjInfoService.updateAeaProjInfo(param);

        result.setAppinstIds(appinstIds);
        result.setApplyinstIds(applyinstIds);
        result.setProcInstIds(procinstIds);
        result.setIteminstIds(iteminstIds);
        result.setApplyinstCodeList(applyinstCodeList);
        result.setParallelApplyResultVo(parallelApplyResultVo);
        return result;
    }

    private void insertApplySubject(String applySubject, String applyinstId, String[] projInfoIds,String unitInfoId,String linkmanInfoId) throws Exception {
        if ("0".equals(applySubject)) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(applyinstId, projInfoIds, linkmanInfoId, linkmanInfoId);
        } else {
            //建设单位
            if (StringUtils.isEmpty(unitInfoId)) throw new InvalidParameterException("申报主体为企业时，unitInfoId必传");
            Map<String, List<String>> puMap = new HashMap<>(1);
            puMap.put(projInfoIds[0], Lists.newArrayList(new String[]{unitInfoId}));
            aeaUnitInfoService.insertApplyOwnerUnitProj(applyinstId, puMap);
        }
    }

    private void insertApplySubject(String applySubject, String applyinstId, String[] projInfoIds, String applyLinkmanId, String linkmanInfoId, List<String> unitPorjIds) {
        if ("0".equals(applySubject)) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(applyinstId, projInfoIds, applyLinkmanId, linkmanInfoId);
        } else {
            AeaApplyinstUnitProj aeaApplyinstUnitProj = new AeaApplyinstUnitProj();
            aeaApplyinstUnitProj.setApplyinstId(applyinstId);
            if (unitPorjIds==null||unitPorjIds.size()==0) return;
            unitPorjIds.stream().forEach(unitPorId->{
                aeaApplyinstUnitProj.setUnitProjId(unitPorId);
                List<AeaApplyinstUnitProj> aeaApplyinstUnitProjs = aeaApplyinstUnitProjMapper.listAeaApplyinstUnitProj(aeaApplyinstUnitProj);
                if (aeaApplyinstUnitProjs==null||aeaApplyinstUnitProjs.size()==0){
                    aeaApplyinstUnitProj.setCreater(SecurityContext.getCurrentUserName());
                    aeaApplyinstUnitProj.setIsDeleted("0");
                    aeaApplyinstUnitProj.setCreateTime(new Date());
                    aeaApplyinstUnitProjMapper.insertAeaApplyinstUnitProj(aeaApplyinstUnitProj);
                }
            });
        }
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
                        logger.error("简单合并申报保存事项的情形实例时失败, itemVerIds: {}", itemVerIds);
                        throw new RuntimeException("简单合并申报保存事项的情形实例时失败", e);
                    }
                }
            });
        }
    }
}
