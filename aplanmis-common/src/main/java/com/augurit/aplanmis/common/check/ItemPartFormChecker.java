package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.ItemPartFormCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 事项的前置扩展表单检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 20)
public class ItemPartFormChecker extends AbstractChecker<AeaItemBasic> {

    @Autowired
    private AeaItemFrontPartformAdminService aeaItemFrontPartformAdminService;

    @Override
    public String doCheck(AeaItemBasic aeaItemBasic, CheckerContext checkerContext) throws ItemPartFormCheckException {

        if (Status.ON.equals(aeaItemBasic.getIsCheckPartform())) {

            Assert.notNull(aeaItemBasic, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取事项所有的前置扩展表单
            List<AeaItemFrontPartformVo> aeaItemFrontPartformVoList = aeaItemFrontPartformAdminService.getAeaItemFrontPartformVoByItemVerId(aeaItemBasic.getItemVerId());

            if (aeaItemFrontPartformVoList.size() > 0) {

                //通过项目ID获取已经填写的所有的阶段扩展表单

                aeaItemFrontPartformVoList.forEach(aeaItemFrontPartformVo -> {

                    if ("1".equals(aeaItemFrontPartformVo.getIsSmartForm())) {

                        aeaItemFrontPartformVo.getStoFormId();//表单ID

                    }
                });
            }
        }
        return null;
    }
}
