package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestAeaProjAgentService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaProjApplyAgentDetailVo;
import com.augurit.aplanmis.mall.userCenter.vo.AgentProjInfoParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("rest/user/apply/agent")
@Api(value = "代办申请接口",tags = "代办申请 --> 代办申请接口")
public class RestProjAgentController {
    Logger logger= LoggerFactory.getLogger(RestProjAgentController.class);

    @Autowired
    RestAeaProjAgentService restAeaProjAgentService;
    @Autowired
    private FileUtilsService fileUtilsService;

    @PostMapping("start")
    @ApiOperation(value = "代办申请 --> 代办申请接口")
    public ContentResultForm saveProjInfoAndInitProjApplyAgent(@RequestBody AgentProjInfoParamVo agentProjInfoParamVo, HttpServletRequest request) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            AeaProjApplyAgent aeaProjApplyAgent=restAeaProjAgentService.saveProjInfoAndInitProjApplyAgent(agentProjInfoParamVo,loginInfo);
            return new ContentResultForm(true,aeaProjApplyAgent.getAgentCode());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办申请接口异常");
        }
    }

    @GetMapping("detail")
    @ApiOperation(value = "代办申请 --> 代办详情接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "代办申请ID（签订中或已签订时）", name = "applyAgentId", required = false, dataType = "string")
    })
    public ContentResultForm getProjInfoAndProjApplyAgent(String projInfoId,String applyAgentId, HttpServletRequest request) {
        try {
            AeaProjApplyAgentDetailVo vo=restAeaProjAgentService.getProjInfoAndProjApplyAgent(projInfoId,applyAgentId,request);
            return new ContentResultForm(true,vo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办详情接口异常");
        }
    }


    @PostMapping("upload/{agreementCode}")
    @ApiOperation(value = "代办申请 --> 代办协议上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "协议编码", name = "agreementCode", required = true, dataType = "string")
    })
    public ContentResultForm uploadFile(@PathVariable String agreementCode, HttpServletRequest request) {
        try {
            fileUtilsService.uploadAttachments("AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE", agreementCode,null, request);
            return new ContentResultForm(true,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办协议上传接口异常");
        }
    }

    @PostMapping("submitAgentAgreement/{applyAgentId}")
    @ApiOperation(value = "代办申请 --> 待签章时提交协议接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string")
    })
    public ResultForm submitAgentAgreement(@PathVariable String applyAgentId) {
        try {
            //todo 此处应该先检查是否签证，调用第三方接口
            restAeaProjAgentService.submitAgentAgreement(applyAgentId);
            return new ResultForm(true,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"待签章时提交协议接口异常");
        }
    }
}
