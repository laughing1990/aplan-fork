package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaWinDayStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaWinWeekStatistics;
import com.augurit.aplanmis.common.vo.analyse.ThemeDayApplyRecord;
import com.augurit.aplanmis.common.vo.analyse.WinApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.WinWeekCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 窗口周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaAnaWinWeekStatisticsMapper {

    void insertAeaAnaWinWeekStatistics(AeaAnaWinWeekStatistics aeaAnaWinWeekStatistics) throws Exception;

    void updateAeaAnaWinWeekStatistics(AeaAnaWinWeekStatistics aeaAnaWinWeekStatistics) throws Exception;

    void deleteAeaAnaWinWeekStatistics(@Param("id") String id) throws Exception;

    List<AeaAnaWinWeekStatistics> listAeaAnaWinWeekStatistics(AeaAnaWinWeekStatistics aeaAnaWinWeekStatistics) throws Exception;

    AeaAnaWinWeekStatistics getAeaAnaWinWeekStatisticsById(@Param("id") String id) throws Exception;

    void deleteThisWeekStatisticsData(@Param("year") int year, @Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<AeaAnaWinWeekStatistics> getWinApplyByTime(@Param("year") String year, @Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId) throws Exception;

    void batchInsertWeekStatisticsData(@Param("weekStatisticsList") List<AeaAnaWinWeekStatistics> weekStatisticsList) throws Exception;

    List<WinWeekCountVo> getWeekStatisticsFromDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("year") Integer year,
                                                  @Param("weekNum") Integer weekNum, @Param("preDate") String preDate, @Param("rootOrgId") String rootOrgId);

    List<WinApplyStatisticsVo> getWinShouliStatisticsByWeek(@Param("startWeek") int startWeek, @Param("endWeek") int endWeek, @Param("rootOrgId") String rootOrgId, @Param("windowId") String windowId);

    List<ThemeDayApplyRecord> getStageGroupStatistics(@Param("thisYear") String thisYear, @Param("startWeekNum") int startWeekNum, @Param("endWeekNum") int endWeekNum, @Param("rootOrgId") String rootOrgId);

    List<ThemeDayApplyRecord> getThemeStageStatistics(@Param("thisYear") String thisYear, @Param("startWeekNum") int startWeekNum, @Param("endWeekNum") int endWeekNum, @Param("rootOrgId") String rootOrgId);

    /**
     * 分组统计阶段各类统计
     *
     * @param thisYear
     * @param
     * @param
     * @param rootOrgId
     * @param windowId
     * @return
     */
    List<AeaAnaWinDayStatistics> getWinStageGroupStatistics(@Param("thisYear") String thisYear, @Param("weekNumStart") int weekNumStart, @Param("weekNumEnd") int weekNumEnd, @Param("rootOrgId") String rootOrgId, @Param("windowId") String windowId);
}
