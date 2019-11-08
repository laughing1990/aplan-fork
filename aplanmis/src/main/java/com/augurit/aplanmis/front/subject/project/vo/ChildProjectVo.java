package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("子项目信息vo")
public class ChildProjectVo {
    // 父项目id
    @ApiModelProperty(value = "父项目id")
    private String parentProjInfoId;
    // 项目id
    @ApiModelProperty(value = "项目id")
    private String projInfoId;
    // 项目名称
    @ApiModelProperty(value = "项目名称")
    private String projInfoName;

    public static ChildProjectVo from(AeaProjInfo aeaProjInfo, String parentProjInfoId) {
        if (aeaProjInfo == null) {
            return null;
        }
        ChildProjectVo child = new ChildProjectVo();
        child.setParentProjInfoId(parentProjInfoId);
        child.setProjInfoId(aeaProjInfo.getProjInfoId());
        child.setProjInfoName(aeaProjInfo.getProjName());
        return child;
    }
}
