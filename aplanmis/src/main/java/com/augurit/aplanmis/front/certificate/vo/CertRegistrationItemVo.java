package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("[取件登记 | 邮政下单] 的事项列表信息")
public class CertRegistrationItemVo {

    @ApiModelProperty(value = "部门名称")
    private String orgName;

    @ApiModelProperty(value = "部门id")
    private String orgId;

    @ApiModelProperty(value = "事项实例名称")
    private String iteminstName;

    @ApiModelProperty(value = "事项实例id")
    private String iteminstId;

    @ApiModelProperty(value = "证件名称")
    private String certinstName;

    @ApiModelProperty(value = "证件实例id")
    private String certinstId;

    @ApiModelProperty(value = "证件编号")
    private String certinstCode;

    @ApiModelProperty(value = "输入输入实例id")
    private String inoutinstId;

    @ApiModelProperty(value = "材料实例id", notes = "证照实例也属于材料实例的一种, matProp = c")
    private String matinstId;

    @ApiModelProperty(value = "签发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    @ApiModelProperty(value = "运单号")
    protected String expressNum;

    @ApiModelProperty(value = "邮件签收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postSignTime;

    @ApiModelProperty(value = "是否已出件", notes = "窗口已取件 或者 邮政已下单")
    private boolean handled;

    public CertRegistrationItemVo() {
        handled = false;
    }

    public static CertRegistrationItemVo from(AeaHiIteminst aeaHiIteminst, AeaHiCertinst aeaHiCertinst) {
        CertRegistrationItemVo certRegistrationItemVo = new CertRegistrationItemVo();
        certRegistrationItemVo.setOrgName(aeaHiIteminst.getApproveOrgName());
        certRegistrationItemVo.setOrgId(aeaHiIteminst.getApproveOrgId());
        certRegistrationItemVo.setIteminstName(aeaHiIteminst.getIteminstName());
        certRegistrationItemVo.setIteminstId(aeaHiIteminst.getIteminstId());
        certRegistrationItemVo.setCertinstName(aeaHiCertinst.getCertinstName());
        certRegistrationItemVo.setCertinstId(aeaHiCertinst.getCertinstId());
        certRegistrationItemVo.setCertinstCode(aeaHiCertinst.getCertinstCode());
        certRegistrationItemVo.setMatinstId(aeaHiCertinst.getMatinstId());
        certRegistrationItemVo.setIssueDate(aeaHiCertinst.getIssueDate());
        certRegistrationItemVo.setInoutinstId(aeaHiCertinst.getInoutinstId());
        return certRegistrationItemVo;
    }
}
