package com.augurit.aplanmis.supermarket.apply.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "申报结果返回数据", description = "申报结果返回数据")
@Data
public class ApplyinstResult {
    //private String procInstId;
    //    private String iteminstId;
    //private String appinstId;
    private String applyinstId;
    private String applyinstCode;
    //private String isSeriesApprove;//是否单项申报 1是  0否
}
