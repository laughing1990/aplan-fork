package com.augurit.aplanmis.common.constants;

import lombok.Getter;

/*
部门辅导详情操作
 */
@Getter
public enum GuideChangeAction {

    ADD("增加", "a"),
    DELETE("删除", "d"),
    CHANGE("更改", "c"),
    UNKNOWN("未知", "3");

    private String name;
    private String value;

    GuideChangeAction(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static GuideChangeAction fromValue(String value) {
        for (GuideChangeAction state : GuideChangeAction.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        return GuideChangeAction.UNKNOWN;
    }
}

