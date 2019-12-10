package com.augurit.aplanmis.supermarket.bidProPurchase;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
public class ServiceLinkmanVo {
    private java.lang.String linkmanInfoId;

    @ApiModelProperty("联系人姓名")
    private java.lang.String linkmanName;


    @ApiModelProperty("办公电话")
    private java.lang.String linkmanOfficePhon;

    @ApiModelProperty("手机号码")
    private java.lang.String linkmanMobilePhone;

    @ApiModelProperty("电子邮件")
    private java.lang.String linkmanMail;
    @ApiModelProperty("证件号")
    private java.lang.String linkmanCertNo;

    public ServiceLinkmanVo() {
    }

    public ServiceLinkmanVo(AeaLinkmanInfo info) {
        BeanUtils.copyProperties(info, this);
    }

    public static List<ServiceLinkmanVo> change2vo(List<AeaLinkmanInfo> linkmanInfos) {
        List<ServiceLinkmanVo> list = new ArrayList<>();
        for (AeaLinkmanInfo info : linkmanInfos) {
            ServiceLinkmanVo vo = new ServiceLinkmanVo(info);
            list.add(vo);
        }
        return list;
    }
}
