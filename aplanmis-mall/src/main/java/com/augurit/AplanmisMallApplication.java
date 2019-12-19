package com.augurit;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@EnableSwaggerBootstrapUI
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.augurit", "com.augurit.aplanmis.mall.interceptor"})//加入flowabale包扫描
@RestController
@EnableAspectJAutoProxy
public class AplanmisMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(AplanmisMallApplication.class, args);
    }

    //用户端首页
    @RequestMapping("/")
    public ModelAndView index_default() {
        return new ModelAndView("mall/index");
    }

    @RequestMapping("/supermarket")
    public ModelAndView superDefault() {
        return new ModelAndView("zjcs/index");
    }
}
