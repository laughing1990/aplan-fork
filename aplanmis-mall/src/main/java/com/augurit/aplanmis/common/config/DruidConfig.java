//package com.augurit.aplanmis.common.config;
//
//import com.alibaba.druid.filter.Filter;
//import com.alibaba.druid.filter.stat.StatFilter;
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.google.common.collect.Lists;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DruidConfig {
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @Bean(initMethod = "init",destroyMethod = "close")
//    public DruidDataSource druidDataSource(){
//        DruidDataSource druidDataSource=new DruidDataSource();
//        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
//        return druidDataSource;
//    }
//
//    @Bean
//    public Filter statFilter(){
//        StatFilter filter=new StatFilter();
//        filter.setSlowSqlMillis(3000);
//        filter.setLogSlowSql(true);
//        filter.setMergeSql(true);
//        return filter;
//    }
//
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean(){
//        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
//    }
//}
