package com.augurit.aplanmis.supermarket.dataexchange.job;

import com.augurit.agcloud.bsc.domain.BscJobTimer;
import com.augurit.agcloud.bsc.mapper.BscJobTimerMapper;
import com.augurit.aplanmis.supermarket.dataexchange.config.SynchroniseDataConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 从前置库同步数据到省中介超市数据库
 */
@Component
@Slf4j
public class SynchroniseFromPreJob {

    @Autowired
    private BscJobTimerMapper bscJobTimerMapper;
    @Autowired
    private SynchroniseDataConfig synchroniseDataConfig;
    private String timeCron;

    //初始化timer
    @PostConstruct
    void init() throws Exception {
        boolean open = synchroniseDataConfig.isOpen();
        timeCron = synchroniseDataConfig.getTimeCorn();
//        cityOrgId = synchroniseDataConfig.getCityOrgId();
        if (open) {
            BscJobTimer timer = bscJobTimerMapper.getBscJobTimerById("f1b15131-e3e2-4a17-ae7a-0449232d18b3");
            if (null == timer) {
                timer = createBscJobTimer();
                bscJobTimerMapper.insertBscJobTimer(timer);
                log.info("insert job province supermarket success");
            } else {
                timer = this.updateBscJobTimer("1");
                bscJobTimerMapper.updateBscJobTimer(timer);
            }
        } else {
            BscJobTimer timer = this.updateBscJobTimer("0");
            bscJobTimerMapper.updateBscJobTimer(timer);
        }
    }

    /**
     * 省厅从前置库同步数据
     */
    public void synchroniseDataFromPreJob() {
        boolean open = synchroniseDataConfig.isOpen();
        if (!open) return;


    }


    private BscJobTimer createBscJobTimer() {
        BscJobTimer timer = new BscJobTimer();
        timer.setTimerId("f1b15131-e3e2-4a17-ae7a-0449232d18b3");
        timer.setTimerName("中介超市省厅从前置库同步地市数据");
        timer.setTimerDesc("省统建中介超市统计定时器，每天凌晨3点开始统计");
        timer.setTimerBeanId("provinceSynchroniseDataJobDataService");
        timer.setTimerClass("ProvinceSynchroniseDataJobDataService");
        timer.setTimerMethod("synchroniseDataJob");
        if (StringUtils.isEmpty(timeCron)) {
            timer.setTimerCron("0 0 1 * * ?");
        } else {
            timer.setTimerCron(timeCron);
        }
        timer.setRumLock("1");
        timer.setRunEndStatus("0");
        timer.setRunException("");
        timer.setBussFlag("aplanmis-mall:supermarket");
        timer.setIsActive("1");
        return timer;
    }

    private BscJobTimer updateBscJobTimer(String isActive) {
        BscJobTimer timer = new BscJobTimer();
        timer.setTimerId("f1b15131-e3e2-4a17-ae7a-0449232d18b3");
        timer.setTimerName("中介超市省厅从前置库同步地市数据");
        timer.setTimerDesc("省统建中介超市统计定时器，每天凌晨3点开始统计");
        timer.setTimerBeanId("provinceSynchroniseDataJobDataService");
        timer.setTimerClass("ProvinceSynchroniseDataJobDataService");
        timer.setTimerMethod("synchroniseDataJob");
        if (StringUtils.isEmpty(timeCron)) {
            timer.setTimerCron("0 0 1 * * ?");
        } else {
            timer.setTimerCron(timeCron);
        }
        timer.setIsActive(isActive);
        return timer;
    }
}
