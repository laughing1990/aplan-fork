package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.ItemFormCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 事项的前置表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 30)
public class ItemFormChecker extends AbstractChecker<AeaItemBasic> {

    @Override
    public void doCheck(AeaItemBasic o, CheckerContext checkerContext) throws ItemFormCheckException {
        // todo
    }
}
