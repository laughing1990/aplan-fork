package com.augurit.aplanmis.common.service.dic;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@DisplayName("编码生成规则测试")
class BscRuleCodeStrategyTest extends BaseTest {

    @Autowired
    private IteminstCodeBscRuleCodeStrategy iteminstCodeBscRuleCodeStrategy;

    @Autowired
    private GcbmBscRuleCodeStrategy gcbmBscRuleCodeStrategy;

    @DisplayName("办件编号")
    @Rollback(false)
    @Nested
    class IteminstCodeTest {

        @Test
        @DisplayName("办件编号")
        void generateCode() {
            String prefix = "1233456789111111111111111111111";
            String codeIc = "testCodeIc";
            String resultCode = iteminstCodeBscRuleCodeStrategy.generateCode(prefix, codeIc, "20191015", "ROOT");

            Assertions.assertEquals("1233456789111111111111111111111201910150003", resultCode);
        }

        @Test
        @DisplayName("多线程同时生成")
        void generateCodeConcurrent() throws InterruptedException {
            String prefix = "1233456789000000000000000000000";
            String codeIc = "concurrent";
            List<String> codes = new ArrayList<>(20);
            CountDownLatch latch = new CountDownLatch(20);
            for (int i = 0; i < 20; i++) {
                new Thread(() -> {
                    try {
                        String newValue = iteminstCodeBscRuleCodeStrategy.generateCode(prefix, codeIc, "20191015", "0368948a-1cdf-4bf8-a828-71d796ba89f6");
                        codes.add(newValue);
                        Thread.sleep(1000);
                        System.out.println(">> AutoCodeNumber concurrent Test, new value:" + newValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(">> generateRootOrgConcurrentTest failed" + Thread.currentThread().getName());
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            latch.await();
            System.out.println(">>> codes: " + codes);
        }
    }

    @DisplayName("工程编码")
    @Rollback(false)
    @Nested
    class Gcbm {

        @Test
        @DisplayName("工程编码1")
        void generateCode() {
            String prefix = "2019";
            String codeIc = "gcbm";
            String resultCode = gcbmBscRuleCodeStrategy.generateCode(prefix, codeIc, "工程编码", "ROOT");

            Assertions.assertEquals("2019-0004", resultCode);
        }

        @Test
        @DisplayName("工程编码2")
        void generateCode2() {
            String prefix = "201910";
            String codeIc = "gcbm";
            String resultCode = gcbmBscRuleCodeStrategy.generateCode(prefix, codeIc, "工程编码", "ROOT");

            Assertions.assertEquals("201910-0005", resultCode);
        }

    }


}