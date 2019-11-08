//package com.augurit.efficiency.job;
//
//
//import com.augurit.efficiency.constant.OperateSource;
//import com.augurit.efficiency.service.AeaAnaOrgUseTimeStatisticService;
//import com.augurit.efficiency.service.AeaAnaWinUseTimeStatisticService;
//import com.augurit.efficiency.service.impl.AeaAnaOrgStatisticsImpl;
//import com.augurit.efficiency.service.impl.AeaAnaThemeStatisticsImpl;
//import com.augurit.efficiency.service.impl.AeaAnaWinStatisticsImpl;
//import com.augurit.efficiency.utils.JobUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * 办件统计
// */
//@Component
//@Configurable
//@EnableScheduling
//@ConditionalOnProperty(prefix = "statistics.job",name = "enable",havingValue = "true", matchIfMissing = false)
//public class StatisticsJob {
//    private static final Logger logger = LoggerFactory.getLogger(StatisticsJob.class);
//
//    private final AtomicBoolean orgStatisticsJobRuning = new AtomicBoolean(false);
//    private final AtomicBoolean themeStatisticsJobRuning = new AtomicBoolean(false);
//    private final AtomicBoolean winStatisticsJobRuning = new AtomicBoolean(false);
//    private final AtomicBoolean winUseTimeStatisticsJobRuning = new AtomicBoolean(false);
//    private final AtomicBoolean orgUseTimeStatisticsJobRuning = new AtomicBoolean(false);
//
//    @Autowired
//    private AeaAnaOrgStatisticsImpl aeaAnaOrgStatisticsImpl;
//    @Autowired
//    private AeaAnaThemeStatisticsImpl aeaAnaThemeStatisticsImpl;
//    @Autowired
//    private AeaAnaWinStatisticsImpl aeaAnaWinStatisticsImpl;
//    @Autowired
//    private AeaAnaWinUseTimeStatisticService aeaAnaWinUseTimeStatisticService;
//    @Autowired
//    private AeaAnaOrgUseTimeStatisticService aeaAnaOrgUseTimeStatisticService;
//
//    @Value("${dg.sso.access.platform.org.top-org-id:}")
//    private String rootOrgId;
//
//    @Scheduled(cron = "${orgStatistics.job.schedule:0 0 3 * * ?}")
//    @Async
//    public void orgDayStatistics() {
//        JobUtil.doJob(orgStatisticsJobRuning, "部门办件统计定时器", ()->orgStatisticsJob(),logger);
//    }
//
//    @Scheduled(cron = "${themeStatistics.job.schedule:0 0 3 * * ?}")
//    @Async
//    public void themeDayStatistics() {
//        JobUtil.doJob(themeStatisticsJobRuning, "主题申报统计定时器", ()->themeStatisticsJob(),logger);
//    }
//
//    @Scheduled(cron = "${winStatistics.job.schedule:0 0 3 * * ?}")
//    @Async
//    public void winDayStatistics() {
//        JobUtil.doJob(winStatisticsJobRuning, "窗口申报统计定时器", ()->winStatisticsJob(),logger);
//    }
//
//    @Scheduled(cron = "${winUseTimeStatistics.job.schedule:0 0 3 * * ?}")
//    @Async
//    public void winUseTimeStatistics() {
//        JobUtil.doJob(winUseTimeStatisticsJobRuning, "统计窗口申报最长延期及最短用时", ()->winUseTimeStatisticsJob(),logger);
//    }
//
//    @Scheduled(cron = "${orgUseTimeStatistics.job.schedule:0 0 3 * * ?}")
//    @Async
//    public void orgUseTimeStatistics() {
//        JobUtil.doJob(orgUseTimeStatisticsJobRuning, "统计部门办件最长延期及最短用时", ()->orgUseTimeStatisticsJob(),logger);
//    }
//
//    private void orgStatisticsJob()throws Exception{
//
//        //1、统计日办件
//        aeaAnaOrgStatisticsImpl.statisticsOrgDayData(rootOrgId, OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //2、统计周办件
//        aeaAnaOrgStatisticsImpl.statisticsOrgWeekData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //3、统计月办件
//        aeaAnaOrgStatisticsImpl.statisticsOrgMonthData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //4、统计年办件
//        aeaAnaOrgStatisticsImpl.statisticsOrgYearData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//    }
//
//    private void themeStatisticsJob()throws Exception{
//        //1、统计日办件
//        aeaAnaThemeStatisticsImpl.statisticsThemeDayData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //2、统计周办件
//        aeaAnaThemeStatisticsImpl.statisticsThemeWeekData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //3、统计月办件
//        aeaAnaThemeStatisticsImpl.statisticsThemeMonthData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //4、统计年办件
//        aeaAnaThemeStatisticsImpl.statisticsThemeYearData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//    }
//
//    private void winStatisticsJob()throws Exception{
//        //1、统计日办件
//        aeaAnaWinStatisticsImpl.statisticsWinDayData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //2、统计周办件
//        aeaAnaWinStatisticsImpl.statisticsWinWeekData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //3、统计月办件
//        aeaAnaWinStatisticsImpl.statisticsWinMonthData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//        //4、统计年办件
//        aeaAnaWinStatisticsImpl.statisticsWinYearData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
//    }
//
//    private void winUseTimeStatisticsJob()throws Exception{
//        aeaAnaWinUseTimeStatisticService.doWinUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
//    }
//
//    private void orgUseTimeStatisticsJob()throws Exception{
//        aeaAnaOrgUseTimeStatisticService.doUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
//    }
//
//}
