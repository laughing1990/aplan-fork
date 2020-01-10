package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/8
 */
public class OpuOmOrgConvert extends AbstractElementUiTreeCovert<OpuOmOrg>{

    public static String TYPE = "REGION";

    @Override
    String getParentId(OpuOmOrg opuOmOrg) {
        return opuOmOrg.getParentOrgId();
    }

    @Override
    public ElementUiRsTreeNode convertToElementUiNode(OpuOmOrg org) {
        ElementUiRsTreeNode node = new ElementUiRsTreeNode();
        node.setId(org.getOrgId());
        node.setLabel(org.getOrgName());
        node.setState(ElementUiRsTreeNode.STATE_OPEN);
        node.setType(org.getOpusOmType().getCode());
        //node.setData(org);
        return node;
    }
}
