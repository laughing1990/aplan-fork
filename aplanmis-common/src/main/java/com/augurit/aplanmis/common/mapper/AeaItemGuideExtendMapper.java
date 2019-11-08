package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideExtendMapper {

    void insertAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    void updateAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    void deleteAeaItemGuideExtend(@Param("id") String id);

    void deleteGuideExtendByItemVerId(@Param("itemVerId") String itemVerId,
                                      @Param("rootOrgId")String rootOrgId);

    List<AeaItemGuideExtend> listAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    AeaItemGuideExtend getAeaItemGuideExtendById(@Param("id") String id);
}