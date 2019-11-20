package com.augurit.aplanmis.front.basis.stage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("开发表单vo")
public class FormFrofileVo {
    @ApiModelProperty(value = "表单id", dataType = "string")
    private String formId;
    @ApiModelProperty(value = "表单名称", dataType = "string")
    private String formName;
    @ApiModelProperty(value = "表单地址", dataType = "string")
    private String formUrl;
    @ApiModelProperty(value = "是否智能表单", dataType = "string")
    private boolean isSmartForm;
    @ApiModelProperty(value = "表单序号", dataType = "string")
    private int formSortNo;
}
