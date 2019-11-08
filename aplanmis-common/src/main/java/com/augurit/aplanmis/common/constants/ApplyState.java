package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * @author qinjp.
 * 申请状态
 * @Date 2019/7/11
 */
@Getter
@ApiModel(value = "申报状态枚举", description = "申报状态枚举:RECEIVE_UNAPPROVAL_APPLY||RECEIVE_APPROVED_APPLY||ACCEPT_DEAL...")
public enum ApplyState implements BaseEnum<TimeruleInstState, String> {
    //已接件未审核（适用于网厅）、已接件并审核、已受理、待补全、已补全、不予受理、审批中、已办结
    @ApiModelProperty(value = "RECEIVE_UNAPPROVAL_APPLY", name = "已接件未审核", allowableValues = "RECEIVE_UNAPPROVAL_APPLY")
    RECEIVE_UNAPPROVAL_APPLY("已接件未审核", "0"),

    @ApiModelProperty(value = "RECEIVE_APPROVED_APPLY", name = "已接件并审核", allowableValues = "RECEIVE_APPROVED_APPLY")
    RECEIVE_APPROVED_APPLY("已接件并审核", "1"),

    @ApiModelProperty(value = "ACCEPT_DEAL", name = "已受理", allowableValues = "ACCEPT_DEAL")
    ACCEPT_DEAL("已受理", "2"),

    @ApiModelProperty(value = "IN_THE_SUPPLEMENT", name = "待补全", allowableValues = "IN_THE_SUPPLEMENT")
    IN_THE_SUPPLEMENT("待补全", "3"),//窗口收件材料不全，事项补正 不改变申报状态

    @ApiModelProperty(value = "SUPPLEMENTARY", name = "已补全", allowableValues = "SUPPLEMENTARY")
    SUPPLEMENTARY("已补全", "4"),//窗口

    @ApiModelProperty(value = "OUT_SCOPE", name = "不予受理", allowableValues = "OUT_SCOPE")
    OUT_SCOPE("不予受理", "5"),

    //    IN_THE_PROCESS_OF_APPROVAL("审批中", "6"),//去掉
    @ApiModelProperty(value = "COMPLETED", name = "已办结", allowableValues = "COMPLETED")
    COMPLETED("已办结", "6"),

    //增加中介超市里程碑状态 2019.11.6
    @ApiModelProperty(value = "IM_MILESTONE_CHOOSE_IMUNIT", name = "待选取中介机构", allowableValues = "IM_MILESTONE_CHOOSE_IMUNIT")
    IM_MILESTONE_CHOOSE_IMUNIT("待选取中介机构", "7"),

    @ApiModelProperty(value = "IM_MILESTONE_CONFIRM_IMUNIT", name = "待确认中介机构", allowableValues = "IM_MILESTONE_CONFIRM_IMUNIT")
    IM_MILESTONE_CONFIRM_IMUNIT("待确认中介机构", "8"),

    @ApiModelProperty(value = "IM_MILESTONE_UPLOAD_CONTRACT", name = "待上传合同", allowableValues = "IM_MILESTONE_UPLOAD_CONTRACT")
    IM_MILESTONE_UPLOAD_CONTRACT("待上传合同", "9"),

    @ApiModelProperty(value = "IM_MILESTONE_CONFIRM_CONTRACT", name = "待确认合同", allowableValues = "IM_MILESTONE_CONFIRM_CONTRACT")
    IM_MILESTONE_CONFIRM_CONTRACT("待确认合同", "10"),

    @ApiModelProperty(value = "IM_MILESTONE_UPLOAD_SERVICE_RESULT", name = "待上传服务结果", allowableValues = "IM_MILESTONE_UPLOAD_SERVICE_RESULT")
    IM_MILESTONE_UPLOAD_SERVICE_RESULT("待上传服务结果", "11");

    private String name;
    private String value;

    ApplyState(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static ApplyState fromValue(String value) {
        for (ApplyState state : ApplyState.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknow apply state: value: " + value);
    }
}

