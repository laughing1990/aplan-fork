package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 事项的前置检查事项-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaItemFrontItemMapper {

     void insertAeaItemFront(AeaItemFrontItem aeaItemFrontItem);

     void updateAeaItemFront(AeaItemFrontItem aeaItemFrontItem);

     void deleteAeaItemFront(@Param("id") String id);

     void batchDelAeaItemFront(@Param("ids") List<String> ids);

     List<AeaItemFrontItem> listAeaItemFront(AeaItemFrontItem aeaItemFrontItem);

     AeaItemFrontItem getAeaItemFrontById(@Param("id") String id);

     List<AeaItemFrontItem> listItemsByItemVerId(@Param("itemVerId")String itemVerId, @Param("rootOrgId")String rootOrgId);

     void batchDelItemByItemVerId(@Param("itemVerId")String itemVerId, @Param("rootOrgId")String rootOrgId);

     /**
      * 通过前置事项版本id获取未过时事项版本数据
      *
      * @param frontCkItemVerId
      * @param rootOrgId
      * @return
      */
     List<AeaItemFrontItem> listNoDeprecatedItemFront(@Param("frontCkItemVerId")String frontCkItemVerId, @Param("rootOrgId")String rootOrgId);

     List<AeaItemFrontItem> listAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem);

     Long getMaxSortNo(AeaItemFrontItem aeaItemFrontItem);

     AeaItemFrontItem getAeaItemFrontItemByFrontItemId(@Param("frontItemId") String frontItemId);

     void changIsActive(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
