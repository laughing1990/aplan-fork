package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.check.exception.ItemPartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事项的前置扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 20)
public class ItemPartFormChecker extends AbstractChecker<AeaItemBasic> {

    @Autowired
    private AeaItemFrontPartformAdminService aeaItemFrontPartformAdminService;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Override
    public void doCheck(AeaItemBasic aeaItemBasic, CheckerContext checkerContext) throws ItemPartFormCheckException {

        if (Status.ON.equals(aeaItemBasic.getIsCheckPartform())) {

            Assert.notNull(aeaItemBasic, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取事项所有的前置扩展表单
            List<AeaItemFrontPartformVo> aeaItemFrontPartformVoList = aeaItemFrontPartformAdminService.getAeaItemFrontPartformVoByItemVerId(aeaItemBasic.getItemVerId());

            if (aeaItemFrontPartformVoList.size() > 0) {

                StringBuffer message = new StringBuffer();


                //通过项目ID获取已经填写的所有的阶段扩展表单
                List<AeaHiItemMatinst> formMatinsts = aeaHiItemMatinstMapper.getFormMatinstByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId());

                if (formMatinsts.size() < 1) {
                    aeaItemFrontPartformVoList.forEach(aeaItemFrontPartformVo -> message.append(aeaItemFrontPartformVo.getPartformName()).append("、"));

                    String error = "【" + message.substring(0, message.length() - 1) + "】" + "尚未填写，无法申报【" + aeaItemBasic.getItemName() + "】。";
                    throw new ItemPartFormCheckException(error, aeaItemFrontPartformVoList);

                } else {
                    List<AeaItemFrontPartformVo> resultPartForms = new ArrayList<>();
                    String forminstIds = formMatinsts.stream().map(AeaHiItemMatinst::getStoFormId).collect(Collectors.joining(","));
                    aeaItemFrontPartformVoList.forEach(aeaItemFrontPartformVo -> {
                        if (!forminstIds.contains(aeaItemFrontPartformVo.getStoFormId())) {
                            resultPartForms.add(aeaItemFrontPartformVo);
                            message.append(aeaItemFrontPartformVo.getPartformName()).append("、");
                        }
                    });

                    if (message.length() > 0) {
                        String error = "【" + message.substring(0, message.length() - 1) + "】" + "尚未填写，无法申报【" + aeaItemBasic.getItemName() + "】。";
                        throw new ItemPartFormCheckException(error, resultPartForms);
                    }
                }


            }
        }
    }
}
