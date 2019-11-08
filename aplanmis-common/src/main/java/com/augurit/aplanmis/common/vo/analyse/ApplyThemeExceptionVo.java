package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "主题申报异常实体")
public class ApplyThemeExceptionVo implements Serializable {
    @ApiModelProperty(value = "排名")
    private int sortNo;
    @ApiModelProperty(value = "主题名称")
    private String themeName;
    @ApiModelProperty(value = "阶段名称")
    private String stageName;
    @ApiModelProperty(value = "不受理数量")
    private Long notAcceptCount;
    @ApiModelProperty(value = "逾期数量")
    private Long overdueCount;
}
