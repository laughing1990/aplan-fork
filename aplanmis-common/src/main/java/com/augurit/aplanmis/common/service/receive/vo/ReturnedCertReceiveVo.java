package com.augurit.aplanmis.common.service.receive.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnedCertReceiveVo extends ReceiveBaseVo {
    @ApiModelProperty(value = "领证人")
    private String issueUserName;

    @ApiModelProperty(value = "领证人手机号码")
    private String issueUserMobile;

    @ApiModelProperty(value = "收件地址")
    private String serviceAddress;
}
