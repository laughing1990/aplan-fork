package com.augurit.aplanmis.mall.login.jwx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * @description JWT实体
 * @author mohaoqi
 * @date 2019/9/3 0003
 ***/
@Component
public class Jwx {

    public static final String CLAIM_KEY_USER="sub";

    public static final String CLAIM_KEY_CREATED="created";

    public static final String CLAIM_KEY_NAME="token";

    public static final String CLAIM_TOKEN_TYPE="bearer";

    public static String secret;

    public static Long expiration;


    @Value("${aplanmis.mall.jwt.secret}")
    public void setSecret(String secret) {
        Jwx.secret = secret;
    }

    @Value("${aplanmis.mall.jwt.expiration}")
    public void setExpiration(Long expiration) {
        Jwx.expiration = expiration;
    }

}
