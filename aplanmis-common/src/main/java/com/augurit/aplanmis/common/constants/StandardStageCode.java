package com.augurit.aplanmis.common.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/8/14
 */
@Getter
public enum StandardStageCode {
    //1 立项用地规划许可 2 工程建设许可 3 施工许可 4 竣工验收 5 并行推进
    LXYDGHXK("立项用地规划许可", "1"),
    GCJSXK("工程建设许可", "2"),
    SGXK("施工许可", "3"),
    JGYS("竣工验收", "4");
    //BXTJ("并行推进", "5");

    private String name;
    private String value;

    StandardStageCode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (StandardStageCode standardStageCode : StandardStageCode.values()) {
            map.put(standardStageCode.getValue(), standardStageCode.getName());
        }
        return map;
    }
}
