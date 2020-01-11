package com.augurit.aplanmis.front.certificate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("物流跟踪查询vo")
public class CertLogisticsDetailVo {

    @ApiModelProperty(value = "事项实例id")
    private String iteminstId;

    @ApiModelProperty(value = "申报实例id")
    private String applyinstId;

    @ApiModelProperty(value = "快递单号")
    private String expressNum;
}
