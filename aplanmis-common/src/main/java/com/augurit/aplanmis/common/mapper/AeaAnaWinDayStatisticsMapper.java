package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaWinDayStatistics;
import com.augurit.aplanmis.common.vo.analyse.RegionApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeDayApplyRecord;
import com.augurit.aplanmis.common.vo.analyse.WinApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.WinStageApplyStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 窗口日申报统计表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaAnaWinDayStatisticsMapper {

    void insertAeaAnaWinDayStatistics(AeaAnaWinDayStatistics aeaAnaWinDayStatistics) throws Exception;

    void updateAeaAnaWinDayStatistics(AeaAnaWinDayStatistics aeaAnaWinDayStatistics) throws Exception;

    void deleteAeaAnaWinDayStatistics(@Param("id") String id) throws Exception;

    List<AeaAnaWinDayStatistics> listAeaAnaWinDayStatistics(AeaAnaWinDayStatistics aeaAnaWinDayStatistics) throws Exception;

    AeaAnaWinDayStatistics getAeaAnaWinDayStatisticsById(@Param("id") String id) throws Exception;

    void deleteThisDayStatisticsData(@Param("statisticsDate") String statisticsDate, @Param("rootOrgId") String rootOrgId) throws Exception;

    void batchInserWinDayStatisticst(@Param("winDayStatisticsList") List<AeaAnaWinDayStatistics> winDayStatisticsList) throws Exception;

    List<AeaAnaWinDayStatistics> getWinApplyByTime(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<RegionApplyStatisticsVo> getRegionApplyByTime(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinApplyStatisticsVo> getWinApplyStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinApplyStatisticsVo> getWinApplyStatistics2(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId, @Param("windowId") String windowId) throws Exception;

    Long getApplyCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("windowId") String windowId, @Param("rootOrgId") String currentOrgId);

    List<WinApplyStatisticsVo> getWinAcceptStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinApplyStatisticsVo> getWinAcceptStatisticsByDay(@Param("windowId") String windowId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<ThemeDayApplyRecord> getThemeStageStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<ThemeDayApplyRecord> getStageGroupStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 窗口统计阶段个状态件
     *
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @param windowId
     * @return
     */
    List<AeaAnaWinDayStatistics> getWinStageGroupStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId, @Param("windowId") String windowId);

    List<WinStageApplyStatisticsVo> getApplyByNet(@Param("windowId") String windowId, @Param("isParallel") String isParallel, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;

    Long countApplyByNet(@Param("windowId") String windowId, @Param("stageId") String stageId, @Param("isParallel") String isParallel, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;

    List<WinStageApplyStatisticsVo> getApplyByWin(@Param("windowId") String windowId, @Param("isParallel") String isParallel, @Param("applySource") String applySource, @Param("oldStates") String[] oldStates, @Param("states") String[] states, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;

    Long countApplyByWin(@Param("windowId") String windowId, @Param("stageId") String stageId, @Param("isParallel") String isParallel, @Param("applySource") String applySource, @Param("oldStates") String[] oldStates, @Param("states") String[] states, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;

    List<WinStageApplyStatisticsVo> getSupplementApplyByWin(@Param("windowId") String windowId, @Param("isParallel") String isParallel, @Param("state") String state, @Param("applySource") String applySource, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime) throws Exception;

    Long countSupplementApplyByWin(@Param("windowId") String windowId, @Param("stageId") String stageId, @Param("isParallel") String isParallel, @Param("state") String state, @Param("applySource") String applySource, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinStageApplyStatisticsVo> getOverTimeApplyByWin(@Param("overdueDate") String overdueDate, @Param("windowId") String windowId, @Param("isParallel") String isParallel, @Param("applySource") String applySource, @Param("rootOrgId") String rootOrgId, @Param("instState") String instState) throws Exception;

    Long countOverTimeApplyByWin(@Param("overdueDate") String overdueDate, @Param("windowId") String windowId, @Param("stageId") String stageId, @Param("isParallel") String isParallel, @Param("applySource") String applySource, @Param("rootOrgId") String rootOrgId, @Param("instState") String instState) throws Exception;
}
