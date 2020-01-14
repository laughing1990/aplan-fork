package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("寄件信息vo")
public class CertMailListVo {

    @ApiModelProperty(value = "事项出件主键id")
    private String sendItemId;

    @ApiModelProperty(value = "申报实例id")
    private String applyinstId;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "运单号")
    private String expressNum;

    @ApiModelProperty(value = "物流公司")
    private String company;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "寄件人")
    private String sender;

    @ApiModelProperty(value = "收件人")
    private String receiver;

    @ApiModelProperty(value = "备注")
    private String remark;

    public static CertMailListVo from(AeaHiSmsSendItem aeaHiSmsSendItem) {
        CertMailListVo certMailListVo = new CertMailListVo();
        certMailListVo.setSendItemId(aeaHiSmsSendItem.getSendItemId());
        certMailListVo.setApplyinstId(aeaHiSmsSendItem.getApplyinstId());
        certMailListVo.setStatus("派送中");
        certMailListVo.setExpressNum(aeaHiSmsSendItem.getExpressNum());
        certMailListVo.setCompany("中国邮政");
        certMailListVo.setPhone("10000000");
        certMailListVo.setSender(aeaHiSmsSendItem.getSenderName());
        certMailListVo.setReceiver(aeaHiSmsSendItem.getAddresseeName());
        certMailListVo.setRemark("急急急");
        return certMailListVo;
    }
}
