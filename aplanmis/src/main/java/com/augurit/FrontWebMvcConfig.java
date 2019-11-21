package com.augurit;

import com.augurit.agcloud.framework.ui.pager.PageArgumentResolver;
import com.augurit.agcloud.framework.util.JdkIdGenerator;
import com.augurit.aplanmis.admin.log.TraceLogInterceptor;
import com.augurit.aplanmis.front.config.JspPathConfig;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

@ComponentScan({"springfox.collection.example.plugins"})
@Configuration
public class FrontWebMvcConfig implements WebMvcConfigurer/*, ApplicationListener<ContextRefreshedEvent> */{

    private static Logger logger = LoggerFactory.getLogger(FrontWebMvcConfig.class);

    @Autowired
    private JspPathConfig jspPathConfig;

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
        // 用户操作日志
        registry.addInterceptor(getTraceLogInterceptor()).excludePathPatterns(new String[]{
                "/ui-static/**",
                "/agcloud/framework/**",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.jpg",
                "/**/*.jpeg",
                "/**/*.woff",
                "/apanmis/admin/sys/traceLog.html",
                "/aea/trace/log/listByPage.do",
                "/rest/aoa/msg/range/getCountAoaMsgRangeForMyReceiveUnReaded"
        });
    }

    @Bean
    public TraceLogInterceptor getTraceLogInterceptor(){

        return new TraceLogInterceptor();
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {

        return new LocalValidatorFactoryBean();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(jspPathConfig.getPrefix());
        resolver.setSuffix(jspPathConfig.getSuffix());
        resolver.setViewNames(jspPathConfig.getViewName());
        resolver.setOrder(jspPathConfig.getOrder());
        return resolver;
    }

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
        //  单个数据大小 KB,MB
        factory.setMaxFileSize(maxFileSize);
        /// 总上传数据大小
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }

    /**
     * 缓存配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
            .addResourceHandler("/ui-static/**")
            .addResourceLocations("/ui-static/")
            .setCachePeriod(31556926);

        //applet部署配置
        String strClasspath="";
        File fileClasspath = null;
        try {
            strClasspath = ResourceUtils.getURL("classpath:").getPath();
            fileClasspath = new File(ResourceUtils.getURL("classpath:").getPath());
            String appletPath=fileClasspath.getParentFile().getParentFile().getParent()+ File.separator;
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                if(!appletPath.contains("file:\\")){
                    appletPath="file:\\"+appletPath;
                }
            }
            registry.addResourceHandler("/applet/**").addResourceLocations(appletPath);
            logger.info("appletPath:"+appletPath);
            registry.addResourceHandler("/resouce/**").addResourceLocations(strClasspath+File.separator+"templates"+File.separator);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
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
     * tiny todo 暂时放开， 前后端集成完成后再关闭
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry
                .addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://192.168.32.53:8083")
                .allowedOrigins("http://192.168.32.53:8001")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8083");
    }

//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//
//        //该地方仅用于本地测试使用，请勿提交到代码库，谢谢
//        MongoDbAchieve uploadFileStrategy = (MongoDbAchieve) SpringUtil.getBean(UploaderFactory.class).create(UploadType.MONGODB.getValue());
//        uploadFileStrategy.setMongodbUri("mongodb://jjt1:jjt1@192.168.30.120:27017/jjt");
//        try {
//            uploadFileStrategy.queryFile("333");
//        }catch (Exception e){
//        }
//    }
}
