package com.augurit.aplanmis.front.basis.theme.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.ThemeCategory;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeTypeVo;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeVo;
import com.augurit.aplanmis.front.common.service.RestOrgService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParThemeService {

    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private RestOrgService restOrgService;

    /**
     * she
     *
     * @param category
     * @return
     */
    public List<ThemeTypeVo> getMainThemeTypeCategory(String category) throws Exception {
        List<ThemeTypeVo> voList = new ArrayList<>();
        List<AeaParTheme> aeaParThemes = new ArrayList<>();
        ThemeTypeVo vo;
        //默认查询并联主线
        if (StringUtils.isEmpty(category)) {
            category = "0";
        }
        AeaParTheme aeaParTheme = new AeaParTheme();
        //主线
        if ("0".equals(category)) {
            //先查询所有的可用的主题
            aeaParTheme.setIsMainline("1");
            aeaParThemes = aeaParThemeService.listAeaParTheme(aeaParTheme);

        }
        //辅线
        else if ("1".equals(category)) {
            aeaParTheme.setIsMainline("0");
            aeaParTheme.setIsAuxiline(("1"));
            aeaParThemes = aeaParThemeService.listAeaParTheme(aeaParTheme);
        }
        String currentOrgId = SecurityContext.getCurrentOrgId();
        if (aeaParThemes.size() > 0) {
            Set<String> themeTypes = aeaParThemes.stream().map(AeaParTheme::getThemeType).collect(Collectors.toSet());
            for (String themeType : themeTypes) {
                BscDicCodeItem itemByTypeCodeAndItemCode = restOrgService.getItemByTypeCodeAndItemCode("THEME_TYPE", themeType);
                vo = new ThemeTypeVo();
                List<AeaParTheme> themeList = aeaParThemeService.getTestRunOrPublishedVerAeaParTheme(themeType, currentOrgId);
                if(itemByTypeCodeAndItemCode!=null){
                    vo.buildThemeTypeVo(itemByTypeCodeAndItemCode);
                }
                List<ThemeVo> themeVos = Optional.ofNullable(themeList).orElse(new ArrayList<>()).stream().map(ThemeVo::buildTheme).collect(Collectors.toList());
                vo.getThemes().addAll(themeVos);
                voList.add(vo);
            }
        }
        return voList;
    }

    public List<ThemeTypeVo> getMainThemeTypeCategory(ThemeCategory themeCategory) throws Exception {
        Assert.notNull(themeCategory, "themeCategory is null.");
        AeaParTheme param = new AeaParTheme();
        switch (themeCategory) {
            // 主线
            case MAINLINE:
                param.setIsMainline(Status.ON);
                break;
            // 辅线
            case AUXILINE_51:
            case AUXILINE_54C:
            case AUXILINE_54Y:
            case OTHERS:
                param.setIsAuxiline(Status.ON);
                break;
            default:
                throw new Exception("unknown themeCategory: " + themeCategory.getValue());
        }
        List<ThemeTypeVo> voList = new ArrayList<>();
        ThemeTypeVo vo;
        List<AeaParTheme> aeaParThemes = aeaParThemeService.listAeaParTheme(param);
        String currentOrgId = SecurityContext.getCurrentOrgId();
        if (aeaParThemes.size() > 0) {
            Set<String> themeTypes = aeaParThemes.stream().map(AeaParTheme::getThemeType).collect(Collectors.toSet());
            Set<String> themeIds = aeaParThemes.stream().map(AeaParTheme::getThemeId).collect(Collectors.toSet());
            for (String themeType : themeTypes) {
                BscDicCodeItem itemByTypeCodeAndItemCode = restOrgService.getItemByTypeCodeAndItemCode("THEME_TYPE", themeType);
                vo = new ThemeTypeVo();
                List<AeaParTheme> themeList = aeaParThemeService.getTestRunOrPublishedVerAeaParTheme(themeType, currentOrgId);
                if (itemByTypeCodeAndItemCode != null) {
                    vo.buildThemeTypeVo(itemByTypeCodeAndItemCode);
                }
                List<ThemeVo> themeVos = Optional.ofNullable(themeList).orElse(new ArrayList<>()).stream().filter(theme -> themeIds.contains(theme.getThemeId())).map(ThemeVo::buildTheme).collect(Collectors.toList());
                vo.getThemes().addAll(themeVos);
                voList.add(vo);
            }
        }
        return voList;
    }
}
