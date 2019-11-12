package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("证照实例参数")
public class CertinstParamVo extends AeaHiCertinst {
    @ApiModelProperty(name = "iteminstId" ,value = "事项实例")
    private String iteminstId; // (ID)
   /* private String certId; // (证照定义ID)
    private String unitInfoId; // (业主单位ID)
    private String projInfoId; // (建设项目ID)
    private String iteminstId;
    private String certinstCode; // (证照编码)
    private String certinstName; // (证照名称)

    *//* @DateTimeFormat(pattern = "yyyy-MM-dd")*//*
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String issueDate; // (制证时间)

    *//*@DateTimeFormat(pattern = "yyyy-MM-dd")*//*
    private String termStart; // (有效期（起始）)

    *//*@DateTimeFormat(pattern = "yyyy-MM-dd")*//*
    private String termEnd; // (有效期（截止）)

    private String caInfo; // (电子签章信息)
    private String certOwner; // (持证者)
    private String issueOrgId; // (颁发单位ID)*/
}
