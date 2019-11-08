package com.augurit.aplanmis.common.domain;

import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/8/14
 */
@Data
public class StageTotalForm {

    /**
     *  阶段名称
     */
    private String stageName;

    /**
     *  阶段排序号
     */
    private String sortNo;

    /**
     * 审批用时
     */
    private Integer avgUseDay;

    /**
     * 跨度用时
     */
    private Integer avgUseWorkDay;
}
