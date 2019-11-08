package com.augurit.aplanmis.rest.opu.controller;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.exception.UserNotFoundMainError;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmZtreeNode;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserInfoService;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserService;
import com.tls.tls_sigature.tls_sigature;
import com.tls.tls_sigature.tls_sigature.GenTLSSignatureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/opus/om")
public class OpuOmRestController {

    private static Logger logger = LoggerFactory.getLogger(OpuOmRestController.class);

    @Autowired
    private OpuOmUserInfoService opuOmUserInfoService;

    @Autowired
    private OpuOmOrgService opuOmOrgService;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private OpuOmUserService opuOmUserService;

    public static final long SDK_APPID = 1400212943;
    public static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nMIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgvld08aP5eFG2hwOg\nQpAx5IWeHj+X2nYsIfC9syTOQs2hRANCAASUMJ1koy1ZfMOG0rA2l99R6+dc7Zmi\nYLIkv8lRBDZKoA+/Cl5tkYo8SZ6NjY9oLnHymzOP+8emlOM7bdEGNWJM\n-----END PRIVATE KEY-----\n";

    /**
     * 根据登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    @GetMapping("/getOpuOmUserInfoByloginName.do")
    public OpuOmUserInfo getOpuOmUserInfoByloginName(String loginName) {

        OpuOmUserInfo userInfo = opuOmUserInfoService.getOpuOmUserInfoByloginName(loginName);
        if (userInfo != null) {
//            String userSig = UserSigGenerator.getUserSig(loginName);
//            userInfo.setUserSig(userSig);
            GenTLSSignatureResult result = tls_sigature.genSig(SDK_APPID, loginName, 2592000, PRIVATE_KEY);
            userInfo.setUserSig(result.urlSig);
        }
        return userInfo;
    }

    /**
     * 根据父id查询子组织、子岗位和子用户
     *
     * @param parentId   父节点id
     * @param parentType 父节点类型 o:组织  p:岗位
     * @return
     * @throws Exception
     */
    @GetMapping("/listOpuOmSubOrgPosUserByParentId.do")
    public List<OpusOmZtreeNode> getOpuOmSubOrgPosUserByOrgId(String parentId, String parentType) throws Exception {

        List<OpusOmZtreeNode> list = opuOmOrgService.getOpuOmSubOrgPosUserByParentId(parentId, parentType);
        if (logger.isDebugEnabled()) {
            logger.debug("成功获取opus下级组织和用户");
        }
        return list;
    }

    /**
     * 查询用户顶级组织，用于登录后选择组织
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @GetMapping("/listUserTopOrg.do")
    public List<OpuOmOrg> listOpuOmUserOrg(String username, String password) throws Exception {

        OpuOmUser opuOmUser = opuOmUserService.getUserByLoginName(username);
        if (opuOmUser == null) {
            throw new UserNotFoundMainError();
        }
        if (!opuOmUser.getLoginPwd().equals(password)) {
            throw new UserNotFoundMainError();
        }
        List<OpuOmOrg> userOrgList = opuOmOrgMapper.listOpuOmUserOrgByUserId(opuOmUser.getUserId());
        if (userOrgList != null && userOrgList.size() > 0) {
            //处理全部的顶级组织（去重），登录只选择顶级组织
            Map<String, String> orgMap = new HashMap<String, String>();
            for (OpuOmOrg opuOmOrg : userOrgList) {
                String topOrgId = opuOmOrg.getOrgSeq().split("\\.")[1];
                orgMap.put(topOrgId, "");
            }
            //处理返回的顶级组织
            List<OpuOmOrg> opuOmOrgList = new ArrayList<>();
            for (String key : orgMap.keySet()) {
                opuOmOrgList.add(opuOmOrgMapper.getOrg(key));
            }
            return opuOmOrgList;
        }
        return null;
    }
}
