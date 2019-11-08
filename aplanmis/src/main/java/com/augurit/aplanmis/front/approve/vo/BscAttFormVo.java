package com.augurit.aplanmis.front.approve.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BscAttFormVo {
    @ApiModelProperty(value = "tableName", name = "业务表名", required = true)
    private String tableName;
    @ApiModelProperty(value = "pkName", name = "业务表主键", required = true)
    private String pkName;
    @ApiModelProperty(value = "recordId", name = "业务主键ID", required = true)
    private String recordId;
    @ApiModelProperty(value = "dirId", name = "保存或删除的文件夹id")
    private String dirId;
}
