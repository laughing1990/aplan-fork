package com.augurit.aplanmis.rest.auth;

import lombok.Data;

@Data
public class AuthUser {

    private String linkmanInfoId;

    private String linikmanName;

    private String loginName;

    private String unitInfoId;

    private String unitInfoName;

    private String rootOrgId;

    private String currentOrgId;

    private String currentOrgName;

    // true： 个人登录， false：业主单位登录
    private boolean personalAccount = false;
}
