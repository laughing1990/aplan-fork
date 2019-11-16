package com.augurit.aplanmis.common.constants;

import lombok.Getter;

@Getter
public enum CertinstSource {
    LOCAL("本地部门出证", "local"),
    EXTERNAL("第三方对接证照", "external");

    private String name;
    private String value;

    CertinstSource(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
