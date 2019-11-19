package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 事项的前置检查事项-Service服务调用接口类
*/
public interface AeaItemFrontItemAdminService {

     List<AeaItemFrontItem> listItemsByItemVerId(String itemVerId);

     void batchSaveFrontItem(String itemVerId, String[] frontItemVerIds, String[] sortNos);

     void batchDelItemByItemVerId(String itemVerId, String rootOrgId);

     void saveAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem);

     void updateAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem);

     void deleteAeaItemFrontItemById(String id);

     PageInfo<AeaItemFrontItem> listAeaItemFrontItemByPage(AeaItemFrontItem aeaItemFrontItem, Page page);

     List<AeaItemFrontItem> listAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem);

     Long getMaxSortNo(AeaItemFrontItem aeaItemFrontItem);

     AeaItemFrontItem getAeaItemFrontItemByFrontItemId(String frontItemId);

     void batchSaveAeaItemFrontItem(String itemVerId,String frontCkItemVerIds)throws Exception;

     void changIsActive(String id, String rootOrgId);
}
