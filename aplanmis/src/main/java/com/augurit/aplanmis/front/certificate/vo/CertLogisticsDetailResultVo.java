package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("物流跟踪详细信息")
public class CertLogisticsDetailResultVo {

    @ApiModelProperty(value = "物流系统返回的物流信息")
    private List<LogisticsOrderDetailVo> logisticsOrderDetails;

    @ApiModelProperty(value = "邮件状态")
    private String status;

    @ApiModelProperty(value = "证件信息")
    private List<AeaHiCertinst> aeaHiCertinsts;

    @ApiModelProperty(value = "寄件人、收件人信息")
    private AeaHiSmsSendItem aeaHiSmsSendItem;

}
