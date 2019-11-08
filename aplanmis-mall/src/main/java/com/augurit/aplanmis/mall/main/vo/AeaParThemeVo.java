package com.augurit.aplanmis.mall.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "主题")
public class AeaParThemeVo {

    @ApiModelProperty(value = "主题id")
    private String themeId;
    @ApiModelProperty(value = "主题名称")
    private String themeName;
    @ApiModelProperty(value = "主题备注说明")
    private String themeMemo;
    @ApiModelProperty(value = "主题类型")
    private String themeType;
    @ApiModelProperty(value = "是否启用主线 0 不启用 1启用 ，默认启用")
    private String isMainline;
    @ApiModelProperty(value = "主线别名")
    private String mainlineAlias;
    @ApiModelProperty(value = "是否启用辅线  0 禁用  1 启用，默认启用")
    private String isAuxiline;
    @ApiModelProperty(value = "辅线别名")
    private String auxilineAlias;
}
