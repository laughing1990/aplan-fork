package com.augurit.aplanmis.common.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)
//@ConditionalOnExpression("${data.exchange.open:false}")
@Slf4j
public class DataSourceAop {
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Before("execution(* com.augurit.aplanmis.supermarket.dataexchange.service.*.*(..))")
    public void setProvinceDB() {

        log.info("切换到前置数据库：");
        DataSourceType.setDB(DataSourceType.PROVINCE_DB);
        String format = String.format("url:%s;username:%s;password:%s", dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
        log.info(format);
    }

    @After("execution(* com.augurit.aplanmis.supermarket.dataexchange.service.*.*(..))")
    public void clearDB() {
        log.info("清除当前前置库数据配置");
        DataSourceType.clearDB();
    }
}
