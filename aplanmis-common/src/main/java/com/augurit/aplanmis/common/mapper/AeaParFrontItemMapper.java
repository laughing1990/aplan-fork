package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段事项前置检测表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFrontItemMapper {

    void insertAeaParFrontItem(AeaParFrontItem aeaParFrontItem);

    void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem);

    void deleteAeaParFrontItem(@Param("id") String id);

    void batchDelAeaItemFrontByIds(@Param("ids") List<String> ids);

    List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem);

    AeaParFrontItem getAeaParFrontItemById(@Param("id") String id);

    List<AeaParFrontItem> listAeaParFrontItemVo(AeaParFrontItem aeaParFrontItem);

    Long getMaxSortNo(AeaParFrontItem aeaParFrontItem);

    AeaParFrontItem getAeaParFrontItemVoByFrontItemId(@Param("frontItemId") String frontItemId);

    List<AeaParFrontItem> listAeaParFrontItemByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void batchDelItemByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
