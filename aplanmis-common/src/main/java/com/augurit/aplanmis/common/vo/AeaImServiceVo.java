package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/10
 */
@Data
@ApiModel("中介服务及对应的资质信息")
public class AeaImServiceVo {

    @ApiModelProperty(value = "服务事项关联ID")
    private String serviceItemId;
    @ApiModelProperty(value = "服务ID")
    private String serviceId;
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;
    @ApiModelProperty(value = "服务编码")
    private String serviceCode; // (服务编码)
    @ApiModelProperty(value = "服务名称")
    private String serviceName; // (服务名称)

    @ApiModelProperty(value = "服务对应的资质列表")
    private List<AeaImQualVo> aeaImQualVos;

}
