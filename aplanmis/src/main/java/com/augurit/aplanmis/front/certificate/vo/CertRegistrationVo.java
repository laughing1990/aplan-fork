package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("取件登记vo")
public class CertRegistrationVo {

    @ApiModelProperty(value = "事项实例列表")
    private List<CertRegistrationItemVo> certRegistrationItemVos;

    @ApiModelProperty(value = "领件人、寄件人信息")
    private AeaHiSmsInfo smsInfo;
}
