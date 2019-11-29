package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * 人员类型
 **/
@Getter
public enum UnitNatureType implements BaseEnum<UnitNatureType, String> {

    QY("企业", "1"),
    SYDW("事业单位", "2"),
    SHZJ("社会组织", "3");


    private String name;
    private String value;

    UnitNatureType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
