package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author Lucas Chan
 * @date 2019/12/9
 */
@Getter
public enum IteminstType implements BaseEnum<TimeruleInstState, String> {
    ADMINISTRATION("行政审批", "a"),
    PROMISE("告知承诺制", "p");

    private String name;
    private String value;

    IteminstType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
