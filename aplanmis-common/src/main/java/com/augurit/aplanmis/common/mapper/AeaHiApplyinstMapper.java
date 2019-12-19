package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.dto.ApproveDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目申报实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:08</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiApplyinstMapper {

    public void insertAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception;

    public void updateAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception;

    public void deleteAeaHiApplyinst(@Param("id") String id) throws Exception;

    public List<AeaHiApplyinst> listAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception;

    public AeaHiApplyinst getAeaHiApplyinstById(@Param("id") String id) throws Exception;

    public AeaHiApplyinst getAeaHiApplyinstByCode(@Param("code") String code) throws Exception;

    public List<AeaHiApplyinst> listAeaHiApplyinstByLinkmanInfoId(@Param("linkmanInfoId") String linkmanInfoId) throws Exception;

    public List<AeaHiApplyinst> listAeaHiApplyinstByState(@Param("state") String state, @Param("rootOrgId") String rootOrgId) throws Exception;

    public List<AeaHiApplyinst> listAeaHiApplyinstByStates(@Param("states") List<String> states, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<ApproveDataDto> searchApproveDataList(@Param("applyinstCode") String applyinstCode, @Param("projInfoName") String projInfoName, @Param("states") List<String> applyStates, @Param("rootOrgId") String rootOrgId);

    int countCurrentMonthApplyinstByApplyinstStates(@Param("states") List<String> applyinstStates, @Param("rootOrgId") String rootOrgId) throws Exception;

    /**
     * 根据申请实例获取办件承诺时间及单位
     *
     * @param applyinstId
     * @return
     */
    AeaHiApplyinst getDueNumAndUnit(@Param("applyinstId") String applyinstId);

    List<AeaHiApplyinst> listAeaHiApplyinstByIds(@Param("ids") List<String> ids) throws Exception;

    List<AeaHiApplyinst> listApplyInstIdAndStateByProjInfoIdAndUser(@Param("projInfoId") String projInfoId, @Param("isSeriesApprove") String isSeriesApprove, @Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId);

    List<AeaHiApplyinst> listApplyInstListByUser( @Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId);


    /**
     * 根据项目id查询已申报过的记录---AEA_HI_APPLYINST表中已经去掉 PROJ_INFO_ID字段
     * 新增关联表 aea_applyisnt_proj
     *
     * @param projInfoId
     * @return
     */
    List<AeaHiApplyinst> getAeaHiApplyinstByProjInfoId(@Param("projInfoId") String projInfoId);

    /**
     * 查询对应时间段内的申请实例
     *
     * @param states
     * @param rootOrgId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<AeaHiApplyinst> queryAeaHiApplyinst(@Param("states") List<String> states, @Param("rootOrgId") String rootOrgId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询对应阶段的申请实例
     *
     * @param stageId
     * @return
     */
    List<AeaHiApplyinst> listAeaHiApplyinstByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    /**计算非并行推挤的
     * @param stageId
     * @param states
     * @param startTime
     * @param endTime
     * @param currentOrgId
     * @param isParallel
     * @param source
     * @return
     */
    List<AeaHiApplyinst> getAeaHiApplyinstByStageIdAndStates(@Param("stageId") String stageId, @Param("states") String[] states, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String currentOrgId, @Param("isParallel") String isParallel,@Param("source") String source);

    List<AeaHiApplyinst> listAeaHiApplyinstWithWindow(@Param("userName") String userName, @Param("userId") String userId, @Param("rootOrgId") String rootOrgId, @Param("applyinstState") String applyinstState, @Param("state") String state, @Param("keyword") String keyword);

    List<AeaHiApplyinst> listAeaHiApplyinstWangShangDaiYuShen(@Param("keyword") String keyword, @Param("userName") String userName);

    List<AeaHiApplyinst> getAllApplyinstesByProjInfoId(@Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<AeaHiApplyinst> getApplyinstByProjCodeAndIteminstId(@Param("projCode") String projCode, @Param("iteminstId") String iteminstId) throws Exception;

    List<String> getProcInstIdByApplyinstIdOrApplyinstCode(@Param("applyinstId") String applyinstId, @Param("applyinstCode") String applyinstCode) throws Exception;

    List<AeaHiApplyinst> getParalleApproveDataByIteminstIdAndProjCode(@Param("projCode") String projCode, @Param("iteminstId") String iteminstId) throws Exception;

    /**
     * 计算并行推挤下的
     * @param stageId
     * @param states
     * @param startTime
     * @param endTime
     * @param currentOrgId
     * @param isParallel
     * @param source
     * @return
     */
    List<AeaHiApplyinst> getAeaHiApplyinstByStageIdAndStatesIsParallel(@Param("stageId") String stageId, @Param("states") String[] states, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String currentOrgId, @Param("isParallel") String isParallel, @Param("source") String source);

    /**
     *
     * 查询并联申报下的并行申报列表
     * @param applyinstId 并联申报实例ID
     * @return
     */
    List<AeaHiApplyinst> getSeriesAeaHiApplyinstListByParentApplyinstId(@Param("applyinstId") String applyinstId);
}
