package com.augurit.efficiency.service;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.aplanmis.common.vo.analyse.*;

import java.util.List;
import java.util.Map;

public interface OrgEfficiencySupersionService {

    /**
     * 事项申报来源统计
     * @param rootOrgId
     * @return
     */
    List<Map> countItemByApplyinstSource(String rootOrgId);

    /**
     * 并联或一般单项统计,格式形如:[{"name":"一般单项","value":218},{"name":"并联单项","value":929}]}
     * @param rootOrgId
     * @return
     */
    List<Map> countItemForSeriesAndStage(String rootOrgId);

    /**
     * 事项办理异常排行
     * @param rootOrgId
     * @return
     */
    List<ApplyUnusualStatisticVo> queryItemExceptionStatistics(String rootOrgId);

    List<ItemStatisticsVo> getItemStateStatistics(String orgId, boolean isCurrent) throws Exception;

    List<ItemDetailFormVo> queryOrgHandleItemStatistics(String startTime, String endTime) throws Exception;

    List<ItemDetailFormVo> queryOrgHandleItemStatisticsToYesterday(String type) throws Exception;

    List<OrgAreaStatisticsVo> queryStatisticsCompletedByArea(String startTime, String endTime) throws Exception;

    List<OrgMonthApplyStatisticsVo> queryOrgApplyStatisticsByMonth(String startMonth, String endMonth) throws Exception;


    Map<String, Object> getOrgItemStatistics(String startTime, String endTime, String type, boolean isCurrent,String orgId) throws Exception;

    List<Map<String, Object>> getOrgItemAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception;

    List<Map<String, Object>> getOrgItemAcceptHistoryStatistics(String itemId, String startTime, String endTime, String type, boolean isCurrent) throws Exception;

    List<BscDicRegion> getCurrentRegionList() throws Exception;

    Map<String, Object> getOrgReceiveStatistics(String startTime, String endTime, String type, String regionId) throws Exception;

    List<List<Object>> getOrgReceiveLimitTimeStatistics(String startTime, String endTime, String type, String regionId) throws Exception;
}
