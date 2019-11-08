package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel("增加企业单位vo")
public class UnitAddVo {
    @ApiModelProperty(value = "是否业主单位", required = true)
    private String isOwner;
    @ApiModelProperty(value = "单位名称", required = true)
    private String applicant;
    @ApiModelProperty(value = "法定代表人", required = true)
    private String idrepresentative;
    @ApiModelProperty(value = "法定代表人证件号", required = true)
    private String idno;
    @ApiModelProperty(value = "单位类型", dataType = "string")
    private String unitType;
    @ApiModelProperty(value = "具体地址", dataType = "string")
    private String applicantDetailSite;
    @ApiModelProperty(value = "工商登记号", dataType = "string")
    private String induCommRegNum;
    @ApiModelProperty(value = "组织机构代码", dataType = "string")
    private String organizationalCode;
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;

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

    @ApiModelProperty(value = "联系人id", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "联系人姓名", required = true)
    private String linkmanName;
    @ApiModelProperty(value = "证件号", required = true)
    private String linkmanCertNo;
    @ApiModelProperty(value = "手机号码", required = true)
    private String linkmanMobilePhone;
    @ApiModelProperty(value = "电子邮件", required = true)
    private String linkmanMail;
    @ApiModelProperty(value = "项目id集合", required = true)
    private List<String> projInfoIds;
    @ApiModelProperty(value = "纳税人识别号", dataType = "string")
    private String taxpayerNum;
    @ApiModelProperty(value = "信用标记码", dataType = "string")
    private String creditFlagNum;

    public AeaUnitInfo toAeaUnitInfo() {
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setApplicant(this.applicant);
        aeaUnitInfo.setIdrepresentative(this.idrepresentative);
        aeaUnitInfo.setIdno(this.idno);
        aeaUnitInfo.setContact(this.linkmanName);
        aeaUnitInfo.setEmail(this.linkmanMail);
        aeaUnitInfo.setMobile(this.linkmanMobilePhone);
        aeaUnitInfo.setUnitType(this.unitType);
        aeaUnitInfo.setApplicantDetailSite(this.applicantDetailSite);
        aeaUnitInfo.setUnifiedSocialCreditCode(this.unifiedSocialCreditCode);
        aeaUnitInfo.setInduCommRegNum(this.induCommRegNum);
        aeaUnitInfo.setOrganizationalCode(this.organizationalCode);
        aeaUnitInfo.setTaxpayerNum(this.taxpayerNum);
        aeaUnitInfo.setCreditFlagNum(this.creditFlagNum);

        aeaUnitInfo.setApplicantDistrict(this.applicantDistrict);
        aeaUnitInfo.setManagementScope(this.managementScope);
        aeaUnitInfo.setRegisteredCapital(this.registerCapital);
        aeaUnitInfo.setRegisterAuthority(this.registerAuthority);
        aeaUnitInfo.setPostalCode(this.postalCode);

        return aeaUnitInfo;
    }

    public AeaLinkmanInfo mergeAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        aeaLinkmanInfo.setLinkmanName(this.linkmanName);
        aeaLinkmanInfo.setLinkmanCertNo(this.linkmanCertNo);
        aeaLinkmanInfo.setLinkmanMobilePhone(this.linkmanMobilePhone);
        aeaLinkmanInfo.setLinkmanMail(this.linkmanMail);
        return aeaLinkmanInfo;
    }

    public AeaUnitLinkman toAeaUnitLinkman(String unitInfoId, String creator) {
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId is null");
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UuidUtil.generateUuid());
        aeaUnitLinkman.setLinkmanInfoId(this.linkmanInfoId);
        aeaUnitLinkman.setUnitInfoId(unitInfoId);
        aeaUnitLinkman.setCreater(creator);
        aeaUnitLinkman.setCreateTime(new Date());
        return aeaUnitLinkman;
    }

    public List<AeaUnitProj> toAeaUnitProjs(String unitInfoId, String unitType, String creator) {
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId is null");
        Assert.notEmpty(projInfoIds, "projInfoIds is empty");
        return projInfoIds.stream().map(projId -> {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setIsOwner(this.isOwner);
            aeaUnitProj.setProjInfoId(projId);
            aeaUnitProj.setUnitInfoId(unitInfoId);
            aeaUnitProj.setUnitProjId(UuidUtil.generateUuid());
            aeaUnitProj.setUnitType(unitType);
            aeaUnitProj.setCreater(creator);
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setIsDeleted("0");
            aeaUnitProj.setLinkmanInfoId(this.linkmanInfoId);
            return aeaUnitProj;
        }).collect(Collectors.toList());
    }
}
