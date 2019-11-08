package com.augurit.aplanmis.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CorrectinstDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String applyinstId;
    private String iteminstId;
    private String projInfoId;
    private String taskinstId;
    private String isFlowTrigger;
    private Long correctDueDays;
    private String correctMemo;
    private String correctState;
    private String matCorrectDtosJson;
}
