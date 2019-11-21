package com.augurit.aplanmis.rest.guide.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;

public enum HandlingType implements BaseEnum<HandlingType, String> {

    IMMEDIATE_HANDLE("即办件", "1"),
    PROMISE_HANDLE("承诺件", "2"),
    REPORT_HANDLE("上报件", "3"),
    JOIN_HANDLE("联办件", "LBJ"),
    REPLY_HANDLE("答复件", "DFJ"),
    PROXY_HANDLE("代办件", "DBJ"),
    UNKNOWN("未知", "00");

    private String name;
    private String value;

    HandlingType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static HandlingType fromValue(String value) {
        for (HandlingType handlingType : HandlingType.values()) {
            if (value.equals(handlingType.getValue())) {
                return handlingType;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
