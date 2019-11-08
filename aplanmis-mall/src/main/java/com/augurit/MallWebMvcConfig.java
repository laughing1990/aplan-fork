package com.augurit;

import com.augurit.agcloud.framework.ui.pager.PageArgumentResolver;
import com.augurit.agcloud.framework.util.JdkIdGenerator;
import com.augurit.aplanmis.mall.interceptor.UserTokenInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;
import java.util.Properties;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HttpMessageConverters messageConverters;
   /* @Autowired
    private JspPathConfig jspPathConfig;*/

    private static Logger logger = LoggerFactory.getLogger(MallWebMvcConfig.class);

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        logger.debug("初始化 EasyUI 分页请求参数处理器");
//        argumentResolvers.add(easyuiPageArgumentResolver());

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
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceResolverHandlerInterceptor());
        registry.addInterceptor(userTokenInterceptor()).addPathPatterns("/rest/user/**",
//                "/supermarket/**",
                "/aea/supermarket/bidProjectManage/**",
                "/supermarket/purchase/**",
                "/aea/supermarket/clientManage/**",
                "/aea/supermarket/serviceMatter/**",
                "/supermarket/linkmanInfo/**",
                "/supermarket/main/getPersonInfo.do",
                "/supermarket/main/getUnitInfo.do",
                "/supermarket/main/**.html",
                "/supermarket/contract/**",
                "/druid/**", "**/swagger-ui.html", "**/doc.html").
                excludePathPatterns("/supermarket/purchase/getAllService",
                        "/supermarket/purchase/getPublicProjPurchaseList",
                        "/supermarket/purchase/getPublicProjPurchaseDatail",
                        "/aea/supermarket/serviceMatter/getServiceMatterNoPage",
                        "/supermarket/main/index.html",
                        "/supermarket/main/header.html",
                        "/supermarket/main/footer.html",
                        "/supermarket/main/procurementNotice/index.html",
                        "/supermarket/main/procurementNotice/details.html",
                        "/supermarket/main/beSelectionNotice/index.html",
                        "/supermarket/main/beSelectionNotice/selectionNoticeDetail.html",
                        "/supermarket/main/beSelectionNotice/contractNoticeDetail.html",
                        "/supermarket/main/imServices.html",
                        "/supermarket/main/imUnits.html",
                        "/supermarket/main/guide.html",
                        "/supermarket/main/notice.html",
                        "/supermarket/main/serviceInfo.html",
                        "/supermarket/main/qualCertInfo.html",
                        "/supermarket/main/agentServiceInfo.html",
                        "/supermarket/main/contactTemplate.html",
                        "/supermarket/purchase/getSelectedQualMajorRequire/**",
                        "/supermarket/main/login.html");
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    /**
     * 识别目前使用的是什么数据源
     *
     * @return
     */
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

  /*  @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(jspPathConfig.getPrefix());
        resolver.setSuffix(jspPathConfig.getSuffix());
        resolver.setViewNames(jspPathConfig.getViewName());
        resolver.setOrder(jspPathConfig.getOrder());
        return resolver;
    }

    @Bean
    public ViewResolver viewResolver() {
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
    
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new MappingPageInfo2EasyuiPageInfoConverter());
//        converters.addAll(this.messageConverters.getConverters());
//    }

//    @Bean
//    public MappingPageInfo2EasyuiPageInfoConverter mappingPageInfo2EasyuiPageInfoConverter() {
//        return new MappingPageInfo2EasyuiPageInfoConverter();
//    }

//    @Bean
//    public PageArgumentResolver easyuiPageArgumentResolver() {
//        return new PageArgumentResolver();
//    }

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
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8084", "http://192.168.15.201:8084");
    }


    //缓存配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                .setCachePeriod(31556926);

        //applet部署配置
//        String strClasspath="";
//        File fileClasspath = null;
//        try {
//            strClasspath = ResourceUtils.getURL("classpath:").getPath();
//            fileClasspath = new File(ResourceUtils.getURL("classpath:").getPath());
//            String appletPath=fileClasspath.getParentFile().getParentFile().getParent()+ File.separator;
//            if(!appletPath.contains("file:\\")){
//                appletPath="file:\\"+appletPath;
//            }
//            registry.addResourceHandler("/applet/**").addResourceLocations(appletPath);
//            logger.info("appletPath:"+appletPath);
//            registry.addResourceHandler("/resouce/**").addResourceLocations(strClasspath+File.separator+"templates"+File.separator);
//        } catch (FileNotFoundException e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
    }
}
