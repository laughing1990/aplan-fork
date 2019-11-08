package com.augurit.aplanmis.front.portal.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("委办局工作台统计实体")
public class ItemCountVo {

    @ApiModelProperty("所有办件")
    private Long suoYouBanJian;

    @ApiModelProperty("待补证")
    private Long daiBuZheng;

    @ApiModelProperty("补正待确认")
    private Long buZhengDaiQueRen;

    @ApiModelProperty("不通过")
    private Long buTongGuo;

    @ApiModelProperty("不予受理")
    private Long buYuShouLi;

    @ApiModelProperty("时限预警数")
    private Long shiXianYuJing;

    @ApiModelProperty("时限逾期数")
    private Long shiXianYuQi;

    @ApiModelProperty("代办任务数")
    private Long daiBan;

    @ApiModelProperty("已办任务数")
    private Long yiBan;
}
