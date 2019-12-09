package com.augurit.aplanmis.supermarket.projPurchase.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("项目详细信息，包括关联的企业信息和联系人信息")
public class ProjUnitLinkVo {
    @ApiModelProperty(value = "项目信息")
    private AeaProjInfo aeaProjInfo;
    @ApiModelProperty(value = "企业信息")
    private UnitInfoVo aeaUnitInfo;
    @ApiModelProperty(value = "联系人信息")
    private LinkmanVo aeaLinkmanInfo;
    @ApiModelProperty(value = "业主投诉电话")
    private String ownerComplaintPhone;

    public ProjUnitLinkVo() {
        this.aeaUnitInfo = new UnitInfoVo();
        this.aeaLinkmanInfo = new LinkmanVo();
        this.aeaProjInfo = new AeaProjInfo();
    }

    public void change2LinkmanVo(AeaLinkmanInfo info) {
        BeanUtils.copyProperties(info, this.aeaLinkmanInfo);
        this.ownerComplaintPhone = info.getLinkmanMobilePhone();
    }

    public void change2UnitVo(AeaUnitInfo info) {
        BeanUtils.copyProperties(info, this.aeaUnitInfo);
    }

    @Data
    private class LinkmanVo {
        private java.lang.String linkmanInfoId; // (主键)
        private java.lang.String linkmanType; // (联系人类型。c表示个人联系人，u表示企业联系人)
        private java.lang.String linkmanCate; // (联系人类别，来自于数据字典)
        @ApiModelProperty("联系人姓名")
        private java.lang.String linkmanName; // (联系人姓名)
        private java.lang.String linkmanAddr; // (联系人住址)
        private java.lang.String linkmanOfficePhon; // (办公电话)
        @ApiModelProperty("手机号码")
        private java.lang.String linkmanMobilePhone; // (手机号码)
        private java.lang.String linkmanFax; // (传真)
        private java.lang.String linkmanMail; // (电子邮件)
        @ApiModelProperty("证件号")
        private java.lang.String linkmanCertNo; // (证件号)
    }

    @Data
    private class UnitInfoVo {
        private java.lang.String unitInfoId; // (主键)
        @ApiModelProperty("单位名称")
        private java.lang.String applicant; // (项目（法人）单位名称)

        @Deprecated
        @ApiModelProperty("企业注册号")
        private java.lang.String idcard; // (单位证照号码)

        @ApiModelProperty(value = "工商登记号", dataType = "string")
        private String induCommRegNum;
        @ApiModelProperty(value = "组织机构代码", dataType = "string")
        private String organizationalCode;
        @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
        private String unifiedSocialCreditCode;
        @ApiModelProperty(value = "纳税人识别号", dataType = "string")
        private String taxpayerNum;
        @ApiModelProperty(value = "信用标记码", dataType = "string")
        private String creditFlagNum;
        private java.lang.String contact; // (联系人姓名)
        private java.lang.String mobile; // (联系人手机号码)
        private java.lang.String email; // (联系人电子邮箱)
    }
}
