package com.augurit.aplanmis.front.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * 兼容jsp属性配置
 */
@ConfigurationProperties(prefix = "spring.mvc.jsp.view")
@Component
@Data
public class JspPathConfig {
    /**
     * 路径前缀
     */
    private String prefix;
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * 视图匹配名称
     */
    private String viewName;
    /**
     * 视图解析顺序
     */
    private int order;

}
