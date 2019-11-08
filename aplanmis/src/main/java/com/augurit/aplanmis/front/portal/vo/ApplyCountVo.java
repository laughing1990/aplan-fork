package com.augurit.aplanmis.front.portal.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyCountVo {

    @ApiModelProperty("网上待预审数")
    private Integer wangShangDaiYuShen;

    @ApiModelProperty("待补全申报数")
    private Integer daiBuQuanShenBao;

    @ApiModelProperty("补全待确认申报数")
    private Integer buQuanDaiQueRenShenBao;

    @ApiModelProperty("不予受理数")
    private Integer buYuShouLiShenBao;

    @ApiModelProperty("申报时限预警数")
    private Integer shenBaoShiXianYuJing;

    @ApiModelProperty("申报时限逾期数")
    private Integer shenBaoShiXianYuQi;

    @ApiModelProperty("项目数")
    private Integer xiangMu;

    @ApiModelProperty("代办任务数")
    private Integer daiBanRenWu;

    @ApiModelProperty("已办任务数")
    private Integer yiBanRenWu;
}
