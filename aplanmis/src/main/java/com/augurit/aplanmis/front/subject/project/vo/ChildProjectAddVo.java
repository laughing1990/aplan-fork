package com.augurit.aplanmis.front.subject.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增子项目vo")
public class ChildProjectAddVo {
    @ApiModelProperty(value = "父项目id", required = true)
    private String parentProjInfoId;
    @ApiModelProperty(value = "子项目名称", required = true)
    private String projName;
    @ApiModelProperty(value = "是否默认新增一个root下的项目工程 true  在root节点下新增；false:非root节点下新增", required = true, dataType = "boolean", allowableValues = "true, false")
    private Boolean isSecond;
    @ApiModelProperty(value = "项目备注", dataType = "string")
    private String foreignRemark;

    //广东模式
    @ApiModelProperty(value = "1:工程规划阶段，2：施工许可阶段", dataType = "string")
    private String stageFlag;

}
