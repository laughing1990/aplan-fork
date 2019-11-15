package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 事项与扩展表单关联表-Service服务调用接口类
 */
public interface AeaItemPartformAdminService {

    PageInfo<AeaItemPartform> listPartFormNoSelectFormByPage(AeaItemPartform partform, Page page);

    void batchDelItemPartform(String[] ids);

    Long getMaxSortNo(String itemVerId);

    void saveAeaItemPartform(AeaItemPartform aeaItemPartform);

    void updateAeaItemPartform(AeaItemPartform aeaItemPartform);

    void deleteAeaItemPartformById(String id);

    PageInfo<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform, Page page);

    List<AeaItemPartform> listAeaItemPartformNoPage(AeaItemPartform aeaItemPartform);

    AeaItemPartform getAeaItemPartformById(String id);

    List<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform);

    void createAndUpdateDevForm(String formCode, String formName, String formLoadUrl, String formId, String itemPartformId) throws Exception;
}
