package com.augurit.aplanmis.rest.guide.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;

public enum ThemeType implements BaseEnum<ThemeType, String> {
    APPROVE_THEME("审批类", "A00001"),
    CHECK_THEME("核准类", "A00002"),
    BACKUP_THEME("备案类", "A00003");

    private String name;
    private String value;

    ThemeType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
