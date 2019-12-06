package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("并联申报暂存阶段，情形，事项传值vo")
public class ItemListTemporaryParamVo {
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "阶段ID", required = true)
    @NotEmpty(message = "stageId is null")
    private String stageId;
    @ApiModelProperty(value = "阶段实例ID")
    private String stageinstId;
    @ApiModelProperty(value = "主题id", required = true)
    private String themeId;
    @ApiModelProperty(value = "主题版本ID" , required = true)
    private String themeVerId;
    @ApiModelProperty(value = "并联申报事项版本ID", dataType = "java.util.List")
    private List<String> itemVerIds;
    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;
    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形")
    private List<ParallelItemStateVo> parallelItemStateIds; // 简单合并申报，选择的并联事项情形
    @ApiModelProperty(value = "第一步暂存实体")
    private SmsInfoVo smsInfoVo;

//    @ApiModelProperty(value = "事项与区划的键值对集合")
//    private List<ItemRegionMap> itemRegionMapList;

}
