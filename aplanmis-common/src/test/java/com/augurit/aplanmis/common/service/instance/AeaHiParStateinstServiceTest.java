package com.augurit.aplanmis.common.service.instance;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("阶段情形测试")
class AeaHiParStateinstServiceTest extends BaseTest {
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;

    @Test
    void listAeaParStateByApplyinstId() {
    }

    @Test
    void batchInsertAeaHiParStateinst() {
    }

    @Test
    void listSelectedParStateinstByStageinstIdOrApplyinstId() throws Exception {
        List<Map<String, String>> maps = aeaHiParStateinstService.listSelectedParStateinstByStageinstIdOrApplyinstId("66a28962-cef7-448c-accb-8d666080c5d6", "");

        List<Map<String, String>> maps1 = aeaHiParStateinstService.listSelectedParStateinstByStageinstIdOrApplyinstId("", "bdc15721-ef9a-4c4f-b9c4-8ca02a1cfd88");
        System.out.println("--------------");
    }
}