package com.augurit.aplanmis.front.third.logistics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class LogisticsConfig {

    /**
     * 如果没有实现第三方的物流接口，则使用默认的事项， 默认实现只会返回静态的假数据
     */
    @Bean
    @Transactional
    @ConditionalOnMissingBean(LogisticsService.class)
    public LogisticsService logisticsService() {
        return new DefaultLogisticsServiceImpl();
    }
}
