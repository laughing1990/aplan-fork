package com.augurit.aplanmis.rest.auth;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@DisplayName("JwtToken测试")
class JwtTokenTest {

    @Test
    void createToken() {
        AuthUser authUser = new AuthUser();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(JwtToken.USER_INFO, authUser);
        String token = JwtToken.createToken(userInfo);
        System.out.println(token);
    }

    @Test
    void getUserInfo() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySW5mbyI6eyJsaW5rbWFuSW5mb0lkIjpudWxsLCJsaW5pa21hbk5hbWUiOm51bGwsImxvZ2luTmFtZSI6bnVsbCwidW5pdEluZm9JZCI6bnVsbCwidW5pdEluZm9OYW1lIjpudWxsLCJyb290T3JnSWQiOm51bGwsImN1cnJlbnRPcmdJZCI6bnVsbCwiY3VycmVudE9yZ05hbWUiOm51bGx9fQ.DqZDLq9bycVDIKfz6gw_3atFKJVEo6FW6aEFd3ChxrU";
        AuthUser userInfo = JwtToken.getUserInfo(token);

        Assert.notNull(userInfo);
    }
}