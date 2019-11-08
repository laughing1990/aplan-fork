package com.augurit.aplanmis.front.apply.vo;

import lombok.Data;

@Data
public class SelectStateVo {
    /**
     * 选择的情形
     */
    private String stateId;
    /**
     * 选择的父情形
     */
    private String parentStateId;
}
