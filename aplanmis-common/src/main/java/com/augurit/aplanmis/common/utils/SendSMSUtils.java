package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 电信短信发送工具类
 */
public class SendSMSUtils {

    public static String getTemplateParam(Map<String, String> map){
        Gson gson = new Gson();
        String templateParam = gson.toJson(map);
        return templateParam;
    }

    public static String getTimestamp(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(date);
        return timestamp;
    }

    /**
     * 调用cp-rest短信接口的请求字符串
     * @param phone           接收人号码
     * @param templateId     模板ID
     * @param templateParamJson 模板参数
     * @param appFlag  发送短信的应用标识，默认是1，表示：唐山市工程建设审批系统。2表示：唐山市多规合一业务协同平台。3是预留应用，其他待拓展。
     * @return
     */
    public static String getCpRestPostEntity(String phone,String templateId,String templateParamJson,String appFlag){
        if(!StringUtils.isNotBlank(appFlag)){
            appFlag = "1";
        }
        String postEntity = "phone=" + phone + "&templateId="+ templateId + "&templateParamJson=" + templateParamJson + "&appFlag=" + appFlag;
        return postEntity;
    }

    /**
     * 获取令牌请求字符串
     * @param grant_type    client_credentials
     * @param app_id
     * @param app_secret
     * @return
     */
    public static String getCCAccessTokenPostEntity(String grant_type,String app_id,String app_secret){
        String postEntity = "grant_type=" + grant_type + "&app_id="+ app_id + "&app_secret=" + app_secret;
        return postEntity;
    }

    /**
     * 发送短息请求字符串
     * @param appId
     * @param accessToken
     * @param phone
     * @param templateId
     * @param timestamp
     * @param templateParam
     * @return
     * @throws Exception
     */
    public static String getSMSPostEntity(String appId,String accessToken,String phone,String templateId,String timestamp,String templateParam)throws Exception {
        String postEntity = "app_id=" + appId + "&access_token="
                + accessToken + "&acceptor_tel=" + phone + "&template_id="
                + templateId + "&template_param=" + templateParam
                + "&timestamp=" + URLEncoder.encode(timestamp, "utf-8");
        return postEntity;
    }

    /**
     * POST请求 ,解决中文乱码问题
     * @param postUrl       请求地址
     * @param postHeaders   请求头
     * @param postEntity    请求发送实体
     * @return 提取HTTP响应报文包体，以字符串形式返回
     */
    public static String httpPost(String postUrl, Map<String, String> postHeaders, String postEntity){
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
