package com.augurit.aplanmis.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author GWCheng
 *
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHelper.class);

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final String USER_AGENT = "User-Agent";
    private static final String CONTENT_TYPE = "Content-Type";

    private static final String DEFAULT_USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0";
    private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    private static final String DEFAULT_CONTENT_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 发送HttpGet请求
     * @param url
     * @return 返回响应体
     */
    public static CloseableHttpResponse sendGet(String url) {
        return sendGetRequest(url,true);
    }

    /**
     * 发送HttpGet请求
     * @param url
     * @param useDefaultUserAgent 请求头使用默认的User-Agent设置
     * @return 返回响应体
     */
    public static CloseableHttpResponse sendGetRequest(String url,boolean useDefaultUserAgent) {

        if(url!=null) {
            LOGGER.info("sendGetRequest 请求url:{},useDefaultUserAgent:{}", url,useDefaultUserAgent);
        }

        HttpGet httpget = new HttpGet(url);

        if(useDefaultUserAgent) {
            httpget.setHeader(USER_AGENT, DEFAULT_USER_AGENT_VALUE);
        }
        httpget.setHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE_VALUE);

        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpget);
            return response;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {

                }
            }
        }

        return response;
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @param decodeBase64 返回是否需要base64解密
     * @return 返回json对象
     */
    public static JSONObject sendGet(String url,boolean decodeBase64) {

        return sendGet(url,decodeBase64,true);
    }

    /**
     * 发送HttpGet请求
     * @param url
     * @param decodeBase64 返回是否需要base64解密
     * @param useDefaultUserAgent 请求头使用默认的User-Agent设置
     * @return 返回json对象
     */
    public static JSONObject sendGet(String url,boolean decodeBase64,boolean useDefaultUserAgent) {

        if(url!=null) {
            LOGGER.info("sendGet 请求url:{},decodeBase64:{},useDefaultUserAgent:{}", url,decodeBase64,useDefaultUserAgent);
        }

        JSONObject result = null;
        HttpGet httpget = new HttpGet(url);

        if(useDefaultUserAgent) {
            httpget.setHeader(USER_AGENT, DEFAULT_USER_AGENT_VALUE);
        }
        httpget.setHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE_VALUE);

        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, Consts.UTF_8);

                LOGGER.info("请求返回:{}",resultStr);

                if(decodeBase64){
                    resultStr = new String(Base64.decodeBase64(resultStr.getBytes()));

                    LOGGER.info("请求返回decodeBase64:{}",resultStr);
                }
                result =  JSON.parseObject(resultStr);
            }
        } catch (ParseException | IOException e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }


    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return 返回结果对象
     */
    public static JSONObject sendPost(String url) {
        return sendPost(url,null,false,false);
    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url
     * @param map 请求参数
     * @param decodeBase64 返回是否需要base64解密
     * @return 返回结果对象
     */
    public static JSONObject sendPost(String url, Map<String, String> map,String decodeBase64) {

        return sendPost(url,map,"true".equals(decodeBase64)?true:false,true);

    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url
     * @param map 请求参数
     * @param decodeBase64 返回是否需要base64解密
     * @param useDefaultUserAgent 请求头使用默认的User-Agent设置
     * @return
     */
    public static JSONObject sendPost(String url, Map<String, String> map,boolean decodeBase64,boolean useDefaultUserAgent) {

        if(url!=null) {
            LOGGER.info("sendPost 请求url:{},请求参数:{},decodeBase64:{},useDefaultUserAgent:{}", url,map==null?"":map.toString(),decodeBase64,useDefaultUserAgent);
        }

        JSONObject result = null;

        CloseableHttpResponse response = null;

        HttpPost httppost = new HttpPost(url);

        if(useDefaultUserAgent) {
            httppost.setHeader(USER_AGENT, DEFAULT_USER_AGENT_VALUE);
        }

        httppost.setHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE_VALUE);

        if(map!=null) {

            List<NameValuePair> formparams = new ArrayList<>();

            for (Map.Entry<String, String> entry : map.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httppost.setEntity(entity);
        }

        try {
            response = httpclient.execute(httppost);

            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String resultStr = EntityUtils.toString(httpEntity, Consts.UTF_8);

                LOGGER.info("请求返回:{}",resultStr);

                if(decodeBase64){
                    resultStr = new String(Base64.decodeBase64(resultStr.getBytes()),Consts.UTF_8);

                    LOGGER.info("请求返回decodeBase64:{}",resultStr);
                }
                result =  JSON.parseObject(resultStr);
            }
        } catch (ParseException | IOException e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * json请求
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPostByJson(String url,JSONObject json){

        LOGGER.info("doPostByJson 请求url:{},json:{}",url,json.toJSONString());

        DefaultHttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        CloseableHttpResponse res = null;
        try {
            StringEntity stringEntity = new StringEntity(json.toString());
            stringEntity.setContentEncoding(DEFAULT_CONTENT_ENCODING);
            stringEntity.setContentType(CONTENT_TYPE_JSON);
            post.setEntity(stringEntity);

            res = client.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(res.getEntity());

                LOGGER.info("请求返回{}",result);

                response = JSON.parseObject(result);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            if(res!=null){
                try {
                    res.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * POST请求 ,尝试请求3次
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