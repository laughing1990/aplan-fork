package com.augurit.aplanmis.common.qo.state;

import com.augurit.aplanmis.common.qo.BaseQo;


public class AeaItemStateQo {
    private String itemStateId;
    private String itemVerId;
    private String stateVerId;
    private String stateName;
    private String isActive;
    private String useEl;
    private String parentStateIdNullable;
    private String parentStateId;
    private String isQuestion;
    private String answerType;
    private String mustAnswer;
    private String isDeleted;
    private String isProcStartCond;

    private AeaItemStateQo() {
        this.isDeleted = "0";
        this.isActive = "1";
    }

    public AeaItemStateQo itemStateIdEq(String itemStateId) {
        this.itemStateId = itemStateId;
        return this;
    }

    public AeaItemStateQo itemVerIdEq(String itemVerId) {
        this.itemVerId = itemVerId;
        return this;
    }

    public AeaItemStateQo stateVerIdEq(String stateVerId) {
        this.stateVerId = stateVerId;
        return this;
    }

    public AeaItemStateQo stateNameLike(String stateName) {
        this.stateName = "%" + stateName + "%";
        return this;
    }

    public AeaItemStateQo isActiveEq(String isActive) {
        this.isActive = isActive;
        return this;
    }

    public AeaItemStateQo useElEq(String useEl) {
        this.useEl = useEl;
        return this;
    }

    public AeaItemStateQo parentStateIdEq(String parentStateId) {
        this.parentStateId = parentStateId;
        return this;
    }

    public AeaItemStateQo parentStateIdNullableIsNull() {
        this.parentStateIdNullable = BaseQo.IS_NULL;
        return this;
    }

    public AeaItemStateQo isQuestionEq(String isQuestion) {
        this.isQuestion = isQuestion;
        return this;
    }

    public AeaItemStateQo answerTypeEq(String answerType) {
        this.answerType = answerType;
        return this;
    }

    public AeaItemStateQo mustAnswerEq(String mustAnswer) {
        this.mustAnswer = mustAnswer;
        return this;
    }

    public AeaItemStateQo Eq(String isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public AeaItemStateQo isProcStartCondEq(String isProcStartCond) {
        this.isProcStartCond = isProcStartCond;
        return this;
    }

    public static AeaItemStateQo createMatInoutQuery() {
        return new AeaItemStateQo();
    }
}
