package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改企业单位vo")
public class UnitEditVo {
    @ApiModelProperty(value = "企业id", required = true)
    private String unitInfoId;
    @ApiModelProperty(value = "企业项目关联id")
    private String unitProjId;
    @ApiModelProperty(value = "申报实例id")
    private String applyinstId;
    @ApiModelProperty(value = "是否业主单位")
    private String isOwner;
    @ApiModelProperty(value = "单位名称", required = true)
    private String applicant;
    @ApiModelProperty(value = "法定代表人", required = true)
    private String idrepresentative;
    @ApiModelProperty(value = "法定代表人证件号", required = true)
    private String idno;
    @ApiModelProperty(value = "具体地址", dataType = "string")
    private String applicantDetailSite;
    @ApiModelProperty(value = "单位类型", dataType = "string")
    private String unitType;
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

    @ApiModelProperty(value = "行政区（园区）", dataType = "string")
    private String applicantDistrict;
    @ApiModelProperty(value = "经营范围", dataType = "string")
    private String managementScope;
    @ApiModelProperty(value = "注册资本", dataType = "string")
    private String registerCapital;
    @ApiModelProperty(value = "注册登记机关", dataType = "string")
    private String registerAuthority;
    @ApiModelProperty(value = "邮政编码", dataType = "string")
    private String postalCode;

    @ApiModelProperty(value = "联系人id")
    private String linkmanInfoId;
    @ApiModelProperty(value = "项目id")
    private String projInfoId;
    @ApiModelProperty(value = "项目id集合")
    private String projInfoIds;

    public AeaUnitInfo mergeAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        aeaUnitInfo.setApplicant(this.applicant);
        aeaUnitInfo.setIdrepresentative(this.idrepresentative);
        aeaUnitInfo.setIdno(this.idno);
        aeaUnitInfo.setApplicantDetailSite(this.applicantDetailSite);
        aeaUnitInfo.setUnitType(this.unitType);
        aeaUnitInfo.setInduCommRegNum(this.induCommRegNum);
        aeaUnitInfo.setOrganizationalCode(this.organizationalCode);
        aeaUnitInfo.setUnifiedSocialCreditCode(unifiedSocialCreditCode);
        aeaUnitInfo.setTaxpayerNum(this.taxpayerNum);
        aeaUnitInfo.setCreditFlagNum(this.creditFlagNum);

        aeaUnitInfo.setApplicantDistrict(this.applicantDistrict);
        aeaUnitInfo.setManagementScope(this.managementScope);
        aeaUnitInfo.setRegisteredCapital(this.registerCapital);
        aeaUnitInfo.setRegisterAuthority(this.registerAuthority);
        aeaUnitInfo.setPostalCode(this.postalCode);

        return aeaUnitInfo;
    }
}
