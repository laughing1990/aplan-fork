package com.augurit.aplanmis.front.subject.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申报主体-非企业
 */
@Data
@ApiModel("申报主体-非企业")
public class ProjectApplySubjectOthersVo {
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
    // 申请实例id
    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;
}
