package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum DeletedStatus implements BaseEnum<DeletedStatus, String> {

    NOT_DELETED("未删除", "0"),
    DELETED("已删除", "1");

    private String name;
    private String value;

    DeletedStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static DeletedStatus fromValue(String value) {
        for (DeletedStatus s : DeletedStatus.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("参数不正确，请检查");
    }
}
