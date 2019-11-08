package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("单位信息vo")
public class UnitVo {
    // 单位id
    @ApiModelProperty(value = "单位id")
    private String unitInfoId;
    // 单位名称
    @ApiModelProperty(value = "单位名称")
    private String applicant;
    // 证件类型
    @ApiModelProperty(value = "证件类型")
    private String idtype;
    // 单位注册号
    @ApiModelProperty(value = "单位注册号")
    private String idcard;

    @ApiModelProperty(value = "工商登记号", dataType = "string")
    private String induCommRegNum;
    @ApiModelProperty(value = "组织机构代码", dataType = "string")
    private String organizationalCode;
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;
    // 法定代表人
    @ApiModelProperty(value = "法定代表人")
    private String idrepresentative;
    // 法定代表人证件号
    @ApiModelProperty(value = "法定代表人证件号")
    private String idno;
    // 联系人id
    @ApiModelProperty(value = "联系人id")
    private String linkmanId;
    // 联系人名称
    @ApiModelProperty(value = "联系人名称")
    private String linkmanName;
    // 联系人手机
    @ApiModelProperty(value = "联系人手机")
    private String linkmanMobilePhone;
    // 联系人证件号
    @ApiModelProperty(value = "联系人证件号")
    private String linkmanCertNo;
    // 联系人邮箱
    @ApiModelProperty(value = "联系人邮箱")
    private String linkmanMail;
    // 企业类型
    @ApiModelProperty(value = "企业类型")
    private String unitType;

    @ApiModelProperty(value = "具体地址", dataType = "string")
    private String applicantDetailSite;

    @ApiModelProperty(value = "单位类型", dataType = "string")
    private String unitTypeName;

    @ApiModelProperty(value = "纳税人识别号", dataType = "string")
    private String taxpayerNum;
    @ApiModelProperty(value = "信用标记码", dataType = "string")
    private String creditFlagNum;

    public static UnitVo from(AeaUnitInfo aeaUnitInfo, AeaLinkmanInfo aeaLinkmanInfo) {
        UnitVo unitVo = new UnitVo();
        unitVo.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
        unitVo.setApplicant(aeaUnitInfo.getApplicant());
        //unitVo.setIdtype(aeaUnitInfo.getIdtype());
        // unitVo.setIdcard(aeaUnitInfo.getIdcard());
        unitVo.setIdrepresentative(aeaUnitInfo.getIdrepresentative());
        unitVo.setIdno(aeaUnitInfo.getIdno());
        unitVo.setApplicantDetailSite(aeaUnitInfo.getApplicantDetailSite());
        unitVo.setInduCommRegNum(aeaUnitInfo.getInduCommRegNum());
        unitVo.setOrganizationalCode(aeaUnitInfo.getOrganizationalCode());
        unitVo.setUnifiedSocialCreditCode(aeaUnitInfo.getUnifiedSocialCreditCode());
        unitVo.setTaxpayerNum(aeaUnitInfo.getTaxpayerNum());
        unitVo.setCreditFlagNum(aeaUnitInfo.getCreditFlagNum());
        if (StringUtils.isNotBlank(aeaUnitInfo.getUnitType())) {
            UnitType unitType = UnitType.fromValue(aeaUnitInfo.getUnitType());
            unitVo.setUnitType(unitType.getValue());
            unitVo.setUnitTypeName(unitType.getName());
        }
        if (aeaLinkmanInfo != null) {
            unitVo.setLinkmanId(aeaLinkmanInfo.getLinkmanInfoId());
            unitVo.setLinkmanName(aeaLinkmanInfo.getLinkmanName());
            unitVo.setLinkmanMobilePhone(aeaLinkmanInfo.getLinkmanMobilePhone());
            unitVo.setLinkmanCertNo(aeaLinkmanInfo.getLinkmanCertNo());
            unitVo.setLinkmanMail(aeaLinkmanInfo.getLinkmanMail());
        }
        return unitVo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitVo unitVo = (UnitVo) o;
        return unitInfoId.equals(unitVo.unitInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitInfoId);
    }
}
