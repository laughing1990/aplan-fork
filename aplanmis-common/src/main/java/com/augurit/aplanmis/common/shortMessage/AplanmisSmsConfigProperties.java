package com.augurit.aplanmis.common.shortMessage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信接口相关配置类
 */
@Component
@ConfigurationProperties(prefix="aplanmis.sms.cprest.send")
public class AplanmisSmsConfigProperties {
    private String url;//调用短信接口的url地址
    private boolean enable;//是否开启短信发送

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
