package com.augurit.aplanmis.data.exchange.constant;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/11/18
 */
@Getter
public enum TableName implements BaseEnum<EtlError, String> {

    SPGL_DFXMSPLCJDSXXXB(TableNameConstant.SPGL_DFXMSPLCJDSXXXB, StepNameConstant.STAGE_ITEM_STEP),
    SPGL_DFXMSPLCJDXXB(TableNameConstant.SPGL_DFXMSPLCJDXXB, StepNameConstant.STAGE_STEP),
    SPGL_DFXMSPLCXXB(TableNameConstant.SPGL_DFXMSPLCXXB, StepNameConstant.THEME_VER_STEP),
    SPGL_XMDWXXB(TableNameConstant.SPGL_XMDWXXB, StepNameConstant.APPLY_PROJ_UNIT_STEP),
    SPGL_XMJBXXB(TableNameConstant.SPGL_XMJBXXB, StepNameConstant.APPLY_PROJ_STEP),
    SPGL_XMQTFJXXB(TableNameConstant.SPGL_XMQTFJXXB, StepNameConstant.ITEM_OTHERS_MAT_STEP),
    SPGL_XMSPSXBLXXB(TableNameConstant.SPGL_XMSPSXBLXXB, StepNameConstant.ITEMINST_STEP),
    SPGL_XMSPSXBLXXXXB(TableNameConstant.SPGL_XMSPSXBLXXXXB, StepNameConstant.ITEM_OPINION_STEP),
    SPGL_XMSPSXPFWJXXB(TableNameConstant.SPGL_XMSPSXPFWJXXB, StepNameConstant.OFFICIAL_DOC_STEP),
    SPGL_DFGHKZXXXB(TableNameConstant.SPGL_DFGHKZXXXB, StepNameConstant.PLAN_CONTROL_LINE_STEP),
    SPGL_XMQQYJXXB(TableNameConstant.SPGL_XMQQYJXXB, StepNameConstant.PRE_IDEA_STEP),
    SPGL_XMYDHXJZXXB(TableNameConstant.SPGL_XMYDHXJZXXB, StepNameConstant.LAND_RED_LINE_STEP),
    SPGL_XMSPSXBLTBCXXXB(TableNameConstant.SPGL_XMSPSXBLTBCXXXB, StepNameConstant.ITEM_SPECIAL_STEP),
    SPGL_ZJFWSXBLXXB(TableNameConstant.SPGL_ZJFWSXBLXXB, StepNameConstant.PROJ_PURCHASE_STEP),
    SPGL_ZJFWSXBLGCXXB(TableNameConstant.SPGL_ZJFWSXBLGCXXB, StepNameConstant.PROJ_PURCHASE_OPINION_STEP);
    private String name;
    private String value;

    TableName(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (TableName tableName : TableName.values()) {
            map.put(tableName.getName(), tableName.getValue());
        }
        return map;
    }
}
