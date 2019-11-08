package com.augurit.aplanmis.common.dto;

import lombok.Data;

@Data
public class AeaItemStateDto {
    private String itemStateId;
    private String itemId;
    private String itemVerId;
    private String stateVerId;
    private String stateName;
    private String stateEl;
    private String sortNo;
    private String stateMemo;
    private String isActive;
    private String creater;
    private String createTime;
    private String modifier;
    private String modifyTime;
    private String userEl;
    private String parentStateId;
    private String stateSeq;
    private String isQuestion;
    private String answerType;
    private String mustAnswer;
    private String isDeleted;
    private String isProcStartCond;
}
