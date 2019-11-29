package com.augurit.aplanmis.common.check;

import base.BaseTest;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@DisplayName("前置检查管理器")
class CheckerManagerTest extends BaseTest {

    @Autowired
    private CheckerManager checkerManager;

    @Test
    void stageCheck() throws Exception {
        Assertions.assertNotNull(checkerManager, "CheckerManager must not null");

        checkerManager.stageCheck(new AeaParStage(), new CheckerContext(new CheckerInfo()));
    }

    @Test
    void itemCheck() throws Exception {
        Assertions.assertNotNull(checkerManager, "CheckerManager must not null");

        checkerManager.itemCheck(Arrays.asList(new AeaItemBasic()), new CheckerContext(new CheckerInfo()));
    }
}