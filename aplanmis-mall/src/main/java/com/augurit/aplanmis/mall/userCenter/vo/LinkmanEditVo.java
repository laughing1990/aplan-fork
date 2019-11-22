package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑联系人vo")
public class LinkmanEditVo {

    @ApiModelProperty(value = "联系人id", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "联系人类型,c表示个人联系人，u表示企业联系人'", required = true, allowableValues = "c, u")
    private String linkmanType;

    @ApiModelProperty(value = "联系人姓名", required = true)
    private String linkmanName;

    @ApiModelProperty(value = "证件号", required = true)
    private String linkmanCertNo;

    @ApiModelProperty(value = "手机号码", required = true)
    private String linkmanMobilePhone;

    @ApiModelProperty(value = "办公电话", required = true)
    private String linkmanOfficePhon;

    @ApiModelProperty(value = "传真", required = true)
    private String linkmanFax;

    @ApiModelProperty(value = "电子邮件", required = true)
    private String linkmanMail;

    @ApiModelProperty(value = "联系人住址", required = true)
    private String linkmanAddr;

    @ApiModelProperty(value = "备注")
    private String linkmanMemo;

    public AeaLinkmanInfo mergeAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        aeaLinkmanInfo.setLinkmanType(this.linkmanType);
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanOfficePhon(this.linkmanOfficePhon);
        aeaLinkmanInfo.setLinkmanFax(this.linkmanFax);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        aeaLinkmanInfo.setLinkmanAddr(this.linkmanAddr);
        aeaLinkmanInfo.setLinkmanMemo(this.linkmanMemo);
        return aeaLinkmanInfo;
    }
}
