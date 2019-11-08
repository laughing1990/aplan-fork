package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideMaterialsMapper {

    void insertAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    void updateAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    void deleteAeaItemGuideMaterials(@Param("id") String id);

    List<AeaItemGuideMaterials> listAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials);

    AeaItemGuideMaterials getAeaItemGuideMaterialsById(@Param("id") String id);

    void batchDeleteGuideMaterialsByItemVerId(@Param("itemVerId") String itemVerId,
                                              @Param("rootOrgId") String rootOrgId);
}
