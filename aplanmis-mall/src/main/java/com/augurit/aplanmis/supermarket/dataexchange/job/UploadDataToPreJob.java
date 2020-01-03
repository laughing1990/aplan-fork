package com.augurit.aplanmis.supermarket.dataexchange.job;

import com.augurit.agcloud.bsc.domain.BscJobTimer;
import com.augurit.agcloud.bsc.mapper.BscJobTimerMapper;
import com.augurit.aplanmis.supermarket.dataexchange.config.UploadDataConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 市同步信息到前置库定时任务
 */
//@Component
@Slf4j
public class UploadDataToPreJob {//extends JobTimer
    @Autowired
    private UploadDataConfig uploadDataConfig;
    @Autowired
    private BscJobTimerMapper bscJobTimerMapper;


    private String timeCron;
    //在省厅配置的市级机构ID
    private String cityOrgId;

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
        boolean first = uploadDataConfig.isFirst();
        if (first) {

        } else {

        }
        //
        //第一次
//        boolean firstUpload = databaseConfig.isFirstUpload();
        /*if(firstUpload){//第一次同步
            //第一次需要同步 中介单位，联系人，关联表
        }*/
    }


    public String testConfig() {
        return uploadDataConfig.toString();
    }

    private BscJobTimer createBscJobTimer() {
        BscJobTimer timer = new BscJobTimer();
        timer.setTimerId("af41f631-04e9-4f24-9b65-4aa21536262b");
        timer.setTimerName("市上传中介超市数据到前置库");
        timer.setTimerDesc("市上传中介超市数据到前置库，每天凌晨1点开始统计");
        timer.setTimerBeanId("uploadDataToPreJob");
        timer.setTimerClass("UploadDataToPreJob");
        timer.setTimerMethod("uploadDataToPreDatabase");
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
        timer.setTimerId("af41f631-04e9-4f24-9b65-4aa21536262b");
        timer.setTimerName("市上传中介超市数据到前置库");
        timer.setTimerDesc("市上传中介超市数据到前置库，每天凌晨1点开始统计");
        timer.setTimerBeanId("uploadDataToPreJob");
        timer.setTimerClass("UploadDataToPreJob");
        timer.setTimerMethod("uploadDataToPreDatabase");
        if (StringUtils.isEmpty(timeCron)) {
            timer.setTimerCron("0 0 1 * * ?");
        } else {
            timer.setTimerCron(timeCron);
        }
        timer.setIsActive(isActive);
        return timer;
    }
}
