package com.augurit.aplanmis.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@Slf4j
@MapperScan(
        basePackages = {"com.augurit.demo.mapper",
                "com.augurit.agcloud.**.mapper",
                "com.augurit.aplanmis.common.mapper",
                "com.augurit.aplanmis.supermarket.dataexchange.mapper"
        },
        sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {//extends ApplicationObjectSupport
    @Value("${data.exchange.open:false}")
    private Boolean dataExchange;

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.aplanmis")
    public DataSource getDateSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "provinceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.province")
    @ConditionalOnExpression("${data.exchange.open:false}")
    public DataSource getDateSource2() {
        return DataSourceBuilder.create().build();
    }

    /***
     * 动态数据源 通过AOP在不同数据源之间动态切换
     */
    @Bean(name = "dynamicDataSource")
//    @Primary
    public DataSource dynamicDataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource);

        //配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put(DataSourceType.DEFAULT_DB, primaryDataSource);
        if (dataExchange) {
           /* Object provinceDataSource = getApplicationContext().getBean("provinceDataSource");
            dsMap.put(DataSourceType.PROVINCE_DB, provinceDataSource);*/
            dsMap.put(DataSourceType.PROVINCE_DB, getDateSource2());
        }
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    /**
     * 创建会话工厂。
     *
     * @param dynamicDataSource 数据源
     * @return 会话工厂
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        Resource[] resources1 = new PathMatchingResourcePatternResolver().getResources("classpath*:com/augurit/agcloud/**/*.xml");
        Resource[] resources2 = new PathMatchingResourcePatternResolver().getResources("classpath*:com/augurit/aplanmis/**/*.xml");
        Resource[] resourcesAll = new Resource[resources2.length + resources1.length];
        System.arraycopy(resources1, 0, resourcesAll, 0, resources1.length);
        System.arraycopy(resources2, 0, resourcesAll, resources1.length, resources2.length);
        List<Resource> resources = new ArrayList<Resource>(Arrays.asList(resourcesAll));
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            String filename = resource.getFilename();
            if (filename.equals("OpuCmsContentChannelMapper.xml")) {
                iterator.remove();
            }
        }
        Resource[] array = resources.toArray(new Resource[resources.size()]);

        //解决myBatis下 不能嵌套jar文件的问题
        VFS.addImplClass(SpringBootVFS.class);

        bean.setMapperLocations(array);
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis.xml"));
        //添加多数据库识别
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        bean.setDatabaseIdProvider(databaseIdProvider);
        return bean.getObject();
    }
}
