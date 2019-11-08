package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaParShareMat;
import com.github.pagehelper.Page;

import java.util.List;

public interface AeaParShareMatService {

    EasyuiPageInfo<AeaItemInout> listOutItemMat(AeaItemInout aeaItemInout, Page page);
    EasyuiPageInfo<AeaItemInout> listInItemMat(AeaItemInout aeaItemInout, Page page);
    List<ZtreeNode> listOutItemTreeByThemeVerId(String themeVerId);
    List<ZtreeNode> listInItemTreeByThemeVerId(String themeVerId);
    void saveSharemat(AeaParShareMat aeaParShareMat);
    List<AeaParShareMat> inOutCheckboxList(String themeVerId, String outInoutId);
}
