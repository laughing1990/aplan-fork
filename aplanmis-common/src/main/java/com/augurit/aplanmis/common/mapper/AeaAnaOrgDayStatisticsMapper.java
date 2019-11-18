package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaOrgDayStatistics;
import com.augurit.aplanmis.common.vo.analyse.ApplyUnusualStatisticVo;
import com.augurit.aplanmis.common.vo.analyse.ItemDetailFormVo;
import com.augurit.aplanmis.common.vo.analyse.OrgAreaStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.OrgItemStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门日办件统计表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaAnaOrgDayStatisticsMapper {

    void insertAeaAnaOrgDayStatistics(AeaAnaOrgDayStatistics aeaAnaOrgDayStatistics);

    void updateAeaAnaOrgDayStatistics(AeaAnaOrgDayStatistics aeaAnaOrgDayStatistics);

    void deleteAeaAnaOrgDayStatistics(@Param("id") String id);

    List<AeaAnaOrgDayStatistics> listAeaAnaOrgDayStatistics(AeaAnaOrgDayStatistics aeaAnaOrgDayStatistics);

    AeaAnaOrgDayStatistics getAeaAnaOrgDayStatisticsById(@Param("id") String id);

    void deleteThisDayStatisticsData(@Param("statisticsDate") String statisticsDate, @Param("rootOrgId") String rootOrgId);

    void batchInserOrgDayStatisticst(@Param("orgDayStatisticsList") List<AeaAnaOrgDayStatistics> orgDayStatisticsList);

    AeaAnaOrgDayStatistics getAeaAnaOrgDayStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId, @Param("statisticsDate") String statisticsDate, @Param("applySource") String applySource);

    List<AeaAnaOrgDayStatistics> queryAeaAnaOrgDayStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId, @Param("applySource") String applySource, @Param("statisticsDateList") List<String> statisticsDateList);

    /**
     * 计算指定日期内的 接件数、受理数、不予受理数、办件数、逾期数
     *
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @param orgId
     * @return
     */
    AeaAnaOrgDayStatistics getDayStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId, @Param("orgId") String orgId);

    List<ApplyUnusualStatisticVo> listApplyUnusualStatistic(@Param("rootOrgId") String rootOrgId);


    /**
     * 计算指定日期内区域的办件数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<OrgAreaStatisticsVo> getDayStatisticsCompletedByArea(@Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 查询部门统计日期大于等于startTime小于等于endTime的统计数据
     *
     * @param orgIds
     * @param itemId
     * @param startTime
     * @return
     */
    List<AeaAnaOrgDayStatistics> queryAeaAnaOrgDayStatisticsGreaterThanStartTime(@Param("orgIds") String[] orgIds, @Param("itemId") String itemId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询时间段内的部门接件情况统计
     *
     * @param startDate
     * @param endDate
     * @param orgId
     * @param rootOrgId
     * @return
     */
    List<ItemDetailFormVo> getOrgDayStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询时间段内的部门事项接件受理情况统计
     *
     * @param orgId
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<AeaAnaOrgDayStatistics> getOrgItemAcceptStatistics(@Param("orgId") String orgId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询时间段内每天的地区/各地区部门/各地区各部门各事项接件受理情况统计
     *
     * @param orgId
     * @param itemId
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<OrgItemStatisticsVo> getAcceptStatisticsByDay(@Param("regionId") String regionId, @Param("orgId") String orgId, @Param("itemId") String itemId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询时间段内各地区的接件受理情况统计
     *
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     * @throws Exception
     */
    List<AeaAnaOrgDayStatistics> getRegionAcceptStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    /**
     * 查询时间段内各地区各部门的接件受理情况统计
     *
     * @param regionId
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     * @throws Exception
     */
    List<AeaAnaOrgDayStatistics> getOrgAcceptStatistics(@Param("regionId") String regionId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;
    List<OrgItemStatisticsVo> getOrgItemAcceptStatisticsByDay(@Param("orgId") String orgId, @Param("itemId") String itemId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<ItemDetailFormVo> getRegionDayStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("regionId") String regionId, @Param("rootOrgId") String rootOrgId);
}
