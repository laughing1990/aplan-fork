package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * 广东三库一平台单位类型
 **/
@Getter
public enum GDUnitType implements BaseEnum<GDUnitType, String> {

    DEVELOPMENT_UNIT("建设单位", "1"),
    CONSTRUCTION_UNIT("施工单位", "2"),
    SURVEY_UNIT("勘察单位", "3"),
    DESIGN_UNIT("设计单位", "4"),
    SUPERVISION_UNIT("监理单位", "5"),
    AGENT_CONSTRUCTION_UNIT("代建单位", "6"),
    HANDLE_UNIT("经办单位", "7"),
    OTHERS("其他", "8"),
    BIDDING_AGENCY("招标代理机构", "21"),
    CONSTRUCTION_CONTRACTOR("施工总包", "22"),
    CONSTRUCTION_SUBCONTRACT("施工分包", "23"),
    LABOR_SUBCONTRACT("劳务分包", "24"),
    CONSTRUCTION_DRAWING_REVIEW("施工图审查", "25"),
    COST_CONSULTING("造价咨询", "26"),
    CONTRACTING_UNIT("承包单位", "27"),
    GENERAL_CONTRACTING_UNIT("工程总承包","28");

    private String name;
    private String value;

    GDUnitType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
