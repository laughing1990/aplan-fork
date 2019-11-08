package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.aplanmis.common.domain.AeaAnaThemeDayStatistics;
import com.augurit.aplanmis.common.vo.analyse.StageApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.ThemePeriodStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 主题日申报统计表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenjw</li>
 * <li>创建时间：2019-09-03 13:41:57</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaAnaThemeDayStatisticsMapper {

    public void insertAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;

    public void updateAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;

    public void deleteAeaAnaThemeDayStatistics(@Param("id") String id) throws Exception;

    public List<AeaAnaThemeDayStatistics> listAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;

    public AeaAnaThemeDayStatistics getAeaAnaThemeDayStatisticsById(@Param("id") String id) throws Exception;

    List<AeaAnaThemeDayStatistics> getAeaAnaThemeDayStatistics(@Param("themeId") String themeId, @Param("stageId") String stageId, @Param("isParallel") String isParallel, @Param("applyinstSource") String applyinstSource, @Param("currentOrgId") String currentOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int getStateAllCount(@Param("stageId") String applyRecordId, @Param("states") String[] states, @Param("currentOrgId") String currentOrgId);

    List<Map<String,Object>> queryThemeStageIds(@Param("themeVerIds") String themeVerIds, @Param("rootOrgId") String rootOrgId);

    AeaAnaThemeDayStatistics getAeaAnaThemeDayStatisticsBySatgeIdAndThemeId(@Param("themeId") String themeId, @Param("stageId") String stageId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId, @Param("isParallel") String isParallel, @Param("applyinstSource") String applyinstSource);

    List<ThemePeriodStatistics> getThemePeriodStatistics(@Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    void batchInsertAeaAnaThemeDayStatistics(@Param("themeDayStatisticsList")List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatistics);

    List<ActStoTimeruleInst> getStageStoTimeruleInst(@Param("stageId") String stageId, @Param("state") String state, @Param("rootOrgId") String rootOrgId, @Param("isParallel") String isParallel, @Param("isApprove") String isApprove, @Param("source") String source,@Param("startTime") String startTime,@Param("endTime") String endTime);

    void deleteThisDayStatisticsData(@Param("statisticsDate") String dateFormat, @Param("rootOrgId") String rootOrgId);

    List<ThemeApplyStatisticsVo> getThemeApplyByTime(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<StageApplyStatisticsVo> getStageApplyByTime(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<AeaAnaThemeDayStatistics> getOutScopeApplyByThemeStage(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<AeaAnaThemeDayStatistics> getOverTimeApply(@Param("rootOrgId") String rootOrgId);

    List<ThemeApplyStatisticsVo> getApplyByTheme(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);
}
