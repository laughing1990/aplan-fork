package com.augurit.aplanmis.supermarket.dataexchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 从前置库同步数据配置
 */
@Component
@ConfigurationProperties(prefix = "synchronise.data")
@Data
public class SynchroniseDataConfig {
    private boolean open;
    private String provinceCityCode;
    private String localCityCode;
    private String timeCorn;
}
