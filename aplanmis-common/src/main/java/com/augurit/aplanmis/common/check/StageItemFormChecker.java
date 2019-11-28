package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.check.exception.StageItemFormCheckException;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 阶段的事项表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 30)
public class StageItemFormChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontItemformService aeaParFrontItemformService;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Override
    public void doCheck(AeaParStage aeaParStage, CheckerContext checkerContext) throws StageItemFormCheckException {

        if (Status.ON.equals(aeaParStage.getIsCheckItemform())) {

            Assert.notNull(aeaParStage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取阶段下事项的前置智能表单
            List<AeaParFrontItemform> aeaParFrontItemformVoList = aeaParFrontItemformService.getAeaParFrontItemformVoByStageId(aeaParStage.getStageId());

            if (aeaParFrontItemformVoList.size() > 0) {

                StringBuffer message = new StringBuffer();

                //通过项目ID获取已经填写的所有的阶段扩展表单
                List<AeaHiItemMatinst> formMatinsts = aeaHiItemMatinstMapper.getFormMatinstByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId());

                if (formMatinsts.size() < 1) {
                    aeaParFrontItemformVoList.forEach(aeaParFrontItemformVo -> message.append(aeaParFrontItemformVo.getFormName()).append("、"));

                    String error = "【" + message.substring(0, message.length() - 1) + "】" + "尚未填写，无法申报【" + aeaParStage.getStageName() + "】。";
                    throw new StageItemFormCheckException(error, aeaParFrontItemformVoList);

                } else {
                    String forminstIds = formMatinsts.stream().map(AeaHiItemMatinst::getStoFormId).collect(Collectors.joining(","));
                    List<AeaParFrontItemform> resultItemForms = new ArrayList<>();
                    aeaParFrontItemformVoList.forEach(aeaParFrontItemformVo -> {
                        if (!forminstIds.contains(aeaParFrontItemformVo.getSubFormId())) {
                            resultItemForms.add(aeaParFrontItemformVo);
                            message.append(aeaParFrontItemformVo.getFormName()).append("、");
                        }
                    });

                    if (message.length() > 0) {
                        String error = "【" + message.substring(0, message.length() - 1) + "】" + "尚未填写，无法申报【" + aeaParStage.getStageName() + "】。";
                        throw new StageItemFormCheckException(error, resultItemForms);
                    }
                }
            }
        }
    }
}
