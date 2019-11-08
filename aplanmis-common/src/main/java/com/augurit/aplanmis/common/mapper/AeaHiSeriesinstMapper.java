package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 串联申报实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:21</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiSeriesinstMapper {

    public void insertAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception;

    public void updateAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception;

    public void deleteAeaHiSeriesinst(@Param("id") String id) throws Exception;

    public List<AeaHiSeriesinst> listAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception;

    public AeaHiSeriesinst getAeaHiSeriesinstById(@Param("id") String id) throws Exception;

    public AeaHiSeriesinst getAeaHiSeriesinstByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;

    public AeaHiSeriesinst getAeaHiSeriesinstByAppinstId(@Param("appinstId") String appinstId) throws Exception;

    List<AeaHiSeriesinst> listAeaHiSeriesinstByApplyinstIds(@Param("applyinstIds") List<String> applyinstIds) throws Exception;

    /**
     * 查询对应时间段内申报的所属主题并行推进事项实例
     * @param queryThemeVerIds
     * @param rootOrgId
     * @param startTime
     * @param endTime
     * @param queryStates
     * @return
     */
    List<AeaHiSeriesinst> queryThemeAeaHiSeriesinsts(@Param("queryThemeVerIds") String queryThemeVerIds,@Param("rootOrgId")String rootOrgId,
                                                     @Param("startTime")String startTime,@Param("endTime")String endTime,@Param("queryStates")String queryStates);

    /**
     * 查询对应时间段内申报的对应阶段的并行推进事项实例
     * @param standardStageCode
     * @param rootOrgId
     * @param startTime
     * @param endTime
     * @param queryStates
     * @return
     */
    List<AeaHiSeriesinst> queryStageAeaHiSeriesinsts(@Param("standardStageCode") String standardStageCode,@Param("rootOrgId")String rootOrgId,
                                                     @Param("startTime")String startTime,@Param("endTime")String endTime,@Param("queryStates")String queryStates);
}
