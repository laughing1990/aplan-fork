package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 事项的前置检查事项-Service服务调用接口类
*/
public interface AeaItemFrontItemAdminService {

     void saveAeaItemFront(AeaItemFrontItem aeaItemFront);

     void updateAeaItemFront(AeaItemFrontItem aeaItemFront);

     void deleteAeaItemFrontById(String id);

     PageInfo<AeaItemFrontItem> listAeaItemFront(AeaItemFrontItem aeaItemFront, Page page);

     AeaItemFrontItem getAeaItemFrontById(String id);

     List<AeaItemFrontItem> listAeaItemFront(AeaItemFrontItem aeaItemFront);

     List<AeaItemFrontItem> listItemsByItemVerId(String itemVerId);

     void batchSaveFrontItem(String itemVerId, String[] frontItemVerIds, String[] sortNos);

     void batchDelItemByItemVerId(String itemVerId);
}
