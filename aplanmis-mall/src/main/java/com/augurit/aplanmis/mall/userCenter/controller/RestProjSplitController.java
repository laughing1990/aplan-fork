package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.utils.FileUtils;
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
import javax.validation.Valid;

@RestController
@RequestMapping("rest/user/proj/split")
@Api(value = "拆分子工程申请接口",tags = "拆分工程申请 --> 拆分工程申请接口")
public class RestProjSplitController {
    Logger logger= LoggerFactory.getLogger(RestProjSplitController.class);

    @Autowired
    RestAeaProjAgentService restAeaProjAgentService;

    @PostMapping("start")
    @ApiOperation(value = "代办申请 --> 代办申请接口")
    public ContentResultForm saveProjInfoAndInitProjApplyAgent(@Valid @RequestBody AgentProjInfoParamVo agentProjInfoParamVo, HttpServletRequest request) {
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

    @PostMapping("upload/{applyAgentId}")
    @ApiOperation(value = "代办申请 --> 代办协议上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string")
    })
    public ContentResultForm uploadFile(@PathVariable String applyAgentId, HttpServletRequest request) {
        try {
            FileUtils.uploadFile("AEA_PROJ_APPLY_AGENT", "APPLY_AGENT_ID", applyAgentId, request);
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
