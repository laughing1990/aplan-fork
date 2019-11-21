package com.augurit.aplanmis.rest.guide.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;

public enum ItemType implements BaseEnum<ItemType, String> {
    ADMINISTRATIVE_LICENSE("行政许可", "01"),
    ADMINISTRATIVE_PUNISHMENT("行政处罚", "02"),
    ADMINISTRATIVE_COERCION("行政强制", "03"),
    ADMINISTRATIVE_LEVY("行政征收", "04"),
    ADMINISTRATIVE_BENEFITS("行政给付", "05"),
    ADMINISTRATIVE_INSPECTION("行政检查", "06"),
    ADMINISTRATIVE_CONFIRMATION("行政确认", "07"),
    ADMINISTRATIVE_REWARD("行政奖励", "08"),
    ADMINISTRATIVE_ADJUDICATION("行政裁决", "09"),
    ADMINISTRATIVE_OTHERS("其他行政权力", "20"),
    COMMON_SERVICE("公共服务", "21"),
    UNKNOWN("未知", "00");

    private String name;
    private String value;

    ItemType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ItemType fromValue(String value) {
        for (ItemType itemType : ItemType.values()) {
            if (value.equals(itemType.getValue())) {
                return itemType;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
