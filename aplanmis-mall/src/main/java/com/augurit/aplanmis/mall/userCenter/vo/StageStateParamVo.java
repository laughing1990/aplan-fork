package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("智能引导参数VO")
public class StageStateParamVo {
    @ApiModelProperty("阶段ID")
    private String stageId;
    @ApiModelProperty("行政区划")
    private String regionalism;
    @ApiModelProperty("建设地点")
    private String projectAddress;
    @ApiModelProperty("情形ID列表")
    private List<String> stateIds;

}
