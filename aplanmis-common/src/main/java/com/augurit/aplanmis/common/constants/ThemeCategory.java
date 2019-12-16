package com.augurit.aplanmis.common.constants;

import lombok.Getter;

@Getter
public enum ThemeCategory {
    MAINLINE("并联申报", "mainline"),
    AUXILINE_51("辅线-多评合一", "51"),
    AUXILINE_52("辅线-方案联审", "52"),
    AUXILINE_53("辅线-联合审图", "53"),
    AUXILINE_54C("辅线-联合测绘", "54C"),
    AUXILINE_54Y("辅线-联合验收", "54Y"),
    OTHERS("辅线-其它", "others"),
    ;

    private String name;
    private String value;

    ThemeCategory(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ThemeCategory fromValue(String value) {
        for (ThemeCategory s : ThemeCategory.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("参数不正确，请检查");
    }


}
