package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.data.exchange.constant.ItemConstant;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ItemBasicMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ItemBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/6
 */
@Service
@Transactional
public class ItemBasicServiceImpl implements ItemBasicService {

    @Autowired
    ItemBasicMapper itemBasicMapper;

    @Autowired
    AeaItemBasicAdminService aeaItemBasicAdminService;

    @Autowired
    AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Override
    public SpglDfxmsplcjdxxb getAeaParThemeVerByItemCodeAndThemeCode(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        SpglDfxmsplcjdxxb spglDfxmsplcjdxxb = itemBasicMapper.getAeaParThemeVerByItemCodeAndThemeCode(spglDfxmsplcjdsxxxb.getSpsxbm(), spglDfxmsplcjdsxxxb.getSplcbm());
        if(spglDfxmsplcjdxxb==null){
            //查询的标准事项的关联item.getSpsxbm(), item.getSplcbm()
            try {
                AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.getAeaHiIteminstById(spglDfxmsplcjdsxxxb.getDfsjzj());
                AeaItemBasic parentItem = itemBasicMapper.findParentItemByItemVerId(aeaHiIteminst.getItemVerId());
                spglDfxmsplcjdxxb = itemBasicMapper.getAeaParThemeVerByItemCodeAndThemeCode(spglDfxmsplcjdsxxxb.getSpsxbm(),parentItem.getItemCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return spglDfxmsplcjdxxb;
    }

    @Override
    public List<AeaItem> findCarryOutItemByCatalogItemId(String itemId) {
        return itemBasicMapper.findCarryOutItemByCatalogItemId(itemId);
    }

    @Override
    public Long findStageItemDueNumByItemIdAndStageId(String itemId, String stageId) {
        String timeLimit = itemBasicMapper.findStageItemDueNumByItemIdAndStageId(itemId, stageId);
        try {
            if (timeLimit != null) {
                return Long.valueOf(timeLimit);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long findCatalogItemDueNum(String itemId, String stageId) {
        List<AeaItem> carryOutItemList = itemBasicMapper.findCarryOutItemByCatalogItemId(itemId);
        Long CatalogTimeLimit = null;
        for (AeaItem item : carryOutItemList) {
            Long timeLimit = this.findStageItemDueNumByItemIdAndStageId(item.getItemId(), stageId);
            if (timeLimit != null) {
                if (CatalogTimeLimit == null) {
                    CatalogTimeLimit = timeLimit;
                } else if (timeLimit > CatalogTimeLimit) {
                    CatalogTimeLimit = timeLimit;
                }
            }
        }
        return CatalogTimeLimit;
    }

    @Override
    public Long findItemDueNumByStageItemId(String stageItemId) {
        AeaParStageItem aeaParStageItem = itemBasicMapper.findStageItemByStageItemId(stageItemId);
        Long timeLimit = null;
        if (aeaParStageItem != null) {
            if (ItemConstant.IS_OPTION_ITEM_YES.equals(aeaParStageItem.getIsOptionItem())) {
                try {
                    timeLimit = Long.valueOf(aeaParStageItem.getDueNum());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                if (ItemConstant.IS_CATALOG_NO.equals(aeaParStageItem.getIsCatalog())) {
                    //1.实施事项直接查询阶段下时限配置
                    timeLimit = this.findStageItemDueNumByItemIdAndStageId(aeaParStageItem.getItemId(), aeaParStageItem.getStageId());
                } else {
                    //2.标准事项查询实施事项的时限最大值
                    timeLimit = this.findCatalogItemDueNum(aeaParStageItem.getItemId(), aeaParStageItem.getStageId());
                }

            }
        }
        return timeLimit;
    }

    @Override
    public ActStoTimeruleInst getProcessinstTimeruleInstByIteminstId(String iteminstId, String rootOrgId) throws Exception {
        ActStoTimeruleInst processinstTimeruleInstByIteminstId = actStoTimeruleInstMapper.getProcessinstTimeruleInstByIteminstId(iteminstId, rootOrgId);
        return processinstTimeruleInstByIteminstId;
    }

}
