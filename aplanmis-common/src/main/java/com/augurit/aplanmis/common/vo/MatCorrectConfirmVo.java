package com.augurit.aplanmis.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MatCorrectConfirmVo {
    private String iteminstId;
    private String applyinstCode;
    private String projName;
    private String linkmanName;
    private String isSeriesApprove;
    private String themeName;
    private String stageName;
    private String iteminstName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private String correctId;
    private String applySubject;
    private String iteminstCode;//办件编号
    private String localCode;
    private String gcbm;
}
