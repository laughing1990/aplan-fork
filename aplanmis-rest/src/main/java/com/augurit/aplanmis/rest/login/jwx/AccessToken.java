package com.augurit.aplanmis.rest.login.jwx;

import lombok.Data;

/***
 * @description 授权token
 * @author mohaoqi
 * @date 2019/9/3 0003
 ***/
@Data
public class AccessToken {

    private String accessToken;

    private Long expiresIn;

    private String tokenType;

}
