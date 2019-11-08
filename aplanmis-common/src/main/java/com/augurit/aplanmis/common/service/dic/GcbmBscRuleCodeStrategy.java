package com.augurit.aplanmis.common.service.dic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 办件编号生成规则
 */
@Service
@Transactional
public class GcbmBscRuleCodeStrategy extends BscRuleCodeStrategy {

    private static final long CODE_VALUE_COUNT = 4L;
    private static final String CODE_NAME = "工程编码";

    @Override
    public String buildCodeName() {
        return CODE_NAME;
    }

    @Override
    public long buildCodeValueCount() {
        return CODE_VALUE_COUNT;
    }

    @Override
    public String buildCodeTemplate() {
        return "-" + BscRuleCodeStrategy.TEMPLATE_STR;
    }

    @Override
    public String buildCodeMemo(String codeMemoFactor) {
        return codeMemoFactor;
    }
}
