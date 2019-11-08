package com.augurit.aplanmis.mall.userCenter.constant;

import lombok.Getter;
@Getter
public enum ItemStateTypeEnum {

        HADONE("已办", "0"),
        HAVENOTDONE("未办", "1"),
        NOTMUSTDONE("不用办", "2");

        private String name;
        private String value;

    ItemStateTypeEnum(String name, String value) {
            this.name = name;
            this.value = value;
        }
}
