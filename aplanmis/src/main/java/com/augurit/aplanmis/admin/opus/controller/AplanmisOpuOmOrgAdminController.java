package com.augurit.aplanmis.admin.opus.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmType;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmZtreeNode;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/opus/om/org/admin")
public class AplanmisOpuOmOrgAdminController {

    private static Logger logger = LoggerFactory.getLogger(AplanmisOpuOmOrgAdminController.class);

    @Autowired
    private AplanmisOpuOmOrgAdminService opuOmOrgService;

    /**
     * * 获取组织机构树节点列表
     */
    @RequestMapping("getOpusOmOrgZtreeNode.do")
    public List<OpusOmZtreeNode> getOpusOmOrgZtreeNode() {

        // 顶级机构
        String orgId = SecurityContext.getCurrentOrgId();
        return opuOmOrgService.getAllOpuOmOrgZTreeByOrgId(orgId);
    }

    /**
     * * 获取组织机构树节点列表
     */
    @RequestMapping("getOpusOmOrgElementUINode.do")
    public ElementUiRsTreeNode getOpusOmOrgElementUINode() {

        // 顶级机构
        String orgId = SecurityContext.getCurrentOrgId();
        return opuOmOrgService.getAllOpuOmOrgElementUITreeByOrgId(orgId);
    }

    /**
     * 加载组织异步ztree 组织岗位用户树（包含组织、岗位和用户）
     *
     * @param id   组织或岗位id
     * @param type 类型为组织 o 或者岗位 p
     * @return ztree树json
     */
    @RequestMapping("/getOpuOmOrgPosUserAsyncZTreeUserExistCheck.do")
    @ResponseBody
    public List<OpusOmZtreeNode> getOpuOmOrgPosUserAsyncZTreeExistCheck(String id, String type) {

        //加载单位树
        List<OpusOmZtreeNode> list = opuOmOrgService.getOpuOmOrgPosUserAsyncZTree(id, type);
        for (OpusOmZtreeNode opusOmZtreeNode : list) {
            if (opusOmZtreeNode.getOpusOmType().equals(OpusOmType.ORG)) {
                opusOmZtreeNode.setNocheck(true);
            }
            if (opusOmZtreeNode.getOpusOmType().equals(OpusOmType.POS)) {
                opusOmZtreeNode.setNocheck(true);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("成功获取ztree组织岗位用户树信息");
        }
        return list;
    }
}