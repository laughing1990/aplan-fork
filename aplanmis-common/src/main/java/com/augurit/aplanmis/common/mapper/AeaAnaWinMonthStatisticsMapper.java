package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaWinDayStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaWinMonthStatistics;
import com.augurit.aplanmis.common.vo.analyse.ThemeDayApplyRecord;
import com.augurit.aplanmis.common.vo.analyse.WinApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.WinMonthApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.WinMonthCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 窗口月申报统计表（在日申报统计表的基础上统计。历史月数据不变。本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaAnaWinMonthStatisticsMapper {

    void insertAeaAnaWinMonthStatistics(AeaAnaWinMonthStatistics aeaAnaWinMonthStatistics) throws Exception;

    void updateAeaAnaWinMonthStatistics(AeaAnaWinMonthStatistics aeaAnaWinMonthStatistics) throws Exception;

    void deleteAeaAnaWinMonthStatistics(@Param("id") String id) throws Exception;

    List<AeaAnaWinMonthStatistics> listAeaAnaWinMonthStatistics(AeaAnaWinMonthStatistics aeaAnaWinMonthStatistics) throws Exception;

    AeaAnaWinMonthStatistics getAeaAnaWinMonthStatisticsById(@Param("id") String id) throws Exception;

    void deleteThisMonthStatisticsData(@Param("month") String month, @Param("rootOrgId") String rootOrgId) throws Exception;

    void batchInsertMonthStatisticsData(@Param("monthStatisticsList") List<AeaAnaWinMonthStatistics> monthStatisticsList) throws Exception;

    List<AeaAnaWinMonthStatistics> getWinApplyByTime(@Param("month") String month, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinMonthCountVo> getMonthStatisticsFromDay(
            @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("lastMonth") String lastMonth,
            @Param("lastYear") String lastYear, @Param("preDate") String preDate, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<Map<String, Object>> getWinShouliStatisticsByMonth(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinMonthApplyStatisticsVo> getWinApplyStatisticsByMonth(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<WinApplyStatisticsVo> getWinShouliStatisticsByMonth2(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("rootOrgId") String currentOrgId, @Param("windowId") String windowId);

    List<ThemeDayApplyRecord> getStageGroupStatistics(@Param("startYearMonth") String startYearMonth, @Param("endYearMonth") String endYearMonth, @Param("rootOrgId") String rootOrgId);

    List<ThemeDayApplyRecord> getThemeStageStatistics(@Param("startYearMonth") String startYearMonth, @Param("endYearMonth") String endYearMonth, @Param("rootOrgId") String rootOrgId);

    List<AeaAnaWinDayStatistics> getWinStageGroupStatistics(@Param("yearMonthStart") String yearMonthStart, @Param("yearMonthEnd") String yearMonthEnd, @Param("rootOrgId") String rootOrgId, @Param("windowId") String windowId);
}
