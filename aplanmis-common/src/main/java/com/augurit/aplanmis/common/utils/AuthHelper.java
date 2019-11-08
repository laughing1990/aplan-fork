package com.augurit.aplanmis.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthHelper {

    private static Logger logger = LoggerFactory.getLogger(AuthHelper.class);

    public static final long cacheTime = 1000 * 60 * 1;	//1为分钟数
    public static final String CORP_ID = "agblspxt";
    public static final String FILE_PATH = "/src/main/webapp/ui-static/aplanmis/zj_access_token";

//    public static final String BASE_URL = "http://19.104.11.179:7304/api"; // 测试访问公共地址
    public static final String BASE_URL = "http://19.104.39.15:7304/api"; // 正式访问公共地址
    public static final String LOGIN_NAME = "奥格并联审批系统"; // 用户名
    public static final String LOGIN_PWD = "dkjhylvl";  // 密码

    public static String getAccessToken() throws Exception {

        String path = System.getProperty("user.dir"); //request.getRealPath("\\");
        try {
            // 获取access token
            return AuthHelper.getAccessToken(path);
        }catch (Exception e){  // 删除
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            return getAccessToken(path);
        }
    }

    /**
     * 获取token方法
     * @param path  工程路径
     * @return
     * @throws Exception
     */
    public static String getAccessToken(String path)throws Exception{

        long curTime = System.currentTimeMillis();
        JSONObject accessTokenValue = (JSONObject) FileUtils.getValue(path + FILE_PATH, "accesstoken", CORP_ID);
        String accToken = "";
        JSONObject jsontemp = new JSONObject();
        if (accessTokenValue == null || curTime - accessTokenValue.getLong("begin_time") >= cacheTime) {
            try{
                Map<String,String> params = new HashMap<String,String>();
                String time = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS").format(new Date());
                params.put("policyName",LOGIN_NAME);
                params.put("loginSign",MD5Util.MD5(time + LOGIN_NAME + LOGIN_PWD ));
                params.put("time",time);
                JSONObject jsonObject = HttpUtil.sendPost(BASE_URL + "/token",params,"false");
                if(jsonObject!=null&& StringUtils.isNotBlank(jsonObject.getString("data"))){
                    accToken = jsonObject.getString("data");
                    JSONObject jsonAccess = new JSONObject();
                    jsontemp.clear();
                    jsontemp.put("access_token", accToken);
                    jsontemp.put("begin_time", curTime);
                    jsonAccess.put(CORP_ID, jsontemp);
                    FileUtils.write2File(path+FILE_PATH, jsonAccess, "accesstoken");
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.getMessage();
            }
        } else {
            return accessTokenValue.getString("access_token");
        }
        return accToken;
    }
}
