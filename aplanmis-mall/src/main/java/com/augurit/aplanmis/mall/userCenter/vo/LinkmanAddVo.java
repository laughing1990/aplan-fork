package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新建联系人")
public class LinkmanAddVo {
    @ApiModelProperty(value = "联系人类型,c表示个人联系人，u表示企业联系人'", required = true, allowableValues = "c, u")
    private String linkmanType;

    @ApiModelProperty(value = "所在单位id")
    private String unitInfoId;

    @ApiModelProperty(value = "项目id，个人申报用")
    private String projInfoId;

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

    public AeaLinkmanInfo toAeaLinkmanInfo() {
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanType(this.linkmanType);
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanAddr(this.linkmanAddr);
        aeaLinkmanInfo.setLinkmanOfficePhon(this.linkmanOfficePhon);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanFax(this.linkmanFax);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setIsActive("1");
        aeaLinkmanInfo.setIsDeleted("0");
        aeaLinkmanInfo.setLinkmanMemo(this.linkmanMemo);
        return aeaLinkmanInfo;
    }
}
