package com.augurit.aplanmis.rest.userCenter.vo;

import lombok.Data;

import java.util.List;

/**
 * 并联申报实例化后，返回对象实体
 */
@Data
public class StageApplyInstantiateResult {
    private List<String> procInstIds;
    private List<String> iteminstIds;
    private List<String> appinstIds;
    private List<String> applyinstIds;
    private List<String> applyinstCodeList;
    private ParallelApplyResultVo parallelApplyResultVo;
}
