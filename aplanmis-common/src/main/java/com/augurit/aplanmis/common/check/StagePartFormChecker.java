package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.StagePartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 阶段的扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 40)
public class StagePartFormChecker extends AbstractChecker<AeaParStage> {

    @Override
    public void doCheck(AeaParStage o, CheckerContext checkerContext) throws StagePartFormCheckException {
        // todo
    }
}
