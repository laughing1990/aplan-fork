package com.augurit.aplanmis.mall.main.vo;


import com.augurit.aplanmis.common.domain.AeaParTheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("主题类别")
public class ThemeTypeVo {

    @ApiModelProperty(value = "主题类别名称")
    private String themeTypeName;
    @ApiModelProperty(value = "主题类别编码")
    private String themeTypeCode;
    @ApiModelProperty(value = "主题列表")
    private List<AeaParTheme> themeList;
}
