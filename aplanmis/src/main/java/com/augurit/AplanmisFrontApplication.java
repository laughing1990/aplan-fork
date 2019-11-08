package com.augurit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class/*, DataSourceAutoConfiguration.class*/})
@EnableTransactionManagement(proxyTargetClass = true)   //开启事物管理功能
@EnableAsync //开启异步。事件发布后可异步执行
@ComponentScan(basePackages = {"com.augurit", "org.flowable.app"})
public class AplanmisFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplanmisFrontApplication.class, args);
	}

	/**
	 * 解决项目启动找不到jar异常
	 * 参考：
	 * https://www.geek-share.com/detail/2765945816.html
	 * @return
	 */
	//会影响到jsp页面
//	@Bean
//	public TomcatServletWebServerFactory tomcatFactory() {
//		return new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
//			}
//		};
//	}
}
