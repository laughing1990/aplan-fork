package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyInstantiateResult;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyResultVo;
import com.google.common.collect.Lists;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.*;

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
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private RestTimeruleinstService restTimeruleinstService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaServiceWindowUserService aeaServiceWindowUserService;

    /**
     * 保存实例、启动流程（停留在收件节点）
     *
     * @param seriesApplyDataVo 单项申报参数实体
     * @throws Exception
     */
    public String stagingApply(SeriesApplyDataVo seriesApplyDataVo) throws Exception {

        //创建事项、情形、申请实例，并启动流程
        SeriesApplyInstantiateResult applyResult = this.instantiateSeriesApply(seriesApplyDataVo, false, new String[]{"1", "2"});
        //挂起流程
        bpmProcessService.suspendProcessInstanceById(applyResult.getProcInstId());

        return seriesApplyDataVo.getApplyinstId();
    }

    /**
     * 提交申请流转到部门审批节点）
     *
     * @param seriesApplyDataVo 单项申报参数实体
     * @throws Exception
     */
    public SeriesApplyResultVo stageApplay(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
//        if (StringUtils.isBlank(seriesApplyDataVo.getComments()))
//            throw new InvalidParameterException("申报意见参数为空！");
        SeriesApplyResultVo resultVo = new SeriesApplyResultVo();
        List<Task> tasks = new ArrayList();

        String appinstId = null;
        String applyinstId = null;
        List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
        //直接发起申报
        if (StringUtils.isBlank(seriesApplyDataVo.getApplyinstId())) {
            SeriesApplyInstantiateResult applyResult = this.instantiateSeriesApply(seriesApplyDataVo, false, new String[]{"1", "2"});
            tasks.addAll(taskService.createTaskQuery().processInstanceId(applyResult.getProcInstId()).list());

            appinstId = applyResult.getAppinstId();
            applyinstId = applyResult.getApplyinstId();
            resultVo.setApplyinstCode(applyResult.getApplyinstCode());
            aeaHiIteminstList = applyResult.getAeaHiIteminstList();
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

            aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(seriesApplyDataVo.getApplyinstId());
            if (aeaHiIteminstList == null || aeaHiIteminstList.size() == 0)
                throw new RuntimeException("当前申请实例关联事项实例为空！");

            applyinstId = seriesApplyDataVo.getApplyinstId();
        }
        if (tasks.size() > 0) {
            //先动态获取当前申报项目所属区域的受理窗口人员，
            List<String> taskAssignees = getTaskAssigneeByProjInfo(seriesApplyDataVo.getProjInfoIds()[0]);
            if (taskAssignees.size() == 0) {
                throw new RuntimeException("申报失败！当前申报项目所属的区域，没有配置办理窗口或没有配置窗口人员，请联系管理员进行相关配置！");
            }
            Task task = tasks.get(0);
            //收件意见
            //bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), seriesApplyDataVo.getComments());
            //推动流程流转，流转至预审
            //1、设置到流程中，动态填充预审节点的办理人
            aeaBpmProcessService.fillNextTaskAssignee(task, taskAssignees);
            //2、完成当前节点，推动流程往下流转
            taskService.complete(task.getId());
            //更新事项状态
            for (AeaHiIteminst iteminst : aeaHiIteminstList) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), task.getId(), appinstId, ItemStatus.RECEIVE_APPLY.getValue(), null);
            }

            //更新申请状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, task.getId(), appinstId, ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(), null);

        } else
            throw new InvalidParameterException("流程流转失败！");
        resultVo.setApplyinstId(seriesApplyDataVo.getApplyinstId());
        return resultVo;
    }

    /**
     * 通过项目所属区域id，获取区域下的窗口人员列表
     *
     * @param projInfoId 项目id
     * @return
     */
    private List<String> getTaskAssigneeByProjInfo(String projInfoId) {
        List<String> assignees = new ArrayList<>();
        //获取项目id
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo != null) {
            List<OpuOmUser> list = aeaServiceWindowUserService.findWindowUserByRegionIdAndAllItemUser(aeaProjInfo.getRegionalism(), SecurityContext.getCurrentOrgId());
            if (list.size() > 0) {
                list.forEach(a -> {
                    assignees.add(a.getLoginName());
                });
            }
        }
        return assignees;
    }

    /**
     * 单项申报实例化方法
     *
     * @param seriesApplyDataVo
     * @param isNeedSaveReceive 是否需要保存回执信息
     * @param receiptTypes      回执类型数组，当isNeedSaveReceive=true时，需要传递
     * @throws Exception
     */
    private SeriesApplyInstantiateResult instantiateSeriesApply(SeriesApplyDataVo seriesApplyDataVo, boolean isNeedSaveReceive, String[] receiptTypes) throws Exception {
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
        if (StringUtils.isBlank(seriesApplyDataVo.getLinkmanInfoId())) {
            throw new InvalidParameterException("联系人ID参数为空！");
        }
        if (StringUtils.isBlank(seriesApplyDataVo.getAppId())) {
            throw new InvalidParameterException("流程模板ID参数为空！");
        }
        if (seriesApplyDataVo.getProjInfoIds() == null || seriesApplyDataVo.getProjInfoIds().length == 0) {
            throw new InvalidParameterException("项目ID参数为空！");
        }

        SeriesApplyInstantiateResult applyInstantiateResult = new SeriesApplyInstantiateResult();
        List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
        String applySource = seriesApplyDataVo.getApplySource();
        String applySubject = seriesApplyDataVo.getApplySubject();
        String linkmanInfoId = seriesApplyDataVo.getLinkmanInfoId();
        String itemVerId = seriesApplyDataVo.getItemVerId();
        String[] matinstsIds = seriesApplyDataVo.getMatinstsIds();
        String appId = seriesApplyDataVo.getAppId();
        String[] projInfoIds = seriesApplyDataVo.getProjInfoIds();
        String[] stateIds = seriesApplyDataVo.getStateIds();
        String unitInfoId = seriesApplyDataVo.getUnitInfoId();
        AeaItemBasic itemBasicByItemVerId = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());
        AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "1", null, ApplyState.RECEIVE_APPROVED_APPLY.getValue());//实例化串联申请实例

        String seriesApplyinstId = seriesApplyinst.getApplyinstId();//申报实例ID
        seriesApplyinst.setProjInfoId(projInfoIds[0]);

        seriesApplyDataVo.setApplyinstId(seriesApplyinstId);//回填申请实例ID

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID

        //1、保存单项实例
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, appinstId, "0", null);

        //2、事项实例
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, null, null, appinstId);
        aeaHiIteminstList.add(aeaHiIteminst);
        Map mapOrg = new HashMap();
        Map isBranchHandle = new HashMap();
        mapOrg.put("itemVerId", itemBasicByItemVerId.getItemVerId());
        mapOrg.put("branchOrg", aeaHiIteminst.getApproveOrgId());
        seriesApplyinst.setBranchOrg(mapOrg.toString());
        isBranchHandle.put(itemBasicByItemVerId.getItemCategoryMark(), false);
        seriesApplyinst.setIsBranchHandle(isBranchHandle);

        //3、情形实例
        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());

        //4、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, seriesApplyinstId, SecurityContext.getCurrentUserName());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(appId, appinstId, seriesApplyinst);

        if (processInstance == null || processInstance.getProcessInstance() == null)
            throw new RuntimeException("流程启动失败！");
        //新增时限规则实例
//        restTimeruleinstService.createTimeruleinstByProcinst(appId, processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());
        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();
        //6.流程发起后，更新初始事项历史的taskId
        AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), appinstId);
        logItemStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

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
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(seriesApplyinstId, projInfoIds, linkmanInfoId, linkmanInfoId);
        } else {
            //建设单位
            Map<String, List<String>> puMap = new HashMap<>(1);
            puMap.put(projInfoIds[0], Lists.newArrayList(new String[]{unitInfoId}));
            aeaUnitInfoService.insertApplyOwnerUnitProj(seriesApplyinstId, puMap);
        }

        //保存回执前需要更新aea_hi_sms数据，否则查询不到数据，回执无法保存
        /*if (isNeedSaveReceive) {
            receiveService.saveReceive(new String[]{seriesApplyinstId}, receiptTypes, SecurityContext.getCurrentUserName());
        }*/

        applyInstantiateResult.setAppinstId(appinstId);
        applyInstantiateResult.setApplyinstId(seriesApplyinstId);
        applyInstantiateResult.setIteminstId(aeaHiIteminst.getIteminstId());
        applyInstantiateResult.setProcInstId(processInstance.getProcessInstance().getId());
        applyInstantiateResult.setApplyinstCode(seriesApplyinst.getApplyinstCode());
        applyInstantiateResult.setAeaHiIteminstList(aeaHiIteminstList);
        return applyInstantiateResult;
    }
}
