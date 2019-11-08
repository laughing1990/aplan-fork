package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.front.supermarket.service.AgentItemApplyService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApplyData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market/apply")
@Api(value = "中介事项启动流程接口", tags = "申报-发起中介事项申报")
public class AgentItemApplyController {
    @Autowired
    private AgentItemApplyService agentItemApplyService;

    @PostMapping("/series/processflow/start")
    @ApiOperation(value = "中介事项申报 -->唐山模式，发起申报", httpMethod = "POST")
    public ContentResultForm<String> startSeriesFlow(@RequestBody AgentItemApplyData agentItemApplyData) throws Exception {
        String applyinstId = agentItemApplyService.startSeriesFlow(agentItemApplyData);
        return new ContentResultForm<>(true, applyinstId, "Series start process success");
    }

    // processinstid taskId  PROCESSINST_ID TASK_ID
    @PostMapping("/series/inst")
    @ApiOperation(value = "现场登记 --> 生成实例，打印回执", httpMethod = "POST")
    public ContentResultForm<String> instantiateSeriesFlow(@RequestBody AgentItemApplyData agentItemApplyData) throws Exception {
        String applyinstId = agentItemApplyService.instantiateSeriesFlow(agentItemApplyData);
        return new ContentResultForm<>(true, applyinstId, "Series instantiate process success");
    }

    @PostMapping("/series/inadmissible")
    @ApiOperation(value = "现场登记 --> 不予受理，生成实例，启动流程，打印不受理回执", httpMethod = "POST")
    public ContentResultForm<String> inadmissible(@RequestBody AgentItemApplyData agentItemApplyData) throws Exception {
        String applyinstId = agentItemApplyService.inadmissibleSeriesFlow(agentItemApplyData);
        return new ContentResultForm<>(true, applyinstId, "Series instantiate process success");
    }
}
