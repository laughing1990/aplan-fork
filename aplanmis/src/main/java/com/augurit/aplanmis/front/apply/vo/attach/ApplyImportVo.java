package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("导入材料附件vo")
public class ApplyImportVo {

    @ApiModelProperty(value = "材料id", required = true)
    private String matId;

    @ApiModelProperty(value = "项目id", required = true)
    private String projInfoId;

    @ApiModelProperty(value = "企业单位id", required = true)
    private String unitInfoId;

    @ApiModelProperty(value = "附件id，多个用逗号分隔", required = true)
    private String fileIds;

    @ApiModelProperty(value = "材料实例ID")
    private String matinstId;
}
