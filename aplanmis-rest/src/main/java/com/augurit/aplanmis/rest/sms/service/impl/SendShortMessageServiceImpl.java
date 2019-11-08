package com.augurit.aplanmis.rest.sms.service.impl;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.SendSMSUtils;
import com.augurit.aplanmis.rest.sms.service.SendShortMessageService;
import com.augurit.aplanmis.rest.sms.smsConfig.SmsConfigProperties;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhangwn on 2019/6/21.
 */
@Service
public class SendShortMessageServiceImpl implements SendShortMessageService {

    private static Logger logger = LoggerFactory.getLogger(SendShortMessageServiceImpl.class);

    @Autowired
    private SmsConfigProperties smsConfigProperties;

    /**访问令牌AT--访问令牌，是调用平台能力接口的通行证，可通过调用"令牌接口"获得。CC模式，AC模式都可，推荐CC模式获取令牌**/
    private static String ACCESS_TOKEN = "";

    @Override
    public ContentResultForm sendSMS(String phone, String templateId,Map<String, String> templateParamMap, String appFlag)throws Exception {
        String appId = "";
        String appSecret = "";
        if("1".equals(appFlag)){
            appId = smsConfigProperties.getAPP_ID();
            appSecret = smsConfigProperties.getAPP_SECRET();
        }else if("2".equals(appFlag)){
            appId = smsConfigProperties.getAPP_ID2();
            appSecret = smsConfigProperties.getAPP_SECRET2();
        }else if("3".equals(appFlag)){
            appId = smsConfigProperties.getAPP_ID3();
            appSecret = smsConfigProperties.getAPP_SECRET3();
        }
        ContentResultForm resultForm = new ContentResultForm(false);
        if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(templateId)&&StringUtils.isNotBlank(appId)
                &&StringUtils.isNotBlank(appSecret)&&templateParamMap != null&&templateParamMap.size()>0){
            int invokeCount = 0;
            resultForm = this.sendShortMessage(phone, templateId, templateParamMap,appId,appSecret);
            while (!resultForm.isSuccess()&&(boolean)resultForm.getContent()&&invokeCount<3){
                invokeCount++;
                resultForm = this.sendShortMessage(phone, templateId, templateParamMap,appId,appSecret);
            }
        }else{
            resultForm.setMessage("传递的参数有误");
        }
        return resultForm;
    }

    /**
     * CC模式获取令牌
     * @return
     * @throws Exception
     */
    private String getAccessTokenForCC(String appId, String appSecret)throws Exception {
        String access_token = "";
        //请求令牌传递的参数
        String tokenPostEntity = SendSMSUtils.getCCAccessTokenPostEntity(smsConfigProperties.getCC_GRANT_TYPE(),appId,appSecret);
        //发起请求令牌
        String resJson = SendSMSUtils.httpPost(smsConfigProperties.getGET_ACCESS_TOKEN_URL(), null, tokenPostEntity);
        if(StringUtils.isNotBlank(resJson)){
            //请求令牌成功
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(resJson,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
            if(map != null){
                //请求标准返回码。返回0表示成功
                String res_code = map.get("res_code");
                //返回码描述信息。成功返回Success
                String res_message = map.get("res_message");
                //访问令牌的有效期（以秒为单位）
//                String expires_in = map.get("expires_in");
                if("0".equals(res_code)&&"Success".equals(res_message)){
                    //获取到的访问令牌
                    access_token = map.get("access_token");
                }
            }
        }
        return access_token;
    }

    private ContentResultForm sendShortMessage(String phone, String templateId,Map<String, String> templateParamMap, String appId, String appSecret)throws Exception {
        ContentResultForm resultForm = new ContentResultForm(false,false,"");
        if(StringUtils.isBlank(ACCESS_TOKEN)){
            ACCESS_TOKEN = getAccessTokenForCC(appId,appSecret);
        }
        if(StringUtils.isNotBlank(ACCESS_TOKEN)){
            String timestamp = SendSMSUtils.getTimestamp();
            String templateParam = SendSMSUtils.getTemplateParam(templateParamMap);
            //发送短信请求的参数
            String smsPostEntity = SendSMSUtils.getSMSPostEntity(appId, ACCESS_TOKEN, phone, templateId, timestamp, templateParam);
            //发送短信
            String resJson = SendSMSUtils.httpPost(smsConfigProperties.getSMS_REQUEST_URL(), null, smsPostEntity);
            if(StringUtils.isNotBlank(resJson)){
                //发送请求成功
                System.out.println(resJson);
                Gson gson = new Gson();
                Map<String, String> map2 = gson.fromJson(resJson,
                        new TypeToken<Map<String, String>>() {
                        }.getType());
                if(map2 != null){
                    /**
                     * res_code：返回0表示成功，其它值标识失败
                     * res_message：成功返回：Success；错误返回：错误信息
                     * idertifier：成功返回：短信唯一标识；错误返回：返回空
                     */
                    String res_code = map2.get("res_code");
                    String res_message = map2.get("res_message");
                    String result = SendSMSUtils.getTemplateParam(map2);
                    resultForm.setMessage(result);
                    if("0".equals(res_code)&&"Success".equals(res_message)){
                        resultForm.setSuccess(true);
                        resultForm.setContent(true);
                        resultForm.setMessage("短信发送成功");
                    }else if("110".equals(res_code)){
                        //token过期返回格式
                        //{"res_code" : 110,"res_message" : "request app [1111111111111111] ACCESS_TOKEN [41f063f7764228a0119c3bde46abe7031561180426177] not valid"}
                        //重新获取访问令牌
                        ACCESS_TOKEN = getAccessTokenForCC(appId,appSecret);
                        resultForm.setMessage("访问令牌过期已刷新令牌");
                        logger.debug("访问令牌过期已刷新令牌");
                        resultForm.setContent(true);
                    }
                }
            }
        }else{
            resultForm.setMessage("获取访问令牌失败");
        }
        return resultForm;
    }

}
