package com.augurit.aplanmis.common.utils;

import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportData;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.constants.MindConst;

import java.util.List;


/**
 * @author tiantian
 * @date 2019/2/20
 */
public class MindViewUtils {

    public static void checkMindExportObj(MindExportObj mindExportObj) {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();
            if (mindExportData != null) {
                if (mindExportObj.getChildren() != null) {
                    for (MindExportObj child : mindExportObj.getChildren()) {
                        if (child != null && child.getData() != null) {
                            if (MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag())) {
                                child.getData().setOperatorTag(MindConst.MIND_NODE_OPERATOR_TAG_NEW);
                            }
                            checkMindExportObj(child);
                        }
                    }
                }
            }
        }
    }

    public static MindBaseNode[] listToArray(List<? extends MindBaseNode> list) {
        if (list != null && !list.isEmpty()) {
            MindBaseNode[] arr = new MindBaseNode[list.size()];

            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }

            return arr;
        }

        return null;
    }
}
