package com.augurit.aplanmis.common.constants;

import lombok.Getter;

@Getter
public enum MatHolder {
    UNIT("企业单位", "c"),
    LINKMAN("个人", "u"),
    PROJECT("工程项目", "p");

    private String name;
    private String value;

    MatHolder(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
