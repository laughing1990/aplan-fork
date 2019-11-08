package com.augurit.aplanmis.common.service.admin.opus.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmZtreeNode;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.convert.OpuOmOrgConvert;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织机构接口实现类
 */
@Service
@Transactional
public class AplanmisOpuOmOrgAdminServiceImpl implements AplanmisOpuOmOrgAdminService {

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private OpuOmOrgService opuOmOrgService;

    /**
     * 根据组织id获取ztree组织树（所有的子孙节点）
     *
     * @param orgId 组织ID
     */
    @Override
    public List<OpusOmZtreeNode> getAllOpuOmOrgZTreeByOrgId(String orgId) {
        return opuOmOrgService.getAllOpuOmOrgZTreeByOrgId(orgId);
    }

    /**
     * 获取组织、岗位、用户 异步ztree树
     *
     * @param id   组织或岗位id
     * @param type 类型为组织 o 或者岗位 p
     */
    @Override
    public List<OpusOmZtreeNode> getOpuOmOrgPosUserAsyncZTree(String id, String type) {
        return opuOmOrgService.getOpuOmOrgPosUserAsyncZTree(id, type);
    }

    @Override
    public OpuOmOrg getOpuOmOrgById(String orgId) {
        return opuOmOrgMapper.getOrg(orgId);
    }

    @Override
    public OpuOmOrg getTopOrgByCurOrgId(String orgId) {
        return opuOmOrgService.getTopOrgByCurOrgId(orgId);
    }

    @Override
    public ElementUiRsTreeNode getAllOpuOmOrgElementUITreeByOrgId(String orgId) {
        OpuOmOrgConvert convert = new OpuOmOrgConvert();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        OpuOmOrg root = opuOmOrgMapper.getActiveOrg(rootOrgId);
        if(root!=null){
            List<OpuOmOrg> opuOmOrgs = opuOmOrgService.listAllActiveAndNormalOrgs(rootOrgId);
            ElementUiRsTreeNode elementUiRsTreeNode = convert.convertToElementUiNode(root, opuOmOrgs);
            return elementUiRsTreeNode;
        }
        return null;
    }

}
