package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum ActiveStatus implements BaseEnum<ActiveStatus, String> {
    NOT_ACTIVE("未启用", "0"),
    ACTIVE("启用", "1"),
    ;

    private String name;
    private String value;

    ActiveStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ActiveStatus fromValue(String value) {
        for (ActiveStatus s : ActiveStatus.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("参数不正确，请检查");
    }


}
