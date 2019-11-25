package com.augurit.aplanmis.admin;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 事项与扩展表单关联表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/partform")
public class AeaItemPartformAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemPartformAdminController.class);

    @Autowired
    private AeaItemPartformAdminService aeaItemPartformService;

    @RequestMapping("/updateAeaItemPartformWithFormId.do")
    public ResultForm updateAeaItemPartformWithFormId(AeaItemPartform partform, String formId, String operation) throws Exception {

        String itemPartformId = partform.getItemPartformId();
        if(StringUtils.isBlank(itemPartformId)){
            throw new InvalidParameterException("参数itemPartformId为空!");
        }
        if(StringUtils.isBlank(formId)){
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaItemPartform itemPartform = aeaItemPartformService.getAeaItemPartformById(itemPartformId);
        if(itemPartform!=null) {
            itemPartform.setStoFormId(formId);
            aeaItemPartformService.updateAeaItemPartform(itemPartform);
        }else{
            if(partform==null){
                return  new ResultForm(false, "传递参数全部丢失!");
            }
            if(StringUtils.isBlank(partform.getItemVerId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数itemVerId为空!");
            }
            if(StringUtils.isBlank(partform.getIsSmartForm())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数isSmartForm为空!");
            }
            partform.setItemPartformId(UUID.randomUUID().toString());
            partform.setPartformName(StringUtils.isBlank(partform.getPartformName())?"未命名":partform.getPartformName());
            partform.setStoFormId(formId);
            partform.setUseEl(Status.OFF);
            aeaItemPartformService.saveAeaItemPartform(partform);
        }
        return new ResultForm(true,"保存成功!");
    }
}
