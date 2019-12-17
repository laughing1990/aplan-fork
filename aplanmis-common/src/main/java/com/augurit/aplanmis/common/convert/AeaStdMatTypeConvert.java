package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaStdmatType;

/**
 * Demo class
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class AeaStdMatTypeConvert {

    public static ElementUiRsTreeNode convertMatType(AeaStdmatType matType) {

        if (matType != null) {
            ElementUiRsTreeNode matTypeNode = new ElementUiRsTreeNode();
            matTypeNode.setId(matType.getStdmatTypeId());
            matTypeNode.setLabel(matType.getTypeName());
            matTypeNode.setType("stdMatType");
            matTypeNode.setData(matType);
            return matTypeNode;
        } else {
            return null;
        }
    }
}
