package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AeaItemGuideConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rowguid; // ()
    private String itemVerId; // (事项版本id)
    private String conditionName; // (材料目录情形简称)
    private String conditionDesc; // (材料目录情形描述)
    private Long ordernumber; // ()
    private String rootOrgId;

}
