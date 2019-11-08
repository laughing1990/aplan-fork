package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("月度统计")
public class MonthlyStatisticsVo implements Serializable{

    @ApiModelProperty(value = "月份", dataType="String")
    private String monthly;

    @ApiModelProperty(value = "受理申报数", dataType="Integer")
    private int acceptApplyCount;

    @ApiModelProperty(value = "办结申报数", dataType="Integer")
    private int completedApplyCount;

    @ApiModelProperty(value = "逾期申报数", dataType="Integer")
    private int overTimeApplyCount;

    @ApiModelProperty(value = "不予受理申报数", dataType="Integer")
    private int outScopeApplyCount;
}
