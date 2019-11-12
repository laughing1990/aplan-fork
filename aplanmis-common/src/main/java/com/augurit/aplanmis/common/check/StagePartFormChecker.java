package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StagePartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 阶段的扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 40)
public class StagePartFormChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontPartformService aeaParFrontPartformService;

    @Override
    public String doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StagePartFormCheckException {

        if (Status.ON.equals(aeaParStage.getIsCheckPartform())) {

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取所有的前置阶段扩展表单信息
            List<AeaParFrontPartformVo> aeaParFrontPartformVos = aeaParFrontPartformService.getAeaParFrontPartformVoByStageId(aeaParStage.getStageId());

            if (aeaParFrontPartformVos.size() > 0) {

                //通过项目ID获取已经填写的所有的阶段扩展表单

                // TODO
                aeaParFrontPartformVos.forEach(aeaParFrontPartformVo -> {
                    if ("1".equals(aeaParFrontPartformVo.getIsSmartForm())) {
                        aeaParFrontPartformVo.getPartformId();//智能表单ID
                    } else {
                        aeaParFrontPartformVo.getDelformId();//开发表单ID
                    }
                });

            }

        }
        return null;
    }
}
