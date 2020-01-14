package com.augurit.aplanmis.common.service.admin.opus;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.support.om.OpusOmZtreeNode;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 15:13
 * @desc
 **/
public interface AplanmisOpuOmOrgAdminService {
    List<OpusOmZtreeNode> getAllOpuOmOrgZTreeByOrgId(String orgId);

    List<OpusOmZtreeNode> getOpuOmOrgPosUserAsyncZTree(String id, String type);

    OpuOmOrg getOpuOmOrgById(String orgId);

    OpuOmOrg getTopOrgByCurOrgId(String orgId);

    ElementUiRsTreeNode getAllOpuOmOrgElementUITreeByOrgId(String orgId);

    List<ElementUiRsTreeNode> getOpusOmOrgAsyncElementUINode(String orgId);
}
