package com.augurit.aplanmis.rest.sms.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.rest.sms.service.SendShortMessageService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/send/short/message")
public class SendShortMessageController {

    private static Logger logger = LoggerFactory.getLogger(SendShortMessageController.class);

    @Autowired
    private SendShortMessageService sendShortMessageService;


    /**
     * @param phone 接收短信的号码
     * @param templateId 短信模板ID
     * @param templateParamJson
     * 短信模板参数json字符串，例如：{"ProjName":"测试短信的项目","applyinstCode":"test_007","taskName":"test_sms","dueNum":"10"}
     * @param appFlag 发送短信的应用标识，默认是1，表示：唐山市工程建设审批系统。2表示：唐山市多规合一业务协同平台。3是预留应用，其他待拓展。
     * @return
     * @throws Exception
     */
    @PostMapping("/sendSms")
    public ContentResultForm sendSms(String phone,String templateId,String templateParamJson,String appFlag)throws Exception{
        ContentResultForm resultForm = new ContentResultForm(false);
        if(StringUtils.isBlank(appFlag)){
            appFlag = "1";
        }
        if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(templateId) && StringUtils.isNotBlank(templateParamJson)){
            Gson gson = new Gson();
            Map<String, String> map = null;
            try {
                map = gson.fromJson(templateParamJson,
                        new TypeToken<Map<String, String>>() {
                        }.getType());
            }catch (Exception e){
                resultForm.setContent(templateParamJson);
                resultForm.setMessage("传递的模板参数有误");
                return resultForm;
            }
            resultForm = sendShortMessageService.sendSMS(phone, templateId, map, appFlag);
        }else{
            resultForm.setMessage("短信接收号码、短信模板ID、短信模板参数不能为空");
        }
        return resultForm;
    }
}
