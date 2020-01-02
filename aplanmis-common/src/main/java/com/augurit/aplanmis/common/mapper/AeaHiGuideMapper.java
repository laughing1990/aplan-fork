package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaHiGuideMapper {

    void deleteAeaHiGuideByGuideId(String guideId);

    void batchDeleteAeaHiGuide(@Param("guideIds") List<String> guideIds);

    void insertAeaHiGuide(AeaHiGuide record);

    AeaHiGuide getAeaHiGuideByGuideId(String guideId);

    void updateAeaHiGuide(AeaHiGuide record);

    void batchInsertAeaHiGuide(@Param("list") List<AeaHiGuide> list);
}