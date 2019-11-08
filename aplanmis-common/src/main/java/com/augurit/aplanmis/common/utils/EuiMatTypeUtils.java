package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.convert.AeaItemMatTypeConvert;
import com.augurit.aplanmis.common.domain.AeaItemMatType;

import java.util.ArrayList;
import java.util.List;


/**
 * 材料类型工具类
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class EuiMatTypeUtils {

    public static List<ElementUiRsTreeNode> buildTree(List<AeaItemMatType> matTypes) {

        List<ElementUiRsTreeNode> treeNodes = new ArrayList();
        List<ElementUiRsTreeNode> rootNodes = getRootNodes(matTypes);
        if (rootNodes != null && rootNodes.size() > 0) {
            for(ElementUiRsTreeNode rootNode : rootNodes) {
                buildChildNodes(rootNode, matTypes);
                treeNodes.add(rootNode);
            }
        }
        return treeNodes;
    }

    public static void buildChildNodes(ElementUiRsTreeNode node, List<AeaItemMatType> matTypes) {

        List<ElementUiRsTreeNode> children = getChildNodes(node, matTypes);
        if (children != null && children.size() > 0) {
            for(ElementUiRsTreeNode child : children) {
                buildChildNodes(child, matTypes);
            }
            node.setChildren(children);
        }
    }

    /**
     * 获取下级节点
     *
     * @param pnode
     * @param matTypes
     * @return
     */
    public static List<ElementUiRsTreeNode> getChildNodes(ElementUiRsTreeNode pnode, List<AeaItemMatType> matTypes) {

        List<ElementUiRsTreeNode> childNodes = new ArrayList();
        if(matTypes!=null && matTypes.size()>0){
            for(AeaItemMatType matType : matTypes) {
                if (StringUtils.isNotBlank(pnode.getId()) && pnode.getId().equals(matType.getParentTypeId())) {
                    ElementUiRsTreeNode nNode = AeaItemMatTypeConvert.convertMatType(matType);
                    childNodes.add(nNode);
                }
            }
        }
        return childNodes;
    }

    /**
     * 获取根节点
     *
     * @param allMatTypes
     * @return
     */
    public static List<ElementUiRsTreeNode> getRootNodes(List<AeaItemMatType> allMatTypes) {

        List<ElementUiRsTreeNode> rootNodes = new ArrayList<ElementUiRsTreeNode>();
        List<AeaItemMatType> firstMenuList = new ArrayList<AeaItemMatType>();
        if(allMatTypes!=null&&allMatTypes.size()>0){
            for(AeaItemMatType matType : allMatTypes){
                if (rootNode(matType)) {
                    firstMenuList.add(matType);
                    rootNodes.add(AeaItemMatTypeConvert.convertMatType(matType));
                }
            }
        }
        if (firstMenuList != null && firstMenuList.size() > 0) {
            allMatTypes.removeAll(firstMenuList);
        }
        return rootNodes;
    }

    /**
     * 判断是否根节点
     *
     * @param node
     * @return
     */
    public static boolean rootNode(AeaItemMatType node) {

        return StringUtils.isBlank(node.getParentTypeId());
    }
}

