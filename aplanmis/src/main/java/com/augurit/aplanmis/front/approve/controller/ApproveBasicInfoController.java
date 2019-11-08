package com.augurit.aplanmis.front.approve.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.approve.service.ApproveService;
import com.augurit.aplanmis.front.approve.vo.ApplyFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/rest/approve")
@Api(value = "审批详情页", tags = "审批详情页-基本信息")
@Slf4j
public class ApproveBasicInfoController {
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @GetMapping("/basic/info")
    @ApiOperation(value = "业务审批 --> 首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSeriesinst", value = "1: 表示事项审批; 0: 表示阶段审批", readOnly = true, dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "isApprover", value = "0: 窗口人员; 1: 审批人员", readOnly = true, dataType = "string", paramType = "query", required = true)
    })
    public ModelAndView combineDocuments(String isSeriesinst, String isApprover, ModelMap modelMap) {
        //isApprover 1 表示审批人员
        if ("1".equals(isSeriesinst) || "1".equals(isApprover)) {
            modelMap.put("isSeries", 1);
        } else {
            modelMap.put("isSeries", 0);
        }
        return new ModelAndView("agcloud/bpm/front/process/components/baseInfo");
    }


    @GetMapping("/basic/apply/info")
    @ApiOperation(value = "业务审批 --> 申报表单基本信息")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "applyinstId", value = "申请实例", dataType = "string", required = true)
            , @ApiImplicitParam(name = "isItemSeek", dataType = "boolean")
            , @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "string")
    })
    public ResultForm getBaseApplyFormInfo(String taskId, String applyinstId, boolean isItemSeek) throws Exception {
        log.info("业务审批 --> 申报表单基本信息， taskId: {},applyinstId:{},isItemSeek:{}", taskId, applyinstId, isItemSeek);
        ApplyFormVo applyFormInfo = approveService.getBaseApplyFormInfo(taskId, applyinstId, isItemSeek);
        return new ContentResultForm(true, applyFormInfo, "success");
    }


    @GetMapping("/basic/getIteminstId")
    @ApiOperation(value = "业务审批 --> 根据taskId获取事项实例id")
    @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "string")
    public ResultForm getIteminstIdByTaskId(String taskId) throws Exception {
        log.info("业务审批 --> 根据taskId获取事项实例id， taskId: {}", taskId);
        return new ContentResultForm(true, approveService.getIteminstByTaskId(taskId), "success");
    }


    @GetMapping("/proj/unit/all")
    @ApiOperation(value = "4.0审批页 --> 查询申报主体")
    @ApiImplicitParam(name = "applyinstId", value = "申请实例", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ResultForm getApplyUnit(String applyinstId) throws Exception {
        List<AeaUnitInfo> applyUnitProj = aeaUnitInfoService.findApplyUnitProj(applyinstId, null);
        return new ContentResultForm<>(true, applyUnitProj);
    }
}
