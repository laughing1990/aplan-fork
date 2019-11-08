package com.augurit.aplanmis.common.service.item;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yinlf
 * @Date 2019/8/3
 */
@DisplayName("事项变更历史记录接口测试")
public class AeaLogItemStateHistServiceTest extends BaseTest {

    @Autowired
    AeaLogItemStateHistService aeaLogItemStateHistService;

    @Test
    @DisplayName("获取事项初始状态记录")
    public void getInitStateAeaLogItemStateHistTest(){
        aeaLogItemStateHistService.getInitStateAeaLogItemStateHist("1","1");
    }
}
