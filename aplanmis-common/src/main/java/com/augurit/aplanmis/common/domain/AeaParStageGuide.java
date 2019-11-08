package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AeaParStageGuide implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guideId;
    private String stageId;
    private String applyObj;
    private String acceptCond;
    private double legalDay;
    private String legalType;
    private String legalDesc;
    private double promiseDay;
    private String promiseType;
    private String promiseDesc;
    private String comsupAddress;
    private String comsupEmail;
    private String comsupLetter;
    private String comsupTel;
    private String comsupOnline;
    private String ckbllc;
    private String ckbllct;
    private String wsbllc;
    private String wsbllct;
    private String handleFlow;
    private String handleTimelimitDesc;
    private String guideDemo;
    private String warmPrompt;
    private String guideAtt;
    private String rootOrgId;

    /**
     * 扩展字段
     */
    private int ckbllctNum = 0;
    private int wsbllctNum = 0;
    private int guideAttNum = 0;
}
