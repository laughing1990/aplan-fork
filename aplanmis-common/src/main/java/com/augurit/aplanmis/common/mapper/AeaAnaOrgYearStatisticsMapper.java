package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaOrgYearStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 部门年办件统计表（在月办件统计表的基础上统计。历史年数据不变。
 * 本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaAnaOrgYearStatisticsMapper {

    public void insertAeaAnaOrgYearStatistics(AeaAnaOrgYearStatistics aeaAnaOrgYearStatistics);
    public void updateAeaAnaOrgYearStatistics(AeaAnaOrgYearStatistics aeaAnaOrgYearStatistics);
    public void deleteAeaAnaOrgYearStatistics(@Param("id") String id);
    public List<AeaAnaOrgYearStatistics> listAeaAnaOrgYearStatistics(AeaAnaOrgYearStatistics aeaAnaOrgYearStatistics);
    public AeaAnaOrgYearStatistics getAeaAnaOrgYearStatisticsById(@Param("id") String id);

    public void deleteThisYearStatisticsData(@Param("year") String year, @Param("rootOrgId") String rootOrgId);
    public void batchInserOrgYearStatisticst(@Param("orgYearStatisticsList") List<AeaAnaOrgYearStatistics> orgYearStatisticsList);
    public AeaAnaOrgYearStatistics getAeaAnaOrgYearStatistics(@Param("orgId") String orgId, @Param("itemId") String itemId, @Param("applySource") String applySource, @Param("year") String year);
}
