package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.vo.analyse.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author cjw
 * @Date 2019/8/28
 */
@Repository
@Mapper
public interface EfficiencySupervisionMapper {

    List<ApplyFormVo> getApplyStatisticsByTIme(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("currentOrgId") String currentOrgId);

    /**
     * 根据申请实例状态查询
     *
     * @param ids
     * @param rootOrgId
     * @return
     */
    List<ApplyPorjVo> getApplyProjVoByState(@Param("ids") String[] ids, @Param("rootOrgId") String rootOrgId);

    List<ApplyFormVo> queryApplyWithRegion(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<ItemDetailFormVo> queryItemStatisticsByTime(@Param("ids") String[] stateIds, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询异常的办件（不予受理或者逾期）
     *
     * @param rootOrgId
     * @return
     */
    List<ItemExceptionCountVo> queryItemExceptionCountVo(@Param("rootOrgId") String rootOrgId);

    /**
     * 根据历史表 统计事项（部门接件）统计
     *
     * @param stateIds
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<ItemDetailFormVo> getItemStatisticsByTime(@Param("ids") String[] stateIds, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    List<ApplyLimitTimeVo> getWinLimitTimeStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("windowId") String windowId, @Param("rootOrgId") String rootOrgId);

    List<ApplyLimitTimeVo> getWinStageLimitTimeStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("windowId") String windowId, @Param("rootOrgId") String rootOrgId);

    List<ApplyUseTimeStatisticsVo> getCompletedApplyUseTimeByTheme(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<ApplyUseTimeStatisticsVo> getCompletedApplyUseTimeByThemeAndWindow(@Param("themeId") String themeId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId) throws Exception;

    BscDicRegion getCurrentRegionByUserId(@Param("currentUserId") String currentUserId);

    List<BscDicRegion> getChildrenReggion(@Param("regionSeq") String regionSeq, @Param("level") String level);

    List<ItemDetailFormVo> getOrgReceiveLimitTimeStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionId") String regionId, @Param("rootOrgId") String currentOrgId);

    /**
     * 查询行政区划下的部门
     * @param search
     * @return
     */
    List<OpuOmOrg> listOpuOmOrg(OpuOmOrg search);

    /**
     * 从历史表统计某状态数据
     * @param regionId
     * @param
     * @param startTime
     * @param endTime
     * @param rootOrgId
     * @return
     */
    List<ItemDetailFormVo> getOrgReceiveStateStatistics(@Param("regionId") String regionId, @Param("newState") String newState, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);
}
