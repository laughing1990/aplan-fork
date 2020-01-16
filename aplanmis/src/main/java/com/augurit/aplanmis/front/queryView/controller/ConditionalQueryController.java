package com.augurit.aplanmis.front.queryView.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.area.RegionService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
import com.augurit.aplanmis.front.queryView.vo.ConditionalQueryDic;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/9/4
 */
@Api(value = "条件查询相关接口", tags = "条件查询相关接口")
@RestController
@RequestMapping("/rest/conditional/query")
@Slf4j
public class ConditionalQueryController {

    @Autowired
    private ConditionalQueryService conditionalQueryService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;

    private static String applyViewId;

    private static String partsViewId;

    @GetMapping("/apply/dic/list")
    @ApiOperation(value = "获取申报相关查询条件的数据列表")
    public ResultForm getApplyConditionalQueryDic() {
        try {
            ConditionalQueryDic conditionalQueryDic = conditionalQueryService.applyConditionalQueryDic();
            return new ContentResultForm<>(true, conditionalQueryDic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/parts/dic/list")
    @ApiOperation(value = "获取办件相关查询条件的数据列表")
    public ResultForm getPartsConditionalQueryDic() {
        try {
            ConditionalQueryDic conditionalQueryDic = conditionalQueryService.parsConditionalQueryDic();
            return new ContentResultForm<>(true, conditionalQueryDic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/task/dic/list")
    @ApiOperation(value = "获取办理任务相关查询条件的数据列表")
    public ResultForm getTaskConditionalQueryDic() {
        try {
            ConditionalQueryDic conditionalQueryDic = conditionalQueryService.taskConditionalQueryDic();
            return new ContentResultForm<>(true, conditionalQueryDic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/theme/stage/list")
    @ApiOperation(value = "根据主题id获取阶段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getApprovalStageByThemeId(String themeId) {
        try {
            List<AeaParStage> stageList = conditionalQueryService.getApprovalStageByThemeId(themeId);
            return new ContentResultForm<>(true, stageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listApplyinfo")
    @ApiOperation(value = "根据查询条件获取申报列表")
    public ResultForm listApplyinfo(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo applyinfoList = conditionalQueryService.listApplyinfoByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, applyinfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/listParts")
    @ApiOperation(value = "根据查询条件获取办件列表")
    public ResultForm listParts(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo partList = conditionalQueryService.listPartsByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, partList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/export/applyinfo")
    @ApiOperation(value = "导出申报查询结果")
    public void exportApplyinfo(@PathParam("param") String param, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ConditionalQueryRequest conditionalQueryRequest = JSON.parseObject(param, ConditionalQueryRequest.class);
        try {
            conditionalQueryService.exportApplyinfo(conditionalQueryRequest, req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    @GetMapping("/export/parts")
    @ApiOperation(value = "导出办件查询结果")
    public void exportPartsInfo(@PathParam("param") String param, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ConditionalQueryRequest conditionalQueryRequest = JSON.parseObject(param, ConditionalQueryRequest.class);
        try {
            conditionalQueryService.exportPartsInfo(conditionalQueryRequest, req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/tree/industry")
    @ApiOperation(value = "获取国标行业树结构")
    public ResultForm listIndustryTree() {
        try {
            List<BscDicCodeItem> industryTreeNodes = conditionalQueryService.listIndustryTree();
            return new ContentResultForm<>(true, industryTreeNodes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/queryApplyInfoTaskId")
    @ApiOperation("所有申报-查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm queryApplyInfoTaskId(String applyinstId) {

        try {
            String taskId = conditionalQueryService.queryApplyInfoTaskId(applyinstId);
            if (StringUtils.isBlank(taskId)) {
                throw new RuntimeException("找不到对应的taskId");
            }
            String viewId;
            if (StringUtils.isNotBlank(applyViewId)) {
                viewId = applyViewId;
            } else {
                viewId = conditionalQueryService.queryViewId("办结");
                applyViewId = viewId;
            }

            if (StringUtils.isBlank(viewId)) {

                if (StringUtils.isNotBlank(partsViewId)) {
                    viewId = partsViewId;
                } else {
                    viewId = conditionalQueryService.queryViewId("所有办件");
                    partsViewId = viewId;
                }
            }
            if (StringUtils.isBlank(viewId)) {
                throw new RuntimeException("找不到对应的viewId");
            }
            HashMap map = new HashMap();
            map.put("taskId", taskId);
            map.put("viewId", viewId);
            return new ContentResultForm<>(true, map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/queryItemInfoTaskId")
    @ApiOperation("所有办件-查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "handler", value = "是否当前用户办理的", dataType = "boolean", paramType = "query", readOnly = true),
    })
    public ResultForm queryItemInfoTaskId(String iteminstId, boolean handler) {

        try {
            String taskId = conditionalQueryService.queryItemInfoTaskId(iteminstId, handler);
            if (StringUtils.isBlank(taskId)) {
                throw new RuntimeException("找不到对应的taskId");
            }
            String viewId;
            if (StringUtils.isNotBlank(partsViewId)) {
                viewId = partsViewId;
            } else {
                viewId = conditionalQueryService.queryViewId("所有办件");
                partsViewId = viewId;
            }
            if (StringUtils.isBlank(viewId)) {
                throw new RuntimeException("找不到对应的viewId");
            }
            HashMap map = new HashMap();
            map.put("taskId", taskId);
            map.put("viewId", viewId);
            return new ContentResultForm<>(true, map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWaitDoTasks")
    @ApiOperation(value = "根据查询条件获取待办列表")
    public ResultForm listWaitDoTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo waitDoTasks = conditionalQueryService.listWaitDoTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listDoneTasks")
    @ApiOperation(value = "根据查询条件获取已办列表")
    public ResultForm listDoneTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo doneTasks = conditionalQueryService.listDoneTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, doneTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listConcludedTasks")
    @ApiOperation(value = "根据查询条件获取办结列表")
    public ResultForm listConcludedTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo concludedTasks = conditionalQueryService.listConcludedTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, concludedTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listPreliminaryTasks")
    @ApiOperation(value = "根据查询条件获取待预审列表")
    public ResultForm listPreliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo preliminaryTasks = conditionalQueryService.listPreliminaryTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, preliminaryTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listDismissedApply")
    @ApiOperation(value = "根据查询条件获取不予受理列表")
    public ResultForm listDismissedApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listDismissedApplyByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedCorrectTasks")
    @ApiOperation(value = "根据查询条件获取待补正办件列表")
    public ResultForm listNeedCorrectTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needCorrectTasks = conditionalQueryService.listNeedCorrectTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needCorrectTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedCompletedApply")
    @ApiOperation(value = "根据查询条件获取待补全申报列表")
    public ResultForm listNeedCompletedApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needCompletedApply = conditionalQueryService.listNeedCompletedApplyByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needCompletedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedConfirmCompletedApply")
    @ApiOperation(value = "根据查询条件获取补全待确认申报列表")
    public ResultForm listNeedConfirmCompletedApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needConfirmCompletedApply = conditionalQueryService.listNeedConfirmCompletedApplyByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needConfirmCompletedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listPickupCheckTasks")
    @ApiOperation(value = "根据查询条件获取待取证列表")
    public ResultForm listPickupCheckTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo doneTasks = conditionalQueryService.listPickupCheckTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, doneTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listPickupCheckTasksWin")
    @ApiOperation(value = "窗口取件列表")
    public ResultForm listPickupCheckTasksWin(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo doneTasks = conditionalQueryService.listPickupCheckTasksByPageState(conditionalQueryRequest, page, "1");
            return new ContentResultForm<>(true, doneTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listPickupCheckTasksExpress")
    @ApiOperation(value = "邮寄取件列表")
    public ResultForm listPickupCheckTasksExpress(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo doneTasks = conditionalQueryService.listPickupCheckTasksByPageState(conditionalQueryRequest, page, "0");
            return new ContentResultForm<>(true, doneTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listPickupCheckTasksFinish")
    @ApiOperation(value = "已取件列表")
    public ResultForm listPickupCheckFinishTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo doneTasks = conditionalQueryService.listPickupCheckTasksByPageFinish(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, doneTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listDoneliminaryTasks")
    @ApiOperation(value = "根据查询条件获取网上已预审列表")
    public ResultForm listDoneliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo preliminaryTasks = conditionalQueryService.listDoneliminaryTasks(conditionalQueryRequest, page);
            //过滤 来源为网上的

            return new ContentResultForm<>(true, preliminaryTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    //todo----
    @GetMapping("/lisSpecialProcedureTasks")
    @ApiOperation(value = "根据查询条件获取特殊程序件列表")
    public ResultForm lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo preliminaryTasks = conditionalQueryService.lisSpecialProcedureTasks(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, preliminaryTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedCorrectTasksOrganizer")
    @ApiOperation(value = "获取有待补正办件的实施主体")
    public ResultForm listNeedCorrectTasksOrganizer() {
        try {
            List<OpuOmOrg> list = conditionalQueryService.listNeedCorrectTasksOrganizer();
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listItemDisgree")
    @ApiOperation(value = "根据查询条件获取-办结（不通过）事项--部门-事项")
    public ResultForm listItemDisgree(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemDisgree(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listItemOutScope")
    @ApiOperation(value = "根据查询条件获取 不予受理列表-部门-事项")
    public ResultForm listItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemOutScope(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listItemToleranceAccept")
    @ApiOperation(value = "根据查询条件获取 容缺受理件")
    public ResultForm listItemToleranceAccept(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemToleranceAccept(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listItemAgreeTolerance")
    @ApiOperation(value = "根据查询条件获取 容缺通过件")
    public ResultForm listItemAgreeTolerance(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemAgreeTolerance(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listUnCompleteFlow")
    @ApiOperation(value = "根据查询条件获取 未完成申报流程")
    public ResultForm listUnCompleteFlow(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo result = conditionalQueryService.listUnCompleteFlow(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listUnConfirmItemCorrect")
    @ApiOperation(value = "根据查询条件获取 补正待确认件---事项")
    public ResultForm listUnConfirmItemCorrect(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listUnConfirmItemCorrect(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/checkDoCompleteFlow")
    @ApiOperation(value = "检查当前流程节点是否可以结束掉")
    public ResultForm checkDoCompleteFlow(String taskId) {
        try {
            return conditionalQueryService.checkDoCompleteFlow(taskId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/doCompleteFlow")
    @ApiOperation(value = "结束流程节点，推动流程流转")
    public ResultForm doCompleteFlow(String taskId, String desActId) {
        try {
            conditionalQueryService.doCompleteFlow(taskId, desActId);
            return new ResultForm(true, "操作成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listSeriesApplyItem")
    @ApiOperation(value = "根据查询条件获取单项申报事项列表")
    public ResultForm listSeriesApplyItem(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo seriesApplyItem = conditionalQueryService.listSeriesApplyItem(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, seriesApplyItem);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/updateRemindRead")
    @ApiOperation(value = "更新提醒的已读状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "remindReceiverIds", value = "提醒信息id集，以,隔开", required = true, dataType = "string")
    })
    public ResultForm updateRemindRead(String remindReceiverIds) {
        try {
            conditionalQueryService.updateRemindRead(remindReceiverIds);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/listMyDrafts")
    @ApiOperation(value = "根据查询条件获取我的草稿箱列表")
    public ResultForm listMyDrafts(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo myDrafts = conditionalQueryService.listMyDrafts(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, myDrafts);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWindowDrafts")
    @ApiOperation(value = "根据查询条件获取当前窗口草稿箱列表")
    public ResultForm listWindowDrafts(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
            if (currentUserWindow == null) throw new Exception("请配置当前用户归属窗口");
            conditionalQueryRequest.setWindowId(currentUserWindow.getWindowId());
            PageInfo myDrafts = conditionalQueryService.listMyDrafts(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, myDrafts);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/sendSms")
    @ApiOperation(value = "短信提醒")
    public ResultForm sendSms(String applyinstId, String iteminstId) {
        try {
            conditionalQueryService.sendSms(applyinstId, iteminstId);
            return new ResultForm(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWindowParts")
    @ApiOperation(value = "根据查询条件获取窗口经办办件列表")
    public ResultForm listWindowParts(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo partList = conditionalQueryService.listWindowPartsByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, partList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/export/windowParts")
    @ApiOperation(value = "导出窗口经办办件查询结果")
    public void exportWindowParts(@PathParam("param") String param, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ConditionalQueryRequest conditionalQueryRequest = JSON.parseObject(param, ConditionalQueryRequest.class);
        try {
            conditionalQueryService.exportWindowParts(conditionalQueryRequest, req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/listSublineApplyInfo")
    @ApiOperation(value = "根据查询条件获取辅线申报列表")
    public ResultForm listSublineApplyInfo(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo applyinfoList = conditionalQueryService.listSublineApplyInfoByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, applyinfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/export/sublineApplyInfo")
    @ApiOperation(value = "导出辅线申报查询结果")
    public void exportSublineApplyInfo(@PathParam("param") String param, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ConditionalQueryRequest conditionalQueryRequest = JSON.parseObject(param, ConditionalQueryRequest.class);
        try {
            conditionalQueryService.exportSublineApplyInfo(conditionalQueryRequest, req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/listSublineParts")
    @ApiOperation(value = "根据查询条件获取辅线办件列表")
    public ResultForm listSublineParts(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo partsInfoList = conditionalQueryService.listSublinePartsByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, partsInfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/export/sublineParts")
    @ApiOperation(value = "导出辅线办件查询结果")
    public void exportSublineParts(@PathParam("param") String param, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ConditionalQueryRequest conditionalQueryRequest = JSON.parseObject(param, ConditionalQueryRequest.class);
        try {
            conditionalQueryService.exportSublineParts(conditionalQueryRequest, req, resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/listWindowItemOutScope")
    @ApiOperation(value = "根据查询条件获取窗口不予受理办件列表")
    public ResultForm listWindowItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo list = conditionalQueryService.listWindowItemOutScope(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listBscDicRegion")
    @ApiOperation("获取行政区划数据字典列表")
    public ResultForm listBscDicRegion() {
        try {
            String orgId = SecurityContext.getCurrentOrgId();
            List<BscDicRegion> list = regionService.getBscDicRegionTreeList(orgId);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/listStageConcludedApplyInfo")
    @ApiOperation(value = "根据查询条件获取阶段已办结的申报")
    public ResultForm listStageConcludedApplyInfo(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo partsInfoList = conditionalQueryService.listStageConcludedApplyInfoByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, partsInfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/listWaitUploadServiceResult")
    @ApiOperation(value = "根据查询条件查询中介服务事项待上传服务结果列表")
    public ResultForm listWaitUploadServiceResult(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
//            PageInfo waitDoTasks = conditionalQueryService.listWaitDoTasksByPage(conditionalQueryRequest, page);
            PageInfo waitDoTasks = conditionalQueryService.listWaitUploadServiceResult(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWaitCancelTasks")
    @ApiOperation(value = "根据查询条件获取待办列表")
    public ResultForm listWaitCancelTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo waitDoTasks = conditionalQueryService.listWaitCancelTasks(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listDoneCancelTasks")
    @ApiOperation(value = "根据查询条件获取已办列表")
    public ResultForm listDoneCancelTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo waitDoTasks = conditionalQueryService.listDoneCancelTasks(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listDoneCancelTasksByBm")
    @ApiOperation(value = "根据查询条件获取已办列表")
    public ResultForm listDoneCancelApplyInfoByBm(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo waitDoTasks = conditionalQueryService.listDoneCancelApplyInfoByBm(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWaitCancelTasksByBm")
    @ApiOperation(value = "根据查询条件获取待办列表")
    public ResultForm listWaitCancelTasksByBm(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo waitDoTasks = conditionalQueryService.listWaitCancelApplyInfoByBm(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, waitDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @PostMapping("/listAgencyDoTasks")
    @ApiOperation(value = "根据查询条件获取项目代办列表")
    public ResultForm listAgencyDoTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo agencyDoTasks = conditionalQueryService.listAgencyDoTasks(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, agencyDoTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/agencyState/list")
    @ApiOperation(value = "查询代办状态")
    public ResultForm getAgencyState() {
        return new ContentResultForm<>(true, AgencyState.getAgencyStateMap());
    }

    @PostMapping("/listItemFills")
    @ApiOperation(value = "根据查询条件获取容缺审核列表")
    public ResultForm listItemFills(AeaHiItemFill aeaHiItemFill, Page page) {
        try {
            PageInfo listItemFills = conditionalQueryService.listItemFills(aeaHiItemFill, page);
            return new ContentResultForm<>(true, listItemFills);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
}