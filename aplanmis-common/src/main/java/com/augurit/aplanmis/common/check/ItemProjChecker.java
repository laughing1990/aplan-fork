package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.ItemProjCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 事项项目信息检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE)
public class ItemProjChecker extends AbstractChecker<AeaItemBasic> {

    @Override
    public void doCheck(AeaItemBasic aeaItemBasic, CheckerContext checkerContext) throws ItemProjCheckException {

        if (Status.ON.equals(aeaItemBasic.getIsCheckProj())) {

            Assert.notNull(aeaItemBasic, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            // TODO
        }
    }
}
