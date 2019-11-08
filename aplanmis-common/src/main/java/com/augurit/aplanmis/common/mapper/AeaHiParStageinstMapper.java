package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段/环节实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:17</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiParStageinstMapper {

    int insertAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception;

    int updateAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception;

    int deleteAeaHiParStageinst(@Param("id") String id) throws Exception;

    List<AeaHiParStageinst> listAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception;

    AeaHiParStageinst getAeaHiParStageinstById(@Param("id") String id) throws Exception;

    AeaHiParStageinst getAeaHiParStageinstByAppinstId(@Param("appinstId") String appinstId) throws Exception;

    /**
     * 根据主题版本ID或阶段ID查询阶段实例列表
     * 1 themeVerId!=null &&（stageId==null || stageId!=null）  查询指定主题版本下的(阶段的)阶段实例列表
     * 2 themeVerId==null && stageId!=null 查询阶段ID下的所有实例列表
     *
     * @param themeVerId 主题版本ID
     * @param stageId    阶段ID
     * @return List<AeaHiParStageinst>
     */
    List<AeaHiParStageinst> listAeaHiParStageinstByStageIdOrThemeVerId(@Param("themeVerId") String themeVerId, @Param("stageId") String stageId) throws Exception;

    /**
     * 根据申请实例查询查询阶段实例
     * @param applyinstId
     * @return
     */
    AeaHiParStageinst getAeaHiParStageinstByApplyinstId(@Param("applyinstId") String applyinstId);

    List<AeaHiParStageinst> listAeaHiParStageinstByApplyinstIds(@Param("applyinstIds") List<String> applyinstIds);

    List<AeaHiParStageinst> listAeaHiParStageinstByIds(@Param("stageinstIds") List<String> stageinstIds);

    /**
     * 根据阶段ID查询所有阶段实例ID
     * @param stageId
     * @return
     */
    List<String> getStageinstIdsByStageid(@Param("stageId") String stageId);


    /**
     * 查询对应时间段内申报的所属主题阶段实例
     * @param queryThemeVerIds
     * @param rootOrgId
     * @param startTime
     * @param endTime
     * @param queryStates
     * @return
     */
    List<AeaHiParStageinst> queryThemeAeaHiParStageinsts(@Param("queryThemeVerIds") String queryThemeVerIds,@Param("rootOrgId")String rootOrgId,
                                                         @Param("startTime")String startTime,@Param("endTime")String endTime,@Param("queryStates")String queryStates);

    /**
     *  查询对应时间段内申报的对应阶段的阶段实例
     * @param standardStageCode
     * @param rootOrgId
     * @param startTime
     * @param endTime
     * @param queryStates
     * @return
     */
    List<AeaHiParStageinst> queryStageAeaHiParStageinsts(@Param("standardStageCode") String standardStageCode,@Param("rootOrgId")String rootOrgId,
                                                         @Param("startTime")String startTime,@Param("endTime")String endTime,@Param("queryStates")String queryStates);

}
