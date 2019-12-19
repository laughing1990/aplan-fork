package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申请实例信息服务类
 * author:sam
 * 申请信息service
 */
public interface AeaHiApplyinstService {
    /**
     * 创建申请实例信息
     *
     * @param applySource   申报来源 必须
     * @param applySubject  申报主体 必须
     * @param linkmanInfoId 联系人ID 必须
     * @param branchOrgMap  分局承办
     * @param isTemporarySubmit 是否临时提交 1 是 0否（应用于暂存）
     * @return
     * @throws Exception
     */
    public AeaHiApplyinst createAeaHiApplyinst(String applySource, String applySubject, String linkmanInfoId, String isSeriesApprove, String branchOrgMap, String applyState, String isTemporarySubmit,String parentApplyinstId) throws Exception;

    /**
     * 创建申请实例信息并保存状态变更记录
     *
     * @param applySource   申报来源 必须
     * @param applySubject  申报主体 必须
     * @param linkmanInfoId 联系人ID 必须
     * @param branchOrgMap  分局承办
     * @param taskId        流程节点实例ID
     * @param appinstId     业务流程模板实例ID
     * @return
     * @throws Exception
     */
    AeaHiApplyinst createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(String applySource, String applySubject, String linkmanInfoId, String isSeriesApprove, String branchOrgMap, String taskId, String appinstId, String applyState, String opuWindowId,String parentApplyinstId) throws Exception;


    /**
     * 更新申请实例信息
     *
     * @param aeaHiApplyinst 申请实例对象
     * @return
     * @throws Exception
     */
    public void updateAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception;

    /**
     * 更新申请实例信息并保存状态变更记录
     *
     * @param aeaHiApplyinst       申请实例对象
     * @param aeaLogApplyStateHist 申请实例状态变更对象
     * @return
     * @throws Exception
     */
    void updateAeaHiApplyinstAndInsertTriggerAeaLogApplyStateHist(AeaHiApplyinst aeaHiApplyinst, AeaLogApplyStateHist aeaLogApplyStateHist) throws Exception;

    /**
     * 修改申请实例状态+新增通过流程触发变更申请状态记录
     *
     * @param applyinstId    申请实例ID
     * @param taskinstId     流程节点实例ID
     * @param appinstId      流程实例ID
     * @param applyinstState 申请实例状态
     */
    void updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(String applyinstId, String taskinstId, String appinstId, String applyinstState, String opuWindowId) throws Exception;

    /**
     * 修改申请实例状态+新增通过人工操作变更申请状态记录
     *
     * @param applyinstId    申请实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param applyinstState 申请实例状态
     */
    void updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String applyinstState, String opuWindowId) throws Exception;

    /**
     * 修改申请实例状态+特别程序，人工操作按钮触发状态改变还要关联特别程序表的busTableName
     *
     * @param applyinstId    申请实例ID
     * @param opsUserOpinion 填写意见内容
     * @param opsAction      操作动作
     * @param opsMemo        操作备注
     * @param applyinstState 申请实例状态
     * @param busTableName   导致状态变更的业务表表名
     * @param busPkName      导致状态变更的业务表主键名
     * @param busRecordId    导致状态变更的业务表记录ID
     */
    void updateAeaHiApplyinstStateAndInsertOpsLinkBusAeaLogItemStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String applyinstState, String opuWindowId, String busTableName, String busPkName, String busRecordId) throws Exception;

    /**
     * 根据申请实例ID 删除申请实例
     *
     * @param applyinstId 申请实例ID
     * @throws Exception
     */
    public void deleteAeaHiApplyinstById(String applyinstId) throws Exception;

    /**
     * 获取申请实例集合
     *
     * @param aeaHiApplyinst
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinst> listAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception;

    /**
     * 获取申请实例分页集合
     *
     * @param aeaHiApplyinst
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<AeaHiApplyinst> listAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst, Page page) throws Exception;

    /**
     * 根据申请实例ID获取当前申请实例
     *
     * @param applyinstId 申请实例ID
     * @return
     * @throws Exception
     */
    public AeaHiApplyinst getAeaHiApplyinstById(String applyinstId) throws Exception;

    /**
     * 根据申请编号获取申请实例
     *
     * @param applyinstCode 申请编号
     * @return
     * @throws Exception
     */
    public AeaHiApplyinst getAeaHiApplyinstByCode(String applyinstCode) throws Exception;

    /**
     * 根据联系人ID获取所有的申请实例
     *
     * @param linkmanInfoId 联系人ID
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinst> listAeaHiApplyinstByLinkmanInfoId(String linkmanInfoId) throws Exception;

    /**
     * 根据申请状态获取申请实例集合
     *
     * @param state 申请状态
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinst> listAeaHiApplyinstByState(String state, String rootOrgId) throws Exception;

    /**
     * 根据多个申请状态获取申请实例
     *
     * @param states 申请状态集合
     * @return
     * @throws Exception
     */
    public List<AeaHiApplyinst> listAeaHiApplyinstByStates(List<String> states, String rootOrgId) throws Exception;

    /**
     * 生成申请编号
     *
     * @return
     * @throws Exception
     */
//    public String generateApplyinstCode() throws Exception;

    /**
     * 获取各状态的办件统计数据
     *
     * @param applyinstState 办件状态
     * @return
     */
    public int countApplyinstByApplyinstState(String applyinstState, String rootOrgId) throws Exception;

    /**
     * 获取各状态的办件统计数据
     *
     * @param applyinstStates 办件状态集合
     * @return
     */
    public int countApplyinstByApplyinstStates(List<String> applyinstStates, String rootOrgId) throws Exception;

    /**
     * 获取本月各状态的办件统计数据
     *
     * @param applyinstStates 办件状态集合
     * @return
     */
    public int countCurrentMonthApplyinstByApplyinstStates(List<String> applyinstStates, String rootOrgId) throws Exception;

    /**
     * 根据项目ID查询当前登录用户的所有申请实例id集合
     *
     * @param projInfoId      项目ID
     * @param isSeriesApprove 0并联 1单项
     * @param unitInfoId      登录单位ID
     * @param userInfoId      登录用户ID
     * @return
     */
    List<AeaHiApplyinst> listApplyInstIdAndStateByProjInfoIdAndUser(String projInfoId, String isSeriesApprove, String unitInfoId, String userInfoId);

    /**
     * 根据项目ID获取所有的申请实例
     *
     * @param projInfoId
     * @param rootOrgId
     * @return
     */
    List<AeaHiApplyinst> getAllApplyinstesByProjInfoId(String projInfoId, String rootOrgId) throws Exception;

    /**
     *
     * 查询并联申报下的并行申报列表
     * @param applyinstId 并联申报实例ID
     * @param isTemporarySubmit 非必须参数  是否暂存 1 是  0否
     * @return
     */
    List<AeaHiApplyinst> getSeriesAeaHiApplyinstListByParentApplyinstId(String applyinstId,String isTemporarySubmit) throws Exception;

}
