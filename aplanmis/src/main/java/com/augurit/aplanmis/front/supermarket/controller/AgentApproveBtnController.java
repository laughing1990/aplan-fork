package com.augurit.aplanmis.front.supermarket.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.constants.OpsActionConstants;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSeriesinstMapper;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import com.augurit.aplanmis.front.approve.service.ApproveItemBtnService;
import com.augurit.aplanmis.front.approve.service.ApproveWinBtnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RequestMapping("/market/approve/btn")
@RestController
@Api(value = "中介事项流程审批按钮", tags = "中介事项审批详情页-审批按钮")
@Slf4j
public class AgentApproveBtnController {

    @Autowired
    private ApproveItemBtnService approveItemBtnService;
    @Autowired
    private ApproveWinBtnService approveWinBtnService;
    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;


    @PutMapping("/win/wfSendAndChangeApplyState")
    @ApiOperation(value = "窗口流程：流程审批并更改申请状态按钮方法（使用于既要推动流程流转，也需要修改申报状态时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "sendObjectStr", value = "流程节点实体字符串", dataType = "string", required = true),
            @ApiImplicitParam(name = "applyState", value = "申请状态", dataTypeClass = ApplyState.class, required = true)})
    public ResultForm wfSendAndChangeApplyState(String sendObjectStr, String applyinstId, String applyState) {
        if (StringUtils.isBlank(sendObjectStr))
            throw new InvalidParameterException("流程发送参数为空！");
        if (StringUtils.isBlank(applyinstId))
            throw new InvalidParameterException("申请实例ID为空！");
        if (StringUtils.isBlank(applyState))
            throw new InvalidParameterException("申请状态为空！");

        BpmTaskSendObject sendObject = (BpmTaskSendObject) JSONObject.parseObject(sendObjectStr, BpmTaskSendObject.class);
        try {
            String nextTaskId = approveWinBtnService.wfSendAndChangeApplyState(sendObject, applyinstId, applyState);
            String taskId = sendObject.getTaskId();
            this.updateProjPurchaseStatus(applyinstId, applyState, taskId, null);
            return new ContentResultForm<>(true, nextTaskId, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }


    }

    @PutMapping("/win/changeApplyState")
    @ApiOperation(value = "窗口流程：更改申请状态按钮方法（使用于无需推动流程流转，仅修改申报状态时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "userOpinion", value = "用户意见", dataType = "string", required = true),
            @ApiImplicitParam(name = "applyState", value = "申请状态", dataTypeClass = ApplyState.class, required = true),
            @ApiImplicitParam(name = "actionCode", value = "按钮操作编号，按钮规则为：窗口流程W开头，事项I开头，加按钮拼音首大写字母，如窗口受理：WSL；如事项材料补正：ICLBZ", dataTypeClass = OpsActionConstants.class, required = true)})
    public ResultForm changeApplyState(String applyinstId, String userOpinion, String applyState, String actionCode) {
        try {
            approveWinBtnService.onlyChangeApplyState(applyinstId, userOpinion, applyState, actionCode);
            return new ResultForm(true, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }

    }

    @PutMapping("/item/wfSendAndChangeItemState")
    @ApiOperation(value = "事项流程：流程审批并更改事项实例状态按钮方法（使用于既要推动流程流转，也需要修改事项状态时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "iteminstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "sendObjectStr", value = "流程节点实体字符串", dataType = "string", required = true),
            @ApiImplicitParam(name = "itemState", value = "事项状态", dataType = "string", required = true)})
    public ResultForm wfSendAndChangeItemState(String sendObjectStr, String iteminstId, String itemState) {
        if (StringUtils.isBlank(sendObjectStr)) throw new InvalidParameterException("流程发送参数为空！");
        if (StringUtils.isBlank(iteminstId)) throw new InvalidParameterException("事项实例ID为空！");
        if (StringUtils.isBlank(itemState)) throw new InvalidParameterException("申请状态为空！");

        BpmTaskSendObject sendObject = (BpmTaskSendObject) JSONObject.parseObject(sendObjectStr, BpmTaskSendObject.class);
        try {
            String nextTaskId = approveItemBtnService.wfSendAndChangeItemState(sendObject, iteminstId, itemState);
            String taskId = sendObject.getTaskId();
            this.updateProjPurchaseStatus(null, itemState, taskId, iteminstId);
            return new ContentResultForm<>(true, nextTaskId, "success");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @PutMapping("/item/changeItemState")
    @ApiOperation(value = "事项流程：更改事项状态按钮方法（使用于无需推动流程流转，仅修改事项实例状态时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "iteminstId", value = "事项实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "userOpinion", value = "用户意见", dataType = "string", required = true),
            @ApiImplicitParam(name = "itemState", value = "事项状态", dataTypeClass = ItemStatus.class, required = true),
            @ApiImplicitParam(name = "taskId", value = "任务ID（当需要挂起流程时，需要传参）", dataType = "string", required = false),
            @ApiImplicitParam(name = "actionCode", value = "按钮操作编号，按钮规则为：窗口流程W开头，事项I开头，加按钮拼音首大写字母，如窗口受理：WSL；如事项材料补正：ICLBZ", dataTypeClass = OpsActionConstants.class, required = true)})
    public ResultForm changeItemState(String iteminstId, String userOpinion, String itemState, String actionCode, String taskId) {
        try {
            approveItemBtnService.onlyChangeItemState(iteminstId, userOpinion, actionCode, itemState, taskId);
            this.updateProjPurchaseStatus(null, itemState, taskId, iteminstId);
            return new ResultForm(true, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @PutMapping("/win/wfSendAndChangeApplyAndItemState")
    @ApiOperation(value = "窗口流程：流程审批并更改申请状态和事项状态按钮方法（使用于既要推动流程流转，也需要修改申报状态和事项状态时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "sendObjectStr", value = "流程节点实体字符串", dataType = "string", required = true),
            @ApiImplicitParam(name = "itemState", value = "事项状态", dataTypeClass = ItemStatus.class, required = true),
            @ApiImplicitParam(name = "applyState", value = "申请状态", dataTypeClass = ApplyState.class, required = true)})
    public ResultForm wfSendAndChangeApplyAndItemState(String sendObjectStr, String applyinstId, String applyState, String itemState) {
        if (StringUtils.isBlank(sendObjectStr))
            throw new InvalidParameterException("流程发送参数为空！");
        if (StringUtils.isBlank(applyinstId))
            throw new InvalidParameterException("申请实例ID为空！");
        if (StringUtils.isBlank(applyState))
            throw new InvalidParameterException("申请状态为空！");
        if (StringUtils.isBlank(itemState))
            throw new InvalidParameterException("事项状态为空！");

        BpmTaskSendObject sendObject = JSONObject.parseObject(sendObjectStr, BpmTaskSendObject.class);
        try {
            String nextTaskId = approveWinBtnService.wfSendAndChangeApplyAndItemState(sendObject, applyinstId, applyState, itemState);
            String taskId = sendObject.getTaskId();
            this.updateProjPurchaseStatus(applyinstId, applyState, taskId, null);
            return new ContentResultForm<>(true, nextTaskId, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    //更新采购状态
    private void updateProjPurchaseStatus(String applyinstId, String applyState, String taskId, String iteminstId) throws Exception {
        if (StringUtils.isBlank(applyinstId) && StringUtils.isNotBlank(iteminstId)) {
            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            if (null != iteminst) {
                String isSeriesApprove = iteminst.getIsSeriesApprove();
                if ("1".equals(isSeriesApprove)) {
                    String seriesinstId = iteminst.getSeriesinstId();
                    AeaHiSeriesinst seriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstById(seriesinstId);
                    if (null == seriesinst) throw new Exception("cannot find seriesinst");
                    applyinstId = seriesinst.getApplyinstId();
                } else {
                    String stageinstId = iteminst.getStageinstId();
                    AeaHiParStageinst stageinst = aeaHiParStageinstMapper.getAeaHiParStageinstById(stageinstId);
                    if (null == stageinst) throw new Exception("can nont find stageinst");
                    applyinstId = stageinst.getApplyinstId();
                }
            }
        }
        PurchaseProjVo purchaseProj = this.getPurchaseProj(applyinstId);
        String projPurchaseId = purchaseProj.getProjPurchaseId();
        String opsLinkInfoId = purchaseProj.getLinkmanInfoId();
        aeaImProjPurchaseService.updateProjPurchaseStateAndInsertPurchaseinstState(projPurchaseId, applyState, null, opsLinkInfoId, null, taskId);

    }

    //根据申请实例ID获取aea_im_proj_purchase信息
    private PurchaseProjVo getPurchaseProj(String applyinstId) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (null == applyinst) throw new Exception("can not find applyinst info");
        String applyinstCode = applyinst.getApplyinstCode();
        PurchaseProjVo purchaseProj = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(applyinstCode, null);
        if (null == purchaseProj) throw new Exception("can not find purchase info");
        return purchaseProj;
    }
}
