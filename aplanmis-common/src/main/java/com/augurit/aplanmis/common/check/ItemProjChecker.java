package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.ItemProjCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 事项项目信息检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE)
public class ItemProjChecker extends AbstractChecker<AeaItemBasic> {

    @Override
    public void doCheck(AeaItemBasic o, CheckerContext checkerContext) throws ItemProjCheckException {
        // todo
    }
}
