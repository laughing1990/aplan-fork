package com.augurit.aplanmis.admin;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.framework.constant.Status;
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

import java.util.UUID;

@RestController
@RequestMapping("/aea/par/stage/partform")
public class AeaParStagePartformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStagePartformController.class);

    @Autowired
    private AeaParStagePartformService stagePartformService;

    @RequestMapping("/saveStagePartformByPartformId.do")
    public ResultForm saveStagePartformByPartformId(AeaParStagePartform stagePartform, String formId, String operation) {

        String stagePartformId = stagePartform.getStagePartformId();
        if(StringUtils.isBlank(stagePartformId)){
            throw new InvalidParameterException("参数stagePartformId为空!");
        }
        if(StringUtils.isBlank(formId)){
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaParStagePartform parStagePartform = stagePartformService.getStagePartformById(stagePartformId);
        if(parStagePartform!=null) {
            parStagePartform.setStoFormId(formId);
            stagePartformService.updateStagePartform(parStagePartform);
        }else{
            if(stagePartform==null){
                return  new ResultForm(false, "传递参数全部丢失!");
            }
            if(StringUtils.isBlank(stagePartform.getStageId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数stageId为空!");
            }
            if(StringUtils.isBlank(stagePartform.getIsSmartForm())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数isSmartForm为空!");
            }
            stagePartform.setStagePartformId(UUID.randomUUID().toString());
            stagePartform.setPartformName(StringUtils.isBlank(stagePartform.getPartformName())?"未命名":stagePartform.getPartformName());
            stagePartform.setStoFormId(formId);
            stagePartform.setUseEl(Status.OFF);
            stagePartformService.saveStagePartform(stagePartform);
        }
        return new ResultForm(true,"保存成功!");
    }
}
