package com.augurit.aplanmis.rest.sms.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;

import java.util.Map;

public interface SendShortMessageService {

    /**
     * 发送短信
     * @param phone              短信接收号码
     * @param templateId        短信模板ID
     * @param templateParamMap 短信模板参数
     *@param appFlag 发送短信的应用标识，默认是1，表示：唐山市工程建设审批系统。2表示：唐山市多规合一业务协同平台。3是预留标识，其他待拓展。
     * @return
     */
    public ContentResultForm sendSMS(String phone, String templateId, Map<String, String> templateParamMap, String appFlag)throws Exception;
}
