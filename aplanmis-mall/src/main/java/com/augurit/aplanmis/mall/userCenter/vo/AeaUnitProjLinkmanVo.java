package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("人员信息")
public class AeaUnitProjLinkmanVo {
    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "单位名称")
    private String applicant;
    @ApiModelProperty(value = "单位性质")
    private String unitNature;
    @ApiModelProperty(value = "具体地址")
    private String applicantDetailSite;
    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "负责人姓名")
    private String leaderName;
    @ApiModelProperty(value = "负责人手机")
    private String leaderMobilePhone;
    @ApiModelProperty(value = "负责人职务")
    private String leaderDuty;
    @ApiModelProperty(value = "经办人姓名")
    private String operatorName;
    @ApiModelProperty(value = "经办人手机")
    private String operatorMobilePhone;
    @ApiModelProperty(value = "经办人职务")
    private String operatorDuty;

}
