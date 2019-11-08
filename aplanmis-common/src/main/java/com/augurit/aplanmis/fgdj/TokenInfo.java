/*
package com.augurit.aplanmis.fgdj;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenInfo {

    public static String USERNAME = "sdsadmin";
    public static String PASSWORD = "sdsadmin";
    public static String GRANTTYPE = "password";
    public static String WSO2 = "Basic dm5TdVdSaGdsVWxLNUg3WXJBTnNFSmd0cW9ZYTpUa21mWVFlckpGV0ZPVFg0ekxETmJfdEF6bHdh";
    public static String TOKENURL = "https://10.4.112.223:8243/token?grant_type={grant_type}&username={username}&password={password}";
    public static String ASSESSURL = "http://10.4.112.223:8280/accessapi/v1/getToken";
    public static String PROJURL = "http://10.4.112.223:8280/projectinformation/v1?$filter=PROJECT_CODE eq '{LOCAL_CODE}'&access_token={access_token}";

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject getBearerToken() throws IOException {
        RestTemplate restTemp = new RestTemplate(new HttpsClientRequestFactory());
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", WSO2);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("grant_type", GRANTTYPE);
        uriMap.put("username", USERNAME);
        uriMap.put("password", PASSWORD);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemp.postForEntity(TOKENURL, entity, String.class, uriMap);
        JSONObject token_info = JSONObject.parseObject(response.getBody());
        return token_info;
    }

    public  JSONObject getAssessToken(String token) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(ASSESSURL, HttpMethod.GET, requestEntity, String.class);
        JSONObject token_info = JSONObject.parseObject(response.getBody());
        token_info.put("token", token);
        return token_info;
    }

    public JSONObject getProjInfo(String token,String assessToken,String localCode) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        Map queryMap = new HashMap();
        queryMap.put("LOCAL_CODE",localCode);
        queryMap.put("access_token",assessToken);

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(PROJURL, HttpMethod.GET, requestEntity, String.class,queryMap);
        JSONObject tokenInfo = JSONObject.parseObject(response.getBody());
        return tokenInfo;
    }
}
*/
