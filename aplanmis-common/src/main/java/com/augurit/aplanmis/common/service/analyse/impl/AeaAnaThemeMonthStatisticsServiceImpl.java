package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.domain.AeaAnaThemeDayStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaThemeMonthStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeDayStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeMonthStatisticsMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeMonthStatisticsService;
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
import java.text.DecimalFormat;
import java.util.*;

/**
 * 主题月申报统计表（在日申报统计表的基础上统计。历史月数据不变。本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:28</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeMonthStatisticsServiceImpl implements AeaAnaThemeMonthStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeMonthStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeMonthStatisticsMapper aeaAnaThemeMonthStatisticsMapper;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;

    public void saveAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        aeaAnaThemeMonthStatisticsMapper.insertAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
    }

    public void updateAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        aeaAnaThemeMonthStatisticsMapper.updateAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
    }

    public void deleteAeaAnaThemeMonthStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeMonthStatisticsMapper.deleteAeaAnaThemeMonthStatistics(id);
    }

    public PageInfo<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeMonthStatistics> list = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeMonthStatistics>(list);
    }

    public AeaAnaThemeMonthStatistics getAeaAnaThemeMonthStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeMonthStatisticsMapper.getAeaAnaThemeMonthStatisticsById(id);
    }

    public List<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        List<AeaAnaThemeMonthStatistics> list = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 获取月主题-阶段基准下的各类统计
     *
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    @Override
    public void analyThemeMonthStatistics(String startTime, String endTime) throws Exception {
        List<Map<String, Object>> duringMonth = getDuringMonth(startTime, endTime);
        for (Map<String, Object> Month : duringMonth) {
            Integer year = (Integer) Month.get("year");
            Integer month = (Integer) Month.get("month");
            String monthStartTime = (String) Month.get("s_time");
            String monthEndTime = (String) Month.get("e_time");
            String currentOrgId = SecurityContext.getCurrentOrgId();

            //1.根据日统计汇总这月的数据
            List<ThemePeriodStatistics> themePeriodStatisticsList = aeaAnaThemeDayStatisticsMapper.getThemePeriodStatistics(currentOrgId, monthStartTime, monthEndTime);
            if (CollectionUtils.isNotEmpty(themePeriodStatisticsList)) {
                for (ThemePeriodStatistics themePeriodStatistics : themePeriodStatisticsList) {
                    AeaAnaThemeMonthStatistics themeMonthStatistics = new AeaAnaThemeMonthStatistics();
                    themeMonthStatistics.setThemeId(themePeriodStatistics.getThemeId());
                    themeMonthStatistics.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    themeMonthStatistics.setMonthApplyCount(themePeriodStatistics.getPeriodApplyCount());
                    themeMonthStatistics.setMonthPreAcceptanceCount(themePeriodStatistics.getPeriodPreAcceptanceCount());
                    themeMonthStatistics.setMonthOutScopeCount(themePeriodStatistics.getPeriodOutScopeCount());
                    themeMonthStatistics.setMonthCompletedCount(themePeriodStatistics.getPeriodCompletedCount());
                    themeMonthStatistics.setMonthOverTimeCount(themePeriodStatistics.getPeriodOverTimeCount());

                    //2.总数，取最新的日统计的总数
                    PageHelper.startPage(new Page<>(1, 1));
                    List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatisticsList = aeaAnaThemeDayStatisticsMapper.getAeaAnaThemeDayStatistics(themePeriodStatistics.getThemeId(), themePeriodStatistics.getApplyRecordId(),
                            null, null, currentOrgId, monthStartTime, monthEndTime);
                    if (CollectionUtils.isNotEmpty(aeaAnaThemeDayStatisticsList)) {
                        AeaAnaThemeDayStatistics themeDayStatistics = aeaAnaThemeDayStatisticsList.get(0);
                        themeMonthStatistics.setThemeName(themeDayStatistics.getThemeName());
                        themeMonthStatistics.setApplyRecordName(themeDayStatistics.getApplyRecordName());
                        themeMonthStatistics.setAllApplyCount(themeDayStatistics.getAllApplyCount());
                        themeMonthStatistics.setAllInSupplementCount(themeDayStatistics.getAllInSupplementCount());
                        themeMonthStatistics.setAllSupplementedCount(themeDayStatistics.getAllSupplementedCount());
                        themeMonthStatistics.setAllPreAcceptanceCount(themeDayStatistics.getAllPreAcceptanceCount());
                        themeMonthStatistics.setAllOutScopeCount(themeDayStatistics.getAllOutScopeCount());
                        themeMonthStatistics.setAllCompletedCount(themeDayStatistics.getAllCompletedCount());
                        themeMonthStatistics.setAllOverTimeCount(themeDayStatistics.getAllOverTimeCount());
                        themeMonthStatistics.setAllPreAcceptanceRate(themeDayStatistics.getAllPreAcceptanceRate());
                        themeMonthStatistics.setAllOutScopeRate(themeDayStatistics.getAllOutScopeRate());
                        themeMonthStatistics.setAllOverTimeRate(themeDayStatistics.getAllOverTimeRate());
                        themeMonthStatistics.setAllCompletedRate(themeDayStatistics.getAllCompletedRate());
                    } else {
                        logger.error("【月统计异常】aeaAnaThemeDayStatisticsList为空");
                    }

                    //3.计算环比&同比
                    AeaAnaThemeMonthStatistics lastMonthSearch = new AeaAnaThemeMonthStatistics();
                    lastMonthSearch.setThemeId(themePeriodStatistics.getThemeId());
                    lastMonthSearch.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    lastMonthSearch.setRootOrgId(currentOrgId);
                    DecimalFormat monthFormat = new DecimalFormat("00");
                    String formatLastMoth = monthFormat.format(month - 1);
                    lastMonthSearch.setStatisticsMonth(year + "-" + formatLastMoth);
                    List<AeaAnaThemeMonthStatistics> themeLastMonthStatisticsList = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(lastMonthSearch);
                    if (CollectionUtils.isNotEmpty(themeLastMonthStatisticsList)) {
                        AeaAnaThemeMonthStatistics themeLastMonthStatistics = themeLastMonthStatisticsList.get(0);
                        themeMonthStatistics.setApplyLrr(computeLLR(themeMonthStatistics.getMonthApplyCount(), themeLastMonthStatistics.getMonthApplyCount()));
                        themeMonthStatistics.setInSupplementLrr(computeLLR(themeMonthStatistics.getAllInSupplementCount(), themeLastMonthStatistics.getAllInSupplementCount()));
                        themeMonthStatistics.setSupplementedLrr(computeLLR(themeMonthStatistics.getAllSupplementedCount(), themeLastMonthStatistics.getAllSupplementedCount()));
                        themeMonthStatistics.setPreAcceptanceLrr(computeLLR(themeMonthStatistics.getMonthPreAcceptanceCount(), themeLastMonthStatistics.getMonthPreAcceptanceCount()));
                        themeMonthStatistics.setOutScopeLrr(computeLLR(themeMonthStatistics.getMonthOutScopeCount(), themeLastMonthStatistics.getMonthOutScopeCount()));
                        themeMonthStatistics.setCompletedLrr(computeLLR(themeMonthStatistics.getMonthCompletedCount(), themeLastMonthStatistics.getMonthCompletedCount()));
                        themeMonthStatistics.setOverTimeLrr(computeLLR(themeMonthStatistics.getMonthOverTimeCount(), themeLastMonthStatistics.getMonthOverTimeCount()));
                    } else {
                        themeMonthStatistics.setApplyLrr(0.00);
                        themeMonthStatistics.setInSupplementLrr(0.00);
                        themeMonthStatistics.setSupplementedLrr(0.00);
                        themeMonthStatistics.setPreAcceptanceLrr(0.00);
                        themeMonthStatistics.setOutScopeLrr(0.00);
                        themeMonthStatistics.setCompletedLrr(0.00);
                        themeMonthStatistics.setOverTimeLrr(0.00);
                    }
                    lastMonthSearch.setStatisticsMonth((year - 1) + monthFormat.format(month));
                    List<AeaAnaThemeMonthStatistics> themeLastYearMonthStatisticsList = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(lastMonthSearch);
                    if (CollectionUtils.isNotEmpty(themeLastYearMonthStatisticsList)) {
                        AeaAnaThemeMonthStatistics themeLastYearMonthStatistics = themeLastYearMonthStatisticsList.get(0);
                        ;
                        themeMonthStatistics.setApplyOnYoyBasis(computeLLR(themeMonthStatistics.getMonthApplyCount(), themeLastYearMonthStatistics.getMonthApplyCount()));
                        themeMonthStatistics.setInSupplementOnYoyBasis(computeLLR(themeMonthStatistics.getAllInSupplementCount(), themeLastYearMonthStatistics.getAllInSupplementCount()));
                        themeMonthStatistics.setSupplementedOnYoyBasis(computeLLR(themeMonthStatistics.getAllSupplementedCount(), themeLastYearMonthStatistics.getAllSupplementedCount()));
                        themeMonthStatistics.setPreAcceptanceOnYoyBasis(computeLLR(themeMonthStatistics.getMonthPreAcceptanceCount(), themeLastYearMonthStatistics.getMonthPreAcceptanceCount()));
                        themeMonthStatistics.setOutScopeOnYoyBasis(computeLLR(themeMonthStatistics.getMonthOutScopeCount(), themeLastYearMonthStatistics.getMonthOutScopeCount()));
                        themeMonthStatistics.setCompletedOnYoyBasis(computeLLR(themeMonthStatistics.getMonthCompletedCount(), themeLastYearMonthStatistics.getMonthCompletedCount()));
                        themeMonthStatistics.setOverTimeOnYoyBasis(computeLLR(themeMonthStatistics.getMonthOverTimeCount(), themeLastYearMonthStatistics.getMonthOverTimeCount()));
                    } else {
                        themeMonthStatistics.setApplyOnYoyBasis(0.00);
                        themeMonthStatistics.setInSupplementOnYoyBasis(0.00);
                        themeMonthStatistics.setSupplementedOnYoyBasis(0.00);
                        themeMonthStatistics.setPreAcceptanceOnYoyBasis(0.00);
                        themeMonthStatistics.setOutScopeOnYoyBasis(0.00);
                        themeMonthStatistics.setCompletedOnYoyBasis(0.00);
                        themeMonthStatistics.setOverTimeOnYoyBasis(0.00);
                    }


                    themeMonthStatistics.setStatisticsMonth(year + "-" + monthFormat.format(month));
                    themeMonthStatistics.setStatisticsStartDate(DateUtils.convertStringToDate(monthStartTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                    themeMonthStatistics.setStatisticsEndDate(DateUtils.convertStringToDate(monthEndTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
                    themeMonthStatistics.setModifier(SecurityContext.getCurrentUserName());
                    themeMonthStatistics.setModifyTime(new Date());
                    themeMonthStatistics.setRootOrgId(currentOrgId);

                    //4.新增或者更新 新增statisticsRecord？
                    AeaAnaThemeMonthStatistics search = new AeaAnaThemeMonthStatistics();
                    search.setThemeId(themePeriodStatistics.getThemeId());
                    search.setApplyRecordId(themePeriodStatistics.getApplyRecordId());
                    search.setStatisticsMonth(year + "-" + monthFormat.format(month));
                    search.setRootOrgId(currentOrgId);
                    List<AeaAnaThemeMonthStatistics> searchList = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(search);
                    if (CollectionUtils.isNotEmpty(searchList)) {
                        AeaAnaThemeMonthStatistics searchThemeMonthStatistics = searchList.get(0);
                        themeMonthStatistics.setThemeMonthStatisticsId(searchThemeMonthStatistics.getThemeMonthStatisticsId());
                        aeaAnaThemeMonthStatisticsMapper.updateAeaAnaThemeMonthStatistics(themeMonthStatistics);
                    } else {
                        themeMonthStatistics.setThemeMonthStatisticsId(UuidUtil.generateUuid());
                        themeMonthStatistics.setStatisticsRecordId(UuidUtil.generateUuid());
                        aeaAnaThemeMonthStatisticsMapper.insertAeaAnaThemeMonthStatistics(themeMonthStatistics);
                    }
                }
            } else {
                logger.error("【月统计异常】themePeriodStatistics为空，统计参数:{ startTime:{}, endTime:{}, year:{}, month:{}, monthStartTime:{}, monthEndTime:{} }",
                        startTime, endTime, year, month, monthStartTime, monthEndTime);
            }
        }
    }

    /**
     * 计算环比
     *
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
     * 获取输入时间对应的年份，月份
     *
     * @param startTime
     * @param endTime
     */
    private List<Map<String, Object>> getDuringMonth(String startTime, String endTime) throws Exception {
        if (startTime.compareTo(endTime) > 0) {
            throw new InvalidParameterException("输入参数错误，开始时间不能大于结束时间！");
        }

        //输入参数的开始年份及月份
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.setTime(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd"));
        int startMonth = start.get(Calendar.MONTH) + 1;
        int startYear = start.get(Calendar.YEAR);
        //输入参数的结束年份及月份
        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.setTime(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd"));
        int endMonth = end.get(Calendar.MONTH) + 1;
        int endYear = end.get(Calendar.YEAR);

        Calendar tmp = Calendar.getInstance();
        tmp.setFirstDayOfWeek(Calendar.MONDAY);

        //输入参数得出需要计算 哪些年份的哪些月份
        List<Map<String, Object>> duringLlist = new ArrayList<>();
        if (startYear != endYear) {
            for (int i = startYear; i <= endYear; i++) {
                Map<String, Object> map = new HashMap<>();
                if (i == startYear) {
                    map.put("year", startYear);
                    map.put("s_month", startMonth);
                    map.put("e_month", 12);
                } else if (i == endYear) {
                    map.put("year", endYear);
                    map.put("s_month", 1);
                    map.put("e_month", endMonth);
                } else {
                    tmp.set(Calendar.YEAR, i);
                    map.put("year", i);
                    map.put("s_month", 1);
                    map.put("e_month", 12);
                }
                duringLlist.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("year", startYear);
            map.put("s_month", startMonth);
            map.put("e_month", endMonth);
            duringLlist.add(map);
        }

        List<Map<String, Object>> months = new ArrayList<>();
        duringLlist.forEach(m -> {
            Integer year = (Integer) m.get("year");
            Integer sMonth = (Integer) m.get("s_month");
            Integer eMonth = (Integer) m.get("e_month");
            for (int i = sMonth; i <= eMonth; i++) {
                Map<String, Object> analyMonth = new HashMap<>();
                analyMonth.put("year", year);
                analyMonth.put("month", i);

                tmp.set(Calendar.YEAR, year);
                tmp.set(Calendar.MONTH, i - 1);
                tmp.set(Calendar.DAY_OF_MONTH, 1);
                analyMonth.put("s_time", DateUtils.convertDateToString(tmp.getTime(), "yyyy-MM-dd"));
                int lastDay = tmp.getActualMaximum(Calendar.DAY_OF_MONTH);
                tmp.set(Calendar.DAY_OF_MONTH, lastDay);
                analyMonth.put("e_time", DateUtils.convertDateToString(tmp.getTime(), "yyyy-MM-dd"));

                months.add(analyMonth);
            }
        });
        return months;
    }

}

