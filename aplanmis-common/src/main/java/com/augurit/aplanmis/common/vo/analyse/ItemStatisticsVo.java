package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by chenjw on 2019/8/29.
 */
@Data
@NoArgsConstructor
@ApiModel("办件统计")
public class ItemStatisticsVo implements Serializable{

    @ApiModelProperty(value = "名称", dataType="String")
    private String name;

    @ApiModelProperty(value = "数量", dataType="long")
    private long count;


    /**
     * 同比：（今年8月份 - 去年8月份）÷|去年8月份| ×100%
     * 同比增长率=（本期数－同期数）÷ |同期数|×100%
     */
    @ApiModelProperty(value = "同比", dataType="String")
    private String onYearonyearBasis;

    /**
     * 环比：（今年8月份 - 今年7月份）÷ 今年7月份×100%
     * 环比增长率=（本期数-上期数）÷上期数×100%。
     */
    @ApiModelProperty(value = "环比", dataType="String")
    private String linkRelativeRatio;

    @ApiModelProperty(value = "百分比", dataType="String")
    private String percent;

    @ApiModelProperty(value = "类型", dataType="String")
    private String type;

}
