package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("领件人vo")
public class SmsInfoVo extends AeaProjInfo {
    @ApiModelProperty(value = "保存到数据表中的标识, 更新领件人时需要传, 保存不用", dataType = "string", allowEmptyValue = true)
    private String id;

    @ApiModelProperty(value = "领取模式: 0,邮政快递; 1, 窗口取证", required = true, allowableValues = "0, 1")
    @NotEmpty(message = "receiveMode is null")
    private String receiveMode;

    @ApiModelProperty(value = "快递类型: 1, 普通快递; 2, 上门取件", allowableValues = "1, 2")
    private String smsType;

    @ApiModelProperty(value = "收件人名字", required = true, example = "小明")
    @NotEmpty(message = "addressName is null")
    private String addresseeName;

    @ApiModelProperty(value = "收件人联系方式", required = true, example = "18810011001")
    @NotEmpty(message = "addresseePhone is null")
    private String addresseePhone;

    @ApiModelProperty(value = "收件人身份证", required = true, example = "360423189821340987")
    @NotEmpty(message = "addresseeIdcard is null")
    private String addresseeIdcard;

    @ApiModelProperty(value = "收件人所在省份", example = "广东省")
    private String addresseeProvince;

    @ApiModelProperty(value = "收件人所在城市", example = "广州市")
    private String addresseeCity;

    @ApiModelProperty(value = "收件人区县", example = "天河区")
    private String addresseeCounty;

    @ApiModelProperty(value = "收件人详细地址", example = "广东省广州市天河区1029号")
    private String addresseeAddr;

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;


    public AeaHiSmsInfo toSmsInfo() {
        AeaHiSmsInfo aeaHiSmsInfo = new AeaHiSmsInfo();
        aeaHiSmsInfo.setId(UuidUtil.generateUuid());
        aeaHiSmsInfo.setReceiveMode(this.receiveMode);
        aeaHiSmsInfo.setSmsType(this.smsType);
        aeaHiSmsInfo.setAddresseeName(this.addresseeName);
        aeaHiSmsInfo.setAddresseePhone(this.addresseePhone);
        aeaHiSmsInfo.setApplyinstId(this.applyinstId);
        aeaHiSmsInfo.setAddresseeProvince(this.addresseeProvince);
        aeaHiSmsInfo.setAddresseeCity(this.addresseeCity);
        aeaHiSmsInfo.setAddresseeCounty(this.addresseeCounty);
        aeaHiSmsInfo.setAddresseeAddr(this.addresseeAddr);
        return aeaHiSmsInfo;
    }

    public AeaHiSmsInfo merge(AeaHiSmsInfo aeaHiSmsInfo) {
        aeaHiSmsInfo.setReceiveMode(this.receiveMode);
        aeaHiSmsInfo.setSmsType(this.smsType);
        aeaHiSmsInfo.setAddresseeName(this.addresseeName);
        aeaHiSmsInfo.setAddresseePhone(this.addresseePhone);
        aeaHiSmsInfo.setApplyinstId(this.applyinstId);
        aeaHiSmsInfo.setAddresseeProvince(this.addresseeProvince);
        aeaHiSmsInfo.setAddresseeCity(this.addresseeCity);
        aeaHiSmsInfo.setAddresseeCounty(this.addresseeCounty);
        aeaHiSmsInfo.setAddresseeAddr(this.addresseeAddr);
        return aeaHiSmsInfo;
    }
}
