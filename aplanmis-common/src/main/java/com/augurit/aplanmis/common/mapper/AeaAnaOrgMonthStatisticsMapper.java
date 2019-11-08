package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaOrgMonthStatistics;
import com.augurit.aplanmis.common.vo.analyse.ItemDetailFormVo;
import com.augurit.aplanmis.common.vo.analyse.OrgMonthApplyStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 部门月办件统计表（在日办件统计表的基础上统计。历史月数据不变。
 * 本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaAnaOrgMonthStatisticsMapper {

    public void insertAeaAnaOrgMonthStatistics(AeaAnaOrgMonthStatistics aeaAnaOrgMonthStatistics);
    public void updateAeaAnaOrgMonthStatistics(AeaAnaOrgMonthStatistics aeaAnaOrgMonthStatistics);
    public void deleteAeaAnaOrgMonthStatistics(@Param("id") String id);
    public List <AeaAnaOrgMonthStatistics> listAeaAnaOrgMonthStatistics(AeaAnaOrgMonthStatistics aeaAnaOrgMonthStatistics);
    public AeaAnaOrgMonthStatistics getAeaAnaOrgMonthStatisticsById(@Param("id") String id);

    public void deleteThisMonthStatisticsData(@Param("statisticsMonth") String statisticsMonth, @Param("rootOrgId") String rootOrgId);
    public void batchInserOrgMonthStatisticst(@Param("orgMonthStatisticsList") List<AeaAnaOrgMonthStatistics> orgMonthStatisticsList);
    public AeaAnaOrgMonthStatistics getAeaAnaOrgMonthStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId,@Param("applySource") String applySource, @Param("statisticsMonth") String statisticsMonth);
    public List<AeaAnaOrgMonthStatistics> queryAeaAnaOrgMonthStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId,@Param("applySource") String applySource, @Param("monthList") List<String> monthList);

    /**
     * 计算指定年月的 接件数、受理数、不予受理数、办件数、逾期数
     * @param yearMonth
     * @param rootOrgId
     * @param orgId
     * @return
     */
    AeaAnaOrgMonthStatistics sumMonthStatistics(@Param("yearMonth") String yearMonth, @Param("rootOrgId")String rootOrgId, @Param("orgId")String orgId);


    /**
     * 月度事项受理统计
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<OrgMonthApplyStatisticsVo> getOrgApplyStatisticsByMonth(@Param("startMonth") String startTime, @Param("endMonth") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 月份时间段内统计事项办理情况
     * @param yearMonthStart
     * @param yearMonthEnd
     * @param orgId
     * @param rootOrgId
     * @return
     */
    List<ItemDetailFormVo> getOrgMonthStatistics(@Param("yearMonthStart") String yearMonthStart, @Param("yearMonthEnd") String yearMonthEnd, @Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId);

    /**
     * 月份时间段内部门事项解决受理情况
     *
     * @param orgId
     * @param month
     * @param rootOrgId
     * @return
     */
    List<AeaAnaOrgMonthStatistics> getOrgItemAcceptStatistics(@Param("orgId") String orgId, @Param("month") String month, @Param("rootOrgId") String rootOrgId);
}
