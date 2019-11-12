package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StageProjCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 阶段事项前置检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE)
public class StageProjChecker extends AbstractChecker<AeaParStage> {

    @Override
    public String doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StageProjCheckException {

        if (Status.ON.equals(aeaParStage.getIsCheckProj())) {

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            // TODO

        }
        return null;
    }
}
