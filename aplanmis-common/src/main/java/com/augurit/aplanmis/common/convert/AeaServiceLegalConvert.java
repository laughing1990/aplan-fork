package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;

import java.util.ArrayList;
import java.util.List;

public class AeaServiceLegalConvert {

    /**
     * 转换法律法规
     *
     * @param legal
     * @return
     */
    public static ElementUiRsTreeNode convertLegal(AeaServiceLegal legal) {

        if (legal != null) {
            ElementUiRsTreeNode legalNode = new ElementUiRsTreeNode();
            legalNode.setId(legal.getLegalId());
            legalNode.setLabel(legal.getLegalName() + "【法律法规】");
            legalNode.setType("legal");
            legalNode.setData(legal);
            return legalNode;
        }
        return null;
    }

    public static List<ElementUiRsTreeNode> convertLegals(List<AeaServiceLegal> legals) {

        List<ElementUiRsTreeNode> allNodes = new ArrayList<>();
        if (legals != null && legals.size()>0) {
            for(AeaServiceLegal legal:legals){
                allNodes.add(convertLegal(legal));
            }
        }
        return allNodes;
    }

    /**
     * 转换法律条款
     *
     * @param clause
     * @return
     */
    public static ElementUiRsTreeNode convertLegalClause(AeaServiceLegalClause clause) {

        if (clause != null) {
            ElementUiRsTreeNode legalNode = new ElementUiRsTreeNode();
            legalNode.setId(clause.getLegalClauseId());
            legalNode.setLabel(clause.getClauseTitle() + "【条款】");
            legalNode.setType("clause");
            legalNode.setData(clause);
            return legalNode;
        }
        return null;
    }

    public static List<ElementUiRsTreeNode> convertLegalClauses(List<AeaServiceLegalClause> clauses) {

        List<ElementUiRsTreeNode> allNodes = new ArrayList<>();
        if (clauses != null && clauses.size()>0) {
            for(AeaServiceLegalClause legalClause : clauses){
                allNodes.add(convertLegalClause(legalClause));
            }
        }
        return allNodes;
    }
}
