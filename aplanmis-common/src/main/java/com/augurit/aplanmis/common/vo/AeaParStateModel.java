package com.augurit.aplanmis.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linpx on 2018/11/5.
 */
public class AeaParStateModel implements Serializable {
    List<AeaParStateModel> list;

    private String parStateId; // (主键)
    private String stageId; // (阶段定义ID)
    private String stateName; // (条件名称)
    private String isQuestion; // (是否问题，0表示答案，1表示问题)
    private String answerType; // (问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案)
    private String mustAnswer; // (是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】)
    private String parentStateId; // (父ID)
    private String stateSeq; // (序列)
    private Long sortNo; // (排列顺序号)
    private Boolean isSelected;

    public List<AeaParStateModel> getList() {
        return list;
    }

    public void setList(List<AeaParStateModel> list) {
        this.list = list;
    }

    public String getParStateId() {
        return parStateId;
    }

    public void setParStateId(String parStateId) {
        this.parStateId = parStateId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getIsQuestion() {
        return isQuestion;
    }

    public void setIsQuestion(String isQuestion) {
        this.isQuestion = isQuestion;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getMustAnswer() {
        return mustAnswer;
    }

    public void setMustAnswer(String mustAnswer) {
        this.mustAnswer = mustAnswer;
    }

    public String getParentStateId() {
        return parentStateId;
    }

    public void setParentStateId(String parentStateId) {
        this.parentStateId = parentStateId;
    }

    public String getStateSeq() {
        return stateSeq;
    }

    public void setStateSeq(String stateSeq) {
        this.stateSeq = stateSeq;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
