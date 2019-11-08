package com.augurit.aplanmis.common.service.project.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.project.StampService;
import com.augurit.aplanmis.common.utils.Base64;
import com.augurit.aplanmis.common.utils.Encrypt;
import com.augurit.aplanmis.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * 印章api 实现类
 *
 * @ClasName: StampServiceImpl
 * @Author: Gongxs
 * @CreateDate:2019/8/19 16:52
 */
@Service
@Slf4j
public class StampServiceImpl implements StampService {


    /**
     * 访问url，需要根据需求加上具体方法名，动态拼接
     */
    //@Value("${stamp.url}")
    private String url;

    /**
     * 印章服务系统id
     */
    //@Value("${stamp.appid}")
    private String appid;

    /**
     * 印章服务系统salt
     */
    //@Value("${stamp.salt}")
    private String salt;

    /**
     * 获取随机数，增强安全性
     *
     * @return
     */
    @Override
    public String getRandom() {
        //用于存放post 请求参数,获取需要的item列表
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appid);

        String requestUrl = url + "randomN";
        JSONObject responseJson = HttpUtil.doPostByJson(requestUrl, jsonObject);
        String randomN = responseJson.getString("randomN");
        return StringUtils.isNotBlank(randomN) ? randomN : null;
    }

    /**
     * 根据随机数和appi 获得加密后密文
     *
     * @param randomNumber
     * @return
     */
    private String getCheckCode(String randomNumber) {
        if (StringUtils.isBlank(randomNumber)) {
            return null;
        }

        String temp = randomNumber + salt;
        String enCode;
        try {
            String checkCode = Encrypt.encodeSHA256(temp.getBytes("UTF-8"));
            enCode = Base64.createBase64().encode(checkCode.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("编码转换异常");
            return null;
        }
        return enCode;
    }

    /**
     * 用信用代码获取印章编号
     *
     * @param unifiedSocialCreditCode
     * @return
     */
    @Override
    public String getStampCode(String unifiedSocialCreditCode) {
        //最终返回给调用处的印章编码
        String stampCode = "";
        String randomNumber = this.getRandom();
        String checkCode = getCheckCode(randomNumber);

        //用于存放post 请求参数,获取需要的item列表
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appid);
        jsonObject.put("randomN", randomNumber);
        jsonObject.put("checkCode", checkCode);
        jsonObject.put("unifiedSocialCreditCode", unifiedSocialCreditCode);
        String requestUrl = url + "getSeals";
        JSONObject responseJson = HttpUtil.doPostByJson(requestUrl, jsonObject);
        JSONArray items = responseJson.getJSONArray("items");

        //对items 遍历，获取有效的印章信息
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            JSONObject object = items.getJSONObject(i);
            //印章使用状态 -1 无效，1 有效, 只返回有效状态的印章编码
            String sealStatus = object.getString("sealStatus");
            if (StringUtils.isNotBlank(sealStatus) && "1".equals(sealStatus)) {
                stampCode = object.getString("sealCode");
            }
        }

        return StringUtils.isNotBlank(stampCode) ? stampCode : "";
    }


    /**
     * 对源文件进行处理， 生成带有印章的新文件
     * 只需要获取文件输入流，并返回输出流（待定）
     *
     * @param tempFilePath 缓存文件地址
     * @param out          获得的加盖签章的输出流
     * @param parameterMap 盖章需要的入参 hash集合
     *                     必须传入的参数： "page": 盖章所在页（0或其他整数值，最大值为文件最大页码 为0，所有页都盖章）
     *                     "unifiedSocialCreditCode": 申请企业或单位的信用代码
     *                     "text": 企业名称
     *                     "title": 待盖章文档的标题
     *                     "orderId": 当前业务的业务号，
     */
    @Override
    public void stampSeal(String tempFilePath, FileOutputStream out, Map<String, String> parameterMap) {
        //首先获取印章编码
        String unifiedSocialCreditCode = parameterMap.get("unifiedSocialCreditCode");
        String stampCode = null;
        if (StringUtils.isNotBlank(unifiedSocialCreditCode)) {
            stampCode = this.getStampCode(unifiedSocialCreditCode);
        }
        //如果参数中没有传入信用代码，或者通过信用代码无法获取有效的印章编码
        if (StringUtils.isBlank(unifiedSocialCreditCode) || StringUtils.isBlank(stampCode)) {
            return;
        }

        //获取随机码和加密密文
        String randomNumber = getRandom();
        String checkCode = getCheckCode(randomNumber);

        JSONObject json = new JSONObject();
        String fileType = "PDF";
        String requestUrl = url + "stampSeal";
        String title = parameterMap.get("title");
        if (StringUtils.isNotBlank(title)) {
            parameterMap.put("title", title);
        }
        //需要盖章文件页码
        String page = parameterMap.get("page");
        if (StringUtils.isNotBlank(page)) {
            json.put("page", page);
        } else {
            json.put("page", "1");
        }
        json.put("sealCode", stampCode);
        json.put("positionType", "1");
        json.put("x", "850");
        json.put("y", "200");
        //公司名称
        String text = parameterMap.get("text");
        if (StringUtils.isNotBlank(text)) {
            json.put("text", text);
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(json);
        String signList = jsonArray.toJSONString();

        //创建HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(requestUrl);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
            File file = new File(tempFilePath);
            multipartEntityBuilder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, tempFilePath);
            multipartEntityBuilder.addTextBody("appid", appid);
            String orderId = parameterMap.get("orderId");
            if (StringUtils.isNotBlank(orderId)) {
                multipartEntityBuilder.addTextBody("orderId", orderId);
            }
            multipartEntityBuilder.addTextBody("title", title, ContentType.MULTIPART_FORM_DATA);
            multipartEntityBuilder.addTextBody("fileType", fileType);
            multipartEntityBuilder.addTextBody("randomN", randomNumber);
            multipartEntityBuilder.addTextBody("checkCode", checkCode);

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            StringBody stringBody = new StringBody(signList, contentType);
            multipartEntityBuilder.addPart("signList", stringBody);
            httpPost.setEntity(multipartEntityBuilder.build());

            // 执行http请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String contentTypeString = entity.getContentType().getValue();
                if(contentTypeString.contains("application/json")){
                    log.info("盖章状态" + "盖章失败");
                    InputStream in = entity.getContent();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    copy(in,byteArrayOutputStream);
                    System.out.println(new String(byteArrayOutputStream.toByteArray(),"UTF-8"));
                }else{
                    log.info("盖章状态" + "盖章成功");
                    InputStream in = entity.getContent();
                    copy(in, out );
                    in.close();
                    out.close();
                }
            } else if (statusCode == 700) {
                log.info("【印章接口调用】签名失败");
                InputStream inResponse = response.getEntity().getContent();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                copy(inResponse, byteArrayOutputStream);
            } else {
                log.info("【系统异常】statusCode = " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }

    }

    /**
     * 对源文件进行处理， 生成带有印章的新文件
     *
     * @param sourceFilePath
     * @param targetPath
     * @param parameterMap
     */
    @Override
    public void stampSeal(String sourceFilePath, String targetPath, Map<String, String> parameterMap) {
        //首先获取印章编码
        String unifiedSocialCreditCode = parameterMap.get("unifiedSocialCreditCode");
        String stampCode = null;
        if (StringUtils.isNotBlank(unifiedSocialCreditCode)) {
            stampCode = this.getStampCode(unifiedSocialCreditCode);
        }
        //如果参数中没有传入信用代码，或者通过信用代码无法获取有效的印章编码
        if (StringUtils.isBlank(unifiedSocialCreditCode) || StringUtils.isBlank(stampCode)) {
            return;
        }

        JSONObject json = new JSONObject();
        String fileType = "PDF";
        String requestUrl = url + "stampSeal";
        String title = parameterMap.get("title");
        if (StringUtils.isNotBlank(title)) {
            parameterMap.put("title", title);
        }
        //需要盖章文件页码
        String page = parameterMap.get("page");
        if (StringUtils.isNotBlank(page)) {
            json.put("page", page);
        } else {
            //默认打印在第一页
            json.put("page", "0");
        }
        json.put("sealCode", stampCode);
        json.put("positionType", "1");
        json.put("x", "850");
        json.put("y", "200");
        //公司名称
        String text = parameterMap.get("text");
        if (StringUtils.isNotBlank(text)) {
            json.put("text", text);
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(json);
        String signList = jsonArray.toJSONString();

        //创建HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        //获取随机码和加密密文
        String randomNumber = getRandom();
        String checkCode = getCheckCode(randomNumber);

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(requestUrl);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);

            File file = new File(sourceFilePath);
            multipartEntityBuilder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, sourceFilePath);
            multipartEntityBuilder.addTextBody("appid", appid);
            String orderId = parameterMap.get("orderId");
            if (StringUtils.isNotBlank(orderId)) {
                multipartEntityBuilder.addTextBody("orderId", orderId);
            }
            multipartEntityBuilder.addTextBody("title", title, ContentType.MULTIPART_FORM_DATA);
            multipartEntityBuilder.addTextBody("fileType", fileType);
            multipartEntityBuilder.addTextBody("randomN", randomNumber);
            multipartEntityBuilder.addTextBody("checkCode", checkCode);

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            StringBody stringBody = new StringBody(signList, contentType);
            multipartEntityBuilder.addPart("signList", stringBody);
            httpPost.setEntity(multipartEntityBuilder.build());

            // 执行http请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String contentTypeString = entity.getContentType().getValue();
                if(contentTypeString.contains("application/json")){
                    System.out.println("盖章失败");
                    InputStream in = entity.getContent();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    copy(in,byteArrayOutputStream);
                    System.out.println(new String(byteArrayOutputStream.toByteArray(),"UTF-8"));
                }else{
                    System.out.println("盖章成功");
                    InputStream in = entity.getContent();
                    FileOutputStream fileOutputStream = new FileOutputStream(targetPath);
                    copy(in, fileOutputStream );
                    in.close();
                    fileOutputStream.close();
                }
            } else if (statusCode == 700) {
                log.info("【印章接口调用】签名失败");
                InputStream in = response.getEntity().getContent();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                copy(in, byteArrayOutputStream);
                System.out.println(new String(byteArrayOutputStream.toByteArray(), "UTF-8"));
            } else {
                log.info("【系统异常】statusCode = " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }

    }

    /**
     * 把输入流写入到输出流中
     *
     * @param in
     * @param out
     * @throws IOException
     */
    private void copy(InputStream in, OutputStream out) throws IOException {
        int len = -1;
        byte[] buff = new byte[10];
        while ((len = in.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
    }

    @Override
    public void verifyStamp(InputStream in, String projCode) throws IOException {
        if (in == null) {
            return;
        }

        //读取输入流生成缓存文件用于验证
        String filePath = "d://verifytemp.pdf";
        File tempFile = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        log.info("文件大小是" + tempFile.length());

        //获取随机码和加密密文
        String randomNumber = getRandom();
        String checkCode = getCheckCode(randomNumber);

        //调用验章接口进行在线验证
        try {
            String fileType = "PDF";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            String requestUrl = url + "verify";
            HttpPost httppost = new HttpPost(requestUrl);
            ContentType contentType = ContentType.create("text/plain", "utf-8");
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", tempFile, ContentType.DEFAULT_BINARY, "1." + fileType);
            multipartEntityBuilder.addTextBody("appid", appid, contentType);
            multipartEntityBuilder.addTextBody("fileType", fileType, contentType);
            multipartEntityBuilder.addTextBody("checkCode", checkCode, contentType);
            multipartEntityBuilder.addTextBody("randomN", randomNumber, contentType);
            httppost.setEntity(multipartEntityBuilder.build());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("【印章验证接口】 接口调用状态码是{}", statusCode);
            log.info("【印章验证接口】json 信息是{}", EntityUtils.toString(resEntity));
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } finally {
            if (in != null) {
                in.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            //验证完毕删除缓存文件
            tempFile.delete();
        }


    }
}
