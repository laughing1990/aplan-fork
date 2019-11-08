package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaParFactor;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/30 030 19:31
 * @desc
 **/
public interface AeaParFactorAdminService {

    List<AeaParFactor> listAeaParFactor(AeaParFactor aeaParFactor);

    PageInfo<AeaParFactor> listAeaParFactorPage(AeaParFactor aeaParFactor, Page page);

    Long getMaxSortNo(String navId, String rootOrgId);

    AeaParFactor getAeaParFactorWithThemeById(String factorId, String rootOrgId) throws Exception;

    void deleteAeaParFactorById(String factorId, String rootOrgId);

    void batchDelAeaParFactorByIds(String[] factorId, String rootOrgId);

    void deleteAeaParFactorByNavId(String navId, String rootOrgId);

    void saveAeaParFactor(AeaParFactor aeaParFactor);

    void updateAeaParFactor(AeaParFactor aeaParFactor);

    void unbindTheme(String factorId);

    List<AeaParFactor> gtreeAeaParFactor(AeaParFactor aeaParFactor);
}
