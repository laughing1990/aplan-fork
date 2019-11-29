package com.augurit.aplanmis.supermarket.register.controller;


import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.vo.AgentRegisterVo;
import com.augurit.aplanmis.supermarket.cert.service.AeaCertService;
import com.augurit.aplanmis.supermarket.certinst.service.AeaHiCertinstService;
import com.augurit.aplanmis.supermarket.register.service.AgentRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/supermarket/agentRegister")
public class AgentRegisterController {
    @Autowired
    AgentRegisterService agentRegisterService;

    @Autowired
    AeaHiCertinstService aeaHiCertinstService;

    @Autowired
    AeaCertService aeaCertService;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @RequestMapping("/index.html")
    public ModelAndView indexAeaImServiceLinkman() throws  Exception{
        //虚拟登录员信息
        OpuOmUser opuOmUser=new OpuOmUser();
        opuOmUser.setUserId("admin");
        opuOmUser.setLoginName("admin");
        //aeaCertService.listAeaCert();
        agentRegisterService.generateVirtualUser(opuOmUser,topOrgId);
        return new ModelAndView("zjcs/login/orgEnter");
    }

    @RequestMapping("/saveRegisterForm")
    public ResultForm saveRegisterForm(AgentRegisterVo agentRegisterVo, HttpServletRequest request) throws Exception {
        if(agentRegisterVo!=null){
                agentRegisterService.saveAgentRegisterInfo(agentRegisterVo,request);
        }else{
            return new ContentResultForm<AgentRegisterVo>(false,agentRegisterVo,"保存对象为空！");
        }
        return new ContentResultForm<AgentRegisterVo>(true,agentRegisterVo);
    }


}
