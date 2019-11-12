package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StageItemFormCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 阶段的事项表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 30)
public class StageItemFormChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontItemformService aeaParFrontItemformService;

    @Override
    public String doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StageItemFormCheckException {

        if (Status.ON.equals(aeaParStage.getIsCheckItemform())) {

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取阶段下事项的前置智能表单
            List<AeaParFrontItemformVo> aeaParFrontItemformVoList = aeaParFrontItemformService.getAeaParFrontItemformVoByStageId(aeaParStage.getStageId());

            if (aeaParFrontItemformVoList.size() > 0) {

                //通过项目ID获取已经填写的所有的阶段扩展表单

                aeaParFrontItemformVoList.forEach(aeaParFrontItemformVo -> {

                    aeaParFrontItemformVo.getSubFormId();//智能表单ID

                });

            }
        }
        return null;
    }
}
