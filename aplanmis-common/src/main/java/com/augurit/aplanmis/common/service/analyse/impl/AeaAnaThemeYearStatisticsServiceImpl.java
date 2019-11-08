package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaAnaThemeYearStatistics;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeYearStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeYearStatisticsService;
import com.augurit.aplanmis.common.utils.AnalyseUtils;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.YearCountVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 主题年申报统计表（在月申报统计表的基础上统计。历史年数据不变。本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:36</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeYearStatisticsServiceImpl implements AeaAnaThemeYearStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeYearStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeYearStatisticsMapper aeaAnaThemeYearStatisticsMapper;
    @Autowired
    private AeaHiApplyinstMapper applyinstMapper;

    public void saveAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        aeaAnaThemeYearStatisticsMapper.insertAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
    }

    public void updateAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        aeaAnaThemeYearStatisticsMapper.updateAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
    }

    public void deleteAeaAnaThemeYearStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeYearStatisticsMapper.deleteAeaAnaThemeYearStatistics(id);
    }

    public PageInfo<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeYearStatistics> list = aeaAnaThemeYearStatisticsMapper.listAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeYearStatistics>(list);
    }

    public AeaAnaThemeYearStatistics getAeaAnaThemeYearStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeYearStatisticsMapper.getAeaAnaThemeYearStatisticsById(id);
    }

    public List<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        List<AeaAnaThemeYearStatistics> list = aeaAnaThemeYearStatisticsMapper.listAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 获取输入时间对应的年份
     * @param startTime
     * @param endTime
     */
    private List<Map<String, Object>> getDuringYear(String startTime, String endTime) throws Exception {
        if (startTime.compareTo(endTime) > 0) {
            throw new InvalidParameterException("输入参数错误，开始时间不能大于结束时间！");
        }

        //输入参数的开始年份及月份
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.setTime(DateUtils.convertStringToDate(startTime, "yyyy-MM-dd"));
        int startYear = start.get(Calendar.YEAR);
        //输入参数的结束年份及月份
        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.setTime(DateUtils.convertStringToDate(endTime, "yyyy-MM-dd"));
        int endYear = end.get(Calendar.YEAR);

        Calendar tmp = Calendar.getInstance();
        tmp.setFirstDayOfWeek(Calendar.MONDAY);

        //输入参数得出需要计算 哪些年份
        List<Map<String, Object>> years = new ArrayList<>();
        for (int i = startYear; i <= endYear; i ++) {
            Map<String, Object> analyMonth = new HashMap<>();
            analyMonth.put("year", i);

            tmp.set(Calendar.YEAR, i);
            tmp.set(Calendar.MONTH, 0);
            tmp.set(Calendar.DAY_OF_MONTH, 1);
            analyMonth.put("s_time", DateUtils.convertDateToString(tmp.getTime(),"yyyy-MM-dd"));
            tmp.set(Calendar.MONTH, 11);
            int lastDay = tmp.getActualMaximum(Calendar.DAY_OF_MONTH);
            tmp.set(Calendar.DAY_OF_MONTH, lastDay);
            analyMonth.put("e_time", DateUtils.convertDateToString(tmp.getTime(),"yyyy-MM-dd"));

            years.add(analyMonth);
        }
        return years;
    }

    /**
     * 从月表里面统计
     * @return
     * @throws Exception
     */
    @Override
    public List<AeaAnaThemeYearStatistics> getYearStatistics() throws Exception {
        //获取当前年月字符串
        List<AeaAnaThemeYearStatistics> result = new ArrayList<>();
        String yearMonth = DateUtils.getDateString("yyyy-MM");
        String year = yearMonth.substring(0,4);
        Integer lastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
        String startDate = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).toString();
        String endDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).toString();

        List<YearCountVo> yearStatistics = aeaAnaThemeYearStatisticsMapper.getYearStatisticsFromMonthTable(lastYear.toString(),year,yearMonth, SecurityContext.getCurrentOrgId());
        if(yearStatistics.size()>0){
            for(YearCountVo obj :yearStatistics){
                AeaAnaThemeYearStatistics statistics = new AeaAnaThemeYearStatistics();
                statistics.setAllApplyCount(obj.getAllApplyCount());
                String[] states= {ApplyState.SUPPLEMENTARY.getValue()};
                long yearAllSupplementary = getCount(obj.getApplyRecordId(),states,startDate,endDate,obj.getIsParallel());
                String[] states2 = {ApplyState.IN_THE_SUPPLEMENT.getValue()};
                long yearAllInTheSupplement = getCount(obj.getApplyRecordId(),states2,startDate,endDate,obj.getIsParallel());
                statistics.setAllInSupplementCount(yearAllInTheSupplement);
                statistics.setAllSupplementedCount(yearAllSupplementary);

                statistics.setAllPreAcceptanceCount(obj.getAllPreAcceptanceCount());
                statistics.setAllOutScopeCount(obj.getAllOutScopeCount());
                statistics.setAllCompletedCount(obj.getAllCompletedCount());
                statistics.setAllOverTimeCount(obj.getAllOverTimeCount());

                statistics.setApplyLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearApplyCount(),obj.getThisYearApplyCount()));

                statistics.setInSupplementLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearInSupplementCount(),obj.getThisYearInSupplementCount()));
                statistics.setSupplementedLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearSupplementedCount(),obj.getThisYearSupplementedCount()));

                statistics.setPreAcceptanceLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearPreAcceptanceCount(),obj.getThisYearPreAcceptanceCount()));
                statistics.setOutScopeLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearOutScopeCount(),obj.getThisYearOutScopeCount()));
                statistics.setCompletedLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearCompletedCount(),obj.getThisYearCompletedCount()));
                statistics.setOverTimeLrr(AnalyseUtils.calculateGrowRate(obj.getLastYearOverTimeCount(),obj.getThisYearOverTimeCount()));

                statistics.setAllPreAcceptanceRate(AnalyseUtils.calculateRate(obj.getAllPreAcceptanceCount(),obj.getAllApplyCount()));
                statistics.setAllOutScopeRate(AnalyseUtils.calculateRate(obj.getAllOutScopeCount(),obj.getAllApplyCount()));
                statistics.setAllOverTimeRate(AnalyseUtils.calculateRate(obj.getAllOverTimeCount(),obj.getAllPreAcceptanceCount()));
                statistics.setAllCompletedRate(AnalyseUtils.calculateRate(obj.getAllCompletedCount(),obj.getAllPreAcceptanceCount()));

                statistics.setThemeId(obj.getThemtId());
                statistics.setThemeName(obj.getThemtName());
                statistics.setApplyRecordId(obj.getApplyRecordId());
                statistics.setApplyRecordName(obj.getApplyRecordName());
                statistics.setStatisticsYear(Long.valueOf(year));
                statistics.setRootOrgId(SecurityContext.getCurrentOrgId());

                statistics.setThemeYearStatisticsId(UUID.randomUUID().toString());

                result.add(statistics);
            }
        }
        return result;
    }

    private long getCount(String stageId,String[] states,String startTime,String endTime,String isParallel){
        List<AeaHiApplyinst> list = applyinstMapper.getAeaHiApplyinstByStageIdAndStates(stageId,states,startTime,endTime,SecurityContext.getCurrentOrgId(), isParallel,"" );//暂时值为空
        return list.size();
    }
}

