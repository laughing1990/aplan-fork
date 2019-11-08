package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStageGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AeaParStageGuideMapper {

     AeaParStageGuide getAeaParStageGuideByStageId(@Param("stageId") String stageId,
                                                   @Param("rootOrgId") String rootOrgId);

     void insertAeaParStageGuide(AeaParStageGuide aeaParStageGuide) ;

     int updateAeaParStageGuide(AeaParStageGuide aeaParStageGuide) ;

     void deleteAeaParStageGuide(@Param("id") String id) ;

     List<AeaParStageGuide> listAeaParStageGuide(AeaParStageGuide aeaParStageGuide) ;

     AeaParStageGuide getAeaParStageGuideById(@Param("id") String id) ;

     Map getAeaParStageGuideById2(@Param("id") String id) ;
}
