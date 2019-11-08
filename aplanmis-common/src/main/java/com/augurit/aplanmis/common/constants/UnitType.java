package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author yinlf
 * @Date 2019/7/12
 */
@Getter
public enum UnitType implements BaseEnum<TimeruleInstState, String> {

    DEVELOPMENT_UNIT("建设单位", "1"),
    CONSTRUCTION_UNIT("施工单位", "2"),
    SURVEY_UNIT("勘察单位", "3"),
    DESIGN_UNIT("设计单位", "4"),
    SUPERVISION_UNIT("监理单位", "5"),
    AGENT_CONSTRUCTION_UNIT("代建单位", "6"),
    HANDLE_UNIT("经办单位", "7"),
    OTHERS("其他", "8"),
    REVIEW_AGENCY("审图机构", "9");

    private String name;
    private String value;

    UnitType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static UnitType fromValue(String value) {
        for (UnitType type : UnitType.values()) {
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknow apply state: value: " + value);
    }
}
