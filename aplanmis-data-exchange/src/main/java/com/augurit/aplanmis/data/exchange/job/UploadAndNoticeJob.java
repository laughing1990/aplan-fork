package com.augurit.aplanmis.data.exchange.job;

import com.augurit.agcloud.bsc.sc.job.timer.JobTimer;
import com.augurit.aplanmis.data.exchange.notice.CityNoticeProperties;
import com.augurit.aplanmis.data.exchange.notice.CityUploadSuccessNotice;
import com.augurit.aplanmis.data.exchange.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 * @Date 2019/7/29
 */
@Component
public class UploadAndNoticeJob extends JobTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadAndNoticeJob.class);

    @Autowired
    CityNoticeProperties cityNoticeProperties;

    @Autowired
    CityUploadSuccessNotice cityUploadSuccessNotice;

    @Autowired
    ImportService importService;

    public void uploadAndNotice() {
        try {
            importService.incrementAllTable();
            LOGGER.info("数据上传成功");
            if (cityNoticeProperties.isOpen()) {
                cityUploadSuccessNotice.notice();
                LOGGER.info("所有通知接口调用成功");
            }
        } catch (Exception e) {
            LOGGER.error("上传数据或调用接口出错");
            e.printStackTrace();
        }
    }
}
