package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 施工和监理单位信息表单需要的单位信息
 */
@Data
public class AeaExProjBuildUnitInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String unitInfoId;//单位ID aea_unit_proj

    private String unitProjId;//单位项目ID aea_unit_proj

    private String applicant;//单位名 aea_unit_info

    private String unitType;//单位类型(项目主体类型) aea_unit_proj

    private String idrepresentative;//法人 aea_unit_info

    private String idmobile;//法人电话 aea_unit_info

    private String linkmanInfoId;//单位负责人id aea_unit_proj

    private String linkmanName;//负责人姓名

    private String linkmanMobilePhone;//负责人电话

    private String linkmanCertNo;//负责人身份证

    private String projLinkmanId;//主键

    private String registerNum;//负责人注册编号

    private String personSafeLicenceNum;//安全生产考核合格证号

    private String qualLevelId;//所属资质等级ID（对应AEA_IM_QUAL_LEVEL表） aea_unit_proj

    private String qualLevelName;//资质等级名称

    private String qualLevelCode;//资历编码

    private String certinstId;//证书实例表ID aea_unit_proj

    private String certinstCode;//证书编码

    private String unitSafeLicenceNum;//安全生产许可证编号 aea_unit_proj

    private String organizationalCode;//组织机构代码 aea_unit_info

    private String unifiedSocialCreditCode;//统一社会信用代码 aea_unit_info

    private String professionCertType;//执业注册证类型，简称注册类型，来自于数据字典（C_REG_LCN_TYPE）

    private String professionSealNum;//执业印章号

    private String titleGrade;//职称等级

    private String titleCertNum;//职称证号

    private String linkmanType;//承担角色

    private String personSetting;//人员设置

    private List<PersonSetting> personSettings;

}
