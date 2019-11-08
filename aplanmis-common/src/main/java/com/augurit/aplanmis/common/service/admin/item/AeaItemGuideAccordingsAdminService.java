package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideAccordings;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaItemGuideAccordingsAdminService {

    void saveAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);

    void updateAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);

    void deleteAeaItemGuideAccordingsById(String id);

    PageInfo<AeaItemGuideAccordings> listAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings, Page page);

    AeaItemGuideAccordings getAeaItemGuideAccordingsById(String id);

    List<AeaItemGuideAccordings> listAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);

    void batchDeleteAeaItemGuideAccordingsByItemVerId(String itemVerId, String rootOrgId);
}