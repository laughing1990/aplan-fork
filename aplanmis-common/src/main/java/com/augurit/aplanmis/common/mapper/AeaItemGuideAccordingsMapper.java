package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideAccordings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设立依据-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemGuideAccordingsMapper {


    void insertAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);

    void updateAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);

    AeaItemGuideAccordings getAeaItemGuideAccordingsById(@Param("id") String id);

    void deleteAeaItemGuideAccordings(@Param("id") String id);

    void batchDeleteGuideAccordingsByItemVerId(@Param("itemVerId") String itemVerId,
                                               @Param("rootOrgId") String rootOrgId);

    List<AeaItemGuideAccordings> listAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings);
}
