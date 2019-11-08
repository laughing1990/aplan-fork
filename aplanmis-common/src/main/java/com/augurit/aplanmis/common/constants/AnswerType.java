package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum AnswerType implements BaseEnum<AnswerType, String> {
    SINGLE("单选", "s"),
    MULTIPLE("多选", "m"),
    ;

    private String name;
    private String value;

    AnswerType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
