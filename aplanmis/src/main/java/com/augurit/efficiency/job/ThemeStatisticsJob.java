package com.augurit.efficiency.job;


import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.efficiency.constant.OperateSource;
import com.augurit.efficiency.service.impl.AeaAnaThemeStatisticsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * bsc定时器效能督查统计入口
 */
@Component
public class ThemeStatisticsJob extends JobTimer{
    private static final Logger logger = LoggerFactory.getLogger(ThemeStatisticsJob.class);

    @Autowired
    private AeaAnaThemeStatisticsImpl aeaAnaThemeStatisticsImpl;

    @Value("${dg.sso.access.platform.org.top-org-id:}")
    private String rootOrgId;

    /**
     * 主题申报统计
     * @throws Exception
     */
    public void themeStatisticsJob()throws Exception{
        //1、统计日办件
        aeaAnaThemeStatisticsImpl.statisticsThemeDayData(rootOrgId, OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //2、统计周办件
        aeaAnaThemeStatisticsImpl.statisticsThemeWeekData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //3、统计月办件
        aeaAnaThemeStatisticsImpl.statisticsThemeMonthData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //4、统计年办件
        aeaAnaThemeStatisticsImpl.statisticsThemeYearData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
    }

}
