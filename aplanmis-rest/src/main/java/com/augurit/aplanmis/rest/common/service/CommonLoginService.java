package com.augurit.aplanmis.rest.common.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.utils.SM3;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.common.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommonLoginService {
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;

    public ResultForm logout(HttpServletRequest request) {
        SessionUtil.clearSession(request);
        SecurityContext.logout();

        SecurityContext.setCurrentLoginUser(null, null);
        return new ResultForm(true);
    }

    public AuthUser changeApplicant(String unitInfoId) {
        AeaUnitInfo unitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        AuthUser currentUser = AuthContext.getCurrentUser();
        currentUser.setUnitInfoId(unitInfoId);
        currentUser.setUnitInfoName(unitInfo == null ? "" : unitInfo.getApplicant());
        return currentUser;
    }

    public AuthUser mobileLogin(String userName, String password) throws Exception {
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setLoginName(userName);
        aeaUnitInfo.setRootOrgId(topOrgId);
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoByLoginName(userName, topOrgId);
        List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);

        if (aeaLinkmanInfo == null && aeaUnitInfos.size() == 0) {
            throw new Exception("用户不存在");
        }
        AuthUser authUser = new AuthUser();
        authUser.setLoginName(userName);
        authUser.setRootOrgId(topOrgId);
        if (aeaLinkmanInfo != null) {
            //委托人登录
            if (password.equals(SM3.generateSM3(aeaLinkmanInfo.getLoginPwd()))) {
                authUser.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
                authUser.setLinikmanName(aeaLinkmanInfo.getLinkmanName());
                authUser.setCurrentOrgId(aeaLinkmanInfo.getRootOrgId());
            } else {
                throw new Exception("密码错误");
            }
        }
        if (aeaUnitInfos != null && aeaUnitInfos.size() > 0) {
            for (AeaUnitInfo owner : aeaUnitInfos) {
                //单位账号登录
                if ("1".equals(owner.getIsOwnerUnit()) && password.equals(SM3.generateSM3(owner.getLoginPwd()))) {
                    authUser.setUnitInfoId(owner.getUnitInfoId());
                    authUser.setUnitInfoName(owner.getApplicant());
                    authUser.setCurrentOrgId(owner.getRootOrgId());
                    break;
                }
            }
        }
        if (StringUtils.isBlank(authUser.getLinkmanInfoId()) && StringUtils.isBlank(authUser.getUnitInfoId())) {
            throw new Exception("用户不存在");
        }
        // 补充用户信息
        supplementAuthUser(authUser);
        return authUser;
    }

    private void supplementAuthUser(AuthUser authUser) {
        if (StringUtils.isNotBlank(authUser.getLinkmanInfoId()) && StringUtils.isBlank(authUser.getUnitInfoId())) {
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoMapper.getAeaUnitListByLinkmanInfoId(authUser.getLinkmanInfoId());
            AeaUnitInfo aeaUnitInfo = Optional.ofNullable(aeaUnitInfos).orElse(new ArrayList<>()).get(0);
            authUser.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
            authUser.setUnitInfoName(aeaUnitInfo.getApplicant());
            authUser.setCurrentOrgId(aeaUnitInfo.getRootOrgId());
            authUser.setPersonalAccount(true);
        } else if (StringUtils.isBlank(authUser.getLinkmanInfoId()) && StringUtils.isNotBlank(authUser.getUnitInfoId())) {
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoId(authUser.getUnitInfoId(), 1);
            if (aeaLinkmanInfos.size() > 0) {
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfos.get(0);
                authUser.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
                authUser.setLinikmanName(aeaLinkmanInfo.getLinkmanName());
                authUser.setCurrentOrgId(aeaLinkmanInfo.getRootOrgId());
                authUser.setPersonalAccount(false);
            }
        }
        if (StringUtils.isNotBlank(authUser.getCurrentOrgId())) {
            OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(authUser.getCurrentOrgId());
            authUser.setCurrentOrgName(Optional.ofNullable(opuOmOrg).orElse(new OpuOmOrg()).getOrgName());
        }
    }
}
