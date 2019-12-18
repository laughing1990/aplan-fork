package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author lucas chan
 * @date 2019/12/12
 */
@Getter
public enum ApplyinstCancelConstants implements BaseEnum<TimeruleInstState, String> {
    SUBMITTED("已提交", "0"),
    ACCEPTED("已接收", "1"),
    PASS("已通过", "2"),
    NOT_PASS("未通过", "3"),
    INVALID("无效", "4");

    private String name;
    private String value;

    ApplyinstCancelConstants(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
