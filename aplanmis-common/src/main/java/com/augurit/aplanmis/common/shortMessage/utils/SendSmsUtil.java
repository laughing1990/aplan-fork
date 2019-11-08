package com.augurit.aplanmis.common.shortMessage.utils;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.shortMessage.AplanmisSmsConfigProperties;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送工具类
 * author:sam
 */
public class SendSmsUtil {
    /**
     * map转换为模板参数json字符串
     * @param map
     * @return
     */
    public static String getTemplateParam(Map<String, String> map){
        Gson gson = new Gson();
        String templateParam = gson.toJson(map);
        return templateParam;
    }

    /**
     * 生成短信固定格式的json字符串,格式为：{'phoneNum':'1111111','templateId':'121212121','templateParamJson':'{...}'}
     * @param phoneNum
     * @param templateId
     * @param templateParamJson
     * @return
     */
    public static String getSmsJobRemindContent(String phoneNum,String templateId,String templateParamJson){
        Map<String,String> map = new HashMap<>();
        map.put("phoneNum",phoneNum);
        map.put("templateId",templateId);
        map.put("templateParamJson",templateParamJson);
        String result = SendSmsUtil.getTemplateParam(map);
        return result;
    }

    /**
     * 发送短信
     * @param phone     接收人号码
     * @param templateParamJson  短信模板参数
     * @param templateId    短信模板ID
     * @return
     */
    public static ResultForm flowNodeSendSms(String phone, String templateParamJson, String templateId, AplanmisSmsConfigProperties smsConfigProperties){
        ResultForm resultForm = new ResultForm(false);
        if(smsConfigProperties.isEnable()){
            String sendSMSUrl = smsConfigProperties.getUrl();
            if(StringUtils.isNotBlank(sendSMSUrl)){
                String postEntity = SendSmsUtil.getCpRestPostEntity(phone,templateId,templateParamJson,"");
                String resJson = SendSmsUtil.httpPost(sendSMSUrl, null, postEntity);

                Gson gson = new Gson();
                resultForm = gson.fromJson(resJson,ResultForm.class);
            }else{
                resultForm.setMessage("请在配置文件配置cp-rest短信接口的url地址");
            }
        }else{
            resultForm.setMessage("配置文件sms.isopen已配置不发送短信");
        }
        return resultForm;
    }

    /**
     * 调用cp-rest短信接口的请求字符串
     * @param phone           接收人号码
     * @param templateId     模板ID
     * @param templateParamJson 模板参数
     * @param appFlag  发送短信的应用标识，默认是1，表示：唐山市工程建设审批系统。2表示：唐山市多规合一业务协同平台。3是预留应用，其他待拓展。
     * @return
     */
    private static String getCpRestPostEntity(String phone,String templateId,String templateParamJson,String appFlag){
        if(!StringUtils.isNotBlank(appFlag)){
            appFlag = "1";
        }
        String postEntity = "phone=" + phone + "&templateId="+ templateId + "&templateParamJson=" + templateParamJson + "&appFlag=" + appFlag;
        return postEntity;
    }

    /**
     * POST请求 ,解决中文乱码问题
     * @param postUrl       请求地址
     * @param postHeaders   请求头
     * @param postEntity    请求发送实体
     * @return 提取HTTP响应报文包体，以字符串形式返回
     */
    private static String httpPost(String postUrl, Map<String, String> postHeaders, String postEntity){
        String resJson = "";
        HttpURLConnection httpURLConnection = null;
        try {
            boolean sendOk = false;
            int resendCount = 0;
            int result = 0;
            URL postURL = null;
            while (!sendOk && resendCount < 3) {
                postURL = new URL(postUrl);
                httpURLConnection = (HttpURLConnection) postURL.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                StringBuilder sbStr = new StringBuilder();
                if(postHeaders != null) {
                    for(String pKey : postHeaders.keySet()) {
                        httpURLConnection.setRequestProperty(pKey, postHeaders.get(pKey));
                    }
                }
                if(postEntity != null) {
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(new String(postEntity).getBytes("utf-8"));
                    outputStream.flush();
                    outputStream.close();
                    result = httpURLConnection.getResponseCode();
                    if(result == 200){
                        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            sbStr.append(inputLine);
                        }
                        in.close();
                        // 处理响应结果
                        sendOk = true;
                        resJson = new String(sbStr.toString().getBytes(), "utf-8");
                    }else{
                        resJson = "远程服务器连接失败,错误代码:" + result;
                        System.out.println(resJson);
                        resendCount++;
                    }
                }
            }
        } catch (Exception e) {
            resJson = e.getMessage();
            System.out.println("向远程服务器提交请求数据失败:  " + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return resJson;
    }
}
