package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.dto.SupplementInfoDto;
import com.augurit.aplanmis.common.vo.MatCorrectConfirmVo;
import com.augurit.aplanmis.rest.userCenter.vo.ApplyDetailVo;
import com.augurit.aplanmis.rest.userCenter.vo.LifeCycleDiagramVo;
import com.augurit.aplanmis.rest.userCenter.vo.StatisticsNumVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestApproveService {

    /**
     * 根据登录信息查询审批中/已办结/草稿箱（未申报）的数据
     *
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param state      0已办结 1审批中 2未申报
     * @param keyword    项目名称/工程代码/项目代码
     */
    PageInfo<ApproveProjInfoDto> searchApproveProjInfoListByUnitOrLinkman(String unitInfoId, String userInfoId, String state, String keyword, int pageNum, int pageSize) throws Exception;

    /**
     * 根据登录信息查询审批进度数据
     *
     * @param unitInfoId 登录后的单位ID
     * @param userInfoId 登录后的用户ID
     * @param keyword    项目名称/工程代码/项目代码
     */
    PageInfo<ApproveProjInfoDto> searchScheduleInquireListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize);

    /**
     * 根据登录信息查询事项的审批情况
     *
     * @param keyword    项目名称/工程代码/项目代码
     * @param unitInfoId 登录后的单位ID
     * @param userId     登录后的用户ID
     */
    PageInfo<ApproveProjInfoDto> searchIteminstApproveInfoListByUnitIdAndUserId(String keyword, String unitInfoId, String userId, int pageNum, int pageSize) throws Exception;

    /**
     * 查询补全状态的数据
     *
     */
    PageInfo<SupplementInfoDto> searchMatComplet(String unitInfoId, String userId, String keyword, int pageNum, int pageSize) throws Exception;

    /**
     * 查询补正状态的数据
     *
     */
    PageInfo<MatCorrectConfirmVo> searchSupplyInfoList(String unitInfoId, String userId, int pageNum, int pageSize) throws Exception;

    /**
     * 搜索项目信息
     *
     */
    PageInfo<AeaProjInfo> findAeaProjInfoByKeyword(String keyWord, int pageNum, int pageSize) throws Exception;

    /**
     * 根据申请实例id、项目编码获取申报详情信息
     *
     */
    ApplyDetailVo getApplyDetailByApplyinstIdAndProjInfoId(String applyinstId, String projInfoId, String isSeriesApprove, String iteminstId, HttpServletRequest request) throws Exception;

    /**
     * 查询申报查询->统计数字
     *
     */
    StatisticsNumVo getApprovalAndMatCompletionNum() throws Exception;

    /**
     * 根据材料实例ID 查询附件列表
     *
     */
    List<BscAttFileAndDir> getMatAttDetailByMatinstId(String matinstId) throws Exception;

    /**
     * 根据项目ID及当前登录用户获取生命周期图相关数据
     *
     * @param projInfoId 项目ID
     * @param unitInfoId 单位ID
     * @param userInfoId 用户ID
     */
    LifeCycleDiagramVo getLiftCycleDiagramInfo(String projInfoId, String unitInfoId, String userInfoId) throws Exception;

    /**
     * 根据登录信息查询撤件列表
     *
     * @param unitInfoId
     * @param userInfoId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageInfo<ApproveProjInfoDto> searchWithdrawApplyListByUnitOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) throws Exception;
}
