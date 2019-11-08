package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemMapper {

    void insertAeaItem(AeaItem aeaItem);

    void updateAeaItem(AeaItem aeaItem);

    List<AeaItem> listAeaItem(AeaItem aeaItem);

    AeaItem getAeaItemById(@Param("id") String id);

    List<AeaItem> getAeaItemByParentItemId(@Param("parentItemId")String parentItemId);

    AeaItem getAeaItemByItemSeq(@Param("itemSeq")String itemSeq);

    List<AeaItem> listAllChildItem(@Param("itemId")String itemId, @Param("rootOrgId") String rootOrgId);
}
