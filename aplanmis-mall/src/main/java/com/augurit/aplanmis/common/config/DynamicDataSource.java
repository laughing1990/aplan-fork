package com.augurit.aplanmis.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Override
    protected Object determineCurrentLookupKey() {
        String db = DataSourceType.getDB();
        log.info("当前数据源为：" + db+"；数据库连接为：");
        String format = String.format("url:%s;username:%s;password:%s", dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
        log.info(format);
        return db;
    }
}
