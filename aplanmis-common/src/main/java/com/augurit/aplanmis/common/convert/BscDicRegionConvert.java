package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;

import java.io.Serializable;

/**
 * @author yinlf
 * @Date 2019/8/8
 */
public class BscDicRegionConvert extends AbstractElementUiTreeCovert<BscDicRegion> implements Serializable {

    public static String TYPE = "REGION";

    @Override
    String getParentId(BscDicRegion region) {
        return region.getParentRegionId();
    }

    @Override
    public ElementUiRsTreeNode convertToElementUiNode(BscDicRegion region) {
        ElementUiRsTreeNode node = new ElementUiRsTreeNode();
        node.setId(region.getId());
        node.setLabel(region.getRegionName());
        node.setState(ElementUiRsTreeNode.STATE_OPEN);
        node.setType(TYPE);
        node.setData(region);
        return node;
    }

}
