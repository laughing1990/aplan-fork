package com.augurit.efficiency.service;

import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 9:14
 */
public interface WinEfficiencySupervisionService {

    Map<String, Object> getThemeApplyStatistics(String startTime, String endTime) throws Exception;

    Map<String, Object> getStageApplyStatistics(String startTime, String endTime) throws Exception;

    List<RegionApplyStatisticsVo> getRegionApplyStatistics(String startTime, String endTime) throws Exception;

    List<WinMonthApplyStatisticsVo> getWinApplyStatisticsByMonth(String startTime, String endTime) throws Exception;

    List<WinApplyStatisticsVo> getWinApplyStatistics(String startTime, String endTime) throws Exception;

    List<ApplyStatisticsVo> getCurWinApplyStatistics() throws Exception;

    List<ApplyStatisticsVo> getApplyStatistics() throws Exception;

    List<ApplyThemeExceptionVo> queryApplyThemeExceptionStatistics() throws Exception;

    List<Map<String, Object>> staticsticsByApplyType() throws Exception;

    List<Map<String, Object>> staticsticsByApplySource() throws Exception;

    PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listDismissedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listNeedConfirmCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listWarnApply(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listOverdueApply(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    Map<String,Object> getWinAcceptStatistics(String startTime, String endTime, String type, boolean b) throws Exception;

    Map<String,Object> getWinAcceptRate(String startTime, String endTime, String type) throws Exception;

    List<Map<String, Object>> getAcceptDealStatisticsByWin(String type, String startTime, String endTime) throws Exception;

    List<Map<String, Object>> getAcceptStatisticsByDay(String windowId, String type, String startTime, String endTime) throws Exception;

    Map<String, Object> getApplyStatisticsByTheme(String type, String startTime, String endTime) throws Exception;
//    List<Map<String, Object>> getAcceptStaticsticsByDay(String windowId, String type, String startTime, String endTime) throws Exception;

    Map<String, Object> getThemeDistributionStatistics(String startTime, String endTime, String type) throws Exception;

    Map<String, Object> getStageApplyStatisticsByType(String startTime, String endTime, String type) throws Exception;

    Map<String, Object> getWinStageAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception;

    Map<String, Object> getCurrentWinAcceptStatistics(String startTime, String endTime, String type, boolean b)throws  Exception;

    List<Map<String, Object>> getCurWinAcceptStatisticsByDay(String type, String startTime, String endTime) throws Exception;
}
