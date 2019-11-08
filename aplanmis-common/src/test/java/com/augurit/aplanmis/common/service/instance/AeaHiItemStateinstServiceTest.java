package com.augurit.aplanmis.common.service.instance;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@DisplayName("单项情形测试")
class AeaHiItemStateinstServiceTest extends BaseTest {
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Test
    void batchInsertAeaHiItemStateinst() throws Exception{
        int xiaohutu = aeaHiItemStateinstService.batchInsertAeaHiItemStateinst("", "", null, null, "xiaohutu");
        System.out.println(xiaohutu);
    }

    @Test
    void listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId() throws Exception{
        List<Map<String, String>> maps1 = aeaHiItemStateinstService.listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId("bbabece1-494a-497d-9319-6ad3c366cfeb", "");
        List<Map<String, String>> maps2 = aeaHiItemStateinstService.listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId("", "a088b392-b491-4508-b1b0-884f6cb925ec");
        System.out.println("----------------------");
    }
}