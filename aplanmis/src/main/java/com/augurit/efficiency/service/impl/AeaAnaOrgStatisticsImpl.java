package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.efficiency.utils.CalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时统计的方法不要调用SecurityContext获取登录信息。
 * 定时器执行的方法是没有用户登录的。
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaAnaOrgStatisticsImpl {

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private AeaAnaOrgDayStatisticsMapper aeaAnaOrgDayStatisticsMapper;
    @Autowired
    private AeaAnaOrgWeekStatisticsMapper aeaAnaOrgWeekStatisticsMapper;
    @Autowired
    private AeaAnaOrgMonthStatisticsMapper aeaAnaOrgMonthStatisticsMapper;
    @Autowired
    private AeaAnaOrgYearStatisticsMapper aeaAnaOrgYearStatisticsMapper;
    @Autowired
    private AeaAnaStatisticsRecordMapper aeaAnaStatisticsRecordMapper;
    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    private static String ORG_REPORTID_DAY = "0001";
    private static String ORG_REPORTNAME_DAY = "部门日办件统计表";
    private static String ORG_REPORTID_WEEK = "0002";
    private static String ORG_REPORTNAME_WEEK = "部门周办件统计表";
    private static String ORG_REPORTID_MONTH = "0003";
    private static String ORG_REPORTNAME_MONTH = "部门月办件统计表";
    private static String ORG_REPORTID_YEAR = "0004";
    private static String ORG_REPORTNAME_YEAR = "部门年办件统计表";
    private static String DEFAULT_CREATER_SYS = "sys_admin_job";

    private final String[] applySourceArray = {"net","win"};

    /**
     * 统计部门日办件
     * 一个部门有多个事项，生成多条记录。
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsOrgDayData(String rootOrgId,String operateSource,String creater,Date today) throws Exception{
        long beginTime = System.currentTimeMillis();
        List<AeaAnaOrgDayStatistics> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statisticsDate = DateUtils.getPreDateByDate(today);
        String dateFormat = sdf.format(statisticsDate);
        String startTime = dateFormat + " 00:00:00";
        String endTime = dateFormat + " 23:59:59";
        if(StringUtils.isBlank(creater)){
            creater =   DEFAULT_CREATER_SYS;
        }
        //查询是否已统计过
        AeaAnaStatisticsRecord statisticsRecord = buildStatisticsRecord(ORG_REPORTID_DAY,ORG_REPORTNAME_DAY,startTime,endTime,operateSource,"b",creater,rootOrgId);
        //找到所有事项对应的所有审批部门
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        for(int i=0,len=orgInfoList.size();i<len;i++){
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            //统计此部门的办件情况
            try {
                List<AeaAnaOrgDayStatistics> oneOrgList = statisticsOneOrgDay(orgId, orgName,statisticsRecord.getStatisticsRecordId(),statisticsRecord.getStatisticsStartDate());
                list.addAll(oneOrgList);
            }catch (Exception e){
                log.debug("统计部门日办件情况出错，部门ID：{}，部门名称：{}，错误信息：{}",orgId,orgName,e.getMessage());
            }
        }
        if(list.size() > 0) {
            //删除当日数据
            aeaAnaOrgDayStatisticsMapper.deleteThisDayStatisticsData(dateFormat,rootOrgId);
            //批量插入记录
            aeaAnaOrgDayStatisticsMapper.batchInserOrgDayStatisticst(list);
        }
        log.debug("日办件统计耗时：" + (System.currentTimeMillis()-beginTime));
    }

    /**
     * 统计部门周办件
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsOrgWeekData(String rootOrgId,String operateSource,String creater,Date today) throws Exception{
        long beginTime = System.currentTimeMillis();
        List<AeaAnaOrgWeekStatistics> list = new ArrayList<>();
        //1、找到所有事项对应的所有审批部门
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取周一、周日
        Date monday = DateUtils.getThisWeekMonday(preDate);
        Date sunday = DateUtils.getThisWeekSunday(preDate);
        String startTime = sdf.format(monday) + " 00:00:00";
        String endTime = sdf.format(sunday) + " 23:59:59";
        if(StringUtils.isBlank(creater)){
            creater =   DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord  record = buildStatisticsRecord(ORG_REPORTID_WEEK,ORG_REPORTNAME_WEEK,startTime,endTime,operateSource,"b",creater,rootOrgId);
        for(int i=0,len=orgInfoList.size();i<len;i++){
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            //统计此部门的办件情况
            try {
                List<AeaAnaOrgWeekStatistics> oneOrgList = statisticsOneOrgWeek(orgId, orgName,record.getStatisticsRecordId(),record.getStatisticsStartDate(),record.getStatisticsEndDate(),today);
                list.addAll(oneOrgList);
            }catch (Exception e){
                log.debug("统计部门周办件情况出错，部门ID：{}，部门名称：{}，错误信息：{}",orgId,orgName,e.getMessage());
            }
        }
        if(list.size() > 0){
            //删除本周数据
            aeaAnaOrgWeekStatisticsMapper.deleteThisWeekStatisticsData(startTime,endTime,rootOrgId);
            //批量插入记录
            aeaAnaOrgWeekStatisticsMapper.batchInserOrgWeekStatisticst(list);
        }
        log.debug("周办件统计耗时：" + (System.currentTimeMillis()-beginTime));
    }

    /**
     * 统计部门月办件
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsOrgMonthData(String rootOrgId,String operateSource,String creater,Date today) throws Exception{
        long beginTime = System.currentTimeMillis();
        List<AeaAnaOrgMonthStatistics> list = new ArrayList<>();
        //1、找到所有事项对应的所有审批部门
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取本月第一天、本月最后一天
        Date firstDay = DateUtils.firstDayOfMonth(preDate);
        Date lastDay = DateUtils.lastDayOfMonth(preDate);
        String startTime = sdf.format(firstDay) + " 00:00:00";
        String endTime = sdf.format(lastDay) + " 23:59:59";
        String statisticsMonth = DateUtils.convertDateToString(preDate, "yyyy-MM");
        if(StringUtils.isBlank(creater)){
            creater =   DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord  record = buildStatisticsRecord(ORG_REPORTID_MONTH,ORG_REPORTNAME_MONTH,startTime,endTime,operateSource,"b",creater,rootOrgId);
        for(int i=0,len=orgInfoList.size();i<len;i++){
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            //统计此部门的办件情况
            try {
                List<AeaAnaOrgMonthStatistics> oneOrgList = statisticsOneOrgMonth(orgId, orgName,record.getStatisticsRecordId(),record.getStatisticsStartDate(),record.getStatisticsEndDate(),today);
                list.addAll(oneOrgList);
            }catch (Exception e){
                log.debug("统计部门月办件情况出错，部门ID：{}，部门名称：{}，错误信息：{}",orgId,orgName,e.getMessage());
            }
        }
        if(list.size() > 0){
            //删除本月数据
            aeaAnaOrgMonthStatisticsMapper.deleteThisMonthStatisticsData(statisticsMonth,rootOrgId);
            //批量插入记录
            aeaAnaOrgMonthStatisticsMapper.batchInserOrgMonthStatisticst(list);
        }
        log.debug("月办件统计耗时：" + (System.currentTimeMillis()-beginTime));
    }

    /**
     * 统计部门年办件
     * @param rootOrgId
     * @param operateSource
     * @param creater
     * @param today
     * @throws Exception
     */
    public void statisticsOrgYearData(String rootOrgId,String operateSource,String creater,Date today) throws Exception{
        long beginTime = System.currentTimeMillis();
        List<AeaAnaOrgYearStatistics> list = new ArrayList<>();
        //1、找到所有事项对应的所有审批部门
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date preDate = DateUtils.getPreDateByDate(today);
        //获取本年第一天、本年最后一天
        Date firstDay = DateUtils.firstDayOfYear(preDate);
        Date lastDay = DateUtils.lastDayOfYear(preDate);
        String startTime = sdf.format(firstDay) + " 00:00:00";
        String endTime = sdf.format(lastDay) + " 23:59:59";
        String year = DateUtils.convertDateToString(preDate, "yyyy");
        if(StringUtils.isBlank(creater)){
            creater =   DEFAULT_CREATER_SYS;
        }
        //查询是否已有统计数据，有则更新记录，反之插入记录
        AeaAnaStatisticsRecord  record = buildStatisticsRecord(ORG_REPORTID_YEAR,ORG_REPORTNAME_YEAR,startTime,endTime,operateSource,"b",creater,rootOrgId);
        for(int i=0,len=orgInfoList.size();i<len;i++){
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            //统计此部门的办件情况
            try {
                List<AeaAnaOrgYearStatistics> oneOrgList = statisticsOneOrgYear(orgId, orgName,record.getStatisticsRecordId(),record.getStatisticsStartDate(),record.getStatisticsEndDate(),today);
                list.addAll(oneOrgList);
            }catch (Exception e){
                log.debug("统计部门年办件情况出错，部门ID：{}，部门名称：{}，错误信息：{}",orgId,orgName,e.getMessage());
            }
        }
        if(list.size() > 0){
            //删除本年数据
            aeaAnaOrgYearStatisticsMapper.deleteThisYearStatisticsData(year,rootOrgId);
            //批量插入记录
            aeaAnaOrgYearStatisticsMapper.batchInserOrgYearStatisticst(list);
        }
        log.debug("年办件统计耗时：" + (System.currentTimeMillis()-beginTime));
    }

    private List<AeaAnaOrgYearStatistics> statisticsOneOrgYear(String orgId,String orgName,String statisticsRecordId,Date firstDay,Date lastDay,Date today)throws ParseException{
        List<AeaAnaOrgYearStatistics> list = new ArrayList<>();
        //查询部门下发布和试运行的全部实施事项
        List<AeaItemBasic> itemBasics = aeaItemBasicMapper.listAeaItemBasicByOrgId(orgId);
        if(itemBasics.size() > 0){
            String preYear = DateUtils.convertDateToString(DateUtils.getPreDateByDate(firstDay), "yyyy");
            //获取本年已统计的月份
            Date preDate = DateUtils.getPreDateByDate(today);
            String thisMonth = DateUtils.convertDateToString(preDate, "yyyy-MM");
            List<String> monthlyList = DateUtils.calculateMonthly(DateUtils.convertDateToString(firstDay, "yyyy-MM"), thisMonth);
            String year = DateUtils.convertDateToString(preDate, "yyyy");
            for(int j=0,leng=applySourceArray.length;j<leng;j++) {
                String applySource = applySourceArray[j];
                for (int i = 0, len = itemBasics.size(); i < len; i++) {
                    AeaItemBasic item = itemBasics.get(i);
                    String itemId = item.getItemId();
                    String itemName = item.getItemName();
                    List<AeaAnaOrgMonthStatistics> monthList = aeaAnaOrgMonthStatisticsMapper.queryAeaAnaOrgMonthStatistics(orgId, itemId,applySource, monthlyList);
                    if (monthList.size() == 0) {
                        log.debug("事项ID：{}，事项名称：{}，未找到办件统计数据！", itemId, itemName);
                        continue;
                    }
                    AeaAnaOrgYearStatistics one = new AeaAnaOrgYearStatistics();
                    //获取上年统计数据
                    AeaAnaOrgYearStatistics preYearStatistics = aeaAnaOrgYearStatisticsMapper.getAeaAnaOrgYearStatistics(orgId, itemId,applySource, preYear);
                    one.setOrgYearStatisticsId(UUID.randomUUID().toString());
                    one.setStatisticsRecordId(statisticsRecordId);
                    one.setOrgId(orgId);
                    one.setOrgName(orgName);
                    BscDicRegion region = covertRegion(orgId);
                    one.setRegionId(region.getRegionId());
                    one.setRegionName(region.getRegionName());
                    one.setItemId(itemId);
                    one.setItemName(itemName);
                    one.setApplyinstSource(applySource);
                    one.setStatisticsStartDate(firstDay);
                    one.setStatisticsEndDate(lastDay);
                    one.setStatisticsYear(Integer.valueOf(year));
                    one.setRootOrgId(item.getRootOrgId());
                    //接件数
                    int yearApplyCount = 0;
                    int allApplyCount = 0;
                    //待补正数
                    int allInSupplementCount = 0;
                    //补正待确认数
                    int allSupplementedCount = 0;
                    //受理数
                    int yearAcceptanceCount = 0;
                    int allAcceptanceCount = 0;
                    //不予受理数
                    int yearOutScopeCount = 0;
                    int allOutScopeCount = 0;
                    //办结数
                    int yearCompletedCount = 0;
                    int allCompletedCount = 0;
                    //特殊程序数
                    int yearSpecificRoutineCount = 0;
                    int allSpecificRoutineCount = 0;
                    //预警数
                    int allWariningCount = 0;
                    //逾期数
                    int yearOverTimeCount = 0;
                    int allOverTimeCount = 0;

                    for (int k = 0, length = monthList.size(); k < length; k++) {
                        AeaAnaOrgMonthStatistics monthStatistics = monthList.get(k);
                        yearApplyCount += monthStatistics.getMonthApplyCount();
                        yearAcceptanceCount += monthStatistics.getMonthAcceptanceCount();
                        yearOutScopeCount += monthStatistics.getMonthOutScopeCount();
                        yearCompletedCount += monthStatistics.getMonthCompletedCount();
                        yearSpecificRoutineCount += monthStatistics.getMonthSpecificRoutineCount();
                        yearOverTimeCount += monthStatistics.getMonthOverTimeCount();
                        allApplyCount = yearApplyCount;
                        allInSupplementCount = monthStatistics.getAllInSupplementCount();
                        allSupplementedCount = monthStatistics.getAllSupplementedCount();
                        allAcceptanceCount = yearAcceptanceCount;
                        allOutScopeCount = yearOutScopeCount;
                        allCompletedCount = yearCompletedCount;
                        allSpecificRoutineCount = yearSpecificRoutineCount;
                        allWariningCount = monthStatistics.getAllWariningCount();
                        allOverTimeCount = yearOverTimeCount;
                        if (monthStatistics.getStatisticsMonth().equals(thisMonth)) {
                            //总数
                            allApplyCount = monthStatistics.getAllApplyCount();
                            allInSupplementCount = monthStatistics.getAllInSupplementCount();
                            allSupplementedCount = monthStatistics.getAllSupplementedCount();
                            allAcceptanceCount = monthStatistics.getAllAcceptanceCount();
                            allOutScopeCount = monthStatistics.getAllOutScopeCount();
                            allCompletedCount = monthStatistics.getAllCompletedCount();
                            allSpecificRoutineCount = monthStatistics.getAllSpecificRoutineCount();
                            allWariningCount = monthStatistics.getAllWariningCount();
                            allOverTimeCount = monthStatistics.getAllOverTimeCount();
                        }
                    }
                    one.setYearApplyCount(yearApplyCount);
                    one.setAllApplyCount(allApplyCount);
                    one.setAllInSupplementCount(allInSupplementCount);
                    one.setAllSupplementedCount(allSupplementedCount);
                    one.setYearAcceptanceCount(yearAcceptanceCount);
                    one.setAllAcceptanceCount(allAcceptanceCount);
                    one.setYearOutScopeCount(yearOutScopeCount);
                    one.setAllOutScopeCount(allOutScopeCount);
                    one.setYearCompletedCount(yearCompletedCount);
                    one.setAllCompletedCount(allCompletedCount);
                    one.setYearSpecificRoutineCount(yearSpecificRoutineCount);
                    one.setAllSpecificRoutineCount(allSpecificRoutineCount);
                    one.setAllWariningCount(allWariningCount);
                    one.setYearOverTimeCount(yearOverTimeCount);
                    one.setAllOverTimeCount(allOverTimeCount);
                    //环比
                    Integer preYearApplyCount = preYearStatistics == null ? 0 : preYearStatistics.getYearApplyCount();
                    Integer preYearAcceptanceCount = preYearStatistics == null ? 0 : preYearStatistics.getYearAcceptanceCount();
                    Integer preYearOutScopeCount = preYearStatistics == null ? 0 : preYearStatistics.getYearOutScopeCount();
                    Integer preYearCompletedCount = preYearStatistics == null ? 0 : preYearStatistics.getYearCompletedCount();
                    Integer preYearSpecificRoutineCount = preYearStatistics == null ? 0 : preYearStatistics.getYearSpecificRoutineCount();
                    Integer preYearOverTimeCount = preYearStatistics == null ? 0 : preYearStatistics.getYearOverTimeCount();
                    one.setApplyLrr(CalculateUtil.calcuLrr(yearApplyCount, preYearApplyCount));
                    one.setAcceptanceLrr(CalculateUtil.calcuLrr(yearAcceptanceCount, preYearAcceptanceCount));
                    one.setOutScopeLrr(CalculateUtil.calcuLrr(yearOutScopeCount, preYearOutScopeCount));
                    one.setCompletedLrr(CalculateUtil.calcuLrr(yearCompletedCount, preYearCompletedCount));
                    one.setSpecificRoutineLrr(CalculateUtil.calcuLrr(yearSpecificRoutineCount, preYearSpecificRoutineCount));
                    one.setOverTimeLrr(CalculateUtil.calcuLrr(yearOverTimeCount, preYearOverTimeCount));
                    //受理率
                    Double allAcceptanceRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allAcceptanceCount / allApplyCount);
                    one.setAllAcceptanceRate(allAcceptanceRate);
                    //不予受理率
                    Double allOutScopeRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOutScopeCount / allApplyCount);
                    one.setAllOutScopeRate(allOutScopeRate);
                    //逾期率
                    Double allOverTimeRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOverTimeCount / allAcceptanceCount);
                    one.setAllOverTimeRate(allOverTimeRate);
                    //办结率
                    Double allCompletedRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allCompletedCount / allAcceptanceCount);
                    one.setAllCompletedRate(allCompletedRate);
                    list.add(one);
                }
            }
        }
        return list;
    }

    private List<AeaAnaOrgMonthStatistics> statisticsOneOrgMonth(String orgId,String orgName,String statisticsRecordId,Date firstDay,Date lastDay,Date today)throws ParseException{
        List<AeaAnaOrgMonthStatistics> list = new ArrayList<>();
        //查询部门下发布和试运行的全部实施事项
        List<AeaItemBasic> itemBasics = aeaItemBasicMapper.listAeaItemBasicByOrgId(orgId);
        if(itemBasics.size() > 0){
            //上一月
            String preMonth = DateUtils.convertDateToString(DateUtils.getPreDateByDate(firstDay), "yyyy-MM");
            //获取本月统计的天数
            Date preDate = DateUtils.getPreDateByDate(today);
            String statisticsMonth = DateUtils.convertDateToString(preDate, "yyyy-MM");
            List<String> dateList = DateUtils.calFirstDay2Date(preDate);
            for(int j=0,leng=applySourceArray.length;j<leng;j++) {
                String applySource = applySourceArray[j];
                for (int i = 0, len = itemBasics.size(); i < len; i++) {
                    AeaItemBasic item = itemBasics.get(i);
                    String itemId = item.getItemId();
                    String itemName = item.getItemName();
                    List<AeaAnaOrgDayStatistics> dayList = aeaAnaOrgDayStatisticsMapper.queryAeaAnaOrgDayStatistics(orgId, itemId, applySource, dateList);
                    if (dayList.size() == 0) {
                        log.debug("事项ID：{}，事项名称：{}，未找到日办件统计数据！", itemId, itemName);
                        continue;
                    }
                    AeaAnaOrgMonthStatistics one = new AeaAnaOrgMonthStatistics();
                    //获取上月统计数据
                    AeaAnaOrgMonthStatistics preMonthStatistics = aeaAnaOrgMonthStatisticsMapper.getAeaAnaOrgMonthStatistics(orgId, itemId,applySource, preMonth);
                    //获取去年当月数据
                    AeaAnaOrgMonthStatistics lastYearMonthStatistics = aeaAnaOrgMonthStatisticsMapper.getAeaAnaOrgMonthStatistics(orgId, itemId, applySource, DateUtils.getLastYearMonth(statisticsMonth));
                    one.setOrgMonthStatisticsId(UUID.randomUUID().toString());
                    one.setStatisticsRecordId(statisticsRecordId);
                    one.setOrgId(orgId);
                    one.setOrgName(orgName);
                    BscDicRegion region = covertRegion(orgId);
                    one.setRegionId(region.getRegionId());
                    one.setRegionName(region.getRegionName());
                    one.setItemId(itemId);
                    one.setItemName(itemName);
                    one.setApplyinstSource(applySource);
                    one.setStatisticsStartDate(firstDay);
                    one.setStatisticsEndDate(lastDay);
                    one.setStatisticsMonth(statisticsMonth);
                    one.setRootOrgId(item.getRootOrgId());
                    //接件数
                    int monthApplyCount = 0;
                    int allApplyCount = 0;
                    //待补正数
                    int allInSupplementCount = 0;
                    //补正待确认数
                    int allSupplementedCount = 0;
                    //受理数
                    int monthAcceptanceCount = 0;
                    int allAcceptanceCount = 0;
                    //不予受理数
                    int monthOutScopeCount = 0;
                    int allOutScopeCount = 0;
                    //办结数
                    int monthCompletedCount = 0;
                    int allCompletedCount = 0;
                    //特殊程序数
                    int monthSpecificRoutineCount = 0;
                    int allSpecificRoutineCount = 0;
                    //预警数
                    int allWariningCount = 0;
                    //逾期数
                    int monthOverTimeCount = 0;
                    int allOverTimeCount = 0;
                    for (int k = 0, length = dayList.size(); k < length; k++) {
                        AeaAnaOrgDayStatistics dayStatistics = dayList.get(k);
                        monthApplyCount += dayStatistics.getDayApplyCount();
                        monthAcceptanceCount += dayStatistics.getDayAcceptanceCount();
                        monthOutScopeCount += dayStatistics.getDayOutScopeCount();
                        monthCompletedCount += dayStatistics.getDayCompletedCount();
                        monthSpecificRoutineCount += dayStatistics.getDaySpecificRoutineCount();
                        monthOverTimeCount += dayStatistics.getDayOverTimeCount();
                        allApplyCount = monthApplyCount;
                        allInSupplementCount = dayStatistics.getAllInSupplementCount();
                        allSupplementedCount = dayStatistics.getAllSupplementedCount();
                        allAcceptanceCount = monthAcceptanceCount;
                        allOutScopeCount = monthOutScopeCount;
                        allCompletedCount = monthCompletedCount;
                        allSpecificRoutineCount = monthSpecificRoutineCount;
                        allWariningCount = dayStatistics.getAllWariningCount();
                        allOverTimeCount = monthOverTimeCount;
                        if (dayStatistics.getStatisticsDate().getTime() == preDate.getTime()) {
                            //总数
                            allApplyCount = dayStatistics.getAllApplyCount();
                            allInSupplementCount = dayStatistics.getAllInSupplementCount();
                            allSupplementedCount = dayStatistics.getAllSupplementedCount();
                            allAcceptanceCount = dayStatistics.getAllAcceptanceCount();
                            allOutScopeCount = dayStatistics.getAllOutScopeCount();
                            allCompletedCount = dayStatistics.getAllCompletedCount();
                            allSpecificRoutineCount = dayStatistics.getAllSpecificRoutineCount();
                            allWariningCount = dayStatistics.getAllWariningCount();
                            allOverTimeCount = dayStatistics.getAllOverTimeCount();
                        }
                    }
                    one.setMonthApplyCount(monthApplyCount);
                    one.setAllApplyCount(allApplyCount);
                    one.setAllInSupplementCount(allInSupplementCount);
                    one.setAllSupplementedCount(allSupplementedCount);
                    one.setMonthAcceptanceCount(monthAcceptanceCount);
                    one.setAllAcceptanceCount(allAcceptanceCount);
                    one.setMonthOutScopeCount(monthOutScopeCount);
                    one.setAllOutScopeCount(allOutScopeCount);
                    one.setMonthCompletedCount(monthCompletedCount);
                    one.setAllCompletedCount(allCompletedCount);
                    one.setMonthSpecificRoutineCount(monthSpecificRoutineCount);
                    one.setAllSpecificRoutineCount(allSpecificRoutineCount);
                    one.setAllWariningCount(allWariningCount);
                    one.setMonthOverTimeCount(monthOverTimeCount);
                    one.setAllOverTimeCount(allOverTimeCount);
                    //环比
                    Integer preMonthApplyCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthApplyCount();
                    Integer preAllInSupplementCount = preMonthStatistics == null ? 0 : preMonthStatistics.getAllInSupplementCount();
                    Integer preAllSupplementedCount = preMonthStatistics == null ? 0 : preMonthStatistics.getAllSupplementedCount();
                    Integer preMonthAcceptanceCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthAcceptanceCount();
                    Integer preMonthOutScopeCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthOutScopeCount();
                    Integer preMonthCompletedCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthCompletedCount();
                    Integer preMonthSpecificRoutineCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthSpecificRoutineCount();
                    Integer preMonthOverTimeCount = preMonthStatistics == null ? 0 : preMonthStatistics.getMonthOverTimeCount();
                    one.setApplyLrr(CalculateUtil.calcuLrr(monthApplyCount, preMonthApplyCount));
                    one.setInSupplementLrr(CalculateUtil.calcuLrr(allInSupplementCount, preAllInSupplementCount));
                    one.setSupplementedLrr(CalculateUtil.calcuLrr(allSupplementedCount, preAllSupplementedCount));
                    one.setAcceptanceLrr(CalculateUtil.calcuLrr(monthAcceptanceCount, preMonthAcceptanceCount));
                    one.setOutScopeLrr(CalculateUtil.calcuLrr(monthOutScopeCount, preMonthOutScopeCount));
                    one.setCompletedLrr(CalculateUtil.calcuLrr(monthCompletedCount, preMonthCompletedCount));
                    one.setSpecificRoutineLrr(CalculateUtil.calcuLrr(monthSpecificRoutineCount, preMonthSpecificRoutineCount));
                    one.setOverTimeLrr(CalculateUtil.calcuLrr(monthOverTimeCount, preMonthOverTimeCount));
                    //同比
                    Integer lastYearMonthApplyCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthApplyCount();
                    Integer lastYearAllInSupplementCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getAllInSupplementCount();
                    Integer lastYearAllSupplementedCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getAllSupplementedCount();
                    Integer lastYearMonthAcceptanceCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthAcceptanceCount();
                    Integer lastYearMonthOutScopeCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthOutScopeCount();
                    Integer lastYearMonthCompletedCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthCompletedCount();
                    Integer lastYearMonthSpecificRoutineCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthSpecificRoutineCount();
                    Integer lastYearMonthOverTimeCount = lastYearMonthStatistics == null ? 0 : lastYearMonthStatistics.getMonthOverTimeCount();
                    one.setApplyOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthApplyCount, lastYearMonthApplyCount));
                    one.setInSupplementOnYoyBasis(CalculateUtil.calcuOnYoyBasis(allInSupplementCount, lastYearAllInSupplementCount));
                    one.setSupplementedOnYoyBasis(CalculateUtil.calcuOnYoyBasis(allSupplementedCount, lastYearAllSupplementedCount));
                    one.setAcceptanceOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthAcceptanceCount, lastYearMonthAcceptanceCount));
                    one.setOutScopeOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthOutScopeCount, lastYearMonthOutScopeCount));
                    one.setCompletedOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthCompletedCount, lastYearMonthCompletedCount));
                    one.setSpecificRoutineOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthSpecificRoutineCount, lastYearMonthSpecificRoutineCount));
                    one.setOverTimeOnYoyBasis(CalculateUtil.calcuOnYoyBasis(monthOverTimeCount, lastYearMonthOverTimeCount));
                    //受理率
                    Double allAcceptanceRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allAcceptanceCount / allApplyCount);
                    one.setAllAcceptanceRate(allAcceptanceRate);
                    //不予受理率
                    Double allOutScopeRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOutScopeCount / allApplyCount);
                    one.setAllOutScopeRate(allOutScopeRate);
                    //逾期率
                    Double allOverTimeRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOverTimeCount / allAcceptanceCount);
                    one.setAllOverTimeRate(allOverTimeRate);
                    //办结率
                    Double allCompletedRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allCompletedCount / allAcceptanceCount);
                    one.setAllCompletedRate(allCompletedRate);
                    list.add(one);
                }
            }
        }
        return list;
    }

    private List<AeaAnaOrgWeekStatistics> statisticsOneOrgWeek(String orgId,String orgName,String statisticsRecordId,Date monday,Date sunday,Date today){
        List<AeaAnaOrgWeekStatistics> list = new ArrayList<>();
        //查询部门下发布和试运行的全部实施事项
        List<AeaItemBasic> itemBasics = aeaItemBasicMapper.listAeaItemBasicByOrgId(orgId);
        if(itemBasics.size() > 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //上周一、上周日
            String preMonday = sdf.format(DateUtils.getThisWeekMonday(DateUtils.getPreDateByDate(monday))) + " 00:00:00";
            String preSunday = sdf.format(DateUtils.getThisWeekSunday(DateUtils.getPreDateByDate(monday))) + " 23:59:59";
            //获取本周统计的天数
            Date preDate = DateUtils.getPreDateByDate(today);
            List<String> dateList = DateUtils.calMonday2Date(preDate);
            int weekNum = DateUtils.getThisWeekNum(preDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(preDate);
            int year = ca.get(Calendar.YEAR);
            for(int j=0,leng=applySourceArray.length;j<leng;j++){
                String applySource = applySourceArray[j];
                for(int i=0,len=itemBasics.size();i<len;i++){
                    AeaItemBasic item = itemBasics.get(i);
                    String itemId = item.getItemId();
                    String itemName = item.getItemName();
                    List<AeaAnaOrgDayStatistics> dayList = aeaAnaOrgDayStatisticsMapper.queryAeaAnaOrgDayStatistics(orgId, itemId,applySource, dateList);
                    if(dayList.size() == 0){
                        log.debug("事项ID：{}，事项名称：{}，未找到日办件统计数据！", itemId,itemName);
                        continue;
                    }
                    AeaAnaOrgWeekStatistics one = new AeaAnaOrgWeekStatistics();
                    //获取上周统计数据
                    AeaAnaOrgWeekStatistics preWeekStatistics = aeaAnaOrgWeekStatisticsMapper.getAeaAnaOrgWeekStatistics(orgId, itemId, preMonday, preSunday,applySource);
                    one.setOrgWeekStatisticsId(UUID.randomUUID().toString());
                    one.setStatisticsRecordId(statisticsRecordId);
                    one.setOrgId(orgId);
                    one.setOrgName(orgName);
                    BscDicRegion region = covertRegion(orgId);
                    one.setRegionId(region.getRegionId());
                    one.setRegionName(region.getRegionName());
                    one.setItemId(itemId);
                    one.setItemName(itemName);
                    one.setApplyinstSource(applySource);
                    one.setStatisticsStartDate(monday);
                    one.setStatisticsEndDate(sunday);
                    one.setWeekNum(weekNum);
                    one.setStatisticsYear(year);
                    one.setRootOrgId(item.getRootOrgId());
                    //接件数
                    int weekApplyCount = 0;
                    int allApplyCount = 0;
                    //待补正数
                    int allInSupplementCount = 0;
                    //补正待确认数
                    int allSupplementedCount = 0;
                    //受理数
                    int weekAcceptanceCount = 0;
                    int allAcceptanceCount = 0;
                    //不予受理数
                    int weekOutScopeCount = 0;
                    int allOutScopeCount = 0;
                    //办结数
                    int weekCompletedCount = 0;
                    int allCompletedCount = 0;
                    //特殊程序数
                    int weekSpecificRoutineCount = 0;
                    int allSpecificRoutineCount = 0;
                    //预警数
                    int allWariningCount = 0;
                    //逾期数
                    int weekOverTimeCount = 0;
                    int allOverTimeCount = 0;
                    for(int k=0,length=dayList.size();k<length;k++){
                        AeaAnaOrgDayStatistics orgDayStatistics = dayList.get(k);
                        weekApplyCount += orgDayStatistics.getDayApplyCount();
                        allApplyCount = weekApplyCount;
                        allInSupplementCount = orgDayStatistics.getAllInSupplementCount();
                        allSupplementedCount = orgDayStatistics.getAllSupplementedCount();
                        weekAcceptanceCount += orgDayStatistics.getDayAcceptanceCount();
                        allAcceptanceCount = weekAcceptanceCount;
                        weekOutScopeCount += orgDayStatistics.getDayOutScopeCount();
                        allOutScopeCount = weekOutScopeCount;
                        weekCompletedCount += orgDayStatistics.getDayCompletedCount();
                        allCompletedCount = weekCompletedCount;
                        weekSpecificRoutineCount += orgDayStatistics.getDaySpecificRoutineCount();
                        allSpecificRoutineCount = weekSpecificRoutineCount;
                        allWariningCount = orgDayStatistics.getAllWariningCount();
                        weekOverTimeCount += orgDayStatistics.getDayOverTimeCount();
                        allOverTimeCount = weekOverTimeCount;
                        if(orgDayStatistics.getStatisticsDate().getTime()==preDate.getTime()){
                            //总数
                            allApplyCount = orgDayStatistics.getAllApplyCount();
                            allInSupplementCount = orgDayStatistics.getAllInSupplementCount();
                            allSupplementedCount = orgDayStatistics.getAllSupplementedCount();
                            allAcceptanceCount = orgDayStatistics.getAllAcceptanceCount();
                            allOutScopeCount = orgDayStatistics.getAllOutScopeCount();
                            allCompletedCount = orgDayStatistics.getAllCompletedCount();
                            allSpecificRoutineCount = orgDayStatistics.getAllSpecificRoutineCount();
                            allWariningCount = orgDayStatistics.getAllWariningCount();
                            allOverTimeCount = orgDayStatistics.getAllOverTimeCount();
                        }
                    }
                    one.setWeekApplyCount(weekApplyCount);
                    one.setAllApplyCount(allApplyCount);
                    one.setAllInSupplementCount(allInSupplementCount);
                    one.setAllSupplementedCount(allSupplementedCount);
                    one.setWeekAcceptanceCount(weekAcceptanceCount);
                    one.setAllAcceptanceCount(allAcceptanceCount);
                    one.setWeekOutScopeCount(weekOutScopeCount);
                    one.setAllOutScopeCount(allOutScopeCount);
                    one.setWeekCompletedCount(weekCompletedCount);
                    one.setAllCompletedCount(allCompletedCount);
                    one.setWeekSpecificRoutineCount(weekSpecificRoutineCount);
                    one.setAllSpecificRoutineCount(allSpecificRoutineCount);
                    one.setAllWariningCount(allWariningCount);
                    one.setWeekOverTimeCount(weekOverTimeCount);
                    one.setAllOverTimeCount(allOverTimeCount);
                    //环比
                    Integer preWeekApplyCount = preWeekStatistics == null?0:preWeekStatistics.getWeekApplyCount();
                    Integer preWeekAcceptanceCount = preWeekStatistics == null?0:preWeekStatistics.getWeekAcceptanceCount();
                    Integer preWeekOutScopeCount = preWeekStatistics == null?0:preWeekStatistics.getWeekOutScopeCount();
                    Integer preWeekCompletedCount = preWeekStatistics == null?0:preWeekStatistics.getWeekCompletedCount();
                    Integer preWeekSpecificRoutineCount = preWeekStatistics == null?0:preWeekStatistics.getWeekSpecificRoutineCount();
                    Integer preWeekOverTimeCount = preWeekStatistics == null?0:preWeekStatistics.getWeekOverTimeCount();
                    one.setApplyLrr(CalculateUtil.calcuLrr(weekApplyCount,preWeekApplyCount));
                    one.setAcceptanceLrr(CalculateUtil.calcuLrr(weekAcceptanceCount,preWeekAcceptanceCount));
                    one.setOutScopeLrr(CalculateUtil.calcuLrr(weekOutScopeCount,preWeekOutScopeCount));
                    one.setCompletedLrr(CalculateUtil.calcuLrr(weekCompletedCount,preWeekCompletedCount));
                    one.setSpecificRoutineLrr(CalculateUtil.calcuLrr(weekSpecificRoutineCount,preWeekSpecificRoutineCount));
                    one.setOverTimeLrr(CalculateUtil.calcuLrr(weekOverTimeCount,preWeekOverTimeCount));
                    //受理率
                    Double allAcceptanceRate = allApplyCount==0?0:CalculateUtil.formatDoubleValue((double) allAcceptanceCount/allApplyCount);
                    one.setAllAcceptanceRate(allAcceptanceRate);
                    //不予受理率
                    Double allOutScopeRate = allApplyCount==0?0:CalculateUtil.formatDoubleValue((double)allOutScopeCount/allApplyCount);
                    one.setAllOutScopeRate(allOutScopeRate);
                    //逾期率
                    Double allOverTimeRate = allAcceptanceCount==0?0:CalculateUtil.formatDoubleValue((double)allOverTimeCount/allAcceptanceCount);
                    one.setAllOverTimeRate(allOverTimeRate);
                    //办结率
                    Double allCompletedRate = allAcceptanceCount==0?0:CalculateUtil.formatDoubleValue((double)allCompletedCount/allAcceptanceCount);
                    one.setAllCompletedRate(allCompletedRate);
                    list.add(one);
                }
            }
        }
        return list;
    }

    /**
     * 因为事项状态变更可能不是一天之内完成的。所以会出现某天统计没有收件却统计有受理等等情况。
     * @param orgId
     * @param orgName
     * @param statisticsRecordId
     * @param statisticsDate
     * @return
     */
    private List<AeaAnaOrgDayStatistics> statisticsOneOrgDay(String orgId, String orgName, String statisticsRecordId, Date statisticsDate)throws Exception{
        List<AeaAnaOrgDayStatistics> list = new ArrayList<>();
        //查询部门下发布和试运行的全部实施事项
        List<AeaItemBasic> itemBasics = aeaItemBasicMapper.listAeaItemBasicByOrgId(orgId);
        if(itemBasics.size() > 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateFormat = sdf.format(statisticsDate);
            String startTime = dateFormat + " 00:00:00";
            String endTime = dateFormat + " 23:59:59";
            String rootOrgId = null;
            //前一天
            String preDate = DateUtils.getPreDateByDate(dateFormat);
            //统计不同来源
            for(int k=0,length = applySourceArray.length;k<length;k++) {
                String applySource = applySourceArray[k];
                for (int i = 0, len = itemBasics.size(); i < len; i++) {
                    AeaAnaOrgDayStatistics one = new AeaAnaOrgDayStatistics();
                    AeaItemBasic itemBasic = itemBasics.get(i);
                    if(StringUtils.isBlank(rootOrgId)){
                        rootOrgId = itemBasic.getRootOrgId();
                    }
                    String itemId = itemBasic.getItemId();
                    AeaAnaOrgDayStatistics preStatistics = aeaAnaOrgDayStatisticsMapper.getAeaAnaOrgDayStatistics(orgId, itemId, preDate, applySource);
                    one.setOrgDayStatisticsId(UUID.randomUUID().toString());
                    one.setStatisticsRecordId(statisticsRecordId);
                    one.setOrgId(orgId);
                    one.setOrgName(orgName);
                    one.setItemId(itemId);
                    one.setItemName(itemBasic.getItemName());
                    one.setApplyinstSource(applySource);
                    one.setStatisticsDate(statisticsDate);
                    one.setRootOrgId(itemBasic.getRootOrgId());
                    BscDicRegion region = covertRegion(orgId);
                    if (region != null) {
                        one.setRegionId(region.getRegionId());
                        one.setRegionName(region.getRegionName());
                    }
                    //日接件数
                    int dayApplyCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.RECEIVE_APPLY.getValue(), orgId, applySource, startTime, endTime);
                    one.setDayApplyCount(dayApplyCount);
                    int allApplyCount = aeaHiIteminstMapper.countTotalIteminst(itemId, orgId, null, applySource);
                    one.setAllApplyCount(allApplyCount);
                    Integer preDayApplyCount = preStatistics == null ? 0 : preStatistics.getDayApplyCount();
                    one.setApplyLrr(CalculateUtil.calcuLrr(dayApplyCount, preDayApplyCount));
                    //待补正数
                    int allInSupplementCount = aeaHiIteminstMapper.countSupplementIteminst(itemId, orgId, ItemStatus.CORRECT_MATERIAL_START.getValue());
                    one.setAllInSupplementCount(allInSupplementCount);
                    //补正待确认数
                    int allSupplementedCount = aeaHiIteminstMapper.countSupplementIteminst(itemId, orgId, "7");
                    one.setAllSupplementedCount(allSupplementedCount);
                    //日受理数
                    int dayAcceptanceCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.DEPARTMENT_DEAL_START.getValue(), orgId, applySource,  startTime, endTime);
                    one.setDayAcceptanceCount(dayAcceptanceCount);
                    int allAcceptanceCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.DEPARTMENT_DEAL_START.getValue(), orgId, applySource,  null, null);
                    one.setAllAcceptanceCount(allAcceptanceCount);
                    Integer preDayAcceptanceCount = preStatistics == null ? 0 : preStatistics.getDayAcceptanceCount();
                    one.setAcceptanceLrr(CalculateUtil.calcuLrr(dayAcceptanceCount, preDayAcceptanceCount));
                    //日不予受理数
                    int dayOutScopeCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.OUT_SCOPE.getValue(), orgId, applySource,  startTime, endTime);
                    one.setDayOutScopeCount(dayOutScopeCount);
                    int allOutScopeCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.OUT_SCOPE.getValue(), orgId, applySource,  null, null);
                    one.setAllOutScopeCount(allOutScopeCount);
                    Integer preDayOutScopeCount = preStatistics == null ? 0 : preStatistics.getDayOutScopeCount();
                    one.setOutScopeLrr(CalculateUtil.calcuLrr(dayOutScopeCount, preDayOutScopeCount));
                    //日办结数
                    String[] states = {ItemStatus.AGREE.getValue(), ItemStatus.AGREE_TOLERANCE.getValue(), ItemStatus.DISAGREE.getValue()};
                    int dayCompletedCount = aeaHiIteminstMapper.countCompletedIteminst(itemId, states, orgId, startTime, endTime,applySource);
                    one.setDayCompletedCount(dayCompletedCount);
                    int allCompletedCount = aeaHiIteminstMapper.countCompletedIteminst(itemId, states, orgId, null, null,applySource);
                    one.setAllCompletedCount(allCompletedCount);
                    Integer preDayCompletedCount = preStatistics == null ? 0 : preStatistics.getDayCompletedCount();
                    one.setCompletedLrr(CalculateUtil.calcuLrr(dayCompletedCount, preDayCompletedCount));
                    //特殊程序数
                    int daySpecificRoutineCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.SPECIFIC_PROC_START.getValue(), applySource,  orgId, startTime, endTime);
                    one.setDaySpecificRoutineCount(daySpecificRoutineCount);
                    int allSpecificRoutineCount = aeaHiIteminstMapper.countApproveIteminst(itemId, ItemStatus.SPECIFIC_PROC_START.getValue(), applySource,  orgId, null, null);
                    one.setAllSpecificRoutineCount(allSpecificRoutineCount);
                    Integer preDaySpecificRoutineCount = preStatistics == null ? 0 : preStatistics.getDaySpecificRoutineCount();
                    one.setSpecificRoutineLrr(CalculateUtil.calcuLrr(daySpecificRoutineCount, preDaySpecificRoutineCount));
                    //预警数
                    //查询部门受理但未办结的办件（事项状态为：3、6、7、8、9、10）
                    AeaHiIteminst search = new AeaHiIteminst();
                    search.setItemId(itemId);
                    search.setApproveOrgId(orgId);
                    search.setApplySource(applySource);
                    String[] states2 = {ItemStatus.ACCEPT_DEAL.getValue(), ItemStatus.CORRECT_MATERIAL_START.getValue(), ItemStatus.CORRECT_MATERIAL_END.getValue(),
                            ItemStatus.DEPARTMENT_DEAL_START.getValue(), ItemStatus.SPECIFIC_PROC_START.getValue(), ItemStatus.SPECIFIC_PROC_END.getValue()};
                    search.setQueryIteminstStates(CalculateUtil.getQueryFieldValue(states2));
                    List<AeaHiIteminst> wariningList = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
                    one.setAllWariningCount(getStateCount(wariningList, "2",null,rootOrgId));
                    //日逾期数
                    //通过时限计算实例表确定当天的逾期数量
                    String[] states3 = {ItemStatus.ACCEPT_DEAL.getValue(), ItemStatus.CORRECT_MATERIAL_START.getValue(), ItemStatus.CORRECT_MATERIAL_END.getValue(),
                            ItemStatus.DEPARTMENT_DEAL_START.getValue(), ItemStatus.SPECIFIC_PROC_START.getValue(), ItemStatus.SPECIFIC_PROC_END.getValue(),
                            ItemStatus.AGREE.getValue(), ItemStatus.AGREE_TOLERANCE.getValue(), ItemStatus.DISAGREE.getValue()};
                    search.clearQueryParam();
                    search.setQueryIteminstStates(CalculateUtil.getQueryFieldValue(states3));
                    List<AeaHiIteminst> overtimeList = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
                    Integer allOverTimeCount = getStateCount(overtimeList, "3",null,rootOrgId);
                    Integer dayOverTimeCount = getStateCount(overtimeList, "3",statisticsDate,rootOrgId);
                    one.setDayOverTimeCount(dayOverTimeCount);
                    one.setAllOverTimeCount(allOverTimeCount);
                    Integer preDayOverTimeCount = preStatistics == null ? 0 : preStatistics.getDayOverTimeCount();
                    one.setOverTimeLrr(CalculateUtil.calcuLrr(dayOverTimeCount, preDayOverTimeCount));
                    //总受理率
                    Double allAcceptanceRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allAcceptanceCount / allApplyCount);
                    one.setAllAcceptanceRate(allAcceptanceRate);
                    //总不予受理率
                    Double allOutScopeRate = allApplyCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOutScopeCount / allApplyCount);
                    one.setAllOutScopeRate(allOutScopeRate);
                    //总逾期率
                    Double allOverTimeRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allOverTimeCount / allAcceptanceCount);
                    one.setAllOverTimeRate(allOverTimeRate);
                    //总办结率
                    Double allCompletedRate = allAcceptanceCount == 0 ? 0 : CalculateUtil.formatDoubleValue((double) allCompletedCount / allAcceptanceCount);
                    one.setAllCompletedRate(allCompletedRate);
                    list.add(one);
                }
            }
        }
        return list;
    }

    /**
     * 创建或更新统计数据生成记录
     * @param reportId
     * @param reportName
     * @param startTime
     * @param endTime
     * @param operateSource
     * @param statisticsType
     * @param creater
     * @param rootOrgId
     * @throws ParseException
     */
    public AeaAnaStatisticsRecord buildStatisticsRecord(String reportId,String reportName,String startTime,String endTime,
                                                         String operateSource,String statisticsType,String creater,String rootOrgId)throws ParseException{
        AeaAnaStatisticsRecord  record = aeaAnaStatisticsRecordMapper.getAeaAnaStatisticsRecord(reportId,reportName,startTime,endTime,operateSource,statisticsType,creater,rootOrgId);
        if(record == null){
            record = createStatisticsRecord(reportId,reportName,startTime,endTime,operateSource,statisticsType,creater,rootOrgId);
        }else{
            Date createTIme = new Date();
            record.setCreateTime(createTIme);
            record.setModifier(creater);
            record.setModifyTime(createTIme);
            aeaAnaStatisticsRecordMapper.updateAeaAnaStatisticsRecord(record);
        }
        return record;
    }

    /**
     * 区县以下的区域换算为区县级区域
     * @param orgId
     * @return
     */
    private BscDicRegion covertRegion(String orgId){
        BscDicRegion dicRegion = bscDicRegionMapper.selectRegionByOrgId(orgId);
        if(dicRegion != null && "s".equals(dicRegion.getRegionType())){
            //换算为区县级区域
            dicRegion = bscDicRegionMapper.getBscDicRegionById(dicRegion.getParentId());
            while (!"a".equals(dicRegion.getRegionType())){
                dicRegion = bscDicRegionMapper.getBscDicRegionById(dicRegion.getParentId());
            }
        }
        return dicRegion;
    }

    /**
     * 创建系统生成记录
     * @param rootOrgId
     * @return
     * @throws ParseException
     */
    private AeaAnaStatisticsRecord createStatisticsRecord(String reportId,String reportName,String startTime,String endTime,
                                                          String operateSource,String statisticsType,String creater,String rootOrgId) throws ParseException{
        AeaAnaStatisticsRecord record = new AeaAnaStatisticsRecord();
        record.setStatisticsRecordId(UUID.randomUUID().toString());
        record.setReportId(reportId);
        record.setReportName(reportName);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        record.setStatisticsStartDate(format.parse(startTime));
        record.setStatisticsEndDate(format.parse(endTime));
        record.setOperateSource(operateSource);
        record.setStatisticsType(statisticsType);
        record.setCreater(creater);
        record.setCreateTime(new Date());
        record.setRootOrgId(rootOrgId);
        aeaAnaStatisticsRecordMapper.insertAeaAnaStatisticsRecord(record);
        return record;
    }

    /**
     * 根据事项实例集合返回预警或逾期办件数量
     * @param list
     * @param state 2返回预警数， 3返回逾期数
     * @return
     * @throws Exception
     */
    @Deprecated
    private int getStateCount(List<AeaHiIteminst> list,String state) {
        int count = 0;
        for(int i=0,len=list.size();i<len; i++){
            AeaHiIteminst iteminst = list.get(i);
            boolean boo = checkApplyTimeState(iteminst.getIteminstId(),state,iteminst.getRootOrgId());
            if(boo){
                count++;
            }
        }
        return count;
    }

    /**
     * 查询办件时限状态
     * @param iteminstId 事项实例ID
     * @param state     查询的状态值 1表示正常，2表示预警，3表示逾期
     * @param rootOrgId 所属顶级组织
     * @return
     */
    private boolean checkApplyTimeState(String iteminstId, String state, String rootOrgId) {
        boolean boo = false;
        try {
            ActStoTimeruleInst timeruleInst = actStoTimeruleInstMapper.getProcessinstTimeruleInstByIteminstId(iteminstId, rootOrgId);
            if(timeruleInst != null){
                boo = state.equals(timeruleInst.getInstState());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return boo;
    }

    /**
     * 根据事项实例集合返回预警或逾期办件数量
     * @param list
     * @param state 2返回预警数， 3返回逾期数
     * @param statisticsDate 统计日期。查询某天逾期的办件数使用
     * @return
     */
    private int getStateCount(List<AeaHiIteminst> list,String state,Date statisticsDate,String rootOrgId) throws Exception{
        int count = 0;
        List<String> iteminstIdList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(int i=0,len=list.size();i<len; i++){
                String iteminstId = list.get(i).getIteminstId();
                iteminstIdList.add(iteminstId);
            }
            List<ActStoTimeruleInst> timeruleInstList = actStoTimeruleInstMapper.listProcessinstTimeruleInst(iteminstIdList.toArray(new String[iteminstIdList.size()]), rootOrgId);
            if(timeruleInstList != null && timeruleInstList.size() > 0){
                SimpleDateFormat sdf = statisticsDate==null?null:new SimpleDateFormat("yyyy-MM-dd");
                for(int i=0,len=timeruleInstList.size();i<len;i++){
                    ActStoTimeruleInst timeruleInst = timeruleInstList.get(i);
                    if(state.equals(timeruleInst.getInstState())){
                        count = statisticsDate == null?count+1:(timeruleInst.getOverdueDate()!= null&&sdf.format(statisticsDate).equals(sdf.format(timeruleInst.getOverdueDate())))?count+1:count;
                    }
                }
            }
        }
        return count;
    }
}
