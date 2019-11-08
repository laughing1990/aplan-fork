package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaLogApplyStateHistService {

    /**
     * 新增申请状态变更记录
     *
     * @param aeaLogApplyStateHist
     */
    void insertAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    /**
     * 修改申请状态变更记录
     *
     * @param aeaLogApplyStateHist
     */
    void updateAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    /**
     * 新增通过流程触发变更申请状态记录
     *
     * @param applyinstId 申请实例ID
     * @param taskinstId  流程节点实例ID
     * @param appinstId   流程实例ID
     * @param oldState    旧申请状态
     * @param newState    新申请状态
     * @param opuWindowId 办理窗口ID
     */
    void insertTriggerAeaLogApplyStateHist(String applyinstId, String taskinstId, String appinstId, String oldState, String newState, String opuWindowId);

    /**
     * 新增通过人工操作变更申请状态记录
     *
     * @param applyinstId    申请实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param oldState       旧申请状态
     * @param newState       新申请状态
     * @param opuWindowId    办理窗口ID
     */
    void insertOpsAeaLogApplyStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String newState, String opuWindowId);

    /**
     * @param applyinstId    申请实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param oldState       旧申请状态
     * @param newState       新申请状态
     * @param opuWindowId    办理窗口ID
     * @param busTableName   导致状态变更的业务表表名
     * @param busPkName      导致状态变更的业务表主键名
     * @param busRecordId    导致状态变更的业务表记录ID
     */
    void insertOpsLinkBusAeaLogApplyStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String oldState, String opuWindowId, String newState, String busTableName, String busPkName, String busRecordId);

    /**
     * 分页查询申请状态变更记录
     *
     * @param aeaLogApplyStateHist
     * @param page
     * @return
     */
    PageInfo<AeaLogApplyStateHist> listAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist, Page page);

    /**
     * 根据ID查询申请状态变更记录
     *
     * @param id 申请状态变更记录ID
     * @return
     */
    AeaLogApplyStateHist getAeaLogApplyStateHistById(String id);

    /**
     * 查询申请状态变更记录
     *
     * @param aeaLogApplyStateHist
     * @return
     */
    List<AeaLogApplyStateHist> findAeaLogApplyStateHist(AeaLogApplyStateHist aeaLogApplyStateHist);

    /**
     * 获取申请初始状态记录
     *
     * @param applyinstId
     * @param appinstId
     * @return
     */
    AeaLogApplyStateHist getInitStateAeaLogApplyStateHist(String applyinstId, String appinstId);

    /**
     * 通过材料补全实例ID获取历史状态
     *
     * @param applyinstCrrectId
     * @return
     * @throws Exception
     */
    AeaLogApplyStateHist getAeaLogApplyStateHistByApplyinstCorrectId(String applyinstCrrectId) throws Exception;


    /**
     * 通过材料补全实例ID获取历史状态列表
     *
     * @param applyinstCorrectId
     * @return
     * @throws Exception
     */
    AeaLogApplyStateHist getAeaLogApplyStateHistListByApplyinstCorrectId(String applyinstCorrectId) throws Exception;

    /**
     * 获取项目第一次申报某个阶段的记录
     *
     * @param projInfoId
     * @param stageId
     * @return 预受理时间
     */
    AeaLogApplyStateHist getProjFirstApplyStageLog(String projInfoId, String stageId);

    /**
     * 获取补全意见列表
     *
     * @param applyinstId
     * @param taskInstId
     * @param rootOrgId
     * @return
     */
    List<SupplyOrSpacialCommentVo> findApplyinstCorrectStateHist(String applyinstId, String taskInstId, String rootOrgId);
}
