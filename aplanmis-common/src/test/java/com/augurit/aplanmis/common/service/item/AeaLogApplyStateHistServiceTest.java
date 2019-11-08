package com.augurit.aplanmis.common.service.item;

import base.BaseTest;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yinlf
 * @Date 2019/8/6
 */
@DisplayName(" 申请状态变更历史记录接口测试")
public class AeaLogApplyStateHistServiceTest extends BaseTest {
    @Autowired
    AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Test
    @DisplayName("获取事项初始状态记录")
    public void getInitStateAeaLogItemStateHistTest(){
        aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist("1","1");
    }
}
