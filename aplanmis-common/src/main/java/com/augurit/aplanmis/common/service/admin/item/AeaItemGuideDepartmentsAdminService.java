package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideDepartments;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * -Service服务调用接口类
 */
public interface AeaItemGuideDepartmentsAdminService {

    void saveAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

    void updateAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

    void deleteAeaItemGuideDepartmentsById(String id);

    void batchDeleteGuideDepartmentsByItemVerId(String itemVerId, String rootOrgId);

    PageInfo<AeaItemGuideDepartments> listAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments, Page page);

    AeaItemGuideDepartments getAeaItemGuideDepartmentsById(String id);

    List<AeaItemGuideDepartments> listAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

}
