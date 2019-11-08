package com.augurit.aplanmis.front.subject.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.service.area.RegionService;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryAeaProjInfo;
import com.augurit.aplanmis.front.subject.project.service.GlobalProjService;
import com.augurit.aplanmis.front.subject.unit.service.GlobalApplicantService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.augurit.aplanmis.common.diyannotation.FiledNameIs;


/**
 * 项目库信息-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/proj/info")
@Api(value = "全局项目库", tags = "全局项目库接口")
@Slf4j
public class GlobalProjController {

    private static Logger logger = LoggerFactory.getLogger(GlobalProjController.class);

    @Autowired
    private GlobalProjService globalProjService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private GlobalApplicantService globalApplicantService;

    @Autowired
    private OpuOmOrgService opuOmOrgService;

    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;

    @Autowired
    private RegionService regionService;

    @GetMapping("/getAeaProjInfoByUnitId")
    @ApiOperation("全局项目库 --> 通过业主单位id获取项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "业主单位id", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
    public List<AeaProjInfo> getAeaProjInfoByUnitId(String unitInfoId) {

        List<AeaProjInfo> aeaProjInfos = globalProjService.getAeaProjInfoByUnitId(unitInfoId);
        return aeaProjInfos;
    }

    @RequestMapping("projInfoView.do")
    public ModelAndView projInfoView() {
        return new ModelAndView("ui-jsp/aplanmis/project/proj_info_view");
    }

    @RequestMapping("viewAeaProjInfo.do")
    public ModelAndView viewAeaProjInfo(AeaProjInfo aeaProjInfo, ModelMap modelMap) {
        if (StringUtils.isNotBlank(aeaProjInfo.getLocalCode())) {
            AeaProjInfo query = new AeaProjInfo();
            query.setLocalCode(aeaProjInfo.getLocalCode());
            List<AeaProjInfo> aeaProjInfos = globalProjService.listAeaProjInfo(query);
            if (aeaProjInfos != null && aeaProjInfos.size() > 0) {
                modelMap.put("aeaProjInfo", aeaProjInfos.get(0));
                modelMap.put("localCode", aeaProjInfo.getLocalCode());
            }
        } else {
            modelMap.put("aeaProjInfo", aeaProjInfo);
        }
        modelMap.put("themeId", aeaProjInfo.getThemeId());
        //return new ModelAndView("aplanmis/project/proj_info_view");
        return new ModelAndView("ui-jsp/aplanmis/project/proj_info_view_bak");
    }

    @PostMapping("/editProjUnitInfo")
    @ApiOperation("全局项目库 --> 编辑项目单位信息")
    public ModelAndView editProjUnitInfo(AeaProjInfo aeaProjInfo) {
        ModelAndView modelAndView = new ModelAndView("ui-jsp/aplanmis/project/proj_unit_info_edit");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {
            modelAndView.addObject("projInfoId", aeaProjInfo.getProjInfoId());
        }
        return modelAndView;
    }

//    /**
//     * @param keyword 查询字段
//     * @param
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/listAeaProjInfo")
//    @ApiOperation("全局项目库 --> metronic分页查询项目列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "keyword", value = "查询字段：姓名，身份证号", required = false, dataType = "string", paramType = "query", readOnly = true),
//    })
//    public ResultForm listAeaProjInfo(String keyword, int pageNum,int pageSize ) throws Exception {
//        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
//        logger.debug("分页查询，过滤条件为{姓名，身份证号}，查询关键字为{keyword}", keyword);
//        return globalProjService.listAeaProjInfo(keyword, page);
//    }
//
    /**
     * @param keyword 查询字段
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/listAeaProjInfo")
    @ApiOperation("全局项目库 --> metronic分页查询项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段：姓名，身份证号", required = false, dataType = "string", paramType = "query", readOnly = true),
    })
    public ResultForm listAeaProjInfo(String keyword, Page page) throws Exception {
        //Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        logger.debug("分页查询，过滤条件为{姓名，身份证号}，查询关键字为{keyword}", keyword);
        return globalProjService.listAeaProjInfo(keyword, page);
    }

    @GetMapping("/searchAeaProjInfo")
    public List<AeaProjInfo> searchAeaProjInfo(AeaProjInfo aeaProjInfo) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaProjInfo);
        return globalProjService.listAeaProjInfo(aeaProjInfo);
    }

    @GetMapping("/getProjInfoData")
    public ModelAndView getProjInfoData(AeaProjInfo aeaProjInfo, String printPage) throws Exception {
        ModelAndView modelAndView = new ModelAndView("ui-jsp/aplanmis/project/proj_info_data");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {
            modelAndView.addObject("projInfoId", aeaProjInfo.getProjInfoId());
        }
        if ("1".equals(printPage)) {
            modelAndView.setViewName("ui-jsp/aplanmis/project/proj_info_print");//跳转到word打印页面
        }
        return modelAndView;
    }

    @GetMapping("/editAeaProjInfoTree")
    public ModelAndView editAeaProjInfoTree(AeaProjInfo aeaProjInfo) throws Exception {
        ModelAndView modelAndView = new ModelAndView("ui-jsp/aplanmis/project/proj_info_tree_edit");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {
            modelAndView.addObject("currTreeProjInfoId", aeaProjInfo.getProjInfoId());
        }
        return modelAndView;
    }

    /**
     * 获取页面所需的全部数据字典信息
     *
     * @return
     */
    @GetMapping("/getAllDicContent")
    @ApiOperation("全局项目库 --> 获取数据字典集合")
    public ResultForm getAllDicContent(AeaParTheme aeaParTheme) throws Exception {

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());
        if (topOrg != null) {
            Map result = new HashMap();
            result.put(DicConstants.XM_MAIN_CLASS, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_MAIN_CLASS, topOrg.getOrgId()));
            result.put(DicConstants.XM_NATURE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, topOrg.getOrgId()));
            result.put(DicConstants.XM_GCFL, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_GCFL, topOrg.getOrgId()));
            result.put(DicConstants.XM_URGENCY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_URGENCY, topOrg.getOrgId()));
            result.put(DicConstants.XM_PROJECT_LEVEL, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, topOrg.getOrgId()));
            result.put(DicConstants.XM_FRLX, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_FRLX, topOrg.getOrgId()));
            //result.put(DicConstants.XM_XZFQ, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_XZFQ, topOrg.getOrgId()));
            result.put(DicConstants.XM_XZFQ, regionService.getBscDicRegionTreeList(topOrg.getOrgId()));
            result.put(DicConstants.XM_ZJLY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_ZJLY, topOrg.getOrgId()));
            result.put(DicConstants.XM_GBHY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_GBHY, topOrg.getOrgId()));
            result.put(DicConstants.XM_MAIN_CLASSIFY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_MAIN_CLASSIFY, topOrg.getOrgId()));
            result.put(DicConstants.XM_SUB_CLASSIFY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_SUB_CLASSIFY, topOrg.getOrgId()));
            result.put(DicConstants.XM_JZLX, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_JZLX, topOrg.getOrgId()));
            result.put(DicConstants.XM_TZLX, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_TZLX, topOrg.getOrgId()));
            result.put(DicConstants.XM_TDLY, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_TDLY, topOrg.getOrgId()));
            result.put(DicConstants.XM_JZXZ, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_JZXZ, topOrg.getOrgId()));
            result.put(DicConstants.XM_FIELD_TYPE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_FIELD_TYPE, topOrg.getOrgId()));
            result.put(DicConstants.LINKMAN_CATE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.LINKMAN_CATE));
            result.put(DicConstants.XM_PROJECT_STEP, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, topOrg.getOrgId()));

            List<BscDicCodeItem> themeTypeList = null;
            List<AeaParTheme> list = aeaParThemeMapper.listAeaParTheme(null);
            if (list != null && list.size() > 0) {
                themeTypeList = new ArrayList<>();
                for (AeaParTheme theme : list) {
                    BscDicCodeItem bscDicCodeItem = new BscDicCodeItem();
                    bscDicCodeItem.setItemCode(theme.getThemeId());
                    bscDicCodeItem.setItemName(theme.getThemeName());
                    themeTypeList.add(bscDicCodeItem);
                }
            }
            result.put(DicConstants.XM_THEME_TYPE, themeTypeList);
            return new ContentResultForm<>(true, result);
//            return result;
        }
        return null;
    }

    /**
     * 根据项目id获取项目信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getAeaProjInfoById")
    @ApiOperation("全局项目库 --> 获取项目树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
    public AeaProjInfo getAeaProjInfoById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaProjInfo对象，ID为：{}", id);
            AeaProjInfo proj = globalProjService.getAeaProjInfoById(id);
            return proj;
        } else {
            logger.debug("构建新的AeaProjInfo对象");
            return new AeaProjInfo();
        }
    }

    @GetMapping("/getOnlyAeaProjInfoById")
    @ApiOperation("全局项目库 --> 编辑页面获取项目回显数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
    public ResultForm getOnlyAeaProjInfoById(String id) {
        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaProjInfo对象，ID为：{}", id);
            AeaProjInfo proj = globalProjService.getOnlyAeaProjInfoById(id);
            //获取项目信息
            List<AeaUnitInfo> buildUnitList = globalApplicantService.getBuildUnitListByProjId(id);
            List<AeaUnitInfo> agencyUnitList = globalApplicantService.getAgencyUnitListByProjId(id);
//            proj.setBuildUnitList(buildUnitList);
//            proj.setAgencyUnitList(agencyUnitList);
            Map result = new HashMap();
            result.put("baseForm", proj);
            result.put("buildUnitList", buildUnitList);
            result.put("agencyUnitList", agencyUnitList);
            return new ContentResultForm<>(true, result);
        } else {
            logger.debug("构建新的AeaProjInfo对象");
            return new ContentResultForm<>(true, new HashMap<>());
        }
    }

    @GetMapping("/updateAeaProjInfo")
    public ResultForm updateAeaProjInfo(AeaProjInfo aeaProjInfo) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaProjInfo);
        globalProjService.updateAeaProjInfo(aeaProjInfo);
        return new ResultForm(true);
    }

    /**
     * 国标行业-tree
     *
     * @return
     */
    @GetMapping("/getIndustryDicTree")
    @ApiOperation("全局项目库 --> 国标行业-tree")
    public List<ZtreeNode> getIndustryDicTree(String typeCode) {

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());
        if (topOrg != null) {
            BscDicCodeType bscDicCodeType = bscDicCodeService.tgetTypeActiveByTypeCode(typeCode, topOrg.getOrgId());
            List<BscDicCodeItem> activeItemsByTypeId = bscDicCodeService.getActiveItemsByTypeId(bscDicCodeType.getTypeId());
            return filterRootProj(activeItemsByTypeId);
        }
        return null;
    }


    private List<ZtreeNode> filterRootProj(List<BscDicCodeItem> list) {
        List<ZtreeNode> result = new ArrayList<>();
        if (list.size() > 0) {
            for (BscDicCodeItem dicCodeItem : list) {
                if ("1".equals(dicCodeItem.getItemIsActive())) {
                    ZtreeNode node = new ZtreeNode();
                    node.setId(dicCodeItem.getItemId());
                    node.setpId(dicCodeItem.getParentId());
                    node.setName(dicCodeItem.getItemName());
                    node.setNocheck(false);
                    node.setOpen(true);
                    result.add(node);
                }
            }
        }
        return result;
    }

    @GetMapping("/getAeaProjInfoByCondition")
    public Map getAeaProjInfoByCondition(AeaProjInfo query) throws Exception {
        Map result = new HashMap();
        if (StringUtils.isNotBlank(query.getLocalCode())) {
            query.setIsDeleted("0");
            List<AeaProjInfo> aeaProjInfos = globalProjService.listAeaProjInfo(query);
            if (aeaProjInfos != null && aeaProjInfos.size() > 0) {
                AeaProjInfo aeaProjInfo = aeaProjInfos.get(0);
                result.put("aeaProjInfo", aeaProjInfo);
            }
        }
        return result;
    }

    /**
     * 项目类型(主题),从AEA_PAR_THEME主题定义表拉取数据
     *
     * @return
     */
    @GetMapping("/getThemeType")
    @ApiOperation("全局项目库 -->  项目类型(主题),从AEA_PAR_THEME主题定义表拉取数据")
    public List<BscDicCodeItem> getThemeType(AeaParTheme aeaParTheme) throws Exception {
        List<BscDicCodeItem> result = null;
        List<AeaParTheme> list = aeaParThemeMapper.listAeaParTheme(aeaParTheme);
        if (list != null && list.size() > 0) {
            result = new ArrayList<>();
            for (AeaParTheme theme : list) {
                BscDicCodeItem bscDicCodeItem = new BscDicCodeItem();
                bscDicCodeItem.setItemCode(theme.getThemeId());
                bscDicCodeItem.setItemName(theme.getThemeName());
                result.add(bscDicCodeItem);
            }
        }
        return result;
    }

    @GetMapping("/listDgLinkmanInfoByunitInfoId")
    @ApiOperation("全局项目库 -->  获取单位信息对应的联系人列表")
    public ResultForm listDgLinkmanInfoByunitInfoId(String unitInfoId) throws Exception {
        try {
            List<AeaLinkmanInfo> list = globalProjService.listDgLinkmanInfoByunitInfoId(unitInfoId);
            return new ContentResultForm<List<AeaLinkmanInfo>>(true, list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "加载联系人失败！");
        }
    }

    @PostMapping("/saveAeaProjInfo")
    @ApiOperation("全局项目库 -->  保存项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseForm", value = "项目信息", required = false, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "buildUnitList", value = "建设单位列表", required = false, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "agencyUnitList", value = "待办单位列表", required = false, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目ID", required = false, dataType = "string", paramType = "query", readOnly = true),
    })
    public ResultForm saveAeaProjInfo(String baseForm, String buildUnitList, String agencyUnitList, String projInfoId) throws Exception {

        try {
            AeaProjInfo aeaProjInfo = JSONObject.parseObject(baseForm, AeaProjInfo.class);
            if (StringUtils.isNotBlank(projInfoId))
                aeaProjInfo.setProjInfoId(projInfoId);
            List<AeaUnitInfo> buildUnits = JSONArray.parseArray(buildUnitList, AeaUnitInfo.class);
            List<AeaUnitInfo> agencyUnits = JSONArray.parseArray(agencyUnitList, AeaUnitInfo.class);
            globalProjService.beforeSaveUnitList(buildUnits, agencyUnits);
            globalProjService.saveAeaProjInfo(aeaProjInfo, buildUnits, agencyUnits);

            return new ResultForm(true);
        } catch (Exception e) {
            e.getStackTrace();
            logger.debug(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }
    }


    @GetMapping("/getConditionalQueryDic")
    @ApiOperation("全局项目库 --> 获取查询列表所需的数据字典集合")
    public ResultForm getConditionalQueryDic() {
        try {
            String orgId = SecurityContext.getCurrentOrgId();
            Map result = new HashMap();
//            result.put(DicConstants.XM_XZFQ, regionService.getBscDicRegionTreeList(orgId));
            result.put(DicConstants.XM_NATURE, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, orgId));
            result.put(DicConstants.XM_PROJECT_STEP, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, orgId));
            result.put(DicConstants.XM_PROJECT_LEVEL, bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, orgId));
            return new ContentResultForm<>(true, result);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/conditionalQueryAeaProjInfo")
    @ApiOperation("全局项目库 --> 多查询条件分页查询项目列表")
    public ResultForm listAeaProjInfo(ConditionalQueryAeaProjInfo conditionalQueryAeaProjInfo, Page page) throws Exception {
        return globalProjService.conditionalQueryAeaProjInfo(conditionalQueryAeaProjInfo, page);
    }

    @GetMapping("/listChildProjInfoByProjInfoId")
    @ApiOperation("全局项目库 --> 通过父类项目id查找子类项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目id", required = true, dataType = "string", paramType = "query", readOnly = true),

    })
    public ResultForm listChildProjInfoByProjInfoId(String projInfoId) throws Exception {
        if(StringUtils.isBlank(projInfoId)){
            return new ResultForm(false, "父类id为空！");
        }
        try {
            return new ContentResultForm<>(true, globalProjService.listChildProjInfoByProjInfoId(projInfoId));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/batchDeleteProjInfo")
    @ApiOperation("全局项目库 --> 批量删除项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoIds", value = "项目id,多个用,隔开", required = true, dataType = "string"),

    })
    public ResultForm batchDeleteProjInfo(String projInfoIds) throws Exception {
        try {
            globalProjService.batchDeleteProjInfo(projInfoIds);
            return new ResultForm(true);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultForm(false, e.getMessage());
        }
    }
}
