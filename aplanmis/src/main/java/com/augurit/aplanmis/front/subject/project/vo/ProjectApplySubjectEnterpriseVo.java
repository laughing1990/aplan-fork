package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProjLinkman;
import com.augurit.aplanmis.front.subject.unit.vo.AeaLinkmanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 申报主体-企业
 */
@Data
@ApiModel("申报主体-企业信息")
public class ProjectApplySubjectEnterpriseVo {
    // 单位id
    @ApiModelProperty(value = "单位id")
    private String unitInfoId;
    // 单位名称
    @ApiModelProperty(value = "单位名称")
    private String applicant;
    // 证件类型
    // @ApiModelProperty(value = "证件类型")
    //private String idtype;
    // 单位注册号
    //@ApiModelProperty(value = "单位注册号")
    //private String idcard;
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
    // 是否业主单位
    @ApiModelProperty(value = "是否业主单位")
    private String isOwner;
    // 申请实例id
    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;

    @ApiModelProperty(value = "单位类型", dataType = "string")
    private String unitTypeName;

    @ApiModelProperty(value = "单位类型", dataType = "string", notes = "来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构")
    private String unitType;

    @ApiModelProperty(value = "具体地址", dataType = "string")
    private String applicantDetailSite;

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
    @ApiModelProperty(value = "纳税人识别号", dataType = "string")
    private String taxpayerNum;
    @ApiModelProperty(value = "信用标记码", dataType = "string")
    private String creditFlagNum;
    @ApiModelProperty(value = "工商登记号", dataType = "string")
    private String induCommRegNum;
    @ApiModelProperty(value = "组织机构代码", dataType = "string")
    private String organizationalCode;
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;

    private List<AeaLinkmanType> linkmanTypes;

    public void changeListToVo(List<AeaUnitProjLinkman> linkmanList, ProjectApplySubjectEnterpriseVo from) {
        List<AeaLinkmanType> list = new ArrayList<>();
        for (AeaUnitProjLinkman man : linkmanList) {
            AeaLinkmanType vo = new AeaLinkmanType(man.getLinkmanInfoId(), man.getLinkmanName(), man.getLinkmanType());
            list.add(vo);
        }
        from.setLinkmanTypes(list);

    }

    public static ProjectApplySubjectEnterpriseVo from(AeaLinkmanInfo aeaLinkmanInfo, AeaUnitInfo aeaUnitInfo, String isOwner) {
        ProjectApplySubjectEnterpriseVo vo = new ProjectApplySubjectEnterpriseVo();
        vo.setApplicantDistrict(aeaUnitInfo.getApplicantDistrict());
        vo.setManagementScope(aeaUnitInfo.getManagementScope());
        vo.setRegisterAuthority(aeaUnitInfo.getRegisterAuthority());
        vo.setRegisterCapital(aeaUnitInfo.getRegisteredCapital());
        vo.setPostalCode(aeaUnitInfo.getPostalCode());
        vo.setTaxpayerNum(aeaUnitInfo.getTaxpayerNum());
        vo.setCreditFlagNum(aeaUnitInfo.getCreditFlagNum());
        vo.setUnifiedSocialCreditCode(aeaUnitInfo.getUnifiedSocialCreditCode());
        vo.setInduCommRegNum(aeaUnitInfo.getInduCommRegNum());
        vo.setOrganizationalCode(aeaUnitInfo.getOrganizationalCode());
        vo.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
        vo.setApplicant(aeaUnitInfo.getApplicant());
        //vo.setIdtype(aeaUnitInfo.getIdtype());
        //vo.setIdcard(aeaUnitInfo.getIdcard());
        vo.setIdrepresentative(aeaUnitInfo.getIdrepresentative());
        vo.setIdno(aeaUnitInfo.getIdno());
        vo.setLinkmanId(aeaLinkmanInfo.getLinkmanInfoId());
        vo.setLinkmanName(aeaLinkmanInfo.getLinkmanName());
        vo.setLinkmanMobilePhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        vo.setLinkmanCertNo(aeaLinkmanInfo.getLinkmanCertNo());
        vo.setLinkmanMail(aeaLinkmanInfo.getLinkmanMail());
        vo.setIsOwner(isOwner);
        vo.setApplicantDetailSite(aeaUnitInfo.getApplicantDetailSite());
        if (StringUtils.isNotBlank(aeaUnitInfo.getUnitType())) {
            UnitType unitType = UnitType.fromValue(aeaUnitInfo.getUnitType());
            vo.setUnitType(unitType.getValue());
            vo.setUnitTypeName(unitType.getName());
        }
        return vo;
    }
}
