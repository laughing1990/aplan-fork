package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemOneform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemOneformMapper {

    void insertAeaItemOneform(AeaItemOneform aeaItemOneform);

    void updateAeaItemOneform(AeaItemOneform aeaItemOneform);

    void deleteAeaItemOneform(@Param("id") String id);

    List<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform);

    AeaItemOneform getAeaItemOneformById(@Param("id") String id);

    List<AeaItemOneform> listAeaItemOneFormByItemVerId(@Param("itemVerId") String itemVerId);

    Double getMaxSortNo(@Param("itemVerId") String itemVerId);

    void changIsActiveState(@Param("id") String id);
}
