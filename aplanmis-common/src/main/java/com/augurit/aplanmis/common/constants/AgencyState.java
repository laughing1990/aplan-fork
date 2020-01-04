package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@ApiModel(value = "代办申请状态枚举", description = "代办申请状态枚举:WAIT_SIGNING||SIGNING||APPLYER_SIGNING...")
public enum AgencyState implements BaseEnum<TimeruleInstState, String> {

    @ApiModelProperty(value = "WAIT_SIGNING", name = "待签订", allowableValues = "WAIT_SIGNING")
    WAIT_SIGNING("待签订", "1"),

    @ApiModelProperty(value = "SIGNING", name = "签订中", allowableValues = "SIGNING")
    SIGNING("签订中", "2"),//项目代办列表点击签订协议修改代办申请状态为2

    @ApiModelProperty(value = "APPLYER_SIGNING", name = "待申请人签章", allowableValues = "APPLYER_SIGNING")
    APPLYER_SIGNING("待申请人签章", "3"),

    @ApiModelProperty(value = "SIGN_COMPLETED", name = "已签订（代办中）", allowableValues = "SIGN_COMPLETED")
    SIGN_COMPLETED("已签订（代办中）", "4"),

    @ApiModelProperty(value = "STOP", name = "代办终止", allowableValues = "STOP")
    STOP("代办终止", "5");

    private String name;
    private String value;

    AgencyState(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static AgencyState fromValue(String value) {
        for (AgencyState state : AgencyState.values()) {
            if (value.equals(state.getValue())) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknow agency state: value: " + value);
    }

    public static Map<String,String> getAgencyStateMap() {
        Map<String,String> map = new HashMap<>();
        for (AgencyState state : AgencyState.values()) {
            map.put(state.getValue(),state.getName());
        }
        return map;
    }
}

