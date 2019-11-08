package com.augurit.aplanmis.common.service.diagram.constant;

import lombok.Getter;

@Getter
public enum HandleStatus {

    FINISHED("办理通过", "FINISHED"),
    NOT_PASS("不通过", "NOT_PASS"),
    TOLERENCE_PASS("容缺通过", "TOLERENCE_PASS"),
    UN_FINISHED("未办", "UN_FINISHED"),
    CORRECT_MATERIAL_START("补正开始", "CORRECT_MATERIAL_START"),
    CORRECT_MATERIAL_END("补正结束", "CORRECT_MATERIAL_END"),
    SPECIFIC_PROC_START("特程开始", "SPECIFIC_PROC_START"),
    SPECIFIC_PROC_END("特程结束", "SPECIFIC_PROC_END"),
    NORMAL_ACCEPT("正常受理", "NORMAL_ACCEPT"),
    HANDLING("办理中", "HANDLING"),
    NEED_NOT_HANDLE("不用办", "NEED_NOT_HANDLE"),
    NEED_HANDLE_BUT_UNDO("要办未办", "NEED_HANDLE_BUT_UNDO"),

    BACK_APPLY("已撤件", "BACK_APPLY"),
    REFUSE_DEAL("不受理", "REFUSE_DEAL"),
    OUT_SCOPE("不予受理", "OUT_SCOPE"),
    RECALL("撤回", "RECALL"),
    REVOKE("撤销", "REVOKE"),

    UNKNOWN("未知", "UNKNOWN");

    private String name;
    private String value;

    HandleStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static HandleStatus fromValue(String value) {
        for (HandleStatus state : HandleStatus.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        return HandleStatus.UNKNOWN;
    }
}
