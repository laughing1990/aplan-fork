package com.augurit.efficiency.job;


import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.efficiency.service.AeaAnaWinUseTimeStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * bsc定时器效能督查统计入口
 */
@Component
public class WinUseTimeStatisticsJob extends JobTimer{
    private static final Logger logger = LoggerFactory.getLogger(WinUseTimeStatisticsJob.class);

    @Autowired
    private AeaAnaWinUseTimeStatisticService aeaAnaWinUseTimeStatisticService;

    @Value("${dg.sso.access.platform.org.top-org-id:}")
    private String rootOrgId;

    /**
     * 窗口申报最长延期及最短用时统计
     * @throws Exception
     */
    public void winUseTimeStatisticsJob()throws Exception{
        aeaAnaWinUseTimeStatisticService.doWinUseTimeStatistics(rootOrgId,"sys_admin_job",null,null);
    }

}
