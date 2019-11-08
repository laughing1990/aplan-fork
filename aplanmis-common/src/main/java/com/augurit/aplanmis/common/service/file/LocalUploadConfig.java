package com.augurit.aplanmis.common.service.file;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "local.upload")
@ToString
public class LocalUploadConfig {
    //true 使用本地上传配置
    private boolean open;
    //本地上传地址
    private String url;

}
