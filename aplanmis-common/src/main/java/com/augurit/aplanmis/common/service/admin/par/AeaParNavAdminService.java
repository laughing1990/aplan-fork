package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParNav;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 *
 * @date 2019/8/29 029 19:28
 * @desc
 **/
public interface AeaParNavAdminService {

    List<AeaParNav> listAeaParNavPage(AeaParNav aeaParNav);

    PageInfo<AeaParNav> listAeaParNavPage(AeaParNav aeaParNav, Page page);

    Long getMaxSortNo(String rootOrgId);

    AeaParNav getAeaParNavById(String navId, String rootOrgId);

    void insertAeaParNav(AeaParNav aeaParNav);

    void updateAeaParNav(AeaParNav aeaParNav);

    void deleteAeaParNav(String navId, String rootOrgId);

    void batchDelAeaParNavByIds(String[] navIds, String rootOrgId);

    void changIsActiveState(String navId, String rootOrgId);
}
