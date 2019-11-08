package com.augurit.aplanmis.common.service.dic;

import com.augurit.aplanmis.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 办件编号生成规则
 */
@Service
@Transactional
public class IteminstCodeBscRuleCodeStrategy extends BscRuleCodeStrategy {

    private static final long CODE_VALUE_COUNT = 4L;
    private static final String ITEMINST_CODE_NAME = "办件编号";

    @Override
    public String buildCodeName() {
        return ITEMINST_CODE_NAME;
    }

    @Override
    public long buildCodeValueCount() {
        return CODE_VALUE_COUNT;
    }

    @Override
    public String buildCodeTemplate() {
        return buildCodeMemo(null) + BscRuleCodeStrategy.TEMPLATE_STR;
    }

    @Override
    public String buildCodeMemo(String codeMemoFactor) {
        return DateUtils.yyyymmdd();
    }
}
