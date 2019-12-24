package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("并行申报，事项，情形详情")
public class ParallelApplyItemStateDetailVo {
    @ApiModelProperty("并行申报ID")
    private String seriesApplyinstId;
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("事项实例ID")
    private String iteminstId;
    @ApiModelProperty("情形列表")
    private List<Map<String,String>> stateList;

}
