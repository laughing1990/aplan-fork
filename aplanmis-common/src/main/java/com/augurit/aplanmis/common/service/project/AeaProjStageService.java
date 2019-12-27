package com.augurit.aplanmis.common.service.project;

import com.augurit.aplanmis.common.domain.AeaProjStage;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/9/5
 */
public interface AeaProjStageService {

    /**
     * 查询项目所有阶段申报状态
     *
     * @param projInfoId 项目ID
     * @return key:标准阶段序号；value：申报状态，0:未申报，1：进行中，2：已办结
     */
    Map<String, String> projAllStageState(String projInfoId);

    /**
     * 查询项目的阶段申报状态
     *
     * @param projInfoId     项目ID
     * @param standardSortNo 标准阶段序号
     * @return 0:未申报，1：进行中，2：已办结
     */
    String projStageState(String projInfoId, String standardSortNo);

    /**
     * 阶段申报触发阶段状态改变
     *
     * @param applyinstId 申请实例ID
     * @param userId      用户ID
     */
    void stageApplyUpdateAeaProjStageState(String applyinstId, String userId);

    /**
     * 阶段申报触发阶段状态改变
     *
     * @param stageId    阶段ID
     * @param projInfoId 项目ID
     * @param userId     用户ID
     */
    void stageApplyUpdateAeaProjStageState(String stageId,String projInfoId, String userId);

    /**
     * 事项办结时更新阶段办结状态
     *
     * @param iteminstId 事项实例ID
     * @param orgId      顶级组织D
     * @param userId     用户ID
     */
    void itemCompletedUpdateProjStageState(String iteminstId, String orgId, String userId);

    /**
     * 获取项目第一次申报某阶段时间
     *
     * @param projInfoId
     * @param stageId
     * @return
     */
    Date getProjFirstApplyStageTime(String projInfoId, String stageId);

    /**
     * 通过开始时间和结束时间查询时间段内办结的项目阶段信息
     *
     * @param startTime 大于等于开始时间
     * @param endTime   小于结束时间
     * @return
     */
    List<AeaProjStage> findProjStageByTimeRange(Date startTime, Date endTime);

    Date getPassTimeByProjInfoId(String projInfoId);
}
