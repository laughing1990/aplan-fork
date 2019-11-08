package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.domain.AeaAnaThemeDayStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaThemeWeekStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeDayStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeWeekStatisticsMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeWeekStatisticsService;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.ThemePeriodStatistics;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 主题周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:19:04</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeWeekStatisticsServiceImpl implements AeaAnaThemeWeekStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeWeekStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeWeekStatisticsMapper aeaAnaThemeWeekStatisticsMapper;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;

    public void saveAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        aeaAnaThemeWeekStatisticsMapper.insertAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
    }

    public void updateAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        aeaAnaThemeWeekStatisticsMapper.updateAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
    }

    public void deleteAeaAnaThemeWeekStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeWeekStatisticsMapper.deleteAeaAnaThemeWeekStatistics(id);
    }

    public PageInfo<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeWeekStatistics> list = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeWeekStatistics>(list);
    }

    public AeaAnaThemeWeekStatistics getAeaAnaThemeWeekStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeWeekStatisticsMapper.getAeaAnaThemeWeekStatisticsById(id);
    }

    public List<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        List<AeaAnaThemeWeekStatistics> list = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 获取周主题-阶段基准下的各类统计
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    @Override
    public void analyThemeWeekStatistics(String startTime, String endTime) throws Exception {
        List<Map<String, Object>> duringWeek = getDuringWeek(startTime, endTime);
        for (Map<String, Object> week : duringWeek) {

            Integer year = (Integer) week.get("year");
            Integer weekNum = (Integer) week.get("week");
            String weekStartTime = (String) week.get("s_time");
            String weekEndTime = (String) week.get("e_time");
            String currentOrgId = SecurityContext.getCurrentOrgId();

            //1.根据日统计汇总这周的数据
            List<ThemePeriodStatistics> themePeriodStatisticsList = aeaAnaThemeDayStatisticsMapper.getThemePeriodStatistics(currentOrgId, weekStartTime, weekEndTime);
            if (CollectionUtils.isNotEmpty(themePeriodStatisticsList)) {
                for (ThemePeriodStatistics themePeriodStatistics : themePeriodStatisticsList) {
                    AeaAnaThemeWeekStatistics themeWeekStatistics = new AeaAnaThemeWeekStatistics();
                    themeWeekStatistics.setThemeId(themePeriodStatistics.getThemeId());
                    themeWeekStatistics.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    themeWeekStatistics.setWeekApplyCount(themePeriodStatistics.getPeriodApplyCount());
                    themeWeekStatistics.setWeekPreAcceptanceCount(themePeriodStatistics.getPeriodPreAcceptanceCount());
                    themeWeekStatistics.setWeekOutScopeCount(themePeriodStatistics.getPeriodOutScopeCount());
                    themeWeekStatistics.setWeekCompletedCount(themePeriodStatistics.getPeriodCompletedCount());
                    themeWeekStatistics.setWeekOverTimeCount(themePeriodStatistics.getPeriodOverTimeCount());

                    //2.总数，取最新的日统计的总数
                    PageHelper.startPage(new Page<>(1, 1));
                    List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatisticsList = aeaAnaThemeDayStatisticsMapper.getAeaAnaThemeDayStatistics(themePeriodStatistics.getThemeId(), themePeriodStatistics.getApplyRecordId(),
                            null, null, currentOrgId, weekStartTime, weekEndTime);
                    if (CollectionUtils.isNotEmpty(aeaAnaThemeDayStatisticsList)) {
                        AeaAnaThemeDayStatistics themeDayStatistics = aeaAnaThemeDayStatisticsList.get(0);
                        themeWeekStatistics.setThemeName(themeDayStatistics.getThemeName());
                        themeWeekStatistics.setApplyRecordName(themeDayStatistics.getApplyRecordName());
                        themeWeekStatistics.setAllApplyCount(themeDayStatistics.getAllApplyCount());
                        themeWeekStatistics.setAllInSupplementCount(themeDayStatistics.getAllInSupplementCount());
                        themeWeekStatistics.setAllSupplementedCount(themeDayStatistics.getAllSupplementedCount());
                        themeWeekStatistics.setAllPreAcceptanceCount(themeDayStatistics.getAllPreAcceptanceCount());
                        themeWeekStatistics.setAllOutScopeCount(themeDayStatistics.getAllOutScopeCount());
                        themeWeekStatistics.setAllCompletedCount(themeDayStatistics.getAllCompletedCount());
                        themeWeekStatistics.setAllOverTimeCount(themeDayStatistics.getAllOverTimeCount());
                        themeWeekStatistics.setAllPreAcceptanceRate(themeDayStatistics.getAllPreAcceptanceRate());
                        themeWeekStatistics.setAllOutScopeRate(themeDayStatistics.getAllOutScopeRate());
                        themeWeekStatistics.setAllOverTimeRate(themeDayStatistics.getAllOverTimeRate());
                        themeWeekStatistics.setAllCompletedRate(themeDayStatistics.getAllCompletedRate());
                    } else {
                        logger.error("【周统计异常】aeaAnaThemeDayStatisticsList为空");
                    }

                    //3.计算环比
                    AeaAnaThemeWeekStatistics lastWeekSearch = new AeaAnaThemeWeekStatistics();
                    lastWeekSearch.setThemeId(themePeriodStatistics.getThemeId());
                    lastWeekSearch.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    lastWeekSearch.setRootOrgId(currentOrgId);
                    lastWeekSearch.setStatisticsYear(new Long(year));
                    lastWeekSearch.setWeekNum(new Long(weekNum - 1));
                    List<AeaAnaThemeWeekStatistics> themeLastWeekStatisticsList = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(lastWeekSearch);
                    if (CollectionUtils.isNotEmpty(themeLastWeekStatisticsList)) {
                        AeaAnaThemeWeekStatistics themeLastWeekStatistics = themeLastWeekStatisticsList.get(0);
                        themeWeekStatistics.setApplyLrr(computeLLR(themeWeekStatistics.getWeekApplyCount(), themeLastWeekStatistics.getWeekApplyCount()));
                        themeWeekStatistics.setPreAcceptanceLrr(computeLLR(themeWeekStatistics.getWeekPreAcceptanceCount(), themeLastWeekStatistics.getWeekPreAcceptanceCount()));
                        themeWeekStatistics.setOutScopeLrr(computeLLR(themeWeekStatistics.getWeekOutScopeCount(), themeLastWeekStatistics.getWeekOutScopeCount()));
                        themeWeekStatistics.setCompletedLrr(computeLLR(themeWeekStatistics.getWeekCompletedCount(), themeLastWeekStatistics.getWeekCompletedCount()));
                        themeWeekStatistics.setOverTimeLrr(computeLLR(themeWeekStatistics.getWeekOverTimeCount(), themeLastWeekStatistics.getWeekOverTimeCount()));
                    } else {
                        themeWeekStatistics.setApplyLrr(0.00);
                        themeWeekStatistics.setPreAcceptanceLrr(0.00);
                        themeWeekStatistics.setOutScopeLrr(0.00);
                        themeWeekStatistics.setCompletedLrr(0.00);
                        themeWeekStatistics.setOverTimeLrr(0.00);
                    }

                    themeWeekStatistics.setStatisticsYear(new Long(year));
                    themeWeekStatistics.setWeekNum(new Long(weekNum));
                    themeWeekStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(weekStartTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                    themeWeekStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(weekEndTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
                    themeWeekStatistics.setModifier(SecurityContext.getCurrentUserName());
                    themeWeekStatistics.setModifyTime(new Date());
                    themeWeekStatistics.setRootOrgId(currentOrgId);

                    //4.新增或者更新 新增statisticsRecord？
                    AeaAnaThemeWeekStatistics search = new AeaAnaThemeWeekStatistics();
                    search.setThemeId(themePeriodStatistics.getThemeId());
                    search.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    search.setStatisticsYear(new Long(year));
                    search.setWeekNum(new Long(weekNum));
                    search.setRootOrgId(currentOrgId);
                    List<AeaAnaThemeWeekStatistics> searchList = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(search);
                    if (CollectionUtils.isNotEmpty(searchList)) {
                        AeaAnaThemeWeekStatistics searchThemeWeekStatistics = searchList.get(0);
                        themeWeekStatistics.setThemeWeekStatisticsId(searchThemeWeekStatistics.getThemeWeekStatisticsId());
                        aeaAnaThemeWeekStatisticsMapper.updateAeaAnaThemeWeekStatistics(themeWeekStatistics);
                    } else {
                        themeWeekStatistics.setThemeWeekStatisticsId(UuidUtil.generateUuid());
                        themeWeekStatistics.setStatisticsRecordId(UuidUtil.generateUuid());
                        aeaAnaThemeWeekStatisticsMapper.insertAeaAnaThemeWeekStatistics(themeWeekStatistics);
                    }
                }
            } else {
                logger.error("【周统计异常】themePeriodStatistics为空，统计参数:{ startTime:{}, endTime:{}, year:{}, weekNum:{}, weekStartTime:{}, weekEndTime:{} }",
                        startTime, endTime, year, weekNum, weekStartTime, weekEndTime);
            }

        }
    }

    /**
     * 计算环比
     * @param nowCount
     * @param lastCount
     * @return
     */
    private Double computeLLR(Long nowCount, Long lastCount) {
        if (lastCount == 0) {
            return 0.00;
        }
        BigDecimal llr = new BigDecimal(nowCount - lastCount).divide(new BigDecimal(lastCount), 2, RoundingMode.HALF_UP);
        return llr.doubleValue();
    }

    /**
     * 获取输入时间对应的年份，周数，周开始时间，周结束时间
     * @param startTime
     * @param endTime
     */
    private List<Map<String, Object>> getDuringWeek(String startTime, String endTime) throws Exception {

        if (startTime.compareTo(endTime) > 0) {
            throw new InvalidParameterException("输入参数错误，开始时间不能大于结束时间！");
        }

        //输入参数的开始年份及周数
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.setTime(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd"));
        int startWeek = start.get(Calendar.WEEK_OF_YEAR);
        int startYear = start.get(Calendar.YEAR);
        //输入参数的结束年份及周数
        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.setTime(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd"));
        int endWeek = end.get(Calendar.WEEK_OF_YEAR);
        int endYear = end.get(Calendar.YEAR);

        Calendar tmp = Calendar.getInstance();
        tmp.setFirstDayOfWeek(Calendar.MONDAY);

        //输入参数得出需要计算 哪些年份的哪些周数
        List<Map<String, Object>> duringLlist = new ArrayList<>();
        if (startYear != endYear) {
            for (int i = startYear; i <= endYear; i++) {
                Map<String, Object> map = new HashMap<>();
                if (i == startYear) {
                    map.put("year", startYear);
                    map.put("s_week", startWeek);
                    map.put("e_week", start.getWeeksInWeekYear());
                } else if (i == endYear) {
                    map.put("year", endYear);
                    map.put("s_week", 1);
                    map.put("e_week", endWeek);
                } else {
                    tmp.set(Calendar.YEAR, i);
                    map.put("year", i);
                    map.put("s_week", 1);
                    map.put("e_week", tmp.getWeeksInWeekYear());
                }
                duringLlist.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("year", startYear);
            map.put("s_week", startWeek);
            map.put("e_week", endWeek);
            duringLlist.add(map);
        }

        List<Map<String, Object>> weeks = new ArrayList<>();
        duringLlist.forEach(m -> {
            Integer year = (Integer) m.get("year");
            Integer sWeek = (Integer)m.get("s_week");
            Integer eWeek = (Integer)m.get("e_week");
            for (int i = sWeek; i <= eWeek; i ++) {
                Map<String, Object> analyWeek = new HashMap<>();
                analyWeek.put("year", year);
                analyWeek.put("week", i);

                tmp.set(Calendar.YEAR, year);
                tmp.set(Calendar.WEEK_OF_YEAR, i);
                tmp.set(Calendar.DAY_OF_WEEK, 2);
                analyWeek.put("s_time", DateUtils.convertDateToString(tmp.getTime(),"yyyy-MM-dd"));
                tmp.set(Calendar.DAY_OF_WEEK, 1);
                analyWeek.put("e_time", DateUtils.convertDateToString(tmp.getTime(),"yyyy-MM-dd"));

                weeks.add(analyWeek);
            }
        });

        return weeks;
    }
}

