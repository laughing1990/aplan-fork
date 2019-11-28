package com.augurit.aplanmis.front.approve.service;

import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaHiIteminstConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.constants.OpsActionConstants;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * 流程审批按钮服务层
 */

@Service
@Slf4j
@Transactional
public class ApproveItemBtnService {
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AplanmisEventPublisher publisher;
    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    /**
     * 事项流程：推动流程流转并更改事项状态；适用于：受理、办结等操作
     *
     * @param sendObject    流程发送对象
     * @param iteminstId    事项实例ID
     * @param iteminstState 事项实例状态
     * @throws Exception
     */
    public String wfSendAndChangeItemState(BpmTaskSendObject sendObject, String iteminstId, String iteminstState) throws Exception {
        if (sendObject == null || StringUtils.isBlank(sendObject.getTaskId()) || sendObject.getSendConfigs() == null)
            throw new InvalidParameterException("流程发送对象参数为空！");
        if (StringUtils.isBlank(iteminstId))
            throw new InvalidParameterException("事项实例ID为空！");
        if (StringUtils.isBlank(iteminstState))
            throw new InvalidParameterException("事项状态为空！");

        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
        if (iteminst == null)
            throw new RuntimeException("获取事项实例为空，请检查事项实例ID！");

        String appinstId = null;
        String applyinstId = null;
        if (StringUtils.isNotBlank(iteminst.getSeriesinstId())) {
            AeaHiSeriesinst seriesinst = aeaHiSeriesinstService.getAeaHiSeriesinstById(iteminst.getSeriesinstId());
            appinstId = seriesinst.getAppinstId();
            applyinstId = seriesinst.getApplyinstId();
        } else {
            AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstById(iteminst.getStageinstId());
            appinstId = stageinst.getAppinstId();
            applyinstId = stageinst.getApplyinstId();
        }

        String iteminstStatus = null;
        //判定是否为容缺办理
        if (iteminstState.contains(ItemStatus.valueOf("DEPARTMENT_DEAL_START").name()) && iteminstState.indexOf(":") > 0) {
            String state = iteminstState.substring(0, iteminstState.indexOf(":"));
            iteminstStatus = ItemStatus.valueOf(state).getValue();

            //更新为容缺受理
            aeaHiIteminstService.updateAeaHiIteminstIsToleranceAccept(iteminstId, AeaHiIteminstConstants.IS_TOLERANCE_ACCEPT_TRUE);
        } else {
            iteminstStatus = ItemStatus.valueOf(iteminstState).getValue();
        }
        if (iteminstStatus == null)
            throw new RuntimeException("事项实例状态转换为空，请检查页面传递状态码格式是否正确！");

        //String iteminstStatus = ItemStatus.valueOf(iteminstState).getValue();
        //更新事项状态及维护事项历史
        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminstId, sendObject.getTaskId(), appinstId, iteminstStatus, null);

        //触发流程发送事件
        Task currTask = bpmTaskService.getRuTaskByTaskId(sendObject.getTaskId());
        publisher.publishEvent(new BpmNodeSendAplanmisEvent(this, sendObject));
        this.changeProjPurchaseState(applyinstId, iteminstStatus, sendObject.getTaskId());
        //推动流程流转
        return bpmTaskService.completeTask(sendObject);
    }

    /**
     * 仅更改事项实例状态
     *
     * @param iteminstId    事项实例按钮
     * @param userOpinion   审批意见
     * @param opsActionCode 操作按钮编号
     * @param iteminstState 事项状态
     * @param taskId        任务ID（当需要挂起流程时，需要传参）
     * @throws Exception e
     */
    public void onlyChangeItemState(String iteminstId, String userOpinion, String opsActionCode, String iteminstState, String taskId) throws Exception {
        if (StringUtils.isBlank(iteminstId))
            throw new InvalidParameterException("事项实例ID为空！");
        if (StringUtils.isBlank(userOpinion))
            throw new InvalidParameterException("用户意见为空！");
        if (StringUtils.isBlank(iteminstState))
            throw new InvalidParameterException("事项状态为空！");
        if (StringUtils.isBlank(opsActionCode))
            throw new InvalidParameterException("操作按钮类型编号为空，按钮规则为：窗口流程W开头，事项I开头，加按钮拼音首大写字母，如窗口受理：WSL；如事项材料补正：ICLBZ！");

        String opsAction = OpsActionConstants.valueOf(opsActionCode).getName();
        String iteminstStatus = ItemStatus.valueOf(iteminstState).getValue();

        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminstId, userOpinion, opsAction, null, iteminstStatus, null);

        //开始补正或特别程序开始时，挂起流程
        if (iteminstStatus.equals(ItemStatus.CORRECT_MATERIAL_START.getValue())
                || iteminstStatus.equals(ItemStatus.SPECIFIC_PROC_START.getValue())) {
            if (StringUtils.isBlank(taskId))
                throw new InvalidParameterException("当前任务ID为空！");

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null)
                throw new RuntimeException("获取任务实例为空！");

            //挂起当前流程
            bpmProcessService.suspendProcessInstanceByIdAndTaskinstId(task.getProcessInstanceId(), taskId);
        }
    }


    /**
     * 中介事项更新采购项目状态
     *
     * @param applyinstId
     * @param iteminstState
     */
    private void changeProjPurchaseState(String applyinstId, String iteminstState, String taskId) {
        try {
            //事项状态
            String[] approve = {"11", "12"};
            String[] reject = {"4", "5", "13"};
            if (Arrays.binarySearch(approve, iteminstState) > -1 || Arrays.binarySearch(reject, iteminstState) > -1) {
                AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if (null == applyinst) return;
                String newState = "14";
                String action = "办结通过";
                if (Arrays.binarySearch(reject, iteminstState) > -1) {
                    newState = "15";
                    action = "办结不通过";
                }
                PurchaseProjVo purchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(applyinst.getApplyinstCode(), null);
                if (null == purchase) return;
                String proPurchaseId = purchase.getProjPurchaseId();
                aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(proPurchaseId, newState, null, SecurityContext.getCurrentUserId(), action, taskId);

            }
        } catch (Exception e) {
            log.info("更新采购项目状态失败");
            e.printStackTrace();
        }


    }
}
