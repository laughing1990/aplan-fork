package com.augurit.aplanmis.data.exchange.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * @author yinlf
 * @Date 2019/11/8
 */
@Getter
public enum SpglCheckError implements BaseEnum<EtlError, String> {

    NKGSJ_MORE_THAN_NJCSJ("130", "NKGSJ时间要早于NJCSJ时间");

    private String name;
    private String value;

    SpglCheckError(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
