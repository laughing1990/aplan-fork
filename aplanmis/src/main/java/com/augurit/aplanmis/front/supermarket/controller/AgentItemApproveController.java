package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
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

@RestController
@RequestMapping("/market/approve")
@Api(value = "中介事项流程审批接口", tags = "中介事项审批-中介事项流程审批接口")
@Slf4j
public class AgentItemApproveController {

    @Autowired
    private AgentItemApproveService agentItemApproveService;

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
}
