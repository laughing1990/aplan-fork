package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@ApiModel(value = "AeaUnitProjLinkman", description = "项目单位联系人类型表")
public class AeaUnitProjLinkman {

    @ApiModelProperty(value = "主键", name = "projLinkmanId")
    private String projLinkmanId;

    @ApiModelProperty(value = "单位与项目关联ID", name = "unitProjId")
    private String unitProjId;

    @ApiModelProperty(value = "联系人ID", name = "linkmanInfoId")
    private String linkmanInfoId;
    //承担角色，来自于数据字典（C_PRJ_PERSON_POST）
    @ApiModelProperty(value = "承担角色，来自于数据字典，包括：1 负责人、2 总监理工", name = "linkmanType")
    private String linkmanType;
    private String professionCertType; // (执业注册证类型，简称注册类型，来自于数据字典（C_REG_LCN_TYPE）)
    private String professionSealNum; // (执业印章号)
    private String titleGrade; // (职称等级，来自于数据字典（C_TITLE）)
    private String titleCertNum; // (职称证号)
    private String registerNum; // (注册编号)
    private String safeLicenceNum; // (安全生产考核合格证号)
    private String prjSpty;   //'审查人员专业 C_PRJ_SPTY 数据字典',
    @ApiModelProperty(value = "时间", name = "creater")
    private String creater;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;
    private String modifier;
    private Date modifyTime;
    private String isDeleted;
    private String linkmanDuty;//职务

    //拓展字段
    private String linkmanName;//联系人名字
    private String linkmanCertNo;//联系人证件号
    public AeaUnitProjLinkman() {

    }

    public AeaUnitProjLinkman(String unitProjId, String linkmanInfoId, String linkmanType) {
        this.projLinkmanId = UUID.randomUUID().toString();
        this.unitProjId = unitProjId;
        this.linkmanInfoId = linkmanInfoId;
        this.linkmanType = linkmanType;

    }


}
