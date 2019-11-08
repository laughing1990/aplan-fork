package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideSpecials;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 特殊程序-Service服务调用接口类
 */
public interface AeaItemGuideSpecialsAdminService {

    void saveAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials);

    void updateAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials);

    void deleteAeaItemGuideSpecialsById(String id);

    void batchDeleteGuideSpecialsByItemVerId(String itemVerId, String rootOrgId);

    PageInfo<AeaItemGuideSpecials> listAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials, Page page);

    AeaItemGuideSpecials getAeaItemGuideSpecialsById(String id);

    List<AeaItemGuideSpecials> listAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials);

}
