package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("自动分拣上传vo")
public class AutoImportVo {

    @ApiModelProperty(value = "项目ID", required = true)
    private String projInfoId;

    @ApiModelProperty(value = "单位ID", required = true)
    private String unitInfoId;

    @ApiModelProperty(value = "材料matIdS", required = true)
    private String matIds;

    @ApiModelProperty(value = "材料实例Ids")
    private String matinstIds;

}
