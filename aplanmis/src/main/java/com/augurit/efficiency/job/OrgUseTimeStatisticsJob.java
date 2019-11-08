package com.augurit.efficiency.job;


import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.efficiency.service.AeaAnaOrgUseTimeStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * bsc定时器效能督查统计入口
 */
@Component
public class OrgUseTimeStatisticsJob extends JobTimer{
    private static final Logger logger = LoggerFactory.getLogger(OrgUseTimeStatisticsJob.class);

    @Autowired
    private AeaAnaOrgUseTimeStatisticService aeaAnaOrgUseTimeStatisticService;

    @Value("${dg.sso.access.platform.org.top-org-id:}")
    private String rootOrgId;

    /**
     * 部门办件最长延期及最短用时统计
     * @throws Exception
     */
    public void orgUseTimeStatisticsJob()throws Exception{
        aeaAnaOrgUseTimeStatisticService.doUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
    }

}
