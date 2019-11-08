package com.augurit.aplanmis.common.constants;

public enum MindType {

    ITEM("事项", "item"),
    SITUATION("情形", "situation"),
    STAGE("阶段", "stage"),
    MATERIAL("材料", "mat"),
    CERTIFICATE("证照", "cert")
    ;

    private String name;
    private String value;

    MindType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
