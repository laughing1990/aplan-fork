package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideMapper {

    void insertAeaItemGuide(AeaItemGuide aeaItemGuide);

    void updateAeaItemGuide(AeaItemGuide aeaItemGuide);

    void deleteAeaItemGuide(@Param("id") String id);

    void deleteAeaItemGuideByItemVerId(@Param("itemVerId") String itemVerId,
                                       @Param("rootOrgId")String rootOrgId);

    List<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide);

    AeaItemGuide getAeaItemGuideById(@Param("id") String id);

    AeaItemGuide getOneByItemVerId(@Param("itemVerId")String itemVerId,
                                   @Param("rootOrgId")String rootOrgId);
}