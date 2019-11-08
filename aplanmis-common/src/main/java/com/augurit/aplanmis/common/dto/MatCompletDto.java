package com.augurit.aplanmis.common.dto;

import lombok.Data;

/**
 * 网厅材料补全实体
 */
@Data
public class MatCompletDto {
    private String isSeriesApprove;//是否串联审批  1 串联  0并联
    private String applyinstId;//申请实例ID
    private String applyinstCode;//申报流水号
    private String projName;//项目名称
    private String opsUserOpinion;//意见
    private String iteminstId;//事项实例ID
    private String stageName;//阶段名称
    private String iteminstName;//事项实例名称
}
