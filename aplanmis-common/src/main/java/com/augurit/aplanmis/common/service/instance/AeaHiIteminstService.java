package com.augurit.aplanmis.common.service.instance;


import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AeaHiIteminstService {
    /**
     * 1.根据阶段实例ID查询事项实例列表
     *
     * @param stageinstId 必须参数   阶段实例ID
     * @return
     * @throws Exception
     */
    List<AeaHiIteminst> getAeaHiIteminstListByStageinstId(String stageinstId) throws Exception;

    /**
     * 2.0 根据申请实例ID查询事项实例列表(单项，并联统一接口)
     *
     * @param applyinstId 必须参数  申请实例ID
     * @return
     * @throws Exception
     */
    List<AeaHiIteminst> getAeaHiIteminstListByApplyinstId(String applyinstId) throws Exception;

    /**
     * 2.0 根据申请实例ID查询事项实例列表(单项，并联统一接口)
     *
     * @param applyinstIds 必须参数  申请实例ID
     * @param isSeriesApprove 0并联  1单项
     * @return
     * @throws Exception
     */
    List<AeaHiIteminst> getAeaHiIteminstListByApplyinstIds(List<String> applyinstIds,String isSeriesApprove) throws Exception;

    /**
     * 2.1 根据申请实例ID和事项实例ID查询事项实例列表(单项，并联统一接口)
     *
     * @param applyinstId 必须参数  申请实例ID
     * @param iteminstId  可选参数  事项实例ID 并联可根据该字段过滤事项
     * @return
     * @throws Exception
     */
    List<AeaHiIteminst> getAeaHiIteminstListByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception;

    /**
     * 3.新增单项的事项实例
     *
     * @param seriesinstId 必须参数   串联实例ID
     * @param itemVerId    必须参数   事项版本ID
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @throws Exception
     */
    AeaHiIteminst insertAeaHiIteminst(String seriesinstId, String itemVerId, String branchOrgMap) throws Exception;

    /**
     * 3.新增单项的事项实例并保存事项历史状态
     *
     * @param seriesinstId 必须参数   串联实例ID
     * @param itemVerId    必须参数   事项版本ID
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @param taskinstId   必须参数 流程节点实例ID
     * @param appinstId    必须参数 流程实例ID
     * @throws Exception
     */
    AeaHiIteminst insertAeaHiIteminstAndTriggerAeaLogItemStateHist(String seriesinstId, String itemVerId, String branchOrgMap, String taskinstId, String appinstId) throws Exception;

    /**
     * 4.新增并联的事项实例
     *
     * @param themeVerId   必须参数   主题版本ID
     * @param stageinstId  必须参数   阶段实例ID
     * @param itemVerId    必须参数    事项版本ID
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @throws Exception
     */
    AeaHiIteminst insertAeaHiIteminst(String themeVerId, String stageinstId, String itemVerId, String branchOrgMap) throws Exception;

    /**
     * 4.新增并联的事项实例并保存事项历史状态
     *
     * @param themeVerId   必须参数   主题版本ID
     * @param stageinstId  必须参数   阶段实例ID
     * @param itemVerId    必须参数    事项版本ID
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @param taskinstId   必须参数 流程节点实例ID
     * @param appinstId    必须参数 流程实例ID
     * @throws Exception
     */
    AeaHiIteminst insertAeaHiIteminstAndTriggerAeaLogItemStateHist(String themeVerId, String stageinstId, String itemVerId, String branchOrgMap, String taskinstId, String appinstId) throws Exception;


    /**
     * 5.批量新增单项的事项实例
     *
     * @param seriesinstId 必须参数   串联实例ID
     * @param itemVerIds   必须参数  事项版本ID列表
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @throws Exception
     */
    List<AeaHiIteminst> batchInsertAeaHiIteminst(String seriesinstId, List<String> itemVerIds, String branchOrgMap) throws Exception;

    /**
     * 5.批量新增单项的事项实例并保存事项历史状态
     *
     * @param seriesinstId 必须参数   串联实例ID
     * @param itemVerIds   必须参数  事项版本ID列表
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @param taskinstId   必须参数 流程节点实例ID
     * @param appinstId    必须参数 流程实例ID
     * @throws Exception
     */
    List<AeaHiIteminst> batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(String seriesinstId, List<String> itemVerIds, String branchOrgMap, String taskinstId, String appinstId) throws Exception;


    /**
     * 6.批量新增并联的事项实例
     *
     * @param themeVerId   必须参数   主题实例ID
     * @param stageinstId  必须参数   阶段实例ID
     * @param itemVerIds   必须参数 事项版本ID列表
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @throws Exception
     */
    List<AeaHiIteminst> batchInsertAeaHiIteminst(String themeVerId, String stageinstId, List<String> itemVerIds, String branchOrgMap) throws Exception;

    /**
     * 6.批量新增并联的事项实例
     *
     * @param themeVerId   必须参数   主题实例ID
     * @param stageinstId  必须参数   阶段实例ID
     * @param itemVerIds   必须参数 事项版本ID列表
     * @param branchOrgMap 非必须参数  分局承办组织和事项版本ID的Map
     * @param taskinstId   必须参数 流程节点实例ID
     * @param appinstId    必须参数 流程实例ID
     * @throws Exception
     */
    List<AeaHiIteminst> batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(String themeVerId, String stageinstId, List<String> itemVerIds, String branchOrgMap, String taskinstId, String appinstId) throws Exception;

    /**
     * 7.新增/修改 事项实例
     *
     * @param aeaHiIteminst
     * @throws Exception
     */
    public AeaHiIteminst saveAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception;

    /**
     * 8.根据ID删除事项实例
     *
     * @param id
     * @throws Exception
     */
    public void deleteAeaHiIteminst(@Param("id") String id) throws Exception;

    /**
     * 9.查询事项实例列表
     *
     * @param aeaHiIteminst
     * @return
     * @throws Exception
     */
    public List<AeaHiIteminst> listAeaHiIteminst(AeaHiIteminst aeaHiIteminst) throws Exception;

    /**
     * 10.根据ID查询事项实例
     *
     * @param id
     * @return
     * @throws Exception
     */
    public AeaHiIteminst getAeaHiIteminstById(@Param("id") String id) throws Exception;

    /**
     * 11.分页查询事项实例列表
     *
     * @param aeaHiIteminst
     * @return
     * @throws Exception
     */
    public PageInfo<AeaHiIteminst> listAeaHiIteminst(AeaHiIteminst aeaHiIteminst, int pageNum, int pageSize) throws Exception;

    /**
     * 12.修改事项实例状态
     *
     * @param iteminstId    必须参数  事项实例ID
     * @param iteminstState 非必须参数 事项实例状态
     * @throws Exception
     */
    public void updateAeaHiIteminstStateAndBusinessState(String iteminstId, String iteminstState) throws Exception;

    /**
     * 13.修改事项实例状态+新增通过流程触发变更事项状态记录
     *
     * @param iteminstId    事项实例ID
     * @param taskinstId    流程节点实例ID
     * @param appinstId     流程实例ID
     * @param iteminstState 事项实例状态
     * @param opuOrgId      办件所属委办局组织ID
     */
    void updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String iteminstState, String opuOrgId) throws Exception;

    /**
     * 14.修改事项实例状态+新增通过人工操作变更事项状态记录
     *
     * @param iteminstId     事项实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param iteminstState  事项实例状态
     * @param opuOrgId       办件所属委办局组织ID
     */
    void updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState, String opuOrgId) throws Exception;

    /**
     * 15.修改事项实例状态+特别程序，人工操作按钮触发状态改变还要关联特别程序表的busTableName
     *
     * @param iteminstId     事项实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param iteminstState  事项实例状态
     * @param opuOrgId       办件所属委办局组织ID
     * @param busTableName   导致状态变更的业务表表名
     * @param busPkName      导致状态变更的业务表主键名
     * @param busRecordId    导致状态变更的业务表记录ID
     */
    void updateAeaHiIteminstStateAndInsertOpsLinkBusAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState, String opuOrgId, String busTableName, String busPkName, String busRecordId) throws Exception;

    /**
     * 16.获取某项目并联/并行事项的事项实例列表
     *
     * @param projInfoId   必须参数 项目代码
     * @param stageId      必须参数 阶段ID
     * @param rootOrgId    必须参数 顶级机构ID
     * @param isOptionItem 可选参数 是否可选事项 0表示并联审批事项 1表示并行推进事项 2前置检查事项
     * @return
     * @throws Exception
     */
    Set<AeaHiIteminst> getAeaHiIteminstByProjInfoIdAndStageId(String projInfoId, String stageId, String isOptionItem, String rootOrgId) throws Exception;

    /**
     * 更新事项实例是否为容缺受理，1表示是容缺受理，0表示否
     *
     * @param iteminstId        事项实例ID
     * @param isToleranceAccept
     * @throws Exception
     */
    void updateAeaHiIteminstIsToleranceAccept(String iteminstId, String isToleranceAccept) throws Exception;

    long countApproveProjInfoListByUnitOrLinkman (String unitInfoId,String userInfoId,String isAll);

    /**
     * 查询项目相关的阶段事项办理状态
     *
     * @param stageIds   阶段id，可查多个
     * @param projInfoId 项目id
     * @return key: itemId, value: status:[1： 已办， 2： 办理中]
     */
    Map<String, HandleStatus> queryItemStatusByStageIdAndProjInfoId(List<String> stageIds, String projInfoId, String rootOrgId);


    /**
     * 根据状态统计办件数
     * @param states 状态数组。长度为0时，查询所有状态的办件数
     * @param rootOrgId
     * @return
     */
    int countTotalItemByStates(String[] states, String rootOrgId) throws Exception;

    /**
     * 根据状态统计当前月份办件数
     * @param states 状态数组。长度为0时，查询所有状态的办件数
     * @param rootOrgId
     * @return
     */
    int countCurrentMonthCountItemByStates(String[] states, String rootOrgId) throws Exception;

    /**
     * 修改事项实例状态+新增通过流程触发变更事项状态记录+新增材料补全历史记录
     *
     * @param iteminstId    事项实例ID
     * @param taskinstId    流程节点实例ID
     * @param appinstId     流程实例ID
     * @param iteminstState 事项实例状态
     */
    void updateAeaHiIteminstStateAndInsertTriggerAeaLogApplyinstStateHist(String iteminstId, String taskinstId, String appinstId, String iteminstState) throws Exception;

    /**
     * 修改事项实例状态+新增通过人工操作变更事项状态记录+新增材料补全历史记录
     *
     * @param iteminstId     事项实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param iteminstState  事项实例状态
     */
    void updateAeaHiIteminstStateAndInsertOpsAeaLogApplyinstStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String iteminstState) throws Exception;

    void batchDeleteAeaHiIteminst(String[] iteminstIds);
}
