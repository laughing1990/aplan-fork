package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.vo.AeaItemFrontCondStateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 事项前提条件表-Service服务调用接口类
 */
public interface AeaItemCondAdminService {

    void saveAeaItemCond(AeaItemCond aeaItemCond);

    void updateAeaItemCond(AeaItemCond aeaItemCond);

    void deleteAeaItemCondById(String id);

    PageInfo<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond, Page page);

    List<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond);

    AeaItemCond getAeaItemCondById(String id);

    Long getMaxSortNo(String rootOrgId);

    void batchDeleteAeaItemCond(String[] ids);

    void changIsActiveState(String id);

    void saveAeaItemCondState(List<AeaItemFrontCondStateVo> stateList, String itemId);

    MindBaseNode listAeaItemCondMindView(String itemId);

    void saveOrUpdateAeaItemCondMindView(MindExportObj mindExportObj);
}
