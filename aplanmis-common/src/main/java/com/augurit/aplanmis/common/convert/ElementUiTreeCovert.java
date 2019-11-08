package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/9
 */
public interface ElementUiTreeCovert<E> {

    ElementUiRsTreeNode convertToElementUiNode(E e);

    ElementUiRsTreeNode convertToElementUiNode(E root, List<E> allChild);
}
