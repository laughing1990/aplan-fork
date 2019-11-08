package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/18
 */
public class BusinessZtreeNode<T> extends ZtreeNode {

    protected List<T> children;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
