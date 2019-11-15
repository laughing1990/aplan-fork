package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("申报材料附件上传vo")
public class ApplyMatUploadVo {
    @ApiModelProperty(value = "材料实例id")
    private String matinstId;
    @ApiModelProperty(value = "材料id", required = true)
    private String matId;
    @ApiModelProperty(value = "材料实例编码", required = true)
    private String matinstCode;
    @ApiModelProperty(value = "材料实例名称", required = true)
    private String matinstName;
    @ApiModelProperty(value = "项目id", required = true)
    private String projInfoId;
    @ApiModelProperty(value = "企业id", required = true)
    private String unitInfoId;
    @ApiModelProperty(value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单")
    private String matProp;
    @ApiModelProperty(value = "证照定义ID")
    private String certId;
    @ApiModelProperty(value = "表单定义ID")
    private String stoFormId;
}
