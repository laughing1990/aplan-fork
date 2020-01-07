package com.augurit.aplanmis.common.constants;

import lombok.Getter;

/*
部门辅导状态
 */
@Getter
public enum GuideApplyState {

    LEADER_SIGNING("牵头部门待签收", "1"),
    LEADER_HANDLING("牵头部门处理中", "2"),
    DEPT_HANDLING("所有部门征求处理中", "3"),
    APPLICANT_CONFIRMING("申请人待确认", "4"),
    FINISHED("结束", "5"),
    DEPT_FINISHED("所有征求部门处理完成", "6"),
    UNKNOWN("未知", "-1");

    private String name;
    private String value;

    GuideApplyState(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static GuideApplyState fromValue(String value) {
        for (GuideApplyState state : GuideApplyState.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        return GuideApplyState.UNKNOWN;
    }
}

