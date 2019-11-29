package com.augurit.aplanmis.data.exchange.service;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yinlf
 * @Date 2019/9/26
 */
@DisplayName("上传数据到前置库测试")
public class ImportServiceTest extends BaseTest {

    @Autowired
    ImportService importService;

    /*@Test
    @DisplayName("上传事项")
    public void testImportItem() {
        importService.importItem(null, null);
    }*/

    @Test
    @DisplayName(("辅线事项上传"))
    public void importSubordinateItemWhenSuccess() {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse("");
        importService.importSubordinateItem();*/
    }
}
