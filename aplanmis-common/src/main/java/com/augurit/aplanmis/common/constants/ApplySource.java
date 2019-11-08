package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author tiantian
 * @date 2019/9/9
 */
@Getter
public enum  ApplySource implements BaseEnum<TimeruleInstState, String> {
    WIN("窗口申报","win"),
    NET("网上申报","net");

    private String name;
    private String value;

    ApplySource(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ApplySource getApplySource(String value){
        ApplySource[] applySources = ApplySource.values();
        for(ApplySource applySource : applySources){
            if(applySource.getValue().equals(value)){
                return applySource;
            }
        }

        return null;
    }
}
