package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.ThemeDayApplyRecord;
import com.augurit.aplanmis.common.vo.analyse.ThemeMonthCountVo;
import com.augurit.aplanmis.common.vo.analyse.ThemeWeekCountVo;
import com.augurit.aplanmis.common.vo.analyse.YearCountVo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时统计的方法不要调用SecurityContext获取登录信息。
 * 定时器执行的方法是没有用户登录的。
 */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class AeaAnaThemeStatisticsImpl {

    private static String THEME_REPORTID_DAY = "0011";
    private static String THEME_REPORTNAME_DAY = "主题日办件统计表";
    private static String THEME_REPORTID_WEEK = "0012";
    private static String THEME_REPORTNAME_WEEK = "主题周办件统计表";
    private static String THEME_REPORTID_MONTH = "0013";
    private static String THEME_REPORTNAME_MONTH = "主题月办件统计表";
    private static String THEME_REPORTID_YEAR = "0014";
    private static String THEME_REPORTNAME_YEAR = "主题年办件统计表";
    private static String DEFAULT_CREATER_SYS = "sys_admin_job";

    @Autowired
    private AeaAnaOrgStatisticsImpl orgStatisticsImpl;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper themeDayStatisticsMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaHiApplyinstMapper applyinstMapper;
    @Autowired
    private AeaLogApplyStateHistMapper logApplyStateHistMapper;
    @Autowired
    private AeaAnaThemeYearStatisticsMapper themeYearStatisticsMapper;
    @Autowired
    private AeaAnaThemeWeekStatisticsMapper themeWeekStatisticsMapper;
    @Autowired
    private AeaAnaThemeMonthStatisticsMapper themeMonthStatisticsMapper;

    /**
     * 统计主题日办件
     * 一个主题有多个阶段，生成多条记录。
     * 统计方向，主题-阶段-申报实例（主题版本越多，查询统计越慢）
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsThemeDayData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statisticsDate = DateUtils.getPreDateByDate(today);
        String dateFormat = sdf.format(statisticsDate);
        String startTime = dateFormat + " 00:00:00";
        String endTime = dateFormat + " 23:59:59";
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已统计过
        AeaAnaStatisticsRecord statisticsRecord = orgStatisticsImpl.buildStatisticsRecord(THEME_REPORTID_DAY, THEME_REPORTNAME_DAY, startTime, endTime, operateSource, "s", creater, rootOrgId);

        //找到全部主题
        AeaParTheme search = new AeaParTheme();
        search.setIsActive("1");
        search.setRootOrgId(rootOrgId);
        List<AeaParTheme> themes = aeaParThemeMapper.listAeaParTheme(search);
        for (int i = 0, len = themes.size(); i < len; i++) {
            String themeId = themes.get(i).getThemeId();
            String themeName = themes.get(i).getThemeName();
            try {
                List<AeaAnaThemeDayStatistics> oneThemeList = packageDayStastics(themeId, themeName, statisticsRecord.getStatisticsRecordId(), statisticsRecord.getStatisticsStartDate(), rootOrgId);
                result.addAll(oneThemeList);
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("统计主题日申报情况出错，主题id：{}，主题名称：{}，错误信息：{}", themeId, themeName, e.getMessage());
            }
        }

        if (result.size() > 0) {
            //删除当日数据
            themeDayStatisticsMapper.deleteThisDayStatisticsData(dateFormat, rootOrgId);
            //批量插入记录
            themeDayStatisticsMapper.batchInsertAeaAnaThemeDayStatistics(result);
        }
        log.debug("日办件统计耗时：" + (System.currentTimeMillis() - beginTime));
    }


    /**
     * 统计主题阶段周申办
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     */
    public void statisticsThemeWeekData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaThemeWeekStatistics> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //昨天
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取周一、周日
        Date monday = DateUtils.getThisWeekMonday(preDate);
        Date sunday = DateUtils.getThisWeekSunday(preDate);
        String startTime = sdf.format(monday) + " 00:00:00";
        String endTime = sdf.format(sunday) + " 23:59:59";
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord record = orgStatisticsImpl.buildStatisticsRecord(THEME_REPORTID_WEEK, THEME_REPORTNAME_WEEK, startTime, endTime, operateSource, "s", creater, rootOrgId);

        int weekNum = DateUtils.getThisWeekNum(preDate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(preDate);
        int year = ca.get(Calendar.YEAR);

        //根据日统计汇总这周的数据
        List<ThemeWeekCountVo> weekStatisticsFromDay = themeWeekStatisticsMapper.getWeekStatisticsFromDay(startTime, endTime, year, weekNum - 1, sdf.format(preDate), rootOrgId);
        if (CollectionUtils.isNotEmpty(weekStatisticsFromDay)) {
            for (ThemeWeekCountVo themeWeekCountVo : weekStatisticsFromDay) {
                AeaAnaThemeWeekStatistics themeWeekStatistics = new AeaAnaThemeWeekStatistics();
                themeWeekStatistics.setThemeWeekStatisticsId(UuidUtil.generateUuid());
                themeWeekStatistics.setStatisticsRecordId(record.getStatisticsRecordId());

                //设置这周最新日统计的总数
                themeWeekStatistics.setThemeId(themeWeekCountVo.getThemeId());
                themeWeekStatistics.setThemeName(themeWeekCountVo.getThemeName());
                themeWeekStatistics.setApplyRecordId(themeWeekCountVo.getApplyRecordId());
                themeWeekStatistics.setApplyRecordName(themeWeekCountVo.getApplyRecordName());
                themeWeekStatistics.setDybzspjdxh(themeWeekCountVo.getDybzspjdxh());
                themeWeekStatistics.setIsNode(themeWeekCountVo.getIsNode());
                themeWeekStatistics.setIsParallel(themeWeekCountVo.getIsParallel());
                themeWeekStatistics.setApplyinstSource(themeWeekCountVo.getApplyinstSource());

                List<AeaAnaThemeDayStatistics> dayDataList = themeDayStatisticsMapper.getAeaAnaThemeDayStatistics(themeWeekCountVo.getThemeId(), themeWeekCountVo.getApplyRecordId(),
                        themeWeekCountVo.getIsParallel(), themeWeekCountVo.getApplyinstSource(), rootOrgId, sdf.format(monday), sdf.format(sunday));
                if (CollectionUtils.isNotEmpty(dayDataList)) {
                    AeaAnaThemeDayStatistics dayStatistics = dayDataList.get(0);
                    themeWeekStatistics.setAllApplyCount(dayStatistics.getAllApplyCount());
                    themeWeekStatistics.setAllInSupplementCount(dayStatistics.getAllInSupplementCount());
                    themeWeekStatistics.setAllSupplementedCount(dayStatistics.getAllSupplementedCount());
                    themeWeekStatistics.setAllPreAcceptanceCount(dayStatistics.getAllPreAcceptanceCount());
                    themeWeekStatistics.setAllOutScopeCount(dayStatistics.getAllOutScopeCount());
                    themeWeekStatistics.setAllCompletedCount(dayStatistics.getAllCompletedCount());
                    themeWeekStatistics.setAllOverTimeCount(dayStatistics.getAllOverTimeCount());
                    themeWeekStatistics.setAllPreAcceptanceRate(dayStatistics.getAllPreAcceptanceRate());
                    themeWeekStatistics.setAllOutScopeRate(dayStatistics.getAllOutScopeRate());
                    themeWeekStatistics.setAllOverTimeRate(dayStatistics.getAllOverTimeRate());
                    themeWeekStatistics.setAllCompletedRate(dayStatistics.getAllCompletedRate());
                } else {
                    themeWeekStatistics.setAllApplyCount(0L);
                    themeWeekStatistics.setAllInSupplementCount(0L);
                    themeWeekStatistics.setAllSupplementedCount(0L);
                    themeWeekStatistics.setAllPreAcceptanceCount(0L);
                    themeWeekStatistics.setAllOutScopeCount(0L);
                    themeWeekStatistics.setAllCompletedCount(0L);
                    themeWeekStatistics.setAllOverTimeCount(0L);
                    themeWeekStatistics.setAllPreAcceptanceRate(0.0);
                    themeWeekStatistics.setAllOutScopeRate(0.0);
                    themeWeekStatistics.setAllOverTimeRate(0.0);
                    themeWeekStatistics.setAllCompletedRate(0.0);
                }

                //设置这周的周统计
                themeWeekStatistics.setWeekApplyCount(themeWeekCountVo.getWeekApplyCount());
                themeWeekStatistics.setWeekPreAcceptanceCount(themeWeekCountVo.getWeekPreAcceptanceCount());
                themeWeekStatistics.setWeekOutScopeCount(themeWeekCountVo.getWeekOutScopeCount());
                themeWeekStatistics.setWeekCompletedCount(themeWeekCountVo.getWeekCompletedCount());
                themeWeekStatistics.setWeekOverTimeCount(themeWeekCountVo.getWeekOverTimeCount());

                //计算环比
                themeWeekStatistics.setApplyLrr(calculateGrowRate(themeWeekCountVo.getLastWeekApplyCount(), themeWeekStatistics.getWeekApplyCount()));
                themeWeekStatistics.setPreAcceptanceLrr(calculateGrowRate(themeWeekCountVo.getLastWeekPreAcceptacneCount(), themeWeekStatistics.getWeekPreAcceptanceCount()));
                themeWeekStatistics.setOutScopeLrr(calculateGrowRate(themeWeekCountVo.getLastWeekOutScopeCount(), themeWeekStatistics.getWeekOutScopeCount()));
                themeWeekStatistics.setCompletedLrr(calculateGrowRate(themeWeekCountVo.getLastWeekCompletedCount(), themeWeekStatistics.getWeekCompletedCount()));
                themeWeekStatistics.setOverTimeLrr(calculateGrowRate(themeWeekCountVo.getLastWeekOverTimeCount(), themeWeekStatistics.getWeekOverTimeCount()));

                themeWeekStatistics.setStatisticsYear(Long.valueOf(year));
                themeWeekStatistics.setWeekNum(Long.valueOf(weekNum));
                themeWeekStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
                themeWeekStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                themeWeekStatistics.setModifier(creater);
                themeWeekStatistics.setModifyTime(new Date());
                themeWeekStatistics.setRootOrgId(rootOrgId);

                result.add(themeWeekStatistics);
            }
            if (result.size() > 0) {
                //删除本周数据
                themeWeekStatisticsMapper.deleteThisWeekStatisticsData(year, weekNum, rootOrgId);
                //批量插入记录
                themeWeekStatisticsMapper.batchInsertWeekStatisticsData(result);
            }
            log.debug("周主题阶段申办统计耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            log.error("【周主题阶段申办统计异常】weekStatisticsFromDay为空，统计参数:{ startTime:{}, endTime:{}, year:{}, weekNum:{} }",
                    startTime, endTime, year, weekNum);
        }
    }


    /**
     * 统计主题阶段月申办
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsThemeMonthData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaThemeMonthStatistics> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //昨天
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取本月第一天、本月最后一天
        Date firstDay = DateUtils.firstDayOfMonth(preDate);
        Date lastDay = DateUtils.lastDayOfMonth(preDate);
        String startTime = sdf.format(firstDay) + " 00:00:00";
        String endTime = sdf.format(lastDay) + " 23:59:59";

        Calendar ca = Calendar.getInstance();
        ca.setTime(preDate);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        DecimalFormat monthFormat = new DecimalFormat("00");
        String nowMonth = year + "-" + monthFormat.format(month);
        String lastMonth = year + "-" + monthFormat.format(month - 1);
        String lastYear = (year - 1) + "-" + monthFormat.format(month);
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord record = orgStatisticsImpl.buildStatisticsRecord(THEME_REPORTID_MONTH, THEME_REPORTNAME_MONTH, startTime, endTime, operateSource, "s", creater, rootOrgId);

        //根据日统计汇总这月的数据
        List<ThemeMonthCountVo> monthStatisticsFromDay = themeMonthStatisticsMapper.getMonthStatisticsFromDay(startTime, endTime, lastMonth, lastYear, sdf.format(preDate), rootOrgId);
        if (CollectionUtils.isNotEmpty(monthStatisticsFromDay)) {
            for (ThemeMonthCountVo themeMonthCountVo : monthStatisticsFromDay) {
                AeaAnaThemeMonthStatistics themeMonthStatistics = new AeaAnaThemeMonthStatistics();
                themeMonthStatistics.setThemeMonthStatisticsId(UuidUtil.generateUuid());
                themeMonthStatistics.setStatisticsRecordId(record.getStatisticsRecordId());

                //设置这月最新日统计的总数
                themeMonthStatistics.setThemeId(themeMonthCountVo.getThemeId());
                themeMonthStatistics.setThemeName(themeMonthCountVo.getThemeName());
                themeMonthStatistics.setApplyRecordId(themeMonthCountVo.getApplyRecordId());
                themeMonthStatistics.setApplyRecordName(themeMonthCountVo.getApplyRecordName());
                themeMonthStatistics.setDybzspjdxh(themeMonthCountVo.getDybzspjdxh());
                themeMonthStatistics.setIsNode(themeMonthCountVo.getIsNode());
                themeMonthStatistics.setIsParallel(themeMonthCountVo.getIsParallel());
                themeMonthStatistics.setApplyinstSource(themeMonthCountVo.getApplyinstSource());

                List<AeaAnaThemeDayStatistics> dayDataList = themeDayStatisticsMapper.getAeaAnaThemeDayStatistics(themeMonthCountVo.getThemeId(), themeMonthCountVo.getApplyRecordId(),
                        themeMonthCountVo.getIsParallel(), themeMonthCountVo.getApplyinstSource(), rootOrgId, sdf.format(firstDay), sdf.format(lastDay));
                if (CollectionUtils.isNotEmpty(dayDataList)) {
                    AeaAnaThemeDayStatistics dayStatistics = dayDataList.get(0);
                    themeMonthStatistics.setAllApplyCount(dayStatistics.getAllApplyCount());
                    themeMonthStatistics.setAllInSupplementCount(dayStatistics.getAllInSupplementCount());
                    themeMonthStatistics.setAllSupplementedCount(dayStatistics.getAllSupplementedCount());
                    themeMonthStatistics.setAllPreAcceptanceCount(dayStatistics.getAllPreAcceptanceCount());
                    themeMonthStatistics.setAllOutScopeCount(dayStatistics.getAllOutScopeCount());
                    themeMonthStatistics.setAllCompletedCount(dayStatistics.getAllCompletedCount());
                    themeMonthStatistics.setAllOverTimeCount(dayStatistics.getAllOverTimeCount());
                    themeMonthStatistics.setAllPreAcceptanceRate(dayStatistics.getAllPreAcceptanceRate());
                    themeMonthStatistics.setAllOutScopeRate(dayStatistics.getAllOutScopeRate());
                    themeMonthStatistics.setAllOverTimeRate(dayStatistics.getAllOverTimeRate());
                    themeMonthStatistics.setAllCompletedRate(dayStatistics.getAllCompletedRate());
                } else {
                    themeMonthStatistics.setAllApplyCount(0L);
                    themeMonthStatistics.setAllInSupplementCount(0L);
                    themeMonthStatistics.setAllSupplementedCount(0L);
                    themeMonthStatistics.setAllPreAcceptanceCount(0L);
                    themeMonthStatistics.setAllOutScopeCount(0L);
                    themeMonthStatistics.setAllCompletedCount(0L);
                    themeMonthStatistics.setAllOverTimeCount(0L);
                    themeMonthStatistics.setAllPreAcceptanceRate(0.0);
                    themeMonthStatistics.setAllOutScopeRate(0.0);
                    themeMonthStatistics.setAllOverTimeRate(0.0);
                    themeMonthStatistics.setAllCompletedRate(0.0);
                }

                //设置这月的月统计
                themeMonthStatistics.setMonthApplyCount(themeMonthCountVo.getMonthApplyCount());
                themeMonthStatistics.setMonthPreAcceptanceCount(themeMonthCountVo.getMonthPreAcceptanceCount());
                themeMonthStatistics.setMonthOutScopeCount(themeMonthCountVo.getMonthOutScopeCount());
                themeMonthStatistics.setMonthCompletedCount(themeMonthCountVo.getMonthCompletedCount());
                themeMonthStatistics.setMonthOverTimeCount(themeMonthCountVo.getMonthOverTimeCount());

                //计算环比&同比
                themeMonthStatistics.setApplyLrr(calculateGrowRate(themeMonthCountVo.getLastMonthApplyCount(), themeMonthStatistics.getMonthApplyCount()));
                themeMonthStatistics.setInSupplementLrr(calculateGrowRate(themeMonthCountVo.getLastMonthInSupplementCount(), themeMonthStatistics.getAllInSupplementCount()));
                themeMonthStatistics.setSupplementedLrr(calculateGrowRate(themeMonthCountVo.getLastMonthSupplementedCount(), themeMonthStatistics.getAllSupplementedCount()));
                themeMonthStatistics.setPreAcceptanceLrr(calculateGrowRate(themeMonthCountVo.getLastMonthPreAcceptacneCount(), themeMonthStatistics.getMonthPreAcceptanceCount()));
                themeMonthStatistics.setOutScopeLrr(calculateGrowRate(themeMonthCountVo.getLastMonthOutScopeCount(), themeMonthStatistics.getMonthOutScopeCount()));
                themeMonthStatistics.setCompletedLrr(calculateGrowRate(themeMonthCountVo.getLastMonthCompletedCount(), themeMonthStatistics.getMonthCompletedCount()));
                themeMonthStatistics.setOverTimeLrr(calculateGrowRate(themeMonthCountVo.getLastMonthOverTimeCount(), themeMonthStatistics.getMonthOverTimeCount()));

                themeMonthStatistics.setApplyOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearApplyCount(), themeMonthStatistics.getMonthApplyCount()));
                themeMonthStatistics.setInSupplementOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearInSupplementCount(), themeMonthStatistics.getAllInSupplementCount()));
                themeMonthStatistics.setSupplementedOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearSupplementedCount(), themeMonthStatistics.getAllSupplementedCount()));
                themeMonthStatistics.setPreAcceptanceOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearPreAcceptacneCount(), themeMonthStatistics.getMonthPreAcceptanceCount()));
                themeMonthStatistics.setOutScopeOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearOutScopeCount(), themeMonthStatistics.getMonthOutScopeCount()));
                themeMonthStatistics.setCompletedOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearCompletedCount(), themeMonthStatistics.getMonthCompletedCount()));
                themeMonthStatistics.setOverTimeOnYoyBasis(calculateGrowRate(themeMonthCountVo.getLastYearOverTimeCount(), themeMonthStatistics.getMonthOverTimeCount()));

                themeMonthStatistics.setStatisticsMonth(nowMonth);
                themeMonthStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
                themeMonthStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                themeMonthStatistics.setModifier(creater);
                themeMonthStatistics.setModifyTime(new Date());
                themeMonthStatistics.setRootOrgId(rootOrgId);

                result.add(themeMonthStatistics);
            }
            if (result.size() > 0) {
                //删除本月数据
                themeMonthStatisticsMapper.deleteThisMonthStatisticsData(nowMonth, rootOrgId);
                //批量插入记录
                themeMonthStatisticsMapper.batchInsertMonthStatisticsData(result);
            }
            log.debug("月主题阶段申办统计耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            log.error("【月主题阶申办统计异常】monthStatisticsFromDay为空，统计参数:{ startTime:{}, endTime:{}, year:{}, month:{} }",
                    startTime, endTime, year, month);
        }
    }


    /**
     * 统计部门年办件
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsThemeYearData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取本年第一天、本年最后一天
        Date firstDay = DateUtils.firstDayOfYear(preDate);
        Date lastDay = DateUtils.lastDayOfYear(preDate);
        String startTime = sdf.format(firstDay) + " 00:00:00";
        String endTime = sdf.format(lastDay) + " 23:59:59";
        String year = DateUtils.convertDateToString(preDate, "yyyy");
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord record = orgStatisticsImpl.buildStatisticsRecord(THEME_REPORTID_YEAR, THEME_REPORTNAME_YEAR, startTime, endTime, operateSource, "s", creater, rootOrgId);

        List<AeaAnaThemeYearStatistics> result = new ArrayList<>();
        String yearMonth = DateUtils.convertDateToString(today, "yyyy-MM");
        Integer lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        String startDate = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).toString();
        String endDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).toString();

        //获取上一年的统计
        List<YearCountVo> yearStatistics = themeYearStatisticsMapper.getYearStatisticsFromMonthTable(lastYear.toString(), year, yearMonth, rootOrgId);
        if (yearStatistics.size() > 0) {
            for (YearCountVo obj : yearStatistics) {
                AeaAnaThemeYearStatistics statistics = new AeaAnaThemeYearStatistics();
                statistics.setAllApplyCount(obj.getAllApplyCount());
                String[] states = {ApplyState.SUPPLEMENTARY.getValue()};
                long yearAllSupplementary = getCount(obj.getApplyRecordId(), states, startDate, endDate, obj.getIsParallel(), rootOrgId, obj.getApplyinstSource());
                String[] states2 = {ApplyState.IN_THE_SUPPLEMENT.getValue()};
                long yearAllInTheSupplement = getCount(obj.getApplyRecordId(), states2, startDate, endDate, obj.getIsParallel(), rootOrgId, obj.getApplyinstSource());

                statistics.setYearApplyCount(obj.getThisYearApplyCount());
                statistics.setYearCompletedCount(obj.getThisYearCompletedCount());
                statistics.setYearOutScopeCount(obj.getThisYearOutScopeCount());
                statistics.setYearOverTimeCount(obj.getThisYearOverTimeCount());
                statistics.setYearPreAcceptanceCount(obj.getThisYearPreAcceptanceCount());

                statistics.setAllInSupplementCount(yearAllInTheSupplement);
                statistics.setAllSupplementedCount(yearAllSupplementary);

                statistics.setAllPreAcceptanceCount(obj.getAllPreAcceptanceCount());
                statistics.setAllOutScopeCount(obj.getAllOutScopeCount());
                statistics.setAllCompletedCount(obj.getAllCompletedCount());
                statistics.setAllOverTimeCount(obj.getAllOverTimeCount());

                statistics.setApplyLrr(calculateGrowRate(obj.getLastYearApplyCount(), obj.getThisYearApplyCount()));

                statistics.setInSupplementLrr(calculateGrowRate(obj.getLastYearInSupplementCount(), obj.getAllInSupplementCount()));
                statistics.setSupplementedLrr(calculateGrowRate(obj.getLastYearSupplementedCount(), obj.getAllSupplementedCount()));

                statistics.setPreAcceptanceLrr(calculateGrowRate(obj.getLastYearPreAcceptanceCount(), obj.getThisYearPreAcceptanceCount()));
                statistics.setOutScopeLrr(calculateGrowRate(obj.getLastYearOutScopeCount(), obj.getThisYearOutScopeCount()));
                statistics.setCompletedLrr(calculateGrowRate(obj.getLastYearCompletedCount(), obj.getThisYearCompletedCount()));
                statistics.setOverTimeLrr(calculateGrowRate(obj.getLastYearOverTimeCount(), obj.getThisYearOverTimeCount()));

                statistics.setAllPreAcceptanceRate(calculateRate(obj.getAllPreAcceptanceCount(), obj.getAllApplyCount()));
                statistics.setAllOutScopeRate(calculateRate(obj.getAllOutScopeCount(), obj.getAllApplyCount()));
                statistics.setAllOverTimeRate(calculateRate(obj.getAllOverTimeCount(), obj.getAllPreAcceptanceCount()));
                statistics.setAllCompletedRate(calculateRate(obj.getAllCompletedCount(), obj.getAllPreAcceptanceCount()));

                statistics.setThemeId(obj.getThemtId());
                statistics.setThemeName(obj.getThemtName());
                statistics.setApplyRecordId(obj.getApplyRecordId());
                statistics.setApplyRecordName(obj.getApplyRecordName());
                statistics.setDybzspjdxh(obj.getDybzspjdxh());
                statistics.setIsNode(obj.getIsNode());
                statistics.setApplyinstSource(obj.getApplyinstSource());
                statistics.setIsParallel(obj.getIsParallel());
                statistics.setStatisticsYear(Long.valueOf(year));
                statistics.setRootOrgId(rootOrgId);

                statistics.setThemeYearStatisticsId(UUID.randomUUID().toString());
                statistics.setStatisticsRecordId(record.getStatisticsRecordId());
                statistics.setStatisticsStartDate(firstDay);
                statistics.setStatisticsEndDate(lastDay);
                statistics.setModifier(creater);
                statistics.setModifyTime(new Date());

                result.add(statistics);
            }
        }

        if (result.size() > 0) {
            //删除本月数据
            themeYearStatisticsMapper.deleteThisYearStatisticsData(year, rootOrgId);
            //批量插入记录
            themeYearStatisticsMapper.batchInsertYearStatisticsData(result);
        }
        log.debug("年办件统计耗时：" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * 封装一个主题下一天的数据
     *
     * @param themeId
     * @param themeName
     * @param statisticsRecordId
     * @param statisticsStartDate
     * @param rootOrgId
     * @return
     */
    private List<AeaAnaThemeDayStatistics> packageDayStastics(String themeId, String themeName, String statisticsRecordId, Date statisticsStartDate, String rootOrgId) {
        //获取该主题下的所有阶段stage
        List<AeaAnaThemeDayStatistics> themeDayList = new ArrayList<>();
        List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByThemeIds("'" + themeId + "'");
        String queryThemeVerIds = themeVers.stream().map(AeaParThemeVer::getThemeVerId).collect(Collectors.joining("','"));
        queryThemeVerIds = "'" + queryThemeVerIds + "'";
        List<Map<String, Object>> stageList = themeDayStatisticsMapper.queryThemeStageIds(queryThemeVerIds, rootOrgId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(statisticsStartDate);
        String startTime = dateStr + " 00:00:00";
        String endTime = dateStr + " 23:59:59";

        for (int i = 0, len = stageList.size(); i < len; i++) {

            String stageId = stageList.get(i).get("stageId").toString();

            String stageName = stageList.get(i).get("stageName").toString();
            try {
                String dybzspjdxh = stageList.get(i).get("dybzspjdxh").toString();
                String isNode = stageList.get(i).get("isNode").toString();

                //查询没有并行推进事项的
                themeDayList.addAll(getStageDataByParallel(stageId, startTime, endTime, "0", rootOrgId, stageName, dybzspjdxh, isNode, themeId, themeName, statisticsRecordId, statisticsStartDate));
                //查询是并行推进事项的
                themeDayList.addAll(getStageDataByParallel(stageId, startTime, endTime, "1", rootOrgId, stageName, dybzspjdxh, isNode, themeId, themeName, statisticsRecordId, statisticsStartDate));
            } catch (Exception e) {
                log.debug("统计主题日办件情况出错，主题themeId：{}，阶段stageId：{}，错误信息：{}", themeId, stageId, e.getMessage());
            }

        }

        return themeDayList;
    }

    /**
     * 获取一个阶段下的统计数据（isParallel是否并行推进事项为主要维度）
     *
     * @param stageId
     * @param
     * @param
     * @param isParallel          是否并行推进0否1是
     * @param rootOrgId
     * @param stageName
     * @param dybzspjdxh
     * @param isNode
     * @param themeId
     * @param themeName
     * @param statisticsRecordId
     * @param statisticsStartDate
     * @return
     */
    private List<AeaAnaThemeDayStatistics> getStageDataByParallel(String stageId, String startTime, String endTime, String isParallel, String rootOrgId, String stageName,
                                                                  String dybzspjdxh, String isNode, String themeId, String themeName, String statisticsRecordId, Date statisticsStartDate) throws Exception {

        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        // 根据历史状态计算一下
        List<AeaLogApplyStateHist> changeHis = new ArrayList<>();
        if ("0".equals(isParallel)) {
            //计算并联
            List<AeaLogApplyStateHist> parChangeHis = logApplyStateHistMapper.getApplyChangeHis(stageId, startTime, endTime, isParallel, "0");
            //计算单项
            List<AeaLogApplyStateHist> serChangeHis = logApplyStateHistMapper.getApplyChangeHis(stageId, startTime, endTime, isParallel, "1");
            changeHis.addAll(parChangeHis);
            changeHis.addAll(serChangeHis);


        } else {
//            changeHis = logApplyStateHistMapper.getApplyChangeHisIsParallel(stageId, startDate, endDate,isParallel);
            //计算单项
            List<AeaLogApplyStateHist> serChangeHis = logApplyStateHistMapper.getApplyChangeHis(stageId, startTime, endTime, isParallel, "1");
            changeHis.addAll(serChangeHis);
        }


        //按来源分组一次,数据会有两条了
        if (changeHis.size() > 0) {
            Map<String, List<AeaLogApplyStateHist>> collect = changeHis.stream().collect(Collectors.groupingBy(AeaLogApplyStateHist::getApplyinstSource));
            Set<Map.Entry<String, List<AeaLogApplyStateHist>>> entries = collect.entrySet();
            for (Map.Entry<String, List<AeaLogApplyStateHist>> entry : entries) {
                String source = entry.getKey();
                List<AeaLogApplyStateHist> sourceList = entry.getValue();
                result.add(getStageDataBySource(source, sourceList, stageId, rootOrgId, isParallel, startTime, endTime,
                        themeId, themeName, stageName, dybzspjdxh, isNode, statisticsRecordId, statisticsStartDate));
            }
        } else {
            AeaAnaThemeDayStatistics win = getStageDataBySource("win", null, stageId, rootOrgId, isParallel, startTime, endTime,
                    themeId, themeName, stageName, dybzspjdxh, isNode, statisticsRecordId, statisticsStartDate);
            if (win != null) {
                result.add(win);
            }
            AeaAnaThemeDayStatistics net = getStageDataBySource("net", null, stageId, rootOrgId, isParallel, startTime, endTime,
                    themeId, themeName, stageName, dybzspjdxh, isNode, statisticsRecordId, statisticsStartDate);
            if (net != null) {
                result.add(net);
            }
        }

        return result;
    }

    /**
     * 主题-来源的计算
     *
     * @param source
     * @param sourceList
     * @param stageId
     * @param rootOrgId
     * @param isParallel
     * @param
     * @param themeId
     * @param themeName
     * @param stageName
     * @param dybzspjdxh
     * @param isNode
     * @param statisticsRecordId
     * @param statisticsStartDate
     */
    private AeaAnaThemeDayStatistics getStageDataBySource(String source, List<AeaLogApplyStateHist> sourceList, String stageId, String rootOrgId, String isParallel, String startTime, String endTime, String themeId, String themeName, String stageName, String dybzspjdxh, String isNode, String statisticsRecordId, Date statisticsStartDate)
            throws Exception {
        //查询当天逾期的东西
        List<ActStoTimeruleInst> actStoTimeruleInsts = new ArrayList<>();

        if ("0".equals(isParallel)) {
            actStoTimeruleInsts.addAll(themeDayStatisticsMapper.getStageStoTimeruleInst(stageId, "3", rootOrgId, isParallel, "0", source, startTime, endTime));
            actStoTimeruleInsts.addAll(themeDayStatisticsMapper.getStageStoTimeruleInst(stageId, "3", rootOrgId, isParallel, "1", source, startTime, endTime));
        } else {
            actStoTimeruleInsts.addAll(themeDayStatisticsMapper.getStageStoTimeruleInst(stageId, "3", rootOrgId, isParallel, "1", source, startTime, endTime));
        }

        long dayOverTimeCount = actStoTimeruleInsts.size();

        if (dayOverTimeCount < 1 && sourceList == null) {
            return null;
        }

        long dayApplyCount = 0, unapprovalApply = 0, approvedApply = 0, hisPreAccept = 0, hisInsupplement = 0, hisSupplementary = 0, hisOutScope = 0, hisCompleted = 0;
        if (CollectionUtils.isNotEmpty(sourceList)) {
            Map<String, List<AeaLogApplyStateHist>> collect = sourceList.stream().collect(Collectors.groupingBy(AeaLogApplyStateHist::getNewState));
            for (String state : collect.keySet()) {
                if (ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue().equals(state)) {
                    unapprovalApply += collect.get(state).size();
                } else if (ApplyState.RECEIVE_APPROVED_APPLY.getValue().equals(state)) {
                    approvedApply += collect.get(state).size();
                } else if (ApplyState.ACCEPT_DEAL.getValue().equals(state)) {
                    hisPreAccept += collect.get(state).size();
                } else if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(state)) {
                    hisInsupplement += collect.get(state).size();
                } else if (ApplyState.SUPPLEMENTARY.getValue().equals(state)) {
                    hisSupplementary += collect.get(state).size();
                } else if (ApplyState.OUT_SCOPE.getValue().equals(state)) {
                    hisOutScope += collect.get(state).size();
                } else if (ApplyState.COMPLETED.getValue().equals(state)) {
                    hisCompleted += collect.get(state).size();
                }
            }
        }

        //从数据库获取前一天记录
        String curDate = startTime.substring(0, 10);
        String oneDayBefore = DateUtils.getPreDateByDate(curDate);
        String oneDayBeforeStart = oneDayBefore + " 00:00:00";
        String oneDayBeforeEnd = oneDayBefore + " 23:59:59";

        AeaAnaThemeDayStatistics onedayBeforStastics = themeDayStatisticsMapper.getAeaAnaThemeDayStatisticsBySatgeIdAndThemeId(themeId, stageId, oneDayBeforeStart, oneDayBeforeEnd, rootOrgId, isParallel, source);

        //昨天逾期总数加当天逾期数
        long allOverTimeCount = dayOverTimeCount + onedayBeforStastics.getAllOverTimeCount();
        //接件数
        dayApplyCount = unapprovalApply + approvedApply;
        double applyLrr = calculateGrowRate(onedayBeforStastics.getDayApplyCount(), dayApplyCount);
        double preAcceptanceLrr = calculateGrowRate(onedayBeforStastics.getDayPreAcceptanceCount(), hisPreAccept);
        double outScopeLrr = calculateGrowRate(onedayBeforStastics.getDayOutScopeCount(), hisOutScope);
        double completedLrr = calculateGrowRate(onedayBeforStastics.getDayCompletedCount(), hisCompleted);
        double overTimeLrr = calculateGrowRate(onedayBeforStastics.getDayOverTimeCount(), dayOverTimeCount);

        //縂比率统计
        PageHelper.startPage(1, 1);
        List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatistics = themeDayStatisticsMapper.getAeaAnaThemeDayStatistics(themeId, stageId, isParallel, source, rootOrgId, null, null);
        if (CollectionUtils.isNotEmpty(aeaAnaThemeDayStatistics)) {
            AeaAnaThemeDayStatistics dayStatistics = aeaAnaThemeDayStatistics.get(0);
            onedayBeforStastics.setAllPreAcceptanceCount(dayStatistics.getAllPreAcceptanceCount());
            onedayBeforStastics.setAllApplyCount(dayStatistics.getAllApplyCount());
            onedayBeforStastics.setAllOutScopeCount(dayStatistics.getAllOutScopeCount());
            onedayBeforStastics.setAllCompletedCount(dayStatistics.getAllCompletedCount());
        }

        long allPreAcceptanceCount = hisPreAccept + onedayBeforStastics.getAllPreAcceptanceCount();
        long allApplyCount = dayApplyCount + onedayBeforStastics.getAllApplyCount();
        long allOutScopeCount = hisOutScope + onedayBeforStastics.getAllOutScopeCount();
        long allCompletedCount = hisCompleted + onedayBeforStastics.getAllCompletedCount();

        double allPreAcceptanceRate = calculateRate(allPreAcceptanceCount, allApplyCount);//总预受理率（预受理数/接件数）
        double allOutScopeRate = calculateRate(allOutScopeCount, allApplyCount);//总不予受理率（不予受理数/接件数）
        double allOverTimeRate = calculateRate(allOverTimeCount, allPreAcceptanceCount);//总逾期率（逾期数/预受理数）
        double allCompletedRate = calculateRate(allCompletedCount, allPreAcceptanceCount);//总办结率（办结数/预受理数）


        //从数据库查询全部已补全和待补全的
        long allInSupplementCount = getAllInSupplementCount(stageId, null, null, isParallel, rootOrgId, source);
        long allSupplementedCount = getAllSupplementedCount(stageId, null, null, isParallel, rootOrgId, source);
        //封裝數據
        AeaAnaThemeDayStatistics result = new AeaAnaThemeDayStatistics();
        result.setThemeDayStatisticsId(UUID.randomUUID().toString());
        result.setStatisticsRecordId(statisticsRecordId);
        result.setThemeId(themeId);
        result.setThemeName(themeName);
        result.setApplyRecordId(stageId);
        result.setApplyRecordName(stageName);
        result.setDybzspjdxh(dybzspjdxh);
        result.setIsNode(isNode);
        result.setIsParallel(isParallel);
        result.setApplyinstSource(source);
        result.setDayApplyCount(dayApplyCount);
        result.setAllApplyCount(allApplyCount);
        result.setApplyLrr(applyLrr);
        result.setAllInSupplementCount(allInSupplementCount);
        result.setAllSupplementedCount(allSupplementedCount);
        result.setDayPreAcceptanceCount(hisPreAccept);
        result.setAllPreAcceptanceCount(allPreAcceptanceCount);
        result.setPreAcceptanceLrr(preAcceptanceLrr);
        result.setDayOutScopeCount(hisOutScope);
        result.setAllOutScopeCount(allOutScopeCount);
        result.setOutScopeLrr(outScopeLrr);
        result.setDayCompletedCount(hisCompleted);
        result.setAllCompletedCount(allCompletedCount);
        result.setCompletedLrr(completedLrr);
        result.setDayOverTimeCount(dayOverTimeCount);
        result.setAllOverTimeCount(allOverTimeCount);
        result.setOverTimeLrr(overTimeLrr);
        result.setAllPreAcceptanceRate(allPreAcceptanceRate);
        result.setAllOutScopeRate(allOutScopeRate);
        result.setAllOverTimeRate(allOverTimeRate);
        result.setAllCompletedRate(allCompletedRate);
        result.setStatisticsDate(statisticsStartDate);
        result.setRootOrgId(rootOrgId);

        return result;
    }

    /**
     * @param past    往期數
     * @param current 本期數
     * @return
     */
    private Double calculateGrowRate(Long past, Long current) {

      /*  if(past==null) past=Long.valueOf("0");//
        if(current==null) current=Long.valueOf("0");*/

        if (past == 0 && current == 0) {
            return 0.0;
        } else if (past == 0) {
            return 1.0;
        } else {
            BigDecimal b1 = new BigDecimal(current - past);
            BigDecimal b2 = new BigDecimal(past);
            BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();

        }
    }

    /**
     * 计算比率，保留两位小数
     *
     * @param
     * @param
     * @return
     */
    private Double calculateRate(Long dividend, Long divisor) {
       /* if(dividend==null) dividend=Long.valueOf("0");
        if(divisor==null) divisor=Long.valueOf("0");*/

        if (dividend == 0 && divisor == 0) {
            return 0.0;
        } else if (divisor == 0) {
            return 1.0;
        } else {
            BigDecimal b1 = new BigDecimal(dividend);
            BigDecimal b2 = new BigDecimal(divisor);
            BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();
        }
    }

    /**
     * 获取所有已补全的数量
     **/
    private long getAllSupplementedCount(String stageId, String startTime, String endTime, String isParallel, String rootOrgId, String source) {
        String[] states = {ApplyState.SUPPLEMENTARY.getValue()};
        return getCount(stageId, states, startTime, endTime, isParallel, rootOrgId, source);
    }

    /**
     * 获取所有待补全数量
     **/
    private long getAllInSupplementCount(String stageId, String startTime, String endTime, String isParallel, String rootOrgId, String source) {
        String[] states = {ApplyState.IN_THE_SUPPLEMENT.getValue()};
        return getCount(stageId, states, startTime, endTime, isParallel, rootOrgId, source);
    }

    /**
     * 获取时间段内接件数
     **/
  /*  private long getApplyCount(String stageId, String startTime, String endTime) {
        return getCount(stageId, null, startTime, endTime);
    }*/
    private long getCount(String stageId, String[] states, String startTime, String endTime, String isParallel, String rootOrgId, String source) {
        List<AeaHiApplyinst> list = new ArrayList<>();
        if ("0".equals(isParallel)) {
            list = applyinstMapper.getAeaHiApplyinstByStageIdAndStates(stageId, states, startTime, endTime, rootOrgId, isParallel, source);
        } else {
            list = applyinstMapper.getAeaHiApplyinstByStageIdAndStatesIsParallel(stageId, states, startTime, endTime, rootOrgId, isParallel, source);

        }
        return list.size();
    }

    /**
     * 统计方向：申报-阶段—主题，计算耗费内存，统计速度会快一点，而且没有为空各统计字段全为0的情况
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsThemeDayData2(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statisticsDate = DateUtils.getPreDateByDate(today);
        String dateFormat = sdf.format(statisticsDate);
        String startTime = dateFormat + " 00:00:00";
        String endTime = dateFormat + " 23:59:59";
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已统计过
        AeaAnaStatisticsRecord statisticsRecord = orgStatisticsImpl.buildStatisticsRecord(THEME_REPORTID_DAY, THEME_REPORTNAME_DAY, startTime, endTime, operateSource, "s", creater, rootOrgId);


        //查并联的
        List<ThemeDayApplyRecord> parLogHisList = logApplyStateHistMapper.listLogApplyStateHistBySourecAndParallel(startTime, endTime, rootOrgId, "0", "0");
        //查串联，分并行的
        List<ThemeDayApplyRecord> serLogHisList = logApplyStateHistMapper.listLogApplyStateHistBySourecAndParallel(startTime, endTime, rootOrgId, "1", "0");
        List<ThemeDayApplyRecord> serPstsllrlLogHisList = logApplyStateHistMapper.listLogApplyStateHistBySourecAndParallel(startTime, endTime, rootOrgId, "1", "1");

        parLogHisList.addAll(serLogHisList);
        parLogHisList.addAll(serPstsllrlLogHisList);

        result = packageThemeDayStastics(parLogHisList, statisticsRecord.getStatisticsRecordId(), statisticsRecord.getStatisticsStartDate(), rootOrgId);


        if (result.size() > 0) {
            //删除当日数据
            themeDayStatisticsMapper.deleteThisDayStatisticsData(dateFormat, rootOrgId);
            //批量插入记录
            themeDayStatisticsMapper.batchInsertAeaAnaThemeDayStatistics(result);
        }
        log.debug("日办件统计耗时：" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * 从applylog里面统计当天数据
     *
     * @param logHisList
     * @param statisticsRecordId
     * @param statisticsStartDate
     * @param rootOrgId
     * @return
     */
    private List<AeaAnaThemeDayStatistics> packageThemeDayStastics(List<ThemeDayApplyRecord> logHisList, String statisticsRecordId, Date statisticsStartDate, String rootOrgId) {
        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        List<List<ThemeDayApplyRecord>> detailList = new ArrayList<>();
        if (!logHisList.isEmpty()) {

            //先按是否并行
            Map<String, List<ThemeDayApplyRecord>> collect = logHisList.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getIsParallel));
            Set<Map.Entry<String, List<ThemeDayApplyRecord>>> entries = collect.entrySet();
            for (Map.Entry<String, List<ThemeDayApplyRecord>> entry : entries) {
                String key = entry.getKey();
                List<ThemeDayApplyRecord> value = entry.getValue();
                //按来源分组
                Map<String, List<ThemeDayApplyRecord>> sourceCollent = value.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getApplyinstSource));
                Set<Map.Entry<String, List<ThemeDayApplyRecord>>> sourceEntry = sourceCollent.entrySet();
                for (Map.Entry<String, List<ThemeDayApplyRecord>> sEntry : sourceEntry) {
                    detailList.add(sEntry.getValue());
                }
            }

        }

        for (int i = 0, len = detailList.size(); i < len; i++) {
            result.addAll(getThemeDayStatistics(detailList.get(i), statisticsRecordId, statisticsStartDate, rootOrgId));
        }

        return result;
    }

    private List<AeaAnaThemeDayStatistics> getThemeDayStatistics(List<ThemeDayApplyRecord> records, String statisticsRecordId, Date startDate, String rootOrgId) {
        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        if (records != null && !records.isEmpty()) {
            //按主题分组
            Map<String, List<ThemeDayApplyRecord>> themeCollect = records.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getThemeId));
            Set<Map.Entry<String, List<ThemeDayApplyRecord>>> entries = themeCollect.entrySet();

            for (Map.Entry<String, List<ThemeDayApplyRecord>> entry : entries) {
                List<ThemeDayApplyRecord> stageRecord = entry.getValue();
                //按阶段分组
                Map<String, List<ThemeDayApplyRecord>> collect = stageRecord.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getStageId));
                Set<Map.Entry<String, List<ThemeDayApplyRecord>>> stateEntries = collect.entrySet();
                for (Map.Entry<String, List<ThemeDayApplyRecord>> en : stateEntries) {
                    String key = en.getKey();
                    List<ThemeDayApplyRecord> value = en.getValue();
                    //封装一条数据
                    AeaAnaThemeDayStatistics dayStatistics = packageOneDayStatistics(statisticsRecordId, startDate, rootOrgId, key, value);
                    result.add(dayStatistics);
                }


            }

        }
        //
        return result;
    }

    private AeaAnaThemeDayStatistics packageOneDayStatistics(String statisticsRecordId, Date statisticsDate, String rootOrgId, String stageId, List<ThemeDayApplyRecord> records) {

        //这里isParallel和source是全部一样的
        ThemeDayApplyRecord applyRecord = records.get(0);
        String isParallel = applyRecord.getIsParallel();
        String applyinstSource = applyRecord.getApplyinstSource();
        String isApprove = applyRecord.getIsApprove();
        long dayApplyCount = 0, unapprovalApply = 0, approvedApply = 0, hisPreAccept = 0, hisInsupplement = 0, hisSupplementary = 0, hisOutScope = 0, hisCompleted = 0;

        Map<String, List<ThemeDayApplyRecord>> collect = records.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getNewState));
        Set<Map.Entry<String, List<ThemeDayApplyRecord>>> entries = collect.entrySet();
        for (Map.Entry<String, List<ThemeDayApplyRecord>> entry : entries) {
            List<ThemeDayApplyRecord> value = entry.getValue();
            String state = entry.getKey();
            if (ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue().equals(state)) {
                unapprovalApply += value.size();
            } else if (ApplyState.RECEIVE_APPROVED_APPLY.getValue().equals(state)) {
                approvedApply += value.size();
            } else if (ApplyState.ACCEPT_DEAL.getValue().equals(state)) {
                hisPreAccept += value.size();
            } else if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(state)) {
                hisInsupplement += value.size();
            } else if (ApplyState.SUPPLEMENTARY.getValue().equals(state)) {
                hisSupplementary += value.size();
            } else if (ApplyState.OUT_SCOPE.getValue().equals(state)) {
                hisOutScope += value.size();
            } else if (ApplyState.COMPLETED.getValue().equals(state)) {
                hisCompleted += value.size();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(statisticsDate);
        String startTime = startDate + " 00:00:00";
        String endTime = startDate + " 23:59:59";

        ThemeDayApplyRecord tmp = records.get(0);
        //查询当天逾期的东西
        List<ActStoTimeruleInst> actStoTimeruleInsts = themeDayStatisticsMapper.getStageStoTimeruleInst(stageId, "3", rootOrgId, isParallel, isApprove, applyinstSource, startTime, endTime);
        long dayOverTimeCount = actStoTimeruleInsts.size();
        String oneDayBefore = DateUtils.getPreDateByDate(startDate);
        String oneDayBeforeStart = oneDayBefore + " 00:00:00";
        String oneDayBeforeEnd = oneDayBefore + " 23:59:59";
        AeaAnaThemeDayStatistics onedayBeforStastics = themeDayStatisticsMapper.getAeaAnaThemeDayStatisticsBySatgeIdAndThemeId(tmp.getThemeId(), stageId, oneDayBeforeStart, oneDayBeforeEnd, rootOrgId, isParallel, applyinstSource);
        long allOverTimeCount = dayOverTimeCount + onedayBeforStastics.getAllOverTimeCount();
        dayApplyCount = unapprovalApply + approvedApply;
        double applyLrr = calculateGrowRate(onedayBeforStastics.getDayApplyCount(), dayApplyCount);
        double preAcceptanceLrr = calculateGrowRate(onedayBeforStastics.getDayPreAcceptanceCount(), hisPreAccept);
        double outScopeLrr = calculateGrowRate(onedayBeforStastics.getDayOutScopeCount(), hisOutScope);
        double completedLrr = calculateGrowRate(onedayBeforStastics.getDayCompletedCount(), hisCompleted);
        double overTimeLrr = calculateGrowRate(onedayBeforStastics.getDayOverTimeCount(), dayOverTimeCount);

        //縂比率统计
        long allPreAcceptanceCount = hisPreAccept + onedayBeforStastics.getAllPreAcceptanceCount();
        long allApplyCount = dayApplyCount + onedayBeforStastics.getAllApplyCount();
        long allOutScopeCount = hisOutScope + onedayBeforStastics.getAllOutScopeCount();
        long allCompletedCount = hisCompleted + onedayBeforStastics.getAllCompletedCount();

        double allPreAcceptanceRate = calculateRate(allPreAcceptanceCount, allApplyCount);//总预受理率（预受理数/接件数）
        double allOutScopeRate = calculateRate(allOutScopeCount, allApplyCount);//总不予受理率（不予受理数/接件数）
        double allOverTimeRate = calculateRate(allOverTimeCount, allPreAcceptanceCount);//总逾期率（逾期数/预受理数）
        double allCompletedRate = calculateRate(allCompletedCount, allPreAcceptanceCount);//总办结率（办结数/预受理数）


        //从数据库查询全部已补全和待补全的
        long allInSupplementCount = getAllInSupplementCount(stageId, null, null, isParallel, rootOrgId, applyinstSource);
        long allSupplementedCount = getAllSupplementedCount(stageId, null, null, isParallel, rootOrgId, applyinstSource);
        //封裝數據
        AeaAnaThemeDayStatistics result = new AeaAnaThemeDayStatistics();
        result.setThemeDayStatisticsId(UUID.randomUUID().toString());
        result.setStatisticsRecordId(statisticsRecordId);
        result.setThemeId(tmp.getThemeId());
        result.setThemeName(tmp.getThemeName());
        result.setApplyRecordId(stageId);
        result.setApplyRecordName(tmp.getStageName());
        result.setDybzspjdxh(tmp.getDybzspjdxh());
        result.setIsNode(tmp.getIsNode());
        result.setIsParallel(isParallel);
        result.setApplyinstSource(applyinstSource);
        result.setDayApplyCount(dayApplyCount);
        result.setAllApplyCount(allApplyCount);
        result.setApplyLrr(applyLrr);
        result.setAllInSupplementCount(allInSupplementCount);
        result.setAllSupplementedCount(allSupplementedCount);
        result.setDayPreAcceptanceCount(hisPreAccept);
        result.setAllPreAcceptanceCount(allPreAcceptanceCount);
        result.setPreAcceptanceLrr(preAcceptanceLrr);
        result.setDayOutScopeCount(hisOutScope);
        result.setAllOutScopeCount(allOutScopeCount);
        result.setOutScopeLrr(outScopeLrr);
        result.setDayCompletedCount(hisCompleted);
        result.setAllCompletedCount(allCompletedCount);
        result.setCompletedLrr(completedLrr);
        result.setDayOverTimeCount(dayOverTimeCount);
        result.setAllOverTimeCount(allOverTimeCount);
        result.setOverTimeLrr(overTimeLrr);
        result.setAllPreAcceptanceRate(allPreAcceptanceRate);
        result.setAllOutScopeRate(allOutScopeRate);
        result.setAllOverTimeRate(allOverTimeRate);
        result.setAllCompletedRate(allCompletedRate);
        result.setStatisticsDate(statisticsDate);
        result.setRootOrgId(rootOrgId);

        return result;
    }
}


