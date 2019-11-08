package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideConditions;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaItemGuideConditionsAdminService {

    void saveAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    void updateAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    void deleteAeaItemGuideConditionsById(String id);

    PageInfo<AeaItemGuideConditions> listAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions, Page page);

    AeaItemGuideConditions getAeaItemGuideConditionsById(String id);

    List<AeaItemGuideConditions> listAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    void batchDeleteGuideConditionsByItemVerId(String itemVerId, String rootOrgId);
}
