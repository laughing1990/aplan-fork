package com.augurit.aplanmis.admin.par;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.sfengine.config.SFProperties;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.MetronicPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 审批阶段-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/stage")
public class AeaParStageAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageAdminController.class);

    @Autowired
    private AeaParStageAdminService aeaParStageAdminService;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Autowired
    private AeaServiceWindowStageService aeaServiceWindowStageService;

    @Autowired
    private SFProperties sFProperties;

    /**
     * 处理主题版本是否可以编辑
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     */
    private void handleThemeVerIsEditable(ModelMap modelMap, String busiType, String busiId){

        boolean curIsEditable = false;
        AeaParStage stage = aeaParStageAdminService.getAeaParStageById(busiId);
        if(stage!=null){
            AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(stage.getThemeVerId());
            if(ver!=null&&ver.isEditable()){
                curIsEditable = true;
            }
            modelMap.put("currentBusiType", busiType);
            modelMap.put("currentBusiId", busiId);
            modelMap.put("curIsEditable", curIsEditable);
            modelMap.put("isNeedState", StringUtils.isBlank(stage.getIsNeedState())?Status.OFF:stage.getIsNeedState());
            modelMap.put("handWay",StringUtils.isBlank(stage.getHandWay())?"1":stage.getHandWay());
            modelMap.put("useOneForm",StringUtils.isBlank(stage.getUseOneForm())?Status.OFF : stage.getUseOneForm());

            modelMap.put("isOptionItem",  StringUtils.isBlank(stage.getIsOptionItem())?Status.OFF:stage.getIsOptionItem());
            modelMap.put("isFrontCheckItem",  StringUtils.isBlank(stage.getIsCheckItem())?Status.OFF:stage.getIsCheckItem());

            modelMap.put("isCheckItem",  StringUtils.isBlank(stage.getIsCheckItem())?Status.OFF:stage.getIsCheckItem());
            modelMap.put("isCheckItemForm",  StringUtils.isBlank(stage.getIsCheckItemform())?Status.OFF:stage.getIsCheckItemform());
            modelMap.put("isCheckPartform",  StringUtils.isBlank(stage.getIsCheckPartform())?Status.OFF:stage.getIsCheckPartform());
            modelMap.put("isCheckProj",  StringUtils.isBlank(stage.getIsCheckProj())?Status.OFF:stage.getIsCheckProj());
            modelMap.put("isCheckStage",  StringUtils.isBlank(stage.getIsCheckStage())?Status.OFF:stage.getIsCheckStage());
        }else{
            throw new InvalidParameterException("无法获取阶段数据!");
        }
    }

    /**
     * 第一步：设置阶段事项
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     * @return
     */
    @RequestMapping("/indexSetStageItem.do")
    public ModelAndView indexSetStageItem(ModelMap modelMap, String busiType, String busiId) {

        handleThemeVerIsEditable(modelMap, busiType, busiId);
        return new ModelAndView("ui-jsp/kitymind/stage/items/index_set_stage_item");
    }

    /**
     * 第二步: 沿用事项不分情形
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     * @return
     */
    @RequestMapping("/showStageItems.do")
    public ModelAndView showStageItems(ModelMap modelMap, String busiType, String busiId) {

        handleThemeVerIsEditable(modelMap, busiType, busiId);
        return new ModelAndView("ui-jsp/kitymind/stage/items/show_stage_item_index");
    }

    /**
     * 第四步：办事指南
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     * @return
     */
    @RequestMapping("/indexStageGuide.do")
    public ModelAndView indexStageGuide(ModelMap modelMap,String busiType, String busiId) {

        handleThemeVerIsEditable(modelMap, busiType, busiId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_guide_index");
    }

    /**
     * 第五步：前置检测
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     * @return
     */
    @RequestMapping("/frontCheckManage.do")
    public ModelAndView frontCheckManage(ModelMap modelMap, String busiType, String busiId) {

        handleThemeVerIsEditable(modelMap, busiType, busiId);
        return new ModelAndView("ui-jsp/kitymind/stage/frontCheck/front_check_manage_index");

    }

    /**
     * 第六步：一张表单
     *
     * @param modelMap
     * @param busiType
     * @param busiId
     * @return
     */
    @RequestMapping("/oneFormManage.do")
    public ModelAndView indexOneForm(ModelMap modelMap, String busiType, String busiId) {

        if (StringUtils.isNotBlank(this.sFProperties.getRestWebApp())) {
            modelMap.put("restWebApp", this.sFProperties.getRestWebApp());
        } else {
            modelMap.put("restWebApp", "http://localhost:7071/bpm-admin/");
        }
        handleThemeVerIsEditable(modelMap, busiType, busiId);
        return new ModelAndView("ui-jsp/kitymind/stage/oneform/par_stage_oneform");
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNoByThemeVerId(String themeVerId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParStageAdminService.getMaxSortNoByThemeVerId(themeVerId, rootOrgId);
    }

    @RequestMapping("/listAeaParStage.do")
    public MetronicPageInfo<AeaParStage> listAeaParStage(AeaParStage aeaParStage, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStage);
        PageInfo<AeaParStage> pageInfo = aeaParStageAdminService.listAeaParStage(aeaParStage, page);
        return PageHelper.toMetronicPageInfo(pageInfo, page);
    }

    @RequestMapping("/listAeaParStageNoPage.do")
    public List<AeaParStage> listAeaParStageNoPage(AeaParStage aeaParStage) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStage);
        return aeaParStageAdminService.listAeaParStage(aeaParStage);
    }

    /**
     * 分页查询阶段信息
     */
    @RequestMapping("/listAeaParStagePage.do")
    public EasyuiPageInfo<AeaParStage> listAeaParStagePage(AeaParStage aeaParStage, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStage);
        return aeaParStageAdminService.listAeaParStagePage(aeaParStage, page);
    }

    @RequestMapping("/getAeaParStage.do")
    public AeaParStage getAeaParStage(String id) {

        if (id != null) {
            logger.debug("根据ID获取AeaParStage对象，ID为：{}", id);
            return aeaParStageAdminService.getAeaParStageById(id);
        } else {
            logger.debug("构建新的AeaParStage对象");
            return new AeaParStage();
        }
    }

    @RequestMapping("/updateAeaParStage.do")
    public ResultForm updateAeaParStage(AeaParStage aeaParStage) {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStage);
        aeaParStageAdminService.updateAeaParStage(aeaParStage);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑
     *
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaParStage.do")
    public ResultForm saveAeaParStage(AeaParStage aeaParStage) throws Exception {
        ContentResultForm<AeaParStage> form = new ContentResultForm<>(true, aeaParStage);
        if (StringUtils.isNotBlank(aeaParStage.getStageId())) {

            aeaParStageAdminService.updateAeaParStage(aeaParStage);

            AeaParThemeVer themeVer = aeaParThemeVerAdminService.getAeaParThemeVerById(aeaParStage.getThemeVerId());
            if(themeVer != null && StringUtils.isNotBlank(themeVer.getThemeVerDiagram())){
                try{
                    JSONObject json = JSONObject.parseObject(themeVer.getThemeVerDiagram());
                    JSONArray cells = (JSONArray) json.get("cells");
                    Map cell = aeaParThemeVerAdminService.getCellsEleById(aeaParStage.getStageId(), cells);
                    if(cell != null && cell.get("attrs") != null && ((Map) cell.get("attrs")).get("stage") != null){
                        Map stage = (Map) ((Map) cell.get("attrs")).get("stage");
                        if(stage != null){
                            stage.put("stageCode", aeaParStage.getStageCode());
                            stage.put("dueNum", aeaParStage.getDueNum());
                            stage.put("isNode", aeaParStage.getIsNode());
                            stage.put("handWay", aeaParStage.getHandWay());
                            stage.put("lcbsxlx", aeaParStage.getLcbsxlx());
                            stage.put("isOptionItem", aeaParStage.getIsOptionItem());
                            stage.put("isSelItem", aeaParStage.getIsSelItem());
                            stage.put("isFrontCheckItem", aeaParStage.getIsCheckItem());
                            stage.put("useOneForm", aeaParStage.getUseOneForm());
                            stage.put("dueUnit", aeaParStage.getDueUnit());
                            stage.put("dybzspjdxh", aeaParStage.getDybzspjdxh());
                            stage.put("isShowItem", aeaParStage.getIsShowItem());

                            Map attrs = (Map) cell.get("attrs");
                            // 4.更换和添加中文
                            String oldName = ((Map) cell.get("lanes")).get("label").toString();
                            ((Map) cell.get("lanes")).put("label", aeaParStage.getStageName());
                            cell.put("auEleName", aeaParStage.getStageName());

                            ((Map)attrs.get(".content")).put("html", aeaParStage.getStageName());
                            themeVer.setThemeVerDiagram(JsonUtils.toJson(json));
                            aeaParThemeVerAdminService.updateAeaParThemeVer(themeVer);
                        }
                    }
                }catch (Exception e){
                    form.setSuccess(false);
                    form.setMessage("已更新阶段，但对应的流程图更新不成功，请确认流程图是否正确。");
                    logger.error("", e);
                    return form;
                }
            }
        } else {

            aeaParStage.setStageId(UUID.randomUUID().toString());
            aeaParStageAdminService.saveAeaParStage(aeaParStage);
        }
        return new ContentResultForm<>(true, aeaParStage);
    }

    @RequestMapping("/deleteAeaParStageById.do")
    public ResultForm deleteAeaParStageById(String id) {

        logger.debug("删除Form对象，对象id为：{}", id);
        if (id != null) {
            aeaParStageAdminService.deleteAeaParStageById(id);
        }
        return new ResultForm(true);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/deleteAeaParStageByIds.do")
    public ResultForm deleteAeaParStageByIds(String[] stageIds) {

        if (stageIds!=null&&stageIds.length>0){
            logger.debug("批量删除阶段信息Form对象，对象id数组为：{}", Arrays.asList(stageIds));
            aeaParStageAdminService.deleteAeaParStageByIds(stageIds);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/listStageByThemeVerId.do")
    public List<AeaParStage> listStageByThemeVerId(String themeVerId){

        AeaParStage aeaParStage = new AeaParStage();
        aeaParStage.setThemeVerId(themeVerId);
        logger.debug("根据Theme Version ID获取Form对象，ID为：{}", themeVerId);
        return aeaParStageAdminService.listAeaParStage(aeaParStage);
    }

    /**
     * 阶段办事指南基本信息
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/guide/index.do")
    public ModelAndView guideIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_basic_info_index");
    }

    /**
     * 阶段办事指南设立依据
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/basicRel/index.do")
    public ModelAndView basicRelIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_basic_rel_index");
    }

    /**
     * 阶段办事指南窗口办理
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/windowRel/index.do")
    public ModelAndView windRelIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_wind_rel_index");
    }

    @RequestMapping("/listStageWindowByPage.do")
    public EasyuiPageInfo<AeaServiceWindowStage> listItemWindowByPage(AeaServiceWindowStage windStage, Page page) throws Exception {

        PageInfo<AeaServiceWindowStage> pageList = aeaServiceWindowStageService.listAeaServiceWindowStage(windStage, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping(value = "/listStageWindowByPageForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyuiPageInfo<AeaServiceWindowStage> listStageWindowByPageForEui(AeaServiceWindowStage windStage, @ModelAttribute PageParam page) throws Exception {

        PageInfo<AeaServiceWindowStage> pageList = aeaServiceWindowStageService.listAeaServiceWindowStage(windStage, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/batchSaveStageWindows.do")
    public ResultForm batchSaveStageWindows(String stageId, String[] windowIds) {

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(windowIds==null){
            throw new InvalidParameterException("参数windowIds为空!");
        }
        if(windowIds!=null&&windowIds.length==0){
            throw new InvalidParameterException("参数windowIds为空!");
        }
        aeaServiceWindowStageService.batchSaveStageWindows(stageId, windowIds);
        return new ResultForm(true);
    }

    @RequestMapping("/delAeaServiceWindowStageById.do")
    public ResultForm delAeaServiceWindowStageById(String windStageId) {

        if(StringUtils.isBlank(windStageId)){
            throw new InvalidParameterException("参数windStageId为空!");
        }
        aeaServiceWindowStageService.delAeaServiceWindowStageById(windStageId);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelAeaServiceWindowStageByIds.do")
    public ResultForm batchDelAeaServiceWindowStageByIds(String[] windStageIds) {

        if(windStageIds==null){
            throw new InvalidParameterException("参数windStageIds为空!");
        }
        if(windStageIds!=null && windStageIds.length==0){
            throw new InvalidParameterException("参数windStageIds为空!");
        }
        aeaServiceWindowStageService.batchDelAeaServiceWindowStageByIds(windStageIds);
        return new ResultForm(true);
    }

    /**
     * 阶段办事指南收费信息
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/charge/index.do")
    public ModelAndView charIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_charge_index");
    }

    /**
     * 阶段办事指南常见问题
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/question/index.do")
    public ModelAndView questionIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("ui-jsp/aplanmis/par/stage/stage_question_index");
    }

    private void handThemeVerIsEditable(ModelMap modelMap, String stageId){

        boolean curIsEditable = false;
        AeaParStage stage = aeaParStageAdminService.getAeaParStageById(stageId);
        if(stage!=null){
            AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(stage.getThemeVerId());
            if(ver!=null&&ver.isEditable()){
                curIsEditable = true;
            }
            modelMap.put("recordId", stageId);
            modelMap.put("curIsEditable", curIsEditable);
        }else{
            throw new InvalidParameterException("无法获取阶段数据!");
        }
    }
}
