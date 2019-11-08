package com.augurit.aplanmis.common.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
@Getter
public enum StrandardThemeType {

    //1 政府投资工程建设项目（房屋建筑类）2 政府投资工程建设项目（线性工程类） 3 一般社会投资项目 4 小型社会投资项目 5 带方案出让用地的社会投资项目 6 工业类投资项目 7 交通工程类项目 8 水利工程类项目 9 能源工程类项目 10 其他
    GOV_INVEST_HOUSE("政府投资工程建设项目（房屋建筑类）","1"),
    GOV_INVEST_LINEAR("政府投资工程建设项目","2"),
    SOCIETY_INVEST_NORMAL("一般社会投资项目","3"),
    SOCIETY_INVEST_SMALL("小型社会投资项目","4"),
    SOCIETY_INVEST_LAND("带方案出让用地的社会投资项目","5"),
    INDUSTRY_INVEST("工业类投资项目","6"),
    TRAFFIC_ENGINEERING("交通工程类项目","7"),
    WATER_CONSERVANCY_ENGINEERING("水利工程类项目","8"),
    ENERGY_ENGINEERING("能源工程类项目","9"),
    OTHERS("其他","10");

    private String name;
    private String value;

    StrandardThemeType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        for (StrandardThemeType strandardThemeType : StrandardThemeType.values()) {
            map.put(strandardThemeType.getValue(), strandardThemeType.getName());
        }
        return map;
    }
}
