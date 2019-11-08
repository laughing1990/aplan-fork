package com.augurit.aplanmis.front.queryView.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.queryView.vo.ConditionalQueryDic;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/9/4
 */
public interface ConditionalQueryService {

    /**
     * 申报查询条件数据
     * @return
     */
    ConditionalQueryDic applyConditionalQueryDic();

    /**
     * 根据主题ID获取主题下阶段
     * @param themeId
     * @return
     */
    List<AeaParStage> getApprovalStageByThemeId(String themeId);

    /**
     * 分页查询申报信息
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listApplyinfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 导出申报数据
     * @param conditionalQueryRequest
     * @param req
     * @param resp
     * @throws Exception
     */
    void exportApplyinfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 办件查询条件数据
     * @return
     */
    ConditionalQueryDic parsConditionalQueryDic();

    /**
     * 分页查询办件信息
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 导出办件数据
     * @param conditionalQueryRequest
     * @param req
     * @param resp
     * @throws Exception
     */
    void exportPartsInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 查询行业树
     * @return
     */
    List<BscDicCodeItem> listIndustryTree();

    /**
     * 根据applyinstId查询对应流程下任一taskId
     * @param applyinstId
     * @return
     */
    String queryApplyInfoTaskId(String applyinstId);

    /**
     * 根据iteminstId查询对应流程下任一taskId
     * @param iteminstId
     * @param entrust
     * @return
     */
    String queryItemInfoTaskId(String iteminstId, boolean entrust);

    /**
     * 根据关键字查视图id
     * @param keyword
     * @return
     */
    String queryViewId(String keyword);

    /**
     * 查询条件数据
     * @return
     */
    ConditionalQueryDic taskConditionalQueryDic();

    /**
     * 分页查询待办任务
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listWaitDoTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 分页查询已办任务
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listDoneTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 分页查询办结任务
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listConcludedTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     *
     * 分页查询待预审列表
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 分页查询不予受理列表
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listDismissedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 分页查询待补正办件列表
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listNeedCorrectTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 分页查询待补全申报列表
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     *
     * 分页查询补全待确认申报列表
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listNeedConfirmCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listPickupCheckTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;
    PageInfo listPickupCheckTasksByPageFinish(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listDoneliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 特殊程序件
     *
     * @param conditionalQueryRequest 查询条件
     * @param page                    分页
     * @return PageInfo
     */
    PageInfo lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 获取有待补正办件的承办单位
     *
     * @return
     * @throws Exception
     */
    List<OpuOmOrg> listNeedCorrectTasksOrganizer() throws Exception;

    /**
     * 办结（不通过）-部门-事项列表
     *
     * @param conditionalQueryRequest 查询参数
     * @param page                    分页参数
     * @return PageInfo
     */
    PageInfo listItemDisgree(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 不予受理(部门)-事项列表
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     */
    PageInfo listItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 容缺受理件-部门
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listItemToleranceAccept(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 容缺通过件-部门
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listItemAgreeTolerance(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 未完成申报流程-后台管理员使用
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listUnCompleteFlow(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    void doCompleteFlow(String taskId, String desActId) throws Exception;

    ResultForm checkDoCompleteFlow(String taskId) throws Exception;

    /**
     * 补正待确认件
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listUnConfirmItemCorrect(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 获取单项申报事项
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listSeriesApplyItem(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listPickupCheckTasksByPageState(ConditionalQueryRequest conditionalQueryRequest, Page page, String express) throws Exception;

    /**
     *  更新提醒的已读状态
     * @param remindReceiverIds
     * @throws Exception
     */
    void updateRemindRead(String remindReceiverIds)throws Exception;

    /**
     * 分页查询我的草稿箱
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listMyDrafts(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 短信发送申请
     * @param applyinstId
     * @param iteminstId
     * @throws Exception
     */
    void sendSms(String applyinstId,String iteminstId) throws Exception;

    /**
     * 分页查询窗口经办办件信息
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listWindowPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;


    /**
     * 导出窗口经办办件数据
     * @param conditionalQueryRequest
     * @param req
     * @param resp
     * @throws Exception
     */
    void exportWindowParts(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 分页查询辅线申报信息
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listSublineApplyInfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;


    /**
     * 导出辅线申报数据
     * @param conditionalQueryRequest
     * @param req
     * @param resp
     * @throws Exception
     */
    void exportSublineApplyInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 分页查询辅线办件
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo listSublinePartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    /**
     * 导出辅线办件查询结果
     * @param conditionalQueryRequest
     * @param req
     * @param resp
     * @throws Exception
     */
    void exportSublineParts(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 窗口不予受理办件
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     */
    PageInfo listWindowItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;


}

