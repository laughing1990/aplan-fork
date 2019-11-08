package com.augurit.efficiency.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.efficiency.service.WinEfficiencySupervisionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwn on 2019/9/17.
 */
@RestController
@Slf4j
@RequestMapping("/win/efficiency/supervision")
@Api(value = "窗口效能督查接口", tags = "窗口效能督查接口")
public class WinEfficiencySupervisionController {

    private static String WIN_EFFICIENCY_SUPERVISION_INDEX = "efficiency/windowPeopleEffect";
    private static String SINGLE_WIN_EFFICIENCY_SUPERVISION_INDEX = "efficiency/singleWinEffect";

    private static String QUERY_PRELIMINARY_TASKS_INDEX = "view/queryAllPreliminaryTasksIndex";
    private static String QUERY_DISMISSED_APPLY_INDEX = "view/queryAllDismissedApplyIndex";
    private static String QUERY_NEEDCORRECT_APPLY_INDEX = "view/queryAllNeedCompletedApplyIndex";
    private static String QUERY_NEEDCONFIRMCOMPLETED_APPLY_INDEX = "view/queryAllNeedConfirmCompletedApplyIndex";
    private static String QUERY_WARN_APPLY_INDEX = "view/queryWarnApplyIndex";
    private static String QUERY_OVERDUE_APPLY_INDEX = "view/queryOverdueApplyIndex";

    @Autowired
    private WinEfficiencySupervisionService winEfficiencySupervisionService;

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView(WIN_EFFICIENCY_SUPERVISION_INDEX);
    }

    @RequestMapping("/winIndex")
    @ApiIgnore
    public ModelAndView winIndex() {
        return new ModelAndView(SINGLE_WIN_EFFICIENCY_SUPERVISION_INDEX);
    }

    @GetMapping("/queryPreliminaryTasksIndex.html")
    @ApiIgnore
    public ModelAndView queryPreliminaryTasksIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_PRELIMINARY_TASKS_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/queryDismissedApplyIndex.html")
    @ApiIgnore
    public ModelAndView queryDismissedApplyIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_DISMISSED_APPLY_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/queryNeedCompletedApplyIndex.html")
    @ApiIgnore
    public ModelAndView queryNeedCorrectApplyIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_NEEDCORRECT_APPLY_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/queryNeedConfirmCompletedApplyIndex.html")
    @ApiIgnore
    public ModelAndView queryNeedConfirmCompletedApplyIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_NEEDCONFIRMCOMPLETED_APPLY_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/queryWarnApplyIndex.html")
    @ApiIgnore
    public ModelAndView queryWarnApplyIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_WARN_APPLY_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/queryOverdueApplyIndex.html")
    @ApiIgnore
    public ModelAndView queryOverdueApplyIndex(String entrust) {
        ModelAndView modelAndView = new ModelAndView(QUERY_OVERDUE_APPLY_INDEX);
        if ("y".equals(entrust)) {
            modelAndView.addObject("entrust", true);
        } else {
            modelAndView.addObject("entrust", false);
        }
        return modelAndView;
    }

    @GetMapping("/listPreliminaryTasks")
    @ApiOperation(value = "根据查询条件获取待预审列表")
    public ResultForm listPreliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo preliminaryTasks = winEfficiencySupervisionService.listPreliminaryTasksByPage(conditionalQueryRequest, page);
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
            PageInfo dismissedApply = winEfficiencySupervisionService.listDismissedApplyByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedCompletedApply")
    @ApiOperation(value = "根据查询条件获取待补全申报列表")
    public ResultForm listNeedCompletedApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needCompletedApply = winEfficiencySupervisionService.listNeedCompletedApplyByPage(conditionalQueryRequest, page);
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
            PageInfo needConfirmCompletedApply = winEfficiencySupervisionService.listNeedConfirmCompletedApplyByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needConfirmCompletedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listWarnApply")
    @ApiOperation(value = "根据查询条件获取预警申报列表")
    public ResultForm listWarnApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needConfirmCompletedApply = winEfficiencySupervisionService.listWarnApply(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needConfirmCompletedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listOverdueApply")
    @ApiOperation(value = "根据查询条件获取逾期申报列表")
    public ResultForm listOverdueApply(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needConfirmCompletedApply = winEfficiencySupervisionService.listOverdueApply(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needConfirmCompletedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getWinShenbaoStatistics")
    @ApiOperation(value = "窗口申报数据统计", notes = "所有窗口申报数据统计（包括网上待预审，待补全申报，补全待确认申报，不予受理，申报时限预警，申报时限逾期）")
    public ResultForm getWinShenbaoStatistics() {
        try {
            List<ApplyStatisticsVo> applyStatistics = winEfficiencySupervisionService.getApplyStatistics();
            return new ContentResultForm<>(true, applyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("所有窗口申报数据统计异常", e);
            return new ContentResultForm<>(false, null, "窗口申报数据统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getCurWinShenbaoStatistics")
    @ApiOperation(value = "当前窗口申报数据统计", notes = "当前窗口申报数据统计（包括网上待预审，待补全申报，补全待确认申报，不予受理，申报时限预警，申报时限逾期）")
    public ResultForm getCurWinShenbaoStatistics() {
        try {
            List<ApplyStatisticsVo> applyStatistics = winEfficiencySupervisionService.getCurWinApplyStatistics();
            return new ContentResultForm<>(true, applyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("当前窗口申报数据统计异常", e);
            return new ContentResultForm<>(false, null, "窗口申报数据统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getWinShouliStatistics")
    @ApiOperation(value = "窗口受理申报统计", notes = "各窗口的接件总数和不予受理数 和 各窗口的受理申报数，网厅申报数，现场申报数，不予受理数，受理率，逾期数，逾期率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getWinShouliStatistics(String startTime, String endTime) {
        ResultForm checkForm = checkTimeParam(startTime, endTime);
        if (checkForm != null) {
            return checkForm;
        }

        try {
            List<WinApplyStatisticsVo> winApplyStatistics = winEfficiencySupervisionService.getWinApplyStatistics(startTime, endTime);
            return new ContentResultForm<>(true, winApplyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("窗口受理申报统计异常", e);
            return new ContentResultForm<>(false, null, "窗口受理申报统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getWinShouliStatisticsByMonth")
    @ApiOperation(value = "月度受理统计", notes = "本窗口受理申报数，办结申报数，逾期申报数，不予受理申报数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getWinShouliStatisticsByMonth(String startTime, String endTime) {
        ResultForm checkForm = checkTimeParam(startTime, endTime, "yyyy-MM");
        if (checkForm != null) {
            return checkForm;
        }
        try {
            startTime = DateUtils.convertDateToString(DateUtils.convertStringToDate(startTime, "yyyy-MM"), "yyyy-MM");
            endTime = DateUtils.convertDateToString(DateUtils.convertStringToDate(endTime, "yyyy-MM"), "yyyy-MM");
            List<WinMonthApplyStatisticsVo> winApplyStatisticsByMonth = winEfficiencySupervisionService.getWinApplyStatisticsByMonth(startTime, endTime);
            return new ContentResultForm<List>(true, winApplyStatisticsByMonth, "查询成功");
        } catch (Exception e) {
            log.error("月度受理统计异常", e);
            return new ContentResultForm<>(false, null, "月度受理统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getRegionShenbaoStatistics")
    @ApiOperation(value = "地区申报统计", notes = "各地区申报数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getRegionShenbaoStatistics(String startTime, String endTime) {
        ResultForm checkForm = checkTimeParam(startTime, endTime);
        if (checkForm != null) {
            return checkForm;
        }
        try {
            List<RegionApplyStatisticsVo> regionApplyStatistics = winEfficiencySupervisionService.getRegionApplyStatistics(startTime, endTime);
            return new ContentResultForm<>(true, regionApplyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("地区申报统计异常", e);
            return new ContentResultForm<>(false, null, "地区申报统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getThemeShenbaoStatistics")
    @ApiOperation(value = "主题申报统计", notes = "各主题的申报数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getThemeShenbaoStatistics(String startTime, String endTime) {
        ResultForm checkForm = checkTimeParam(startTime, endTime);
        if (checkForm != null) {
            return checkForm;
        }
        try {
            Map<String, Object> themeApplyStatistics = winEfficiencySupervisionService.getThemeApplyStatistics(startTime, endTime);
            return new ContentResultForm<>(true, themeApplyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("主题申报统计异常", e);
            return new ContentResultForm<>(false, null, "主题申报统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getStageShenbaoStatistics")
    @ApiOperation(value = "阶段申报统计", notes = "各阶段的申报数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getStageShenbaoStatistics(String startTime, String endTime) {
        ResultForm checkForm = checkTimeParam(startTime, endTime);
        if (checkForm != null) {
            return checkForm;
        }
        try {
            Map<String, Object> stageApplyStatistics = winEfficiencySupervisionService.getStageApplyStatistics(startTime, endTime);
            return new ContentResultForm<>(true, stageApplyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("阶段申报统计异常", e);
            return new ContentResultForm<>(false, null, "阶段申报统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getThemShenbaoExceptionStatistics")
    @ApiOperation(value = "主题阶段申报异常排名", notes = "各主题阶段的申报异常情况（不予受理数，逾期数）")
    public ResultForm getThemShenbaoExceptionStatistics() {
        try {
            List<ApplyThemeExceptionVo> applyThemeExceptionVos = winEfficiencySupervisionService.queryApplyThemeExceptionStatistics();
            return new ContentResultForm<>(true, applyThemeExceptionVos, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败！" + e.getMessage());
        }
    }

    @GetMapping("/getProjStatisticsByApplyType")
    @ApiOperation(value = "项目串并联申报统计", notes = "项目串并联申报统计（全单项申报项目，串并联申报项目，全并联申报项目）")
    public ResultForm getProjStatisticsByApproveType() {
        try {
            List<Map<String, Object>> result = winEfficiencySupervisionService.staticsticsByApplyType();
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败！" + e.getMessage());
        }
    }

    @GetMapping("/getShenbaoStatisticsByApplySource")
    @ApiOperation(value = "申报来源统计", notes = "申报来源统计（网上大厅，现场大厅）")
    public ResultForm getShenbaoStatisticsByApplySource() {
        try {
            List<Map<String, Object>> result = winEfficiencySupervisionService.staticsticsByApplySource();
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败！" + e.getMessage());
        }
    }

    @GetMapping("/getWinAcceptDealStatistics")
    @ApiOperation(value = "所有服务窗口的接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D】", dataType = "string", paramType = "String")
    })
    public ResultForm getWinAcceptDealStatistics(String type, String startTime, String endTime) {
        if (StringUtils.isBlank(type)) {
            ResultForm resultForm = checkTimeParam(startTime, endTime);
            if (resultForm != null) return resultForm;
        }
        try {
            List<Map<String, Object>> acceptStaticsticsByWin = winEfficiencySupervisionService.getAcceptDealStatisticsByWin(type, startTime, endTime);
            return new ContentResultForm<>(true, acceptStaticsticsByWin, "查询成功！");
        } catch (Exception e) {
            log.error("服所有服务窗口的接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "所有服务窗口的接件受理情况统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getWinAcceptStatisticsByDay")
    @ApiOperation(value = "服务窗口的每日接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "windowId", value = "窗口ID", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getWinAcceptStatisticsByDay(String windowId, String type, String startTime, String endTime) {
        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "请求缺少参数!");
        }
        if (StringUtils.isBlank(type)) {
            ResultForm resultForm = checkTimeParam(startTime, endTime);
            if (resultForm != null) return resultForm;
        }
        try {
            List<Map<String, Object>> acceptStaticsticsByDay = winEfficiencySupervisionService.getAcceptStatisticsByDay(windowId, type, startTime, endTime);
            return new ContentResultForm<>(true, acceptStaticsticsByDay, "查询成功！");
        } catch (Exception e) {
            log.error("服务窗口的每日接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "服务窗口的每日接件受理情况统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getCurWinAcceptStatisticsByDay")
    @ApiOperation(value = "当前服务窗口的每日接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D】", dataType = "string", paramType = "String"),
    })
    public ResultForm getCurWinAcceptStatisticsByDay(String type, String startTime, String endTime) {
        if (StringUtils.isBlank(type)) {
            ResultForm resultForm = checkTimeParam(startTime, endTime);
            if (resultForm != null) return resultForm;
        }
        try {
            List<Map<String, Object>> acceptStaticsticsByDay = winEfficiencySupervisionService.getCurWinAcceptStatisticsByDay(type, startTime, endTime);
            return new ContentResultForm<>(true, acceptStaticsticsByDay, "查询成功！");
        } catch (Exception e) {
            log.error("当前服务窗口的每日接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "当前服务窗口的每日接件受理情况统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getThemeApplyStatistics")
    @ApiOperation(value = "主题申报的接件受理情况统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D】", dataType = "string", paramType = "String")
    })
    public ResultForm getThemeApplyStatistics(String type, String startTime, String endTime) throws Exception {
        if (StringUtils.isBlank(type)) {
            ResultForm resultForm = checkTimeParam(startTime, endTime);
            if (resultForm != null) return resultForm;
        }
        try {

            Map<String, Object> applyStatisticsByTheme = winEfficiencySupervisionService.getApplyStatisticsByTheme(type, startTime, endTime);
            return new ContentResultForm<>(true, applyStatisticsByTheme, "查询成功！");
        } catch (Exception e) {
            log.error("主题申报的接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "主题申报的接件受理情况统计异常，" + e.getMessage());
        }
    }

    /**
     * 检查时间参数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    private ResultForm checkTimeParam(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return new ContentResultForm<>(false, null, "请求缺少参数！");
        }

        if (startTime.compareTo(endTime) > 0) {
            return new ContentResultForm<>(false, null, "开始日期大于结束日期！");
        }
        try {
            Date _startTime = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date _endTime = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
        } catch (ParseException e) {
            return new ContentResultForm<>(false, null, "传入日期格式错误！");
        }

        return null;
    }

    /**
     * 检查时间参数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    private ResultForm checkTimeParam(String startTime, String endTime, String format) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return new ContentResultForm<>(false, null, "请求缺少参数！");
        }

        if (startTime.compareTo(endTime) > 0) {
            return new ContentResultForm<>(false, null, "开始日期大于结束日期！");
        }
        try {
            Date _startTime = DateUtils.convertStringToDate(startTime, format);
            Date _endTime = DateUtils.convertStringToDate(endTime, format);
        } catch (ParseException e) {
            return new ContentResultForm<>(false, null, "传入日期格式错误！");
        }

        return null;
    }

    //新版2019-10-17
    @GetMapping("/getWinAcceptStatistics")
    @ApiOperation(value = "统计接件受理情况", notes = "统计接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getWinAcceptStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getWinAcceptStatistics(startTime, endTime, type, false);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("统计接件受理情况", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getWinAcceptTotalStatistics")
    @ApiOperation(value = "窗口接件量统计", notes = "窗口接件量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getWinAcceptTotalStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getWinAcceptRate(startTime, endTime, type);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("窗口接件量统计异常", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getThemeDistributionStatistics")
    @ApiOperation(value = "主题分布情况", notes = "主题分布情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D，为空自定义时间】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getThemeDistributionStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getThemeDistributionStatistics(startTime, endTime, type);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("主题分布情况统计异常", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getStageApplyStatistics")
    @ApiOperation(value = "阶段申报数情况", notes = "阶段申报数情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D，为空自定义时间】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getStageApplyStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getStageApplyStatisticsByType(startTime, endTime, type);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("阶段申报数情况统计异常", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getCurrentWinAcceptStatistics")
    @ApiOperation(value = "统计接件受理情况", notes = "统计接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getCurrentWinAcceptStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getCurrentWinAcceptStatistics(startTime, endTime, type, true);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("统计接件受理情况", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getCurrentWinStageAcceptStatistics")
    @ApiOperation(value = "窗口阶段受理情况", notes = "窗口阶段受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getCurrentWinStageAcceptStatistics(String startTime, String endTime, String type) {
        try {
            Map<String, Object> result = winEfficiencySupervisionService.getWinStageAcceptStatistics(startTime, endTime, type, true);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("窗口阶段受理情况", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getWinStageAvgLimitTimeStatistics")
    @ApiOperation(value = "申报阶段平均用时", notes = "申报阶段平均用时")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "isCurrent", value = "是否查询当前窗口", dataType = "boolean", paramType = "boolean", required = false),
            @ApiImplicitParam(name = "windowId", value = "指定窗口Id", dataType = "string", paramType = "String", required = false),
    })
    public ResultForm getWinStageAvgLimitTimeStatistics(String startTime, String endTime, String type, @RequestParam(required = false,value = "false")boolean isCurrent, @RequestParam(required = false)String windowId) throws Exception {

        try {
            Map<String, Object> result = winEfficiencySupervisionService.getWinStageAvgLimitTimeStatistics(startTime, endTime, type,isCurrent,windowId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("申报阶段平均用时", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getWinStageLimitTimeStatistics")
    @ApiOperation(value = "申报阶段时限", notes = "申报阶段时限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "isCurrent", value = "是否查询当前窗口", dataType = "boolean", paramType = "boolean", required = false),
            @ApiImplicitParam(name = "windowId", value = "指定窗口Id", dataType = "string", paramType = "String", required = false),
    })
    public ResultForm getWinStageLimitTimeStatistics(String startTime, String endTime, String type, @RequestParam(required = false,value = "false")boolean isCurrent,@RequestParam(required = false)String windowId) throws Exception {

        try {
            Map<String, Object> result = winEfficiencySupervisionService.getWinStageLimitTimeStatistics(startTime, endTime, type,isCurrent,windowId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("窗口阶段受理情况", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getCompletedApplyUseTimeByTheme")
    @ApiOperation(value = "按主题统计办结申报的用时情况", notes = "按主题统计办结申报的用时情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String")
    })
    public ResultForm getCompletedApplyUseTimeByTheme(String startTime, String endTime, String type) {
        try {
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            List<Map<String, Object>> result = winEfficiencySupervisionService.getCompletedApplyUseTimeByTheme(type, startTime, endTime);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("按主题统计办结申报的用时情况统计异常", e);
            return new ContentResultForm<>(false, null, "按主题统计办结申报的用时情况统计异常，" + e.getMessage());
        }
    }

    @GetMapping("/getCompletedApplyUseTimeByThemeAndWindow")
    @ApiOperation(value = "按主题窗口统计办结申报的用时情况", notes = "按主题窗口统计办结申报的用时情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "themeId", value = "主题ID", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm getCompletedApplyUseTimeByThemeAndWindow(String themeId, String startTime, String endTime, String type) throws Exception {
        try {
            if (StringUtils.isBlank(themeId)) {
                return new ResultForm(false, "请求缺少参数!");
            }
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            List<Map<String, Object>> result = winEfficiencySupervisionService.getCompletedApplyUseTimeByThemeAndWindow(themeId, type, startTime, endTime);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("按主题窗口统计办结申报的用时情况统计异常", e);
            return new ContentResultForm<>(false, null, "按主题窗口统计办结申报的用时情况统计异常，" + e.getMessage());
        }
    }
}
