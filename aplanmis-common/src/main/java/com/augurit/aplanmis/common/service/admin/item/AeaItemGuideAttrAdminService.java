package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideAttr;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface AeaItemGuideAttrAdminService {

    void saveAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);

    void updateAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);

    void deleteAeaItemGuideAttrById(String id);

    PageInfo<AeaItemGuideAttr> listAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr, Page page);

    AeaItemGuideAttr getAeaItemGuideAttrById(String id);

    void batchDeleteItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);
}
