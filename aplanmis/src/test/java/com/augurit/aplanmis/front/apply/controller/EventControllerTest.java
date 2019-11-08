package com.augurit.aplanmis.front.apply.controller;

import com.augurit.BaseControllerTest;
import com.augurit.aplanmis.common.event.AplanmisEventProperties;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.ApplyStartAplanmisEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@DisplayName("事件测试")
public class EventControllerTest extends BaseControllerTest {

    @Autowired
    private AplanmisEventProperties aplanmisEventProperties;

    @Autowired
    private AplanmisEventPublisher publisher;

    @Test
    @DisplayName("测试事件配置")
    public void aplanmisEventProperties() {
        System.out.println(aplanmisEventProperties.getTypeList());
        System.out.println(aplanmisEventProperties.isEnable());
    }

    @Test
    @DisplayName("测试事件异步发送")
    public void publishEvent() {
        publisher.publishEvent(new ApplyStartAplanmisEvent(this));
    }


}
