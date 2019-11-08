package com.augurit.aplanmis.mall.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "首页静态数字")
@Data
public class StaticticsVo {

    @ApiModelProperty(value = "累计接件数")
    private int allCount;
    @ApiModelProperty(value = "累计办结数")
    private int allComplete;
    @ApiModelProperty(value = "本月接件数")
    private int nowMonthCount;
    @ApiModelProperty(value = "本月办结数")
    private int nowMonthComplete;
}
