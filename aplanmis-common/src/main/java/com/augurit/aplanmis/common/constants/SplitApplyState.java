package com.augurit.aplanmis.common.constants;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import lombok.Getter;

/*
部门辅导状态
 */
@Getter
public enum SplitApplyState {

    INIT("未发起", "1"),
    APPROVING("待审核", "2"),
    PASSED("审核通过", "3"),
    REJECTED("审核不通过", "4");

    private String name;
    private String value;

    SplitApplyState(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static SplitApplyState fromValue(String value) {
        for (SplitApplyState state : SplitApplyState.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        throw new InvalidParameterException("拆分子工程申请状态不正确, value: " + value);
    }
}

