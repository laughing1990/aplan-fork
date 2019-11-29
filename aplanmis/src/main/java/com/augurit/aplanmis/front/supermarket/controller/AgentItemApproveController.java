package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.front.apply.service.RestApplyMatService;
import com.augurit.aplanmis.front.apply.vo.Mat2MatInstVo;
import com.augurit.aplanmis.front.apply.vo.SaveMatinstVo;
import com.augurit.aplanmis.front.supermarket.service.AgentItemApproveService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApproveForm;
import com.augurit.aplanmis.front.supermarket.vo.ReceiveServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private RestApplyMatService restApplyMatService;

    @GetMapping("/approveSubmitMat.html")
    public ModelAndView index(String taskId, String viewId, String busRecordId) {
        ModelAndView mv = new ModelAndView("supermarket/approveSubmitMat");
        mv.addObject("taskId", taskId);
        mv.addObject("viewId", viewId);
        mv.addObject("busRecordId", busRecordId);

        return mv;
    }

    @GetMapping("/applyForm.html")
    public ModelAndView applyForm(String taskId, String isSeriesinst, String iteminstId) {
        ModelAndView mv = new ModelAndView("approve/applyForm");
        mv.addObject("taskId", taskId);
        mv.addObject("isSeriesinst", isSeriesinst);
        mv.addObject("iteminstId", iteminstId);

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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "matinstId", value = "材料实例ID", dataType = "string", required = false),
            @ApiImplicitParam(name = "request", value = "文件流", dataType = "HttpServletRequest", required = true),
            @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = false),
            @ApiImplicitParam(name = "matId", value = "材料定义ID", dataType = "string", required = false),
    })
    @PostMapping("/uploadServiceResultAtt")
    public ResultForm uploadServiceResult(String matinstId, String applyinstId, String matId, HttpServletRequest request) throws Exception {
        matinstId = agentItemApproveService.uploadServiceResultAtt(matinstId, applyinstId, matId, request);

        return new ContentResultForm<>(true, matinstId, "successs");
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


    @PostMapping("/matinst/batch/save")
    @ApiOperation("申报页面根据材料定义生成材料实例id")
    public ContentResultForm<List<Mat2MatInstVo>> saveMatinsts(@RequestBody SaveMatinstVo saveMatinstVo, String applyinstId) {


        List<Mat2MatInstVo> mat2MatInstVos = restApplyMatService.saveMatinsts(saveMatinstVo);
        return new ContentResultForm<>(true, mat2MatInstVos, "Batch save matinst success");
    }

    @PostMapping("/receivePaperAndStartProcess")
    @ApiOperation("窗口人员收取纸质服务结果并发起流程")
    public ResultForm uploadPaperMatAndStartProcess(@RequestBody ReceiveServiceResult saveMatinstVo) throws Exception {
        if (null == saveMatinstVo || StringUtils.isBlank(saveMatinstVo.getApplyinstId()) || StringUtils.isBlank(saveMatinstVo.getIteminstId()))
            throw new Exception("params is null");
        agentItemApproveService.uploadPaperMatAndStartProcess(saveMatinstVo);
        return new ResultForm(true, "success");
    }
}
