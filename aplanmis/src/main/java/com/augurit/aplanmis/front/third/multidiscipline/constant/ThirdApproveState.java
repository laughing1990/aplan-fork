package com.augurit.aplanmis.front.third.multidiscipline.constant;

import lombok.Getter;

@Getter
public enum ThirdApproveState {
    UN_HANDLE("未办理", "UN_HANDLE"),
    APPROVING("审批中", "APPROVING"),
    HANDLED("已办结", "HANDLED");

    private String name;
    private String value;

    ThirdApproveState(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
