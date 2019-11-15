package com.augurit.efficiency.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.WinMonthCountVo;
import com.augurit.aplanmis.common.vo.analyse.WinStageApplyStatisticsVo;
import com.augurit.aplanmis.common.vo.analyse.WinWeekCountVo;
import com.augurit.aplanmis.common.vo.analyse.WinYearCountVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ma Yanhao
 * @date 2019/9/18 10:15
 * 定时统计的方法不要调用SecurityContext获取登录信息。
 * 定时器执行的方法是没有用户登录的。
 */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class AeaAnaWinStatisticsImpl {

    private static String WIN_REPORTID_DAY = "2001";
    private static String WIN_REPORTNAME_DAY = "窗口日申报统计表";
    private static String WIN_REPORTID_WEEK = "2002";
    private static String WIN_REPORTNAME_WEEK = "窗口周申报统计表";
    private static String WIN_REPORTID_MONTH = "2003";
    private static String WIN_REPORTNAME_MONTH = "窗口月申报统计表";
    private static String WIN_REPORTID_YEAR = "2004";
    private static String WIN_REPORTNAME_YEAR = "窗口年申报统计表";
    private static String DEFAULT_CREATER_SYS = "sys_admin_job";

    private static String APPLY_SOURCE_WIN = "win";
    private static String APPLY_SOURCE_NET = "net";

    @Autowired
    private AeaAnaOrgStatisticsImpl aeaAnaOrgStatisticsImpl;
    @Autowired
    private AeaServiceWindowMapper aeaServiceWindowMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;


    @Autowired
    private AeaAnaWinDayStatisticsMapper aeaAnaWinDayStatisticsMapper;
    @Autowired
    private AeaAnaWinWeekStatisticsMapper aeaAnaWinWeekStatisticsMapper;
    @Autowired
    private AeaAnaWinMonthStatisticsMapper aeaAnaWinMonthStatisticsMapper;
    @Autowired
    private AeaAnaWinYearStatisticsMapper aeaAnaWinYearStatisticsMapper;

    /**
     * 窗口申报日统计
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsWinDayData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaWinDayStatistics> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statisticsDate = DateUtils.getPreDateByDate(today);
        String dateFormat = sdf.format(statisticsDate);
        String startTime = dateFormat + " 00:00:00";
        String endTime = dateFormat + " 23:59:59";
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已统计过
        AeaAnaStatisticsRecord statisticsRecord = aeaAnaOrgStatisticsImpl.buildStatisticsRecord(WIN_REPORTID_DAY, WIN_REPORTNAME_DAY, startTime, endTime, operateSource, "s", creater, rootOrgId);
        //找到所有服务窗口
        AeaServiceWindow windowSearch = new AeaServiceWindow();
        windowSearch.setIsActive("1");
        windowSearch.setRootOrgId(rootOrgId);
        List<AeaServiceWindow> serviceWindows = aeaServiceWindowMapper.listAeaServiceWindow(windowSearch);
        for (AeaServiceWindow window : serviceWindows) {
            String windowId = window.getWindowId();
            String windowName = window.getWindowName();
            //统计此服务窗口的办理情况
            try {
                List<AeaAnaWinDayStatistics> oneWinList = statisticsOneWinDay(rootOrgId, window, statisticsRecord.getStatisticsRecordId(), statisticsRecord.getStatisticsStartDate());
                list.addAll(oneWinList);
            } catch (Exception e) {
                log.error("统计窗口日办件情况出错，窗口ID：{}，窗口名称：{}，错误信息：{}", windowId, windowName, e);
            }
        }
        //批量插入数据
        if (list.size() > 0) {
            //删除当日数据
            aeaAnaWinDayStatisticsMapper.deleteThisDayStatisticsData(dateFormat, rootOrgId);
            //批量插入记录
            aeaAnaWinDayStatisticsMapper.batchInserWinDayStatisticst(list);
        }
        log.debug("窗口日申报统计耗时：" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * 统计一天某窗口不同来源的数据
     *
     * @param rootOrgId
     * @param window
     * @param statisticsRecordId
     * @param statisticsDate
     * @return
     * @throws Exception
     */
    private List<AeaAnaWinDayStatistics> statisticsOneWinDay(String rootOrgId, AeaServiceWindow window, String statisticsRecordId, Date statisticsDate) throws Exception {
        List<AeaAnaWinDayStatistics> list = new ArrayList<>();

        //窗口申报-來源窗口 统计
        String applySource = "win";
        List<AeaAnaWinDayStatistics> statisticsByWinApply = statisticsOneWinDayByApplySource(rootOrgId, window, applySource, "0", statisticsRecordId, statisticsDate);
        List<AeaAnaWinDayStatistics> statisticsByWinParallelApply = statisticsOneWinDayByApplySource(rootOrgId, window, applySource, "1", statisticsRecordId, statisticsDate);
        //网上申报 统计
        applySource = "net";
        List<AeaAnaWinDayStatistics> statisticsByNetApply = statisticsOneWinDayByApplySource(rootOrgId, window, applySource, "0", statisticsRecordId, statisticsDate);
        List<AeaAnaWinDayStatistics> statisticsByNetParallelApply = statisticsOneWinDayByApplySource(rootOrgId, window, applySource, "1", statisticsRecordId, statisticsDate);

        list.addAll(statisticsByWinApply);
        list.addAll(statisticsByWinParallelApply);
        list.addAll(statisticsByNetApply);
        list.addAll(statisticsByNetParallelApply);
        return list;
    }

    /**
     * 统计一天某窗口某来源的数据
     *
     * @param rootOrgId
     * @param window
     * @param applySource
     * @return
     * @throws Exception
     */
    private List<AeaAnaWinDayStatistics> statisticsOneWinDayByApplySource(String rootOrgId, AeaServiceWindow window, String applySource, String isParallel, String statisticsRecordId, Date statisticsDate) throws Exception {

        String statisticsDateStr = DateUtils.convertDateToString(statisticsDate, "yyyy-MM-dd");
        String startTime = statisticsDateStr + " 00:00:00";
        String endTime = statisticsDateStr + " 23:59:59";

        String statisticsPreDate = DateUtils.getPreDateByDate(statisticsDateStr);

        Map<String, List<WinStageApplyStatisticsVo>> applyCollect = null;
        if (APPLY_SOURCE_WIN.equals(applySource)) {
            List<WinStageApplyStatisticsVo> applyByWin = aeaAnaWinDayStatisticsMapper.getApplyByWin(window.getWindowId(), isParallel, applySource, new String[]{ApplyState.RECEIVE_APPROVED_APPLY.getValue()},
                    new String[]{ApplyState.RECEIVE_APPROVED_APPLY.getValue()}, rootOrgId, startTime, endTime);
            applyCollect = applyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        } else {
            List<WinStageApplyStatisticsVo> applyByNet = aeaAnaWinDayStatisticsMapper.getApplyByNet(window.getWindowId(), isParallel, rootOrgId, startTime, endTime);
            applyCollect = applyByNet.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        }

        List<WinStageApplyStatisticsVo> inSupplementApplyByWin = aeaAnaWinDayStatisticsMapper.getSupplementApplyByWin(window.getWindowId(), isParallel,
                ApplyState.IN_THE_SUPPLEMENT.getValue(), applySource, rootOrgId, startTime, endTime);
        Map<String, List<WinStageApplyStatisticsVo>> inSupplementApplyCollect = inSupplementApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        List<WinStageApplyStatisticsVo> supplementApplyByWin = aeaAnaWinDayStatisticsMapper.getSupplementApplyByWin(window.getWindowId(), isParallel,
                ApplyState.SUPPLEMENTARY.getValue(), applySource, rootOrgId, startTime, endTime);
        Map<String, List<WinStageApplyStatisticsVo>> supplementApplyCollect = supplementApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        List<WinStageApplyStatisticsVo> acceptDealApplyByWin = aeaAnaWinDayStatisticsMapper.getApplyByWin(window.getWindowId(), isParallel, applySource,
                null, new String[]{ApplyState.ACCEPT_DEAL.getValue()}, rootOrgId, startTime, endTime);
        Map<String, List<WinStageApplyStatisticsVo>> acceptDealApplyCollect = acceptDealApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        List<WinStageApplyStatisticsVo> outScopeApplyByWin = aeaAnaWinDayStatisticsMapper.getApplyByWin(window.getWindowId(), isParallel, applySource,
                null, new String[]{ApplyState.OUT_SCOPE.getValue()}, rootOrgId, startTime, endTime);
        Map<String, List<WinStageApplyStatisticsVo>> outScopeApplyCollect = outScopeApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        List<WinStageApplyStatisticsVo> completedApplyByWin = aeaAnaWinDayStatisticsMapper.getApplyByWin(window.getWindowId(), isParallel, applySource,
                null, new String[]{ApplyState.COMPLETED.getValue()}, rootOrgId, startTime, endTime);
        Map<String, List<WinStageApplyStatisticsVo>> completedApplyCollect = completedApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        List<WinStageApplyStatisticsVo> overTimeApplyByWin = aeaAnaWinDayStatisticsMapper.getOverTimeApplyByWin(statisticsDateStr, window.getWindowId(),
                isParallel, applySource, rootOrgId, "3");
        Map<String, List<WinStageApplyStatisticsVo>> overTimeApplyCollect = overTimeApplyByWin.stream().collect(Collectors.groupingBy(WinStageApplyStatisticsVo::getStageId));

        //统计到所有阶段的ID
        Set<String> stageIds = new HashSet();
        stageIds.addAll(applyCollect.keySet());
        stageIds.addAll(inSupplementApplyCollect.keySet());
        stageIds.addAll(supplementApplyCollect.keySet());
        stageIds.addAll(acceptDealApplyCollect.keySet());
        stageIds.addAll(outScopeApplyCollect.keySet());
        stageIds.addAll(completedApplyCollect.keySet());
        stageIds.addAll(overTimeApplyCollect.keySet());

        List<AeaAnaWinDayStatistics> list = new ArrayList<>();
        for (String stageId : stageIds) {
            AeaAnaWinDayStatistics one = new AeaAnaWinDayStatistics();
            one.setRootOrgId(rootOrgId);
            one.setWindowId(window.getWindowId());
            one.setApplyRecordId(stageId);
            one.setIsParallel(isParallel);
            one.setApplySource(applySource);
            one.setStatisticsDate(DateUtils.convertStringToDate(statisticsPreDate, "yyyy-MM-dd"));
            List<AeaAnaWinDayStatistics> winPreStatisticsList = aeaAnaWinDayStatisticsMapper.listAeaAnaWinDayStatistics(one);
            AeaAnaWinDayStatistics winPreStatistics = null;
            if (CollectionUtils.isNotEmpty(winPreStatisticsList)) {
                winPreStatistics = winPreStatisticsList.get(0);
            } else {
                log.error("窗口申报统计，获取昨日统计为空！窗口ID:{}, 窗口名称:{}, 申报来源:{}, 统计时间:{}", window.getWindowId(), window.getWindowName(), applySource, statisticsPreDate);
            }

            AeaAnaWinDayStatistics winDayStatistics = new AeaAnaWinDayStatistics();
            winDayStatistics.setWindowDayStatisticsId(UuidUtil.generateUuid());
            winDayStatistics.setWindowId(window.getWindowId());
            winDayStatistics.setWindowName(window.getWindowName());
            winDayStatistics.setRegionId(window.getRegionId());
            winDayStatistics.setRegionName(window.getRegionName());
            winDayStatistics.setApplySource(applySource);

            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
            if (aeaParStage == null) {
                continue;
            }
            winDayStatistics.setApplyRecordId(stageId);
            winDayStatistics.setApplyRecordName(aeaParStage.getStageName());
            winDayStatistics.setDybzspjdxh(aeaParStage.getDybzspjdxh());
            winDayStatistics.setIsNode(aeaParStage.getIsNode());
            winDayStatistics.setIsParallel(isParallel);

            Long dayApplyCount = applyCollect.get(stageId) == null ? 0 : Long.valueOf(applyCollect.get(stageId).size());
            winDayStatistics.setDayApplyCount(dayApplyCount);
            //日接件环比
            Long preDayApplyCount = winPreStatistics == null ? 0 : winPreStatistics.getDayApplyCount();
            double applyLrr = calculateGrowRate(preDayApplyCount, dayApplyCount);
            winDayStatistics.setApplyLrr(applyLrr);

            winDayStatistics.setAllInSupplementCount(inSupplementApplyCollect.get(stageId) == null ? 0 : Long.valueOf(inSupplementApplyCollect.get(stageId).size()));
            winDayStatistics.setAllSupplementedCount(supplementApplyCollect.get(stageId) == null ? 0 : Long.valueOf(supplementApplyCollect.get(stageId).size()));

            Long dayPreAcceptanceCount = acceptDealApplyCollect.get(stageId) == null ? 0 : Long.valueOf(acceptDealApplyCollect.get(stageId).size());
            winDayStatistics.setDayPreAcceptanceCount(dayPreAcceptanceCount);
            //日预受理环比
            Long preDayPreAcceptanceCount = winPreStatistics == null ? 0 : winPreStatistics.getDayPreAcceptanceCount();
            double preAcceptanceLrr = calculateGrowRate(preDayPreAcceptanceCount, dayPreAcceptanceCount);
            winDayStatistics.setPreAcceptanceLrr(preAcceptanceLrr);

            Long dayOutScopeCount = outScopeApplyCollect.get(stageId) == null ? 0 : Long.valueOf(outScopeApplyCollect.get(stageId).size());
            winDayStatistics.setDayOutScopeCount(dayOutScopeCount);
            Long preDayOutScopeCount = winPreStatistics == null ? 0 : winPreStatistics.getDayOutScopeCount();
            double outScopeLrr = calculateGrowRate(preDayOutScopeCount, dayOutScopeCount);
            winDayStatistics.setOutScopeLrr(outScopeLrr);

            Long dayCompletedCount = completedApplyCollect.get(stageId) == null ? 0 : Long.valueOf(completedApplyCollect.get(stageId).size());
            winDayStatistics.setDayCompletedCount(dayCompletedCount);
            Long preDayCompletedCount = winPreStatistics == null ? 0 : winPreStatistics.getDayCompletedCount();
            double completedLrr = calculateGrowRate(preDayCompletedCount, dayCompletedCount);
            winDayStatistics.setCompletedLrr(completedLrr);

            Long dayOverTimeCount = overTimeApplyCollect.get(stageId) == null ? 0 : Long.valueOf(overTimeApplyCollect.get(stageId).size());
            winDayStatistics.setDayOverTimeCount(dayOverTimeCount);
            Long preDayOverTimeCount = winPreStatistics == null ? 0 : winPreStatistics.getAllOverTimeCount();
            double overTimeLrr = calculateGrowRate(preDayOverTimeCount, dayOverTimeCount);
            winDayStatistics.setOverTimeLrr(overTimeLrr);

            Long allApplyCount = 0L;
            if ("win".equals(applySource)) {
                allApplyCount = aeaAnaWinDayStatisticsMapper.countApplyByWin(window.getWindowId(), stageId, isParallel, applySource, new String[]{ApplyState.RECEIVE_APPROVED_APPLY.getValue()},
                        new String[]{ApplyState.RECEIVE_APPROVED_APPLY.getValue()}, rootOrgId, null, endTime);
                winDayStatistics.setAllApplyCount(allApplyCount);
            } else {
                allApplyCount = aeaAnaWinDayStatisticsMapper.countApplyByNet(window.getWindowId(), stageId, isParallel, rootOrgId, null, endTime);
                winDayStatistics.setAllApplyCount(allApplyCount);
            }

            Long allPreAcceptanceCount = aeaAnaWinDayStatisticsMapper.countApplyByWin(window.getWindowId(), stageId, isParallel, applySource, null,
                    new String[]{ApplyState.ACCEPT_DEAL.getValue()}, rootOrgId, null, endTime);
            winDayStatistics.setAllPreAcceptanceCount(allPreAcceptanceCount);
            Long allOutScopeCount = aeaAnaWinDayStatisticsMapper.countApplyByWin(window.getWindowId(), stageId, isParallel, applySource, null,
                    new String[]{ApplyState.OUT_SCOPE.getValue()}, rootOrgId, null, endTime);
            winDayStatistics.setAllOutScopeCount(allOutScopeCount);
            Long allCompletedCount = aeaAnaWinDayStatisticsMapper.countApplyByWin(window.getWindowId(), stageId, isParallel, applySource, null,
                    new String[]{ApplyState.COMPLETED.getValue()}, rootOrgId, null, endTime);
            winDayStatistics.setAllCompletedCount(allCompletedCount);
            Long allOverTimeCount = aeaAnaWinDayStatisticsMapper.countOverTimeApplyByWin(null, window.getWindowId(), stageId,
                    isParallel, applySource, rootOrgId, "3");
            winDayStatistics.setAllOverTimeCount(allOverTimeCount);

            //总比率
            double allPreAcceptanceRate = calculateRate(allPreAcceptanceCount, allApplyCount);
            double allOutScopeRate = calculateRate(allOutScopeCount, allApplyCount);
            double allOverTimeRate = calculateRate(allOverTimeCount, allPreAcceptanceCount);
            double allCompletedRate = calculateRate(allCompletedCount, allPreAcceptanceCount);

            winDayStatistics.setAllPreAcceptanceRate(allPreAcceptanceRate);
            winDayStatistics.setAllOutScopeRate(allOutScopeRate);
            winDayStatistics.setAllOverTimeRate(allOverTimeRate);
            winDayStatistics.setAllCompletedRate(allCompletedRate);

            winDayStatistics.setRootOrgId(rootOrgId);
            winDayStatistics.setStatisticsRecordId(statisticsRecordId);
            winDayStatistics.setStatisticsDate(statisticsDate);
            list.add(winDayStatistics);
        }

        return list;
    }


    /**
     * 窗口申报周统计
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsWinWeekData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaWinWeekStatistics> result = new ArrayList<>();
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
        AeaAnaStatisticsRecord record = aeaAnaOrgStatisticsImpl.buildStatisticsRecord(WIN_REPORTID_WEEK, WIN_REPORTNAME_WEEK, startTime, endTime, operateSource, "s", creater, rootOrgId);

        int weekNum = DateUtils.getThisWeekNum(preDate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(preDate);
        int year = ca.get(Calendar.YEAR);

        //根据日统计汇总这周的数据
        List<WinWeekCountVo> weekStatisticsFromDay = aeaAnaWinWeekStatisticsMapper.getWeekStatisticsFromDay(startTime, endTime, year, weekNum - 1, sdf.format(preDate), rootOrgId);
        if (CollectionUtils.isNotEmpty(weekStatisticsFromDay)) {
            for (WinWeekCountVo winWeekCountVo : weekStatisticsFromDay) {

                AeaAnaWinWeekStatistics winWeekStatistics = null;
                try {
                    winWeekStatistics = new AeaAnaWinWeekStatistics();
                    winWeekStatistics.setWindowWeekStatisticsId(UuidUtil.generateUuid());
                    winWeekStatistics.setStatisticsRecordId(record.getStatisticsRecordId());

                    //设置这周最新日统计的总数
                    winWeekStatistics.setWindowId(winWeekCountVo.getWindowId());
                    AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(winWeekCountVo.getWindowId());
                    winWeekStatistics.setWindowName(window.getWindowName());
                    winWeekStatistics.setRegionId(window.getRegionId());
                    winWeekStatistics.setRegionName(window.getRegionName());
                    winWeekStatistics.setApplyRecordId(winWeekCountVo.getApplyRecordId());
                    AeaParStage stage = aeaParStageMapper.getAeaParStageById(winWeekCountVo.getApplyRecordId());
                    winWeekStatistics.setApplyRecordName(stage.getStageName());
                    winWeekStatistics.setDybzspjdxh(stage.getDybzspjdxh());
                    winWeekStatistics.setIsNode(stage.getIsNode());
                    winWeekStatistics.setIsParallel(winWeekCountVo.getIsParallel());
                    winWeekStatistics.setApplySource(winWeekCountVo.getApplySource());

                    //获取最新的日数据
                    AeaAnaWinDayStatistics search = new AeaAnaWinDayStatistics();
                    search.setWindowId(winWeekCountVo.getWindowId());
                    search.setApplySource(winWeekCountVo.getApplySource());
                    search.setApplyRecordId(winWeekCountVo.getApplyRecordId());
                    search.setIsParallel(winWeekCountVo.getIsParallel());
                    search.setRootOrgId(rootOrgId);
                    search.setStartTime(sdf.format(monday));
                    search.setStartTime(sdf.format(sunday));
                    List<AeaAnaWinDayStatistics> recentWinDayStatistics = aeaAnaWinDayStatisticsMapper.listAeaAnaWinDayStatistics(search);

                    if (CollectionUtils.isNotEmpty(recentWinDayStatistics)) {
                        AeaAnaWinDayStatistics aeaAnaWinDayStatistics = recentWinDayStatistics.get(0);
                        winWeekStatistics.setAllApplyCount(aeaAnaWinDayStatistics.getAllApplyCount());
                        winWeekStatistics.setAllPreAcceptanceCount(aeaAnaWinDayStatistics.getAllInSupplementCount());
                        winWeekStatistics.setAllOutScopeCount(aeaAnaWinDayStatistics.getAllOutScopeCount());
                        winWeekStatistics.setAllCompletedCount(aeaAnaWinDayStatistics.getAllCompletedCount());
                        winWeekStatistics.setAllOverTimeCount(aeaAnaWinDayStatistics.getAllOverTimeCount());
                        winWeekStatistics.setAllPreAcceptanceRate(aeaAnaWinDayStatistics.getAllPreAcceptanceRate());
                        winWeekStatistics.setAllOutScopeRate(aeaAnaWinDayStatistics.getAllOutScopeRate());
                        winWeekStatistics.setAllOverTimeRate(aeaAnaWinDayStatistics.getAllOverTimeRate());
                        winWeekStatistics.setAllCompletedRate(aeaAnaWinDayStatistics.getAllCompletedRate());
                    } else {
                        winWeekStatistics.setAllApplyCount(0L);
                        winWeekStatistics.setAllPreAcceptanceCount(0L);
                        winWeekStatistics.setAllOutScopeCount(0L);
                        winWeekStatistics.setAllCompletedCount(0L);
                        winWeekStatistics.setAllOverTimeCount(0L);
                        winWeekStatistics.setAllPreAcceptanceRate(0.0);
                        winWeekStatistics.setAllOutScopeRate(0.0);
                        winWeekStatistics.setAllOverTimeRate(0.0);
                        winWeekStatistics.setAllCompletedRate(0.0);
                    }

                    //设置这周的周统计
                    winWeekStatistics.setWeekApplyCount(winWeekCountVo.getWeekApplyCount());
                    winWeekStatistics.setAllInSupplementCount(winWeekCountVo.getAllInSupplementCount());
                    winWeekStatistics.setAllSupplementedCount(winWeekCountVo.getAllSupplementedCount());
                    winWeekStatistics.setWeekPreAcceptanceCount(winWeekCountVo.getWeekPreAcceptanceCount());
                    winWeekStatistics.setWeekOutScopeCount(winWeekCountVo.getWeekOutScopeCount());
                    winWeekStatistics.setWeekCompletedCount(winWeekCountVo.getWeekCompletedCount());
                    winWeekStatistics.setWeekOverTimeCount(winWeekCountVo.getWeekOverTimeCount());

                    //计算环比
                    winWeekStatistics.setApplyLrr(calculateGrowRate(winWeekCountVo.getLastWeekApplyCount(), winWeekCountVo.getWeekApplyCount()));
                    winWeekStatistics.setPreAcceptanceLrr(calculateGrowRate(winWeekCountVo.getLastWeekPreAcceptacneCount(), winWeekCountVo.getWeekPreAcceptanceCount()));
                    winWeekStatistics.setOutScopeLrr(calculateGrowRate(winWeekCountVo.getLastWeekOutScopeCount(), winWeekCountVo.getWeekOutScopeCount()));
                    winWeekStatistics.setCompletedLrr(calculateGrowRate(winWeekCountVo.getLastWeekCompletedCount(), winWeekCountVo.getWeekCompletedCount()));
                    winWeekStatistics.setOverTimeLrr(calculateGrowRate(winWeekCountVo.getLastWeekOverTimeCount(), winWeekCountVo.getWeekOverTimeCount()));

                    winWeekStatistics.setStatisticsYear(Long.valueOf(year));
                    winWeekStatistics.setWeekNum(Long.valueOf(weekNum));
                    winWeekStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
                    winWeekStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                    winWeekStatistics.setModifier(creater);
                    winWeekStatistics.setModifyTime(new Date());
                    winWeekStatistics.setRootOrgId(rootOrgId);

                    result.add(winWeekStatistics);
                } catch (Exception e) {
                    log.error("窗口统计周统计失败："+e.getMessage());
                    log.error("错误记录:"+JSONObject.toJSONString(winWeekCountVo));
                    e.printStackTrace();
                }

            }
            if (result.size() > 0) {
                //删除本周数据
                aeaAnaWinWeekStatisticsMapper.deleteThisWeekStatisticsData(year, weekNum, rootOrgId);
                //批量插入记录
                aeaAnaWinWeekStatisticsMapper.batchInsertWeekStatisticsData(result);
            }
            log.debug("周窗口申办统计耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            log.error("【周窗口申办统计异常】weekStatisticsFromDay为空，统计参数:{ startTime:{}, endTime:{}, year:{}, weekNum:{} }",
                    startTime, endTime, year, weekNum);
        }
    }


    /**
     * 窗口申报月统计
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsWinMonthData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaWinMonthStatistics> result = new ArrayList<>();
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
        AeaAnaStatisticsRecord record = aeaAnaOrgStatisticsImpl.buildStatisticsRecord(WIN_REPORTID_MONTH, WIN_REPORTNAME_MONTH, startTime, endTime, operateSource, "s", creater, rootOrgId);

        //根据日统计汇总这月的数据
        List<WinMonthCountVo> monthStatisticsFromDay = aeaAnaWinMonthStatisticsMapper.getMonthStatisticsFromDay(startTime, endTime, lastMonth, lastYear, sdf.format(preDate), rootOrgId);
        if (CollectionUtils.isNotEmpty(monthStatisticsFromDay)) {
            for (WinMonthCountVo winMonthCountVo : monthStatisticsFromDay) {
                try {
                    AeaAnaWinMonthStatistics winMonthStatistics = new AeaAnaWinMonthStatistics();
                    winMonthStatistics.setWindowMonthStatisticsId(UuidUtil.generateUuid());
                    winMonthStatistics.setStatisticsRecordId(record.getStatisticsRecordId());

                    //设置这月最新日统计的总数
                    winMonthStatistics.setWindowId(winMonthCountVo.getWindowId());
                    AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(winMonthCountVo.getWindowId());
                    winMonthStatistics.setWindowName(window.getWindowName());
                    winMonthStatistics.setRegionId(window.getRegionId());
                    winMonthStatistics.setRegionName(window.getRegionName());
                    winMonthStatistics.setApplySource(winMonthCountVo.getApplySource());
                    winMonthStatistics.setApplyRecordId(winMonthCountVo.getApplyRecordId());
                    AeaParStage stage = aeaParStageMapper.getAeaParStageById(winMonthCountVo.getApplyRecordId());
                    winMonthStatistics.setApplyRecordName(stage.getStageName());
                    winMonthStatistics.setDybzspjdxh(stage.getDybzspjdxh());
                    winMonthStatistics.setIsNode(stage.getIsNode());
                    winMonthStatistics.setIsParallel(winMonthCountVo.getIsParallel());
                    winMonthStatistics.setApplySource(winMonthCountVo.getApplySource());

                    //获取最新的日数据
                    AeaAnaWinDayStatistics search = new AeaAnaWinDayStatistics();
                    search.setWindowId(winMonthCountVo.getWindowId());
                    search.setApplySource(winMonthCountVo.getApplySource());
                    search.setApplyRecordId(winMonthCountVo.getApplyRecordId());
                    search.setIsParallel(winMonthCountVo.getIsParallel());
                    search.setRootOrgId(rootOrgId);
                    search.setStartTime(sdf.format(firstDay));
                    search.setStartTime(sdf.format(lastDay));
                    List<AeaAnaWinDayStatistics> recentWinDayStatistics = aeaAnaWinDayStatisticsMapper.listAeaAnaWinDayStatistics(search);

                    if (CollectionUtils.isNotEmpty(recentWinDayStatistics)) {
                        AeaAnaWinDayStatistics aeaAnaWinDayStatistics = recentWinDayStatistics.get(0);
                        winMonthStatistics.setAllApplyCount(aeaAnaWinDayStatistics.getAllApplyCount());
                        winMonthStatistics.setAllPreAcceptanceCount(aeaAnaWinDayStatistics.getAllPreAcceptanceCount());
                        winMonthStatistics.setAllOutScopeCount(aeaAnaWinDayStatistics.getAllOutScopeCount());
                        winMonthStatistics.setAllCompletedCount(aeaAnaWinDayStatistics.getAllCompletedCount());
                        winMonthStatistics.setAllOverTimeCount(aeaAnaWinDayStatistics.getAllOverTimeCount());
                        winMonthStatistics.setAllPreAcceptanceRate(aeaAnaWinDayStatistics.getAllPreAcceptanceRate());
                        winMonthStatistics.setAllOutScopeRate(aeaAnaWinDayStatistics.getAllOutScopeRate());
                        winMonthStatistics.setAllOverTimeRate(aeaAnaWinDayStatistics.getAllOverTimeRate());
                        winMonthStatistics.setAllCompletedRate(aeaAnaWinDayStatistics.getAllCompletedRate());
                    } else {
                        winMonthStatistics.setAllApplyCount(0L);
                        winMonthStatistics.setAllInSupplementCount(0L);
                        winMonthStatistics.setAllSupplementedCount(0L);
                        winMonthStatistics.setAllPreAcceptanceCount(0L);
                        winMonthStatistics.setAllOutScopeCount(0L);
                        winMonthStatistics.setAllCompletedCount(0L);
                        winMonthStatistics.setAllOverTimeCount(0L);
                        winMonthStatistics.setAllPreAcceptanceRate(0.0);
                        winMonthStatistics.setAllOutScopeRate(0.0);
                        winMonthStatistics.setAllOverTimeRate(0.0);
                        winMonthStatistics.setAllCompletedRate(0.0);
                    }

                    //设置这月的月统计
                    winMonthStatistics.setMonthApplyCount(winMonthCountVo.getMonthApplyCount());
                    winMonthStatistics.setAllInSupplementCount(winMonthCountVo.getAllInSupplementCount());
                    winMonthStatistics.setAllSupplementedCount(winMonthCountVo.getAllSupplementedCount());
                    winMonthStatistics.setMonthPreAcceptanceCount(winMonthCountVo.getMonthPreAcceptanceCount());
                    winMonthStatistics.setMonthOutScopeCount(winMonthCountVo.getMonthOutScopeCount());
                    winMonthStatistics.setMonthCompletedCount(winMonthCountVo.getMonthCompletedCount());
                    winMonthStatistics.setMonthOverTimeCount(winMonthCountVo.getMonthOverTimeCount());

                    //计算环比&同比
                    winMonthStatistics.setApplyLrr(calculateGrowRate(winMonthCountVo.getLastMonthApplyCount(), winMonthCountVo.getMonthApplyCount()));
                    winMonthStatistics.setInSupplementLrr(calculateGrowRate(winMonthCountVo.getLastMonthInSupplementCount(), winMonthCountVo.getAllInSupplementCount()));
                    winMonthStatistics.setSupplementedLrr(calculateGrowRate(winMonthCountVo.getLastMonthSupplementedCount(), winMonthCountVo.getAllSupplementedCount()));
                    winMonthStatistics.setPreAcceptanceLrr(calculateGrowRate(winMonthCountVo.getLastMonthPreAcceptacneCount(), winMonthCountVo.getMonthPreAcceptanceCount()));
                    winMonthStatistics.setOutScopeLrr(calculateGrowRate(winMonthCountVo.getLastMonthOutScopeCount(), winMonthCountVo.getMonthOutScopeCount()));
                    winMonthStatistics.setCompletedLrr(calculateGrowRate(winMonthCountVo.getLastMonthCompletedCount(), winMonthCountVo.getMonthCompletedCount()));
                    winMonthStatistics.setOverTimeLrr(calculateGrowRate(winMonthCountVo.getLastMonthOverTimeCount(), winMonthCountVo.getMonthOverTimeCount()));

                    winMonthStatistics.setApplyOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearApplyCount(), winMonthCountVo.getMonthApplyCount()));
                    winMonthStatistics.setInSupplementOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearInSupplementCount(), winMonthCountVo.getAllInSupplementCount()));
                    winMonthStatistics.setSupplementedOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearSupplementedCount(), winMonthCountVo.getAllSupplementedCount()));
                    winMonthStatistics.setPreAcceptanceOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearPreAcceptacneCount(), winMonthCountVo.getMonthPreAcceptanceCount()));
                    winMonthStatistics.setOutScopeOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearOutScopeCount(), winMonthCountVo.getMonthOutScopeCount()));
                    winMonthStatistics.setCompletedOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearCompletedCount(), winMonthCountVo.getMonthCompletedCount()));
                    winMonthStatistics.setOverTimeOnYoyBasis(calculateGrowRate(winMonthCountVo.getLastYearOverTimeCount(), winMonthCountVo.getMonthOverTimeCount()));

                    winMonthStatistics.setStatisticsMonth(nowMonth);
                    winMonthStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
                    winMonthStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                    winMonthStatistics.setModifier(creater);
                    winMonthStatistics.setModifyTime(new Date());
                    winMonthStatistics.setRootOrgId(rootOrgId);

                    result.add(winMonthStatistics);
                }catch (Exception e){
                    log.error("窗口统计月失败："+e.getMessage());
                    log.error("失败记录："+JSONObject.toJSONString(winMonthCountVo));

                    e.printStackTrace();
                }

            }
            if (result.size() > 0) {
                //删除本月数据
                aeaAnaWinMonthStatisticsMapper.deleteThisMonthStatisticsData(nowMonth, rootOrgId);
                //批量插入记录
                aeaAnaWinMonthStatisticsMapper.batchInsertMonthStatisticsData(result);
            }
            log.debug("月窗口申办统计耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            log.error("【月窗口申办统计异常】monthStatisticsFromDay为空，统计参数:{ startTime:{}, endTime:{}, year:{}, month:{} }",
                    startTime, endTime, year, month);
        }
    }


    /**
     * 窗口申报年统计
     *
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsWinYearData(String rootOrgId, String operateSource, String creater, Date today) throws Exception {
        long beginTime = System.currentTimeMillis();
        List<AeaAnaWinYearStatistics> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        //昨天
        Date preDate = DateUtils.getPreDateByDate(today);
        String year = sdf.format(preDate);
        //获取起始时间
        String startMonth = year + "-01";
        String endMonth = year + "-12";
        String startTime = startMonth + "-01 00:00:00";
        String endTime = endMonth + "-31 23:59:59";
        if (StringUtils.isBlank(creater)) {
            creater = DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord record = aeaAnaOrgStatisticsImpl.buildStatisticsRecord(WIN_REPORTID_YEAR, WIN_REPORTNAME_YEAR, startTime, endTime, operateSource, "s", creater, rootOrgId);

        //根据月统计汇总这年的数据
        List<WinYearCountVo> yearStatisticsFromMonth = aeaAnaWinYearStatisticsMapper.getYearStatisticsFromMonth(rootOrgId, startMonth, endMonth, Integer.parseInt(year) - 1, DateUtils.convertDateToString(preDate, "yyyy-MM-dd"));
        if (CollectionUtils.isNotEmpty(yearStatisticsFromMonth)) {
            for (WinYearCountVo winYearCountVo : yearStatisticsFromMonth) {

                AeaAnaWinYearStatistics winYearStatistics = null;
                try {
                    winYearStatistics = new AeaAnaWinYearStatistics();
                    winYearStatistics.setWindowYearStatisticsId(UuidUtil.generateUuid());
                    winYearStatistics.setStatisticsRecordId(record.getStatisticsRecordId());


                    //设置这年最新日统计的总数
                    winYearStatistics.setWindowId(winYearCountVo.getWindowId());
                    AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(winYearCountVo.getWindowId());
                    winYearStatistics.setWindowName(window.getWindowName());
                    winYearStatistics.setRegionId(window.getRegionId());
                    winYearStatistics.setRegionName(window.getRegionName());
                    winYearStatistics.setApplyRecordId(winYearCountVo.getApplyRecordId());
                    AeaParStage stage = aeaParStageMapper.getAeaParStageById(winYearCountVo.getApplyRecordId());
                    winYearStatistics.setApplyRecordName(stage.getStageName());
                    winYearStatistics.setDybzspjdxh(stage.getDybzspjdxh());
                    winYearStatistics.setIsNode(stage.getIsNode());
                    winYearStatistics.setIsParallel(winYearCountVo.getIsParallel());
                    winYearStatistics.setApplySource(winYearCountVo.getApplySource());

                    //获取最新的日数据
                    AeaAnaWinMonthStatistics search = new AeaAnaWinMonthStatistics();
                    search.setWindowId(winYearCountVo.getWindowId());
                    search.setApplySource(winYearCountVo.getApplySource());
                    search.setApplyRecordId(winYearCountVo.getApplyRecordId());
                    search.setIsParallel(winYearCountVo.getIsParallel());
                    search.setRootOrgId(rootOrgId);
                    search.setStartTime(startMonth);
                    search.setEndTime(endMonth);
                    List<AeaAnaWinMonthStatistics> recentWinMonthStatistics = aeaAnaWinMonthStatisticsMapper.listAeaAnaWinMonthStatistics(search);

                    if (CollectionUtils.isNotEmpty(recentWinMonthStatistics)) {
                        AeaAnaWinMonthStatistics aeaAnaWinMonthStatistics = recentWinMonthStatistics.get(0);
                        winYearStatistics.setAllApplyCount(aeaAnaWinMonthStatistics.getAllApplyCount());
                        winYearStatistics.setAllPreAcceptanceCount(aeaAnaWinMonthStatistics.getAllInSupplementCount());
                        winYearStatistics.setAllOutScopeCount(aeaAnaWinMonthStatistics.getAllOutScopeCount());
                        winYearStatistics.setAllCompletedCount(aeaAnaWinMonthStatistics.getAllCompletedCount());
                        winYearStatistics.setAllOverTimeCount(aeaAnaWinMonthStatistics.getAllOverTimeCount());
                        winYearStatistics.setAllPreAcceptanceRate(aeaAnaWinMonthStatistics.getAllPreAcceptanceRate());
                        winYearStatistics.setAllOutScopeRate(aeaAnaWinMonthStatistics.getAllOutScopeRate());
                        winYearStatistics.setAllOverTimeRate(aeaAnaWinMonthStatistics.getAllOverTimeRate());
                        winYearStatistics.setAllCompletedRate(aeaAnaWinMonthStatistics.getAllCompletedRate());
                    } else {
                        winYearStatistics.setAllApplyCount(0L);
                        winYearStatistics.setAllPreAcceptanceCount(0L);
                        winYearStatistics.setAllOutScopeCount(0L);
                        winYearStatistics.setAllCompletedCount(0L);
                        winYearStatistics.setAllOverTimeCount(0L);
                        winYearStatistics.setAllPreAcceptanceRate(0.0);
                        winYearStatistics.setAllOutScopeRate(0.0);
                        winYearStatistics.setAllOverTimeRate(0.0);
                        winYearStatistics.setAllCompletedRate(0.0);
                    }

                    //设置这年的统计
                    winYearStatistics.setYearApplyCount(winYearCountVo.getYearApplyCount());
                    winYearStatistics.setAllInSupplementCount(winYearCountVo.getAllInSupplementCount());
                    winYearStatistics.setAllSupplementedCount(winYearCountVo.getAllSupplementedCount());
                    winYearStatistics.setYearPreAcceptanceCount(winYearCountVo.getYearPreAcceptanceCount());
                    winYearStatistics.setYearOutScopeCount(winYearCountVo.getYearOutScopeCount());
                    winYearStatistics.setYearCompletedCount(winYearCountVo.getYearCompletedCount());
                    winYearStatistics.setYearOverTimeCount(winYearCountVo.getYearOverTimeCount());

                    //计算环比
                    winYearStatistics.setInSupplementLrr(calculateGrowRate(winYearCountVo.getLastYearInSupplementCount(), winYearCountVo.getAllInSupplementCount()));
                    winYearStatistics.setSupplementedLrr(calculateGrowRate(winYearCountVo.getLastYearSupplementedCount(), winYearCountVo.getAllSupplementedCount()));
                    winYearStatistics.setApplyLrr(calculateGrowRate(winYearCountVo.getLastYearApplyCount(), winYearCountVo.getYearApplyCount()));
                    winYearStatistics.setPreAcceptanceLrr(calculateGrowRate(winYearCountVo.getLastYearPreAcceptacneCount(), winYearCountVo.getYearPreAcceptanceCount()));
                    winYearStatistics.setOutScopeLrr(calculateGrowRate(winYearCountVo.getLastYearOutScopeCount(), winYearCountVo.getYearOutScopeCount()));
                    winYearStatistics.setCompletedLrr(calculateGrowRate(winYearCountVo.getLastYearCompletedCount(), winYearCountVo.getYearCompletedCount()));
                    winYearStatistics.setOverTimeLrr(calculateGrowRate(winYearCountVo.getLastYearOverTimeCount(), winYearCountVo.getYearOverTimeCount()));

                    winYearStatistics.setStatisticsYear(Long.valueOf(year));
                    winYearStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"));
                    winYearStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                    winYearStatistics.setModifier(creater);
                    winYearStatistics.setModifyTime(new Date());
                    winYearStatistics.setRootOrgId(rootOrgId);

                    result.add(winYearStatistics);
                } catch (Exception e) {
                   log.error("窗口统计年数据失败！"+e.getMessage());
                   log.error("错误数据记录："+JSONObject.toJSONString(winYearCountVo));
                   e.printStackTrace();
                }

            }
            if (result.size() > 0) {
                //删除本周数据
                aeaAnaWinYearStatisticsMapper.deleteThisYearStatisticsData(year, rootOrgId);
                //批量插入记录
                aeaAnaWinYearStatisticsMapper.batchInsertYearStatisticsData(result);
            }
            log.debug("年窗口申办统计耗时：" + (System.currentTimeMillis() - beginTime));
        } else {
            log.error("【年窗口申办统计异常】yearStatisticsFromMonth为空，统计参数:{ startTime:{}, endTime:{}, year:{} }",
                    startTime, endTime, year);
        }
    }

    /**
     * @param past    往期數
     * @param current 本期數
     * @return
     */
    private Double calculateGrowRate(Long past, Long current) {
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

}
