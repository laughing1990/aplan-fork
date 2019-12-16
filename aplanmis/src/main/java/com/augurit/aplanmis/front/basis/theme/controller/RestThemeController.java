package com.augurit.aplanmis.front.basis.theme.controller;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ThemeCategory;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.project.AeaProjStageService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.augurit.aplanmis.front.basis.theme.service.ParThemeService;
import com.augurit.aplanmis.front.basis.theme.vo.StageVo;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeResultVo;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeTypeVo;
import com.augurit.aplanmis.front.basis.theme.vo.ThemeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/theme")
@Api(value = "主题接口", tags = "申报-[主题 阶段 事项]")
@Slf4j
public class RestThemeController {

    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private ParThemeService parThemeService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private AeaServiceWindowStageService aeaServiceWindowStageService;
    @Autowired
    private AeaProjStageService aeaProjStageService;

    @Deprecated
    @GetMapping("/stages/{themeId}")
    @ApiOperation("并联申报 --> 查询主题下阶段接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题id", required = true, dataType = "string", paramType = "path", readOnly = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<List<ThemeResultVo>> stages(@PathVariable String themeId, @RequestParam(required = false, value = "projInfoId") String projInfoId) {
        if (log.isDebugEnabled()) {
            log.debug("查询主题下阶段, themeId: {}", themeId);
        }
        try {
            AeaParThemeVer themeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(themeId, null,SecurityContext.getCurrentOrgId());
            if (themeVer == null) {
                return new ContentResultForm<>(true, null, "themeVer is null");
            }

            List<AeaParStage> filtedStages;
            List<AeaParStage> aeaParStages = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(themeId, themeVer.getThemeVerId(),null);

            // 当前窗口
            AeaServiceWindow currentWindow = Optional.ofNullable(aeaServiceWindowService.getCurrentUserWindow()).orElse(new AeaServiceWindow());
            // 不是所有阶段办理, 窗口阶段权限过滤
            if (Status.OFF.equals(currentWindow.getIsAllStage())) {
                Set<String> currentStageIds = Optional.ofNullable(aeaServiceWindowStageService.findCurrentUserWindowStage()).orElse(new ArrayList<>()).stream().map(AeaParStage::getStageId).collect(Collectors.toSet());
                filtedStages = aeaParStages.stream().filter(stage -> currentStageIds.contains(stage.getStageId())).collect(Collectors.toList());
            } else {
                filtedStages = aeaParStages == null ? new ArrayList<>() : aeaParStages;
            }

            Map<String, List<StageVo>> stages = filtedStages.stream().map(StageVo::build)
                    .peek(vo -> {
                        if (StringUtils.isNotBlank(projInfoId) && StringUtils.isNotBlank(vo.getDybzspjdxh())) {
                            String stageState = aeaProjStageService.projStageState(projInfoId, vo.getDybzspjdxh());
                            vo.setStageState(stageState);
                        }
                    })
                    .collect(Collectors.groupingBy(StageVo::getIsNode));
            AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(themeVer.getThemeId());
            boolean mainLineEnabled = Status.ON.equals(aeaParTheme.getIsMainline());
            boolean auxilineEnabled = Status.ON.equals(aeaParTheme.getIsAuxiline());
            boolean techspectlineEnabled = Status.ON.equals(aeaParTheme.getIsTechspectline());
            List<ThemeResultVo> themeResultVos = new ArrayList<>();
            stages.forEach((key, value) -> {
                if ("1".equals(key) && mainLineEnabled) {
                    themeResultVos.add(new ThemeResultVo("main", value));
                } else if ("2".equals(key) && auxilineEnabled) {
                    themeResultVos.add(new ThemeResultVo("help", value));
                } else if ("3".equals(key) && techspectlineEnabled) {
                    themeResultVos.add(new ThemeResultVo("check", value));
                }
            });
            return new ContentResultForm<>(true, themeResultVos, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "阶段查询失败, themeId: " + themeId + ", message: " + e.getMessage());
        }
    }

    /**
     *获取并联审批主题分类
     */
    @Deprecated
    @GetMapping("/getMainThemeTypeCategory/{category}")
    @ApiOperation(value = "并联申报 --> 根据条件查询主题分类列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "分类，0 主线；1 辅线", required = true, dataType = "string", paramType = "path", allowableValues = "0, 1")
    })
    public ContentResultForm<List<ThemeTypeVo>> getMainThemeType(@PathVariable String category) throws Exception{
        List<ThemeTypeVo> list = parThemeService.getMainThemeTypeCategory(category);
        return new ContentResultForm<>(true,list,"Query success");
    }

    @Deprecated
    @GetMapping("/theme/type/list")
    @ApiOperation("获取主题，对应主题的项目类型")
    @ApiImplicitParam(name = "立项类型", required = true, dataType = "string", paramType = "query")
    public ContentResultForm<List<ThemeVo>> getProjApplyType(String themeType) throws Exception {
        if (StringUtils.isNotBlank(themeType)) {
            List<AeaParTheme> aeaParThemeList = aeaParThemeService.getAeaParThemeListByThemeType(themeType);
            List<ThemeVo> themeVos = Optional.ofNullable(aeaParThemeList).orElse(new ArrayList<>()).stream().map(ThemeVo::buildTheme).collect(Collectors.toList());
            return new ContentResultForm<>(true, themeVos, "Query success.");
        }
        return new ContentResultForm<>(false, null, "themeType is null.");
    }

    /**
     * 获取主线 or 辅线 or 其它 的主题
     */
    @GetMapping("/list")
    @ApiOperation("获取主线 or 辅线 or 其它 的主题")
    public ContentResultForm<List<ThemeTypeVo>> themeCategories(ThemeCategory themeCategory, @RequestParam(required = false) String themeId) {
        try {
            return new ContentResultForm<>(true, parThemeService.getThemesByTypeCategory(themeCategory, themeId), "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/stages")
    @ApiOperation("并联申报、辅线申报、其它申报下的阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeId", value = "主题id", required = true, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<List<StageVo>> stages(String themeId, ThemeCategory themeCategory, @RequestParam(required = false, value = "projInfoId") String projInfoId) {
        if (log.isDebugEnabled()) {
            log.debug("查询主题下阶段, themeId: {}", themeId);
        }
        try {
            AeaParThemeVer themeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(themeId, null, SecurityContext.getCurrentOrgId());
            if (themeVer == null) {
                return new ContentResultForm<>(true, null, "themeVer is null");
            }

            List<AeaParStage> filtedStages;
            List<AeaParStage> aeaParStages = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(themeId, themeVer.getThemeVerId(), null);

            // 当前窗口
            AeaServiceWindow currentWindow = Optional.ofNullable(aeaServiceWindowService.getCurrentUserWindow()).orElse(new AeaServiceWindow());
            // 不是所有阶段办理, 窗口阶段权限过滤
            if (Status.OFF.equals(currentWindow.getIsAllStage())) {
                Set<String> currentStageIds = Optional.ofNullable(aeaServiceWindowStageService.findCurrentUserWindowStage()).orElse(new ArrayList<>()).stream().map(AeaParStage::getStageId).collect(Collectors.toSet());
                filtedStages = aeaParStages.stream().filter(stage -> currentStageIds.contains(stage.getStageId())).collect(Collectors.toList());
            } else {
                filtedStages = aeaParStages == null ? new ArrayList<>() : aeaParStages;
            }

            List<StageVo> finalStages = filtedStages.stream()
                    .filter(s -> {
                        switch (themeCategory) {
                            // 主线
                            case MAINLINE:
                                return "1".equals(s.getIsNode());
                            // 辅线
                            case AUXILINE_51:
                            case AUXILINE_52:
                            case AUXILINE_53:
                            case AUXILINE_54C:
                            case AUXILINE_54Y:
                                return "2".equals(s.getIsNode()) && themeCategory.getValue().equals(s.getDygjbzfxfw());
                            case OTHERS:
                                return !"1".equals(s.getIsNode())
                                        && !ThemeCategory.AUXILINE_51.getValue().equals(s.getDygjbzfxfw())
                                        && !ThemeCategory.AUXILINE_52.getValue().equals(s.getDygjbzfxfw())
                                        && !ThemeCategory.AUXILINE_53.getValue().equals(s.getDygjbzfxfw())
                                        && !ThemeCategory.AUXILINE_54C.getValue().equals(s.getDygjbzfxfw())
                                        && !ThemeCategory.AUXILINE_54Y.getValue().equals(s.getDygjbzfxfw())
                                        ;
                            default:
                                return false;
                        }
                    })
                    .map(StageVo::build)
                    .peek(vo -> {
                        if (StringUtils.isNotBlank(projInfoId) && StringUtils.isNotBlank(vo.getDybzspjdxh())) {
                            String stageState = aeaProjStageService.projStageState(projInfoId, vo.getDybzspjdxh());
                            vo.setStageState(stageState);
                        }
                    }).collect(Collectors.toList());
            return new ContentResultForm<>(true, finalStages, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "阶段查询失败, themeId: " + themeId + ", message: " + e.getMessage());
        }
    }

}
