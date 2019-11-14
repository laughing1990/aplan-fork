package com.augurit.aplanmis.front.basis.stage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("开发表单vo")
public class StageDevFormVo {

    @ApiModelProperty(value = "表单id", dataType = "string")
    private String formId;

    @ApiModelProperty(value = "表单名称", dataType = "string")
    private String formName;

    @ApiModelProperty(value = "表单地址", dataType = "string")
    private String formUrl;

}