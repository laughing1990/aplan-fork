package com.augurit.aplanmis.rest.guide;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.rest.guide.service.ItemGuideService;
import com.augurit.aplanmis.rest.guide.vo.OrgVo;
import com.augurit.aplanmis.rest.guide.vo.StageVo;
import com.augurit.aplanmis.rest.guide.vo.ThemeVo;
import com.augurit.aplanmis.rest.index.service.vo.ThemeTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/rest/guide")
@Api(value = "办事指南", tags = "办事指南 --> 相关接口")
public class ItemGuideRestController {

    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private ItemGuideService itemGuideService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    /*@GetMapping("/item/list")
    @Transactional(readOnly = true)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "orgId", name = "部门id")
            , @ApiImplicitParam(value = "themeId", name = "主题id")
            , @ApiImplicitParam(value = "stageId", name = "阶段id")
            , @ApiImplicitParam(value = "itemName", name = "事项名称")
    })
    @ApiOperation(value = "查询事项列表", notes = "办事指南 --> 事项列表")
    public ResultForm listAllItems(String orgId, String stageId, String themeId, String itemName) {
        AeaItemBasic queryParam = new AeaItemBasic();
        queryParam.setOrgId(orgId);
        queryParam.setItemName("%" + itemName + "%");
        List<AeaItemBasic> aeaItemBasics = itemGuideService.listAll4Published(queryParam);
        if (StringUtils.isNotBlank(themeId)) {
            List<String> itemVerIds = itemGuideService.getItemVerIdsByThemeIdAndStageId(stageId, themeId);
            List<ItemBasicVo> result = aeaItemBasics.stream()
                    .filter(basic -> itemVerIds.contains(basic.getItemVerId()))
                    .map(b -> new ItemBasicVo(b.getItemVerId(), b.getItemName()))
                    .collect(Collectors.toList());
            return new ContentResultForm<>(true, result);
        }
        List<ItemBasicVo> result = aeaItemBasics.stream().map(b -> new ItemBasicVo(b.getItemVerId(), b.getItemName())).collect(Collectors.toList());
        return new ContentResultForm<>(true, result);
    }*/

    @GetMapping("/theme/and/org/list")
    @Transactional(readOnly = true)
    @ApiOperation(value = "查询主题和部门", notes = "办事指南 --> 主题和部门列表")
    public ResultForm listAllThemesAndOrgs() {
        List<ThemeVo> themes = itemGuideService.listAllThemes().stream().map(t -> new ThemeVo(t.getThemeId(), t.getThemeName())).collect(Collectors.toList());
        List<OrgVo> opuOmOrgs = opuOmOrgMapper.listOpuOmOrg(new OpuOmOrg()).stream().map(o -> new OrgVo(o.getOrgId(), o.getOrgName())).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("themes", themes);
        result.put("opuOmOrgs", opuOmOrgs);
        return new ContentResultForm<>(true, result);
    }

    /**
     * 获取并联审批主题分类
     */
    @GetMapping("/getMainThemeTypeCategory/{category}")
    @ApiOperation(value = "并联申报 --> 根据条件查询主题分类列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "分类，0 主线；1 辅线", required = true, dataType = "string", paramType = "path", allowableValues = "0, 1")
    })
    public ContentResultForm<List<ThemeTypeVo>> getMainThemeType(@PathVariable String category) throws Exception {
        List<ThemeTypeVo> list = itemGuideService.getMainThemeTypeCategory(category);
        return new ContentResultForm<>(true, list, "Query success");
    }

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

    @GetMapping("/stage/list")
    @Transactional(readOnly = true)
    @ApiOperation(value = "查询主题下的阶段", notes = "办事指南 --> 阶段列表")
    public ResultForm listAllStages(String themeId) {
        List<StageVo> stages = itemGuideService.listAllStages(themeId).stream().map(s -> new StageVo(s.getStageId(), s.getStageName())).collect(Collectors.toList());
        return new ContentResultForm<>(true, stages);
    }

    @GetMapping("/details")
    @Transactional(readOnly = true)
    @ApiOperation(value = "办事指南详情", notes = "办事指南 --> 办事指南详情")
    @ApiImplicitParam(name = "itemVerId", value = "事项版本id", required = true, dataType = "String")
    public ResultForm details(String itemVerId) {
        Assert.notNull(itemVerId, "itemVerId is null.");

        return new ContentResultForm<>(true, itemGuideService.guideDetails(itemVerId));
    }

    /**
     * 获取单项申报的材料、情形问题和答案
     *
     * @param itemVerId   事项版本id
     * @param itemStateId 所选情形id
     * @param isParent    是否根节点
     */
    @GetMapping("/getItemStatesAndMaterials")
    @Transactional(readOnly = true)
    @ApiOperation(value = "办事指南 --> 情形选择", notes = "获取情形问题和答案及其下材料", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "itemStateId", value = "所选情形id，root节点为空", dataType = "String"),
            @ApiImplicitParam(name = "itemVerId", value = "事项版本Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isParent", value = "是否根节点", required = true, dataType = "boolean")})
    public ResultForm getSeriesMatList(String itemVerId, String itemStateId, boolean isParent) {
        Assert.notNull(itemVerId, "itemVerId不能为空");

        return new ContentResultForm<>(true, itemGuideService.getStatesAndMaterials(itemVerId, itemStateId));
    }

    @GetMapping("/findAllMats")
    @Transactional(readOnly = true)
    @ApiOperation(value = "办事指南 --> 材料列表", notes = "根据所选的情形id查询所有材料", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerId", value = "事项版本Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "itemStateIds", value = "所选情形id", dataType = "collection")})
    public ResultForm findAllMats(String itemVerId, @RequestParam List<String> itemStateIds) {
        Assert.notNull(itemVerId, "itemVerId不能为空");

        return new ContentResultForm<>(true, itemGuideService.findAllMats(itemVerId, itemStateIds));
    }
}
