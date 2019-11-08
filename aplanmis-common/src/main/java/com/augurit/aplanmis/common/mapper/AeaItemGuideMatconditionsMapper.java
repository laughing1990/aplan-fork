package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideMatconditions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideMatconditionsMapper {

    void insertAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

    void updateAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

    void deleteAeaItemGuideMatconditions(@Param("id") String id);

    List<AeaItemGuideMatconditions> listAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions);

    AeaItemGuideMatconditions getAeaItemGuideMatconditionsById(@Param("id") String id);

    void batchDeleteGuideMatconditionsByItemVerId(@Param("itemVerId") String itemVerId,
                                                  @Param("rootOrgId") String rootOrgId);

}
