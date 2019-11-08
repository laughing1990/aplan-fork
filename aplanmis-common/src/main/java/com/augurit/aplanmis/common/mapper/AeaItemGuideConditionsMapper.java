package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideConditions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideConditionsMapper {


    void insertAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    void updateAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    void deleteAeaItemGuideConditions(@Param("id") String id);

    void batchDeleteGuideConditionsByItemVerId(@Param("itemVerId") String itemVerId,
                                               @Param("rootOrgId") String rootOrgId);

    List<AeaItemGuideConditions> listAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions);

    AeaItemGuideConditions getAeaItemGuideConditionsById(@Param("id") String id);
}
