package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段的前置阶段检测表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFrontStageMapper {

    void insertAeaParFrontStage(AeaParFrontStage aeaParFrontStage) ;

    void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) ;

    void deleteAeaParFrontStage(@Param("id") String id) ;

    List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) ;

    AeaParFrontStage getAeaParFrontStageById(@Param("id") String id) ;

    List<AeaParFrontStage> listAeaParFrontStageVo(AeaParFrontStage aeaParFrontStage) ;

    Long getMaxSortNo(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId) ;

    AeaParFrontStage getAeaParFrontStageVoById(@Param("id") String id) ;

    List<AeaParFrontStage> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) ;

    List<AeaParFrontStage> getHistStageByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
