package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("个人申报时申请人和联系人vo")
public class PersonalInfoVo {
    public static final String LINK = "link";
    public static final String APPLY = "apply";

    @ApiModelProperty(value = "申请人或联系人id", required = true, dataType = "string")
    private String linkmanInfoId;

    @ApiModelProperty(value = "申请人或联系人名称", required = true, dataType = "string", example = "小明")
    @NotEmpty(message = "linkmanName is null")
    private String linkmanName;

    @ApiModelProperty(value = "申请人或联系人身份证号", required = true, dataType = "string", example = "123456789123456789")
    @NotEmpty(message = "linkmanCertNo is null")
    private String linkmanCertNo;

    @ApiModelProperty(value = "申请人或联系人电话", required = true, dataType = "string", example = "10086")
    @NotEmpty(message = "linkmanMobilePhone is null")
    private String linkmanMobilePhone;

    @ApiModelProperty(value = "申请人或联系人邮箱", dataType = "string", example = "1234@163.com")
    private String linkmanMail;

    @ApiModelProperty(value = "申请人: apply, 联系人: link", required = true, dataType = "string", allowableValues = "apply, link", example = "apply")
    @NotEmpty(message = "applyOrLinkType is null")
    private String applyOrLinkType;

    @ApiModelProperty(value = "项目id", dataType = "string")
    private String projInfoId;

    public AeaLinkmanInfo toAeaLinkmanInfo() {
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanInfoId(UuidUtil.generateUuid());
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        aeaLinkmanInfo.setLinkmanType("c");
        return aeaLinkmanInfo;
    }

    public void merge(AeaLinkmanInfo aeaLinkmanInfo) {
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        aeaLinkmanInfo.setLinkmanType("c");
    }

    public AeaLinkmanInfo toQueryParam() {
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        return aeaLinkmanInfo;
    }
}
