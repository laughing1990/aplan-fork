package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.handler.BaseEnum;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.utils.AnalyseUtils;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.aplanmis.common.vo.conditional.ApplyInfo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import com.augurit.efficiency.service.WinEfficiencySupervisionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 9:16
 */
@Service
@Slf4j
public class WinEfficiencySupervisionServiceImpl implements WinEfficiencySupervisionService {

    @Autowired
    AeaServiceWindowUserMapper aeaServiceWindowUserMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;
    @Autowired
    private EfficiencySupervisionMapper efficiencySupervisionMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private BscDicCodeItemService bscDicCodeItemService;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;
    @Autowired
    private AeaAnaThemeWeekStatisticsMapper aeaAnaThemeWeekStatisticsMapper;
    @Autowired
    private AeaAnaThemeMonthStatisticsMapper aeaAnaThemeMonthStatisticsMapper;
    @Autowired
    private AeaAnaThemeYearStatisticsMapper aeaAnaThemeYearStatisticsMapper;
    @Autowired
    private AeaAnaWinDayStatisticsMapper aeaAnaWinDayStatisticsMapper;
    @Autowired
    private AeaAnaWinWeekStatisticsMapper aeaAnaWinWeekStatisticsMapper;
    @Autowired
    private AeaAnaWinMonthStatisticsMapper aeaAnaWinMonthStatisticsMapper;
    @Autowired
    private AeaServiceWindowMapper aeaServiceWindowMapper;
    @Autowired
    private ConditionalQueryMapper conditionalQueryMapper;
    @Autowired
    private AeaLogApplyStateHistMapper aeaLogApplyStateHistMapper;

    private static String YISHOULI = "yiShouLi";
    private static String CAILIAOBUQUAN = "caiLiaoBuQuan";
    private static String BUYUSHOULI = "buYuShouLi";

    /**
     * 主题申报统计
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getThemeApplyStatistics(String startTime, String endTime) throws Exception {
        Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
        Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int startYear = calendar.get(Calendar.YEAR);

        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);

        Map<String, Object> result = new HashMap<>();
        if (endYear - startYear > 1) {
            //1.开始
            calendar.set(Calendar.YEAR, startYear);
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            String startEndTime = DateUtils.convertDateToString(calendar.getTime(), "yyyy-MM-dd");
            List<ThemeApplyStatisticsVo> themeApplyByTimeStart = aeaAnaThemeDayStatisticsMapper.getThemeApplyByTime(startTime, startEndTime, SecurityContext.getCurrentOrgId());

            //2.期间
            int startDuringYear = startYear + 1;
            int endDuringYear = endYear - 1;
            List<ThemeApplyStatisticsVo> themeApplyByTimeDuring = aeaAnaThemeYearStatisticsMapper.getThemeApplyByTime(startDuringYear, endDuringYear, SecurityContext.getCurrentOrgId());

            //3.结束
            calendar.set(Calendar.YEAR, endYear);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String endStartTime = DateUtils.convertDateToString(calendar.getTime(), "yyyy-MM-dd");
            List<ThemeApplyStatisticsVo> themeApplyByTimeEnd = aeaAnaThemeDayStatisticsMapper.getThemeApplyByTime(endStartTime, endTime, SecurityContext.getCurrentOrgId());

            Map<String, ThemeApplyStatisticsVo> themeApplyByTimeMap = new HashMap<>();

            for (ThemeApplyStatisticsVo vo : themeApplyByTimeStart) {
                themeApplyByTimeMap.put(vo.getName(), vo);
            }
            for (ThemeApplyStatisticsVo vo : themeApplyByTimeDuring) {
                if (themeApplyByTimeMap.size() != 0 && themeApplyByTimeMap.get(vo.getName()) != null) {
                    themeApplyByTimeMap.get(vo.getName()).setApplyCount(vo.getApplyCount() + themeApplyByTimeMap.get(vo.getName()).getApplyCount());
                } else {
                    themeApplyByTimeMap.put(vo.getName(), vo);
                }
            }
            for (ThemeApplyStatisticsVo vo : themeApplyByTimeEnd) {
                if (themeApplyByTimeMap.size() != 0 && themeApplyByTimeMap.get(vo.getName()) != null) {
                    themeApplyByTimeMap.get(vo.getName()).setApplyCount(vo.getApplyCount() + themeApplyByTimeMap.get(vo.getName()).getApplyCount());
                } else {
                    themeApplyByTimeMap.put(vo.getName(), vo);
                }
            }

            List<ThemeApplyStatisticsVo> themeApplyByTime = new ArrayList<>();
            Long total = 0L;
            for (ThemeApplyStatisticsVo vo : themeApplyByTimeMap.values()) {
                total += vo.getApplyCount();
                vo.setCount(vo.getApplyCount());
            }
            for (ThemeApplyStatisticsVo vo : themeApplyByTimeMap.values()) {
                vo.setPrecent(formatPercent(calculateRate(vo.getCount(), total)));
                themeApplyByTime.add(vo);
            }
            result.put("total", total);
            result.put("applyStatistics", themeApplyByTime);
        } else {
            List<ThemeApplyStatisticsVo> themeApplyByTime = aeaAnaThemeDayStatisticsMapper.getThemeApplyByTime(startTime, endTime, SecurityContext.getCurrentOrgId());
            Long total = 0L;
            for (ThemeApplyStatisticsVo vo : themeApplyByTime) {
                total += vo.getApplyCount();
                vo.setCount(vo.getApplyCount());
            }
            for (ThemeApplyStatisticsVo vo : themeApplyByTime) {
                vo.setPrecent(formatPercent(calculateRate(vo.getCount(), total)));
            }
            result.put("total", total);
            result.put("applyStatistics", themeApplyByTime);
        }

        String nowTime = DateUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        if (endTime.compareTo(nowTime) >= 0) {
            //找到全部主题
            AeaParTheme search = new AeaParTheme();
            search.setIsActive("1");
            search.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaParTheme> themes = aeaParThemeMapper.listAeaParTheme(search);
            //主题类型
            List<BscDicCodeItem> themeType = bscDicCodeItemService.getActiveItemsByTypeCode("THEME_TYPE", SecurityContext.getCurrentOrgId());
            Map<String, Long> themeTypeCount = new HashMap<>();
            Map<String, String> themeNameMap = new HashMap<>();
            for (BscDicCodeItem item : themeType) {
                themeTypeCount.put(item.getItemCode(), 0L);
                themeNameMap.put(item.getItemCode(), item.getItemName());
            }

            for (int i = 0, len = themes.size(); i < len; i++) {
                AeaParTheme theme = themes.get(i);
                List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByThemeIds("'" + theme.getThemeId() + "'");
                String queryThemeVerIds = getqueryThemeVerIds(themeVers);
                //对应时间段内申报的所属主题阶段实例数量
                List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryThemeAeaHiParStageinsts(queryThemeVerIds, SecurityContext.getCurrentOrgId(), nowTime, nowTime, null);
                //查询对应时间段内申报的所属主题并行推进事项实例数量
                List<AeaHiSeriesinst> seriesinsts = aeaHiSeriesinstMapper.queryThemeAeaHiSeriesinsts(queryThemeVerIds, SecurityContext.getCurrentOrgId(), nowTime, nowTime, null);
                int num = stageinsts.size() + seriesinsts.size();

                Long count = themeTypeCount.get(theme.getThemeType());
                themeTypeCount.put(theme.getThemeType(), count + num);
            }

            Long total = (Long) result.get("total");
            List<ThemeApplyStatisticsVo> themeApplyByTime = (List<ThemeApplyStatisticsVo>) result.get("applyStatistics");
            Set<String> themeTypeSet = themeTypeCount.keySet();
            for (ThemeApplyStatisticsVo vo : themeApplyByTime) {
                vo.setCount(themeTypeCount.get(vo.getCode()) + vo.getCount());
                vo.setApplyCount(themeTypeCount.get(vo.getCode()) + vo.getApplyCount());
                total += themeTypeCount.get(vo.getCode());
                themeTypeSet.remove(vo.getCode());
            }
            //假如有新的主题类型
            for (String type : themeTypeSet) {
                ThemeApplyStatisticsVo vo = new ThemeApplyStatisticsVo();
                vo.setCode(type);
                vo.setName(themeNameMap.get(type));
                vo.setCount(themeTypeCount.get(type));
                vo.setApplyCount(vo.getCount());
                total += vo.getCount();
                themeApplyByTime.add(vo);
            }
            //计算百分比
            for (ThemeApplyStatisticsVo vo : themeApplyByTime) {
                vo.setPrecent(formatPercent(calculateRate(vo.getCount(), total)));
            }
            result.put("total", total);
        }

        return result;
    }

    /**
     * 阶段申报统计
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getStageApplyStatistics(String startTime, String endTime) throws Exception {
        Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
        Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int startYear = calendar.get(Calendar.YEAR);

        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);

        Map<String, Object> result = new HashMap<>();
        if (endYear - startYear > 1) {
            //1.开始
            calendar.set(Calendar.YEAR, startYear);
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            String startEndTime = DateUtils.convertDateToString(calendar.getTime(), "yyyy-MM-dd");
            List<StageApplyStatisticsVo> stageApplyByTimeStart = aeaAnaThemeDayStatisticsMapper.getStageApplyByTime(startTime, startEndTime, SecurityContext.getCurrentOrgId());

            //2.期间
            int startDuringYear = startYear + 1;
            int endDuringYear = endYear - 1;
            List<StageApplyStatisticsVo> stageApplyByTimeDuring = aeaAnaThemeYearStatisticsMapper.getStageApplyByTime(startDuringYear, endDuringYear, SecurityContext.getCurrentOrgId());

            //3.结束
            calendar.set(Calendar.YEAR, endYear);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String endStartTime = DateUtils.convertDateToString(calendar.getTime(), "yyyy-MM-dd");
            List<StageApplyStatisticsVo> stageApplyByTimeEnd = aeaAnaThemeDayStatisticsMapper.getStageApplyByTime(endStartTime, endTime, SecurityContext.getCurrentOrgId());

            Map<String, StageApplyStatisticsVo> stageApplyByTimeMap = new HashMap<>();

            for (StageApplyStatisticsVo vo : stageApplyByTimeStart) {
                stageApplyByTimeMap.put(vo.getCode(), vo);
            }
            for (StageApplyStatisticsVo vo : stageApplyByTimeDuring) {
                if (stageApplyByTimeMap.size() != 0 && stageApplyByTimeMap.get(vo.getCode()) != null) {
                    Long applyCount = stageApplyByTimeMap.get(vo.getCode()).getApplyCount();
                    stageApplyByTimeMap.get(vo.getCode()).setApplyCount(vo.getApplyCount() + applyCount);
                } else {
                    stageApplyByTimeMap.put(vo.getCode(), vo);
                }
            }
            for (StageApplyStatisticsVo vo : stageApplyByTimeEnd) {
                if (stageApplyByTimeMap.size() != 0 && stageApplyByTimeMap.get(vo.getCode()) != null) {
                    Long applyCount = stageApplyByTimeMap.get(vo.getCode()).getApplyCount();
                    stageApplyByTimeMap.get(vo.getCode()).setApplyCount(vo.getApplyCount() + applyCount);
                } else {
                    stageApplyByTimeMap.put(vo.getCode(), vo);
                }
            }

            List<StageApplyStatisticsVo> stageApplyByTime = new ArrayList<>();
            Long total = 0L;

            Map<String, String> standardStage = StandardStageCode.toMap();
            Map<String, Long> realStageTotalApplyMap = new HashMap<>();
            for (String stageType : standardStage.keySet()) {
                realStageTotalApplyMap.put(stageType, 0L);
            }
            for (StageApplyStatisticsVo vo : stageApplyByTimeMap.values()) {
                if (StringUtils.isNotBlank(vo.getCode())) {
                    String[] stageTypes = vo.getCode().split(",");
                    for (String stageType : stageTypes) {
                        if (StringUtils.isNotBlank(stageType)) {
                            Long count = realStageTotalApplyMap.get(stageType);
                            count += vo.getApplyCount();
                            realStageTotalApplyMap.put(stageType, count);
                            total += vo.getApplyCount();
                        }
                    }
                }
                /*else {
                    throw new Exception("存在无法对应国家标准审批阶段的阶段，请检查阶段定义！");
                }*/
            }
            for (String stageType : realStageTotalApplyMap.keySet()) {
                StageApplyStatisticsVo vo = new StageApplyStatisticsVo();
                vo.setCode(stageType);
                vo.setName(standardStage.get(stageType));
                vo.setApplyCount(realStageTotalApplyMap.get(stageType));
                vo.setCount(realStageTotalApplyMap.get(stageType));
                vo.setPrecent(formatPercent(calculateRate(realStageTotalApplyMap.get(stageType), total)));
                stageApplyByTime.add(vo);
            }
            result.put("total", total);
            result.put("applyStatistics", stageApplyByTime);
        } else {
            List<StageApplyStatisticsVo> stageApplyByTime = aeaAnaThemeDayStatisticsMapper.getStageApplyByTime(startTime, endTime, SecurityContext.getCurrentOrgId());
            List<StageApplyStatisticsVo> realstageTotalApplyByTime = new ArrayList<>();
            Long total = 0L;

            Map<String, String> standardStage = StandardStageCode.toMap();
            Map<String, Long> realStageTotalApplyMap = new HashMap<>();
            for (String stageType : standardStage.keySet()) {
                realStageTotalApplyMap.put(stageType, 0L);
            }
            for (StageApplyStatisticsVo vo : stageApplyByTime) {
                if (StringUtils.isNotBlank(vo.getCode())) {
                    String[] stageTypes = vo.getCode().split(",");
                    for (String stageType : stageTypes) {
                        if (StringUtils.isNotBlank(stageType)) {
                            if (realStageTotalApplyMap.get(stageType) != null) {
                                Long count = realStageTotalApplyMap.get(stageType);
                                count += vo.getApplyCount();
                                realStageTotalApplyMap.put(stageType, count);
                                total += vo.getApplyCount();
                            }
                        }
                    }
                }
                /*else {
                    throw new Exception("存在无法对应国家标准审批阶段的阶段，请检查阶段定义！");
                }*/
            }
            for (String stageType : realStageTotalApplyMap.keySet()) {
                StageApplyStatisticsVo vo = new StageApplyStatisticsVo();
                vo.setCode(stageType);
                vo.setName(standardStage.get(stageType));
                vo.setApplyCount(realStageTotalApplyMap.get(stageType));
                vo.setCount(realStageTotalApplyMap.get(stageType));
                vo.setPrecent(formatPercent(calculateRate(realStageTotalApplyMap.get(stageType), total)));
                realstageTotalApplyByTime.add(vo);
            }
            result.put("total", total);
            result.put("applyStatistics", realstageTotalApplyByTime);
        }


        String nowTime = DateUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        if (endTime.compareTo(nowTime) >= 0) {
            Map<String, Long> nowStageCount = new HashMap<>();
            for (StandardStageCode standardStageCode : StandardStageCode.values()) {
                String value = standardStageCode.getValue();
                String name = standardStageCode.getName();
                //查询对应时间段内申报的对应阶段的阶段实例
                List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryStageAeaHiParStageinsts(value, SecurityContext.getCurrentOrgId(), nowTime, nowTime, null);
                //查询对应时间段内申报的对应阶段的并行推进事项实例
                List<AeaHiSeriesinst> seriesinsts = aeaHiSeriesinstMapper.queryStageAeaHiSeriesinsts(value, SecurityContext.getCurrentOrgId(), nowTime, nowTime, null);
                int num = stageinsts.size() + seriesinsts.size();
                nowStageCount.put(standardStageCode.getValue(), Long.valueOf(num));
            }

            Long total = (Long) result.get("total");
            List<StageApplyStatisticsVo> stageApplyByTime = (List<StageApplyStatisticsVo>) result.get("applyStatistics");
            for (StageApplyStatisticsVo vo : stageApplyByTime) {
                vo.setCount(nowStageCount.get(vo.getCode()) + vo.getApplyCount());
                vo.setApplyCount(vo.getCount());
                total += nowStageCount.get(vo.getCode());
            }
            for (StageApplyStatisticsVo vo : stageApplyByTime) {
                vo.setPrecent(formatPercent(calculateRate(vo.getCount(), total)));
            }
            result.put("total", total);
        }

        return result;
    }

    /**
     * 地区申报统计
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @Override
    public List<RegionApplyStatisticsVo> getRegionApplyStatistics(String startTime, String endTime) throws Exception {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<RegionApplyStatisticsVo> regionApplyByTime = aeaAnaWinDayStatisticsMapper.getRegionApplyByTime(startTime, endTime, rootOrgId);

        return regionApplyByTime;
    }

    /**
     * 月度申报受理统计
     *
     * @param startTime 统计起始时间
     * @param endTime   统计结束时间
     * @return
     * @throws Exception
     */
    @Override
    public List<WinMonthApplyStatisticsVo> getWinApplyStatisticsByMonth(String startTime, String endTime) throws Exception {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<WinMonthApplyStatisticsVo> applyStatisticsByMonth = aeaAnaWinMonthStatisticsMapper.getWinApplyStatisticsByMonth(startTime, endTime, rootOrgId);
        return applyStatisticsByMonth;
    }

    /**
     * 窗口受理申报统计
     *
     * @param startTime 统计起始时间
     * @param endTime   统计结束时间
     * @return
     * @throws Exception
     */
    @Override
    public List<WinApplyStatisticsVo> getWinApplyStatistics(String startTime, String endTime) throws Exception {
        List<WinApplyStatisticsVo> WinApplyStatisticsList = null;
        String rootOrgId = SecurityContext.getCurrentOrgId();

        WinApplyStatisticsList = aeaAnaWinDayStatisticsMapper.getWinApplyStatistics(startTime, endTime, rootOrgId);

        Map<String, List<WinApplyStatisticsVo>> winApplyStatisticsMap = WinApplyStatisticsList.stream().collect(Collectors.groupingBy(WinApplyStatisticsVo::getWindowId));
        //返回结果
        List<WinApplyStatisticsVo> result = new ArrayList<>();

        //存储已统计window, 方便查找新申报的窗口（实时统计)
        Set<String> windowIds = new HashSet<>();
        //如果结束日期参数大于等于今日，需要加上实时统计的数据
        String nowTime = DateUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        String nowTimeStart = nowTime + " 00:00:00";
        String nowTimeEnd = nowTime + " 23:59:59";
        boolean onTime = false;
        if (endTime.compareTo(nowTime) >= 0) {
            onTime = true;
        }

        for (String windowId : winApplyStatisticsMap.keySet()) {
            windowIds.add(windowId);

            WinApplyStatisticsVo winApplyStatisticsVo = new WinApplyStatisticsVo();
            winApplyStatisticsVo.setWindowId(windowId);
            winApplyStatisticsVo.setApplyCount(0L);
            winApplyStatisticsVo.setShouliCount(0L);
            winApplyStatisticsVo.setBuyushouliCount(0L);
            winApplyStatisticsVo.setBanjieCount(0L);
            winApplyStatisticsVo.setYuqiCount(0L);

            //一个窗口查询出两条记录，一条现场申报，一条网上申报
            for (WinApplyStatisticsVo data : winApplyStatisticsMap.get(windowId)) {
                winApplyStatisticsVo.setWindowName(data.getWindowName());

                winApplyStatisticsVo.setApplyCount(data.getApplyCount() + winApplyStatisticsVo.getApplyCount());

                if ("win".equals(data.getApplySource())) {
                    winApplyStatisticsVo.setWinApplyCount(data.getApplyCount());
                } else {
                    winApplyStatisticsVo.setNetApplyCount(data.getApplyCount());
                }

                winApplyStatisticsVo.setShouliCount(data.getShouliCount() + winApplyStatisticsVo.getShouliCount());
                winApplyStatisticsVo.setBuyushouliCount(data.getBuyushouliCount() + winApplyStatisticsVo.getBuyushouliCount());
                winApplyStatisticsVo.setBanjieCount(data.getBanjieCount() + winApplyStatisticsVo.getBanjieCount());
                winApplyStatisticsVo.setYuqiCount(data.getYuqiCount() + winApplyStatisticsVo.getYuqiCount());

            }

            winApplyStatisticsVo.setYuqiRate(calculateRate(winApplyStatisticsVo.getYuqiCount(), winApplyStatisticsVo.getShouliCount()));
            winApplyStatisticsVo.setShouliRate(calculateRate(winApplyStatisticsVo.getShouliCount(), winApplyStatisticsVo.getApplyCount()));
            winApplyStatisticsVo.setYuqiRatio(formatPercent(calculateRate(winApplyStatisticsVo.getYuqiCount(), winApplyStatisticsVo.getApplyCount())));
            winApplyStatisticsVo.setShouliRatio(formatPercent(calculateRate(winApplyStatisticsVo.getShouliCount(), winApplyStatisticsVo.getApplyCount())));

            result.add(winApplyStatisticsVo);
        }

        List<WinApplyStatisticsVo> collect = result.stream().sorted((m1, m2) -> {
            return -m1.getShouliRate().compareTo(m2.getShouliRate());
        }).collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            collect.get(i).setSortNo(i + 1);
        }
        return collect;
    }

    @Override
    public List<ApplyStatisticsVo> getCurWinApplyStatistics() throws Exception {
        List<ApplyStatisticsVo> list = new ArrayList<>();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setEntrust(true);
        conditionalQueryRequest.setHandler(false);
        conditionalQueryRequest.setLoginName(SecurityContext.getCurrentUserName());
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());

        List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
        if (CollectionUtils.isEmpty(windows)) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        String windowId = windows.get(0).getWindowId();
        conditionalQueryRequest.setWindowId(windowId);

        //查询网上待预审
        ApplyStatisticsVo v1 = queryWangshangdaiyushen(conditionalQueryRequest);
        //查询申报待补全
        ApplyStatisticsVo v2 = queryShenbaodaibuquan(conditionalQueryRequest);
        //查询申报已补全
        ApplyStatisticsVo v3 = queryShenbaoyibuquan(conditionalQueryRequest);
        //查询不予受理
        ApplyStatisticsVo v4 = queryBuyushouli(conditionalQueryRequest);
        //查询申报时限预警
        conditionalQueryRequest.setInstState(TimeruleInstState.WARN.getValue());
        ApplyStatisticsVo v5 = queryShenbaoxianshiyujing(conditionalQueryRequest);
        //查询申报时限逾期
        conditionalQueryRequest.setInstState(TimeruleInstState.OVERDUE.getValue());
        ApplyStatisticsVo v6 = queryShenbaoxianshiyuqi(conditionalQueryRequest);
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        list.add(v6);
        return list;
    }

    /**
     * 申报统计
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ApplyStatisticsVo> getApplyStatistics() throws Exception {
        List<ApplyStatisticsVo> list = new ArrayList<>();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(false);
        conditionalQueryRequest.setLoginName(SecurityContext.getCurrentUserName());
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());

        //查询网上待预审
        ApplyStatisticsVo v1 = queryWangshangdaiyushen(conditionalQueryRequest);
        //查询申报待补全
        ApplyStatisticsVo v2 = queryShenbaodaibuquan(conditionalQueryRequest);
        //查询申报已补全
        ApplyStatisticsVo v3 = queryShenbaoyibuquan(conditionalQueryRequest);
        //查询不予受理
        ApplyStatisticsVo v4 = queryBuyushouli(conditionalQueryRequest);
        //查询申报时限预警
        conditionalQueryRequest.setInstState(TimeruleInstState.WARN.getValue());
        ApplyStatisticsVo v5 = queryShenbaoxianshiyujing(conditionalQueryRequest);
        //查询申报时限逾期
        conditionalQueryRequest.setInstState(TimeruleInstState.OVERDUE.getValue());
        ApplyStatisticsVo v6 = queryShenbaoxianshiyuqi(conditionalQueryRequest);
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        list.add(v6);
        return list;
    }

    /**
     * 主题申报异常排名
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ApplyThemeExceptionVo> queryApplyThemeExceptionStatistics() throws Exception {
        //实时的数据
        //不予受理
        List<AeaAnaThemeDayStatistics> outScopeList = aeaAnaThemeDayStatisticsMapper.getOutScopeApplyByThemeStage(null, null, SecurityContext.getCurrentOrgId());
        Map<String, AeaAnaThemeDayStatistics> outScopeCount = new HashMap<>();
        for (AeaAnaThemeDayStatistics dayStatistics : outScopeList) {
            outScopeCount.put(dayStatistics.getThemeId() + dayStatistics.getApplyRecordId(), dayStatistics);
        }
        Set<String> outScopeKey = outScopeCount.keySet();
        //总逾期
        List<AeaAnaThemeDayStatistics> overTimeList = aeaAnaThemeDayStatisticsMapper.getOverTimeApply(SecurityContext.getCurrentOrgId());
        Map<String, AeaAnaThemeDayStatistics> overTimeCount = new HashMap<>();
        for (AeaAnaThemeDayStatistics dayStatistics : overTimeList) {
            overTimeCount.put(dayStatistics.getThemeId() + dayStatistics.getApplyRecordId(), dayStatistics);
        }

        //数据封装
        List<ApplyThemeExceptionVo> exceptionVos = new ArrayList<>();

        for (String key : outScopeKey) {
            ApplyThemeExceptionVo vo = new ApplyThemeExceptionVo();
            vo.setThemeName(outScopeCount.get(key).getThemeName());
            vo.setStageName(outScopeCount.get(key).getApplyRecordName());
            vo.setNotAcceptCount(outScopeCount.get(key).getAllOutScopeCount());
            if (overTimeCount.get(key) != null) {
                vo.setOverdueCount(overTimeCount.get(key).getAllOverTimeCount());
                overTimeCount.remove(key);
            } else {
                vo.setOverdueCount(0L);
            }
            exceptionVos.add(vo);
        }
        for (String key : overTimeCount.keySet()) {
            ApplyThemeExceptionVo vo = new ApplyThemeExceptionVo();
            vo.setThemeName(overTimeCount.get(key).getThemeName());
            vo.setStageName(overTimeCount.get(key).getApplyRecordName());
            vo.setNotAcceptCount(0L);
            vo.setOverdueCount(overTimeCount.get(key).getAllOverTimeCount());
            exceptionVos.add(vo);
        }
        List<ApplyThemeExceptionVo> collect = exceptionVos.stream().sorted((a, b) -> {
            long diff = a.getNotAcceptCount() + a.getOverdueCount() - b.getNotAcceptCount() - b.getOverdueCount();
            if (diff > 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            collect.get(i).setSortNo(i + 1);
        }
        return collect;
    }

    /**
     * 项目并联审批统计
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> staticsticsByApplyType() throws Exception {
        //申报成功的状态
        List<Map<String, Object>> result = new ArrayList<>();
        List<ApplyPorjVo> list = efficiencySupervisionMapper.getApplyProjVoByState(null, SecurityContext.getCurrentOrgId());
        int _single = 0, _parllel = 0, _partParllel = 0;
        if (list.size() > 0) {
            Map<String, List<ApplyPorjVo>> collect = list.stream().collect(Collectors.groupingBy(ApplyPorjVo::getProjInfoId));
            for (String projInfoId : collect.keySet()) {
                int single = 0, parallel = 0;
                for (ApplyPorjVo vo : collect.get(projInfoId)) {
                    if ("0".equals(vo.getIsApprove())) {
                        parallel += 1;
                    } else {
                        single += 1;
                    }
                }
                if (single == 0) {
                    _parllel += 1;
                }
                if (parallel == 0) {
                    _single += 1;
                }
                if (parallel > 0 && single > 0) {
                    _partParllel += 1;
                }
            }
        }
        HashMap<String, Object> parlMap = new HashMap<>();
        parlMap.put("name", "全并联项目");
        parlMap.put("value", _parllel);

        HashMap<String, Object> singleMap = new HashMap<>();
        singleMap.put("name", "全单项审批项目");
        singleMap.put("value", _single);
        HashMap<String, Object> partParlMap = new HashMap<>();
        partParlMap.put("name", "部分并联项目");
        partParlMap.put("value", _partParllel);
        result.add(singleMap);
        result.add(parlMap);
        result.add(partParlMap);

        return result;
    }

    /**
     * 申报来源统计
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> staticsticsByApplySource() throws Exception {
        String[] stateArr = {ApplyState.RECEIVE_APPROVED_APPLY.getValue(), ApplyState.ACCEPT_DEAL.getValue(),
                ApplyState.IN_THE_SUPPLEMENT.getValue(), ApplyState.SUPPLEMENTARY.getValue(),
                ApplyState.COMPLETED.getValue()};
        AeaHiApplyinst search = new AeaHiApplyinst();
        search.setApplyinstSource("win");
        List<AeaHiApplyinst> winList = getApplyinstByStatusAndCondition(null, search);//null查询所有

        search.setApplyinstSource("net");
        List<AeaHiApplyinst> netList = getApplyinstByStatusAndCondition(stateArr, search);
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(new HashMap<String, Object>() {{
            put("name", "网上大厅");
            put("value", netList.size());
        }});
        result.add(new HashMap<String, Object>() {{
            put("name", "现场大厅");
            put("value", winList.size());
        }});
        return result;
    }

    @Override
    public PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());

        String loginName = SecurityContext.getOpusLoginUser().getUser().getLoginName();
        conditionalQueryRequest.setLoginName(loginName);

        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listAllPreliminaryTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待预审");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listDismissedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listDismissedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "不予受理");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listNeedCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "待补全申报");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedConfirmCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listNeedConfirmCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "补全待确认");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listWarnApply(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }
        conditionalQueryRequest.setHandler(false);
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setInstState(TimeruleInstState.WARN.getValue());

        if (StringUtils.isNotBlank(conditionalQueryRequest.getIndustry())) {
            conditionalQueryRequest.setIndustries(conditionalQueryRequest.getIndustry().trim().split(","));
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    @Override
    public PageInfo listOverdueApply(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
            if (CollectionUtils.isEmpty(windows)) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            String windowId = windows.get(0).getWindowId();
            conditionalQueryRequest.setWindowId(windowId);
        }
        conditionalQueryRequest.setHandler(false);
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setInstState(TimeruleInstState.OVERDUE.getValue());

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    /**
     * 接件受理率统计（窗口，日期维度）
     *
     * @param type
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getAcceptDealStatisticsByWin(String type, String startTime, String endTime) throws Exception {
        if ("D".equals(type)) {//昨日日统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDate = DateUtils.getPreDateByDate(new Date());
            String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");
            String preDateStartTime = preDateStr + " 00:00:00";
            String preDateEndTime = preDateStr + " 23:59:59";

            //查询所有的窗口
            AeaServiceWindow windowSearch = new AeaServiceWindow();
            windowSearch.setIsActive("1");
            windowSearch.setRootOrgId(rootOrgId);
            List<AeaServiceWindow> serviceWindows = aeaServiceWindowMapper.listAeaServiceWindow(windowSearch);
            serviceWindows = serviceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());

            List<AeaAnaWinDayStatistics> aeaAnaWinDayStatisticsList = aeaAnaWinDayStatisticsMapper.getWinApplyByTime(preDateStartTime, preDateEndTime, rootOrgId);
            Map<String, List<AeaAnaWinDayStatistics>> collect = aeaAnaWinDayStatisticsList.stream().collect(Collectors.groupingBy(AeaAnaWinDayStatistics::getWindowId));
            //窗口统计
            List<Map<String, Object>> windowDatas = new ArrayList<>();
            for (AeaServiceWindow window : serviceWindows) {
                Long countApply = 0L;
                Long countAcceptDeal = 0L;

                List<AeaAnaWinDayStatistics> winDayData = collect.get(window.getWindowId());
                if (winDayData != null) {
                    for (AeaAnaWinDayStatistics statistics : winDayData) {
                        countApply += statistics.getDayApplyCount();
                        countAcceptDeal += statistics.getDayPreAcceptanceCount();
                    }
                }

                String acceptDealRate = formatPercent(calculateRate(countAcceptDeal, countApply));
                Map<String, Object> windowData = new HashMap<>();
                windowData.put("windowId", window.getWindowId());
                windowData.put("windowName", window.getWindowName());
                windowData.put("apply", countApply);
                windowData.put("acceptDeal", countAcceptDeal);
                windowData.put("acceptDealRate", acceptDealRate);
                windowDatas.add(windowData);
            }

            return windowDatas;
        } else if ("W".equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            //查询所有的窗口
            AeaServiceWindow windowSearch = new AeaServiceWindow();
            windowSearch.setIsActive("1");
            windowSearch.setRootOrgId(rootOrgId);
            List<AeaServiceWindow> serviceWindows = aeaServiceWindowMapper.listAeaServiceWindow(windowSearch);
            serviceWindows = serviceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());

            //窗口统计
            List<AeaAnaWinWeekStatistics> aeaAnaWinWeekStatisticsList = aeaAnaWinWeekStatisticsMapper.getWinApplyByTime(thisYear, thisWeekNum, rootOrgId);
            Map<String, List<AeaAnaWinWeekStatistics>> collect = aeaAnaWinWeekStatisticsList.stream().collect(Collectors.groupingBy(AeaAnaWinWeekStatistics::getWindowId));
            List<Map<String, Object>> windowDatas = new ArrayList<>();
            for (AeaServiceWindow window : serviceWindows) {
                Long countApply = 0L;
                Long countAcceptDeal = 0L;

                List<AeaAnaWinWeekStatistics> winWeekData = collect.get(window.getWindowId());
                if (winWeekData != null) {
                    for (AeaAnaWinWeekStatistics statistics : winWeekData) {
                        countApply += statistics.getWeekApplyCount();
                        countAcceptDeal += statistics.getWeekPreAcceptanceCount();
                    }
                }

                String acceptDealRate = formatPercent(calculateRate(countAcceptDeal, countApply));
                Map<String, Object> windowData = new HashMap<>();
                windowData.put("windowId", window.getWindowId());
                windowData.put("windowName", window.getWindowName());
                windowData.put("apply", countApply);
                windowData.put("acceptDeal", countAcceptDeal);
                windowData.put("acceptDealRate", acceptDealRate);
                windowDatas.add(windowData);
            }

            return windowDatas;
        } else if ("M".equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String thisMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            //查询所有的窗口
            AeaServiceWindow windowSearch = new AeaServiceWindow();
            windowSearch.setIsActive("1");
            windowSearch.setRootOrgId(rootOrgId);
            List<AeaServiceWindow> serviceWindows = aeaServiceWindowMapper.listAeaServiceWindow(windowSearch);
            serviceWindows = serviceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());

            //窗口统计
            List<AeaAnaWinMonthStatistics> aeaAnaWinMonthStatisticsList = aeaAnaWinMonthStatisticsMapper.getWinApplyByTime(thisMonth, rootOrgId);
            Map<String, List<AeaAnaWinMonthStatistics>> collect = aeaAnaWinMonthStatisticsList.stream().collect(Collectors.groupingBy(AeaAnaWinMonthStatistics::getWindowId));
            List<Map<String, Object>> windowDatas = new ArrayList<>();
            for (AeaServiceWindow window : serviceWindows) {
                Long countApply = 0L;
                Long countAcceptDeal = 0L;

                List<AeaAnaWinMonthStatistics> winMonthData = collect.get(window.getWindowId());
                if (winMonthData != null) {
                    for (AeaAnaWinMonthStatistics statistics : winMonthData) {
                        countApply += statistics.getMonthApplyCount();
                        countAcceptDeal += statistics.getMonthPreAcceptanceCount();
                    }
                }

                String acceptDealRate = formatPercent(calculateRate(countAcceptDeal, countApply));
                Map<String, Object> windowData = new HashMap<>();
                windowData.put("windowId", window.getWindowId());
                windowData.put("windowName", window.getWindowName());
                windowData.put("apply", countApply);
                windowData.put("acceptDeal", countAcceptDeal);
                windowData.put("acceptDealRate", acceptDealRate);
                windowDatas.add(windowData);
            }

            return windowDatas;
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            //查询所有的窗口
            AeaServiceWindow windowSearch = new AeaServiceWindow();
            windowSearch.setIsActive("1");
            windowSearch.setRootOrgId(rootOrgId);
            List<AeaServiceWindow> serviceWindows = aeaServiceWindowMapper.listAeaServiceWindow(windowSearch);
            serviceWindows = serviceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());

            //窗口统计
            List<AeaAnaWinDayStatistics> winAcceptStatistics = aeaAnaWinDayStatisticsMapper.getWinApplyByTime(startTime, endTime, rootOrgId);
            Map<String, List<AeaAnaWinDayStatistics>> collect = winAcceptStatistics.stream().collect(Collectors.groupingBy(AeaAnaWinDayStatistics::getWindowId));
            List<Map<String, Object>> windowDatas = new ArrayList<>();
            for (AeaServiceWindow window : serviceWindows) {
                Long countApply = 0L;
                Long countAcceptDeal = 0L;

                List<AeaAnaWinDayStatistics> winDuringData = collect.get(window.getWindowId());
                if (winDuringData != null) {
                    for (AeaAnaWinDayStatistics statistics : winDuringData) {
                        countApply += statistics.getDayApplyCount();
                        countAcceptDeal += statistics.getDayPreAcceptanceCount();
                    }
                }
                String acceptRate = formatPercent(calculateRate(countAcceptDeal, countApply));
                Map<String, Object> windowData = new HashMap<>();
                windowData.put("windowId", window.getWindowId());
                windowData.put("windowName", window.getWindowName());
                windowData.put("apply", countApply);
                windowData.put("acceptDeal", countAcceptDeal);
                windowData.put("acceptDealRate", acceptRate);
                windowDatas.add(windowData);
            }

            return windowDatas;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAcceptStatisticsByDay(String winodwId, String type, String startTime, String endTime) throws Exception {
        if ("D".equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDay = DateUtils.getPreDateByDate(new Date());
            return getDayApplyDataByWin(winodwId, preDay, preDay, rootOrgId);
        } else if ("W".equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date thisWeekMonday = DateUtils.getThisWeekMonday(new Date());
            Date thisWeekSunday = DateUtils.getThisWeekSunday(new Date());
            return getDayApplyDataByWin(winodwId, thisWeekMonday, thisWeekSunday, rootOrgId);
        } else if ("M".equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date firstDay = DateUtils.firstDayOfMonth(new Date());
            Date lastDay = DateUtils.lastDayOfMonth(new Date());
            return getDayApplyDataByWin(winodwId, firstDay, lastDay, rootOrgId);
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
            return getDayApplyDataByWin(winodwId, startDate, endDate, rootOrgId);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getCurWinAcceptStatisticsByDay(String type, String startTime, String endTime) throws Exception {
        List<AeaServiceWindowUser> windows = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
        if (CollectionUtils.isEmpty(windows)) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        String windowId = windows.get(0).getWindowId();
        return getAcceptStatisticsByDay(windowId, type, startTime, endTime);
    }

    @Override
    public Map<String, Object> getApplyStatisticsByTheme(String type, String startTime, String endTime) throws Exception {
        if ("D".equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDate = DateUtils.getPreDateByDate(new Date());
            String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");
            List<ThemeApplyStatisticsVo> applyByTheme = aeaAnaThemeDayStatisticsMapper.getApplyByTheme(preDateStr, preDateStr, rootOrgId);
            return getThemeApplyStatistics(applyByTheme);
        } else if ("W".equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String yearStr = DateUtils.convertDateToString(new Date(), "yyyy");
            int year = Integer.parseInt(yearStr);
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            List<ThemeApplyStatisticsVo> applyByTheme = aeaAnaThemeWeekStatisticsMapper.getApplyByTheme(year, thisWeekNum, rootOrgId);
            return getThemeApplyStatistics(applyByTheme);
        } else if ("M".equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String month = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            List<ThemeApplyStatisticsVo> applyByTheme = aeaAnaThemeMonthStatisticsMapper.getApplyByTheme(month, rootOrgId);
            return getThemeApplyStatistics(applyByTheme);
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<ThemeApplyStatisticsVo> applyByTheme = aeaAnaThemeDayStatisticsMapper.getApplyByTheme(startTime, endTime, rootOrgId);
            return getThemeApplyStatistics(applyByTheme);
        }
        return null;
    }

    private Map<String, Object> getThemeApplyStatistics(List<ThemeApplyStatisticsVo> applyByTheme) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Long total = 0L;
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, List<ThemeApplyStatisticsVo>> collect = applyByTheme.stream().collect(Collectors.groupingBy(ThemeApplyStatisticsVo::getThemeId));

        List<BscDicCodeItem> colorList = bscDicCodeItemService.getActiveItemsByTypeCode("WIN_ECHARTS_COLOR", SecurityContext.getCurrentOrgId());
        int colorIndex = 0;

        for (Map.Entry<String, List<ThemeApplyStatisticsVo>> entry : collect.entrySet()) {
            Long apply = 0L;
            Long acceptDeal = 0L;
            Long overTime = 0L;

            Map<String, Object> data = new HashMap<>();
            for (ThemeApplyStatisticsVo vo : entry.getValue()) {
                apply += vo.getApply();
                acceptDeal += vo.getAcceptDeal();
                overTime += vo.getOvertime();
                if ("win".equals(vo.getApplyinstSource())) {
                    data.put("winApply", vo.getApply());
                } else if ("net".equals(vo.getApplyinstSource())) {
                    data.put("netApply", vo.getApply());
                }
                data.put("themeName", vo.getThemeName());
            }

            total += apply;
            data.put("apply", apply);
            data.put("acceptDeal", acceptDeal);
            data.put("acceptDealRate", calculateRate(acceptDeal, apply));
            data.put("overTime", overTime);
            data.put("overTimeRate", calculateRate(overTime, acceptDeal));
            if (data.get("winApply") == null) {
                data.put("winApply", 0L);
            }
            if (data.get("netApply") == null) {
                data.put("netApply", 0L);
            }
            if (colorIndex < colorList.size()) {
                data.put("color", colorList.get(colorIndex++).getItemCode());
            }
            dataList.add(data);
        }

        List<Map<String, Object>> sorted = dataList.stream().sorted(Comparator.comparing(WinEfficiencySupervisionServiceImpl::comparingByApply).reversed()).collect(Collectors.toList());
        result.put("total", total);
        result.put("theme", sorted);
        return result;
    }

    private static Long comparingByApply(Map<String, Object> map) {
        return (Long) map.get("apply");
    }

    private List<Map<String, Object>> getDayApplyDataByWin(String windowId, Date startDate, Date endDate, String rootOrgId) throws Exception {
        String startTime = DateUtils.convertDateToString(startDate, "yyyy-MM-dd") + " 00:00:00";
        String endTime = DateUtils.convertDateToString(endDate, "yyyy-MM-dd") + " 23:59:59";

        long millisecond = endDate.getTime() - startDate.getTime();
        int day = (int) (millisecond / DateUtils.MILLIS_PER_DAY);

        //日期统计
        List<WinApplyStatisticsVo> winAcceptStatisticsByDay = aeaAnaWinDayStatisticsMapper.getWinAcceptStatisticsByDay(windowId, startTime, endTime, rootOrgId);
        if (winAcceptStatisticsByDay == null) {
            winAcceptStatisticsByDay = new ArrayList<>();
        }
        Map<String, List<WinApplyStatisticsVo>> collect = winAcceptStatisticsByDay.stream().collect(Collectors.groupingBy(WinApplyStatisticsVo::getDay));
        for (int i = 0; i <= day; i++) {
            Date date = DateUtils.addDays(startDate, i);
            String dateStr = DateUtils.convertDateToString(date, "yyyy-MM-dd");
            if (collect.get(dateStr) == null) {
                WinApplyStatisticsVo vo = new WinApplyStatisticsVo();
                vo.setApplyCount(0L);
                vo.setShouliCount(0L);
                vo.setDay(dateStr);
                winAcceptStatisticsByDay.add(vo);

            }
        }
        List<Map<String, Object>> dayDatas = winAcceptStatisticsByDay.stream().sorted(Comparator.comparing(WinApplyStatisticsVo::getDay)).map(vo -> {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", vo.getDay());
            dayData.put("apply", vo.getApplyCount());
            dayData.put("acceptDeal", vo.getShouliCount());
            return dayData;
        }).collect(Collectors.toList());
        return dayDatas;
    }


    /**
     * 统计网上待预审
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryWangshangdaiyushen(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("网上待预审");
        /*AeaHiApplyinst search = new AeaHiApplyinst();
        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        search.setApplyinstState(ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue());
        search.setApplyinstSource("net");
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);*/
        List<TaskInfo> taskInfos = conditionalQueryMapper.listAllPreliminaryTasks(conditionalQueryRequest);
        vo.setCount(taskInfos.size());
        return vo;
    }

    /**
     * 统计申报待补全
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryShenbaodaibuquan(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("申报待补全");
        /*AeaHiApplyinst search = new AeaHiApplyinst();
        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        search.setApplyinstState(ApplyState.IN_THE_SUPPLEMENT.getValue());
        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);*/
        List<TaskInfo> taskInfos = conditionalQueryMapper.listNeedCompletedApply(conditionalQueryRequest);
        vo.setCount(taskInfos.size());
        return vo;
    }

    /**
     * 统计申报已补全
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryShenbaoyibuquan(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("申报已补全");
        /*AeaHiApplyinst search = new AeaHiApplyinst();
        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        search.setApplyinstState(ApplyState.SUPPLEMENTARY.getValue());
        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);*/
        List<TaskInfo> taskInfos = conditionalQueryMapper.listNeedConfirmCompletedApply(conditionalQueryRequest);
        vo.setCount(taskInfos.size());
        return vo;
    }

    /**
     * 统计不予受理
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryBuyushouli(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("不予受理");
        /*AeaHiApplyinst search = new AeaHiApplyinst();
        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        search.setApplyinstState(ApplyState.OUT_SCOPE.getValue());
        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);*/
        List<TaskInfo> taskInfos = conditionalQueryMapper.listDismissedApply(conditionalQueryRequest);
        vo.setCount(taskInfos.size());
        return vo;
    }

    /**
     * 统计申报时限预警
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryShenbaoxianshiyujing(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("申报时限预警");
        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);
        vo.setCount(applyInfoList.size());
        return vo;
    }

    /**
     * 统计申报时限逾期
     *
     * @return
     * @throws Exception
     */
    private ApplyStatisticsVo queryShenbaoxianshiyuqi(ConditionalQueryRequest conditionalQueryRequest) throws Exception {
        ApplyStatisticsVo vo = new ApplyStatisticsVo();
        vo.setName("申报时限逾期");
        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);
        vo.setCount(applyInfoList.size());
        return vo;
    }

    private String getqueryThemeVerIds(List<AeaParThemeVer> themeVers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = themeVers.size(); i < len; i++) {
            String verId = themeVers.get(i).getThemeVerId();
            sb.append(",").append("'").append(verId).append("'");
        }
        String s = sb.toString().substring(1);
        return s;
    }

    /**
     * 根据申请实例集合返回逾期申报数量
     *
     * @param list
     * @return
     * @throws Exception
     */
    public int getCount(List<AeaHiApplyinst> list) throws Exception {
        int count = 0;
        for (int i = 0, len = list.size(); i < len; i++) {
            AeaHiApplyinst applyinst = list.get(i);
            boolean boo = checkApplyTimeState(applyinst, "3");
            if (boo) {
                count++;
            }
        }
        return count;
    }

    /**
     * 查询申报时限状态
     *
     * @param applyinst 申请实例
     * @param state     查询的状态值 1表示正常，2表示预警，3表示逾期
     * @return
     */
    private boolean checkApplyTimeState(AeaHiApplyinst applyinst, String state) throws Exception {
        String applyinstId = applyinst.getApplyinstId();
        boolean boo = false;
        ActStoTimeruleInst timeruleInst = null;
        if ("0".equals(applyinst.getIsSeriesApprove())) {
            //并联申报
            AeaHiParStageinst parStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            if (parStageinst != null) {
                timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(parStageinst.getStageinstId());
            }
        } else {
            //单项申报
            AeaHiIteminst iteminst = aeaHiIteminstMapper.queryAeaHiIteminstBySeriesApplyinstId(applyinstId);
            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId());
        }
        if (timeruleInst != null) {
            boo = state.equals(timeruleInst.getInstState());
        }
        return boo;
    }


    /**
     * 查询某种状态下申请实例
     *
     * @param stateArr
     * @return
     */
    private List<AeaHiApplyinst> getApplyinstByStatusAndCondition(String[] stateArr, AeaHiApplyinst search) throws Exception {
        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiApplyinst> applyinsts = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
        if (stateArr == null) {
            return applyinsts;
        }
        String join = StringUtils.join(stateArr, ",");
        if (applyinsts.size() > 0) {
            List<AeaHiApplyinst> collect = applyinsts.stream().filter(obj -> join.indexOf(obj.getApplyinstState()) >= 0).collect(Collectors.toList());
            return collect;
        }
        return new ArrayList<AeaHiApplyinst>();
    }

    /**
     * 获取本月，上个月，去年同月份的起始时间
     *
     * @return
     */
    private Map<String, String> getPreDateMap() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
        int preYear = year - 1;
        int preMonth = month - 1;

        String nowMonthStart = year + "-" + String.format("%02d", month) + "-01 00:00:00";
        String nowMonthEnd = year + "-" + String.format("%02d", month) + "-" + lastDay + " 23:59:59";
        String preMonthStart = year + "-" + String.format("%02d", preMonth) + "-01 00:00:00";
        String preMonthEnd = year + "-" + String.format("%02d", preMonth) + "-" + lastDay + " 23:59:59";
        String preYearStart = preYear + "-" + String.format("%02d", month) + "-01 00:00:00";
        String preYearEnd = preYear + "-" + String.format("%02d", month) + "-" + lastDay + " 23:59:59";

        Map<String, String> result = new HashMap<>();
        result.put("nowMonthStart", nowMonthStart);
        result.put("nowMonthEnd", nowMonthEnd);
        result.put("preMonthStart", preMonthStart);
        result.put("preMonthEnd", preMonthEnd);
        result.put("preYearStart", preYearStart);
        result.put("preYearEnd", preYearEnd);
        return result;
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
            BigDecimal divide = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP);
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
            BigDecimal divide = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();
        }
    }

    /**
     * 将double2位小数转换成百分比
     *
     * @param value
     * @return
     */
    private String formatPercent(double value) {
        BigDecimal bd = new BigDecimal(value * 100);
        double doubleValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return doubleValue + "%";
    }

    private Page changeOrderBySql(Page page) {
        if (page != null && StringUtils.isNotBlank(page.getOrderBy())) {
            String[] sqls = page.getOrderBy().split(" ");
            if (sqls != null && sqls.length == 2) {
                String fields = sqls[0];
                String orderBy = sqls[1];

                if (fields.contains(",")) {
                    String replaceSql = " " + orderBy + ",";
                    page.setOrderBy(page.getOrderBy().replaceAll(",", replaceSql));
                }
            }
        }

        return page;
    }

    private void loadTaskInfo(List<TaskInfo> taskList, String viewName) {
        if (taskList != null && taskList.size() > 0) {

            String viewId = queryViewId(viewName);

            if (StringUtils.isBlank(viewId)) {
                viewId = queryViewId("所有办件");
            }

            for (TaskInfo taskInfo : taskList) {

                taskInfo.setViewId(viewId);

                if (ApplySource.NET.getValue().equals(taskInfo.getApplySource())) {
                    taskInfo.setApplySource("网厅");
                } else if (ApplySource.WIN.getValue().equals(taskInfo.getApplySource())) {
                    taskInfo.setApplySource("窗口");
                }

                taskInfo.setRemainingOrOverTimeText(loadRemainingOrOverTimeText(taskInfo.getInstState(), taskInfo.getTimeruleUnit(), taskInfo.getRemainingTime(), taskInfo.getOverdueTime()));

                taskInfo.setDueNumText("-天");
                taskInfo.setUseLimitTimeText("-天");

                taskInfo.setDueNumText(loadDueNumText(taskInfo.getDueNumText(), taskInfo.getTimeruleUnit(), taskInfo.getDueNum()));

                if (taskInfo.getUseLimitTime() != null) {
                    loadUseLimitTimeText(taskInfo);
                }
            }

        }
    }

    private String queryViewId(String keyword) {
        List<String> viewIdList = conditionalQueryMapper.queryViewId(keyword);

        if (viewIdList != null && viewIdList.size() > 0) {
            return viewIdList.get(0);
        }

        return null;
    }

    /**
     * 计算要显示的剩余/逾期时间
     *
     * @param instState
     * @param timeruleUnit
     * @param remainingTime
     * @param overdueTime
     * @return
     */
    private String loadRemainingOrOverTimeText(String instState, String timeruleUnit, Double remainingTime, Double overdueTime) {
        Double time = remainingTime;
        if (TimeruleInstState.OVERDUE.getValue().equals(instState)) {
            time = overdueTime;
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return (TimeruleInstState.OVERDUE.getValue().equals(instState) ? "逾期" : "剩余") + sb.toString();
        }

        return null;
    }

    private String loadDueNumText(String dueNumText, String timeruleUnit, Double time) {
        if (StringUtils.isBlank(timeruleUnit)) {
            timeruleUnit = TimeruleUnit.WD.getValue();
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return sb.toString();
        }

        return dueNumText;

    }

    private void loadUseLimitTimeText(TaskInfo taskInfo) {
        if (StringUtils.isBlank(taskInfo.getTimeruleUnit())) {
            return;
        }

        String timeruleUnit = taskInfo.getTimeruleUnit();

        Double time = taskInfo.getUseLimitTime();
        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            taskInfo.setUseLimitTimeText(sb.toString());
        }
    }

    private void loadApplyinfo(List<ApplyInfo> applyInfoList) {

        if (applyInfoList != null && applyInfoList.size() > 0) {

            //申报状态
            List<BscDicCodeItem> applyStateList = getBscDicCodeItemList(ApplyState.values());

            for (ApplyInfo applyInfo : applyInfoList) {

                if (ApplySource.NET.getValue().equals(applyInfo.getApplySource())) {
                    applyInfo.setApplySource("网厅");
                } else if (ApplySource.WIN.getValue().equals(applyInfo.getApplySource())) {
                    applyInfo.setApplySource("窗口");
                }

                for (BscDicCodeItem bscDicCodeItem : applyStateList) {
                    if (bscDicCodeItem.getItemCode().equals(applyInfo.getApplyState())) {
                        applyInfo.setApplyState(bscDicCodeItem.getItemName());
                        break;
                    }
                }

                applyInfo.setRemainingOrOverTimeText(loadRemainingOrOverTimeText(applyInfo.getInstState(), applyInfo.getTimeruleUnit(), applyInfo.getRemainingTime(), applyInfo.getOverdueTime()));

                applyInfo.setDueNumText("-天");

                applyInfo.setDueNumText(loadDueNumText(applyInfo.getDueNumText(), applyInfo.getTimeruleUnit(), applyInfo.getDueNum()));

            }
        }
    }

    private List<BscDicCodeItem> getBscDicCodeItemList(BaseEnum[] baseEnums) {
        List<BscDicCodeItem> bscDicCodeItemList = new ArrayList<>();
        for (BaseEnum baseEnum : baseEnums) {
            BscDicCodeItem instState = new BscDicCodeItem();
            instState.setItemName(baseEnum.getName());
            instState.setItemCode(baseEnum.getValue().toString());
            instState.setItemId(baseEnum.getValue().toString());
            bscDicCodeItemList.add(instState);
        }
        return bscDicCodeItemList;
    }

    @Override
    public Map<String, Object> getWinAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception {
        List<WinApplyStatisticsVo> result = new ArrayList<>();
        //查询所有窗口
        AeaServiceWindow winsearch = new AeaServiceWindow();
        winsearch.setRootOrgId(SecurityContext.getCurrentOrgId());
        winsearch.setIsActive("1");
        List<AeaServiceWindow> aeaServiceWindows = aeaServiceWindowMapper.listAeaServiceWindow(winsearch);
        if (aeaServiceWindows.size() == 0) throw new Exception("服务窗口为空！");
        //获取个窗口当天正式情况,aea_log_apply_state_hist-统计
       /* for(AeaServiceWindow window :aeaServiceWindows){
            WinApplyStatisticsVo todayData = getTodayStatistics(window);
            result.add(todayData);
        }
        List<WinApplyStatisticsVo> _tmp = new ArrayList<>();*/
        //统计最终结果
        if ("D".equals(type)) { //统计昨日的
            result = getYesterdayWinStatic(isCurrent);
        } else if ("W".equals(type)) {
            result = getWeekWinStatic(isCurrent);
        } else if ("M".equals(type)) {
            result = getMonthWinStatic(isCurrent);
        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入日期参数有问题。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
            result = getDateWinStatic(startTime, endTime, isCurrent);


        }


        //如果记录为空是，默认给他数据0
        if (result.size() == 0) {
            aeaServiceWindows = aeaServiceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());
            for (AeaServiceWindow window : aeaServiceWindows) {
                WinApplyStatisticsVo vo = new WinApplyStatisticsVo();
                vo.setWindowName(window.getWindowName());
                vo.setShouliCount(0l);
                vo.setCaiLiaoBuquanCount(0l);
                vo.setBuyushouliCount(0l);
                result.add(vo);
            }
        }
        Map<String, Object> resultObj = new HashMap<>();
        if (result.size() > 0) {
            List<String> title = result.stream().map(obj -> obj.getWindowName()).collect(Collectors.toList());
            List<String> yiShouLi = new ArrayList<>();
            List<String> caiLiaoBuQuan = new ArrayList<>();
            List<String> buYuShouLi = new ArrayList<>();
            List<String> color = new ArrayList<>();
            for (WinApplyStatisticsVo vo : result) {
                yiShouLi.add(vo.getShouliCount().toString());
                caiLiaoBuQuan.add(vo.getCaiLiaoBuquanCount().toString());
                buYuShouLi.add(vo.getBuyushouliCount().toString());
            }

            resultObj.put("title", title);
            resultObj.put("yiShouLi", yiShouLi);
            resultObj.put("caiLiaoBuQuan", caiLiaoBuQuan);
            resultObj.put("buYuShouLi", buYuShouLi);
        }


        return resultObj;
    }

    /**
     * 获取所有窗口，并设置指定显示颜色
     *
     * @return
     * @throws Exception
     */
    private List<AeaServiceWindow> getAllServiceWindow() throws Exception {
        AeaServiceWindow winsearch = new AeaServiceWindow();
        winsearch.setRootOrgId(SecurityContext.getCurrentOrgId());
        winsearch.setIsActive("1");
        List<AeaServiceWindow> aeaServiceWindows = aeaServiceWindowMapper.listAeaServiceWindow(winsearch);
        int windowCount = aeaServiceWindows.size();
        if (windowCount == 0) {
            throw new Exception("服务窗口，请检查！");
        }
        //先对窗口sortno排序
        aeaServiceWindows = aeaServiceWindows.stream().sorted(Comparator.comparing(AeaServiceWindow::getSortNo)).collect(Collectors.toList());

        //判断每个窗口是不是配置了有颜色不然就要去字典表里去找并选其中一个并插进去一个
        List<String> hadColor = new ArrayList<>();
        for (int i = 0, len = windowCount; i < len; i++) {
            AeaServiceWindow window = aeaServiceWindows.get(i);
            if (StringUtils.isNotBlank(window.getEchartsColor())) {
                hadColor.add(window.getEchartsColor());
            }
        }
        List<BscDicCodeItem> colorList = bscDicCodeItemService.getActiveItemsByTypeCode("WIN_ECHARTS_COLOR", SecurityContext.getCurrentOrgId());
        if (colorList.size() == 0 || aeaServiceWindows.size() > colorList.size())
            throw new Exception("颜色字典查询为空异常，请检查！");
        if (hadColor.size() > 0 && hadColor.size() < aeaServiceWindows.size()) {
            colorList = colorList.stream().filter(obj -> !hadColor.contains(obj.getItemCode())).collect(Collectors.toList());
        }

        int tmp = 0;
        for (int i = 0, len = windowCount; i < len; i++) {
            AeaServiceWindow window = aeaServiceWindows.get(i);
            if (StringUtils.isBlank(window.getEchartsColor())) {
                window.setEchartsColor(colorList.get(tmp++).getItemCode());
                updateServiceWindowColor(window);
            }

        }

        return aeaServiceWindows;
    }

    private void updateServiceWindowColor(AeaServiceWindow window) {
        AeaServiceWindow newWin = new AeaServiceWindow();
        newWin.setWindowId(window.getWindowId());
        newWin.setEchartsColor(window.getEchartsColor());
        newWin.setModifier(SecurityContext.getCurrentUserId());
        newWin.setModifyTime(new Date());
        aeaServiceWindowMapper.updateAeaServiceWindow(newWin);
    }


    /**
     * 从统计表计算昨日的统计数
     *
     * @param isCurrent
     * @return
     */
    private List<WinApplyStatisticsVo> getYesterdayWinStatic(boolean isCurrent) throws Exception {
        String windowId = getcurrentWindowId(isCurrent);
        String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
        String startTime = yesterday + " 00:00:00";
        String endTime = yesterday + " 23:59:59";

        List<WinApplyStatisticsVo> winShenliStatistics = aeaAnaWinDayStatisticsMapper.getWinApplyStatistics2(startTime, endTime, SecurityContext.getCurrentOrgId(), windowId);
        for (int i = 0, len = winShenliStatistics.size(); i < len; i++) {
            WinApplyStatisticsVo vo = winShenliStatistics.get(i);
            List<AeaLogApplyStateHist> hists = aeaLogApplyStateHistMapper.getWindowApplyHistoryByState(vo.getWindowId(), "3", startTime, endTime, SecurityContext.getCurrentOrgId());
            vo.setCaiLiaoBuquanCount((long) hists.size());
        }
        return winShenliStatistics;
    }

    /**
     * 是否查询当前窗口
     *
     * @param isCurrent
     * @return
     */
    private String getcurrentWindowId(boolean isCurrent) throws Exception {
        String windowId = null;
        if (isCurrent) {
            List<AeaServiceWindow> windows = aeaServiceWindowMapper.findAeaServiceWindowByUserId(SecurityContext.getCurrentUserId());
            if (windows.size() != 1) {
                throw new Exception("该用户没有配置窗口或所属窗口不唯一！");
            }
            windowId = windows.get(0).getWindowId();
        }
        return windowId;
    }

    /**
     * 计算实时数据
     *
     * @param result
     * @param tmp
     */
    private void composeResultStatistics(List<WinApplyStatisticsVo> result, List<WinApplyStatisticsVo> tmp) {
        for (int i = 0, len = tmp.size(); i < len; i++) {
            WinApplyStatisticsVo tVo = tmp.get(i);
            for (int j = 0, len2 = result.size(); j < len2; j++) {
                WinApplyStatisticsVo rVo = result.get(j);

                if (rVo.getWindowId().equals(tVo.getWindowId())) {
                    rVo.setShouliCount(rVo.getShouliCount() + tVo.getShouliCount());
                    rVo.setCaiLiaoBuquanCount(rVo.getCaiLiaoBuquanCount() + tVo.getCaiLiaoBuquanCount());
                    rVo.setBuyushouliCount(rVo.getBuyushouliCount() + tVo.getBuyushouliCount());
                    break;
                }
            }
        }
    }

    /**
     * 获取时间段内的统计
     *
     * @param startTime
     * @param endTime
     * @param isCurrent 是否当前窗口
     * @return
     * @throws Exception
     */
    private List<WinApplyStatisticsVo> getDateWinStatic(String startTime, String endTime, boolean isCurrent) throws Exception {
        startTime += " 00:00:00";
        endTime += " 23:59:59";
        String windowId = getcurrentWindowId(isCurrent);
        List<WinApplyStatisticsVo> winShenliStatistics = aeaAnaWinDayStatisticsMapper.getWinApplyStatistics2(startTime, endTime, SecurityContext.getCurrentOrgId(), windowId);
        for (int i = 0, len = winShenliStatistics.size(); i < len; i++) {
            WinApplyStatisticsVo vo = winShenliStatistics.get(i);
            List<AeaLogApplyStateHist> hists = aeaLogApplyStateHistMapper.getWindowApplyHistoryByState(vo.getWindowId(), "3", startTime, endTime, SecurityContext.getCurrentOrgId());
            vo.setCaiLiaoBuquanCount((long) hists.size());
        }
        return winShenliStatistics;
    }

    /**
     * 获取窗口-本月统计
     *
     * @param isCurrent
     * @return
     * @throws Exception
     */
    private List<WinApplyStatisticsVo> getMonthWinStatic(boolean isCurrent) throws Exception {
        String thisMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
        String windowId = getcurrentWindowId(isCurrent);

        List<WinApplyStatisticsVo> winShouliStatisticsByMonth = aeaAnaWinMonthStatisticsMapper.getWinShouliStatisticsByMonth2(thisMonth, thisMonth, SecurityContext.getCurrentOrgId(), windowId);
        String startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
//        String endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
        String yestoday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");//只统计到昨天的
        String endTime = yestoday + " 23:59:59";
        for (int i = 0, len = winShouliStatisticsByMonth.size(); i < len; i++) {
            WinApplyStatisticsVo vo = winShouliStatisticsByMonth.get(i);
            List<AeaLogApplyStateHist> hists = aeaLogApplyStateHistMapper.getWindowApplyHistoryByState(vo.getWindowId(), "3", startTime, endTime, SecurityContext.getCurrentOrgId());
            vo.setCaiLiaoBuquanCount((long) hists.size());
        }

        return winShouliStatisticsByMonth;
    }

    /**
     * 获取窗口-本周的统计
     *
     * @param isCurrent
     * @return
     * @throws Exception
     */
    private List<WinApplyStatisticsVo> getWeekWinStatic(boolean isCurrent) throws Exception {
        int thisWeekNum = DateUtils.getThisWeekNum(new Date());

        String windowId = getcurrentWindowId(isCurrent);

        List<WinApplyStatisticsVo> result = aeaAnaWinWeekStatisticsMapper.getWinShouliStatisticsByWeek(thisWeekNum, thisWeekNum, SecurityContext.getCurrentOrgId(), windowId);
        String startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
//        String endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd")+ " 23:59:59";
        String yestoday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");//只统计到昨天的
        String endTime = yestoday + " 23:59:59";


        for (int i = 0, len = result.size(); i < len; i++) {
            WinApplyStatisticsVo vo = result.get(i);
            List<AeaLogApplyStateHist> hists = aeaLogApplyStateHistMapper.getWindowApplyHistoryByState(vo.getWindowId(), "3", startTime, endTime, SecurityContext.getCurrentOrgId());
            vo.setCaiLiaoBuquanCount((long) hists.size());
        }
        return result;

    }

    /**
     * 统计当天接件情况
     *
     * @param window
     * @return
     */
    private WinApplyStatisticsVo getTodayStatistics(AeaServiceWindow window) {
        long yiShouLi = 0, caiLiaoBuQuan = 0, buYuShouLi = 0;
        String dayStr = DateUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        String startTime = dayStr + " 00:00:00";
        String endTime = dayStr + " 23:59:59";
        List<AeaLogApplyStateHist> applyStateHists = aeaLogApplyStateHistMapper.getWindowApplyHistory(window.getWindowId(), window.getRootOrgId(), startTime, endTime);
        if (applyStateHists.size() > 0) {
            Map<String, List<AeaLogApplyStateHist>> collect = applyStateHists.stream().collect(Collectors.groupingBy(AeaLogApplyStateHist::getNewState));
            if (collect.containsKey("1")) {
                yiShouLi += collect.get("1").size();
            }
            if (collect.containsKey("2")) {
                yiShouLi += collect.get("2").size();
            }

            if (collect.containsKey("3")) {
                caiLiaoBuQuan += collect.get("3").size();
            }
            if (collect.containsKey("5")) {
                buYuShouLi += collect.get("5").size();
            }

        }

        WinApplyStatisticsVo result = new WinApplyStatisticsVo();
        result.setWindowId(window.getWindowId());
        result.setWindowName(window.getWindowName());
        result.setShouliCount(yiShouLi);
        result.setCaiLiaoBuquanCount(caiLiaoBuQuan);
        result.setBuyushouliCount(buYuShouLi);
        return result;
    }

    @Override
    public Map<String, Object> getWinAcceptRate(String startTime, String endTime, String type) throws Exception {
        List<AeaServiceWindow> aeaServiceWindows = getAllServiceWindow();


        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday + " 00:00:00";
            endTime = yesterday + " 23:59:59";
        } else if ("W".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
            endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd") + " 23:59:59";

        } else if ("M".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
        }

        long total = 0;
        List<WinApplyStatisticsVo> vos = new ArrayList<>();
        for (int i = 0, len = aeaServiceWindows.size(); i < len; i++) {
            WinApplyStatisticsVo vo = getTotalCountByTime(startTime, endTime, aeaServiceWindows.get(i));
            Long applyCount = vo.getApplyCount();
            total += applyCount;
            //过滤掉接件为0的窗口
            /*if(applyCount != null && applyCount > 0){
                vos.add(vo);
            }*/
            vos.add(vo);
        }

        for (int j = 0, len = vos.size(); j < len; j++) {
            Double rate = AnalyseUtils.calculateRate(vos.get(j).getApplyCount(), total);
            vos.get(j).setWinJieJianRate(rate);
        }


        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("data", vos);
        return result;
    }

    /**
     * 计算时间段内接件总量统计
     *
     * @param startTime
     * @param endTime
     * @param aeaServiceWindow
     * @return
     */
    private WinApplyStatisticsVo getTotalCountByTime(String startTime, String endTime, AeaServiceWindow aeaServiceWindow) {
        WinApplyStatisticsVo result = new WinApplyStatisticsVo();
        long count = aeaAnaWinDayStatisticsMapper.getApplyCount(startTime, endTime, aeaServiceWindow.getWindowId(), SecurityContext.getCurrentOrgId());
        result.setWindowName(aeaServiceWindow.getWindowName());
        result.setWindowId(aeaServiceWindow.getWindowId());
        result.setApplyCount(count);
        result.setEchartsColor(aeaServiceWindow.getEchartsColor());
        return result;
    }


    /**
     * 根据时间段查询主题分布情况
     *
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> getThemeDistributionStatistics(String startTime, String endTime, String type) throws Exception {
        //根据时间类似判断开始和结束时间
        List<ThemeDayApplyRecord> list = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday;
            endTime = yesterday;
            list = aeaAnaWinDayStatisticsMapper.getThemeStageStatistics(startTime, endTime, rootOrgId);

        } else if ("W".equals(type)) {
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            list = aeaAnaWinWeekStatisticsMapper.getThemeStageStatistics(thisYear, thisWeekNum, thisWeekNum, rootOrgId);

        } else if ("M".equals(type)) {
            String yearMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            list = aeaAnaWinMonthStatisticsMapper.getThemeStageStatistics(yearMonth, yearMonth, rootOrgId);

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday;
            }
            list = aeaAnaWinDayStatisticsMapper.getThemeStageStatistics(startTime, endTime, rootOrgId);
        }
        //直接主题日统计表获取数据
        List<String> dybzspjdxhList = getGjbzjdInfo("code");
        List<String> stageNameList = new ArrayList<>();
        List<List<String>> themeResult = new ArrayList<>();
//        List<ThemeDayApplyRecord> list = aeaAnaWinDayStatisticsMapper.getThemeStageStatistics(startTime, endTime, SecurityContext.getCurrentOrgId());
        if (list.size() > 0) {

            stageNameList.add("produce");
            stageNameList.addAll(getGjbzjdInfo("name"));
            themeResult.add(stageNameList);
            //安主题分组
            Map<String, List<ThemeDayApplyRecord>> themeCollect = list.stream().collect(Collectors.groupingBy(ThemeDayApplyRecord::getThemeId));
            Set<Map.Entry<String, List<ThemeDayApplyRecord>>> entries = themeCollect.entrySet();
            for (Map.Entry<String, List<ThemeDayApplyRecord>> entry : entries) {
                List<String> themeData = queryThemeStatistics(entry, dybzspjdxhList);
                themeResult.add(themeData);
            }


        }
        Map<String, Object> result = new HashMap<>();
        result.put("stageLength", dybzspjdxhList.size());
        result.put("data", themeResult);
        return result;
    }


    /**
     * 统计一个主题下的个阶段情况
     *
     * @param entry
     * @param dybzspjdxhList
     * @return
     */
    private List<String> queryThemeStatistics(Map.Entry<String, List<ThemeDayApplyRecord>> entry, List<String> dybzspjdxhList) {

        List<String> dataList = new ArrayList<>();
        List<ThemeDayApplyRecord> list = entry.getValue();
        String themeName = list.get(0).getThemeName();
        dataList.add(themeName);
        long isParallelCount = 0;
        for (int i = 0, len = dybzspjdxhList.size(); i < len; i++) {
            long count = 0;
            for (int j = 0, len2 = list.size(); j < len2; j++) {
                if (list.get(j).getDybzspjdxh().indexOf(dybzspjdxhList.get(i)) != -1 && "0".equals(list.get(j).getIsParallel())) {
                    count += list.get(j).getApplyCount();
                }
                if (list.get(j).getDybzspjdxh().indexOf(dybzspjdxhList.get(i)) != -1 && "1".equals(list.get(j).getIsParallel())) {
                    isParallelCount += list.get(j).getApplyCount();
                }
            }
            dataList.add(String.valueOf(count));
        }
        //将并行推进的事项的申报页加到dybzspjdxh=5的中去
        long _tmp = Long.valueOf(dataList.get(dybzspjdxhList.size() - 1)) + isParallelCount;
        dataList.remove(dybzspjdxhList.size() - 1);
        dataList.add(String.valueOf(_tmp));
        return dataList;

    }


    @Override
    public Map<String, Object> getStageApplyStatisticsByType(String startTime, String endTime, String type) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<ThemeDayApplyRecord> list = new ArrayList<>();
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday;
            endTime = yesterday;
            list = aeaAnaWinDayStatisticsMapper.getStageGroupStatistics(startTime, endTime, rootOrgId);
        } else if ("W".equals(type)) {
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            list = aeaAnaWinWeekStatisticsMapper.getStageGroupStatistics(thisYear, thisWeekNum, thisWeekNum, rootOrgId);
        } else if ("M".equals(type)) {
            String yearMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            list = aeaAnaWinMonthStatisticsMapper.getStageGroupStatistics(yearMonth, yearMonth, rootOrgId);
        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday;
            }
            list = aeaAnaWinDayStatisticsMapper.getStageGroupStatistics(startTime, endTime, rootOrgId);
        }


        //直接主题日统计表获取数据
        List<String> dybzspjdxhList = getGjbzjdInfo("code");
        List<String> title = getGjbzjdInfo("name");
        List<Map<String, Object>> stageResult = new ArrayList<>();

        long isParallelCount = 0;
        for (int j = 0, len2 = dybzspjdxhList.size(); j < len2; j++) {
            long count = 0;
            for (int i = 0, len = list.size(); i < len; i++) {
                ThemeDayApplyRecord record = list.get(i);
                if (list.get(i).getDybzspjdxh().indexOf(dybzspjdxhList.get(j)) != -1 && "0".equals(record.getIsParallel())) {
                    count += record.getApplyCount();
                }
                if (list.get(i).getDybzspjdxh().indexOf(dybzspjdxhList.get(j)) != -1 && "1".equals(record.getIsParallel())) {
                    isParallelCount += record.getApplyCount();
                }

            }
            Map<String, Object> record = new HashMap<>();
            record.put("name", title.get(j));
            record.put("data", count);
            stageResult.add(record);
        }
        if (isParallelCount > 0) {//将并行推进事项的申报数也加入到dybzspjdxh=5的记录去
            long tmp = (long) stageResult.get(4).get("data");
            tmp += isParallelCount;
            stageResult.get(4).put("data", tmp);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("data", stageResult);
        return result;
    }

    /**
     * @param startTime
     * @param endTime
     * @param type
     * @param isCurrent 是否统计当前窗口
     * @return
     */
    @Override
    public Map<String, Object> getWinStageAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception {
        String windowId = getcurrentWindowId(isCurrent);
        List<AeaAnaWinDayStatistics> list = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        //不计算当天实时的
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday;
            endTime = yesterday;
            list = aeaAnaWinDayStatisticsMapper.getWinStageGroupStatistics(startTime, endTime, rootOrgId, windowId);
        } else if ("W".equals(type)) {
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            list = aeaAnaWinWeekStatisticsMapper.getWinStageGroupStatistics(thisYear, thisWeekNum, thisWeekNum, rootOrgId, windowId);
        } else if ("M".equals(type)) {
            String yearMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            list = aeaAnaWinMonthStatisticsMapper.getWinStageGroupStatistics(yearMonth, yearMonth, rootOrgId, windowId);
        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday;
            }
            list = aeaAnaWinDayStatisticsMapper.getWinStageGroupStatistics(startTime, endTime, rootOrgId, windowId);
        }


        return queryWinStageStatistics(list, type, rootOrgId, windowId,startTime,endTime);
    }

    private Map<String, Object> queryWinStageStatistics(List<AeaAnaWinDayStatistics> list, String type, String rootOrgId, String windowId, String startTime, String endTime) throws Exception {

        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday + " 00:00:00";
            endTime = yesterday + " 23:59:59";
        } else if ("W".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
            endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd") + " 23:59:59";

        } else if ("M".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
        }
        List<Long> yslList = new ArrayList<>();
        List<Long> byslList = new ArrayList<>();
        List<Long> clbqList = new ArrayList<>();
        long isParallelYiShouLi = 0, isParallelBuYuShouLi = 0, isParallelCaiLiaoBuQuan = 0;
        //计算时间段内的材料补全

        List<ThemeDayApplyRecord> applyRecords = aeaLogApplyStateHistMapper.getWindowApplyCountGroupByStage(windowId, "3", startTime, endTime, rootOrgId);
        for (int i = 0; i < 5; i++) {

            long yiShouLi = 0, buYuShouli = 0, caiLiaoBuQuan = 0;
            for (int j = 0, len = list.size(); j < len; j++) {
                AeaAnaWinDayStatistics obj = list.get(j);
                if (obj.getDybzspjdxh().indexOf(String.valueOf(i + 1)) != -1 && "0".equals(obj.getIsParallel())  ) {
                    yiShouLi += obj.getDayPreAcceptanceCount();
                    buYuShouli += obj.getDayOutScopeCount();
                }
                if (obj.getDybzspjdxh().indexOf(String.valueOf(i + 1)) != -1 && "1".equals(obj.getIsParallel())  ) {
                    isParallelYiShouLi += obj.getDayPreAcceptanceCount();
                    isParallelBuYuShouLi += obj.getDayOutScopeCount();
                }


            }
            for (int h = 0, len2 = applyRecords.size(); h < len2; h++) {
                ThemeDayApplyRecord record = applyRecords.get(h);
                if (record.getDybzspjdxh().indexOf(String.valueOf(i + 1)) != -1 && "0".equals(record.getIsParallel())  ) {
                    caiLiaoBuQuan += record.getCount();
                }
                if (record.getDybzspjdxh().indexOf(String.valueOf(i + 1)) != -1 && "1".equals(record.getIsParallel() ) ) {
                    isParallelCaiLiaoBuQuan += record.getCount();
                }
            }
            if (i == 4) {
                yslList.add(yiShouLi + isParallelYiShouLi);
                byslList.add(buYuShouli + isParallelBuYuShouLi);
                clbqList.add(caiLiaoBuQuan + isParallelCaiLiaoBuQuan);
            } else {
                yslList.add(yiShouLi);
                byslList.add(buYuShouli);
                clbqList.add(caiLiaoBuQuan);
            }

        }


        Map<String, Object> result = new HashMap<>();
        List<String> titile = getGjbzjdInfo("name");
        result.put("title", titile);
        result.put("yiShouLi", yslList);
        result.put("caiLiaoBuQuan", clbqList);
        result.put("buYuShouLi", byslList);
        return result;
    }

    /**
     * 获取国家标准审批阶段
     *
     * @return
     * @throws Exception
     */
    private List<String> getGjbzjdInfo(String type) throws Exception {
        List<BscDicCodeItem> items = bscDicCodeItemService.getActiveItemsByTypeCode("GJBZSPJD", SecurityContext.getCurrentOrgId());
        if ("name".equals(type)) {

            List<String> name = items.stream().map(BscDicCodeItem::getItemName).collect(Collectors.toList());
            return name;
        } else {

            List<String> code = items.stream().map(BscDicCodeItem::getItemCode).collect(Collectors.toList());
            return code;
        }

    }

    @Override
    public Map<String, Object> getCurrentWinAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception {

        List<WinApplyStatisticsVo> resultList = new ArrayList<>();
        //统计最终结果
        if ("D".equals(type)) { //统计昨日的
            resultList = getYesterdayWinStatic(isCurrent);
        } else if ("W".equals(type)) {
            resultList = getWeekWinStatic(isCurrent);
        } else if ("M".equals(type)) {
            resultList = getMonthWinStatic(isCurrent);
        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入日期参数有问题。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
            resultList = getDateWinStatic(startTime, endTime, isCurrent);


        }
        List<Map<String,Object>> dataList = new ArrayList<>();
        long shouliCount=0,caiLiaoBuquanCount=0,buyushouliCount=0,jiejian=0;
        if (resultList.size() > 0) {
            WinApplyStatisticsVo vo = resultList.get(0);
             shouliCount = vo.getShouliCount();
             caiLiaoBuquanCount = vo.getCaiLiaoBuquanCount();
             buyushouliCount = vo.getBuyushouliCount();
            jiejian = vo.getApplyCount();
        }
        Map<String,Object> ysl = new HashMap<>();
        ysl.put("name","已受理");
        ysl.put("value",shouliCount);
        Map<String,Object> clbq = new HashMap<>();
        clbq.put("name","材料补全");
        clbq.put("value",caiLiaoBuquanCount);
        Map<String,Object> bysl = new HashMap<>();
        bysl.put("name","不予受理");
        bysl.put("value",buyushouliCount);
        dataList.add(ysl);
        dataList.add(clbq);
        dataList.add(bysl);
        Map<String,Object> result = new HashMap<>();
        result.put("total",jiejian);
        result.put("data",dataList);
        return result;
    }
    @Override
    public Map<String, Object> getWinStageLimitTimeStatistics(String startTime, String endTime, String type, boolean isCurrent, String windowId) throws Exception {
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday + " 00:00:00";
            endTime = yesterday + " 23:59:59";
        } else if ("W".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
            endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd") + " 23:59:59";

        } else if ("M".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
        }


        List<ApplyLimitTimeVo> list = efficiencySupervisionMapper.getWinLimitTimeStatistics(startTime,endTime,windowId,SecurityContext.getCurrentOrgId());
        List<Map<String, Object>> dataList = new ArrayList<>();
        int total = 0;

        for(int i=0;i<3;i++){
            Map<String ,Object> map = new HashMap<>();
            String name = TimeruleInstState.getTimeruleInstState(String.valueOf(i+1)).getName();
            int count = 0;
            for(ApplyLimitTimeVo vo :list){
                if(vo.getInstState().equals(String.valueOf(i+1))){
                    count += vo.getCount();
                    total += vo.getCount();
                    break;
                }
            }
            map.put("name",name);
            map.put("value",count);
            dataList.add(map);
        }

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("data",dataList);
        result.put("total",total);
        return result;

    }
    @Override
    public Map<String, Object> getWinStageAvgLimitTimeStatistics(String startTime, String endTime, String type, boolean isCurrent, String windowId) throws Exception{
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday + " 00:00:00";
            endTime = yesterday + " 23:59:59";
        } else if ("W".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
            endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd") + " 23:59:59";

        } else if ("M".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
        }
        List<ApplyLimitTimeVo> list = efficiencySupervisionMapper.getWinStageLimitTimeStatistics(startTime,endTime,windowId,SecurityContext.getCurrentOrgId());
        List<List<String>> resultDataList = new ArrayList<>();
        List<List<String>> winStageList = new ArrayList<>();
        if(list.size()>0){
            List<String> dybzspjdxhList = new ArrayList<>();
            dybzspjdxhList =  getGjbzjdInfo("code");
            Map<String, List<ApplyLimitTimeVo>> collect = list.stream().collect(Collectors.groupingBy(ApplyLimitTimeVo::getWindowId));
            Set<Map.Entry<String, List<ApplyLimitTimeVo>>> entries = collect.entrySet();
            for(Map.Entry<String, List<ApplyLimitTimeVo>> entry: entries){
                List<ApplyLimitTimeVo> winList = entry.getValue();

                List<String> dataList = new ArrayList<>();
                String windowName = list.get(0).getWindowName();
                dataList.add(windowName);
                int isParallelCount = 0 ; double isParallelLimitTime =0.0;
                for (int i = 0, len = dybzspjdxhList.size(); i < len; i++) {
                    int count = 0;double limitTime =0.0;
                    for (int j = 0, len2 = list.size(); j < len2; j++) {
                        if (list.get(j).getDybzspjdxh().indexOf(dybzspjdxhList.get(i)) != -1 && "0".equals(list.get(j).getIsParallel())) {
                            count +=1;
                            limitTime += list.get(j).getUseLimitTime();
                        }
                        if (list.get(j).getDybzspjdxh().indexOf(dybzspjdxhList.get(i)) != -1 && "1".equals(list.get(j).getIsParallel())) {
                            isParallelCount ++;
                            isParallelLimitTime += list.get(j).getUseLimitTime();
                        }
                    }
                    String avgTime = getAvgTime(limitTime,count);
                    dataList.add(avgTime);
                }
                //将并行推进的事项的申报页加到dybzspjdxh=5的中去
                double _tmp = Double.valueOf(dataList.get(dybzspjdxhList.size() - 1)) + isParallelCount;
                dataList.remove(dybzspjdxhList.size() - 1);
                dataList.add(getAvgTime(isParallelLimitTime,isParallelCount));
                winStageList.add(dataList);
            }
        }

        //显示所有的窗口
        AeaServiceWindow winsearch = new AeaServiceWindow();
        winsearch.setRootOrgId(SecurityContext.getCurrentOrgId());
        winsearch.setIsActive("1");
        List<AeaServiceWindow> aeaServiceWindows = aeaServiceWindowMapper.listAeaServiceWindow(winsearch);
        if (aeaServiceWindows.size() == 0) throw new Exception("服务窗口为空！");
        for(AeaServiceWindow window :aeaServiceWindows){
            boolean had = false;
            for(int i=0,len = winStageList.size();i<len;i++){
                if(winStageList.get(i).get(0).equals(window.getWindowName())){
                    had = true;
                    resultDataList.add(winStageList.get(i));
                    break;
                }
            }
            if(!had){
                resultDataList.add(Arrays.asList(window.getWindowName(),"0","0","0","0","0"));
            }
        }

        Map<String,Object> result = new HashMap<>();
        List<String> winNameList = aeaServiceWindows.stream().map(obj -> obj.getWindowName()).collect(Collectors.toList());
        result.put("title",winNameList);
        result.put("data",resultDataList);
        return result;
    }
    /**
     * 计算平均时间
     * @param limitTime
     * @param count
     * @return
     */
    private String getAvgTime(double limitTime, int count) {
        if(count ==0){
            return "0";
        }
        BigDecimal b1 = new BigDecimal(limitTime);
        BigDecimal b2 = new BigDecimal(count);
//        BigDecimal divide = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal divide = b1.divide(b2,  BigDecimal.ROUND_HALF_UP);
        return divide.toString();
    }
}
