package com.augurit.aplanmis.supermarket.dataexchange.job;

import com.augurit.agcloud.bsc.domain.BscJobTimer;
import com.augurit.agcloud.bsc.mapper.BscJobTimerMapper;
import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.aplanmis.supermarket.dataexchange.config.UploadDataConfig;
import com.augurit.aplanmis.supermarket.dataexchange.dataExchangeJobService.DataExchangeJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 市同步信息到前置库定时任务
 */
@Component
@Slf4j
public class UploadDataToPreJob extends JobTimer {

    @Autowired
    private UploadDataConfig uploadDataConfig;
    @Autowired
    private BscJobTimerMapper bscJobTimerMapper;
    @Autowired
    private DataExchangeJobService dataExchangeJobService;

    private String timeCron;

    //初始化timer
    @PostConstruct
    void init() throws Exception {
        boolean open = uploadDataConfig.isOpen();
        timeCron = uploadDataConfig.getTimeCorn();
        if (open) {
            BscJobTimer timer = bscJobTimerMapper.getBscJobTimerById("af41f631-04e9-4f24-9b65-4aa21536262b");
            if (null == timer) {
                timer = createBscJobTimer();
                bscJobTimerMapper.insertBscJobTimer(timer);
                log.info("insert job city upload supermarket  success");
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
     * 同步数据到前置库---目前只同步单位及联系人信息，服务类型
     */
    public void uploadDataToPreDatabase() {
        try {
            dataExchangeJobService.uploadDataToPreDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("upload data fail");
        }
    }


    public String testConfig() {
        return uploadDataConfig.toString();
    }

    private BscJobTimer createBscJobTimer() {
        BscJobTimer timer = new BscJobTimer();
        timer.setTimerId("af41f631-04e9-4f24-9b65-4aa21536262b");
        timer.setTimerName("省厅中介超市上传数据到前置库");
        timer.setTimerDesc("省厅中介超市上传数据到前置库，每天凌晨1点开始统计");
        timer.setTimerBeanId("uploadDataToPreJob");
        timer.setTimerClass("UploadDataToPreJob");
        timer.setTimerMethod("uploadDataToPreDatabase");
        if (StringUtils.isEmpty(timeCron)) {
            timer.setTimerCron("0 */5 * * * ?");
        } else {
            timer.setTimerCron(timeCron);
        }
        timer.setRumLock("1");
        timer.setRunEndStatus("0");
        timer.setRunException("");
        timer.setBussFlag("aplanmis-mall");
        timer.setIsActive("1");
        return timer;
    }

    private BscJobTimer updateBscJobTimer(String isActive) {
        BscJobTimer timer = new BscJobTimer();
        timer.setTimerId("af41f631-04e9-4f24-9b65-4aa21536262b");
        timer.setTimerName("省厅中介超市上传数据到前置库");
        timer.setTimerDesc("省厅中介超市上传数据到前置库，每天凌晨1点开始统计");
        timer.setTimerClass("UploadDataToPreJob");
        timer.setTimerBeanId("uploadDataToPreJob");
        timer.setTimerMethod("uploadDataToPreDatabase");
        if (StringUtils.isEmpty(timeCron)) {
            timer.setTimerCron("0 */5 * * * ?");
        } else {
            timer.setTimerCron(timeCron);
        }
        timer.setIsActive(isActive);
        return timer;
    }
}
