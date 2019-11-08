package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideMatconditions;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface AeaItemGuideMatconditionsAdminService {

    void saveAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

    void updateAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

    void deleteAeaItemGuideMatconditionsById(String id);

    void batchDeleteGuideMatconditionsByItemVerId(String itemVerId, String rootOrgId);

    PageInfo<AeaItemGuideMatconditions> listAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions, Page page);

    AeaItemGuideMatconditions getAeaItemGuideMatconditionsById(String id);

    List<AeaItemGuideMatconditions> listAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

}
