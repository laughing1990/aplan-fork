package com.augurit.aplanmis.front.apply.vo;

import lombok.Data;

/**
 * 单项申报实例化后，返回对象实体
 */
@Data
public class ApplyInstantiateResult {
    private String procInstId;
//    private String iteminstId;
    private String appinstId;
    private String applyinstId;
    private String applyinstCode;
    private String isSeriesApprove;//是否单项申报 1是  0否
}
