package com.augurit.aplanmis.mall.guide.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("办事指南阶段事项列表VO")
public class RestStageAndItemVo {


    @ApiModelProperty("阶段列表")
    List<RestStageVo> stages;
    @ApiModelProperty("并联事项列表")
    List<RestItemListVo> parallelItems;
    @ApiModelProperty("并行事项列表")
    List<RestItemListVo> seriItems;


}
