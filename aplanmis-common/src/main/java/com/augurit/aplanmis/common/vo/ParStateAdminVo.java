package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaParState;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linpx on 2018/11/6.
 */
public class ParStateAdminVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AeaParState> answerList;   //答案情形的列表
    private AeaParState questionState;  //问题情形
    private String parentQuestionStateId;   //问题情形的父情形ID

    public List<AeaParState> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AeaParState> answerList) {
        this.answerList = answerList;
    }

    public AeaParState getQuestionState() {
        return questionState;
    }

    public void setQuestionState(AeaParState questionState) {
        this.questionState = questionState;
    }

    public String getParentQuestionStateId() {
        return parentQuestionStateId;
    }

    public void setParentQuestionStateId(String parentQuestionStateId) {
        this.parentQuestionStateId = parentQuestionStateId;
    }
}
