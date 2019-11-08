package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("阶段信息VO")
public class AeaParStageVo {
    @ApiModelProperty("阶段名称")
    private String stageName;
    @ApiModelProperty("阶段ID")
    private String stageId;
    @ApiModelProperty("阶段实例ID")
    private String stageInstId;

    @ApiModelProperty("并行事项列表")
    private List<AeaItemBasicVo> coreItemList;
    @ApiModelProperty("并联事项列表")
    private List<AeaItemBasicVo> parallelItemList;

    @ApiModelProperty("该阶段状态 0已办结 1办理中 2未办理")
    private String stateType;

    @ApiModelProperty("并联事项办结数")
    private int parallelDoneNumber;
    @ApiModelProperty("并行事项办结数")
    private int coreDoneNumber;

    @ApiModelProperty("该阶段历时")
    private Double stageRunTime;
    @ApiModelProperty("该阶段事项总数")
    private int stageItemNum;
    @ApiModelProperty("事项办结比率")
    private String endProp;

   /* @ApiModelProperty("并联事项逾期数")
    private int parallelOverdueNumber;
    @ApiModelProperty("并行事项逾期数")
    private int coreOverdueNumber;*/
}
