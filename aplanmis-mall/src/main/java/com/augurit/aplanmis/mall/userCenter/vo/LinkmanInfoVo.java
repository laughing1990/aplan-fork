package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("联系人信息")
public class LinkmanInfoVo {
    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "联系人类型 c表示个人联系人，u表示企业联系人")
    private String linkmanType;
    @ApiModelProperty(value = "联系人姓名")
    private String linkmanName;
    @ApiModelProperty(value = "证件号")
    private String linkmanCertNo;
    @ApiModelProperty(value = "手机号码")
    private String linkmanMobilePhone;
    @ApiModelProperty(value = "办公电话")
    private String linkmanOfficePhon;
    @ApiModelProperty(value = "传真")
    private String linkmanFax;
    @ApiModelProperty(value = "电子邮件")
    private String linkmanMail;
    @ApiModelProperty(value = "联系人住址")
    private String linkmanAddr;
    @ApiModelProperty(value = "备注")
    private String linkmanMemo;

    public static LinkmanInfoVo fromAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        if (aeaLinkmanInfo == null) {
            return null;
        }
        LinkmanInfoVo linkmanInfoVo = new LinkmanInfoVo();
        linkmanInfoVo.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
        linkmanInfoVo.setLinkmanType(aeaLinkmanInfo.getLinkmanType());
        linkmanInfoVo.setLinkmanName(aeaLinkmanInfo.getLinkmanName());
        linkmanInfoVo.setLinkmanCertNo(aeaLinkmanInfo.getLinkmanCertNo());
        linkmanInfoVo.setLinkmanMobilePhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        linkmanInfoVo.setLinkmanOfficePhon(aeaLinkmanInfo.getLinkmanOfficePhon());
        linkmanInfoVo.setLinkmanFax(aeaLinkmanInfo.getLinkmanFax());
        linkmanInfoVo.setLinkmanMail(aeaLinkmanInfo.getLinkmanMail());
        linkmanInfoVo.setLinkmanAddr(aeaLinkmanInfo.getLinkmanAddr());
        linkmanInfoVo.setLinkmanMemo(aeaLinkmanInfo.getLinkmanMemo());
        return linkmanInfoVo;
    }
}
