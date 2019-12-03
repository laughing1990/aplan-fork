package com.augurit.aplanmis.supermarket.register.controller;


import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
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
import java.util.List;

@RestController
@RequestMapping("/supermarket/agentRegister")
public class AgentRegisterController {
    @Autowired
    AgentRegisterService agentRegisterService;

    @Autowired
    AeaHiCertinstService aeaHiCertinstService;

    @Autowired
    AeaCertService aeaCertService;

    @Autowired
    AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    AeaLinkmanInfoService aeaLinkmanInfoService;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @RequestMapping("/register.html")
    public ModelAndView registerIndex() throws  Exception{
        return new ModelAndView("zjcs/login/regist");
    }

    @RequestMapping("/index.html")
    public ModelAndView indexAeaImServiceLinkman() throws  Exception{
        //虚拟登录员信息
        OpuOmUser opuOmUser=new OpuOmUser();
        opuOmUser.setUserId("tourist");
        opuOmUser.setLoginName("游客");
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

    /**
     * 根据统一社会信用代码判断是否已重复
     * @param unifiedSocialCreditCode
     * @return
     */
    @RequestMapping("/checkUnitIsExisted")
    public ResultForm checkUnitIsExisted(String unifiedSocialCreditCode) {
        if (StringUtils.isNotBlank(unifiedSocialCreditCode)) {
            List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoService.getUnitInfoListByIdCard(unifiedSocialCreditCode);
            if (CollectionUtils.isNotEmpty(listAeaUnitInfos)) {
                return new ContentResultForm<Boolean>(true, false, "该统一社会信用代码已注册！");
            }
        }
        return new ContentResultForm<Boolean>(true, true);
    }

    /**
     * 根据身份证号判断用户是否已重复
     * @param linkmanCertNo
     * @return
     */
    @RequestMapping("/checkManIsExisted")
    public ResultForm checkManIsExisted(String linkmanCertNo) {
        if (StringUtils.isNotBlank(linkmanCertNo)) {
            List<AeaLinkmanInfo> links = aeaLinkmanInfoService.getAeaLinkmanInfoListByCertNo(linkmanCertNo);
            if (CollectionUtils.isNotEmpty(links)) {
                return new ContentResultForm<Boolean>(true, false, "该身份证号已注册用户！");
            }
        }
        return new ContentResultForm<Boolean>(true, true);
    }

}
