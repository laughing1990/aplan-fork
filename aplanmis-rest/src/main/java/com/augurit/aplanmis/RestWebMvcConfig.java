package com.augurit.aplanmis;

import com.augurit.agcloud.framework.ui.pager.PageArgumentResolver;
import com.augurit.agcloud.framework.util.JdkIdGenerator;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.List;
import java.util.Properties;

@ComponentScan({"springfox.collection.example.plugins"})
@Configuration
public class RestWebMvcConfig implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(RestWebMvcConfig.class);

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        logger.debug("初始化 EasyUI 分页请求参数处理器");

        logger.debug("初始化mertronic的DataTable分页请求参数处理器");
        argumentResolvers.add(datatablePageArgumentResolver());

        logger.debug("OPUS子系统 - 添加移动设备检测机制");
        argumentResolvers.add(deviceHandlerMethodArgumentResolver());
    }


    @Bean
    public PageArgumentResolver datatablePageArgumentResolver() {
        return new PageArgumentResolver();
    }

    /**
     * 定义拦截器链
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(deviceResolverHandlerInterceptor());

    }


    @Bean(name = "validator")
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {

        return new LocalValidatorFactoryBean();
    }

//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix(jspPathConfig.getPrefix());
//        resolver.setSuffix(jspPathConfig.getSuffix());
//        resolver.setViewNames(jspPathConfig.getViewName());
//        resolver.setOrder(jspPathConfig.getOrder());
//        return resolver;
//    }

    /*@Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(jspPathConfig.getPrefix());
        resolver.setSuffix(jspPathConfig.getSuffix());
        resolver.setViewNames(jspPathConfig.getViewName());
        resolver.setOrder(jspPathConfig.getOrder());
        return resolver;
    }*/

    //自动代理
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JdkIdGenerator defaultIdGenerator() {
        return new JdkIdGenerator();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // Opus子系统配置
    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Value("${spring.servlet.multipart.max-file-size:100Mb}")
    private String maxFileSize;
    @Value("${spring.servlet.multipart.max-request-size:1024Mb}")
    private String maxRequestSize;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(maxFileSize); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }

    //缓存配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/ui-static/**")
                .addResourceLocations("/ui-static/")
                .setCachePeriod(31556926);
    }

    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

    /**
     * 处理跨域请求
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
