package com.augurit.aplanmis.front.certificate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("查看输出材料信息")
public class CertOutinstVo {

    @ApiModelProperty(value = "普通材料或者证照材料实例id", notes = "type为mat时, 普通材料; cert, 证照材料")
    private String id;

    @ApiModelProperty(value = "材料名称")
    private String name;

    @ApiModelProperty(value = "材料类型", notes = "mat: 普通材料; cert: 证照材料")
    private String type;

    @ApiModelProperty(value = "材料对象", notes = "aeaHiItemMatinst 或者 aeaHiCertinst")
    private Object data;
}
