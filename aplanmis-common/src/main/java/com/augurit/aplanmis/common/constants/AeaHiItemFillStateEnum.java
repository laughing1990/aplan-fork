package com.augurit.aplanmis.common.constants;

import lombok.Getter;

/**
 * 容缺补齐状态枚举
 */
@Getter
public enum AeaHiItemFillStateEnum {

    UN_START("未开始","1"),
    DOING("补齐中","2"),
    COMPLETED("补齐完毕","3");


    private String name;
    private String value;

    AeaHiItemFillStateEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
