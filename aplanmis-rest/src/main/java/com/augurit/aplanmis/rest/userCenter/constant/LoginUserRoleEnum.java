package com.augurit.aplanmis.rest.userCenter.constant;

import lombok.Getter;


@Getter
public enum LoginUserRoleEnum {

    PERSONAL("个人", "0"),
    HANDLE("经办人", "1"),
    UNIT("单位", "2");

    private String name;
    private String value;

    LoginUserRoleEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
