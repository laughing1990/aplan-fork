package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.augurit.aplanmis.common.domain.AeaUnitProjLinkman;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AeaProjDrawing implements Serializable {

    private String projInfoId;

    private String organizationalCode; //'组织机构代码'
    private String unifiedSocialCreditCode;//统一社会信用代码',
    private String applicant;//单位名称',',
    private String unitType;//项目主体类型', 数据字典',XM_DWLX
    private String unitInfoId;//单位id',
    private String unitProjId;//项目和企业关联id

    private String linkmanInfoId;//项目负责人id
    private String projectLeader ;//项目负责人
    private String projectLeaderCertNum;//项目负责人 ID Card

    private String linkmanType;//承担角色，来PROJ_UNIT_LINKMAN_TYPE项目单位联系人类型
    private String professionCertType;//执业注册证类型',',
    private String professionSealNum;//执业印章号
    private String titleGrade;//职称等级，来自于数据字典（C_TITLE）
    private String titleCertNum;//职称证号',
    private String prjSpty; //审查专业

    List<AeaUnitProjLinkman> linkmen;

}
