package com.augurit.aplanmis.admin;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aea/par/stage/partform")
public class AeaParStagePartformController {

private static Logger logger = LoggerFactory.getLogger(AeaParStagePartformController.class);

    @Autowired
    private AeaParStagePartformService aeaParStagePartformService;

    @RequestMapping("/saveStagePartformByPartformId.do")
    public ResultForm saveStagePartformByPartformId(String stageItemId, String formId, String operation) {

        if(StringUtils.isBlank(stageItemId)){
            throw new InvalidParameterException("参数stageItemId为空!");
        }
        if(StringUtils.isBlank(formId)){
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaParStagePartform stagePartform = new AeaParStagePartform();
        stagePartform.setStagePartformId(stageItemId);
        stagePartform.setPartformId(formId);
        aeaParStagePartformService.updateStagePartform(stagePartform);
        return new ResultForm(true);
    }
}
