package com.augurit;

//import io.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) throws Exception {
        SpringLiquibase liquibase = new SpringLiquibase();

        //sam 解决使用读写分离后liquibase无法正常启动的问题
        /*if(dataSource instanceof MasterSlaveDataSource) {
            dataSource = ((MasterSlaveDataSource) dataSource).getConnection().getDataSourceMap().get("master");
        }*/

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:liquibase/master.xml");
//        liquibase.setContexts("development,test,production");
        liquibase.setShouldRun(true);
        liquibase.setDropFirst(false);
        return liquibase;
    }
}

