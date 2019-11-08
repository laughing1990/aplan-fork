package com.augurit.efficiency.job;


import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.efficiency.constant.OperateSource;
import com.augurit.efficiency.service.impl.AeaAnaOrgStatisticsImpl;
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
public class OrgStatisticsJob extends JobTimer{
    private static final Logger logger = LoggerFactory.getLogger(OrgStatisticsJob.class);

    @Autowired
    private AeaAnaOrgStatisticsImpl aeaAnaOrgStatisticsImpl;

    @Value("${dg.sso.access.platform.org.top-org-id:}")
    private String rootOrgId;

    /**
     * 部门办件统计
     * @throws Exception
     */
    public void orgStatisticsJob()throws Exception{
        //1、统计日办件
        aeaAnaOrgStatisticsImpl.statisticsOrgDayData(rootOrgId, OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //2、统计周办件
        aeaAnaOrgStatisticsImpl.statisticsOrgWeekData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //3、统计月办件
        aeaAnaOrgStatisticsImpl.statisticsOrgMonthData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
        //4、统计年办件
        aeaAnaOrgStatisticsImpl.statisticsOrgYearData(rootOrgId,OperateSource.SYS_AUTO_CREATE.getValue(),null,new Date());
    }
}
