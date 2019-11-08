package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaThemeWeekStatistics;
import com.augurit.aplanmis.common.vo.analyse.ThemeApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeWeekCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:19:04</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaAnaThemeWeekStatisticsMapper {

    void insertAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    void updateAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    void deleteAeaAnaThemeWeekStatistics(@Param("id") String id) throws Exception;

    List<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    AeaAnaThemeWeekStatistics getAeaAnaThemeWeekStatisticsById(@Param("id") String id) throws Exception;

    List<ThemeWeekCountVo> getWeekStatisticsFromDay(
            @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("year") Integer year,
            @Param("weekNum") Integer weekNum, @Param("preDate") String preDate, @Param("rootOrgId") String rootOrgId);

    void deleteThisWeekStatisticsData(@Param("year") int year, @Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId);

    void batchInsertWeekStatisticsData(@Param("weekStatisticsList") List<AeaAnaThemeWeekStatistics> result);

    List<ThemeApplyStatisticsVo> getApplyByTheme(@Param("year") int year, @Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId) throws Exception;
}
