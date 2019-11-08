package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum InOutStatus implements BaseEnum<InOutStatus, String> {
    IN("输入", "1"),
    OUT("输出", "0")
    ;

    private String name;
    private String value;

    InOutStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
