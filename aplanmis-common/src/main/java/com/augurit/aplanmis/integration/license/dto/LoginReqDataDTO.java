package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhangXinhui
 * @date 2019/11/4 004 16:24
 * @desc
 **/
@Data
public class LoginReqDataDTO implements Serializable {
    private String account;
    private String password;
    private String app_key;
    private String app_secret;
}
