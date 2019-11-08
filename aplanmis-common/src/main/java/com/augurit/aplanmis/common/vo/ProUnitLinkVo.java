package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目详细信息，包括关联的企业信息和联系人信息")
public class ProUnitLinkVo {
    @ApiModelProperty(value = "项目信息")
    private AeaProjInfo aeaProjInfo;
    @ApiModelProperty(value = "企业信息")
    private AeaUnitInfo aeaUnitInfo;
    @ApiModelProperty(value = "联系人信息")
    private AeaLinkmanInfo aeaLinkmanInfo;
    @ApiModelProperty(value = "业主投诉电话")
    private String ownerComplaintPhone;

}
