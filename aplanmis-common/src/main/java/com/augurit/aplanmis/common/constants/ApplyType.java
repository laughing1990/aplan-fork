package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author tiantian
 * @date 2019/9/9
 */
@Getter
public enum ApplyType  implements BaseEnum<TimeruleInstState, String> {
    SERIES("单项", "1"),
    UNIT("并联", "0");

    private String name;
    private String value;

    ApplyType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /// 用到的时候再放开
    /*public static ApplyType getApplyType(String value){
        ApplyType[] applyTypes = ApplyType.values();
        for(ApplyType applyType : applyTypes){
            if(applyType.getValue().equals(value)){
                return applyType;
            }
        }

        return null;
    }*/
}
