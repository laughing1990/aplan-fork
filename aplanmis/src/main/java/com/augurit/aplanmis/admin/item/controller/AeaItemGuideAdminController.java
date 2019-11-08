package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideChargesAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideExtendAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideQuestionsAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemVerAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * -Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/guide")
public class AeaItemGuideAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideAdminController.class);

    @Autowired
    private AeaItemGuideAdminService aeaItemGuideAdminService;

    @Autowired
    private AeaItemGuideQuestionsAdminService guideQuestionsAdminService;

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @Autowired
    private AeaItemGuideChargesAdminService guideChargesService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaItemGuideExtendAdminService aeaItemGuideExtendAdminService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private AeaServiceWindowItemService windowItemService;

    @Autowired
    private IBscAttService bscAttService;

    @RequestMapping("/indexAeaItemGuide.do")
    public ModelAndView indexAeaItemGuide() {

        return new ModelAndView("aea/item/guide_index");
    }

    @RequestMapping("/listAeaItemGuide.do")
    public PageInfo<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemGuide);
        return aeaItemGuideAdminService.listAeaItemGuide(aeaItemGuide, page);
    }

    @RequestMapping("/getAeaItemGuide.do")
    public AeaItemGuide getAeaItemGuide(String id) {
        if (id != null) {
            logger.debug("根据ID获取AeaItemGuide对象，ID为：{}", id);
            return aeaItemGuideAdminService.getAeaItemGuideById(id);
        } else {
            logger.debug("构建新的AeaItemGuide对象");
            return new AeaItemGuide();
        }
    }

    @RequestMapping("/updateAeaItemGuide.do")
    public ResultForm updateAeaItemGuide(AeaItemGuide aeaItemGuide) {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemGuide);
        aeaItemGuideAdminService.updateAeaItemGuide(aeaItemGuide);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑
     *
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaItemGuide.do")
    public ResultForm saveAeaItemGuide(AeaItemGuide aeaItemGuide) {

        if (aeaItemGuide.getId() != null && !"".equals(aeaItemGuide.getId())) {
            aeaItemGuideAdminService.updateAeaItemGuide(aeaItemGuide);
        } else {
            if (aeaItemGuide.getId() == null || "".equals(aeaItemGuide.getId())) {
                aeaItemGuide.setId(UUID.randomUUID().toString());
            }
            aeaItemGuideAdminService.saveAeaItemGuide(aeaItemGuide);
        }

        return new ContentResultForm<>(true, aeaItemGuide);
    }

    @RequestMapping("/deleteAeaItemGuideById.do")
    public ResultForm deleteAeaItemGuideById(String id) {
        logger.debug("删除Form对象，对象id为：{}", id);
        if (id != null) {
            aeaItemGuideAdminService.deleteAeaItemGuideById(id);
        }
        return new ResultForm(true);
    }

    // ================== 新版操作指南配置 ======================

    /**
     * 基本信息
     *
     * @param itemVerId 事项版本
     * @return
     */
    @RequestMapping("/basicInfoIndex.do")
    public ModelAndView basicInfoIndex(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/basic_info_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        AeaItemGuide aeaItemGuide = new AeaItemGuide();
        aeaItemGuide.setItemVerId(ver.getItemVerId());
        List<AeaItemGuide> aeaItemGuides = aeaItemGuideAdminService.listAeaItemGuide(aeaItemGuide);
        String topOrgId = SecurityContext.getCurrentOrgId();
        if (aeaItemGuides != null && !aeaItemGuides.isEmpty()) {
            aeaItemGuide = aeaItemGuides.get(0);
            List<BscAttForm> ckbllctList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE", "CKBLLCT", aeaItemGuide.getId(), null, topOrgId, null);
            List<BscAttForm> wsbllctList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE", "WSBLLCT", aeaItemGuide.getId(), null, topOrgId, null);
            aeaItemGuide.setCkbllctNum(ckbllctList == null ? 0L : ckbllctList.size());
            aeaItemGuide.setWsbllctNum(wsbllctList == null ? 0L : wsbllctList.size());
        }
        view.addObject("aeaItemGuide", aeaItemGuide);
        // 事项类型
        List<BscDicCodeItem> itemTypes = bscDicCodeService.getActiveItemsByTypeCode("DEPT_ITEM_TYPE", topOrgId);
        view.addObject("itemTypes", itemTypes);
        // 办件类型
        List<BscDicCodeItem> itemPropertys = bscDicCodeService.getActiveItemsByTypeCode("ITEM_PROPERTY", topOrgId);
        view.addObject("itemPropertys", itemPropertys);
        // 时限单位
        List<BscDicCodeItem> dueUnits = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrgId);
        view.addObject("dueUnits", dueUnits);
        // 权力来源
        List<BscDicCodeItem> itemSources = bscDicCodeService.getActiveItemsByTypeCode("ITEM_SOURCE", topOrgId);
        view.addObject("itemSources", itemSources);
        // 实施主体性质
        List<BscDicCodeItem> deptTypes = bscDicCodeService.getActiveItemsByTypeCode("ITEM_DEPT_TYPE", topOrgId);
        view.addObject("deptTypes", deptTypes);
        // 行使层级
        List<BscDicCodeItem> useLevels = bscDicCodeService.getActiveItemsByTypeCode("ITEM_XSCJ", topOrgId);
        view.addObject("useLevels", useLevels);
        // 受理方式
        List<BscDicCodeItem> itemSlfss = bscDicCodeService.getActiveItemsByTypeCode("ITEM_SLFS", topOrgId);
        view.addObject("itemSlfss", itemSlfss);
        // 服务对象
        List<BscDicCodeItem> itemFwjgxzs = bscDicCodeService.getActiveItemsByTypeCode("ITEM_FWJGXZ", topOrgId);
        view.addObject("itemFwjgxzs", itemFwjgxzs);
        return view;
    }

    /**
     * 设立依据
     *
     * @param itemVerId
     * @return
     */
    @RequestMapping("/baseOnInfoIndex.do")
    public ModelAndView baseOnInfo(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/base_info_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        view.addObject("recordId", itemVerId);
        return view;
    }

    /**
     * 扩展信息
     *
     * @param itemVerId
     * @return
     */
    @RequestMapping("/extendInfoIndex.do")
    public ModelAndView extendInfo(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/extend_info_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        AeaItemGuideExtend aeaItemGuideExtend = new AeaItemGuideExtend();
        aeaItemGuideExtend.setItemVerId(ver.getItemVerId());
        List<AeaItemGuideExtend> aeaItemGuideExtends = aeaItemGuideExtendAdminService.listAeaItemGuideExtend(aeaItemGuideExtend);
        if (aeaItemGuideExtends != null && aeaItemGuideExtends.size() > 0) {
            aeaItemGuideExtend = aeaItemGuideExtends.get(0);
            List<BscAttForm> zzzResultGuidList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE_EXTEND","ZZZ_RESULT_GUID", aeaItemGuideExtend.getId(), null, SecurityContext.getCurrentOrgId(), null);
            aeaItemGuideExtend.setZzzResultGuidNum(zzzResultGuidList == null ? 0L : zzzResultGuidList.size());
        }
        view.addObject("aeaItemGuideExtend", aeaItemGuideExtend);
        // 主题分类
        List<BscDicCodeItem> themeTypes = bscDicCodeService.getActiveItemsByTypeCode("THEME_TYPE", SecurityContext.getCurrentOrgId());
        view.addObject("themeTypes", themeTypes);
        return view;
    }

    /**
     * 收费信息首页
     *
     * @param itemVerId
     * @return
     */
    @RequestMapping("/chargeInfoIndex.do")
    public ModelAndView chargeInfoIndex(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/charge_info_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        return view;
    }

    /**
     * 收费信息分页列表
     *
     * @param charges
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/listGuideChargeInfos.do")
    public EasyuiPageInfo<AeaItemGuideCharges> listGuideCharges(AeaItemGuideCharges charges, Page page) throws Exception {

        PageInfo<AeaItemGuideCharges> pageList = guideChargesService.listAeaItemGuideCharges(charges, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "查询见问题列表,带分页", notes = "查询见问题列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "charges", value = "必填" , dataType = "AeaItemGuideCharges" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/charge/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaItemGuideCharges> listQuestionsByPage(AeaItemGuideCharges charges, @ModelAttribute PageParam page){

        logger.debug("分页查询，过滤条件为{}，对象为{}", charges);
        PageInfo<AeaItemGuideCharges> pageList = guideChargesService.listAeaItemGuideCharges(charges, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    /**
     * 获取最大排序号
     *
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过事项版本id获取收费信息最大排序号", notes = "通过事项版本id获取收费信息最大排序号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "itemVerId", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getChargeMaxSortNoByItemVerId.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getChargesMaxSortNoByItemVerId(String itemVerId) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return guideChargesService.getMaxSortNoByItemVerId(itemVerId, rootOrgId);
    }

    /**
     * 保存或者编辑
     *
     * @param charges
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存或者编辑收费信息", notes = "保存或者编辑收费信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "charges", value = "必填" , dataType = "AeaItemGuideCharges" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveOrUpdateChargeInfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveOrUpdateChargeInfo(AeaItemGuideCharges charges) throws Exception {

        if (StringUtils.isNotBlank(charges.getRowguid())) {
            guideChargesService.updateAeaItemGuideCharges(charges);
        } else {
            charges.setRowguid(UuidUtil.generateUuid());
            guideChargesService.saveAeaItemGuideCharges(charges);
        }
        return new ContentResultForm<AeaItemGuideCharges>(true, charges);
    }

    /**
     * 通过id获取
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过id获取收费信息", notes = "通过id获取获取收费信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getGuideChargeInfoById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaItemGuideCharges getGuideChargesById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        return guideChargesService.getAeaItemGuideChargesById(id);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过id删除收费信息", notes = "通过id获取获取收费信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/delGuideChargeInfoById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm delGuideChargesById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        guideChargesService.deleteAeaItemGuideChargesById(id);
        return new ResultForm(true);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过ids批量删除收费信息", notes = "通过ids批量删除收费信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/batchDelChargeInfoByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelChargeInfoByIds(String[] ids) throws Exception {

        if (ids != null && ids.length > 0) {
            guideChargesService.batchDelAeaItemGuideChargesByIds(ids);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    /**
     * 常见问题问答首页
     *
     * @param itemVerId
     * @return
     */
    @RequestMapping("/questionIndex.do")
    public ModelAndView question(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/quest_answer_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        return view;
    }

    /**
     * 常见问题列表，分页查询
     *
     * @param questions
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/listQuestAnswers.do")
    public EasyuiPageInfo<AeaItemGuideQuestions> listGuideQuestions(AeaItemGuideQuestions questions, Page page) throws Exception {

        PageInfo<AeaItemGuideQuestions> pageList = guideQuestionsAdminService.listAeaItemGuideQuestions(questions, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "查询见问题列表,带分页", notes = "查询见问题列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "questions", value = "必填" , dataType = "AeaItemGuideQuestions" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/questions/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaItemGuideQuestions> listQuestionsByPage(AeaItemGuideQuestions questions, @ModelAttribute PageParam page){

        logger.debug("分页查询，过滤条件为{}，对象为{}", questions);
        PageInfo<AeaItemGuideQuestions> pageList = guideQuestionsAdminService.listAeaItemGuideQuestions(questions, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }


    /**
     * 获取最大排序号
     *
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过事项版本id获取常见问题最大排序号", notes = "通过事项版本id获取常见问题最大排序号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "itemVerId", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getQueMaxSortNoByItemVerId.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getMaxSortNoByItemVerId(String itemVerId) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return guideQuestionsAdminService.getMaxSortNoByItemVerId(itemVerId, rootOrgId);
    }

    /**
     * 保存或者编辑
     *
     * @param questions
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存或编辑常见问题答案", notes = "保存或编辑常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "questions", value = "必填" , dataType = "AeaItemGuideQuestions" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveOrUpdateQuestAnswer.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveOrUpdateQuestAnswer(AeaItemGuideQuestions questions) throws Exception {

        if (StringUtils.isNotBlank(questions.getId())) {
            guideQuestionsAdminService.updateAeaItemGuideQuestions(questions);
        } else {
            questions.setId(UuidUtil.generateUuid());
            guideQuestionsAdminService.saveAeaItemGuideQuestions(questions);
        }
        return new ContentResultForm<AeaItemGuideQuestions>(true, questions);
    }

    /**
     * 通过id获取
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过id获取常见问题答案", notes = "通过id获取常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getQuestAnswerById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaItemGuideQuestions getQuestAnswerById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        return guideQuestionsAdminService.getAeaItemGuideQuestionsById(id);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过id删除常见问题答案", notes = "通过id删除常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/delQuestAnswerById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm delQuestAnswerById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        guideQuestionsAdminService.deleteAeaItemGuideQuestionsById(id);
        return new ResultForm(true);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过ids批量删除常见问题答案", notes = "通过ids批量删除常见问题答案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填" , required = true, allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/batchDelQuestAnswerByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelQuestAnswerByIds(String[] ids) throws Exception {

        if (ids != null && ids.length > 0) {
            guideQuestionsAdminService.batchDelQuestAnswerByIds(ids);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }


    /**
     * 将事项库的基本信息同步过去办事指南表
     *
     * @return
     */
    @RequestMapping("/syncLocalAeaItemGuide.do")
    public ResultForm syncLocalAeaItemGuide() {

        aeaItemGuideAdminService.syncLocalAeaItemGuide();
        return new ResultForm(true);
    }

    @RequestMapping("/saveOrUpdateBasicInfo.do")
    public ContentResultForm<AeaItemGuide> saveOrUpdateBasicInfo(AeaItemGuide aeaItemGuide, HttpServletRequest request) throws Exception {

        String topOrgId = SecurityContext.getCurrentOrgId();
        if (StringUtils.isNotBlank(aeaItemGuide.getId())) {
            aeaItemGuideAdminService.updateAeaItemGuide(aeaItemGuide);
        } else {
            aeaItemGuide.setId(UuidUtil.generateUuid());
            aeaItemGuideAdminService.saveAeaItemGuide(aeaItemGuide);
        }
        aeaItemGuideAdminService.saveSampleFile(aeaItemGuide, request);
        List<BscAttForm> ckbllctList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE", "CKBLLCT", aeaItemGuide.getId(), null, topOrgId, null);
        List<BscAttForm> wsbllctList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE", "WSBLLCT", aeaItemGuide.getId(), null, topOrgId, null);
        aeaItemGuide.setCkbllctNum(ckbllctList == null ? 0L : ckbllctList.size());
        aeaItemGuide.setWsbllctNum(wsbllctList == null ? 0L : wsbllctList.size());
        return new ContentResultForm(true, aeaItemGuide);
    }

    @RequestMapping("/saveOrUpdateExtendInfo.do")
    public ContentResultForm<AeaItemGuideExtend> saveOrUpdateExtendInfo(AeaItemGuideExtend extend, HttpServletRequest request) throws Exception {

        String topOrgId = SecurityContext.getCurrentOrgId();
        if (StringUtils.isNotBlank(extend.getId())) {
            aeaItemGuideExtendAdminService.updateAeaItemGuideExtend(extend);
        } else {
            extend.setId(UuidUtil.generateUuid());
            aeaItemGuideExtendAdminService.saveAeaItemGuideExtend(extend);
        }
        aeaItemGuideExtendAdminService.saveSampleFile(extend, request);
        List<BscAttForm> zzzResultGuidList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE_EXTEND","ZZZ_RESULT_GUID", extend.getId(), null, topOrgId, null);
        extend.setZzzResultGuidNum(zzzResultGuidList == null ? 0L : zzzResultGuidList.size());
        return new ContentResultForm(true, extend);
    }

    @RequestMapping("/downloadFile.do")
    public void downloadFile(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {

//        attachmentAdminService.downloadFileStrategy(detailId, response);
        fileUtilsService.downloadAttachment(detailId,response,request,false);
    }

    @RequestMapping("/itemServiceWindowRelIndex.do")
    public ModelAndView itemServiceWindowRelIndex(String itemVerId) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        ModelAndView view = new ModelAndView("ui-jsp/item/guide/item_service_window_rel_index");
        view.addObject("itemVerId", itemVerId);
        view.addObject("curIsEditable", curIsEditable);
        view.addObject("recordId", itemVerId);
        return view;
    }

    @RequestMapping("/listItemWindowByPage.do")
    public EasyuiPageInfo<AeaServiceWindowItem> listItemWindowByPage(AeaServiceWindowItem windItem, Page page) throws Exception {

        windItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageInfo<AeaServiceWindowItem> pageList = windowItemService.listAeaServiceWindowItem(windItem, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    // ========================= 前后端分离 =========================

    @ApiOperation(value = "查询事项窗口列表,带分页", notes = "查询事项窗口列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "windItem", value = "必填" , dataType = "AeaServiceWindowItem" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/listItemWindowByPageForEui.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaServiceWindowItem> listItemWindowByPageForEui(AeaServiceWindowItem windItem, @ModelAttribute PageParam page){

        windItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageInfo<AeaServiceWindowItem> pageList = windowItemService.listAeaServiceWindowItem(windItem, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/batchSaveItemWindows.do")
    public ResultForm batchSaveItemWindows(String itemVerId, String[] windowIds) {

        if(StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("请求参数itemVerId不能为空!");
        }
        if(windowIds==null){
            throw new InvalidParameterException("请求参数windowIds不能为空!");
        }
        if(windowIds!=null&&windowIds.length==0){
            throw new InvalidParameterException("请求参数windowIds不能为空!");
        }
        windowItemService.batchSaveItemWindows(itemVerId, windowIds);
        return new ResultForm(true);
    }

    @RequestMapping("/delWindowItemById.do")
    public ResultForm deleteWindowItemById(String windItemId) {

        if(StringUtils.isBlank(windItemId)){
            throw new InvalidParameterException("请求参数windItemId不能为空!");
        }
        windowItemService.delAeaServiceWindowItemById(windItemId);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelWindowItemByIds.do")
    public ResultForm deleteWindowItemByIds(String[] windItemIds) {

        if(windItemIds==null){
            throw new InvalidParameterException("请求参数windItemIds不能为空!");
        }
        if(windItemIds!=null&&windItemIds.length==0){
            throw new InvalidParameterException("请求参数windItemIds不能为空!");
        }
        windowItemService.batchDelAeaServiceWindowItemByIds(windItemIds);
        return new ResultForm(true);
    }

    @RequestMapping("/delItemGuideAtt.do")
    public ResultForm delItemGuideAtt(String itemGuideId, String type, String detailId) throws Exception {

        if (StringUtils.isBlank(itemGuideId) || StringUtils.isBlank(type) || StringUtils.isBlank(detailId)) {
            return new ResultForm(false);
        }
        aeaItemGuideAdminService.delItemGuideAtt(itemGuideId, type, detailId);
        return new ResultForm(true);
    }
}
