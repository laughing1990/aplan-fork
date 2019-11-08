package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.convert.AeaCertTypeConvert;
import com.augurit.aplanmis.common.domain.AeaCertType;

import java.util.ArrayList;
import java.util.List;


/**
 * 证照类型工具类
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
public class EuiCertTypeUtils {

    public static List<ElementUiRsTreeNode> buildTree(List<AeaCertType> certTypes) {

        List<ElementUiRsTreeNode> treeNodes = new ArrayList();
        List<ElementUiRsTreeNode> rootNodes = getRootNodes(certTypes);
        if (rootNodes != null && rootNodes.size() > 0) {
            for(ElementUiRsTreeNode rootNode : rootNodes) {
                buildChildNodes(rootNode, certTypes);
                treeNodes.add(rootNode);
            }
        }
        return treeNodes;
    }

    public static void buildChildNodes(ElementUiRsTreeNode node, List<AeaCertType>certTypes) {

        List<ElementUiRsTreeNode> children = getChildNodes(node,certTypes);
        if (children != null && children.size() > 0) {
            for(ElementUiRsTreeNode child : children) {
                buildChildNodes(child, certTypes);
            }
            node.setChildren(children);
        }
    }

    /**
     * 获取下级节点
     *
     * @param pnode
     * @param certTypes
     * @return
     */
    public static List<ElementUiRsTreeNode> getChildNodes(ElementUiRsTreeNode pnode, List<AeaCertType> certTypes) {

        List<ElementUiRsTreeNode> childNodes = new ArrayList();
        if(certTypes!=null && certTypes.size()>0){
            for(AeaCertType certType : certTypes) {
                if (StringUtils.isNotBlank(pnode.getId()) && pnode.getId().equals(certType.getParentTypeId())) {
                    ElementUiRsTreeNode nNode = AeaCertTypeConvert.convertCertType(certType);
                    childNodes.add(nNode);
                }
            }
        }
        return childNodes;
    }

    /**
     * 获取根节点
     *
     * @param allCertTypes
     * @return
     */
    public static List<ElementUiRsTreeNode> getRootNodes(List<AeaCertType> allCertTypes) {

        List<ElementUiRsTreeNode> rootNodes = new ArrayList<ElementUiRsTreeNode>();
        List<AeaCertType> firstMenuList = new ArrayList<AeaCertType>();
        if(allCertTypes!=null&&allCertTypes.size()>0){
            for(AeaCertType certType : allCertTypes){
                if (rootNode(certType)) {
                    firstMenuList.add(certType);
                    rootNodes.add(AeaCertTypeConvert.convertCertType(certType));
                }
            }
        }
        if (firstMenuList != null && firstMenuList.size() > 0) {
            allCertTypes.removeAll(firstMenuList);
        }
        return rootNodes;
    }

    /**
     * 判断是否根节点
     *
     * @param node
     * @return
     */
    public static boolean rootNode(AeaCertType node) {

        return StringUtils.isBlank(node.getParentTypeId());
    }
}

