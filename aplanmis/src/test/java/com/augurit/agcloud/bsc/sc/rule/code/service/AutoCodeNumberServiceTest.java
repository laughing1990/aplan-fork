package com.augurit.agcloud.bsc.sc.rule.code.service;

import com.augurit.BaseServiceTest;
import com.augurit.agcloud.bsc.domain.BscRuleCode;
import com.augurit.agcloud.bsc.mapper.BscRuleCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("测试流水号生成规则")
@Slf4j
public class AutoCodeNumberServiceTest extends BaseServiceTest {

    @Autowired
    private AutoCodeNumberService autoCodeNumberService;
    @Autowired
    private BscRuleCodeMapper bscRuleCodeMapper;

    @DisplayName("并发测试")
    @Test
    void generateRootOrgConcurrentTest() throws Exception {
        String codeIc = "APPLYINST_CODE-20190925";
        String orgId = "ROOT";
        BscRuleCode bscRuleCode = bscRuleCodeMapper.getBscRuleCodeByCodeIc(codeIc, orgId);
        log.info(">> bscRuleCode.getCodeValueNext(), nextValue: {}", bscRuleCode.getCodeValueNext());
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    String newValue = autoCodeNumberService.generateRootOrg(codeIc);
                    Thread.sleep(1000);
                    log.info(">> AutoCodeNumber concurrent Test, new value: {}", newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(">> generateRootOrgConcurrentTest failed" + Thread.currentThread().getName());
                }
            }).start();
        }
    }

    @DisplayName("加锁并发测试")
    @Test
    void generateRootOrgConcurrentTestWithLock() throws Exception {
        String codeIc = "APPLYINST_CODE-20190925";
        String orgId = "ROOT";
        BscRuleCode bscRuleCode = bscRuleCodeMapper.getBscRuleCodeByCodeIc(codeIc, orgId);
        log.info(">> bscRuleCode.getCodeValueNext(), nextValue: {}", bscRuleCode.getCodeValueNext());
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    String newValue = getNewValue(codeIc);
                    Thread.sleep(1000);
                    log.info(">> AutoCodeNumber concurrent Test, new value: {}", newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(">> generateRootOrgConcurrentTest failed" + Thread.currentThread().getName());
                }
            }).start();
        }
    }

    private synchronized String getNewValue(String codeIc) throws Exception {
        return autoCodeNumberService.generateRootOrg(codeIc);
    }

}
