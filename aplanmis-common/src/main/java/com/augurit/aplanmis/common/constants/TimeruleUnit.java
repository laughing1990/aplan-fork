package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * 时限单位
 * @author tiantian
 * @date 2019/9/9
 */
@Getter
public enum TimeruleUnit implements BaseEnum<TimeruleInstState, String> {

    ND("自然日", "ND"),
    WD("工作日", "WD"),
    NH("小时（自然日）", "NH"),
    WH("小时（工作日）", "WH");

    private String name;
    private String value;

    TimeruleUnit(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static TimeruleUnit getTimeruleUnit(String value){
        TimeruleUnit[] timeruleUnits = TimeruleUnit.values();
        for(TimeruleUnit timeruleUnit : timeruleUnits){
            if(timeruleUnit.getValue().equals(value)){
                return timeruleUnit;
            }
        }

        return null;
    }

    public static boolean isHourUnit(String value){
        if(NH.value.equals(value)){
            return true;
        }

        if(WH.value.equals(value)){
            return true;
        }

        return false;

    }

    public static boolean isDayUnit(String value){
        if(ND.value.equals(value)){
            return true;
        }

        if(WD.value.equals(value)){
            return true;
        }

        return false;

    }
}
