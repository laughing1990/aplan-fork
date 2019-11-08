package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zhangwn on 2019/8/29.
 */
@Data
@NoArgsConstructor
@ApiModel("办件状态数量统计")
public class ItemStatusCountVo implements Serializable{

    @ApiModelProperty(value = "已办结（事项实例状态为：通过、容缺通过、不通过）", dataType="Integer")
    private int completedCount;

    @ApiModelProperty(value = "待补正（事项实例状态为：补正（开始））", dataType="Integer")
    private int correctStartCount;

    @ApiModelProperty(value = "补正待确认（事项实例状态为：补正（结束））", dataType="Integer")
    private int correctEndCount;

    @ApiModelProperty(value = "已受理（事项实例状态去除：已接件、已撤件、不受理、不予受理的）", dataType="Integer")
    private int acceptedCount;

    @ApiModelProperty(value = "预警（act_sto_timerule_inst状态为：预警）", dataType="Integer")
    private int warningCount;

    @ApiModelProperty(value = "已逾期（act_sto_timerule_inst状态为：逾期）", dataType="Integer")
    private int overTimeCount;

    @ApiModelProperty(value = "办结率（已办结/已受理）", dataType="Double")
    private double completedRate;

    @ApiModelProperty(value = "逾期率（已逾期/已办结）", dataType="Double")
    private double overTimedRate;

}
