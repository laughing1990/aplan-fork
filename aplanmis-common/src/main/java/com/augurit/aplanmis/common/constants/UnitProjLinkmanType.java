package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

/**
 * 人员类型
 **/
@Getter
public enum UnitProjLinkmanType implements BaseEnum<UnitProjLinkmanType, String> {

    FZR("负责人", "1"),
    ZJLGCS("总监理工程师", "2"),
    XMZJ("项目总监", "3"),
    JSFZR("技术负责人", "4"),
    ZYSJFZR("专业设计负责人", "5"),
    ZYSJRY("专业设计人员", "6"),
    TSJGSCR("图审机构审查人", "7"),
    TSJGSDR("图审机构审定人", "8"),
    AQY("安全员", "9"),
    ZLQ("质量员", "10"),
    SGY("施工员", "11"),
    QYY("取样员", "12"),
    ZLY("资料员", "13"),
    LWY("劳务员", "14"),
    JLGCS("监理工程师", "15"),
    JLY("监理员", "16"),
    JZY("见证员", "17");


    private String name;
    private String value;

    UnitProjLinkmanType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
