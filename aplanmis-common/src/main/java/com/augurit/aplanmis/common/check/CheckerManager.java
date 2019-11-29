package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.check.exception.CheckException;
import com.augurit.aplanmis.common.check.exception.ItemItemCheckException;
import com.augurit.aplanmis.common.check.exception.ItemPartFormCheckException;
import com.augurit.aplanmis.common.check.exception.ItemProjCheckException;
import com.augurit.aplanmis.common.check.exception.StageItemCheckException;
import com.augurit.aplanmis.common.check.exception.StageItemFormCheckException;
import com.augurit.aplanmis.common.check.exception.StagePartFormCheckException;
import com.augurit.aplanmis.common.check.exception.StageProjCheckException;
import com.augurit.aplanmis.common.check.exception.StageStageCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CheckerManager {

    @Autowired
    private List<Checker<AeaParStage>> stageCheckers;

    @Autowired
    private List<Checker<AeaItemBasic>> itemCheckers;

    public CheckStageResultInfo stageCheck(AeaParStage aeaParStage, CheckerContext checkerContext) {
        log.info("阶段前置检查: stageId: " + aeaParStage.getStageId() + ", stageName: " + aeaParStage.getStageName() + ", 是否前置检查阶段事项表单: " + aeaParStage.getIsCheckItemform()
                + ", 是否前置检查阶段事项扩展表单: " + aeaParStage.getIsCheckPartform() + ", 是否前置检查项目信息: " + aeaParStage.getIsCheckProj()
                + ", 是否前置检查阶段信息: " + aeaParStage.getIsCheckStage());

        CheckStageResultInfo result = new CheckStageResultInfo();
        if (stageCheckers != null) {
            OrderComparator.sort(stageCheckers);

            List<String> errorMsg = new ArrayList<>();
            try {
                for (Checker<AeaParStage> checker : stageCheckers) {
                    try {
                        checker.check(aeaParStage, checkerContext);
                    } catch (CheckException e) {
                        result.setPassed(false);
                        if (e instanceof StageStageCheckException) {
                            StageStageCheckException stageStageCheckException = (StageStageCheckException) e;
                            result.setFrontStages(stageStageCheckException.getAeaParFrontStages());
                        } else if (e instanceof StageItemCheckException) {
                            StageItemCheckException itemCheckException = (StageItemCheckException) e;
                            result.setFrontItems(itemCheckException.getFrontItems());
                        } else if (e instanceof StageItemFormCheckException) {
                            StageItemFormCheckException itemFormCheckException = (StageItemFormCheckException) e;
                            result.setAeaParFrontItemforms(itemFormCheckException.getAeaParFrontItemforms());
                        } else if (e instanceof StagePartFormCheckException) {
                            StagePartFormCheckException partFormCheckException = (StagePartFormCheckException) e;
                            result.setAeaParFrontPartforms(partFormCheckException.getAeaParFrontPartforms());
                        } else if (e instanceof StageProjCheckException) {
                            log.error(e.getMessage());
                            // todo
                        }
                        if (StringUtils.isNotBlank(e.getMessage())) {
                            errorMsg.add(e.getMessage());
                        }
                    }
                }
                if (errorMsg.size() > 0) {
                    result.setMessage(StringUtils.join(errorMsg.toArray(new String[0]), "\n"));
                }
            } finally {
                checkerContext.clear();
            }
        }
        return result;
    }

    public CheckItemResultInfo itemCheck(List<AeaItemBasic> aeaItemBasics, CheckerContext checkerContext) {
        CheckItemResultInfo resultInfo = new CheckItemResultInfo();
        if (itemCheckers != null) {
            OrderComparator.sort(itemCheckers);

            List<String> errorMsg = new ArrayList<>();
            try {
                for (AeaItemBasic aeaItemBasic : aeaItemBasics) {
                    log.info("事项前置检查: itemVerId: " + aeaItemBasic.getItemVerId() + ", itemName: " + aeaItemBasic.getItemName() + ", 是否允许设置前置检查事项: " + aeaItemBasic.getIsCheckItem()
                            + ", 是否前置检查事项扩展表单: " + aeaItemBasic.getIsCheckPartform() + ", 是否前置检查测项目信息: " + aeaItemBasic.getIsCheckProj());

                    CheckItemResultInfo.ItemResult itemResult = new CheckItemResultInfo.ItemResult();
                    for (Checker<AeaItemBasic> checker : itemCheckers) {
                        try {
                            checker.check(aeaItemBasic, checkerContext);
                        } catch (CheckException e) {
                            // 当前正在被检查的事项
                            if (itemResult.getCheckedItem() != null) {
                                itemResult.setCheckedItem(aeaItemBasic);
                            }
                            if (e instanceof ItemItemCheckException) {
                                ItemItemCheckException itemItemCheckException = (ItemItemCheckException) e;
                                itemResult.setFrontItemBasic(itemItemCheckException.getAeaItemFronts());
                            } else if (e instanceof ItemPartFormCheckException) {
                                ItemPartFormCheckException itemPartFormCheckException = (ItemPartFormCheckException) e;
                                itemResult.setItemFrontPartforms(itemPartFormCheckException.getAeaItemFrontPartformVoList());
                            } else if (e instanceof ItemProjCheckException) {
                                log.error(e.getMessage());
                                // todo
                            }
                            if (StringUtils.isNotBlank(e.getMessage())) {
                                errorMsg.add(e.getMessage());
                            }
                        }
                    }
                    if (itemResult.getCheckedItem() != null) {
                        resultInfo.setPassed(false);
                        resultInfo.getItemResults().add(itemResult);
                    }
                    if (errorMsg.size() > 0) {
                        resultInfo.setMessage(StringUtils.join(errorMsg.toArray(new String[0]), "\n"));
                    }
                }
            } finally {
                checkerContext.clear();
            }
        }
        return resultInfo;
    }

}
