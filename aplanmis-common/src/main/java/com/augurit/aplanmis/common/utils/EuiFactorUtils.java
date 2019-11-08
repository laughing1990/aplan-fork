package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFactor;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题情形工具类
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class EuiFactorUtils {

    public static List<AeaParFactor> buildTree(List<AeaParFactor> factors) {

        List<AeaParFactor> treeNodes = new ArrayList();
        List<AeaParFactor> rootNodes = getRootNodes(factors);
        if (rootNodes != null && rootNodes.size() > 0) {
            for(AeaParFactor rootNode : rootNodes) {
                buildChildNodes(rootNode, factors);
                treeNodes.add(rootNode);
            }
        }
        return treeNodes;
    }

    public static void buildChildNodes(AeaParFactor node, List<AeaParFactor> factors) {

        List<AeaParFactor> children = getChildNodes(node, factors);
        if (children != null && children.size() > 0) {
            for(AeaParFactor child : children) {
                buildChildNodes(child, factors);
            }
            node.setHasChildren(true);
            node.setChildren(children);
        }
    }

    /**
     * 获取下级节点
     *
     * @param pnode
     * @param factors
     * @return
     */
    public static List<AeaParFactor> getChildNodes(AeaParFactor pnode, List<AeaParFactor> factors) {

        List<AeaParFactor> childNodes = new ArrayList();
        if(factors!=null && factors.size()>0){
            for(AeaParFactor factor : factors) {
                if (StringUtils.isNotBlank(pnode.getFactorId()) && pnode.getFactorId().equals(factor.getParentFactorId())) {
                    childNodes.add(factor);
                }
            }
        }
        return childNodes;
    }

    /**
     * 获取根节点
     *
     * @param factors
     * @return
     */
    public static List<AeaParFactor> getRootNodes(List<AeaParFactor> factors) {

        List<AeaParFactor> rootNodes = new ArrayList<AeaParFactor>();
        List<AeaParFactor> firstFactorList = new ArrayList<AeaParFactor>();
        if(factors!=null&&factors.size()>0){
            for(AeaParFactor factor : factors){
                if (rootNode(factor)) {
                    firstFactorList.add(factor);
                    rootNodes.add(factor);
                }
            }
        }
        if (firstFactorList != null && firstFactorList.size() > 0) {
            factors.removeAll(firstFactorList);
        }
        return rootNodes;
    }

    /**
     * 判断是否根节点
     *
     * @param node
     * @return
     */
    public static boolean rootNode(AeaParFactor node) {

        return StringUtils.isBlank(node.getParentFactorId());
    }

    public static ElementUiRsTreeNode convertFactor(AeaParFactor factor){

        ElementUiRsTreeNode node = new ElementUiRsTreeNode();
        node.setId(factor.getFactorId());
        node.setLabel(factor.getFactorName());
        node.setType("factor");
        node.setData(factor);
        return node;
    }

    public static List<ElementUiRsTreeNode> convertFactors(List<AeaParFactor> factors){

        List<ElementUiRsTreeNode> allNodes = new ArrayList<>();
        if(factors!=null&&factors.size()>0){
            for(AeaParFactor factor:factors){
                allNodes.add(convertFactor(factor));
            }
        }
        return allNodes;
    }
}
