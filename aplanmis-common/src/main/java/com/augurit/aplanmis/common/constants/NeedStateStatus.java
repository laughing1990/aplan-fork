package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum NeedStateStatus implements BaseEnum<NeedStateStatus, String> {
    NOT_NEED_STATE("不分情形", "0"),
    NEED_STATE("分情形", "1"),
    ;

    private String name;
    private String value;

    NeedStateStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
