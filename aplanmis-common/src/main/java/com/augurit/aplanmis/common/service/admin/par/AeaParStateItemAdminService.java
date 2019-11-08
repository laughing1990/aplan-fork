package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStateItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 情形与事项关联定义表-Service服务调用接口类
 */
public interface AeaParStateItemAdminService {

    void saveAeaParStateItem(AeaParStateItem aeaParStateItem);

    void saveStateItemRel(String parStateId, String stageItemId);

    void updateAeaParStateItem(AeaParStateItem aeaParStateItem);

    void deleteAeaParStateItemById(String id);

    PageInfo<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem, Page page);

    AeaParStateItem getAeaParStateItemById(String id);

    List<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem);

    List<AeaParStateItem> listStateItemByStateId(String parStateId);

    Long getMaxSortNo();

    void saveAeaParStateItemByStateIds(String stageItemId, String stateIds);

    Map<String, Object> getStageMatList(String stageId, String parentId, String level) throws Exception;

    Map<String, Object> getRemoveStateIds(String stateId, String stageId) throws Exception;

    void deleteStateItemByStageItemId(String stageItemId);

    void saveAeaParStateItemByStageItemIds(String stateId, String stageItemIds, String stageId);

    void saveAeaParStateItemByStageMatItemIds(String stateId,
                                              String stageItemIds,
                                              String matCertId,
                                              String stageId,
                                              String fileType);

    void saveAeaParStateMatItemByStageItemIds(String stateId, String stageItemIds);

}
