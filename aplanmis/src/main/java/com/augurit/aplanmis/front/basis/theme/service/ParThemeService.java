package com.augurit.aplanmis.front.basis.theme.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ThemeCategory;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeTypeVo;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeVo;
import com.augurit.aplanmis.front.common.service.RestOrgService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParThemeService {

    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private RestOrgService restOrgService;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private BscDicCodeItemService bscDicCodeItemService;

    /**
     * she
     *
     * @param category
     * @return
     */
    @Deprecated
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

    public List<ThemeTypeVo> getThemesByTypeCategory(ThemeCategory themeCategory, String themeId) throws Exception {
        Assert.notNull(themeCategory, "themeCategory is null.");
        List<ThemeTypeVo> voList = new ArrayList<>();

        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParTheme> aeaParThemeList = aeaParThemeMapper.listAeaParThemeByThemeCategory(themeId, themeCategory.getValue(), currentOrgId);
        if (aeaParThemeList == null || aeaParThemeList.size() < 1) {
            throw new Exception("没有符合条件的主题, 请检查配置是否正确");
        }
        List<BscDicCodeItem> bscDicCodeItems = bscDicCodeItemService.getActiveItemsByTypeCode("THEME_TYPE", currentOrgId);
        Map<String, BscDicCodeItem> itemMap = bscDicCodeItems.stream().collect(Collectors.toMap(BscDicCodeItem::getItemCode, item -> item));

        Map<String, ThemeTypeVo> themeTypeVoMap = new HashMap<>();
        aeaParThemeList.forEach(theme -> {
            ThemeTypeVo themeTypeVo;
            if (themeTypeVoMap.get(theme.getThemeType()) != null) {
                themeTypeVo = themeTypeVoMap.get(theme.getThemeType());
            } else {
                themeTypeVo = new ThemeTypeVo();
            }
            ThemeVo themeVo = ThemeVo.buildTheme(theme);
            themeTypeVo.getThemes().add(themeVo);
            BscDicCodeItem bscDicCodeItem = itemMap.get(theme.getThemeType());
            if (bscDicCodeItem != null) {
                themeTypeVo.buildThemeTypeVo(bscDicCodeItem);
            }
            voList.add(themeTypeVo);
        });

        return voList;
    }
}
