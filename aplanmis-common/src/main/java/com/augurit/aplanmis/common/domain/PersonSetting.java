package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 人员设置表单
 */
@Data
public class PersonSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projLinkmanId;

    private String unitProjId;//单位与项目关联ID

    private String linkmanType;//人员类型

    private String linkmanInfoId;//联系人ID

    private String linkmanName;//联系人姓名

    private String safeLicenceNum;//安全考核证

    private String professionCertType;//职业注册证类型

    private String titleCertNum;//职称证号

    private String linkmanCertNo;//身份证
}
