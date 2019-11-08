package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取材料一单清列表数据传值vo")
public class MatListParamVo {
    @ApiModelProperty(value = "阶段ID")
    private String stageId;
    @ApiModelProperty(value = "事项情形ID数组")
    private String[] itemStateIds;
    @ApiModelProperty(value = "阶段情形ID数组")
    private String[] stageStateIds;
    @ApiModelProperty(value = "并行事项版本ID数组")
    private String[] coreItemVerIds;
    @ApiModelProperty(value = "并联事项版本ID数组")
    private String[] parallelItemVerIds;
    @ApiModelProperty(value = "并行标准事项版本ID数组")
    private String[] coreParentItemVerIds;
    @ApiModelProperty(value = "并联标准事项版本ID数组")
    private String[] paraParentllelItemVerIds;

}
