package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("表单材料绑定vo")
public class BindForminstVo {

    @ApiModelProperty(value = "材料定义id")
    private String matId;

    @ApiModelProperty(value = "表单实例id")
    private String stoForminstId;

    @ApiModelProperty(value = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "企业单位id")
    private String unitInfoId;

    @ApiModelProperty(value = "联系人id")
    private String linkmainInfoId;
}
