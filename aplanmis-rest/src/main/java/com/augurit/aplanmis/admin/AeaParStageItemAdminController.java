package com.augurit.aplanmis.admin;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * -Controller 页面控制转发类
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
     * @return 返回结果对象 包含结果信息
     * @
     */
    @RequestMapping("/saveAeaParStageItemBySubformId.do")
    public ResultForm saveAeaParStageItemBySubformId(AeaParStageItem aeaParStageItem, String formId, String operation) {

        ResultForm result = new ContentResultForm<>(true, aeaParStageItem);
        aeaParStageItem.setSubFormId(formId);
        if(ActStoConstant.SMART_FORM_ENTITY_OPERATION_NEW.equals(operation)){
            if (StringUtils.isNotBlank(aeaParStageItem.getStageItemId())) {
                aeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
                result = new ContentResultForm<>(true, aeaParStageItem);
            } else {
                result = new ResultForm(false, "智能表单与事项关联表Id为空!");
            }
        }
        else if(ActStoConstant.SMART_FORM_ENTITY_OPERATION_UPDATE.equals(operation)){
            result = new ContentResultForm<>(true, aeaParStageItem);
        }
        return result;
    }
}