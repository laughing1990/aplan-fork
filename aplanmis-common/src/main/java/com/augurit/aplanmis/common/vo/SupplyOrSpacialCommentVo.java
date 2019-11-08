package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("补正，补全，特殊程序意见")
public class SupplyOrSpacialCommentVo {
    @ApiModelProperty(value = "环节名称", required = true, dataType="string")
    private String nodeName;
    @ApiModelProperty(value = "办理时间", required = true, dataType="string")
    private String endDate;
    @ApiModelProperty(value = "开始时间", required = true, dataType = "string")
    private String startDate;
    @ApiModelProperty(value = "人员", required = true, dataType="string")
    private String man;
    @ApiModelProperty(value = "办理意见", required = true, dataType="string")
    private String commentMessage;
    @ApiModelProperty(value = "任务ID", required = true, dataType="string")
    private String id;
    @ApiModelProperty(value = "状态", required = true, dataType="string")
    private String signState;
    @ApiModelProperty(value = "材料个数", required = true, dataType = "int")
    private int matNum;
    @ApiModelProperty(value = "材料列表")
    private List<MatinstVo> matinstList;

}
