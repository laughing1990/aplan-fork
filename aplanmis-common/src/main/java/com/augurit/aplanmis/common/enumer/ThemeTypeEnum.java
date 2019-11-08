package com.augurit.aplanmis.common.enumer;

public enum ThemeTypeEnum {

    ONE("A00001","政府投资类"),
    TWO("A00002","预先服务协同"),
    THREE("A00003","社会投资类"),
    OTHER("A00004","其他类");



    private String themeTypeCode;
    private String themeTypeName;

    private ThemeTypeEnum(String themeTypeCode,String themeTypeName){
        this.themeTypeCode  = themeTypeCode;
        this.themeTypeName = themeTypeName;
    }

    public static String getName(String themeTypeCode) {
        for (ThemeTypeEnum c : ThemeTypeEnum.values()) {
            if (c.themeTypeCode.equals(themeTypeCode)) {
                return c.themeTypeName;
            }
        }
        return null;
    }
}
