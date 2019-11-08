package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.convert.ElementUiTreeCovert;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题表-Service服务调用接口类
 */
public interface AeaParThemeAdminService {

    void saveAeaParTheme(AeaParTheme aeaParTheme);

    void updateAeaParTheme(AeaParTheme aeaParTheme);

    void deleteAeaParThemeById(String id);

    PageInfo<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme, Page page);

    AeaParTheme getAeaParThemeById(String id);

    List<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme);

    Long getMaxSortNo(String rootOrgId);

    List<ZtreeNode> gtreeTheme(AeaParTheme theme);

    List<ElementUiRsTreeNode> gtreeThemeForEUi(AeaParTheme theme);
}
