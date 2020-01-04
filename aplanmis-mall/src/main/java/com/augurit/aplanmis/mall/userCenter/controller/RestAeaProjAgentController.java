package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestAeaProjAgentService;
import com.augurit.aplanmis.mall.userCenter.vo.AgentProjInfoParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("rest/apply/agent")
@Api(value = "代办申请接口",tags = "代办申请 --> 代办申请接口")
public class RestAeaProjAgentController {
    Logger logger= LoggerFactory.getLogger(RestAeaProjAgentController.class);

    @Autowired
    RestAeaProjAgentService restAeaProjAgentService;

    @GetMapping("start")
    @ApiOperation(value = "代办申请 --> 代办申请接口")
    public ContentResultForm listItemAndStateByStageId(@Valid @RequestBody AgentProjInfoParamVo agentProjInfoParamVo, HttpServletRequest request) {
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            AeaProjApplyAgent aeaProjApplyAgent=restAeaProjAgentService.saveProjInfoAndInitProjApplyAgent(agentProjInfoParamVo,loginInfo);
            return new ContentResultForm(true,aeaProjApplyAgent.getAgentCode());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办申请接口异常");
        }
    }

}
