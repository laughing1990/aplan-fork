package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum InOutType implements BaseEnum<InOutType, String> {
    MAT("材料", "mat"),
    CERT("证照", "cert")
    ;

    private String name;
    private String value;

    InOutType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
