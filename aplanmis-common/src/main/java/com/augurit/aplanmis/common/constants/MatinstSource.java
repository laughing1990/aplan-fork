package com.augurit.aplanmis.common.constants;

import lombok.Getter;

@Getter
public enum MatinstSource {
    UNIT("企业单位", "u"),
    LINKMAN("个人", "l");

    private String name;
    private String value;

    MatinstSource(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
