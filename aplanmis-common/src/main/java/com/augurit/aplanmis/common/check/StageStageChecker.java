package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StageStageCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 阶段的前置阶段检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class StageStageChecker extends AbstractChecker<AeaParStage> {

    @Override
    public void doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StageStageCheckException {
        if (Status.ON.equals(aeaParStage.getIsCheckStage())) {
            log.info("事项: " + aeaParStage.getStageName() + " 需要对前置阶段进行检查");

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            // todo
        }
    }
}
