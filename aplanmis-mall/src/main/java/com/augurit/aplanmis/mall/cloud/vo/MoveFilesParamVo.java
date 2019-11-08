package com.augurit.aplanmis.mall.cloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("移动文件实体参数")
public class MoveFilesParamVo {
    @ApiModelProperty("当前目录ID")
    private String currentDirId;
    @ApiModelProperty("目标目录ID")
    private String targetDirId;
    @ApiModelProperty("文件ID数组")
    private String[] detailIds;
}
