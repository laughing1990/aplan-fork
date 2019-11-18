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
    private String account;//用户账户名称
    private String password;//用户账户密码
    private String app_key;//分配给应用程序的app_key
    private String app_secret;//分配给应用程序的app_secret
}
