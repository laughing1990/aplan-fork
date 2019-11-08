package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaThemeMonthStatistics;
import com.augurit.aplanmis.common.vo.analyse.ThemeApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeMonthCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题月申报统计表（在日申报统计表的基础上统计。历史月数据不变。本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:28</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaAnaThemeMonthStatisticsMapper {

    void insertAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    void updateAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    void deleteAeaAnaThemeMonthStatistics(@Param("id") String id) throws Exception;

    List<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    AeaAnaThemeMonthStatistics getAeaAnaThemeMonthStatisticsById(@Param("id") String id) throws Exception;

    List<ThemeMonthCountVo> getMonthStatisticsFromDay(
            @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("lastMonth") String lastMonth,
            @Param("lastYear") String lastYear, @Param("preDate") String preDate, @Param("rootOrgId") String rootOrgId);

    void deleteThisMonthStatisticsData(@Param("month") String month, @Param("rootOrgId") String rootOrgId);

    void batchInsertMonthStatisticsData(@Param("monthStatisticsList") List<AeaAnaThemeMonthStatistics> result);

    List<ThemeApplyStatisticsVo> getApplyByTheme(@Param("month") String month, @Param("rootOrgId") String rootOrgId) throws Exception;
}
