package com.augurit;

import com.augurit.agcloud.framework.ui.pager.PageArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author yinlf
 */
@Configuration
@Slf4j
public class CustomerWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

        configurer.enable();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        log.debug("初始化 EasyUI 分页请求参数处理器");

        log.debug("初始化mertronic的DataTable分页请求参数处理器");
        argumentResolvers.add(datatablePageArgumentResolver());
    }


    @Bean
    public PageArgumentResolver datatablePageArgumentResolver() {
        return new PageArgumentResolver();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
