package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "创建/修改文件夹实体参数")
public class BscAttDirParamVo {
    @ApiModelProperty(value = "文件夹ID")
    String dirId;
    @ApiModelProperty(value = "文件夹名称")
    String dirName;
    @ApiModelProperty(value = "文件夹编码")
    String dirCode;
    @ApiModelProperty(value = "文件夹备注")
    String dirDemo;
    @ApiModelProperty(value = "是否根文件夹 0否 1是")
    String isRoot;
    @ApiModelProperty(value = "父级文件夹ID")
    String parentId;
    @ApiModelProperty(value = "父级文件夹名称")
    String parentDirName;

}
