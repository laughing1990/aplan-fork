package com.augurit.efficiency.controller;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.efficiency.service.OrgEfficiencySupersionService;
import com.augurit.efficiency.utils.TimeParamUtil;
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

    private static String ORG_EFFICIENCY_SUPERVISION_INDEX = "efficiency/assEffect";
    private static String OLD_WIN_EFFICIENCY_SUPERVISION_INDEX = "efficiency/assignPeopleEffect";
    private static String SINGLE_ORG_EFFICIENCY_SUPERVISION_INDEX = "efficiency/singleAssEffect";

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView(ORG_EFFICIENCY_SUPERVISION_INDEX);
    }

    @RequestMapping("/oldIndex")
    @ApiIgnore
    public ModelAndView oldIndex() {
        return new ModelAndView(OLD_WIN_EFFICIENCY_SUPERVISION_INDEX);
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
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query"),
    })
    public ResultForm getOrgItemAcceptStatistics(String startTime, String endTime, String type) {
        try {
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = TimeParamUtil.checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            List<Map<String, Object>> orgItemAcceptStatistics = orgEfficiencySupersionService.getOrgItemAcceptStatistics(startTime, endTime, type, true);
            return new ContentResultForm<>(true, orgItemAcceptStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("委办局-时间区间内部门事项接件受理统计异常", e);
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getOrgItemAcceptHistoryStatistics")
    @ApiOperation(value = "委办局-时间区间内部门事项历史接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "itemId", value = "事项ID", readOnly = true, dataType = "string", paramType = "query", required = true),
    })
    public ResultForm getOrgItemAcceptHistoryStatistics(String itemId, String startTime, String endTime, String type) {
        try {
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = TimeParamUtil.checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            if (StringUtils.isBlank(itemId)) {
                return new ContentResultForm<>(false, null, "请求缺少参数！");
            }
            List<Map<String, Object>> orgItemAcceptHistroyStatistics = orgEfficiencySupersionService.getOrgItemAcceptHistoryStatistics(itemId, startTime, endTime, type, true);
            return new ContentResultForm<>(true, orgItemAcceptHistroyStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("委办局-时间区间内部门事项历史接件受理统计异常", e);
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getRegionOrgAcceptStatistics")
    @ApiOperation(value = "委办局-时间区间内各地区/各地区部门接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "regionId", value = "行政区ID", readOnly = true, dataType = "string", paramType = "query"),
    })
    public ResultForm getRegionOrgAcceptStatistics(String regionId, String startTime, String endTime, String type) {
        try {
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = TimeParamUtil.checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            List<Map<String, Object>> regionOrgAcceptStatistics = orgEfficiencySupersionService.getRegionOrgAcceptStatistics(regionId, startTime, endTime, type);
            return new ContentResultForm<>(true, regionOrgAcceptStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("委办局-时间区间内各地区/各地区部门接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getRegionOrgAcceptHistoryStatistics")
    @ApiOperation(value = "委办局-时间区间内各地区/各地区部门历史接件受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-mm-dd】", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "时间类型D昨天，W本周，M本月，为空是自定义时间段", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "regionId", value = "行政区ID", readOnly = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "orgId", value = "部门ID", readOnly = true, dataType = "string", paramType = "query"),
    })
    public ResultForm getRegionOrgAcceptHistoryStatistics(String regionId, String orgId, String startTime, String endTime, String type) {
        try {
            if (StringUtils.isBlank(type)) {
                ResultForm resultForm = TimeParamUtil.checkTimeParam(startTime, endTime);
                if (resultForm != null) return resultForm;
            }
            if (StringUtils.isBlank(regionId) && StringUtils.isBlank(orgId)) {
                return new ContentResultForm<>(false, null, "请求缺少参数！");
            }
            List<Map<String, Object>> regionOrgAcceptHistoryStatistics = orgEfficiencySupersionService.getRegionOrgAcceptHistoryStatistics(regionId, orgId, startTime, endTime, type);
            return new ContentResultForm<>(true, regionOrgAcceptHistoryStatistics, "查询成功！");
        } catch (Exception e) {
            log.error("时间区间内各地区/各地区部门历史接件受理情况统计异常", e);
            return new ContentResultForm<>(false, null, "查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "委办局-获取当前所在城市区划")
    @GetMapping("/getCurrentCityRegions")
    public ContentResultForm getCurrentRegionList(){
        try {
            List<BscDicRegion> result = orgEfficiencySupersionService.getCurrentRegionList();
            return new ContentResultForm(true,result,"查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询城市区划失败1");
        }
    }

    @GetMapping("/getOrgReceiveStatistics")
    @ApiOperation(value = "部门接件总量及受理情况", notes = "部门接件总量及受理情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段为空】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "regionId", value = "区划id", dataType = "string", paramType = "string", required = true),
    })
    public ResultForm getOrgReceiveStatistics(String startTime, String endTime, String type, String regionId) throws Exception {

        try {
            Map<String, Object> result = orgEfficiencySupersionService.getOrgReceiveStatistics(startTime, endTime, type,regionId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("部门接件总量及受理情况", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getOrgReceiveLimitTimeStatistics")
    @ApiOperation(value = "部门接件时限状态", notes = "部门接件时限状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段为空】", dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "regionId", value = "区划id", dataType = "string", paramType = "string", required = true),
    })
    public ResultForm getOrgReceiveLimitTimeStatistics(String startTime, String endTime, String type, String regionId) throws Exception {

        try {
            Map<String,Object>  result = orgEfficiencySupersionService.getOrgReceiveLimitTimeStatistics(startTime, endTime, type,regionId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("部门接件时限状态", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getRegionOrgItemUseTimeStatistics")
    @ApiOperation(value = "地区/部门/事项 事项实例用时情况统计", notes = "地区/部门/事项 事项实例用时情况统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间【yyyy-MM-dd】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "type", value = "类型【周W月M日D灵活时间段A】", dataType = "string", paramType = "String"),
            @ApiImplicitParam(name = "regionId", value = "区划ID", dataType = "string", paramType = "string"),
            @ApiImplicitParam(name = "orgId", value = "部门ID", dataType = "string", paramType = "string")
    })
    public ResultForm getRegionOrgItemUseTimeStatistics(String startTime, String endTime, String type, String regionId, String orgId) {

        try {
            List<UseTimeStatisticsVo> result = orgEfficiencySupersionService.getItemUseTimeStatistics(startTime, endTime, type, regionId, orgId);
            return new ContentResultForm<>(true, result, "查询成功！");
        } catch (Exception e) {
            log.error("地区/部门/事项 事项实例用时情况统计异常", e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
}
