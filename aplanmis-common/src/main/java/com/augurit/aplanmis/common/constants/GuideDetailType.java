package com.augurit.aplanmis.common.constants;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import lombok.Getter;

/*
部门辅导状态
 */
@Getter
public enum GuideDetailType {

    INTELLIGENCE("智能引导", "s"),
    OWNER("业主", "o"),
    LEADER_DEPT("牵头部门", "l"),
    ITEM_DEPT("事项部门", "i"),
    RESULT("最终结果", "r"),
    ;

    private String name;
    private String value;

    GuideDetailType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static GuideDetailType fromValue(String value) {
        for (GuideDetailType state : GuideDetailType.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        throw new InvalidParameterException("未知的部门辅导明细类型");
    }
}

