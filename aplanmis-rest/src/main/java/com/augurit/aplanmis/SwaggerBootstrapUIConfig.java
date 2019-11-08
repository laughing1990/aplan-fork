package com.augurit.aplanmis;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 只在开发环境下开启 swagger bootstrap ui的增强功能
 */
@Configuration
@EnableSwaggerBootstrapUI
@Profile({"dev", "local"})
public class SwaggerBootstrapUIConfig {
}
