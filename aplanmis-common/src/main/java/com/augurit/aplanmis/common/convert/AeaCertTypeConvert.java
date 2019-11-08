package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaCertType;

public class AeaCertTypeConvert {

    public static ElementUiRsTreeNode convertCertType(AeaCertType certType) {

        if (certType != null) {
            ElementUiRsTreeNode certTypeNode = new ElementUiRsTreeNode();
            certTypeNode.setId(certType.getCertTypeId());
            certTypeNode.setLabel(certType.getTypeName());
            certTypeNode.setType("certType");
            certTypeNode.setData(certType);
            return certTypeNode;
        } else {
            return null;
        }
    }
}
