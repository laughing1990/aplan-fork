package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaHiGuideDetailMapper {

    void deleteByGuideDetailId(String guideDetailId);

    void batchDeleteAeaHiGuideDetail(@Param("guideDetailIds") List<String> guideDetailIds);

    void insertAeaHiGuideDetail(AeaHiGuideDetail record);

    AeaHiGuideDetail getAeaHiGuideDetailByGuideDetailId(String guideDetailId);

    void updateAeaHiGuideDetail(AeaHiGuideDetail record);

    void batchInsertAeaHiGuideDetail(@Param("list") List<AeaHiGuideDetail> aeaHiGuideDetails);
}