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
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;

    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;

    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;
    @Autowired
    private AeaImContractMapper aeaImContractMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaImServiceResultMapper aeaImServiceResultMapper;


    /**
     * 选取中介机构
     * <p>
     * purchaseId = null;// 采购需求ID
     * unitInfoId 选取的中介竞标ID
     *
     * @throws Exception E
     */
    public void chooseImunit(String projPurchaseId, String unitBiddingId) throws Exception {

        AeaImProjPurchase purchase = this.getApplyinstIdByProPurchaseId(projPurchaseId);
        String applyinstId = purchase.getApplyinstId();
        String opsLinkInfoId = purchase.getLinkmanInfoId();//业主委托人
        String isWonBid = "1";
        //更新中标状态
        aeaImUnitBiddingMapper.updateWinBid(unitBiddingId, projPurchaseId, isWonBid);
        //选中中标企业最高价
        AeaImBiddingPrice biddingPrice = aeaImBiddingPriceMapper.getBiddingPrice(projPurchaseId, "2");
        biddingPrice.setIsChoice("1");
        aeaImBiddingPriceMapper.updateAeaImBiddingPrice(biddingPrice);

        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID

        //更新申请实例历史状态
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_CONFIRM_IMUNIT.getValue(), null);// 待中介机构确认

        //更新采购需求状态
        aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.CHOOSE_END, null, opsLinkInfoId, null, taskId);//已选取，待确认

    }

    /**
     * 中介机构确认
     *
     * @param projPurchaseId 项目采购ID
     * @param unitBiddingId  单位竞价ID
     * @param confirmFlag    1：已确认中标，0：已放弃中标
     * @throws Exception
     */
    public void confirmImunit(String projPurchaseId, String unitBiddingId, String confirmFlag) throws Exception {

        AeaImProjPurchase purchase = this.getApplyinstIdByProPurchaseId(projPurchaseId);
        String opsLinkInfoId = purchase.getLinkmanInfoId();//业主委托人
        String applyinstId = purchase.getApplyinstId();
        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID
        //放弃中标
        if ("0".equals(confirmFlag)) {
            //修改单位中标状态，并删除，不在参与竞标
            aeaImUnitBiddingMapper.abortWinBid(unitBiddingId, projPurchaseId);
            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_CHOOSE_IMUNIT.getValue(), null);// 重新选取中介机构
            //更新采购需求状态
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.WAIT_CHOOSE, null, opsLinkInfoId, null, taskId);//待选取
        } else {
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_UPLOAD_CONTRACT.getValue(), null);// 待上传合同
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.UPLOAD_CONTRACT, null, opsLinkInfoId, null, taskId);//待上传合同
        }


    }

    /**
     * 上传合同
     *
     * @param aeaImContract 采购需求ID
     * @param opsLinkInfoId 业主委托人
     * @throws Exception
     */
    public void uploadContract(AeaImContract aeaImContract, String opsLinkInfoId, HttpServletRequest request) throws Exception {
        String projPurchaseId = aeaImContract.getProjPurchaseId();

        //保存或合同信息
        if (StringUtils.isNotBlank(aeaImContract.getContractId())) {
            aeaImContract.setModifyTime(new Date());
            aeaImContract.setModifier(SecurityContext.getCurrentUserName());
            aeaImContractMapper.updateAeaImContract(aeaImContract);
        } else {
            //先判断是否已经存在合同信息
            AeaImContract contract = aeaImContractMapper.getAeaImContractByProjPurchaseId(projPurchaseId);
            if (null != contract) throw new Exception("合同已存在");

            aeaImContract.setContractId(UUID.randomUUID().toString());
            aeaImContract.setCreateTime(new Date());
            aeaImContractMapper.insertAeaImContract(aeaImContract);
            //跟新报价表为已上传合同
            aeaImUnitBiddingMapper.updateUploadContract(aeaImContract.getUnitBiddingId(), "1");

            //获取申请实例历史记录列表
            AeaImProjPurchase purchase = this.getApplyinstIdByProPurchaseId(projPurchaseId);
            String applyinstId = purchase.getApplyinstId();
            AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
            if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
            String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
            String appinstId = applyStateHist.getAppinstId();// 模板实例ID

            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_CONFIRM_CONTRACT.getValue(), null);// 待确认合同

            //更新采购需求状态
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.CONFIRM_CONTRACT, null, opsLinkInfoId, null, taskId);// 待确认合同

        }
        //上传合同附件
        fileUtilsService.uploadAttachments("AEA_IM_CONTRACT", "CONTRACT_ID", aeaImContract.getContractId(), null, request);

    }

    /**
     * 确认合同
     *
     * @param contract               合同实体
     * @param confirmFlag            1：已确认合同有效，0：已确认合同无效
     * @param auditOpinion           审核意见
     * @param postponeServiceEndTime 延期时间 当confirmFlag==0
     * @throws Exception
     */
    public void confirmContract(AeaImContract contract, String confirmFlag, String auditOpinion, Date postponeServiceEndTime) throws Exception {
        if (null == contract || StringUtils.isBlank(contract.getContractId())) throw new Exception("contract is null");
        String projPurchaseId = contract.getProjPurchaseId();
        AeaImProjPurchase purchase = this.getApplyinstIdByProPurchaseId(projPurchaseId);
        String applyinstId = purchase.getApplyinstId();
        String opsLinkInfoId = purchase.getLinkmanInfoId();
        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID
        //更新合同信息
        this.updateContract(contract, confirmFlag, auditOpinion, postponeServiceEndTime);
        if (StringUtils.isBlank(confirmFlag) || "1".equals(confirmFlag)) {
            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_UPLOAD_SERVICE_RESULT.getValue(), null);// 待上传服务结果
            //更新采购需求状态
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.SERVICE_PROGRESS, null, opsLinkInfoId, null, taskId);//服务中
        } else {//合同无效
            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.IM_MILESTONE_CONFIRM_CONTRACT.getValue(), null);// 重新上传合同
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.CONFIRM_CONTRACT, null, opsLinkInfoId, null, taskId);//待上传合同
        }

    }

    /**
     * 上传服务结果电子件或窗口收取纸质件
     * 纸质件只能审批系统收取
     *
     * @param projPurchaseId 采购需求ID
     * @param message        如果在窗口上传电子件和收纸质件，则需要填写意见
     * @throws Exception
     */
    public void uploadServiceResult(String projPurchaseId, String message, String iteminstId) throws Exception {

        AeaImProjPurchase purchase = this.getApplyinstIdByProPurchaseId(projPurchaseId);
        String applyinstId = purchase.getApplyinstId();
        String opsLinkInfoId = purchase.getLinkmanInfoId();// 如果是由窗口人员操作，此ID为空
        //获取申请实例历史记录列表
        AeaLogApplyStateHist applyStateHist = this.getLastAeaLogApplyStateHist(applyinstId);
        if (applyStateHist == null) throw new Exception("找不到申请实例历史记录！");
        String taskId = applyStateHist.getTaskinstId();// 节点ID（此时还停留在《发布采购需求》节点）
        String appinstId = applyStateHist.getAppinstId();// 模板实例ID

        String creater = SecurityContext.getCurrentUserName();
        String rootOrgId = "";

        if (StringUtils.isBlank(iteminstId)) {
            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if (iteminsts.size() < 1) throw new Exception("找不到事项实例信息！");
            AeaHiIteminst iteminst = iteminsts.get(0);
            iteminstId = iteminst.getIteminstId();
            rootOrgId = iteminst.getRootOrgId();
        } else {
            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            if (null == iteminst) throw new Exception("找不到事项实例信息！");
            rootOrgId = iteminst.getRootOrgId();
        }

        //查询所有材料定义
        List<MatinstVo> matinstVos = aeaImProjPurchaseService.listPurchaseMatinst(iteminstId, applyinstId);
        boolean requiredPaper = false;
        for (MatinstVo vo : matinstVos) {
            String attIsRequire = vo.getAttIsRequire();
            String paperIsRequire = vo.getPaperIsRequire();
            Long dueCopyCount = vo.getDueCopyCount();
            Long duePaperCount = vo.getDuePaperCount();
            Long realCopyCount = vo.getRealCopyCount() == null ? 0L : vo.getRealCopyCount();
            Long realPaperCount = vo.getRealPaperCount() == null ? 0L : vo.getRealPaperCount();
            Long attCount = vo.getAttCount();
            boolean attFlag = false;
            boolean paperFlag = false;
            if (StringUtils.isNotBlank(attIsRequire) && "1".equals(attIsRequire)) {
                attFlag = attCount == 0;
            }
            if (StringUtils.isNotBlank(paperIsRequire) && "1".equals(paperIsRequire)) {
                paperFlag = (realPaperCount < duePaperCount || realCopyCount < dueCopyCount);
            }

            if (attFlag || paperFlag) {
                requiredPaper = true;
                break;
            }
        }

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        //更新竞价表，已上传服务结果 =1
        aeaImUnitBiddingMapper.updateUploadResult(projPurchaseId, "1");
        //aea_im_service_result 插入数据或修改数据
        if (requiredPaper) {
            //需要纸质件
            //保持当前流程为挂起状态，等待窗口收齐纸质件在发起流程

            this.insertOrUpdateServiceResult(projPurchaseId, "0", creater, rootOrgId);
            //更新申请实例历史状态
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), opuWinId);// 已接件并审核

            //如果中介机构或窗口人员上传了服务结果，则进入服务已完成
            aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.SERVICE_FINISH, null, opsLinkInfoId, null, taskId);//服务已完成
            return;
        }
        //收齐资料，执行


        this.insertOrUpdateServiceResult(projPurchaseId, "1", creater, rootOrgId);

        //更新事项实例历史状态
        aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminstId, taskId, appinstId, ItemStatus.ACCEPT_DEAL.getValue(), rootOrgId);

        //更新申请实例历史状态
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, ApplyState.ACCEPT_DEAL.getValue(), opuWinId);// 已受理

        //如果服务结果和纸质已收齐，则流转下一节点，进入部门审批中。
        aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, AuditFlagStatus.DEPARTMENT_APPROVAL, null, opsLinkInfoId, null, taskId);//部门审批中
        //推动流程流转
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) throw new Exception("找不到节点信息！");
        if (bpmProcessService.isProcessSuspended(task.getProcessInstanceId())) {
            bpmProcessService.activateProcessInstanceById(task.getProcessInstanceId());//激活当前流程
        }
        bpmTaskService.addTaskComment(taskId, task.getProcessInstanceId(), StringUtils.isBlank(message) ? "" : message);//收件意见
        taskService.complete(taskId, new String[]{"zhuanjiapingshen"}, (Map) null);
    }

    /**
     * 保存材料输入输出实例
     *
     * @param matinstIds 材料实例ID
     * @param itemVerId  事项版本ID
     * @param iteminstId 事项实例ID
     * @param creater    创建人
     * @return Set<String>
     * @throws Exception e
     */
    private String[] saveItemInoutinst(String[] matinstIds, String itemVerId, String iteminstId, String creater) throws Exception {
        Set<String> matIds = new HashSet<>();
        // 保存材料实例---不分情形
        for (String matinstId : matinstIds) {
            AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            if (null == matinst) continue;
            String matId = matinst.getMatId();
            matIds.add(matId);
            List<AeaItemInout> itemInouts = aeaItemInoutMapper.getAeaItemMatByItemVerIdAndMatIdAndStateId(itemVerId, matId, null);
            if (itemInouts.isEmpty()) continue;
            AeaItemInout inout = itemInouts.get(0);
            //保存材料实例---不分情形,重复材料不保存
            String inoutId = inout.getInoutId();
            AeaHiItemInoutinst inoutinst = new AeaHiItemInoutinst();
            inoutinst.buildInoutInst(iteminstId, inoutId, matinstId, creater, inout.getRootOrgId());
            aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(inoutinst);
        }
        return matIds.toArray(new String[matIds.size()]);
    }

    /**
     * 保存或更新服务结果 aea_im_service_result
     *
     * @param projPurchaseId 采购项目ID
     * @param auditFlag      服务结果状态：0 待确定，1 已确定 ，2 已退回
     * @param creater        创建人
     * @param rootOrgId      rootOrgId
     * @return AeaImServiceResult.class
     * @throws Exception e
     */
    private AeaImServiceResult insertOrUpdateServiceResult(String projPurchaseId, String auditFlag, String creater, String rootOrgId) throws Exception {
        //获取中标单位竞价ID
        List<AeaImUnitBidding> unitBiddings = aeaImUnitBiddingMapper.getUnitBiddingByProjPurchase(projPurchaseId);
        if (unitBiddings.isEmpty()) throw new Exception("can not find unitbidding");
        AeaImUnitBidding unitBidding = unitBiddings.get(0);
        String unitBiddingId = unitBidding.getUnitBiddingId();
        //先查询当前采购下是否存在服务结果，如果存在，则修改，不存在，add
        List<AeaImServiceResult> results = aeaImServiceResultMapper.listServiceResultByProjPurchaseId(projPurchaseId);
        if (results.isEmpty()) {
            AeaImServiceResult serviceResult = new AeaImServiceResult();
            serviceResult.createResult(projPurchaseId, unitBiddingId, auditFlag, creater, rootOrgId);
            aeaImServiceResultMapper.insertAeaImServiceResult(serviceResult);
            return serviceResult;
        } else {
            AeaImServiceResult serviceResult = results.get(0);
            serviceResult.buildModifyResult(auditFlag, creater);
            aeaImServiceResultMapper.updateAeaImServiceResult(serviceResult);
            return serviceResult;
        }
    }

    /**
     * 更新合同信息
     *
     * @param contract
     * @param confirmFlag
     * @param auditOpinion
     * @param postponeServiceEndTime
     * @throws Exception
     */
    private void updateContract(AeaImContract contract, String confirmFlag, String auditOpinion, Date postponeServiceEndTime) throws Exception {
        contract.setAuditTime(new Date());
        contract.setModifyTime(new Date());
        contract.setAuditOpinion(auditOpinion);
        if ("1".equals(confirmFlag)) {
            contract.setAuditFlag("1");
            contract.setIsConfirm("1");
        } else {//合同无效
            //更新合同审核信息
            contract.setAuditFlag("2");
            contract.setIsConfirm("0");
            contract.setPostponeServiceEndTime(postponeServiceEndTime);
        }
        aeaImContractMapper.updateAeaImContract(contract);
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
        AeaItemBasic itemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, applyData.getRootOrgId());

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID
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
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(applyData.getMatinstsIds(), applyinstId, applyData.getCreater());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(itemBasic.getAppId(), appinstId, seriesApplyinst, "system");

        if (processInstance == null || processInstance.getProcessInstance() == null) {
            throw new RuntimeException("流程启动失败！");
        }

        //新增时限规则实例
//        restTimeruleinstService.createTimeruleinstByProcinst(itemBasic.getAppId(), processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());

        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();
        //保存意见
        if (tasks.isEmpty()) throw new Exception("找不到流程的第一个节点，请检查配置");
        Task task = tasks.get(0);
        String message = applyData.getComments();
        bpmTaskService.addTaskComment(task.getId(), task.getProcessInstanceId(), StringUtils.isBlank(message) ? "" : message);//收件意见

        //挂起当前流程，只有上传了服务结果在根据流程来启动
        bpmProcessService.suspendProcessInstanceById(processInstance.getProcessInstance().getId());

        //判断申报来源，如果是中介超市，则推送到预审节点，
       /* String applySource = applyData.getApplySource();
        if (StringUtils.isNotBlank(applySource) && "net".equals(applySource)) {
            taskService.complete(task.getId());
        } else {
            //如果是审批系统，则挂起当前流程，只有上传了服务结果在根据流程来启动
            bpmProcessService.suspendProcessInstanceById(processInstance.getProcessInstance().getId());
        }*/


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

        //初始化采购需求实体信息
        AeaImProjPurchase aeaImProjPurchase = data.createAeaImProjPurchase();
        //保存机构要求
        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();
        aeaImUnitRequire.setUnitRequireId(UuidUtil.generateUuid());
        aeaImUnitRequire.setRootOrgId(data.getRootOrgId());
        if (StringUtils.isNotBlank(aeaImUnitRequire.getPromiseService())) {
            if (aeaImUnitRequire.getPromiseService().equals("true")) {

                aeaImUnitRequire.setPromiseService("1");
            } else if (aeaImUnitRequire.getPromiseService().equals("false")) {
                aeaImUnitRequire.setPromiseService("0");

            }
        }
        //fixme 先设置不需要资质
        aeaImUnitRequire.setIsQualRequire("0");
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
        if ("0".equals(data.getApplySubject())) {
            aeaImProjPurchase.setPublishLinkmanInfoId(data.getPublishLinkmanInfoId());
        } else {
            aeaImProjPurchase.setPublishUnitInfoId(unitInfoId);
        }

        //保存采购项目信息表
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);
        data.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        this.savePurchaseinst(data);

        //创建投资项目和采购需求项目ID关联
        if ("1".equals(data.getIsApproveProj())) {
            String parentProjId = data.getApproveProjInfoId();
            String childProjId = data.getProjInfoId();
            String projSeq = data.getApproveProjInfoId() + "," + data.getProjInfoId();
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
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman(data.getProjInfoId(), linkmanInfoId, "link", data.getApplyinstId(), data.getCreater());
        aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);

        //保存企业报价---todo 如果是直接选取，需要生成竞价信息及中标事项，修改事项转态
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
                aeaImAvoidUnit.setCreater(data.getCreater());
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

    }

    /**
     * 根据采购ID获取申报实例id
     *
     * @param projPurchaseId 采购ID
     * @return applyinstId 申请实例ID
     */
    private AeaImProjPurchase getApplyinstIdByProPurchaseId(String projPurchaseId) throws Exception {
        AeaImProjPurchase purchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
        if (null == purchase) throw new Exception("找不到采购项目信息");
        String applyinstCode = purchase.getApplyinstCode();
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(applyinstCode);
        if (null == applyinst) throw new Exception("找不到申报信息");
        purchase.setApplyinstId(applyinst.getApplyinstId());
        return purchase;
    }
}
