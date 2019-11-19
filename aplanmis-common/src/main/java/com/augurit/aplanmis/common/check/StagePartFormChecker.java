package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.check.exception.StagePartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 阶段的扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 40)
public class StagePartFormChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontPartformService aeaParFrontPartformService;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

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

                StringBuffer message = new StringBuffer();

                //通过项目ID获取已经填写的所有的阶段扩展表单
                List<AeaHiItemMatinst> formMatinsts = aeaHiItemMatinstMapper.getFormMatinstByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId());

                if (formMatinsts.size() < 1) {

                    aeaParFrontPartformVos.forEach(aeaParFrontPartformVo -> {
                        message.append(aeaParFrontPartformVo.getPartformName()).append("、");
                    });
                    String error = "【" + message.substring(0, message.length() - 1) + "】";
                    return error + "尚未填写，无法申报【" + aeaParStage.getStageName() + "】。";

                } else {
                    String forminstIds = formMatinsts.stream().map(AeaHiItemMatinst::getStoFormId).collect(Collectors.joining(","));
                    aeaParFrontPartformVos.forEach(aeaParFrontPartformVo -> {
                        if (!forminstIds.contains(aeaParFrontPartformVo.getStoFormId())) {
                            message.append(aeaParFrontPartformVo.getPartformName()).append("、");
                        }
                    });

                    if (message.length() > 0) {
                        String error = "【" + message.substring(0, message.length() - 1) + "】";
                        return error + "尚未填写，无法申报【" + aeaParStage.getStageName() + "】。";
                    }
                }
            }
        }
        return null;
    }
}
