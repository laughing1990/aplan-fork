package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.ItemPartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 事项的前置扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 20)
public class ItemPartFormChecker extends AbstractChecker<AeaItemBasic> {

    @Override
    public void doCheck(AeaItemBasic o, CheckerContext checkerContext) throws ItemPartFormCheckException {
        // todo
    }
}
