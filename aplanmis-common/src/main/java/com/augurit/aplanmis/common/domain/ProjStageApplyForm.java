package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ProjStageApplyForm {
    private String applyinstId;
    private String projInfoId;
    private String stageId;
    private String lcbsxlx;
    private String dybzspjdxh;
    private Date acceptTime;
}
