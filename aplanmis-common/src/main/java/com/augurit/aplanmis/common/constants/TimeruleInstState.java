package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 *
 * 时限实例状态
 * @author tiantian
 * @date 2019/9/9
 */
@Getter
public enum TimeruleInstState implements BaseEnum<TimeruleInstState, String> {
    NORMAL("正常", "1"),
    WARN("预警", "2"),
    OVERDUE("逾期", "3");

    private String name;
    private String value;

    TimeruleInstState(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static TimeruleInstState getTimeruleInstState(String value){
        TimeruleInstState[] timeruleInstStates = TimeruleInstState.values();
        for(TimeruleInstState timeruleInstState : timeruleInstStates){
            if(timeruleInstState.getValue().equals(value)){
                return timeruleInstState;
            }
        }

        return null;
    }
}