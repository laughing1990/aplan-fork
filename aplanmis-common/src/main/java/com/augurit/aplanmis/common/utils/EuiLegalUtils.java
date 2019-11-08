package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.convert.AeaServiceLegalConvert;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;

import java.util.ArrayList;
import java.util.List;


/**
 * 法律法规工具类
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class EuiLegalUtils {

    public static List<ElementUiRsTreeNode> buildTree(List<AeaServiceLegal> legals) {

        List<ElementUiRsTreeNode> treeNodes = new ArrayList();
        List<ElementUiRsTreeNode> rootNodes = getRootNodes(legals);
        if (rootNodes != null && rootNodes.size() > 0) {
            for(ElementUiRsTreeNode rootNode : rootNodes) {
                buildChildNodes(rootNode, legals);
                treeNodes.add(rootNode);
            }
        }
        return treeNodes;
    }

    public static void buildChildNodes(ElementUiRsTreeNode node, List<AeaServiceLegal>legals) {

        List<ElementUiRsTreeNode> children = getChildNodes(node, legals);
        if (children != null && children.size() > 0) {
            List<ElementUiRsTreeNode> needHandleChildren = new ArrayList<>();
            for(ElementUiRsTreeNode child : children) {
                if(child.getType().equals("legal")){
                    needHandleChildren.add(child);
                }
            }
            if (needHandleChildren != null && needHandleChildren.size() > 0) {
                for(ElementUiRsTreeNode needHandleChild : needHandleChildren) {
                    buildChildNodes(needHandleChild, legals);
                }
            }
            node.setChildren(children);
        }
    }

    /**
     * 获取下级节点
     *
     * @param pnode
     * @param legals
     * @return
     */
    public static List<ElementUiRsTreeNode> getChildNodes(ElementUiRsTreeNode pnode, List<AeaServiceLegal> legals) {

        List<ElementUiRsTreeNode> childNodes = new ArrayList();
        if(legals!=null && legals.size()>0){
            for(AeaServiceLegal legal : legals) {
                if (StringUtils.isNotBlank(pnode.getId()) && pnode.getId().equals(legal.getParentLegalId())) {
                    // 处理法律法规
                    childNodes.add(AeaServiceLegalConvert.convertLegal(legal));
                }
            }
        }
        // 处理条款
        AeaServiceLegal plegal = (AeaServiceLegal)pnode.getData();
        List<ElementUiRsTreeNode> clauseNodes = AeaServiceLegalConvert.convertLegalClauses(plegal.getLegalClauses());
        childNodes.addAll(clauseNodes);
        return childNodes;
    }

    /**
     * 获取根节点
     *
     * @param allLegals
     * @return
     */
    public static List<ElementUiRsTreeNode> getRootNodes(List<AeaServiceLegal> allLegals) {

        List<ElementUiRsTreeNode> rootNodes = new ArrayList<ElementUiRsTreeNode>();
        List<AeaServiceLegal> firstLegalList = new ArrayList<AeaServiceLegal>();
        if(allLegals!=null&&allLegals.size()>0){
            for(AeaServiceLegal legal : allLegals){
                if (rootNode(legal)) {
                    firstLegalList.add(legal);
                    rootNodes.add(AeaServiceLegalConvert.convertLegal(legal));
                }
            }
        }
        if (firstLegalList != null && firstLegalList.size() > 0) {
            allLegals.removeAll(firstLegalList);
        }
        return rootNodes;
    }

    /**
     * 判断是否根节点
     *
     * @param node
     * @return
     */
    public static boolean rootNode(AeaServiceLegal node) {

        return StringUtils.isBlank(node.getParentLegalId());
    }
}

