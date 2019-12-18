package com.augurit.aplanmis.common.service.item;

import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaLogItemStateHistService {

    /**
     * 新增事项状态变更记录
     *
     * @param aeaLogItemStateHist
     */
    void insertAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * 修改事项状态变更记录
     *
     * @param aeaLogItemStateHist
     */
    void updateAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * 批量新增通过流程触发变更事项状态记录
     *
     * @param aeaLogItemStateHistList
     */
    void batchInsertTriggerAeaLogItemStateHist(List<AeaLogItemStateHist> aeaLogItemStateHistList);

    /**
     * 新增通过流程触发变更事项状态记录
     *
     * @param iteminstId 事项实例ID
     * @param taskinstId 流程节点实例ID
     * @param appinstId  业务流程模板实例ID
     * @param oldState   旧事项状态
     * @param newState   新事项状态
     * @param opuOrgId   办件所属委办局组织ID
     */
    void insertTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String oldState, String newState, String opuOrgId);

    /**
     * 构造对象流程触发变更事项状态记录
     *
     * @param iteminstId 事项实例ID
     * @param taskinstId 流程节点实例ID
     * @param appinstId  业务流程模板实例ID
     * @param oldState   旧事项状态
     * @param newState   新事项状态
     * @return
     */
    AeaLogItemStateHist constructTriggerAeaLogItemStateHist(String iteminstId, String taskinstId, String appinstId, String oldState, String newState, String opuOrgId);


    /**
     * 新增通过人工操作变更事项状态记录
     *
     * @param iteminstId     事项实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param oldState       旧事项状态
     * @param newState       新事项状态
     */
    void insertOpsAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuOrgId);

    /**
     * @param iteminstId     事项实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param oldState       旧事项状态
     * @param newState       新事项状态
     * @param busTableName   导致状态变更的业务表表名
     * @param busPkName      导致状态变更的业务表主键名
     * @param busRecordId    导致状态变更的业务表记录ID
     */
    void insertOpsLinkBusAeaLogItemStateHist(String iteminstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuOrgId, String busTableName, String busPkName, String busRecordId);


    /**
     * 获取事项初始状态记录
     *
     * @param iteminstId
     * @param appinstId
     * @return
     */
    AeaLogItemStateHist getInitStateAeaLogItemStateHist(String iteminstId, String appinstId);

    /**
     * 分页查询事项状态变更记录
     *
     * @param aeaLogItemStateHist
     * @param page
     * @return
     */
    PageInfo<AeaLogItemStateHist> listAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist, Page page);

    /**
     * 根据ID查询事项状态变更记录
     *
     * @param id 事项状态变更记录ID
     * @return
     */
    AeaLogItemStateHist getAeaLogItemStateHistById(String id);

    /**
     * 查询事项状态变更记录
     *
     * @param aeaLogItemStateHist
     * @return
     */
    List<AeaLogItemStateHist> findAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * 通过材料补正实例ID获取历史状态
     *
     * @param correctId
     * @return
     * @throws Exception
     */
    AeaLogItemStateHist getAeaLogItemStateHistByCorrectId(String correctId) throws Exception;

    /**
     * 获取事项实例某个状态的最新一次记录
     *
     * @param iteminstId
     * @param newState
     * @return
     * @throws Exception
     */
    AeaLogItemStateHist getLastAeaLogItemStateHistByState(String iteminstId, String newState) throws Exception;

    /**
     * 查询特别程序的意见列表
     *
     * @param taskInstId
     * @param rootOrgId
     * @return
     */
    List<SupplyOrSpacialCommentVo> findSpacialAeaLogItemStateHist(String taskInstId, String rootOrgId);

    /**
     * 查询补正的意见列表
     *
     * @param taskInstId
     * @param rootOrgId
     * @return
     */
    List<SupplyOrSpacialCommentVo> findItemCorrectStateHist(String taskInstId, String rootOrgId);

    void batchDeleteAeaLogItemStateHist(List<String> ids);

    List<AeaLogItemStateHist> findAeaLogItemStateHistByIteminstIds(String[] iteminstIds);
}
