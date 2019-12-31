package com.augurit.aplanmis.admin;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.fasterxml.uuid.impl.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.UUDecoder;

import java.util.UUID;

/**
 * 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/stage/item")
public class AeaParStageItemAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageItemAdminController.class);

    @Autowired
    private AeaParStageItemAdminService aeaParStageItemAdminService;

    /**
     * 保存或编辑智能表单与事项关联
     *
     * @param aeaParStageItem
     * @param formId
     * @param operation
     * @return
     */
    @RequestMapping("/saveAeaParStageItemBySubformId.do")
    public ResultForm saveAeaParStageItemBySubformId(AeaParStageItem aeaParStageItem, String formId, String operation) {

        String stageItemId = aeaParStageItem.getStageItemId();
        if(StringUtils.isBlank(stageItemId)){
            return  new ResultForm(false, "智能表单与事项关联表Id为空!");
        }
        if(StringUtils.isBlank(formId)){
            return  new ResultForm(false, "智能表单Id为空!");
        }
        AeaParStageItem stageItem = aeaParStageItemAdminService.getAeaParStageItemById(stageItemId);
        if(stageItem!=null) {
            stageItem.setSubFormId(formId);
            aeaParStageItemAdminService.updateAeaParStageItem(stageItem);
        }else{
            if(aeaParStageItem==null){
                return  new ResultForm(false, "传递参数全部丢失!");
            }
            if(StringUtils.isBlank(aeaParStageItem.getStageId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数stageId为空!");
            }
            if(StringUtils.isBlank(aeaParStageItem.getItemId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数itemId为空!");
            }
            if(StringUtils.isBlank(aeaParStageItem.getItemVerId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数itemVerId为空!");
            }
            if(StringUtils.isBlank(aeaParStageItem.getIsOptionItem())){
                return  new ResultForm(false, "智智能表单与阶段事项关联表记录不存在,传递参数isOptionItem为空!");
            }
            aeaParStageItem.setStageItemId(UUID.randomUUID().toString());
            aeaParStageItem.setSubFormId(formId);
            aeaParStageItemAdminService.saveAeaParStageItem(aeaParStageItem);
        }
        return new ResultForm(true,"保存成功!");
    }
}