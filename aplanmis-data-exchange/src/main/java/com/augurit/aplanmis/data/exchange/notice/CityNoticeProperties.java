package com.augurit.aplanmis.data.exchange.notice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 * @Date 2019/7/17
 */
@Component
@ConfigurationProperties(
        prefix = "aplanmis.data.exchange.city.notice"
)
@Data
public class CityNoticeProperties {

    private boolean open;
    private String[] serverUrls;

}
