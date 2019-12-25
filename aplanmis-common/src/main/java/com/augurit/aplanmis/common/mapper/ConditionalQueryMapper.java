package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bpm.common.domain.ActStoRemindAndReceiver;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.vo.conditional.ApplyInfo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.common.vo.conditional.PartsInfo;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/9/4
 */
@Mapper
@Repository
public interface ConditionalQueryMapper {
    List<ApplyInfo> listApplyinfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<PartsInfo> listPartsInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<String> queryApplyInfoTaskId(String applyinstId);

    List<String> queryItemInfoTaskId(@Param("iteminstId") String iteminstId, @Param("loginName") String loginName);

    List<String> queryViewId(String keyword);

    List<TaskInfo> listWaitDoTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listDoneTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listConcludedTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listAllPreliminaryTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listPreliminaryTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listDoneliminaryTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listDismissedApply(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listNeedCorrectTasks(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listNeedCompletedApply(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listNeedConfirmCompletedApply(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listPickupCheckTasksByPage(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    List<TaskInfo> listPickupCheckTasksFinishByPage(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 获取有待补正办件的实施主体
     *
     * @return
     * @throws Exception
     */
    List<OpuOmOrg> listNeedCorrectTasksOrganizer(@Param("windowId") String windowId) throws Exception;

    /**
     * 不予受理-部门事项列表
     *
     * @param conditionalQueryRequest
     * @return List<TaskInfo>
     */
    List<TaskInfo> listItemOutScope(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 部门-办结（不通过）
     *
     * @param conditionalQueryRequest
     * @return List<TaskInfo>
     */
    List<TaskInfo> listItemDisgree(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 容缺受理
     *
     * @param conditionalQueryRequest
     * @return List<TaskInfo>
     */
    List<TaskInfo> listItemToleranceAccept(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 容缺通过
     *
     * @param conditionalQueryRequest
     * @return List<TaskInfo>
     */
    List<TaskInfo> listItemAgreeTolerance(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 补正待确认件
     *
     * @param conditionalQueryRequest
     * @return List<TaskInfo>
     * @throws Exception
     */
    List<TaskInfo> listUnConfirmItemCorrect(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 未完成流程
     *
     * @param conditionalQueryRequest
     * @return
     */
    List<TaskInfo> listUnCompleteFlow(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 查询特别程序件
     *
     * @param conditionalQueryRequest 查询条件
     * @return List<TaskInfo>
     */
    List<TaskInfo> lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest);


    /**
     * 查询单项申报事项
     * @param conditionalQueryRequest 查询条件
     * @return
     */
    List<AeaItemBasic> listSeriesApplyItem(ConditionalQueryRequest conditionalQueryRequest);

    /**
     * 根据类型获取待取证
     * @param conditionalQueryRequest
     * @return
     */
    List<TaskInfo> listPickupCheckTasksByPageState(ConditionalQueryRequest conditionalQueryRequest);

    /**
     * 获取任务节点的提醒信息
     * @param receiverUserId
     * @param taskIdList
     * @return
     */
    List<ActStoRemindAndReceiver> listTaskRemindInfo(@Param("receiverUserId") String receiverUserId,@Param("taskIdList")List<String> taskIdList);


    /**
     *  我的草稿箱
     * @param conditionalQueryRequest
     * @return
     */
    List<ApplyInfo> listMyDrafts(ConditionalQueryRequest conditionalQueryRequest)throws Exception;

    /**
     * 查询窗口经办办件信息
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<PartsInfo> listWindowParts(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 辅线申报查询
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<ApplyInfo> listSublineApplyInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 辅线办件查询
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<PartsInfo> listSublinePartsInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 窗口不予受理办件查询
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<TaskInfo> listWindowItemOutScope(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 阶段办结申报
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<ApplyInfo> listStageConcludedApplyInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     * 根据查询条件查询中介服务事项待上传服务结果列表
     *
     * @param conditionalQueryRequest 查询条件
     * @return List<TaskInfo>
     */
    List<TaskInfo> listWaitUploadServiceResult(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     *
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<TaskInfo> listWaitCancelApplyInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     *
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<TaskInfo> listDoneCancelApplyInfo(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     *
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<TaskInfo> listDoneCancelApplyInfoByBm(ConditionalQueryRequest conditionalQueryRequest) throws Exception;

    /**
     *
     * @param conditionalQueryRequest
     * @return
     * @throws Exception
     */
    List<TaskInfo> listWaitCancelApplyInfoByBm(ConditionalQueryRequest conditionalQueryRequest) throws Exception;
}
