package com.augurit.aplanmis.supermarket.register.controller;

import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.vo.OwnerRegisterVo;
import com.augurit.aplanmis.supermarket.register.service.AgentRegisterService;
import com.augurit.aplanmis.supermarket.register.service.OwnerRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/supermarket/ownerRegister")
public class OwnerRegisterController {
    @Autowired
    OwnerRegisterService ownerRegisterService;
    @Autowired
    AgentRegisterService agentRegisterService;
    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @RequestMapping("/index.html")
    public ModelAndView indexAeaImServiceLinkman() throws  Exception{
        //虚拟登录员信息
        OpuOmUser opuOmUser=new OpuOmUser();
        opuOmUser.setUserId("admin");
        agentRegisterService.generateVirtualUser(opuOmUser,topOrgId);
        return new ModelAndView("aea/im/service/service_linkman_index");
    }

    @RequestMapping("/saveRegisterForm")
    public ResultForm saveRegisterForm(OwnerRegisterVo ownerRegisterVo, HttpServletRequest request) throws Exception {
        if(ownerRegisterVo!=null){
            ownerRegisterService.saveAgentRegisterInfo(ownerRegisterVo,request);
        }else{
            return new ContentResultForm<OwnerRegisterVo>(false,ownerRegisterVo);
        }
        return new ContentResultForm<OwnerRegisterVo>(true,ownerRegisterVo);
    }


}
