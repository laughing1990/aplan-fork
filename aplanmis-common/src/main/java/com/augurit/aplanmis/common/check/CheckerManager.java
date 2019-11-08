package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CheckerManager {

    @Autowired
    private List<Checker<AeaParStage>> stageCheckers;

    @Autowired
    private List<Checker<AeaItemBasic>> itemCheckers;

    public void stageCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws Exception {
        log.info("阶段前置检查: stageId: " + aeaParStage.getStageId() + ", stageName: " + aeaParStage.getStageName() + ", 是否前置检查阶段事项表单: " + aeaParStage.getIsCheckItemform()
                + ", 是否前置检查阶段事项扩展表单: " + aeaParStage.getIsCheckPartform() + ", 是否前置检查项目信息: " + aeaParStage.getIsCheckProj()
                + ", 是否前置检查阶段信息: " + aeaParStage.getIsCheckStage());

        if (stageCheckers != null) {
            OrderComparator.sort(stageCheckers);

            try {
                for (Checker<AeaParStage> checker : stageCheckers) {
                    checker.check(aeaParStage, checkerContext);
                }
            } finally {
                checkerContext.clear();
            }
        }
    }

    public void itemCheck(AeaItemBasic aeaItemBasic, CheckerContext checkerContext) throws Exception {
        log.info("事项前置检查: itemVerId: " + aeaItemBasic.getItemVerId() + ", itemName: " + aeaItemBasic.getItemName() + ", 是否允许设置前置检查事项: " + aeaItemBasic.getIsCheckItem()
                + ", 是否前置检查事项扩展表单: " + aeaItemBasic.getIsCheckPartform() + ", 是否前置检查测项目信息: " + aeaItemBasic.getIsCheckProj());

        if (itemCheckers != null) {
            OrderComparator.sort(itemCheckers);

            try {
                for (Checker<AeaItemBasic> checker : itemCheckers) {
                    checker.check(aeaItemBasic, checkerContext);
                }
            } finally {
                checkerContext.clear();
            }
        }
    }

}
