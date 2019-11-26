package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.front.supermarket.service.AgentItemApproveService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApproveForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/market/approve")
@Api(value = "中介事项流程审批接口", tags = "中介事项审批-中介事项流程审批接口")
@Slf4j
public class AgentItemApproveController {

    @Autowired
    private AgentItemApproveService agentItemApproveService;

    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;

    @GetMapping("/approveSubmitMat.html")
    public ModelAndView index(String taskId,String viewId,String busRecordId){
        ModelAndView mv =new ModelAndView("supermarket/approveSubmitMat");
        mv.addObject("taskId",taskId);
        mv.addObject("viewId",viewId);
        mv.addObject("busRecordId",busRecordId);

        return mv;
    }

    @GetMapping("/basic/apply/info")
    @ApiOperation(value = "业务审批 --> 申报表单基本信息")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "applyinstId", value = "申请实例", dataType = "string", required = true)
            , @ApiImplicitParam(name = "isItemSeek", value = "是否意见征集", dataType = "boolean")
            , @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "string")
    })
    public ResultForm getBaseApplyFormInfo(String taskId, String applyinstId, boolean isItemSeek) throws Exception {
        log.info("中介事项业务审批 --> 申报表单基本信息， taskId: {},applyinstId:{},isItemSeek:{}", taskId, applyinstId, isItemSeek);
        AgentItemApproveForm applyFormInfo = agentItemApproveService.getBaseApplyFormInfo(taskId, applyinstId, isItemSeek);
        return new ContentResultForm(true, applyFormInfo, "success");
    }

    /**
     * 上传中介服务结果附件 todo
     *
     * @return
     */
    @ApiOperation(value = "上传中介服务结果附件", notes = "上传中介服务结果附件-新接口，需要判断是否推动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true)
            , @ApiImplicitParam(name = "matinstIds", value = "事项实例id", required = true)
    })
    @GetMapping("/uploadServiceResult")
    public ResultForm uploadServiceResult(String[] matinstIds, String applyinstId) throws Exception {
//        agentItemApproveService.uploadServiceResult(matinstIds, applyinstId);

        return new ResultForm(true, "successs");
    }

    @ApiOperation(value = "查询采购项目申报材料及实例列表", notes = "上传服务结果-新接口，需要判断是否推动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true)
            , @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true)
    })
    @GetMapping("/getProjPurchaseMatinstList")
    public ContentResultForm<MatinstVo> getProjPurchaseMatinstList(String applyinstId, String iteminstId) throws Exception {
        List<MatinstVo> vos = aeaImProjPurchaseService.listPurchaseMatinst(iteminstId, applyinstId);
        return new ContentResultForm(true, vos, "success");
    }

}
