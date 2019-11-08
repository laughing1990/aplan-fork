package com.augurit.aplanmis.data.exchange.service.aplanmis;


import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdxxb;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/6
 */
public interface ItemBasicService {

    /**
     * 查询事项所在主题版本和阶段序号
     *
     * @return
     */
    SpglDfxmsplcjdxxb getAeaParThemeVerByItemCodeAndThemeCode(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);


    /**
     * 查询实施事项
     *
     * @param itemId
     * @return
     */
    List<AeaItem> findCarryOutItemByCatalogItemId(String itemId);

    /**
     * 查询事项在阶段下办理时限
     *
     * @param itemId  事项ID
     * @param stageId 阶段ID
     * @return
     */
    Long findStageItemDueNumByItemIdAndStageId(String itemId, String stageId);

    /**
     * 查询标准事项在阶段下办理时限
     *
     * @param itemId
     * @param stageId
     * @return
     */
    Long findCatalogItemDueNum(String itemId, String stageId);

    /**
     * 查找阶段下事项的办理时限
     *
     * @param stageItemId
     * @return
     */
    Long findItemDueNumByStageItemId(String stageItemId);

    ActStoTimeruleInst getProcessinstTimeruleInstByIteminstId(String iteminstId, String rootOrgId) throws Exception;

}
