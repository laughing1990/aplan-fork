package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * @author yinlf
 * 事项状态
 * @Date 2019/7/11
 */
@Getter
@ApiModel(value = "事项状态枚举", description = "RECEIVE_APPLY||BACK_APPLY||ACCEPT_DEAL...")
public enum ItemStatus implements BaseEnum<TimeruleInstState, String> {
    @ApiModelProperty(value = "RECEIVE_APPLY", name = "已接件", allowableValues = "RECEIVE_APPLY")
    RECEIVE_APPLY("已接件", "1"),

    @ApiModelProperty(value = "BACK_APPLY", name = "已撤件", allowableValues = "BACK_APPLY")
    BACK_APPLY("已撤件", "2"),

    @ApiModelProperty(value = "ACCEPT_DEAL", name = "窗口受理", allowableValues = "ACCEPT_DEAL")
    ACCEPT_DEAL("窗口受理", "3"),

    @ApiModelProperty(value = "REFUSE_DEAL", name = "不受理", allowableValues = "REFUSE_DEAL")
    REFUSE_DEAL("不受理", "4"),

    @ApiModelProperty(value = "OUT_SCOPE", name = "不予受理", allowableValues = "OUT_SCOPE")
    OUT_SCOPE("不予受理", "5"),

    @ApiModelProperty(value = "CORRECT_MATERIAL_START", name = "补正（开始）", allowableValues = "CORRECT_MATERIAL_START")
    CORRECT_MATERIAL_START("补正（开始）", "6"),

    @ApiModelProperty(value = "CORRECT_MATERIAL_END", name = "补正（结束）", allowableValues = "CORRECT_MATERIAL_END")
    CORRECT_MATERIAL_END("补正（结束）", "7"),

    @ApiModelProperty(value = "DEPARTMENT_DEAL_START", name = "部门受理", allowableValues = "DEPARTMENT_DEAL_START")
    DEPARTMENT_DEAL_START("部门受理", "8"),

    @ApiModelProperty(value = "SPECIFIC_PROC_START", name = "特别程序（开始）", allowableValues = "SPECIFIC_PROC_START")
    SPECIFIC_PROC_START("特别程序（开始）", "9"),

    @ApiModelProperty(value = "SPECIFIC_PROC_END", name = "特别程序（结束）", allowableValues = "SPECIFIC_PROC_END")
    SPECIFIC_PROC_END("特别程序（结束）", "10"),

    @ApiModelProperty(value = "AGREE", name = "办结（通过）", allowableValues = "AGREE")
    AGREE("办结（通过）", "11"),

    @ApiModelProperty(value = "AGREE_TOLERANCE", name = "办结（容缺通过）", allowableValues = "AGREE_TOLERANCE")
    AGREE_TOLERANCE("办结（容缺通过）", "12"),

    @ApiModelProperty(value = "DISAGREE", name = "办结（不通过）", allowableValues = "DISAGREE")
    DISAGREE("办结（不通过）", "13"),

    @ApiModelProperty(value = "RECALL", name = "撤回", allowableValues = "RECALL")
    RECALL("撤回", "14"),

    @ApiModelProperty(value = "REVOKE", name = "撤销", allowableValues = "REVOKE")
    REVOKE("撤销", "15"),

    @ApiModelProperty(value = "APPLY_REVOKE", name = "撤件待确认", allowableValues = "APPLY_REVOKE")
    APPLY_REVOKE("撤件待确认", "16");

    private String name;
    private String value;

    ItemStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否办结状态
     * @param iteminstState
     * @return
     */
    public static boolean isEnd(String iteminstState) {
        if(
                AGREE.getValue().equals(iteminstState)
                        || AGREE_TOLERANCE.getValue().equals(iteminstState)
                        || DISAGREE.getValue().equals(iteminstState)) {
            return true;
        }
        return false;
    }

    /**
     * 是否通过状态
     *
     * @param iteminstState
     * @return
     */
    public static boolean isAgreed(String iteminstState) {
        if (
                AGREE.getValue().equals(iteminstState)
                        || AGREE_TOLERANCE.getValue().equals(iteminstState)) {
            return true;
        }
        return false;
    }

    public static boolean isHandling(String iteminstState) {
        if (!(BACK_APPLY.getValue().equals(iteminstState)
                || REFUSE_DEAL.getValue().equals(iteminstState)
                || OUT_SCOPE.getValue().equals(iteminstState)
                || RECALL.getValue().equals(iteminstState)
                || REVOKE.getValue().equals(iteminstState)) && !isEnd(iteminstState)) {
            return true;
        }
        return false;
    }

    public static ItemStatus fromValue(String value) {
        for (ItemStatus state : ItemStatus.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknow apply state: value: " + value);
    }
}

