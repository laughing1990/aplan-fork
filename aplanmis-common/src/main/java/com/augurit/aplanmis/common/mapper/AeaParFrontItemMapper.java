package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.vo.AeaParFrontItemVo;
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

    void insertAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void updateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    void deleteAeaParFrontItem(@Param("id") String id) throws Exception;

    List<AeaParFrontItem> listAeaParFrontItem(AeaParFrontItem aeaParFrontItem) throws Exception;

    AeaParFrontItem getAeaParFrontItemById(@Param("id") String id) throws Exception;

    List<AeaParFrontItemVo> listAeaParFrontItemVo(AeaParFrontItem aeaParFrontItem) throws Exception;

    Long getMaxSortNo(AeaParFrontItem aeaParFrontItem) throws Exception;

    AeaParFrontItemVo getAeaParFrontItemVoByFrontItemId(@Param("frontItemId") String frontItemId) throws Exception;

    List<AeaParFrontItemVo> listAeaParFrontItemByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);
}
