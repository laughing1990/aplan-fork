package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.StageProjCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 阶段事项前置检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE)
public class StageProjChecker extends AbstractChecker<AeaParStage> {

    @Override
    public void doCheck(AeaParStage o, CheckerContext checkerContext) throws StageProjCheckException {
        // todo
    }
}
