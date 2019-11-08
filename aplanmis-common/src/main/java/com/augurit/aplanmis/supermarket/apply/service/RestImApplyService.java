package com.augurit.aplanmis.supermarket.apply.service;

import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import com.augurit.aplanmis.supermarket.apply.vo.ImServiceItemPurchaseVo;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-11-6
 * Description: 工建审批系统和中介超市发布采购需求，并启动流程相关接口
 */
@Service
@Transactional
public class RestImApplyService {

    @Autowired
    private RestTimeruleinstService restTimeruleinstService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;

    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AeaBpmProcessService aeaBpmProcessService;

    @Autowired
    private BpmProcessService bpmProcessService;

    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private BpmTaskService bpmTaskService;

    @Autowired
    AeaImBiddingPriceMapper aeaImBiddingPriceMapper;

    @Autowired
    AeaImAvoidUnitMapper aeaImAvoidUnitMapper;

    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;


    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;


    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;
    @Autowired
    AeaProjLinkmanMapper aeaProjLinkmanMapper;


    public String publishPurchaseAndStartProcess() throws Exception {

        String itemVerId = null;//事项版本ID

        String applySource = null;//申报来源

        String applySubject = null;//申报主体

        String ConstructionUnitId = null;// 建设单位ID

        String projInfoId = null;//如果是基于投资项目发布采购需求，则要传投资项目ID

        String purchasrProjInfoId = UUID.randomUUID().toString();//采购需求项目ID

        String isApproveProj = null;//是否为投资审批项目：1 是，0 否

        String applyLinkmanId = null;//申报主体为个人

        String linkmanInfoId = null;//个人联系人 或 单位联系人

        String branchOrgMap = null;// List<Map>, Map(itemVerId,orgId) 审批部门ID

        String[] matinstsIds = null;// 材料实例Ids

        String appId = null;// 流程模板ID

        String[] stateIds = null;// 情形Ids数组

        ImServiceItemPurchaseVo imServiceItemPurchaseVo = new ImServiceItemPurchaseVo();

        if (StringUtils.isBlank(imServiceItemPurchaseVo.getServiceItemId()))
            throw new Exception("缺少参数：ServiceItemId ");
        if (StringUtils.isBlank(imServiceItemPurchaseVo.getProjInfoId()))
            throw new Exception("缺少参数：ProjInfoId ");
        if (StringUtils.isBlank(imServiceItemPurchaseVo.getLinkmanInfoId()))
            throw new Exception("缺少参数：LinkmanInfoId ");
        if (StringUtils.isBlank(imServiceItemPurchaseVo.getBiddingType()))
            throw new Exception("缺少参数：BiddingType ");
        if (imServiceItemPurchaseVo.getChoiceImunitTime() == null)
            throw new Exception("缺少参数：ChoiceImunitTime ");
        if (imServiceItemPurchaseVo.getExpirationDate() == null)
            throw new Exception("缺少参数：ExpirationDate ");

        //初始化采购需求实体信息
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setProjPurchaseId(purchasrProjInfoId);
        aeaImProjPurchase.setProjInfoId(projInfoId);
        aeaImProjPurchase.setServiceItemId(imServiceItemPurchaseVo.getServiceItemId());// 服务和中介服务事项关联ID
        aeaImProjPurchase.setQuoteType("0");// 报价方式,0 金额 1 下浮率
        aeaImProjPurchase.setChoiceImunitTime(imServiceItemPurchaseVo.getChoiceImunitTime());// 选取中介时间
        aeaImProjPurchase.setExpirationDate(imServiceItemPurchaseVo.getExpirationDate());// 截止日期
        aeaImProjPurchase.setIsDefineAmount(imServiceItemPurchaseVo.getIsDefineAmount());// 是否确认金额，1 是 0 否
        aeaImProjPurchase.setSelectCondition(imServiceItemPurchaseVo.getSelectCondition());// 服务选择条件：1 多个服务具备其一，0 多个服务都具备
        aeaImProjPurchase.setOwnerComplaintPhone(imServiceItemPurchaseVo.getLinkmanMobilePhone());// 业主投诉电话
        aeaImProjPurchase.setIsDiscloseIm(imServiceItemPurchaseVo.getIsDiscloseIm());// 是否公示中选机构： 1 是， 0 否
        aeaImProjPurchase.setIsDiscloseBidding(imServiceItemPurchaseVo.getIsDiscloseBidding());// 是否公示中标公告：1 是， 0 否
        aeaImProjPurchase.setIsLiveWitness("0");// 是否现场见证：1 是， 0 否
        aeaImProjPurchase.setApplyinstCode(imServiceItemPurchaseVo.getApplyinstCode());// 关联的审批流水号
        aeaImProjPurchase.setIsApproveProj(isApproveProj);// 是否为投资审批项目：1 是，0 否
        aeaImProjPurchase.setContacts(imServiceItemPurchaseVo.getLinkmanName());// 业主联系人
        aeaImProjPurchase.setMoblie(imServiceItemPurchaseVo.getLinkmanMobilePhone());// 联系电话
        aeaImProjPurchase.setBiddingType(imServiceItemPurchaseVo.getBiddingType());// 竞价类型：1 随机中标，2 自主选择
        aeaImProjPurchase.setAuditFlag(AuditFlagStatus.WAIT_CHOOSE);// 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
        aeaImProjPurchase.setBasePrice(imServiceItemPurchaseVo.getBasePrice());// 最低价格（万元）
        aeaImProjPurchase.setHighestPrice(imServiceItemPurchaseVo.getHighestPrice());// 最高价格（万元）
        aeaImProjPurchase.setServiceContent(imServiceItemPurchaseVo.getServiceContent());// 服务内容
        aeaImProjPurchase.setLinkmanInfoId(linkmanInfoId);// 业主委托人信息ID
        aeaImProjPurchase.setIsDelete("0");
        aeaImProjPurchase.setIsActive("1");
        aeaImProjPurchase.setCreater(SecurityContext.getCurrentUserId());
        aeaImProjPurchase.setCreateTime(new Date());
        aeaImProjPurchase.setRootOrgId(SecurityContext.getCurrentOrgId());
        //申报主体为单位时
        if ("1".equals(applySubject)) {
            aeaImProjPurchase.setPublishUnitInfoId(ConstructionUnitId);// 业主单位ID
        }
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);

        //初始化采购需求历史状态信息
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
        aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        aeaImPurchaseinst.setNewPurchaseFlag(AuditFlagStatus.WAIT_CHOOSE);// 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
        aeaImPurchaseinst.setParentPurchaseinstId("root");
        aeaImPurchaseinst.setLinkmanInfoId(linkmanInfoId);
        aeaImPurchaseinst.setIsOwnFile("0");// 是否拥有附件
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

        if ("1".equals(isApproveProj)) {
            //创建投资项目和采购需求项目ID关联
            AeaParentProj aeaParentProj = new AeaParentProj();
            aeaParentProj.setNodeProjId(UUID.randomUUID().toString());
            aeaParentProj.setParentProjId(projInfoId);
            aeaParentProj.setChildProjId(purchasrProjInfoId);
            aeaParentProj.setProjSeq(projInfoId + "," + purchasrProjInfoId);
            aeaParentProj.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaParentProj.setCreater(SecurityContext.getCurrentUserId());
            aeaParentProj.setCreateTime(new Date());
            aeaParentProjMapper.insertAeaParentProj(aeaParentProj);
        }

        AeaItemBasic itemBasicByItemVerId = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applySource, applySubject, linkmanInfoId, "1", branchOrgMap, null, appinstId, ApplyState.IM_MILESTONE_CHOOSE_IMUNIT.getValue(), opuWinId);//实例化串联申请实例

        String seriesApplyinstId = seriesApplyinst.getApplyinstId();//申报实例ID

        seriesApplyinst.setProjInfoId(purchasrProjInfoId);

        //1、保存单项实例
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, appinstId, null, null);

        //2、事项实例
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, branchOrgMap, null, appinstId);


        //暂时把中介事项设置为不分情形 2019.11.7

        //把所有情形丢到变量里，用于流程启动情形
        /* if (stateIds != null && stateIds.length > 0) {
            Map<String, Boolean> stateinsts = new HashMap();
            for (String stateId : stateIds) {
                stateinsts.put(stateId, true);
            }
            if (stateinsts.size() > 0)
                seriesApplyinst.setStateinsts(stateinsts);
        }
        */

        //3、情形实例
        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());

        //4、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(matinstsIds, seriesApplyinstId, SecurityContext.getCurrentUserName());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(appId, appinstId, seriesApplyinst);

        if (processInstance == null || processInstance.getProcessInstance() == null) {
            throw new RuntimeException("流程启动失败！");
        }

        //新增时限规则实例
        restTimeruleinstService.createTimeruleinstByProcinst(itemBasicByItemVerId.getAppId(), processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());

        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();

        //挂起当前流程
        bpmProcessService.suspendProcessInstanceById(processInstance.getProcessInstance().getId());

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
        aeaApplyinstProj.setProjInfoId(purchasrProjInfoId);//4.0版本已废弃了多项目申报，所以只有一个值
        aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
        aeaApplyinstProj.setCreateTime(new Date());
        aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);

        //9、申报主体
        if ("0".equals(applySubject)) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(seriesApplyinstId, new String[]{purchasrProjInfoId}, applyLinkmanId, linkmanInfoId);
        } else {

            //建设单位
            Map<String, List<String>> puMap = new HashMap();
            List<String> unitInfoIds = new ArrayList();
            unitInfoIds.add(ConstructionUnitId);
            puMap.put(purchasrProjInfoId, unitInfoIds);
            aeaUnitInfoService.insertApplyOwnerUnitProj(seriesApplyinstId, puMap);//创建采购需求项目和建设单位关联

            //如果是投资项目，则需要创建投资项目和建设单位关联
            if ("1".equals(isApproveProj)) {

                AeaUnitProj unitProj = aeaUnitProjMapper.findUnitPorojByProjInfoIdAndUnitInfoId(projInfoId, ConstructionUnitId, AeaUnitConstants.IS_OWNER_TRUE);
                if (unitProj == null) {
                    unitProj = new AeaUnitProj();
                    unitProj.setUnitInfoId(UUID.randomUUID().toString());
                    unitProj.setIsOwner(AeaUnitConstants.IS_OWNER_TRUE);
                    unitProj.setIsDeleted("0");
                    unitProj.setProjInfoId(projInfoId);
                    unitProj.setUnitInfoId(ConstructionUnitId);
                    unitProj.setUnitType("1");// 1 建设单位
                    unitProj.setCreater(SecurityContext.getCurrentUserId());
                    unitProj.setCreateTime(new Date());
                    aeaUnitProjMapper.insertAeaUnitProj(unitProj);
                }
            }
        }

        return seriesApplyinstId;
    }

    /**
     * 选取中介机构
     *
     * @throws Exception
     */
    public void chooseImunit() throws Exception {

        ApplyState.IM_MILESTONE_CONFIRM_IMUNIT.getValue();// 待中介机构确认

    }

    /**
     * 中介机构确认
     *
     * @throws Exception
     */
    public void confirmImunit() throws Exception {


        ApplyState.IM_MILESTONE_CHOOSE_IMUNIT.getValue();// 重新选取中介机构


        ApplyState.IM_MILESTONE_UPLOAD_CONTRACT.getValue();// 待上传合同

    }

    /**
     * 上传合同
     *
     * @throws Exception
     */
    public void uploadContract() throws Exception {

        ApplyState.IM_MILESTONE_CONFIRM_CONTRACT.getValue();// 待确认合同

    }

    /**
     * 确认合同
     *
     * @throws Exception
     */
    public void confirmContract() throws Exception {

        String confirmFlag = null;// 1：已确认合同有效，0：已确认合同无效

        String purchaseId = null;// 采购需求ID

        String applyinstId = null;// 申请实例ID

        // TODO 业务操作


        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID

        if ("1".equals(confirmFlag)) {

            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_UPLOAD_SERVICE_RESULT.getValue(), null);// 待上传服务结果

            //更新采购需求状态

            //更新采购需求实例历史状态

//            AuditFlagStatus.SERVICE_PROGRESS;// 服务中

        } else {
            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_UPLOAD_CONTRACT.getValue(), null);// 重新上传合同
        }

    }

    /**
     * 上传服务结果电子件或窗口收取纸质件
     *
     * @throws Exception
     */
    public void uploadServiceResult() throws Exception {

        String applyinstId = null;// 申请实例ID

        String purchaseId = null;// 采购需求ID

        String message = null;// 如果在窗口上传电子件和收纸质件，则需要填写意见

        // TODO 业务操作

        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID

        List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (iteminsts.size() < 1) throw new Exception("找不到事项实例信息！");
        AeaHiIteminst iteminst = iteminsts.get(0);

        //更新事项实例历史状态
        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), taskId, appinstId, ItemStatus.ACCEPT_DEAL.getValue(), SecurityContext.getCurrentOrgId());

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        //更新申请实例历史状态
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);// 已接件并审核

        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.ACCEPT_DEAL.getValue(), opuWinId);// 已受理

        //更新采购需求状态
        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(purchaseId);
        aeaImProjPurchase.setAuditFlag(AuditFlagStatus.SERVICE_FINISH);// 服务完成
        aeaImProjPurchase.setModifier(SecurityContext.getCurrentUserId());
        aeaImProjPurchase.setModifyTime(new Date());
        aeaImProjPurchaseMapper.updateAeaImProjPurchase(aeaImProjPurchase);

        //更新采购需求实例历史状态
        AeaImPurchaseinst oldAeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstByProjPurchaseId(purchaseId);

        AeaImPurchaseinst newAeaImPurchaseinst = new AeaImPurchaseinst();
        newAeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
        newAeaImPurchaseinst.setParentPurchaseinstId(oldAeaImPurchaseinst.getPurchaseinstId());
//        newAeaImPurchaseinst.
        newAeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
        newAeaImPurchaseinst.setCreateTime(new Date());
        newAeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(newAeaImPurchaseinst);

        //推动流程流转
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (bpmProcessService.isProcessSuspended(task.getProcessInstanceId())) {
            bpmProcessService.activateProcessInstanceById(task.getProcessInstanceId());//激活当前流程
        }
        bpmTaskService.addTaskComment(taskId, task.getProcessInstanceId(), StringUtils.isBlank(message) ? "" : message);//收件意见
        taskService.complete(taskId, (Map) null);
    }

    /**
     * 获取最后一条申请实例历史记录
     *
     * @return
     * @throws Exception
     */
    private AeaLogApplyStateHist getLastAeaLogApplyStateHist(String applyinstId) throws Exception {

        if (StringUtils.isBlank(applyinstId)) return new AeaLogApplyStateHist();

        //获取申请实例历史记录列表
        AeaLogApplyStateHist aeaLogApplyStateHist = new AeaLogApplyStateHist();
        aeaLogApplyStateHist.setApplyinstId(applyinstId);
        aeaLogApplyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLogApplyStateHist> aeaLogApplyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(aeaLogApplyStateHist);

        if (aeaLogApplyStateHists.size() < 1) return new AeaLogApplyStateHist();

        return aeaLogApplyStateHists.get(0);
    }


    /**
     * 唐山模式---发起申报，挂起流程，选取中介后 流程流转到部门审批
     *
     * @param applyData
     */
    public ApplyinstResult purchaseStartProcess(ImItemApplyData applyData) throws Exception {
        ApplyinstResult result = new ApplyinstResult();
        String itemVerId = applyData.getItemVerId();
        AeaItemBasic itemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID
//        result.setAppinstId(appinstId);
//        String branchOrgMap = applyData.getBranchOrgMap();
        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applyData.getApplySource(),
                applyData.getApplySubject(), applyData.getLinkmanInfoId(), "1", null, null, appinstId, ApplyState.IM_MILESTONE_CHOOSE_IMUNIT.getValue(), opuWinId);//实例化串联申请实例
        result.setApplyinstId(seriesApplyinst.getApplyinstId());
        result.setApplyinstCode(seriesApplyinst.getApplyinstCode());

        String applyinstId = seriesApplyinst.getApplyinstId();//申报实例ID
        seriesApplyinst.setProjInfoId(applyData.getProjInfoId());

        //1、保存单项实例
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(applyinstId, appinstId, null, null);

        //2、事项实例
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, null, null, appinstId);


        //暂时把中介事项设置为不分情形 2019.11.7

        //把所有情形丢到变量里，用于流程启动情形
        /* if (stateIds != null && stateIds.length > 0) {
            Map<String, Boolean> stateinsts = new HashMap();
            for (String stateId : stateIds) {
                stateinsts.put(stateId, true);
            }
            if (stateinsts.size() > 0)
                seriesApplyinst.setStateinsts(stateinsts);
        }
        */

        //3、情形实例---中介事项默认不分情形，不需要实例化
//        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());

        //4、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(applyData.getMatinstsIds(), applyinstId, SecurityContext.getCurrentUserName());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(itemBasic.getAppId(), appinstId, seriesApplyinst);

        if (processInstance == null || processInstance.getProcessInstance() == null) {
            throw new RuntimeException("流程启动失败！");
        }

        //新增时限规则实例
        restTimeruleinstService.createTimeruleinstByProcinst(itemBasic.getAppId(), processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());

        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();

        //挂起当前流程
        bpmProcessService.suspendProcessInstanceById(processInstance.getProcessInstance().getId());

        //6.流程发起后，更新初始事项历史的taskId
        AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), appinstId);
        logItemStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

        //流程发起后，更新初始申请历史的taskId
        AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(applyinstId, appinstId);
        applyStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);

        //8.保存申请实例与项目之间的关系 aea_applyinst_proj
        AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
        aeaApplyinstProj.setApplyinstId(seriesApplyinst.getApplyinstId());
        aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
        aeaApplyinstProj.setProjInfoId(applyData.getProjInfoId());//4.0版本已废弃了多项目申报，所以只有一个值
        aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
        aeaApplyinstProj.setCreateTime(new Date());
        aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);


        //9、申报主体
        if ("0".equals(applyData.getApplySubject())) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(applyinstId, new String[]{applyData.getProjInfoId()}, applyData.getApplyLinkmanId(), applyData.getLinkmanInfoId());
        } else {

            //建设单位
            Map<String, List<String>> puMap = new HashMap();
            List<String> unitInfoIds = new ArrayList();
            unitInfoIds.add(applyData.getConstructionUnitId());
            puMap.put(applyData.getProjInfoId(), unitInfoIds);
            aeaUnitInfoService.insertApplyOwnerUnitProj(applyinstId, puMap);//创建采购需求项目和建设单位关联

            //如果是投资项目，则需要创建投资项目和建设单位关联
            if ("1".equals(applyData.getIsApproveProj())) {

                AeaUnitProj unitProj = aeaUnitProjMapper.findUnitPorojByProjInfoIdAndUnitInfoId(applyData.getProjInfoId(), applyData.getConstructionUnitId(), AeaUnitConstants.IS_OWNER_TRUE);
                if (unitProj == null) {
                    unitProj = new AeaUnitProj();
                    unitProj.setUnitInfoId(UUID.randomUUID().toString());
                    unitProj.setIsOwner(AeaUnitConstants.IS_OWNER_TRUE);
                    unitProj.setIsDeleted("0");
                    unitProj.setProjInfoId(applyData.getProjInfoId());
                    unitProj.setUnitInfoId(applyData.getConstructionUnitId());
                    unitProj.setUnitType("1");// 1 建设单位
                    unitProj.setCreater(SecurityContext.getCurrentUserId());
                    unitProj.setCreateTime(new Date());
                    aeaUnitProjMapper.insertAeaUnitProj(unitProj);
                }
            }
        }
        return result;
    }

    /**
     * 保存采购项目信息
     *
     * @param data
     * @throws Exception
     */
    public AeaImProjPurchase savePurchaseProjInfo(ImPurchaseData data) throws Exception {
        //参数校验
        this.validParams(data);
        String unitInfoId = data.getPublishUnitInfoId();
        String applySubject = data.getApplySubject();
        //初始化采购需求实体信息
        AeaImProjPurchase aeaImProjPurchase = data.createAeaImProjPurchase();

        //保存机构要求
        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();
        aeaImUnitRequire.setUnitRequireId(UuidUtil.generateUuid());
        aeaImUnitRequire.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImUnitRequireMapper.insertAeaImUnitRequire(aeaImUnitRequire);

        aeaImProjPurchase.setUnitRequireId(aeaImUnitRequire.getUnitRequireId());

        //保存资质专业要求
        List<AeaImMajorQual> aeaImMajorQuals = data.getAeaImMajorQuals();
        if (aeaImMajorQuals != null && "1".equals(aeaImUnitRequire.getIsQualRequire())) {
            for (AeaImMajorQual aeaImMajorQual : aeaImMajorQuals) {
                aeaImMajorQual.buildImMajor(aeaImUnitRequire.getUnitRequireId(), data.getCreater());
                aeaImMajorQualMapper.insertAeaImMajorQual(aeaImMajorQual);
            }
        }
        //保存采购项目
        AeaProjInfo aeaProjInfo = data.createProjInfo(data);
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
        aeaImProjPurchase.setProjInfoId(aeaProjInfo.getProjInfoId());

        if ("0".equals(data.getApplySubject())) {
            aeaImProjPurchase.setPublishLinkmanInfoId(data.getPublishLinkmanInfoId());
        } else {
            aeaImProjPurchase.setPublishUnitInfoId(unitInfoId);
        }

        //保存采购项目信息表
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);
        this.savePurchaseinst(data);

        //创建投资项目和采购需求项目ID关联
        if ("1".equals(data.getIsApproveProj())) {
            String parentProjId = data.getProjInfoId();
            String childProjId = aeaProjInfo.getProjInfoId();
            String projSeq = data.getProjInfoId() + "," + data.getProjPurchaseId();
            AeaParentProj aeaParentProj = new AeaParentProj(parentProjId, childProjId, projSeq, data.getCreater(), data.getRootOrgId());
            aeaParentProjMapper.insertAeaParentProj(aeaParentProj);
        }
        //保存采购项目与联系人关联
        String linkmanInfoId = data.getLinkmanInfoId();
        if (StringUtils.isBlank(linkmanInfoId)) {
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.findAllUnitLinkman(unitInfoId);

            for (AeaLinkmanInfo linkmanInfo : aeaLinkmanInfos) {
                if (linkmanInfo.getLinkmanType() != null && linkmanInfo.getLinkmanType().contains("u")) {
                    linkmanInfoId = linkmanInfo.getLinkmanInfoId();
                    break;
                }
            }
        }
        //保存项目联系人关系
        if (StringUtils.isNotBlank(linkmanInfoId)) {
            AeaProjLinkman aeaProjLinkman = new AeaProjLinkman(aeaProjInfo.getProjInfoId(), linkmanInfoId, "link", data.getApplyinstId(), data.getCreater());
            aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
        }


        //保存企业报价
        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(aeaImProjPurchase.getBiddingType())) {
            if (StringUtils.isNotBlank(data.getAgentUnitInfoId())) {
                saveImUnitBidding(data, aeaImProjPurchase.getProjPurchaseId());
            }
        }

        // 保存回避单位
        if (Status.ON.equals(data.getIsAvoid())) {
            String avoidUnitInfoIds = data.getAvoidUnitInfoIds();
            for (String avoidUnitInfoId : avoidUnitInfoIds.split(",")) {
                AeaImAvoidUnit aeaImAvoidUnit = new AeaImAvoidUnit();
                aeaImAvoidUnit.setAvoidUnitId(UuidUtil.generateUuid());
                aeaImAvoidUnit.setUnitInfoId(avoidUnitInfoId);
                aeaImAvoidUnit.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
                aeaImAvoidUnit.setCreater(SecurityContext.getCurrentUserName());
                aeaImAvoidUnit.setCreateTime(new Date());
                aeaImAvoidUnit.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImAvoidUnitMapper.insertAeaImAvoidUnit(aeaImAvoidUnit);
            }
        }


        return aeaImProjPurchase;
    }

    /**
     * 保存采购实例信息
     *
     * @param purchaseData
     * @throws Exception
     */
    public void savePurchaseinst(ImPurchaseData purchaseData) throws Exception {
        //初始化采购需求历史状态信息
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.buildImPurchaseinst(purchaseData.getProjPurchaseId(), AuditFlagStatus.WAIT_CHOOSE, "root", purchaseData.getLinkmanInfoId(), "0", purchaseData.getCreater(), purchaseData.getRootOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
    }

    private void saveImUnitBidding(ImPurchaseData data, String projPurchaseId) throws Exception {
        AeaUnitInfo agentUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(data.getAgentUnitInfoId());

        if (agentUnitInfo != null) {

            AeaImUnitService aeaImUnitService = aeaImUnitServiceMapper.getUnitServiceByUnitInfoIdAndServiceItemId(agentUnitInfo.getUnitInfoId(), data.getServiceItemId());

            if (aeaImUnitService != null) {
                AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
                aeaImUnitBidding.buildImUnitBidding(projPurchaseId, data.getAgentUnitInfoId(), aeaImUnitService.getUnitServiceId(), data.getCreater(), data.getRootOrgId());
                // 查询已绑定联系人
                List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(data.getAgentUnitInfoId(), "1", "1", "");
                if (aeaLinkmanInfos != null && aeaLinkmanInfos.size() > 0) {
                    AeaLinkmanInfo linkmanInfo = aeaLinkmanInfos.get(0);
                    aeaImUnitBidding.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                }
                aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);

                // 保存竞价价格
                AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice(aeaImUnitBidding.getUnitBiddingId(), data.getBasePrice(), "1", "0", data.getCreater(), data.getRootOrgId());
                aeaImBiddingPriceMapper.insertAeaImBiddingPrice(aeaImBiddingPrice);
            }
        }
    }

    private void validParams(ImPurchaseData data) throws Exception {
        if (StringUtils.isBlank(data.getServiceItemId()))
            throw new Exception("缺少参数：ServiceItemId ");
        if (StringUtils.isBlank(data.getLinkmanInfoId()))
            throw new Exception("缺少参数：LinkmanInfoId ");
        if (StringUtils.isBlank(data.getBiddingType()))
            throw new Exception("缺少参数：BiddingType ");
        if (data.getChoiceImunitTime() == null)
            throw new Exception("缺少参数：ChoiceImunitTime ");
        if (data.getExpirationDate() == null)
            throw new Exception("缺少参数：ExpirationDate ");

        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }
        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(data.getProjName());
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (!aeaProjInfoCondList.isEmpty()) {
            throw new RuntimeException("项目名称已存在");
        }
    }
}
