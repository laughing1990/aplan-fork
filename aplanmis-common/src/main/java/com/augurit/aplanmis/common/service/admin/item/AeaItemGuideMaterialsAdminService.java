package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface AeaItemGuideMaterialsAdminService {

    void saveAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    void updateAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    void deleteAeaItemGuideMaterialsById(String id);

    PageInfo<AeaItemGuideMaterials> listAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials, Page page);

    AeaItemGuideMaterials getAeaItemGuideMaterialsById(String id);

    List<AeaItemGuideMaterials> listAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    void batchDeleteGuideMaterialsByItemVerId(String itemVerId, String rootOrgId);
}
