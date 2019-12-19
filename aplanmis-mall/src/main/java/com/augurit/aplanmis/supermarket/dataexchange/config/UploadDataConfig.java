package com.augurit.aplanmis.supermarket.dataexchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传数据配置
 */
@Component
@ConfigurationProperties(prefix = "upload.data")
@Data
public class UploadDataConfig {
    private boolean open;
    private String provinceCityCode;
    private String localCityCode;
    private String timeCorn;
}
