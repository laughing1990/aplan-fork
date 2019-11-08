package com.augurit.aplanmis.front.basis.theme.vo;

import com.augurit.aplanmis.common.domain.AeaParTheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("主题")
public class ThemeVo {
    @ApiModelProperty(name = "themeId", value = "主题id", dataType = "string")
    private String themeId;
    @ApiModelProperty(name = "themeName", value = "主题名称", dataType = "string")
    private String themeName;
    @ApiModelProperty(name = "themeType", value = "主题类型", dataType = "string")
    private String themeType;
    @ApiModelProperty(name = "bigImgPath", value = "主题关联的图片", dataType = "string")
    private String bigImgPath;
    @ApiModelProperty(name = "themeCode", value = "主题编码", dataType = "string")
    private String themeCode;
    @ApiModelProperty(value = "是否启用主线 0 不启用 1启用 ，默认启用")
    private String isMainline;
    @ApiModelProperty(value = "主线别名")
    private String mainlineAlias;
    @ApiModelProperty(value = "是否启用辅线  0 禁用  1 启用，默认启用")
    private String isAuxiline;
    @ApiModelProperty(value = "辅线别名")
    private String auxilineAlias;
    @ApiModelProperty(value = "是否启用技术审查线 0 禁用， 1启用，默认禁用")
    private String isTechspectline;
    @ApiModelProperty(value = "技术审查线别名")
    private String techspectlineAlias;

    public static ThemeVo buildTheme(AeaParTheme aeaParTheme) {
        ThemeVo themeVo = new ThemeVo();
        themeVo.setThemeId(aeaParTheme.getThemeId());
        themeVo.setThemeName(aeaParTheme.getThemeName());
        themeVo.setThemeType(aeaParTheme.getThemeType());
        themeVo.setBigImgPath(aeaParTheme.getBigImgPath());
        themeVo.setThemeCode(aeaParTheme.getThemeCode());
        themeVo.setIsMainline(aeaParTheme.getIsMainline());
        themeVo.setMainlineAlias(aeaParTheme.getMainlineAlias());
        themeVo.setIsAuxiline(aeaParTheme.getIsAuxiline());
        themeVo.setAuxilineAlias(aeaParTheme.getAuxilineAlias());
        themeVo.setIsTechspectline(aeaParTheme.getIsTechspectline());
        themeVo.setTechspectlineAlias(aeaParTheme.getTechspectlineAlias());

        return themeVo;
    }
}