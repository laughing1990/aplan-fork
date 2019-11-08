package com.augurit.aplanmis.front.subject.linkman.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("联系人信息")
public class LinkmanVo {
    @ApiModelProperty(value = "联系人ID")
    private String addressId;
    @ApiModelProperty(value = "联系人姓名")
    private String addressName;
    @ApiModelProperty(value = "手机号码")
    private String addressPhone;
    @ApiModelProperty(value = "证件号")
    private String addressIdCard;
    @ApiModelProperty(value = "电子邮件")
    private String addressMail;

    public static LinkmanVo from(AeaLinkmanInfo aeaLinkmanInfo) {
        LinkmanVo linkmanVo = new LinkmanVo();
        linkmanVo.setAddressId(aeaLinkmanInfo.getLinkmanInfoId());
        linkmanVo.setAddressPhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        linkmanVo.setAddressName(aeaLinkmanInfo.getLinkmanName());
        linkmanVo.setAddressIdCard(aeaLinkmanInfo.getLinkmanCertNo());
        linkmanVo.setAddressMail(aeaLinkmanInfo.getLinkmanMail());
        return linkmanVo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkmanVo linkmanVo = (LinkmanVo) o;
        return addressId.equals(linkmanVo.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId);
    }
}
