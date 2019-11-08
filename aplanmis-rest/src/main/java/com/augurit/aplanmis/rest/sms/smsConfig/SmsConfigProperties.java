package com.augurit.aplanmis.rest.sms.smsConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sms.config")
@Component
@Data
public class SmsConfigProperties {

    /**短信发送请求地址**/
    private String SMS_REQUEST_URL;

    /**获取令牌请求地址**/
    private String GET_ACCESS_TOKEN_URL;

    /**CC授权模式**/
    private String CC_GRANT_TYPE;

    /**唐山市工程建设审批系统**/
    private String APP_ID;
    private String APP_SECRET;

    /**唐山市多规合一业务协同平台**/
    private String APP_ID2;
    private String APP_SECRET2;

    private String APP_ID3;
    private String APP_SECRET3;
}

