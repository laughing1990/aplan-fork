package com.augurit.aplanmis.rest.guide.controller;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.service.impl.BscAttServiceImpl;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.rest.guide.service.RestGuideService;
import com.augurit.aplanmis.rest.guide.vo.RestGuideVo;
import com.augurit.aplanmis.rest.guide.vo.RestSingleGuideVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/guide")
@Api(value = "办事指南", tags = "办事指南 --> 相关接口")
public class RestGuideController {
    Logger logger = LoggerFactory.getLogger(RestGuideController.class);

    @Autowired
    RestGuideService restGuideService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    IBscAttService bscAttService;
    @Autowired
    AeaParThemeService aeaParThemeService;
    @Autowired
    BscAttServiceImpl bscAttServiceImpl;

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;


   /* @GetMapping("/toGuideIndexPage")
    @ApiOperation(value = "首页-->跳转办事指南页面接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "部门ID", name = "chooseOrgId", dataType = "string"),
            @ApiImplicitParam(value = "主题ID", name = "themeId", dataType = "string"),
            @ApiImplicitParam(value = "主线阶段ID", name = "mainLineStageId", dataType = "string"),
            @ApiImplicitParam(value = "辅线阶段ID", name = "auxiliaryStageId", dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", dataType = "string")})
    public ModelAndView toMainIndexPage(ModelMap modelMap, String themeId, String mainLineStageId, String auxiliaryStageId, String chooseOrgId, String projInfoId) {
        modelMap.put("themeId", themeId);
        modelMap.put("mainLineStageId", mainLineStageId);
        modelMap.put("auxiliaryStageId", auxiliaryStageId);
        modelMap.put("chooseOrgId", chooseOrgId);
        modelMap.put("projInfoId", projInfoId);
        return new ModelAndView("mall/guide/guideIndex");
    }*/

    @GetMapping("/item/list")
    @ApiOperation(value = "办事指南 --> 获取按部门申报时所有事项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "部门ID", name = "orgId", dataType = "string"),
            @ApiImplicitParam(value = "事项名称", name = "itemName", dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize")})
    public ResultForm getAllItemList(String orgId, @RequestParam(required = false) String itemName, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 1000 : pageSize;
        try {
            AeaItemBasic aeaItemBasic = new AeaItemBasic();
            if (StringUtils.isEmpty(orgId)) {
                if (StringUtils.isNotBlank(itemName)) {
                    aeaItemBasic.setKeyword(itemName);
                }
                return new ContentResultForm<>(true, aeaItemBasicService.listAeaItemBasic(aeaItemBasic, pageNum, pageSize, topOrgId));
            } else {
                return new ContentResultForm<>(true, aeaItemBasicService.getAeaItemBasicListByOrgId(orgId, pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "获取按部门申报时所有事项列表异常");
        }
    }

    @GetMapping("/guide/detailed/{stageId}")
    @ApiOperation(value = "办事指南 --> 并联申报 -->  获取阶段对应的办事指南数据")
    @ApiImplicitParam(value = "阶段ID", name = "stageId", dataType = "string")
    public ContentResultForm<RestGuideVo> getGuideByStageId(@PathVariable("stageId") String stageId) {
        try {
            return new ContentResultForm<>(true, restGuideService.getGuideByStageId(stageId, topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取阶段对应的办事指南数据异常");
        }
    }

    @GetMapping("/guide/single/detailed/{itemVerId}")
    @ApiOperation(value = "办事指南 --> 单项申报 --> 获取事项对应的办事指南数据")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string")
    public ContentResultForm<RestSingleGuideVo> getGuideByItemId(@PathVariable("itemVerId") String itemVerId) {
        try {
            return new ContentResultForm<>(true, restGuideService.getGuideByItemVerId(itemVerId, topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取事项对应的办事指南数据异常");
        }
    }

    @GetMapping("commonMat/list/{itemVerId}")
    @ApiOperation(value = "单项办事指南 --> 根据事项版本id查询公共材料接口")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string")
    public ResultForm getMatListByItemVerId(@PathVariable("itemVerId") String itemVerId) {
        try {
            String[] itemVerIds = new String[2];
            itemVerIds[0] = itemVerId;
            return new ContentResultForm<>(true, aeaItemMatService.getMatListByItemVerIds(itemVerIds, "0", "1"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "根据事项版本id查询公共材料接口异常");
        }

    }

    @GetMapping("stateTree/list")
    @ApiOperation(value = "单项办事指南 -->根据事项版本id查询情形树")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string")
    public ResultForm getStateTreeByItemVerId(String itemVerId) {
        try {
            return new ContentResultForm<>(true, aeaItemStateService.listTreeAeaItemStateByItemVerId(itemVerId, ""));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "根据事项版本id查询情形树异常");
        }
    }

    @GetMapping("matState/list")
    @ApiOperation(value = "根据事项情形id获取材料列表")
    @ApiImplicitParam(value = "itemStateIds", name = "事项情形id数组", required = true)
    public ResultForm getMatByItemStateId(String[] itemStateIds) {
        try {
            return new ContentResultForm<>(true, aeaItemMatService.getMatListByItemStateIds(itemStateIds));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "根据事项情形id获取材料列表异常");
        }
    }


    @GetMapping({"attLinkAndDetailNoPage/list"})
    @ApiOperation(value = "根据matId获取示例材料列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "tableName", name = "tableName", required = true, dataType = "string"),
            @ApiImplicitParam(value = "pkName", name = "pkName", required = true, dataType = "string"),
            @ApiImplicitParam(value = "recordId", name = "recordId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "attType", name = "attType", required = true, dataType = "string")})
    public ResultForm listAttLinkAndDetailNoPage(String tableName, String pkName, String recordId, String attType, String keyword) {
        try {
            return new ContentResultForm<>(true, this.bscAttService.listAttLinkAndDetailNoPage(tableName, pkName, recordId, attType, topOrgId, keyword));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "根据matId获取示例材料列表异常");
        }
    }

//    @GetMapping("/att/preview/{detailId}")
//    @ApiOperation(value = "办事指南--> 预览电子件")
//    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
//    public ModelAndView preview(@PathVariable("detailId") String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
//        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
//        return modelAndView;
//    }

    @GetMapping("/theme/file/list/{themeId}")
    @ApiOperation(value = "办事指南--> 根据主题ID获取流程图附件")
    @ApiImplicitParam(name = "themeId", value = "主题ID", dataType = "string", required = true)
    public ResultForm getFileList(@PathVariable("themeId") String themeId) {
        try {
            List<AeaParTheme> themeList = aeaParThemeService.getMaxVerAeaParTheme("", themeId);
            if (themeList == null || themeList.size() == 0) return new ResultForm(false, "该主题未找到最大版本主题");
            String themeVerId = themeList.get(0).getThemeVerId();
            if (StringUtils.isNotBlank(themeVerId)) {
                List<BscAttForm> attList = bscAttServiceImpl.listAttLinkAndDetailNoPage("AEA_PAR_THEME_VER", "THEME_VER_ID", themeVerId, "", topOrgId, "");
                return new ContentResultForm<>(true, attList.size() > 0 ? attList.get(0) : new ArrayList<>());//暂时取第一条数据
            }
            return new ResultForm(false, "该主题未上传流程图");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }

    }

    @GetMapping("itemState/findByParentItemStateId/{itemStateId}")
    @ApiOperation("办事指南 --> 根据父情形ID查找(事项)子情形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "情形ID", name = "itemStateId", required = true, dataType = "string")
    })
    public ContentResultForm<List<AeaItemState>> findByParentItemStateId(@PathVariable("itemStateId") String itemStateId) {
        try {
            return new ContentResultForm<>(true, aeaItemStateService.listAeaItemStateByParentId(null, "", itemStateId, topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据父情形ID查找(事项)子情形列表失败!");
        }
    }
}
