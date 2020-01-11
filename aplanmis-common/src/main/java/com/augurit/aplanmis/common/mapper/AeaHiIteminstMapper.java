package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.dto.SupplementInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:16</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiIteminstMapper {

    public void insertAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception;

    public void updateAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception;

    public void deleteAeaHiIteminst(@Param("id") String id) throws Exception;

    public List<AeaHiIteminst> listAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception;

    public AeaHiIteminst getAeaHiIteminstById(@Param("id") String id) throws Exception;

    public AeaHiIteminst getAeaHiIteminstByProcinstId(@Param("procinstId") String procinstId) throws Exception;

    List<AeaHiIteminst> getAeaHiIteminstListByStageinstId(@Param("stageinstId") String stageinstId) throws Exception;

    //isDeleted  1 则包含 isDeleted字段
    List<AeaHiIteminst> getAeaHiIteminstListByApplyinstId(@Param("applyinstId") String applyinstId, @Param("isDeleted") String isDeleted);

    List<ApproveProjInfoDto> getApproveProjInfoListByUnitOrLinkman(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("state") String state,@Param("applyinstState") String applyinstState,@Param("keyword") String keyword,@Param("filterStates") String[] filterStates);


    /**
     * 撤件列表
     * @param unitInfoId
     * @param userInfoId
     * @param state
     * @param keyword
     * @return
     */
    List<ApproveProjInfoDto> getWithdrawApplyListByUnitOrLinkman(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("keyword") String keyword);

    long countApproveProjInfoListByUnitOrLinkman(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("isAll") String isAll);

    List<ApproveProjInfoDto> getScheduleInquireListByUnitInfoIdOrLinkman(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("keyword") String keyword);

    List<AeaProjInfo> getScheduleProjListByUnitInfoIdOrLinkman(@Param("keyword") String keyword, @Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("parentProjInfoId") String parentProjInfoId);

    List<ApproveProjInfoDto> getIteminstApproveInfoListByUnitIdAndUserId(@Param("keyword") String keyword, @Param("unitInfoId") String unitInfoId, @Param("userId") String userId);

    List<AeaHiIteminst> getSeriesAeaHiIteminstByProjInfoId(@Param("projInfoId") String projInfoId);


    List<SupplementInfoDto> searchMatComplet(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("supplyState") String supplyState);

    List<SupplementInfoDto> searchMatCompletByUser(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("keyword") String keyword);


    List<SupplementInfoDto> searchSupplementInfo(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("supplyState") String supplyState);

    /**
     * 根据申请实例查询所有的事项实例（单项或并联）
     *
     * @param applyinstId 申请实例ID
     * @return List<AeaHiIteminst>
     */
    List<AeaHiIteminst> listAllAeaHiIteminstByApplyinstId(@Param("applyinstId") String applyinstId, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据单项实例查询事项实例
     *
     * @param seriesinstId
     * @return
     */
    AeaHiIteminst getAeaHiIteminstBySeriesinstId(@Param("seriesinstId") String seriesinstId);

    List<AeaHiIteminst> listAeaHiIteminstListByStageinstIds(@Param("stageinstIds") List<String> stageinstIds) throws Exception;

    List<AeaHiIteminst> listAeaHiiteminstListBySeriesinstIds(@Param("seriesinstIds") List<String> seriesinstIds) throws Exception;


    //===============================aplanmis-analyse==========================================

    List<AeaHiIteminst> queryAeaHiIteminstList(AeaHiIteminst aeaHiIteminst);

    AeaHiIteminst queryAeaHiIteminstBySeriesApplyinstId(@Param("applyinstId") String applyinstId);

    List<AeaHiIteminst> listAeaHiIteminstByProjInfoIdAndStageIds(@Param("stageIds") List<String> stageIds, @Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId);

    List<AeaHiIteminst> listSeriesAeaHiIteminstByProjInfoId(@Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId);


    int countTotalItemByStates(@Param("states") String[] states, @Param("rootOrgId") String rootOrgId);

    int countCurrentMonthCountItemByStates(@Param("states") String[] states, @Param("rootOrgId") String rootOrgId);

    int countTotalItemByApplyType(@Param("isSeriesApprove") String isSeriesApprove, @Param("rootOrgId") String rootOrgId);

    List<AeaHiIteminst> getAeaHiIteminstListByApplyinstIds(@Param("ids") List<String> applyinstIds, @Param("isSeriesApprove") String isSeriesApprove, @Param("isDeleted") String isDeleted);

    /**
     * 查询部门对应状态和来源办件数
     *
     * @param itemId
     * @param state
     * @param orgId
     * @param applySource
     * @param startTime
     * @param endTime
     * @return
     */
    int countApproveIteminst(@Param("itemId") String itemId, @Param("state") String state, @Param("orgId") String orgId, @Param("applySource") String applySource, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询部门事项实例对应状态和来源数（不查询历史表）
     *
     * @param itemId
     * @param orgId
     * @param state
     * @param applySource
     * @return
     */
    int countTotalIteminst(@Param("itemId") String itemId, @Param("orgId") String orgId, @Param("state") String state, @Param("applySource") String applySource);

    /**
     * 查询部门待补正办件数和补正待确认办件数
     *
     * @param itemId
     * @param orgId
     * @param state
     * @return
     */
    int countSupplementIteminst(@Param("itemId") String itemId, @Param("orgId") String orgId, @Param("state") String state);

    /**
     * 查询部门办结数
     *
     * @param itemId
     * @param states
     * @param orgId
     * @param startTime
     * @param endTime
     * @param applySource
     * @return
     */
    int countCompletedIteminst(@Param("itemId") String itemId, @Param("states") String[] states, @Param("orgId") String orgId,
                               @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("applySource") String applySource);

    long countIteminstByTime(@Param("states") String[] states, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据并联事项实例ID查询改并联申请下所有的事项实例
     *
     * @param iteminstId
     * @return
     */
    List<AeaHiIteminst> getUnitAeaHiIteminstByiteminstId(@Param("iteminstId") String iteminstId);

    Long countSeriesIteminst(@Param("rootOrgId") String rootOrgId);

    Long countStageIteminst(@Param("rootOrgId") String rootOrgId);

    Long countIteminstByApplySource(@Param("applyinstSource") String applyinstSource, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询部门逾期办件或预警办件数
     *
     * @param state
     * @param queryIteminstStates
     * @param orgId
     * @param rootOrgId
     * @return
     */
    int countOverTimeOrWarningAeaHiIteminst(@Param("state") String state, @Param("queryIteminstStates") String queryIteminstStates, @Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId);

    List<AeaHiIteminst> listAeaHiIteminstByProjInfoIdAndItemVerIds(@Param("projInfoId") String projInfoId, @Param("itemVerIds") List<String> itemVerIds, @Param("rootOrgId") String rootOrgId);

    List<ApproveProjInfoDto> getDraftApplyList(@Param("unitInfoId") String unitInfoId, @Param("userInfoId") String userInfoId, @Param("keyword") String keyword);

    void batchDeleteAeaHiIteminst(@Param("ids") String[] iteminstIds);
}
