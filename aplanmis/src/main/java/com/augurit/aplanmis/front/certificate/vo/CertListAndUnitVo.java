package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaCert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "证照定义列表、业主单位信息，项目信息")
public class CertListAndUnitVo {
    @ApiModelProperty(value = "certList",name = "证照定义列表")
    private List<AeaCert> certList;

    @ApiModelProperty(value = "applicants",name = "业主单位名称")
    private String applicant;

    @ApiModelProperty(value = "unitInfoId",name = "业主单位ID")
    private String unitInfoId;

    @ApiModelProperty(value = "projInfoId",name = "项目ID")
    private String projInfoId;

    @ApiModelProperty(value = "projName",name = "项目名称")
    private String projName;

    @ApiModelProperty(value = "issueOrgId",name = "颁发机构ID")
    private String issueOrgId;

    @ApiModelProperty(value = "issueOrgName",name = "颁发机构")
    private String issueOrgName;

}
