package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/9
 */
public abstract class AbstractElementUiTreeCovert<E> implements ElementUiTreeCovert<E>{

    abstract String getParentId(E e);

    @Override
    public ElementUiRsTreeNode convertToElementUiNode(E root, List<E> allChild) {

        ElementUiRsTreeNode rootNode = this.convertToElementUiNode(root);
        addChildNodes(rootNode,allChild);
        return rootNode;
    }

    protected  void addChildNodes(ElementUiRsTreeNode rootNode, List<E> allChild){

        List<ElementUiRsTreeNode> childNodes = new ArrayList<>();
        Iterator<E> iterator = allChild.iterator();
        while (iterator.hasNext()) {
            E child = iterator.next();
            if(rootNode.getId().equals(this.getParentId(child))){
                ElementUiRsTreeNode childNode = this.convertToElementUiNode(child);
                childNodes.add(childNode);
                iterator.remove();
            }
        }
        rootNode.setChildren(childNodes);
        for(ElementUiRsTreeNode r : childNodes){
            addChildNodes(r,allChild);
        }
    }
}
