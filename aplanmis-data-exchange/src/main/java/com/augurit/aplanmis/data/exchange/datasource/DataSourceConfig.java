package com.augurit.aplanmis.data.exchange.datasource;

import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author yinlf
 */
@Configuration
@Slf4j
@MapperScan(basePackages = {"com.augurit.demo.mapper","com.augurit.aplanmis.data.exchange.mapper", "com.augurit.agcloud.**.mapper", "com.augurit.aplanmis.common.mapper"}, sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig extends ApplicationObjectSupport {

    @Value("${aplanmis.data.exchange.analyse.open}")
    private Boolean uploadToAnalyse;

    @Value("${aplanmis.data.exchange.upload-duogui}")
    private Boolean uploadDuoGui;

    @Primary
    @Bean(name = "aplanmisDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.aplanmis")
    public DataSource getDateSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "provinceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.province")
    public DataSource getDateSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "analyseDataSource")
    @ConditionalOnExpression("${aplanmis.data.exchange.analyse.open}")
    @ConfigurationProperties(prefix = "spring.datasource.analyse")
    public DataSource getDateSource3() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "duoGuiDataSource")
    @ConditionalOnExpression("${aplanmis.data.exchange.upload-duogui}")
    @ConfigurationProperties(prefix = "spring.datasource.duogui")
    public DataSource getDateSource4() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("aplanmisDataSource") DataSource aplanmisDataSource,
                                        @Qualifier("provinceDataSource") DataSource provinceDataSource) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceType.DataBaseType.APLANMIS, aplanmisDataSource);
        targetDataSource.put(DataSourceType.DataBaseType.PROVINCE, provinceDataSource);
        if(uploadToAnalyse){
            Object analyseDataSource = getApplicationContext().getBean("analyseDataSource");
            targetDataSource.put(DataSourceType.DataBaseType.ANALYSE, analyseDataSource);
        }
        if(uploadDuoGui){
            Object analyseDataSource = getApplicationContext().getBean("duoGuiDataSource");
            targetDataSource.put(DataSourceType.DataBaseType.DUOGUI, analyseDataSource);
        }
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(aplanmisDataSource);
        return dataSource;
    }

    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        Resource[] resources1 = new PathMatchingResourcePatternResolver().getResources("classpath*:com/augurit/agcloud/**/*.xml");
        Resource[] resources2 = new PathMatchingResourcePatternResolver().getResources("classpath*:com/augurit/aplanmis/**/*.xml");
        Resource[] resourcesAll = new Resource[resources2.length + resources1.length];
        System.arraycopy(resources1, 0, resourcesAll, 0, resources1.length);
        System.arraycopy(resources2, 0, resourcesAll, resources1.length, resources2.length);
        List<Resource> resources = new ArrayList<Resource>(Arrays.asList(resourcesAll));
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()){
            Resource resource = iterator.next();
            String filename = resource.getFilename();
            if(filename.equals("OpuCmsContentChannelMapper.xml")){
                iterator.remove();
            }
        }
        Resource[] array=resources.toArray(new Resource[resources.size()]);

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