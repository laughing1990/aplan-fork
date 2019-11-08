package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStateItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 情形与事项关联定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStateItemMapper {

     void insertAeaParStateItem(AeaParStateItem aeaParStateItem) ;

     void updateAeaParStateItem(AeaParStateItem aeaParStateItem) ;

     void deleteAeaParStateItem(@Param("id") String id) ;

     List<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem) ;

     AeaParStateItem getAeaParStateItemById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

     Long getMaxSortNo(@Param("rootOrgId")String rootOrgId);

     List<AeaParStateItem> listStageStateItemByItemAndOption(@Param("stageId") String stageId,
                                                            @Param("isOptionItem")String isOptionItem,
                                                            @Param("itemId") String itemId,
                                                            @Param("itemVerId") String itemVerId,
                                                            @Param("rootOrgId") String rootOrgId);

     List<AeaParStateItem> listStageStateItemByStateAndOption(@Param("stageId") String stageId,
                                                             @Param("isOptionItem")String isOptionItem,
                                                             @Param("parStateId") String parStateId,
                                                             @Param("rootOrgId") String rootOrgId);

     List<AeaParStateItem> listStageStateItem(@Param("parStateId") String parStateId,
                                             @Param("stageId") String stageId,
                                             @Param("rootOrgId") String rootOrgId);

     void deleteAeaParStateItemByStateId(@Param("parStateId") String parStateId,
                                        @Param("rootOrgId")String rootOrgId);

     List<AeaParStateItem> listStateItemByStateId(@Param("parStateId") String parStateId,
                                                 @Param("rootOrgId") String rootOrgId);

     List<AeaParStateItem> listStateItemByParentStateId(@Param("parentStateId") String parentStateId,
                                                       @Param("rootOrgId") String rootOrgId);
}
