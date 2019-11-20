package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StageStageCheckException;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontStageService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 阶段的前置阶段检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class StageStageChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontStageService aeaParFrontStageService;

    @Autowired
    private AeaParStageService aeaParStageService;

    @Override
    public String doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StageStageCheckException {

        if (Status.ON.equals(aeaParStage.getIsCheckStage())) {
            log.info("阶段: " + aeaParStage.getStageName() + " 需要对前置阶段进行检查");

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取所有的前置阶段信息
            List<AeaParFrontStage> aeaParFrontStageVoList = aeaParFrontStageService.getHistStageByStageId(aeaParStage.getStageId());

            //通过项目ID获取所有已经办结或办结容缺通过的阶段信息
            List<AeaParStage> parStages = aeaParStageService.getCompletedStageByProjInfoId(projInfoId);

            if (aeaParFrontStageVoList.size() > 0) {

                StringBuffer message = new StringBuffer();

                if (parStages.size() < 1) {
                    aeaParFrontStageVoList.forEach(aeaParFrontStageVo -> {
                        message.append(aeaParFrontStageVo.getHistStageName()).append("、");
                    });
                    String error = "【" + message.substring(0, message.length() - 1) + "】";
                    return error + "尚未审批通过，无法申报【" + aeaParStage.getStageName() + "】。";
                }

                String stageIds = parStages.stream().map(AeaParStage::getStageId).collect(Collectors.joining(","));
                aeaParFrontStageVoList.forEach(aeaParFrontStageVo -> {
                    if (!stageIds.contains(aeaParFrontStageVo.getHistStageId())) {
                        message.append(aeaParFrontStageVo.getHistStageName()).append("、");
                    }
                });

                if (message.length() > 0) {
                    String error = "【" + message.substring(0, message.length() - 1) + "】";
                    return error + "尚未审批通过，无法申报【" + aeaParStage.getStageName() + "】。";
                }
            }
        }
        return null;
    }
}
