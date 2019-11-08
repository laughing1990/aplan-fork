package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * -Service服务调用接口类
 */
public interface AeaParStageItemAdminService {

    void saveAeaParStageItem(AeaParStageItem aeaParStageItem);

    void updateAeaParStageItem(AeaParStageItem aeaParStageItem);

    void deleteAeaParStageItemById(String id);

    List<AeaParStageItem> listAeaStageItemCondition(AeaParStageItem aeaParStageItem);

    EasyuiPageInfo<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem, Page page);

    EasyuiPageInfo<AeaParStageItem> listStageAllItem(AeaParStageItem aeaParStageItem, Page page);

    EasyuiPageInfo<AeaParStageItem> listAeaParStageMatItem(AeaParStageItem aeaParStageItem, Page page);

    EasyuiPageInfo<AeaParStageItem> listAeaParStateItem(AeaParStageItem aeaParStageItem, Page page);

    AeaParStageItem getAeaParStageItemById(String id);

    List<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem);

    List<AeaParStageItem> listAeaStageItemByStageId(String stageId, String isOptionItem);

    List<AeaParStageItem> listAeaStageItemInfoByStageId(String stageId);

    void batchSaveStageItem(String stageId, String[] itemIds, String[] sortNos, String isOptionItem);

    List<ZtreeNode> listStageItemTreeByStageId(String stageId);

    List<ZtreeNode> getStageItemTreeByStageId(String stageId);

    EasyuiPageInfo<AeaParStageItem> listAeaParStageItemWhichBindState(AeaParStageItem aeaParStageItem, Page page);

    List<AeaParStageItem> listAeaParStageItemWithoutBindState(String stageId);

    List<AeaParStageItem> listStageItemByInId(String StageId, String isOptionItem, String inId);

    void batchDeleteStageItemByStageId(String stageId);

    /**
     * 删除阶段下某一类型的事项
     *
     * @param stageId
     * @param isOptionItem
     */
    void batchDeleteOneTypeStageItem(String stageId, String isOptionItem);

    /**
     * 由于事项版本变更批量更新阶段事项
     *
     * @param themeVerId
     * @param itemId
     * @param itemVerId
     * @param newItemVerId
     * @param rootOrgId
     *
     */
    void batchUpdateStageItemByItemVerChange(String themeVerId, String itemId, String itemVerId, String newItemVerId, String rootOrgId);

    /**
     * 出重事项
     *
     * @param stageId
     * @param isOptionItem
     */
    void handleStageItemsToOnly(String stageId, String isOptionItem);

    /**
     * 通过阶段ID获取最大排序号
     *
     * @param stageId
     * @return
     */
    Long getMaxSortNoByStageId(String stageId);

    List<AeaParStageItem> checkBeforeSeriesFlow(String projInfoId, String itemVerId, String rootOrgId);

}
