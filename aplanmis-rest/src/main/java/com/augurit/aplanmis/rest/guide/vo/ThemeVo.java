package com.augurit.aplanmis.rest.guide.vo;

import com.augurit.aplanmis.common.domain.AeaParTheme;
import lombok.Data;

@Data
public class ThemeVo {
    private String themeId;
    private String themeName;

    public ThemeVo() {
    }

    public ThemeVo(String themeId, String themeName) {
        this.themeId = themeId;
        this.themeName = themeName;
    }

    public static ThemeVo buildTheme(AeaParTheme aeaParTheme) {
        ThemeVo themeVo = new ThemeVo();
        themeVo.setThemeId(aeaParTheme.getThemeId());
        themeVo.setThemeName(aeaParTheme.getThemeName());
        return themeVo;
    }
}
