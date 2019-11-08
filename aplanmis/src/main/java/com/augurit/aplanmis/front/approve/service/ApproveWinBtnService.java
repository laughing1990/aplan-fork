package com.augurit.aplanmis.front.approve.service;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.constants.OpsActionConstants;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * 窗口流程按钮service
 * @author sam
 */
@Service
@Transactional
public class ApproveWinBtnService {
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private AplanmisEventPublisher publisher;

    /**
     * 窗口流程：推动流程流转并更改申请状态和事项状态；适用于：网上申报 预审通过，不予受理等操作
     * @param sendObject 流程发送对象
     * @param applyinstId 申请实例ID
     * @param applyState 需要变更的申报状态
     * @param itemState 需要变更的事项状态
     * @throws Exception
     * @author sam
     */
    public String wfSendAndChangeApplyAndItemState(BpmTaskSendObject sendObject,String applyinstId,String applyState,String itemState) throws Exception{
        if(sendObject==null|| StringUtils.isBlank(sendObject.getTaskId())||sendObject.getSendConfigs()==null)
            throw new InvalidParameterException("流程发送对象参数为空！");
        if(StringUtils.isBlank(applyinstId))
            throw new InvalidParameterException("申请实例ID为空！");
        if(StringUtils.isBlank(applyState))
            throw new InvalidParameterException("申请状态为空！");
        if(StringUtils.isBlank(itemState))
            throw new InvalidParameterException("事项状态为空！");

        //1、通过申请实例id作为业务主键，查询流程模板实例信息
        ActStoAppinst stoAppinst = new ActStoAppinst();
        stoAppinst.setMasterRecordId(applyinstId);
        stoAppinst.setFlowMode("proc");
        List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(stoAppinst);
        if(appinsts==null)
            throw new RuntimeException("无法申请实例ID为："+applyinstId+"获取到模板流程实例，请检查！");
        String applyStatus = ApplyState.valueOf(applyState).getValue();

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        String opuOrgId = SecurityContext.getCurrentOrgId();
        //2、推动流程流转
        String taskId = bpmTaskService.completeTask(sendObject);

        //3、更新申请状态及维护状态历史
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, sendObject.getTaskId(), appinsts.get(0).getAppinstId(), applyStatus, opuWinId);

        //4、通过申请实例id查询事项实例列表，更新事项实例状态
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        String iteminstStatus = ItemStatus.valueOf(itemState).getValue();
        for(AeaHiIteminst iteminst : aeaHiIteminstList){
            //更新事项状态及维护状态历史
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), sendObject.getTaskId(), appinsts.get(0).getAppinstId(), iteminstStatus, opuOrgId);
        }

        //触发流程发送事件 已废弃
//        Task currTask = bpmTaskService.getRuTaskByTaskId(sendObject.getTaskId());
//        publisher.publishEvent(new BpmNodeSendAplanmisEvent(this,sendObject));


        return taskId;
    }

    /**
     * 窗口流程：推动流程流转并更改申请状态；适用于：受理、办结等操作
     * @param sendObject 流程发送对象
     * @param applyinstId 申请实例ID
     * @param applyState 需要变更的申报状态
     * @throws Exception
     * @author sam
     */
    public String wfSendAndChangeApplyState(BpmTaskSendObject sendObject,String applyinstId,String applyState) throws Exception{
        if(sendObject==null|| StringUtils.isBlank(sendObject.getTaskId())||sendObject.getSendConfigs()==null)
            throw new InvalidParameterException("流程发送对象参数为空！");
        if(StringUtils.isBlank(applyinstId))
            throw new InvalidParameterException("申请实例ID为空！");
        if(StringUtils.isBlank(applyState))
            throw new InvalidParameterException("申请状态为空！");

        ActStoAppinst stoAppinst = new ActStoAppinst();
        stoAppinst.setMasterRecordId(applyinstId);
        stoAppinst.setFlowMode("proc");
        List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(stoAppinst);
        if(appinsts==null)
            throw new RuntimeException("无法申请实例ID为："+applyinstId+"获取到模板流程实例，请检查！");

        //推动流程流转
        String taskId = bpmTaskService.completeTask(sendObject);

        String applyStatus = ApplyState.valueOf(applyState).getValue();
        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        //更新申请状态及维护状态历史
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, sendObject.getTaskId(), appinsts.get(0).getAppinstId(), applyStatus, opuWinId);

        //触发流程发送事件 已废弃
//        Task currTask = bpmTaskService.getRuTaskByTaskId(sendObject.getTaskId());
//        publisher.publishEvent(new BpmNodeSendAplanmisEvent(this,sendObject));

        return taskId;
    }

    /**
     * 只变更申报状态
     * @param applyinstId 申报实例ID
     * @param userOpinion 用户意见
     * @param applyinstState 申报状态
     * @param opsActionCode 操作按钮类型编号
     * @throws Exception
     */
    public void onlyChangeApplyState(String applyinstId,String userOpinion,String applyinstState,String opsActionCode) throws Exception{
        if(StringUtils.isBlank(applyinstId))
            throw new InvalidParameterException("申请实例ID为空！");
        if(StringUtils.isBlank(userOpinion))
            throw new InvalidParameterException("用户意见为空！");
        if(StringUtils.isBlank(applyinstState))
            throw new InvalidParameterException("申请状态为空！");
        if(StringUtils.isBlank(opsActionCode))
            throw new InvalidParameterException("操作按钮类型编号为空，按钮规则为：窗口流程W开头，事项I开头，加按钮拼音首大写字母，如窗口受理：WSL；如事项材料补正：ICLBZ！");

        String opsAction = OpsActionConstants.valueOf(opsActionCode).getName();
        String applyStatus = ApplyState.valueOf(applyinstState).getValue();
        //更新申请状态及维护状态历史
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinstId,userOpinion,opsAction,null,applyStatus,null);
    }
}
