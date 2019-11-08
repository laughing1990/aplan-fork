package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.check.exception.StageItemFormCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 阶段的事项表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 30)
public class StageItemFormChecker extends AbstractChecker<AeaParStage> {

    @Override
    public void doCheck(AeaParStage o, CheckerContext checkerContext) throws StageItemFormCheckException {
        // todo
    }
}
