package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申报主体-个人
 */
@Data
@ApiModel("申报主体-个人信息")
public class ProjectApplySubjectApplicantVo {
    // 申请人id
    @ApiModelProperty(value = "申请人id")
    private String applyLinkmanId;
    // 申请人
    @ApiModelProperty(value = "申请人")
    private String applyLinkmanName;
    // 申请人电话
    @ApiModelProperty(value = "申请人电话")
    private String applyLinkmanTel;
    // 申请人身份证号
    @ApiModelProperty(value = "申请人身份证号")
    private String applyLinkmanIdCard;
    // 申请人邮箱
    @ApiModelProperty(value = "申请人邮箱")
    private String applyLinkmanEmail;
    // 联系人id
    @ApiModelProperty(value = "联系人id")
    private String linkLinkmanId;
    // 联系人
    @ApiModelProperty(value = "联系人")
    private String linkLinkmanName;
    // 联系人身份证号
    @ApiModelProperty(value = "联系人身份证号")
    private String linkLinkmanIdCard;
    // 联系人电话
    @ApiModelProperty(value = "联系人电话")
    private String linkLinkmanTel;
    // 联系人邮箱
    @ApiModelProperty(value = "联系人邮箱")
    private String linkLinkmanEmail;

    public static ProjectApplySubjectApplicantVo from(AeaLinkmanInfo entity, AeaLinkmanInfo contact) {
        ProjectApplySubjectApplicantVo applicant = null;
        if (entity != null) {
            applicant = new ProjectApplySubjectApplicantVo();
            applicant.setApplyLinkmanId(entity.getLinkmanInfoId());
            applicant.setApplyLinkmanName(entity.getLinkmanName());
            applicant.setApplyLinkmanTel(entity.getLinkmanMobilePhone());
            applicant.setApplyLinkmanIdCard(entity.getLinkmanCertNo());
            applicant.setApplyLinkmanEmail(entity.getLinkmanMail());
        }
        if (contact != null) {
            if (applicant == null) {
                applicant = new ProjectApplySubjectApplicantVo();
            }
            applicant.setLinkLinkmanId(contact.getLinkmanInfoId());
            applicant.setLinkLinkmanName(contact.getLinkmanName());
            applicant.setLinkLinkmanIdCard(contact.getLinkmanCertNo());
            applicant.setLinkLinkmanTel(contact.getLinkmanMobilePhone());
            applicant.setLinkLinkmanEmail(contact.getLinkmanMail());
        }
        return applicant;
    }
}
