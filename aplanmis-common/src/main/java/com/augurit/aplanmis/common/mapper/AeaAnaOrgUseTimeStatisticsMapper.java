package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaOrgUseTimeStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 办件用时统计表（统计办理时限延期最长和办理时限压缩最短的办件）-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:zhangwn</li>
 * <li>创建时间：2019-09-19 18:57:42</li>
 * </ul>
 */
@Mapper
public interface AeaAnaOrgUseTimeStatisticsMapper {

    void insertAeaAnaOrgUseTimeStatistics(AeaAnaOrgUseTimeStatistics aeaAnaOrgUseTimeStatistics);

    void updateAeaAnaOrgUseTimeStatistics(AeaAnaOrgUseTimeStatistics aeaAnaOrgUseTimeStatistics);

    void deleteAeaAnaOrgUseTimeStatistics(@Param("id") String id);

    void deleteAeaAnaOrgUseTimeStatisticsByOrgId(@Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId);

    List<AeaAnaOrgUseTimeStatistics> listAeaAnaOrgUseTimeStatistics(AeaAnaOrgUseTimeStatistics aeaAnaOrgUseTimeStatistics);

    AeaAnaOrgUseTimeStatistics getAeaAnaOrgUseTimeStatisticsById(@Param("id") String id);

    List<AeaAnaOrgUseTimeStatistics> listAeaAnaOrgUseTimeStatisticsByItemAndItemVer(@Param("rootOrgId") String rootOrgId,
                                                                                    @Param("itemId") String itemId,
                                                                                    @Param("itemVerId") String itemVerId);

}
