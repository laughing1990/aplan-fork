package com.augurit.aplanmis.common.service.projAccept.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OpinionSummaryVo implements Serializable {
    private Integer sortNum;
    private String deptName;
    private String opinion;
}
