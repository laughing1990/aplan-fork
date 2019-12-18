package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeDayApplyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaLogApplyStateHistMapper {

    void insertAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    void updateAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    void deleteAeaLogApplyStateHist(@Param("id") String id);

    List<AeaLogApplyStateHist> listAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    AeaLogApplyStateHist getAeaLogApplyStateHistById(@Param("id") String id);

    AeaLogApplyStateHist getInitStateAeaLogApplyStateHist(@Param("applyinstId") String applyinstId, @Param("appinstId") String appinstId);

    AeaLogApplyStateHist getAeaLogApplyStateHistByApplyinstCorrectId(@Param("applyinstCorrectId") String applyinstCorrectId);

    AeaLogApplyStateHist getAeaLogApplyStateHistListByApplyinstCorrectId(@Param("applyinstCorrectId") String applyinstCorrectId);

    /**
     * 获取项目第一次申报某个阶段的预受理记录
     *
     * @param projInfoId
     * @param standardSortNo
     * @return
     */
    AeaLogApplyStateHist getFirstApplyAeaProjStageByProjInfoIdAndStandardSortNo(@Param("projInfoId") String projInfoId, @Param("standardSortNo") String standardSortNo);

    AeaLogApplyStateHist getLastApplyStageLogByState(@Param("applyinstId") String applyinstId, @Param("newState") String newState, @Param("rootOrgId") String rootOrgId);

    /**
     * 获取某阶段id下的改变的实体
     **/
    List<AeaLogApplyStateHist> getApplyChangeHis(@Param("stageId") String stageId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isParallel") String isParallel, @Param("isApprove") String isApprove);

    List<SupplyOrSpacialCommentVo> findApplyinstCorrectStateHist(@Param("applyinstId") String applyinstId, @Param("taskInstId") String taskInstId, @Param("rootOrgId") String rootOrgId);

    List<AeaLogApplyStateHist> listLatestLogApplyStateHist(@Param("rootOrgId") String rootOrgId);


    List<AeaLogApplyStateHist> getWindowApplyHistory(@Param("windowId") String windowId, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<AeaLogApplyStateHist> getWindowApplyHistoryByState(@Param("windowId") String windowId, @Param("state") String state, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<AeaLogApplyStateHist> getApplyChangeHisIsParallel(@Param("stageId") String stageId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("isParallel") String isParallel);

    List<ThemeDayApplyRecord> listLogApplyStateHistBySourecAndParallel(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId, @Param("isApprove") String isApprove, @Param("isParallel") String isParallel);

    /**
     * 计算时间段内国标阶段的
     *
     * @param windowId
     * @param state
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<ThemeDayApplyRecord> getWindowApplyCountGroupByStage(@Param("windowId") String windowId, @Param("state") String state, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);
}
