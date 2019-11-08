package com.augurit.aplanmis.rest.common.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.common.utils.SessionUtil;
import com.augurit.aplanmis.rest.common.vo.BindUnitInfoVo;
import com.augurit.aplanmis.rest.common.vo.LoginInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    private ContentResultForm getRestResult(boolean success, String message) {
        ContentResultForm contentResultForm = new ContentResultForm(success);
        contentResultForm.setMessage(message);
        return contentResultForm;
    }


    public ContentResultForm login(String userName, String password, String isOwner, HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(isOwner)) {
            return getRestResult(false, "请选择登录角色");
        }

        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setLoginName(userName);
        aeaUnitInfo.setRootOrgId(topOrgId);
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoByLoginName(userName, topOrgId);
        List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);

        if (aeaLinkmanInfo == null && aeaUnitInfos.size() == 0) {
            return getRestResult(false, "用户不存在");
        }

        String userId;
        List<AeaUnitInfo> unitList;
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setIsOwner(isOwner);

        // true：中介机构；fasle:业主单位；
        boolean flag = "0".equals(isOwner);

        OpuOmUser opuOmUser = new OpuOmUser();
        opuOmUser.setLoginName(userName);

        if (aeaLinkmanInfo != null && aeaUnitInfos.size() == 0) {

            //AeaLinkmanInfo link = aeaLinkmanInfo;
            userId = aeaLinkmanInfo.getLinkmanInfoId();

            //委托人登录
            if (password.equals(aeaLinkmanInfo.getLoginPwd())) {
                //查询登录人单位信息列表
                unitList = aeaUnitInfoMapper.listAeaUnitInfoByLinkIdHasBind(userId);

                if (unitList.size() > 0) {
                    for (AeaUnitInfo unitInfo : unitList) {
                        //判断是否有匹配当前登录角色（业主或中介）的单位
                        loginInfoVo.setUnitId(unitInfo.getUnitInfoId());
                        loginInfoVo.setUnitName(unitInfo.getApplicant());
                        if (flag) {
                            if (StringUtils.isNotBlank(unitInfo.getIsImUnit()) && "1".equals(unitInfo.getIsImUnit())) {
                                loginInfoVo.setIsPersonAccount("0");
                                loginInfoVo.setIsAdministrators(unitInfo.getIsAdministrators());
                                break;
                            }
                        } else {
                            if (StringUtils.isNotBlank(unitInfo.getIsOwnerUnit()) && "1".equals(unitInfo.getIsOwnerUnit())) {
                                loginInfoVo.setIsPersonAccount("0");
                                loginInfoVo.setIsAdministrators(unitInfo.getIsAdministrators());
                                break;
                            }
                        }
                    }
                    loginInfoVo.setUnitInfoList(unitList);//网厅使用
                    loginInfoVo.setUnitInfos(getBindUnitInfo(unitList));//中介超市使用

                } else {
                    //个人登录
                    if (flag) {
                        return getRestResult(false, "该用户没有绑定单位");
                    }

                    loginInfoVo.setIsPersonAccount("1");
                    loginInfoVo.setIsAdministrators("0");
                }

                loginInfoVo.setUserId(userId);
                loginInfoVo.setPersonName(aeaLinkmanInfo.getLinkmanName());
                loginInfoVo.setImgUrl(aeaLinkmanInfo.getImgUrl());

                opuOmUser.setUserId(userId);
                opuOmUser.setUserName(aeaLinkmanInfo.getLinkmanName());

            } else {
                return getRestResult(false, "密码错误");
            }
        } else if (aeaLinkmanInfo == null && aeaUnitInfos.size() == 1) {

            aeaUnitInfo = aeaUnitInfos.get(0);

            loginInfoVo.setImgUrl(aeaUnitInfo.getImgUrl());

            //单位账号登录
            if (password.equals(aeaUnitInfo.getLoginPwd())) {
                loginInfoVo.setUnitId(aeaUnitInfo.getUnitInfoId());
                loginInfoVo.setUnitName(aeaUnitInfo.getApplicant());
                if (flag) {
                    if (StringUtils.isNotBlank(aeaUnitInfo.getIsImUnit()) && "1".equals(aeaUnitInfo.getIsImUnit())) {
                        loginInfoVo.setIsPersonAccount("0");
                        loginInfoVo.setIsAdministrators("1");
                    }
                } else {
                    if (StringUtils.isNotBlank(aeaUnitInfo.getIsOwnerUnit()) || "1".equals(aeaUnitInfo.getIsOwnerUnit())) {
                        loginInfoVo.setIsPersonAccount("0");
                        loginInfoVo.setIsAdministrators("1");
                    }
                }

                if (StringUtils.isBlank(loginInfoVo.getUnitId())) {
                    return getRestResult(false, "该账号没有此角色权限");
                }

                opuOmUser.setUserId(aeaUnitInfo.getUnitInfoId());
                opuOmUser.setUserName(aeaUnitInfo.getApplicant());

            } else {
                return getRestResult(false, "密码错误");
            }
        } else {
            return getRestResult(false, "用户重复");
        }

        loginInfoVo = SessionUtil.saveLoginInfoVo(request, loginInfoVo);

        setLoginUserToSecurityContext(opuOmUser);

        return new ContentResultForm<>(true, loginInfoVo);
    }


    public ResultForm logout(HttpServletRequest request) throws Exception {
        /*request.getSession().removeAttribute("SESSION_USER_KEY");
        request.getSession().removeAttribute("SESSION_UNIT_KEY");*/
        SessionUtil.clearSession(request);
        //request.getSession().invalidate();
        SecurityContext.logout();

        SecurityContext.setCurrentLoginUser(null, null);
        return new ResultForm(true);
    }

    public ResultForm changeApplicant(HttpServletRequest request, String unitInfoId) throws Exception {
        AeaUnitInfo unitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        loginInfoVo.setUnitId(unitInfoId);
        loginInfoVo.setUnitName(unitInfo == null ? "" : unitInfo.getApplicant());
        loginInfoVo = SessionUtil.saveLoginInfoVo(request, loginInfoVo);
        return new ContentResultForm<>(true, loginInfoVo);
    }

    public void setLoginUserToSecurityContext(OpuOmUser opuOmUser) {
        com.augurit.agcloud.framework.security.user.OpuOmUser loginUser = new com.augurit.agcloud.framework.security.user.OpuOmUser();
        BeanUtils.copyProperties(opuOmUser, loginUser);
        loginUser.setUserName(opuOmUser.getUserName());
        OpusLoginUser opusLoginUser = new OpusLoginUser();
        opusLoginUser.setCurrentOrgId(topOrgId);
        opusLoginUser.setUser(loginUser);
        opusLoginUser.setCurrentTmn("pc");
        OpuOmOrg org = opuOmOrgMapper.getOrg(topOrgId);
        if (org != null) {
            opusLoginUser.setCurrentOrgCode(org.getOrgCode());
        }
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(loginUser.getUserId());
        List<com.augurit.agcloud.framework.security.user.OpuOmOrg> loginOrgs = new ArrayList<>();
        for (OpuOmOrg ooo : opuOmOrgs) {
            com.augurit.agcloud.framework.security.user.OpuOmOrg loginOrg = new com.augurit.agcloud.framework.security.user.OpuOmOrg();
            BeanUtils.copyProperties(ooo, loginOrg);
            loginOrgs.add(loginOrg);
        }
        opusLoginUser.setOrgs(loginOrgs);
        SecurityContext.setCurrentLoginUser(opusLoginUser, null);
    }

    private List<BindUnitInfoVo> getBindUnitInfo(List<AeaUnitInfo> unitList) {
        List<BindUnitInfoVo> bindUnitInfoVos = new ArrayList<>();
        if (unitList != null) {
            for (AeaUnitInfo aeaUnitInfo : unitList) {

                if ("1".equals(aeaUnitInfo.getIsOwnerUnit()) || "1".equals(aeaUnitInfo.getIsImUnit())) {
                    BindUnitInfoVo bindUnitInfoVo = new BindUnitInfoVo();
                    BeanUtils.copyProperties(aeaUnitInfo, bindUnitInfoVo);
                    bindUnitInfoVos.add(bindUnitInfoVo);
                }
            }
        }

        return bindUnitInfoVos;
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
            if (password.equals(aeaLinkmanInfo.getLoginPwd())) {
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
                if ("1".equals(owner.getIsOwner()) && password.equals(owner.getLoginPwd())) {
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
            AeaLinkmanInfo aeaLinkmanInfo = Optional.ofNullable(aeaLinkmanInfos).orElse(new ArrayList<>()).get(0);
            authUser.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            authUser.setLinikmanName(aeaLinkmanInfo.getLinkmanName());
            authUser.setCurrentOrgId(aeaLinkmanInfo.getRootOrgId());
            authUser.setPersonalAccount(false);
        }
        if (StringUtils.isNotBlank(authUser.getCurrentOrgId())) {
            OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(authUser.getCurrentOrgId());
            authUser.setCurrentOrgName(Optional.ofNullable(opuOmOrg).orElse(new OpuOmOrg()).getOrgName());
        }
    }
}
