package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.front.supermarket.service.AgentItemApplyService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApplyData;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private RestImApplyService restImApplyService;
    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @PostMapping("/series/processflow/start")
    @ApiOperation(value = "中介事项申报 -->唐山模式，发起申报", httpMethod = "POST")
    @Transactional
    public ContentResultForm<String> startSeriesFlow(@RequestBody AgentItemApplyData agentItemApplyData) throws Exception {
        String applyinstIdParam = agentItemApplyData.getApplyinstId();

        //需要先保存 采购项目信息，发起事项流程时关联的是采购项目信息
        AeaProjInfo aeaProjInfo = agentItemApplyData.createAeaProjInfo();
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);

        ImItemApplyData applyData = agentItemApplyData.createItemApplyData();
        applyData.setProjInfoId(aeaProjInfo.getProjInfoId());
        //发起中介事项流程
        ApplyinstResult result = restImApplyService.purchaseStartProcess(applyData);

        String applyinstId = result.getApplyinstId();
        String applyinstCode = result.getApplyinstCode();
        //保存采购信息
        ImPurchaseData purchaseData = agentItemApplyData.createPurchaseData(applyinstId, applyinstCode);
        //回填采购项目ID
        purchaseData.setProjInfoId(aeaProjInfo.getProjInfoId());
        restImApplyService.savePurchaseProjInfo(purchaseData);
        //保存受理回执，物料回执
        if (StringUtils.isBlank(applyinstIdParam)) {
            receiveService.saveAgentItemReceive(applyinstId, new String[]{"1", "2"}, SecurityContext.getCurrentUserName(), agentItemApplyData.getComments());
        }
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
