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
public interface ConditionalJumpService {

    ConditionalQueryDic applyConditionalQueryDic();

    List<AeaParStage> getApprovalStageByThemeId(String themeId);

    PageInfo listApplyinfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    void exportApplyinfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    ConditionalQueryDic parsConditionalQueryDic();

    PageInfo listPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    void exportPartsInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    List<BscDicCodeItem> listIndustryTree();

    String queryApplyInfoTaskId(String applyinstId);

    String queryItemInfoTaskId(String iteminstId, boolean entrust);

    String queryViewId(String keyword);

    ConditionalQueryDic taskConditionalQueryDic();

    PageInfo listWaitDoTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listDoneTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listConcludedTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listDismissedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listNeedCorrectTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

    PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception;

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
}

