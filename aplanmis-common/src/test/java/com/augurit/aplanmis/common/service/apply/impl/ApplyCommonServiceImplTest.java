package com.augurit.aplanmis.common.service.apply.impl;

import base.BaseTest;
import com.augurit.aplanmis.common.service.apply.ApplyCommonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

class ApplyCommonServiceImplTest extends BaseTest {

    @Autowired
    private ApplyCommonService applyCommonService;

    @Test
    @Rollback(false)
    void clearHistoryInst() throws Exception {
        String applyinstId = "a4d64677-a633-48ce-a73e-d1ce201d6554";
        applyCommonService.clearHistoryInst(applyinstId);
    }
}