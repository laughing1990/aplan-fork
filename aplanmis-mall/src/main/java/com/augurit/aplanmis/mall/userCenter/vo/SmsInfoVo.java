package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
@ApiModel("领件人vo")
public class SmsInfoVo{
    @ApiModelProperty(value = "主键smsId", dataType = "string", allowEmptyValue = true)
    private String id;

    @ApiModelProperty(value = "签收方式: 0,邮政快递; 1, 窗口取证 2 网上签收", required = true, allowableValues = "0, 1")
    @NotEmpty(message = "receiveMode is null")
    private String receiveMode;

    @ApiModelProperty(value = "领取方式， 1：一次领取，0：多次领取",  allowableValues = "0, 1")
    private String receiveType;


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
        return setAeaHiSmsInfo(aeaHiSmsInfo);
    }

    public AeaHiSmsInfo merge(AeaHiSmsInfo aeaHiSmsInfo) {
        return setAeaHiSmsInfo(aeaHiSmsInfo);
    }

    private AeaHiSmsInfo setAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) {
        aeaHiSmsInfo.setReceiveMode(this.receiveMode);
        aeaHiSmsInfo.setReceiveType(this.receiveType);
        aeaHiSmsInfo.setAddresseeName(this.addresseeName);
        aeaHiSmsInfo.setAddresseePhone(this.addresseePhone);
        aeaHiSmsInfo.setApplyinstId(this.applyinstId);
        aeaHiSmsInfo.setAddresseeProvince(this.addresseeProvince);
        aeaHiSmsInfo.setAddresseeCity(this.addresseeCity);
        aeaHiSmsInfo.setAddresseeCounty(this.addresseeCounty);
        aeaHiSmsInfo.setAddresseeAddr(this.addresseeAddr);
        aeaHiSmsInfo.setAddresseeIdcard(this.addresseeIdcard);
        return aeaHiSmsInfo;
    }
}
