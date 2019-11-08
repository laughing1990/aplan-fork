package com.augurit.aplanmis.common.event;

import lombok.Getter;

/**
 * 事件类型
 */
@Getter
public enum AplanmisEventType {

    // 申报相关 APPLY_前缀
    APPLY_START("申报开始", "APPLY_START"),
    APPLY_ACCEPT("受理", "APPLY_ACCEPT"),

    APPLY_OUT_SCOPE("不予受理", "APPLY_OUT_SCOPE"),

    APPLY_CORRECT_MATERIAL_START("补正开始", "APPLY_CORRECT_MATERIAL_START"),

    APPLY_CORRECT_MATERIAL_END("补正结束", "APPLY_CORRECT_MATERIAL_END"),

    APPLY_SPECIFIC_PROC_START("特程开始", "APPLY_SPECIFIC_PROC_START"),

    APPLY_SPECIFIC_PROC_END("特程结束", "APPLY_SPECIFIC_PROC_END"),

    APPLY_AGREE("通过", "APPLY_AGREE"),
    APPLY_AGREE_TOLERANCE("容缺通过", "APPLY_AGREE_TOLERANCE"),
    APPLY_DISAGREE("不通过", "APPLY_DISAGREE"),

    APPLY_COMPLETED("已办结", "APPLY_COMPLETED"),

    // 状态相关
    STATE_ITEM_COMPLETED("事项办结通过", "ITEM_COMPLETED"),

    Apply_ACCEPT_DEAL("窗口受理", "APPLY_ACCEPT_DEAL"),

    Apply_MAIL_QU_JIAN("邮寄方式取件", "Apply_MAIL_QU_JIAN"),

    // 监控相关 MONITOR_前缀
    MONITOR_CASE_OVERDUE("办件逾期", "MONITOR_CASE_OVERDUE"),

    //流程相关
    BPM_NODE_SEND("流程办理", "BPM_NODE_SEND"),
    BPM_REMIND_SEND("流程催办提醒", "BPM_REMIND_SEND"),

    //审批超时
    APPROVE_OVER_TIME("审批超时", "APPROVE_OVER_TIME"),
    //预警办件
    WARNING_TASK("预警办件", "WARNING_TASK"),

    //超期默许（仅用于唐山）
    TACITLY("超期默许", "TACITLY");


    private String desc;
    private String value;

    AplanmisEventType(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
