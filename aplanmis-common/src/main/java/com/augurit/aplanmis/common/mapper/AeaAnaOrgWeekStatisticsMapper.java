package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaOrgWeekStatistics;
import com.augurit.aplanmis.common.vo.analyse.ItemDetailFormVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 部门周办件统计表（在日办件统计表的基础上统计。历史周数据不变。
 * 本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaAnaOrgWeekStatisticsMapper {
    public void insertAeaAnaOrgWeekStatistics(AeaAnaOrgWeekStatistics aeaAnaOrgWeekStatistics);
    public void updateAeaAnaOrgWeekStatistics(AeaAnaOrgWeekStatistics aeaAnaOrgWeekStatistics);
    public void deleteAeaAnaOrgWeekStatistics(@Param("id") String id);
    public List <AeaAnaOrgWeekStatistics> listAeaAnaOrgWeekStatistics(AeaAnaOrgWeekStatistics aeaAnaOrgWeekStatistics);
    public AeaAnaOrgWeekStatistics getAeaAnaOrgWeekStatisticsById(@Param("id") String id);

    public void deleteThisWeekStatisticsData(@Param("statisticsStartDate") String statisticsStartDate, @Param("statisticsEndDate") String statisticsEndDate, @Param("rootOrgId") String rootOrgId);

    public void batchInserOrgWeekStatisticst(@Param("orgWeekStatisticsList") List<AeaAnaOrgWeekStatistics> orgWeekStatisticsList);

    public AeaAnaOrgWeekStatistics getAeaAnaOrgWeekStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("applySource") String applySource);

    /**
     * 计算指定周的 接件数、受理数、不予受理数、办件数、逾期数
     * @param weekNum
     * @param rootOrgId
     * @param orgId
     * @return
     */
    AeaAnaOrgWeekStatistics sumWeekStatistics(@Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId, @Param("orgId") String orgId);

    /**
     * 周范围内，接件情况统计
     * @param thisYear
     * @param weekNumStart
     * @param weekNumEnd
     * @param orgId
     * @param rootOrgId
     * @return
     */
    List<ItemDetailFormVo> getOrgWeekStatistics(@Param("thisYear") String thisYear,@Param("weekNumStart") int weekNumStart, @Param("weekNumEnd") int weekNumEnd, @Param("orgId") String orgId,@Param("rootOrgId") String rootOrgId);

    /**
     * 查询周范围内的部门事项接件受理情况统计
     *
     * @param orgId
     * @param year
     * @param weekNum
     * @param rootOrgId
     * @return
     */
    List<AeaAnaOrgWeekStatistics> getOrgItemAcceptStatistics(@Param("orgId") String orgId, @Param("year") String year, @Param("weekNum") int weekNum, @Param("rootOrgId") String rootOrgId);

    List<ItemDetailFormVo> getRegionWeekStatistics(@Param("thisYear") String thisYear, @Param("startWeekNum") int startWeekNum, @Param("endWeekNum") int endWeekNum, @Param("regionId") String regionId, @Param("rootOrgId") String rootOrgId);
}
