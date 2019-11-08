package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaServiceWindowStageMapper {

    void insertAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage);

    void updateAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage);

    void delAeaServiceWindowStageById(@Param("id") String id) ;

    void batchDelAeaServiceWindowStageByIds(@Param("ids")String[] ids);

    List<AeaServiceWindowStage> listAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage);

    AeaServiceWindowStage getAeaServiceWindowStageById(@Param("id") String id) ;

    void deleteAeaServiceWindowStageByWindowIdAndStageId(@Param("windowId") String windowId, @Param("stageId") String stageId);

    void enableWindowStage(@Param("windowId") String windowId, @Param("stageId") String stageId);

    void disableWindowStage(@Param("windowId") String windowId, @Param("stageId") String stageId);

    List<AeaParStage> findWindowStageByUserId(@Param("userId") String userId,@Param("themeVerId") String themeVerId,@Param("rootOrgId") String rootOrgId);

    List<AeaParStage> findWindowStageByWindowIdAndKeywordAndIsActive(@Param("windowId") String windowId, @Param("keyword") String keyword, @Param("isActive") String isActive);

    void batchDeleteWindowStageByWindowIdAndStageId(@Param("windowId") String windowId, @Param("stageIds") String[] stageIds);

    void batchInsertAeaServiceWindowStage(@Param("aeaServiceWindowStageList") List<AeaServiceWindowStage> aeaServiceWindowStageList);

    void delAeaServiceWindowStageByWindowId(@Param("windowId") String windowId, @Param("rootOrgId") String rootOrgId);

    List<AeaServiceWindowStage> findCurrentUserWindowStage(@Param("stageId") String stageId, @Param("userId") String userId, @Param("rootOrgId") String rootOrgId);

    List<AeaServiceWindowStage> findAeaServiceWindowStageByWindowId(@Param("windowId") String windowId, @Param("keyword") String keyword);
}