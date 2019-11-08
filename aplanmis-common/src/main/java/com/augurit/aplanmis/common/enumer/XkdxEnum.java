package com.augurit.aplanmis.common.enumer;

public enum XkdxEnum {

    ONE("1","自然人"),
    TWO("2","企业法人"),
    THREE("3","行政机关"),
    FOUR("4","事业法人及其他社团组织"),
    FIVE("5","个人、企业和非企业（机关单位）"),;



    private String xkdxCode;
    private String xkdxName;

    private XkdxEnum(String xkdxCode, String xkdxName){
        this.xkdxCode  = xkdxCode;
        this.xkdxName = xkdxName;
    }

    public static String getName(String themeTypeCode) {
        for (XkdxEnum c : XkdxEnum.values()) {
            if (c.xkdxCode.equals(themeTypeCode)) {
                return c.xkdxName;
            }
        }
        return null;
    }
}
