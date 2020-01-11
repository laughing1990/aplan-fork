package com.augurit.aplanmis.common.service.search;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.dto.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ApproveDataService {
    /**
     * 审批数据-根据事项划分
     * @param keyword 非必须参数 项目名称，项目编码，申办流水号
     * @return
     */
    List<AnnounceDataDto> searchAnnounceDataList(String keyword,String rootOrgId) throws Exception;

    /**
     * 分页查询审批数据-根据事项划分
     * @param keyword 非必须参数  项目名称，项目编码，申办流水号
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AnnounceDataDto> searchAnnounceDataList(String keyword, int pageNum,int pageSize,String rootOrgId);

    /**
     * 分页查询受理，审批，办结等状态的办件数据
     * @param applyinstCode  非必须参数 受理号
     * @param projInfoName  非必须参数 项目名称
     * @param pageNum
     * @param pageSize
     * @param applyStates 非必须参数 申报状态  为null时，查询所有  0,1受理情况  2,5 ,3,4审批情况 6办结
     * @return
     */
    PageInfo<ApproveDataDto> searchApproveDataList(String applyinstCode,String projInfoName,int pageNum,int pageSize,List<String> applyStates,String rootOrgId);

    /**
     *  根据登录信息查询审批中/已办结/草稿箱（未申报）的数据
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param state 0已办结 1审批中 2未申报
     * @param keyword 项目名称/工程代码/项目代码
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ApproveProjInfoDto> searchApproveProjInfoListByUnitOrLinkman(String unitInfoId, String userInfoId, String state,String applyinstState, String keyword,String [] filterStates, int pageNum, int pageSize) throws Exception;

    /**
     * 根据登录信息查询审批进度数据
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param keyword 项目名称/工程代码/项目代码
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ApproveProjInfoDto> searchScheduleInquireListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId,String keyword,int pageNum,int pageSize);

    /**
     *  根据登录信息查询已申报项目(用于进度查询)
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param keyword 项目名称/工程代码/项目代码
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<AeaProjInfo> getScheduleProjListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize);

    /**
     * 根据登录信息查询事项的审批情况
     * @param keyword 项目名称/工程代码/项目代码
     * @param unitInfoId 登录后的单位ID
     * @param userId 登录后的用户ID
     * @return
     */
    PageInfo<ApproveProjInfoDto> searchIteminstApproveInfoListByUnitIdAndUserId(String keyword,String unitInfoId,String userId,int pageNum,int pageSize) throws Exception;

    /**
     * 查询材料补全的数据列表
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param supplyState 待补全状态参数  枚举值参考ApplyState ：3
     * @return
     * @throws Exception
     */
    PageInfo<SupplementInfoDto> searchMatComplet(String unitInfoId,String userInfoId,String supplyState,int pageNum,int pageSize) throws Exception;

    //替代上面方法
    PageInfo<SupplementInfoDto> searchMatCompletByUser(String unitInfoId,String userInfoId,String keyword,int pageNum,int pageSize) throws Exception;

    /**
     * 查询材料补正的数据列表
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param supplyState 补正状态参数  枚举值参考ItemStatus ：6
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageInfo<SupplementInfoDto> searchSupplementInfo(String unitInfoId, String userInfoId,String supplyState, int pageNum, int pageSize) throws Exception;

        /**
         * 生命周期图-查询项目状态
         * @param applyinstId 必须字段 申请实例ID
         * @return
         * @throws Exception
         */
    ProjStateDto searchProjStateDtoByApplyinstId(String applyinstId) throws Exception;

    List<AeaProjInfo>  getScheduleProjListByUnitInfoIdOrLinkmanNoPage(String unitId, String userId, String keyword,String parentProjInfoId);
}
