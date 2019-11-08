package com.augurit.aplanmis.supermarket.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditUnitVo {
    private String unitInfoId; // (主键)
    @ApiModelProperty("单位名称")
    private String applicant; // (项目（法人）单位名称)

    private String unitNature; // (单位性质：1 企业，2 事业单位，3 社会组织)

    private String registeredCapital; // (注册资本)
    private String registerAuthority; // (注册登记机关)
    private String idrepresentative; // (法人姓名)
    private String idno; // (法人身份证号码)
    private String unifiedSocialCreditCode;
    private String postalCode; // (邮政编码)
    private String managementScope; // (经营范围)
}
