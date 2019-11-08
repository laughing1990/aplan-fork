package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaItemMatType;

/**
 * Demo class
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class AeaItemMatTypeConvert {

    public static ElementUiRsTreeNode convertMatType(AeaItemMatType matType) {

        if (matType != null) {
            ElementUiRsTreeNode matTypeNode = new ElementUiRsTreeNode();
            matTypeNode.setId(matType.getMatTypeId());
            matTypeNode.setLabel(matType.getTypeName());
            matTypeNode.setType("matType");
            matTypeNode.setData(matType);
            return matTypeNode;
        } else {
            return null;
        }
    }
}
