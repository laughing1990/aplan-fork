package com.augurit.aplanmis.front.subject.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目信息")
public class ProjectKeywordVo {
    @ApiModelProperty(value = "项目主键ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目编码")
    private String localCode;
    @ApiModelProperty(value = "项目名称")
    private String projName;

    public ProjectKeywordVo(String projInfoId, String localCode, String projName) {
        this.projInfoId = projInfoId;
        this.localCode = localCode;
        this.projName = projName;
    }
}
