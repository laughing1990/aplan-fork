package com.augurit.aplanmis.integration.license.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gd.license.service")
@Data
public class LicenseConfig {

    private String appKey;
    private String appSecret;
    private String account;
    private String password;
    private String apiRoot;
    private String webRoot;

}
