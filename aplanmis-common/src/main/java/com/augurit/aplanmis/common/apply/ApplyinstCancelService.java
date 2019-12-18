package com.augurit.aplanmis.common.apply;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmOrg;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.vo.AeaHiApplyinstCancelVo;
import com.augurit.aplanmis.common.apply.vo.AeaHiItemCancelVo;
import com.augurit.aplanmis.common.apply.vo.ApplyinstCancelInfoVo;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyinstCancelConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiApplyinstCancelLogService;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiApplyinstCancelService;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiItemCancelService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-12-12
 * Description: 工程建设系统撤回办件接口
 */
public abstract class ApplyinstCancelService {

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AeaHiApplyinstCancelService aeaHiApplyinstCancelService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaHiItemCancelService aeaHiItemCancelService;
    @Autowired
    private AeaHiApplyinstCancelLogService aeaHiApplyinstCancelLogService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    private String message = null;

    private final static int STATUS_CODE_200 = 200;
    private final static int STATUS_CODE_201 = 201;
    private final static int STATUS_CODE_101 = 101;
    private final static int STATUS_CODE_102 = 102;
    private final static int STATUS_CODE_103 = 103;
    private final static int STATUS_CODE_104 = 104;

    //撤件时触发的外部事件
    public abstract void preCustomEvents(String applyinstId) throws Exception;

    //撤件后触发的外部事件
    public abstract void postCustomEvents(String applyinstId) throws Exception;

    /**
     * 检测申报实例或事项实例是否满足撤件申请
     *
     * @param applyinstId
     * @return
     */
    public String checkApplyinstAndIteminstState(String applyinstId) {
        return this.resultMessage(this.message, checkApplyinstAndIteminstStates(applyinstId));
    }

    /**
     * 获取报实例或事项实例状态
     *
     * @param applyinstId
     * @return
     */
    private int checkApplyinstAndIteminstStates(String applyinstId) {
        this.message = "符合撤件申请条件！";
        int flag = this.STATUS_CODE_200;
        try {
            if (StringUtils.isBlank(applyinstId)) {
                this.message = "缺少参数！";
                flag = this.STATUS_CODE_104;
            } else {
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
                if (ApplyState.COMPLETED.getValue().equals(aeaHiApplyinst.getApplyinstState()) || ApplyState.WITHDRAWAL_COMPLETED.getValue().equals(aeaHiApplyinst.getApplyinstState())) {
                    this.message = "该办件已经办结，无法撤销！";
                    flag = this.STATUS_CODE_101;
                } else if (ApplyState.WITHDRAWAL_ACCEPTED.getValue().equals(aeaHiApplyinst.getApplyinstState()) || ApplyState.WITHDRAWAL_SUBMITTED.getValue().equals(aeaHiApplyinst.getApplyinstState())) {
                    this.message = "该办件正在申请撤销中！";
                    flag = this.STATUS_CODE_102;
                } else {
                    StringBuffer Message = new StringBuffer();
                    List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
                    for (AeaHiIteminst iteminst : iteminstList) {
                        if (ItemStatus.AGREE.getValue().equals(iteminst.getIteminstState()) || ItemStatus.AGREE_TOLERANCE.getValue().equals(iteminst.getIteminstState()) || ItemStatus.DISAGREE.getValue().equals(iteminst.getIteminstState())) {
                            Message.append("【" + iteminst.getIteminstName() + "】、");
                        }
                    }
                    if (Message.length() > 0) {
                        this.message = Message.substring(0, Message.length() - 1) + "已完成部门审批！";
                        flag = this.STATUS_CODE_201;
                    }
                }
            }
        } catch (Exception e) {
            this.message = "检查申报实例和事项实例出错！";
            flag = this.STATUS_CODE_103;
        }
        return flag;
    }

    /**
     * 保存业主的撤件申请信息
     *
     * @param applyinstCancelInfoVo
     * @return
     * @throws Exception
     */
    public String createApplyinstCancelInfo(ApplyinstCancelInfoVo applyinstCancelInfoVo) throws Exception {

        if (StringUtils.isBlank(applyinstCancelInfoVo.getApplyinstId()) || StringUtils.isBlank(applyinstCancelInfoVo.getApplyReason()) || StringUtils.isBlank(applyinstCancelInfoVo.getApplyUserId())) {
            return "缺少参数！";
        }

        if (StringUtils.isBlank(applyinstCancelInfoVo.getApplyUserIdnumber()) || StringUtils.isBlank(applyinstCancelInfoVo.getApplyUserName()) || StringUtils.isBlank(applyinstCancelInfoVo.getApplyUserPhone())) {
            AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(applyinstCancelInfoVo.getApplyUserId());
            if (linkmanInfo == null) return "找不到申请人信息！";
            applyinstCancelInfoVo.setApplyUserName(linkmanInfo.getLinkmanName());
            applyinstCancelInfoVo.setApplyUserIdnumber(linkmanInfo.getLinkmanCertNo());
            applyinstCancelInfoVo.setApplyUserPhone(linkmanInfo.getLinkmanMobilePhone());
        }

        //回填项目ID
        List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstCancelInfoVo.getApplyinstId());
        if (projInfos.size() < 1) return "找不到项目信息！";

        //查询模板实例信息
        ActStoAppinst appinst = new ActStoAppinst();
        appinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        appinst.setMasterRecordId(applyinstCancelInfoVo.getApplyinstId());
        List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
        if (appinsts.size() < 1) throw new Exception("找不到模板实例！");
        appinst = appinsts.get(0);

        AeaHiApplyinstCancel aeaHiApplyinstCancel = new AeaHiApplyinstCancel();
        BeanUtils.copyProperties(applyinstCancelInfoVo, aeaHiApplyinstCancel);
        aeaHiApplyinstCancel.setApplyinstCancelId(UUID.randomUUID().toString());
        aeaHiApplyinstCancel.setAppinstId(appinst.getAppinstId());
        aeaHiApplyinstCancel.setProjInfoId(projInfos.get(0).getProjInfoId());
        aeaHiApplyinstCancel.setApplyTime(new Date());
        aeaHiApplyinstCancel.setCreater(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCancel.setCreateTime(new Date());
        aeaHiApplyinstCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinstCancel.setCancelState(ApplyinstCancelConstants.SUBMITTED.getValue());
        aeaHiApplyinstCancel.setIsDeleted("0");
        aeaHiApplyinstCancel.setIsSuspendedBefore("1");

        // 挂起主流程并更新申请实例状态
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(appinst.getProcinstId()).list();
        if (tasks.size() < 1) throw new Exception("找不到主流程节点！");
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), tasks.get(0).getId(), appinst.getAppinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue(), null);
        if (!bpmProcessService.isProcessSuspended(appinst.getProcinstId())) {
            bpmProcessService.suspendProcessInstanceById(appinst.getProcinstId());
            aeaHiApplyinstCancel.setIsSuspendedBefore("0");// 记录主流程挂起状态
        }
        aeaHiApplyinstCancelService.saveAeaHiApplyinstCancel(aeaHiApplyinstCancel);

        //获取历史记录实例
        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getLastApplyStageLogByState(aeaHiApplyinstCancel.getApplyinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue());
        if (aeaLogApplyStateHist == null) throw new Exception("申报历史实例信息为空！");
        // 申报撤件实例和申办历史实例关联
        AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog = new AeaHiApplyinstCancelLog();
        aeaHiApplyinstCancelLog.setCancelLogId(UUID.randomUUID().toString());
        aeaHiApplyinstCancelLog.setApplyinstCancelId(aeaHiApplyinstCancel.getApplyinstCancelId());
        aeaHiApplyinstCancelLog.setApplyStateHistId(aeaLogApplyStateHist.getStateHistId());
        aeaHiApplyinstCancelLog.setCreater(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCancelLog.setCreateTime(new Date());
        aeaHiApplyinstCancelLogService.saveAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);

        // 挂起子流程并更新事项实例状态
        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinstCancel.getApplyinstId());
        for (AeaHiIteminst iteminst : iteminstList) {
            if (StringUtils.isNotBlank(iteminst.getProcinstId())) {
                if (!bpmProcessService.isProcessSuspended(iteminst.getProcinstId())) {
                    bpmProcessService.suspendProcessInstanceById(iteminst.getProcinstId());
                    iteminst.setIsSuspendedBefore("0");// 记录主流程挂起状态
                    aeaHiIteminstService.updateAeaHiIteminst(iteminst);
                }
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(iteminst.getProcinstId()).singleResult();
                if (historicProcessInstance == null) throw new Exception("找不到流程实例信息！");
                //判断当前流程是否结束
                if (historicProcessInstance.getEndTime() == null) {
                    List<Task> taskList = taskService.createTaskQuery().processInstanceId(iteminst.getProcinstId()).list();
                    if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskList.get(0).getId(), appinst.getAppinstId(), ItemStatus.APPLY_REVOKE.getValue(), iteminst.getApproveOrgId());
                } else {
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), "业主申请撤件", "申报撤件", null, ItemStatus.APPLY_REVOKE.getValue(), iteminst.getApproveOrgId());
                }
            }
        }
        return null;
    }

    /**
     * 部门人员签收撤件申请信息
     *
     * @param iteminstCancelId
     * @throws Exception
     */
    public void signUpIteminstCancelTask(String iteminstCancelId) throws Exception {
        if (StringUtils.isBlank(iteminstCancelId)) throw new Exception("缺少参数!");
        AeaHiItemCancel aeaHiItemCancel = aeaHiItemCancelService.getAeaHiItemCancelById(iteminstCancelId);
        if (aeaHiItemCancel == null) throw new Exception("找不到撤件申请信息!");
        String opsUserId = SecurityContext.getCurrentUserId();
        if (StringUtils.isBlank(opsUserId)) throw new Exception("无法获取当前用户信息!");
        aeaHiItemCancel.setApprovalUserId(opsUserId);
        aeaHiItemCancel.setApprovalUserName(SecurityContext.getCurrentUser().getUserName());
        aeaHiItemCancel.setApprovalSignTime(new Date());
        aeaHiItemCancel.setCancelState(ApplyinstCancelConstants.ACCEPTED.getValue());
        aeaHiItemCancel.setModifier(opsUserId);
        aeaHiItemCancel.setModifyTime(new Date());
        aeaHiItemCancelService.updateAeaHiItemCancel(aeaHiItemCancel);
    }

    /**
     * 窗口人员签收撤件申请信息
     *
     * @param applyinstCancelId
     * @throws Exception
     */
    public void signUpApplyinstCancelTask(String applyinstCancelId) throws Exception {
        if (StringUtils.isBlank(applyinstCancelId)) throw new Exception("缺少参数!");
        AeaHiApplyinstCancel aeaHiApplyinstCancel = aeaHiApplyinstCancelService.getAeaHiApplyinstCancelById(applyinstCancelId);
        if (aeaHiApplyinstCancel == null) throw new Exception("找不到撤件申请信息!");
        String opsUserId = SecurityContext.getCurrentUserId();
        if (StringUtils.isBlank(opsUserId)) throw new Exception("无法获取当前用户信息!");
        aeaHiApplyinstCancel.setHandleUserId(opsUserId);
        aeaHiApplyinstCancel.setHandleUserName(SecurityContext.getCurrentUser().getUserName());
        aeaHiApplyinstCancel.setHandleSignTime(new Date());
        aeaHiApplyinstCancel.setCancelState(ApplyinstCancelConstants.ACCEPTED.getValue());
        aeaHiApplyinstCancel.setModifier(opsUserId);
        aeaHiApplyinstCancel.setModifyTime(new Date());
        aeaHiApplyinstCancelService.updateAeaHiApplyinstCancel(aeaHiApplyinstCancel);
    }

    /**
     * 窗口人员受理撤件申请
     *
     * @param cancelVo
     * @return
     * @throws Exception
     */
    public String updateIteminstCancelInfo(AeaHiApplyinstCancelVo cancelVo) throws Exception {

        if (StringUtils.isBlank(cancelVo.getApplyinstCancelId()) || StringUtils.isBlank(cancelVo.getHandleOpinion()) || StringUtils.isBlank(cancelVo.getCancelState())) {
            return "缺少参数";
        }
        AeaHiApplyinstCancel aeaHiApplyinstCancel = aeaHiApplyinstCancelService.getAeaHiApplyinstCancelById(cancelVo.getApplyinstCancelId());
        if (aeaHiApplyinstCancel == null) return "找不到撤件申请信息！";
        if (!ApplyinstCancelConstants.SUBMITTED.getValue().equals(aeaHiApplyinstCancel.getCancelState()))
            return "该撤件申请已受理！";
        aeaHiApplyinstCancel.setHandleOpinion(cancelVo.getHandleOpinion());
        aeaHiApplyinstCancel.setCancelState(cancelVo.getCancelState());
        aeaHiApplyinstCancel.setAttId(cancelVo.getAttId());
        aeaHiApplyinstCancel.setHandleEndTime(new Date());
        aeaHiApplyinstCancel.setModifier(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCancel.setModifyTime(new Date());

        //获取历史记录实例
        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getLastApplyStageLogByState(aeaHiApplyinstCancel.getApplyinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue());
        if (aeaLogApplyStateHist == null) throw new Exception("申报历史实例信息为空！");
        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        //获取所有事项实例
        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinstCancel.getApplyinstId());
        //获取主流程实例,并且推到办结节点
        ActStoAppinst appinst = actStoAppinstService.getActStoAppinstById(aeaHiApplyinstCancel.getAppinstId());
        if (appinst == null) throw new Exception("找不到流程模板实例!");
        if (ApplyinstCancelConstants.ACCEPTED.getValue().equals(cancelVo.getCancelState())) {
            //受理撤件申请
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCancel.getAppinstId(), ApplyState.WITHDRAWAL_ACCEPTED.getValue(), opuWinId);
            //创建部门人员的撤件审批实例和更改事项实例状态
            this.createIteminstCancelInfo(iteminstList, aeaHiApplyinstCancel.getHandleOpinion(), aeaHiApplyinstCancel.getApplyinstCancelId(), aeaHiApplyinstCancel.getAppinstId());
            //在预审节点撤件,则直接流转到结束节点
            AeaHiItemCancel hiItemCancel = new AeaHiItemCancel();
            hiItemCancel.setApplyinstCancelId(aeaHiApplyinstCancel.getApplyinstCancelId());
            hiItemCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiItemCancel> itemCancels = aeaHiItemCancelService.listAeaHiItemCancel(hiItemCancel);
            if (itemCancels.size() < 1) {
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(appinst.getProcinstId()).list();
                if (taskList.size() < 1) throw new Exception("找不到主流程的节点！");
                for (Task task : taskList) {
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(appinst.getProcinstId()).singleResult();
                    if (historicProcessInstance.getEndTime() == null) {
                        bpmTaskService.taskChangeToEnd(task.getId(), historicProcessInstance.getEndActivityId(), StringUtils.isBlank(cancelVo.getHandleOpinion()) ? "同意撤件，终止流程" : cancelVo.getHandleOpinion());
                    }
                }
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCancel.getAppinstId(), ApplyState.WITHDRAWAL_COMPLETED.getValue(), opuWinId);
            }
        } else if (ApplyinstCancelConstants.NOT_PASS.getValue().equals(cancelVo.getCancelState())) {
            //激活主流程并更改申请实例状态
            if (!"1".equals(aeaHiApplyinstCancel.getIsSuspendedBefore())) {
                if (bpmProcessService.isProcessSuspended(appinst.getProcinstId())) {
                    bpmProcessService.activateProcessInstanceById(appinst.getProcinstId());
                }
            }
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCancel.getAppinstId(), aeaLogApplyStateHist.getOldState(), opuWinId);

            //激活子流程并更改事项实例状态
            for (AeaHiIteminst iteminst : iteminstList) {
                if (StringUtils.isNotBlank(iteminst.getProcinstId())) {
                    AeaLogItemStateHist itemStateHist = aeaLogItemStateHistService.getLastAeaLogItemStateHistByState(iteminst.getIteminstId(), ItemStatus.APPLY_REVOKE.getValue());
                    if (itemStateHist == null) throw new Exception("找不到事项历史状态记录！");
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(iteminst.getProcinstId()).singleResult();
                    if (historicProcessInstance == null) throw new Exception("找不到流程实例信息！");
                    if (historicProcessInstance.getEndTime() == null) {
                        if ("0".equals(iteminst.getIsSuspendedBefore()) || StringUtils.isBlank(iteminst.getIsSuspendedBefore()) && bpmProcessService.isProcessSuspended(iteminst.getProcinstId())) {
                            bpmProcessService.activateProcessInstanceById(iteminst.getProcinstId());
                        }
                        List<Task> taskList = taskService.createTaskQuery().processInstanceId(iteminst.getProcinstId()).list();
                        if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                        //改变事项实例状态
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskList.get(0).getId(), aeaHiApplyinstCancel.getAppinstId(), itemStateHist.getOldState(), iteminst.getApproveOrgId());
                    } else {
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), cancelVo.getHandleOpinion(), "申报撤件", null, itemStateHist.getOldState(), iteminst.getApproveOrgId());
                    }
                    iteminst.setIsSuspendedBefore("");//清除流程挂起标志
                    aeaHiIteminstService.updateAeaHiIteminst(iteminst);
                }
            }
        }
        aeaHiApplyinstCancelService.updateAeaHiApplyinstCancel(aeaHiApplyinstCancel);
        return null;
    }

    /**
     * 保存“申请撤销”实例数据（使用于窗口申请撤销）
     *
     * @param aeaHiApplyinstCancel
     * @return
     */
    public String saveRevokeData(AeaHiApplyinstCancel aeaHiApplyinstCancel) {
        try {
            if (StringUtils.isBlank(aeaHiApplyinstCancel.getApplyinstId()) || StringUtils.isBlank(aeaHiApplyinstCancel.getApplyReason()) || StringUtils.isBlank(aeaHiApplyinstCancel.getApplyUserId()) || StringUtils.isBlank(aeaHiApplyinstCancel.getHandleOpinion())) {
                return "缺少参数！";
            }

            //回填项目ID
            List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(aeaHiApplyinstCancel.getApplyinstId());
            if (projInfos.size() < 1) return "找不到项目信息！";
            aeaHiApplyinstCancel.setProjInfoId(projInfos.get(0).getProjInfoId());
            //补全申请人信息
            if (StringUtils.isBlank(aeaHiApplyinstCancel.getApplyUserName()) || StringUtils.isBlank(aeaHiApplyinstCancel.getApplyUserIdnumber()) || StringUtils.isBlank(aeaHiApplyinstCancel.getApplyUserPhone())) {
                AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinstCancel.getApplyUserId());
                if (linkmanInfo == null) return "找不到申请人信息！";
                aeaHiApplyinstCancel.setApplyUserName(linkmanInfo.getLinkmanName());
                aeaHiApplyinstCancel.setApplyUserIdnumber(linkmanInfo.getLinkmanCertNo());
                aeaHiApplyinstCancel.setApplyUserPhone(linkmanInfo.getLinkmanMobilePhone());
            }
            //查询接收人部门信息
            List<OpuOmOrg> orgs = SecurityContext.getOrgs();
            if (orgs.size() < 1) return "找不到当前受理人部门信息！";
            if (orgs.get(0).getOrgId().equals(SecurityContext.getCurrentOrgId())) {
                aeaHiApplyinstCancel.setHandleOrgName(orgs.get(orgs.size() - 1).getOrgName());
                aeaHiApplyinstCancel.setHandleOrgId(orgs.get(orgs.size() - 1).getOrgId());
            } else {
                aeaHiApplyinstCancel.setHandleOrgName(orgs.get(0).getOrgName());
                aeaHiApplyinstCancel.setHandleOrgId(orgs.get(0).getOrgId());
            }
            //查询模板实例信息
            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(aeaHiApplyinstCancel.getApplyinstId());
            appinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
            if (appinsts.size() < 1) throw new Exception("找不到模板实例！");
            appinst = appinsts.get(0);

//            String message = null;
            //判断是否符合撤件条件
            int flag = this.checkApplyinstAndIteminstStates(aeaHiApplyinstCancel.getApplyinstId());
            if (this.STATUS_CODE_200 == flag || (this.STATUS_CODE_201 == flag && "1".equals(aeaHiApplyinstCancel.getIsCancel()))) {
                // 保存申请撤销实例数据
                String applyinstCancelId = UUID.randomUUID().toString();
                aeaHiApplyinstCancel.setApplyTime(new Date());
                aeaHiApplyinstCancel.setApplyinstCancelId(applyinstCancelId);
                aeaHiApplyinstCancel.setHandleUserId(SecurityContext.getCurrentUserId());
                aeaHiApplyinstCancel.setHandleUserName(SecurityContext.getCurrentUser().getUserName());
                aeaHiApplyinstCancel.setAppinstId(appinst.getAppinstId());
                aeaHiApplyinstCancel.setHandleSignTime(new Date());
                aeaHiApplyinstCancel.setHandleEndTime(new Date());
                aeaHiApplyinstCancel.setCancelState(ApplyinstCancelConstants.ACCEPTED.getValue());
                aeaHiApplyinstCancel.setIsDeleted("0");
                aeaHiApplyinstCancel.setCreater(SecurityContext.getCurrentUserId());
                aeaHiApplyinstCancel.setCreateTime(new Date());
                aeaHiApplyinstCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiApplyinstCancel.setIsSuspendedBefore("1");
                //挂起主流程
                if (!bpmProcessService.isProcessSuspended(appinst.getProcinstId())) {
                    bpmProcessService.suspendProcessInstanceById(appinst.getProcinstId());
                    aeaHiApplyinstCancel.setIsSuspendedBefore("0");
                }
                aeaHiApplyinstCancelService.saveAeaHiApplyinstCancel(aeaHiApplyinstCancel);

                //更改申请实例状态
                String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
                List<Task> tasks = taskService.createTaskQuery().processInstanceId(appinst.getProcinstId()).list();
                if (tasks.size() < 1) throw new Exception("找不到主流程节点！");
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), tasks.get(0).getId(), appinst.getAppinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue(), opuWinId);
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), tasks.get(0).getId(), appinst.getAppinstId(), ApplyState.WITHDRAWAL_ACCEPTED.getValue(), opuWinId);
                //获取历史记录实例
                AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getLastApplyStageLogByState(aeaHiApplyinstCancel.getApplyinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue());
                if (aeaLogApplyStateHist == null) throw new Exception("申报历史实例信息为空！");
                // 申报撤件实例和申办历史实例关联
                AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog = new AeaHiApplyinstCancelLog();
                aeaHiApplyinstCancelLog.setCancelLogId(UUID.randomUUID().toString());
                aeaHiApplyinstCancelLog.setApplyinstCancelId(applyinstCancelId);
                aeaHiApplyinstCancelLog.setApplyStateHistId(aeaLogApplyStateHist.getStateHistId());
                aeaHiApplyinstCancelLog.setCreater(SecurityContext.getCurrentUserId());
                aeaHiApplyinstCancelLog.setCreateTime(new Date());
                aeaHiApplyinstCancelLogService.saveAeaHiApplyinstCancelLog(aeaHiApplyinstCancelLog);
                //更改事项实例状态
                List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinstCancel.getApplyinstId());
                //创建部门人员的撤件审批实例和更改事项实例状态
                this.createIteminstCancelInfo(iteminstList, aeaHiApplyinstCancel.getHandleOpinion(), applyinstCancelId, appinst.getAppinstId());
            } else {
                return this.message;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "撤销办件失败！";
        }

        try {
            this.preCustomEvents(aeaHiApplyinstCancel.getApplyinstId());    //调用外部事件
        } catch (Exception e) {
            return "调用外部事件发生错误！";
        }
        return null;
    }

    /**
     * 撤销办件，终止流程
     *
     * @param itemCancelVo
     * @return
     */
    public String revokeApplyinst(AeaHiItemCancelVo itemCancelVo) {
        try {
            if (StringUtils.isBlank(itemCancelVo.getApplyinstId()) || StringUtils.isBlank(itemCancelVo.getIteminstCancelId()) || StringUtils.isBlank(itemCancelVo.getApprovalOpinion()) || StringUtils.isBlank(itemCancelVo.getCancelState())) {
                return "缺少参数！";
            }
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(itemCancelVo.getApplyinstId());
            if (!ApplyState.WITHDRAWAL_ACCEPTED.getValue().equals(aeaHiApplyinst.getApplyinstState())) {
                return "该撤件申请还未受理！";
            }

            //更新部门审批意见
            AeaHiItemCancel aeaHiItemCancel = aeaHiItemCancelService.getAeaHiItemCancelById(itemCancelVo.getIteminstCancelId());
            if (aeaHiItemCancel == null) return "找不到申请信息！";
            if (aeaHiItemCancel.getApprovalEndTime() != null) return "该申请信息已经办结！";
            aeaHiItemCancel.setApprovalOpinion(itemCancelVo.getApprovalOpinion());
            aeaHiItemCancel.setCancelState(itemCancelVo.getCancelState());
            aeaHiItemCancel.setApprovalEndTime(new Date());
            aeaHiItemCancel.setAttId(itemCancelVo.getAttId());
            aeaHiItemCancel.setModifier(SecurityContext.getCurrentUserId());
            aeaHiItemCancel.setModifyTime(new Date());
            aeaHiItemCancelService.updateAeaHiItemCancel(aeaHiItemCancel);

            //检测所有审批部门是否全部同意撤件
            AeaHiItemCancel hiItemCancel = new AeaHiItemCancel();
            hiItemCancel.setApplyinstCancelId(aeaHiItemCancel.getApplyinstCancelId());
            hiItemCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiItemCancel> itemCancels = aeaHiItemCancelService.listAeaHiItemCancel(hiItemCancel);
            boolean isCancel = true;
            Map<String, AeaHiItemCancel> map = new HashMap();
            Map<String, String> approvalOpinionMap = new HashMap();
            for (AeaHiItemCancel aeaHiItemCancel1 : itemCancels) {
                //存在其他部门未签收或者未审批情况,则不做任何操作
                if (StringUtils.isBlank(aeaHiItemCancel1.getApprovalUserId()) || aeaHiItemCancel1.getApprovalSignTime() == null || aeaHiItemCancel1.getApprovalEndTime() == null) {
                    return null;
                }
                if (ApplyinstCancelConstants.NOT_PASS.getValue().equals(aeaHiItemCancel1.getCancelState()))
                    isCancel = false;
                map.put(aeaHiItemCancel1.getIteminstId(), aeaHiItemCancel1);
                approvalOpinionMap.put(aeaHiItemCancel1.getProcInstId(), aeaHiItemCancel1.getApprovalOpinion());
            }
            AeaHiApplyinstCancel aeaHiApplyinstCancel = aeaHiApplyinstCancelService.getAeaHiApplyinstCancelById(aeaHiItemCancel.getApplyinstCancelId());
            List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(itemCancelVo.getApplyinstId());
            //审批部门全部同意撤销办件
            if (isCancel) {
                for (AeaHiIteminst iteminst : iteminstList) {
                    AeaHiItemCancel aeaHiItemCancel1 = map.get(iteminst.getIteminstId());
                    if (aeaHiItemCancel1 == null) continue;
                    if ("1".equals(aeaHiItemCancel1.getIsConcluding())) {
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), aeaHiItemCancel1.getApprovalOpinion(), "申报撤件", null, ItemStatus.BACK_APPLY.getValue(), iteminst.getApproveOrgId());
                    } else {
                        List<Task> taskList = taskService.createTaskQuery().processInstanceId(iteminst.getProcinstId()).list();
                        if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskList.get(0).getId(), aeaHiApplyinstCancel.getAppinstId(), ItemStatus.BACK_APPLY.getValue(), iteminst.getApproveOrgId());
                    }
                }
                //获取该模板实例下的全部子流程
                ActStoAppinstSubflow actStoAppinstSubflow = new ActStoAppinstSubflow();
                actStoAppinstSubflow.setAppinstId(aeaHiApplyinstCancel.getAppinstId());
                actStoAppinstSubflow.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<ActStoAppinstSubflow> actStoAppinstSubflows = actStoAppinstSubflowService.listActStoAppinstSubflow(actStoAppinstSubflow);
                if (actStoAppinstSubflows.size() < 1) throw new Exception("找不到子流程!");
                for (ActStoAppinstSubflow appinstSubflow : actStoAppinstSubflows) {
                    //激活流程并流转到结束节点
                    if (bpmProcessService.isProcessSuspended(appinstSubflow.getSubflowProcinstId())) {
                        bpmProcessService.activateProcessInstanceById(appinstSubflow.getSubflowProcinstId());
                    }
                    //如果存在多工作项,需把全部task推到结束节点
                    List<Task> taskList = taskService.createTaskQuery().processInstanceId(appinstSubflow.getSubflowProcinstId()).list();
                    if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                    for (Task tasks : taskList) {
                        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(appinstSubflow.getSubflowProcinstId()).singleResult();
                        if (historicProcessInstance == null) throw new Exception("找不到流程实例信息！");
                        if (historicProcessInstance.getEndTime() != null) {
                            break;
                        }
                        bpmTaskService.taskChangeToEnd(tasks.getId(), historicProcessInstance.getEndActivityId(), StringUtils.isBlank(approvalOpinionMap.get(appinstSubflow.getSubflowProcinstId())) ? "同意撤件,终止审批." : approvalOpinionMap.get(appinstSubflow.getSubflowProcinstId()));
                    }
                }
                //获取主流程实例,并且推到办结节点
                ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(aeaHiApplyinstCancel.getAppinstId());
                if (actStoAppinst == null) throw new Exception("找不到流程模板实例!");
                if (bpmProcessService.isProcessSuspended(actStoAppinst.getProcinstId())) {
                    bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());
                }
                //如果当前主流程节点在部门审批，则继续流转
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list();
                if (taskList.size() < 1) throw new Exception("找不到主流程节点!");
                for (Task task : taskList) {
                    if ("bumenshenpi".equals(task.getTaskDefinitionKey())) {
                        BpmTaskSendObject sendObject = new BpmTaskSendObject();
                        sendObject.setTaskId(task.getId());
                        bpmTaskService.completeTask(sendObject);
                    } else {
                        break;
                    }
                }
                //更新申报撤销实例信息
                aeaHiApplyinstCancel.setCancelState(ApplyinstCancelConstants.PASS.getValue());
            } else {
                for (AeaHiIteminst iteminst : iteminstList) {
                    AeaHiItemCancel aeaHiItemCancel1 = map.get(iteminst.getIteminstId());
                    if (aeaHiItemCancel1 == null) continue;
                    AeaLogItemStateHist itemStateHist = aeaLogItemStateHistService.getLastAeaLogItemStateHistByState(iteminst.getIteminstId(), ItemStatus.APPLY_REVOKE.getValue());
                    if (itemStateHist == null) throw new Exception("找不到事项历史状态记录！");
                    if ("1".equals(aeaHiItemCancel1.getIsConcluding())) {
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), aeaHiItemCancel1.getApprovalOpinion(), "申报撤件", null, itemStateHist.getOldState(), iteminst.getApproveOrgId());
                    } else {
                        List<Task> taskList = taskService.createTaskQuery().processInstanceId(iteminst.getProcinstId()).list();
                        if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskList.get(0).getId(), aeaHiApplyinstCancel.getAppinstId(), itemStateHist.getOldState(), iteminst.getApproveOrgId());
                        //恢复流程原来活动状态
                        if (bpmProcessService.isProcessSuspended(iteminst.getProcinstId()) && "0".equals(aeaHiItemCancel1.getIsSuspendedBefore())) {
                            bpmProcessService.activateProcessInstanceById(iteminst.getProcinstId());
                        }
                    }
                    //清空事项实例的流程挂起标志
                    iteminst.setIsSuspendedBefore("");
                    aeaHiIteminstService.updateAeaHiIteminst(iteminst);
                }

                //激活主流程
                ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(aeaHiApplyinstCancel.getAppinstId());
                if (actStoAppinst == null) throw new Exception("找不到流程模板实例!");
                if (bpmProcessService.isProcessSuspended(actStoAppinst.getProcinstId()) && "0".equals(aeaHiApplyinstCancel.getIsSuspendedBefore())) {
                    bpmProcessService.activateProcessInstanceById(actStoAppinst.getProcinstId());
                }

                //获取历史记录实例
                AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getLastApplyStageLogByState(aeaHiApplyinstCancel.getApplyinstId(), ApplyState.WITHDRAWAL_SUBMITTED.getValue());
                if (aeaLogApplyStateHist == null) throw new Exception("申报历史实例信息为空！");
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(actStoAppinst.getProcinstId()).list();
                if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCancel.getApplyinstId(), taskList.get(0).getId(), aeaHiApplyinstCancel.getAppinstId(), aeaLogApplyStateHist.getOldState(), null);
                aeaHiApplyinstCancel.setCancelState(ApplyinstCancelConstants.NOT_PASS.getValue());
            }

            //更新申报撤销申请实例信息
            aeaHiApplyinstCancel.setHandleEndTime(new Date());
            aeaHiApplyinstCancel.setModifier(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCancel.setModifyTime(new Date());
            aeaHiApplyinstCancelService.updateAeaHiApplyinstCancel(aeaHiApplyinstCancel);
        } catch (Exception e) {
            return "办件撤销操作失败！";
        }

        try {
            this.postCustomEvents(itemCancelVo.getApplyinstId());    //调用外部事件
        } catch (Exception e) {
            return "调用外部事件发生错误！";
        }

        return null;
    }

    /**
     * 上传电子附件
     *
     * @param attId
     * @param request
     * @return
     * @throws Exception
     */
    public String uploadAttFile(String attId, String tableName, HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(tableName)) throw new Exception("缺少表名参数！");

        if (StringUtils.isBlank(attId)) {
            attId = UUID.randomUUID().toString();
        }

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            fileUtilsService.uploadAttachments(tableName, "ATT_ID", attId, files);
        }

        return attId;
    }

    /**
     * 删除电子附件（可批量删除）
     *
     * @param detailIds
     * @throws Exception
     */
    public boolean deleteAttFile(String detailIds) throws Exception {
        if (StringUtils.isBlank(detailIds)) throw new Exception("附件ID为空！");
        String[] _detailIds = detailIds.split(",");
        return fileUtilsService.deleteAttachments(_detailIds);
    }

    /**
     * 添加建设单位或项目联系人
     *
     * @param applyinstId
     * @param linkmanInfo
     * @throws Exception
     */
    public void saveUnitLinkman(String applyinstId, AeaLinkmanInfo linkmanInfo) throws Exception {

        if (StringUtils.isBlank(linkmanInfo.getLinkmanInfoId()) && StringUtils.isBlank(applyinstId))
            throw new Exception("缺少参数，无法新增联系人！");
        else {
            if (StringUtils.isNotBlank(linkmanInfo.getLinkmanInfoId())) {
                linkmanInfo.setModifier(SecurityContext.getCurrentUserId());
                linkmanInfo.setModifyTime(new Date());
                aeaLinkmanInfoService.updateAeaLinkmanInfo(linkmanInfo);
            } else {
                //保存联系人信息
                linkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
                linkmanInfo.setLinkmanType("u");
                linkmanInfo.setIsActive("1");
                linkmanInfo.setIsDeleted("0");
                linkmanInfo.setCreater(SecurityContext.getCurrentUserId());
                linkmanInfo.setCreateTime(new Date());
                linkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaLinkmanInfoService.insertAeaLinkmanInfo(linkmanInfo);

                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if (aeaHiApplyinst == null) throw new Exception("找不到申请实例信息");
                if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                    //获取该项目下的所有建设单位
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByApplyinstIdAndUnitType(applyinstId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() < 1) throw new Exception("找不到建设单位信息！");
                    //保存联系人和单位关联关系
                    aeaLinkmanInfoService.insertUnitLinkman(aeaUnitInfos.get(0).getUnitInfoId(), linkmanInfo.getLinkmanInfoId());
                } else {
                    List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
                    if (projInfos.size() < 1) throw new Exception("找不到项目信息！");
                    AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
                    aeaProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                    aeaProjLinkman.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                    aeaProjLinkman.setProjInfoId(projInfos.get(0).getProjInfoId());
                    aeaProjLinkman.setApplyinstId(applyinstId);
                    aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
                    aeaProjLinkman.setCreateTime(new Date());
                    aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
                }
            }
        }
    }

    /**
     * 获取联系人列表
     *
     * @param applyinstId
     * @return
     * @throws Exception
     */
    public List<AeaLinkmanInfo> getLinkmanInfoList(String applyinstId) throws Exception {
        List<AeaLinkmanInfo> linkmanInfos = new ArrayList();
        if (StringUtils.isNotBlank(applyinstId)) {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null) throw new Exception("找不到申请实例信息");
            if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                //获取该项目下的所有建设单位
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByApplyinstIdAndUnitType(applyinstId, UnitType.DEVELOPMENT_UNIT.getValue());
                if (aeaUnitInfos.size() < 1) throw new Exception("找不到建设单位信息！");
                //获取建设单位下的联系人
                linkmanInfos.addAll(aeaLinkmanInfoService.findAllUnitLinkman(aeaUnitInfos.get(0).getUnitInfoId()));
            } else {
                List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
                if (projInfos.size() < 1) throw new Exception("找不到项目信息！");
                //获取项目的联系人
                linkmanInfos.addAll(aeaLinkmanInfoService.findAllUnitLinkman(projInfos.get(0).getProjInfoId()));
            }
        }
        return linkmanInfos;
    }

    /**
     * 部门人员获取撤件申请信息
     *
     * @param iteminstId
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinstCancel> getApplyinstCancelListByIteminstId(String iteminstId) throws Exception {
        List<AeaHiApplyinstCancel> aeaHiApplyinstCancels = new ArrayList();
        if (StringUtils.isNotBlank(iteminstId)) {
            //获取该事项实例的所有撤件申请信息
            aeaHiApplyinstCancels.addAll(aeaHiApplyinstCancelService.listAeaHiApplyinstCancelByIteminstId(iteminstId));
            if (aeaHiApplyinstCancels.size() > 0) {
                for (AeaHiApplyinstCancel aeaHiApplyinstCancel : aeaHiApplyinstCancels) {
                    //获取该撤件申请的部门意见信息
                    AeaHiItemCancel aeaHiItemCancel = new AeaHiItemCancel();
                    aeaHiItemCancel.setApplyinstCancelId(aeaHiApplyinstCancel.getApplyinstCancelId());
                    aeaHiItemCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaHiItemCancel> hiItemCancels = aeaHiItemCancelService.listAeaHiItemCancel(aeaHiItemCancel);
                    //撤件申请信息默认只读状态
                    aeaHiApplyinstCancel.setIsOnlyRead("1");
                    //判断撤件申请是不是已经被窗口人员受理
                    if (!ApplyinstCancelConstants.SUBMITTED.getValue().equals(aeaHiApplyinstCancel.getCancelState())) {
                        for (AeaHiItemCancel aeaHiItemCancel1 : hiItemCancels) {
                            //判断撤件申请是否已进入部门受理
                            if (ApplyinstCancelConstants.ACCEPTED.getValue().equals(aeaHiApplyinstCancel.getCancelState()) && "1".equals(aeaHiApplyinstCancel.getIsOnlyRead())) {
                                //判断该意见是否为本部门的意见，再判断该意见是否已经办结
                                if (aeaHiItemCancel1.getIteminstId().equals(iteminstId) && aeaHiItemCancel1.getApprovalEndTime() == null && StringUtils.isNotBlank(aeaHiItemCancel1.getApprovalUserId()) && aeaHiItemCancel1.getApprovalUserId().equals(SecurityContext.getCurrentUserId())) {
                                    aeaHiApplyinstCancel.setIsOnlyRead("0");
                                    aeaHiApplyinstCancel.setIteminstCancelId(aeaHiItemCancel1.getIteminstCancelId());
                                }
                            }
                            if (StringUtils.isNotBlank(aeaHiItemCancel1.getAttId())) {
                                aeaHiItemCancel1.setBscAttFileAndDirs(this.getAttFiles(aeaHiItemCancel1.getAttId(), "AEA_HI_ITEM_CANCEL"));
                            }
                        }
                        if (hiItemCancels.size() > 0) aeaHiApplyinstCancel.setAeaHiItemCancels(hiItemCancels);
                    }
                    if (StringUtils.isNotBlank(aeaHiApplyinstCancel.getAttId()))
                        aeaHiApplyinstCancel.setBscAttFileAndDirs(this.getAttFiles(aeaHiApplyinstCancel.getAttId(), "AEA_HI_APPLYINST_CANCEL"));
                }
            }
        }
        return aeaHiApplyinstCancels;
    }

    /**
     * 窗口人员获取撤件申请信息
     *
     * @param applyinstId
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinstCancel> getApplyinstCancelListByApplyinstId(String applyinstId) throws Exception {
        List<AeaHiApplyinstCancel> aeaHiApplyinstCancels = new ArrayList();
        if (StringUtils.isNotBlank(applyinstId)) {
            //获取该申请实例的所有撤件申请信息
            AeaHiApplyinstCancel aeaHiApplyinstCancel = new AeaHiApplyinstCancel();
            aeaHiApplyinstCancel.setApplyinstId(applyinstId);
            aeaHiApplyinstCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiApplyinstCancels.addAll(aeaHiApplyinstCancelService.listAeaHiApplyinstCancel(aeaHiApplyinstCancel));
            for (AeaHiApplyinstCancel aeaHiApplyinstCancel1 : aeaHiApplyinstCancels) {
                aeaHiApplyinstCancel1.setIsOnlyRead("1");
                //判断撤件申请是不是已经被窗口人员受理
                if (ApplyinstCancelConstants.SUBMITTED.getValue().equals(aeaHiApplyinstCancel1.getCancelState())) {
                    if (StringUtils.isNotBlank(aeaHiApplyinstCancel1.getHandleUserId()) && StringUtils.isBlank(aeaHiApplyinstCancel1.getHandleOpinion()) && aeaHiApplyinstCancel1.getHandleEndTime() == null && aeaHiApplyinstCancel1.getHandleUserId().equals(SecurityContext.getCurrentUserId())) {
                        aeaHiApplyinstCancel1.setIsOnlyRead("0");
                    }
                } else {
                    //获取该撤件申请的部门意见信息
                    AeaHiItemCancel aeaHiItemCancel = new AeaHiItemCancel();
                    aeaHiItemCancel.setApplyinstCancelId(aeaHiApplyinstCancel1.getApplyinstCancelId());
                    aeaHiItemCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaHiItemCancel> hiItemCancels = aeaHiItemCancelService.listAeaHiItemCancel(aeaHiItemCancel);
                    for (AeaHiItemCancel itemCancel : hiItemCancels) {
                        if (StringUtils.isNotBlank(itemCancel.getAttId())) {
                            itemCancel.setBscAttFileAndDirs(this.getAttFiles(itemCancel.getAttId(), "AEA_HI_ITEM_CANCEL"));
                        }
                    }
                    if (StringUtils.isNotBlank(aeaHiApplyinstCancel1.getAttId()))
                        aeaHiApplyinstCancel.setBscAttFileAndDirs(this.getAttFiles(aeaHiApplyinstCancel1.getAttId(), "AEA_HI_APPLYINST_CANCEL"));
                    aeaHiApplyinstCancel1.setAeaHiItemCancels(hiItemCancels);
                }
            }
        }
        return aeaHiApplyinstCancels;
    }

    /**
     * 获取电子附件列表
     *
     * @param recordId
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<BscAttFileAndDir> getAttFiles(String recordId, String tableName) throws Exception {
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList();
        if (StringUtils.isNotBlank(recordId) && StringUtils.isNotBlank(tableName)) {
            bscAttFileAndDirs.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), tableName, "ATT_ID", new String[]{recordId}));
        }
        return bscAttFileAndDirs;
    }

    /**
     * 创建部门人员撤件审批实例
     *
     * @param iteminstList
     * @param handleOpinion
     * @param applyinstCancelId
     * @param appinstId
     * @throws Exception
     */
    private void createIteminstCancelInfo(List<AeaHiIteminst> iteminstList, String handleOpinion, String applyinstCancelId, String appinstId) throws Exception {
        for (AeaHiIteminst iteminst : iteminstList) {
            //当前已经启动流程的部门才能接收到撤件申请
            if (StringUtils.isNotBlank(iteminst.getProcinstId())) {
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(iteminst.getProcinstId()).singleResult();
                if (historicProcessInstance == null) throw new Exception("找不到流程实例信息！");
                // 申报撤件实例和事项历史实例关联
                AeaHiItemCancel aeaHiItemCancel = new AeaHiItemCancel();
                aeaHiItemCancel.setIteminstCancelId(UUID.randomUUID().toString());
                aeaHiItemCancel.setIteminstId(iteminst.getIteminstId());
                aeaHiItemCancel.setApplyinstCancelId(applyinstCancelId);
                aeaHiItemCancel.setApprovalOrgId(iteminst.getApproveOrgId());
                aeaHiItemCancel.setApprovalOrgName(iteminst.getApproveOrgName());
                aeaHiItemCancel.setCancelState(ApplyinstCancelConstants.SUBMITTED.getValue());
                aeaHiItemCancel.setCreater(SecurityContext.getCurrentUserId());
                aeaHiItemCancel.setCreateTime(new Date());
                aeaHiItemCancel.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiItemCancel.setProcInstId(iteminst.getProcinstId());

                //如果是业主网上申请撤件，则子流程已经挂起
                if (ItemStatus.APPLY_REVOKE.getValue().equals(iteminst.getIteminstState())) {
                    aeaHiItemCancel.setIsConcluding("0");
                    aeaHiItemCancel.setIsSuspendedBefore(iteminst.getIsSuspendedBefore());
                    if (historicProcessInstance.getEndTime() != null) {
                        aeaHiItemCancel.setIsConcluding("1");
                    }
                } else {
                    //判断当前流程是否结束
                    if (historicProcessInstance.getEndTime() == null) {
                        if (bpmProcessService.isProcessSuspended(iteminst.getProcinstId())) {
                            aeaHiItemCancel.setIsSuspendedBefore("1");
                        } else {
                            aeaHiItemCancel.setIsSuspendedBefore("0");
                            bpmProcessService.suspendProcessInstanceById(iteminst.getProcinstId());//挂起当前流程
                        }
                        List<Task> taskList = taskService.createTaskQuery().processInstanceId(iteminst.getProcinstId()).list();
                        if (taskList.size() < 1) throw new Exception("找不到事项子流程的节点！");
                        //改变事项实例状态
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskList.get(0).getId(), appinstId, ItemStatus.APPLY_REVOKE.getValue(), iteminst.getApproveOrgId());
                        aeaHiItemCancel.setIsConcluding("0");
                    } else {
                        aeaHiItemCancel.setIsConcluding("1");
                        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), handleOpinion, "申报撤件", null, ItemStatus.APPLY_REVOKE.getValue(), iteminst.getApproveOrgId());
                    }
                }
                AeaLogItemStateHist itemStateHist = aeaLogItemStateHistService.getLastAeaLogItemStateHistByState(iteminst.getIteminstId(), ItemStatus.APPLY_REVOKE.getValue());
                if (itemStateHist == null) throw new Exception("找不到事项历史状态记录！");
                aeaHiItemCancel.setItemStateHistId(itemStateHist.getStateHistId());
                aeaHiItemCancelService.saveAeaHiItemCancel(aeaHiItemCancel);
            }
        }
    }

    /**
     * 组装返回信息和状态码
     *
     * @param message
     * @param flag
     * @return
     */
    private String resultMessage(String message, int flag) {
        return "{'message':'" + message + "','flag':'" + flag + "'}";
    }

}
