package com.augurit.aplanmis.common.shortMessage.utils;

import lombok.Data;

@Data
public class SendMsmContent {
    private String phoneNum;//手机号
    private String templateId;//短信模板ID
    private String templateParamJson;//模板参数json

}
