package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("自动分拣上传vo")
public class AutoImportParamVo {
    @ApiModelProperty(value = "项目ID", required = true)
    private String projInfoId;
    @ApiModelProperty(value = "单位ID(限单位申报传参)", required = true)
    private String unitInfoId;
    @ApiModelProperty(value = "用户ID(限个人申报传参)", required = true)
    private String userInfoId;
    @ApiModelProperty(value = "材料matIdS", required = true)
    private String matIds;
    @ApiModelProperty(value = "材料实例Ids")
    private String matinstIds;

}
