package com.augurit.aplanmis.front.certificate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("证件到达vo")
public class CertArrivedVo {

    @ApiModelProperty(value = "事项实例列表")
    private List<CertRegistrationItemVo> certRegistrationItemVos;

    @ApiModelProperty(value = "项目名称")
    private String projName;

    @ApiModelProperty(value = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "单位名称")
    private String unitInfoName;

    @ApiModelProperty(value = "申请人名称")
    private String linkmanInfoName;

    @ApiModelProperty(value = "申请人身份证号")
    private String linkmanIdCard;

    @ApiModelProperty(value = "申请人手机号")
    private String linkmanMobilephone;

}
