package com.augurit.efficiency.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum OperateSource implements BaseEnum<OperateSource, String> {
    SYS_AUTO_CREATE("程序生成", "1"),
    LOGIN_USER_CREATE("人工生成", "0");

    private String name;
    private String value;

    OperateSource(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static OperateSource fromValue(String value) {
        for (OperateSource s : OperateSource.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("参数不正确，请检查");
    }


}
