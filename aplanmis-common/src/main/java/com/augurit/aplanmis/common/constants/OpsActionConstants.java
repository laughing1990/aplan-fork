package com.augurit.aplanmis.common.constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * qinJianPing
 */
@Getter
@ApiModel(value = "按钮操作枚举", description = "WIN_BANLI||WIN_SHOULI||WIN_BANJIE...")
public enum OpsActionConstants {
    @ApiModelProperty(value = "WIN_BANLI", name = "办理（窗口）", allowableValues = "WIN_BANLI")
    WIN_BANLI("办理（窗口）", "WBL"),//1

    @ApiModelProperty(value = "WIN_SHOULI", name = "受理（窗口）", allowableValues = "WIN_SHOULI")
    WIN_SHOULI("受理（窗口）", "WSL"),//2

    @ApiModelProperty(value = "WIN_BANJIE", name = "办结（窗口）", allowableValues = "WIN_BANJIE")
    WIN_BANJIE("办结（窗口）", "WBJ"),//3

    @ApiModelProperty(value = "ITEM_BANLI", name = "办理（事项）", allowableValues = "ITEM_BANLI")
    ITEM_BANLI("办理（事项）", "IBL"),//4

    @ApiModelProperty(value = "ITEM_SHOULI", name = "受理（事项）", allowableValues = "ITEM_SHOULI")
    ITEM_SHOULI("受理（事项）", "ISL"),//5

    @ApiModelProperty(value = "ITEM_BANJIE_BUTONGGUO", name = "办结不通过（事项）", allowableValues = "ITEM_BANJIE_BUTONGGUO")
    ITEM_BANJIE_BUTONGGUO("办结不通过（事项）", "IBJBTG"),//6

    @ApiModelProperty(value = "ITEM_BANJIE_TONGGUO", name = "办结通过（事项）", allowableValues = "ITEM_BANJIE_TONGGUO")
    ITEM_BANJIE_TONGGUO("办结通过（事项）", "IBJTG"),//7

    @ApiModelProperty(value = "ITEM_BANJIE_RONGQUETONGGUO", name = "办结容缺通过（事项）", allowableValues = "ITEM_BANJIE_RONGQUETONGGUO")
    ITEM_BANJIE_RONGQUETONGGUO("办结容缺通过（事项）", "IBJRQTG"),//8

    @ApiModelProperty(value = "ITEM_JINRU_TEBIECHENGXU", name = "开始特别程序（事项）", allowableValues = "ITEM_JINRU_TEBIECHENGXU")
    ITEM_JINRU_TEBIECHENGXU("开始特别程序（事项）", "IJRTBCX"),//9

    @ApiModelProperty(value = "ITEM_TUICHU_TEBIECHENGXU", name = "退出特别程序（事项）", allowableValues = "ITEM_TUICHU_TEBIECHENGXU")
    ITEM_TUICHU_TEBIECHENGXU("退出特别程序（事项）", "IKSTBCX"),//10

    @ApiModelProperty(value = "ITEM_BUSHOULI", name = "不受理（事项）", allowableValues = "ITEM_BUSHOULI")
    ITEM_BUSHOULI("不受理（事项）", "IBSL"),//11

    @ApiModelProperty(value = "ITEM_CAILIAOBUZHENG", name = "材料补正（事项）", allowableValues = "ITEM_CAILIAOBUZHENG")
    ITEM_CAILIAOBUZHENG("材料补正（事项）", "ICLBZ");//12

    private String name;
    private String value;

    OpsActionConstants(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

