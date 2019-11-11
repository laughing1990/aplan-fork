package com.augurit.efficiency.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.efficiency.service.OrgEfficiencySupersionService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwn on 2019/9/17.
 */
@RestController
@Slf4j
@RequestMapping("/org/efficiency/supervision")
@Api(value = "部门效能督查接口", tags = "部门效能督查接口")
public class OrgEfficiencySupervisionController {
    @Autowired
    private OrgEfficiencySupersionService orgEfficiencySupersionService;

    private static String ORG_EFFICIENCY_SUPERVISION_INDEX = "efficiency/assignPeopleEffect";
    private static String SINGLE_ORG_EFFICIENCY_SUPERVISION_INDEX = "efficiency/singleAssEffect";

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView(ORG_EFFICIENCY_SUPERVISION_INDEX);
    }

    @RequestMapping("/orgIndex")
    @ApiIgnore
    public ModelAndView orgIndex() {
        return new ModelAndView(SINGLE_ORG_EFFICIENCY_SUPERVISION_INDEX);
    }

    @RequestMapping("/itemStateStatistics")
    @ApiOperation(value = "办件状态统计")
    public ResultForm itemStateStatistics(String applyinstId) {
        try {
            List<ItemStatisticsVo> list = orgEfficiencySupersionService.getItemStateStatistics("", false);
            return new ContentResultForm<>(true, list, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "shibai" + e.getMessage());
        }
    }

    @RequestMapping("/itemStateStatisticsByOrg")
    @ApiOperation(value = "办件状态统计-根据部门id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "指定部门orgid", readOnly = true, dataType = "string", paramType = "query", required = false),
            @ApiImplicitParam(name = "isCurrent", value = "是否当前部门", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    public ResultForm itemStateStatisticsByOrg(@RequestParam(required = false) String orgId, boolean isCurrent){
        try {
            List<ItemStatisticsVo> list = orgEfficiencySupersionService.getItemStateStatistics(orgId, isCurrent);
            return new ContentResultForm<>(true, list, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "shibai" + e.getMessage());
        }
    }

    @GetMapping("/queryOrgHandleItemStatistics")
    @ApiOperation(value = "委办局-时间区间内部门办理事项统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm queryOrgHandleItemStatistics(String startTime, String endTime) {
        try {
            List<ItemDetailFormVo> result = orgEfficiencySupersionService.queryOrgHandleItemStatistics(startTime, endTime);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/queryYesterdayOrgHandleItemStatistics")
    @ApiOperation(value = "委办局-昨日部门办理事项统计")
    public ContentResultForm queryTodayOrgHandleItemStatistics() {
        try {
            Date preDate = DateUtils.getPreDateByDate(new Date());
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(preDate);
            List<ItemDetailFormVo> result = orgEfficiencySupersionService.queryOrgHandleItemStatistics(startTime, startTime);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/queryThisWeekOrgHandleItemStatistics")
    @ApiOperation(value = "委办局-本周部门办理事项统计")
    public ContentResultForm queryThisWeekOrgHandleItemStatistics() {
        try {
            List<ItemDetailFormVo> result = orgEfficiencySupersionService.queryOrgHandleItemStatisticsToYesterday("W");
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/queryThisMonthOrgHandleItemStatistics")
    @ApiOperation(value = "委办局-本月部门办理事项统计")
    public ContentResultForm queryThisMonthOrgHandleItemStatistics() {
        try {
            List<ItemDetailFormVo> result = orgEfficiencySupersionService.queryOrgHandleItemStatisticsToYesterday("M");
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/queryItemExceptionStatistics")
    @ApiOperation(value = "事项办理异常排行")
    public ResultForm queryItemExceptionStatistics() {
        try {
            List<ApplyUnusualStatisticVo> res = orgEfficiencySupersionService
                    .queryItemExceptionStatistics(SecurityContext.getCurrentOrgId());
            return new ContentResultForm<List<ApplyUnusualStatisticVo>>(true, res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultForm(false);
        }

    }


    @GetMapping("/countItemForSeriesAndStage")
    @ApiOperation(value = "并联或一般单项统计")
    public ResultForm countItemForSeriesAndStage() {
        try {
            List<Map> res = orgEfficiencySupersionService.countItemForSeriesAndStage(SecurityContext.getCurrentOrgId());
            return new ContentResultForm<List<Map>>(true, res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultForm(false);
        }

    }

    @GetMapping("/countItemByApplyinstSource")
    @ApiOperation(value = "事项申报来源统计")
    public ResultForm countItemByApplyinstSource() {
        try {
            List<Map> res = orgEfficiencySupersionService.countItemByApplyinstSource(SecurityContext.getCurrentOrgId());
            return new ContentResultForm<List<Map>>(true, res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultForm(false);
        }

    }


    @GetMapping("/queryStatisticsByMonth")
    @ApiOperation(value = "月度受理统计", notes = "本窗口受理申报数，办结申报数，逾期申报数，不予受理申报数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startMonth", value = "开始时间【yyyy-MM】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endMonth", value = "结束时间【yyyy-MM】", dataType = "string", paramType = "String", required = true)
    })
    public ResultForm queryStatisticsByMonth(String startMonth, String endMonth) throws Exception {

        try {
            startMonth = DateUtils.convertDateToString(DateUtils.convertStringToDate(startMonth, "yyyy-MM"), "yyyy-MM");
            endMonth = DateUtils.convertDateToString(DateUtils.convertStringToDate(endMonth, "yyyy-MM"), "yyyy-MM");
            List<OrgMonthApplyStatisticsVo> winApplyStatisticsByMonth = orgEfficiencySupersionService.queryOrgApplyStatisticsByMonth(startMonth, endMonth);
            return new ContentResultForm<List>(true, winApplyStatisticsByMonth, "查询成功");
        } catch (Exception e) {
            log.error("月度受理统计异常", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/queryItemStatisticsByArea")
    @ApiOperation(value = "地区办件统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true)
    })
    public ResultForm queryMonthStatisticsCompletedByArea(String startTime, String endTime) {

        try {
            List<OrgAreaStatisticsVo> orgDayStatisticsByAreaa = orgEfficiencySupersionService.queryStatisticsCompletedByArea(startTime, endTime);
            return new ContentResultForm<>(true, orgDayStatisticsByAreaa, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败！" + e.getMessage());
        }
    }


    //页面跳转


    @GetMapping("/getOrgItemStatistics")
    @ApiOperation(value = "委办局-时间区间内部门事项受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "StartTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "isCurrent", value = "是否查询当前部门的", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgId", value = "查询部门的id", readOnly = true, dataType = "string", paramType = "query", required = false)
    })
    public ContentResultForm getOrgItemStatistics(String startTime,String endTime,String type,boolean isCurrent,@RequestParam(required = false)String orgId){
        try {
            Map<String, Object> result = orgEfficiencySupersionService.getOrgItemStatistics(startTime, endTime, type, isCurrent, orgId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getOrgItemAcceptStatistics")
    @ApiOperation(value = "委办局-时间区间内部门事项接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query", required = true),
    })
    public ContentResultForm getOrgItemAcceptStatistics(String startTime, String endTime, String type) {
        try {
            List<Map<String, Object>> orgItemAcceptStatistics = orgEfficiencySupersionService.getOrgItemAcceptStatistics(startTime, endTime, type, true);
            return new ContentResultForm<>(true, orgItemAcceptStatistics, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getOrgItemAcceptHistoryStatistics")
    @ApiOperation(value = "委办局-时间区间内部门事项历史接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "itemId", value = "事项ID", readOnly = true, dataType = "string", paramType = "query", required = true),
    })
    public ContentResultForm getOrgItemAcceptHistoryStatistics(String itemId, String startTime, String endTime, String type) {
        try {
            List<Map<String, Object>> orgItemAcceptHistroyStatistics = orgEfficiencySupersionService.getOrgItemAcceptHistoryStatistics(itemId, startTime, endTime, type, true);
            return new ContentResultForm<>(true, orgItemAcceptHistroyStatistics, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }
}
