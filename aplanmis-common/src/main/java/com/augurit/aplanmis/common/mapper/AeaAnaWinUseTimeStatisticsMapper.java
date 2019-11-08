package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaWinUseTimeStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 申报用时统计表（统计办理时限延期最长和办理时限压缩最短的申报）-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:zhangwn</li>
 * <li>创建时间：2019-09-23 16:10:17</li>
 * </ul>
 */
@Mapper
public interface AeaAnaWinUseTimeStatisticsMapper {

    void insertAeaAnaWinUseTimeStatistics(AeaAnaWinUseTimeStatistics aeaAnaWinUseTimeStatistics);

    void updateAeaAnaWinUseTimeStatistics(AeaAnaWinUseTimeStatistics aeaAnaWinUseTimeStatistics);

    void deleteAeaAnaWinUseTimeStatistics(@Param("id") String id);

    List<AeaAnaWinUseTimeStatistics> listAeaAnaWinUseTimeStatistics(AeaAnaWinUseTimeStatistics aeaAnaWinUseTimeStatistics);

    AeaAnaWinUseTimeStatistics getAeaAnaWinUseTimeStatisticsById(@Param("id") String id);

    void deleteAeaAnaWinUseTimeStatisticsByWindow(@Param("windowId") String windowId, @Param("rootOrgId") String rootOrgId);
}
