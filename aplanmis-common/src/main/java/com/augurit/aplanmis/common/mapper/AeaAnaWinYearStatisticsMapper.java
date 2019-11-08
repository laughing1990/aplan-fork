package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaWinYearStatistics;
import com.augurit.aplanmis.common.vo.analyse.WinYearCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 窗口年申报统计表（在月申报统计表的基础上统计。历史年数据不变。本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaAnaWinYearStatisticsMapper {

    public void insertAeaAnaWinYearStatistics(AeaAnaWinYearStatistics aeaAnaWinYearStatistics) throws Exception;

    public void updateAeaAnaWinYearStatistics(AeaAnaWinYearStatistics aeaAnaWinYearStatistics) throws Exception;

    public void deleteAeaAnaWinYearStatistics(@Param("id") String id) throws Exception;

    public List<AeaAnaWinYearStatistics> listAeaAnaWinYearStatistics(AeaAnaWinYearStatistics aeaAnaWinYearStatistics) throws Exception;

    public AeaAnaWinYearStatistics getAeaAnaWinYearStatisticsById(@Param("id") String id) throws Exception;

    public void deleteThisYearStatisticsData(@Param("year") String year, @Param("rootOrgId") String rootOrgId) throws Exception;

    public void batchInsertYearStatisticsData(@Param("yearStatisticsList") List<AeaAnaWinYearStatistics> yearStatisticsList) throws Exception;

    public List<WinYearCountVo> getYearStatisticsFromMonth(@Param("rootOrgId") String rootOrgId, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("lastYear") Integer lastYear, @Param("preDay") String preDay) throws Exception;

    public List<Map<String, Object>> getRegionTotalApplyByTime(@Param("startYear") int startYear, @Param("endYear") int year, @Param("rootOrgId") String rootOrgId) throws Exception;

    public List<Map<String, Object>> getWinShenliStatistics(@Param("startTime") int startTime, @Param("endTime") int endTime, @Param("rootOrgId") String rootOrgId) throws Exception;
}
