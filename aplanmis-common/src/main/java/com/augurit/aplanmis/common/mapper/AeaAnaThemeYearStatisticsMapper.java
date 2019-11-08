package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaThemeYearStatistics;
import com.augurit.aplanmis.common.vo.analyse.StageApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.YearCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题年申报统计表（在月申报统计表的基础上统计。历史年数据不变。本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:36</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaAnaThemeYearStatisticsMapper {

    public void insertAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;

    public void updateAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;

    public void deleteAeaAnaThemeYearStatistics(@Param("id") String id) throws Exception;

    public List<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;

    public AeaAnaThemeYearStatistics getAeaAnaThemeYearStatisticsById(@Param("id") String id) throws Exception;

    List<YearCountVo> getYearStatisticsFromMonthTable(@Param("lastYear") String lastYear, @Param("year")String year, @Param("yearMonth")String yearMonth, @Param("rootOrgId")String currentOrgId);

    void batchInsertYearStatisticsData(@Param("yearStatisticsList") List<AeaAnaThemeYearStatistics> result);

    void deleteThisYearStatisticsData(@Param("year") String year, @Param("rootOrgId") String rootOrgId);

    List<ThemeApplyStatisticsVo> getThemeApplyByTime(@Param("startYear") int startYear, @Param("endYear") int endYear, @Param("rootOrgId") String rootOrgId);

    List<StageApplyStatisticsVo> getStageApplyByTime(@Param("startYear") int startYear, @Param("endYear") int endYear, @Param("rootOrgId") String rootOrgId);

}
