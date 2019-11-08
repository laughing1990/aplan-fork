package com.augurit.aplanmis;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class/*, DataSourceAutoConfiguration.class*/})
@EnableTransactionManagement(proxyTargetClass = true)   //开启事物管理功能
@ComponentScan(basePackages = {"com.augurit", "org.flowable.app"})
public class AplanmisRestApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Value("${server.port:8490}")
	Integer httpsPort;

	@Value("${http.port:7071}")
	Integer httpPort;

	public static void main(String[] args) {
		SpringApplication.run(AplanmisRestApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				// 如果要强制使用https，请松开以下注释，打开后，所有的http会拦截到https
				// SecurityConstraint constraint = new SecurityConstraint();
				// constraint.setUserConstraint("CONFIDENTIAL");
				// SecurityCollection collection = new SecurityCollection();
				// collection.addPattern("/*");
				// constraint.addCollection(collection);
				// context.addConstraint(constraint);
			}
		};
		// 添加http
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	// 配置http
	private Connector createStandardConnector() {
		// 默认协议为org.apache.coyote.http11.Http11NioProtocol
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setSecure(false);
		connector.setScheme("http");
		connector.setPort(httpPort);
		// 当http重定向到https时的https端口号
		connector.setRedirectPort(httpsPort);
		return connector;
	}
}
