package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段事项前置检测表-Service服务调用接口类
 */
public interface AeaParFrontItemService {

    void saveAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void deleteAeaParFrontItemById(String id) throws Exception;

    PageInfo<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem, Page page) throws Exception;

    AeaParFrontItem getAeaParFrontItemById(String id) throws Exception;

    List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    PageInfo<AeaParFrontItem> listAeaParFrontItemVoByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception;

    Long getMaxSortNo(AeaParFrontItem aeaParFrontItem) throws Exception;

    AeaParFrontItem getAeaParFrontItemVoByFrontItemId(String frontItemId) throws Exception;

    List<AeaParFrontItem> listAeaParFrontItemByStageId(String stageId);

    void batchSaveAeaParFrontItem(String stageId,String itemVerIds)throws Exception;

    List<AeaParFrontItem> listAeaParFrontItemVoByNoPage(AeaParFrontItem aeaParFrontItem) throws Exception;

    void batchSaveFrontItem(String stageId, String[] itemVerIds, Long[] sortNos);

    void batchDelItemByStageId(String stageId, String rootOrgId);

    void changIsActive(String id, String rootOrgId);

}
