package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdxxb;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/6
 */
@Mapper
@Repository
public interface ItemBasicMapper {
    /**
     * 查询实施事项
     *
     * @param itemId 标准事项ID
     * @return
     */
    List<AeaItem> findCarryOutItemByCatalogItemId(@Param("itemId") String itemId);

    /**
     * 查询事项在阶段下办理时限
     *
     * @param itemId  事项ID
     * @param stageId 阶段ID
     * @return
     */
    String findStageItemDueNumByItemIdAndStageId(@Param("itemId") String itemId, @Param("stageId") String stageId);

    /**
     * 查询实施阶段事项关联信息
     *
     * @param stageItemId
     * @return
     */
    AeaParStageItem findStageItemByStageItemId(@Param("stageItemId") String stageItemId);

    SpglDfxmsplcjdxxb getAeaParThemeVerByItemCodeAndThemeCode(@Param("itemCode") String itemCode, @Param("themeCode") String themeCode);

    AeaItemBasic findParentItemByItemVerId(@Param("itemVerId") String itemVerId);
}
