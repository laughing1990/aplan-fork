package com.augurit.aplanmis.admin.window.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.window.vo.AeaWindowStageVo;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.service.admin.window.AeaServiceWindowAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author ZhangXinhui
 * @date 2019/8/1 001 13:34
 * @desc
 **/
@RestController
@RequestMapping("/aea/service/window")
@Api(tags = "服务窗口")
public class AeaServiceWindowAdminController {

    private static final Logger log = LoggerFactory.getLogger(AeaServiceWindowAdminController.class);

    @Autowired
    private AeaServiceWindowAdminService windowService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaServiceWindowItemService windowItemService;

    @Autowired
    private AeaServiceWindowUserService windowUserService;

    @Autowired
    private AeaServiceWindowStageService windowStageService;

    @Autowired
    private FileUtilsService fileUtilsService;

    /**
     * 服务窗口
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelMap modelMap) {

        // 窗口类型
        String topOrgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> windowTypes = bscDicCodeService.getActiveItemsByTypeCode("WINDOW_TYPE", topOrgId);
        modelMap.put("windowTypes", windowTypes);
        return new ModelAndView("ui-jsp/aplanmis/item/service_window_index");
    }

    /**
     * 窗口事项
     *
     * @param modelMap
     * @param windowId
     * @return
     */
    @RequestMapping("/item/index.do")
    @ApiOperation("打开窗口事项页")
    @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String")
    public ModelAndView itemIndex(ModelMap modelMap, String windowId) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("窗口windowId为空!");
        }

        String topOrgId = getCurrentOrgId();
        AeaServiceWindow window = windowService.getAeaServiceWindowById(windowId);
        if (window != null) {
            if (!window.getRootOrgId().equalsIgnoreCase(topOrgId)) {
                return new ModelAndView("ui-jsp/aplanmis/item/service_window_item_index");
            }
        }
        modelMap.put("window", window);

        // 事项类型
        List<BscDicCodeItem> itemTypes = bscDicCodeService.getActiveItemsByTypeCode("DEPT_ITEM_TYPE", topOrgId);
        modelMap.put("itemTypes", itemTypes);

        // 事项状态
        List<BscDicCodeItem> itemStatus = bscDicCodeService.getActiveItemsByTypeCode("ITEM_STATUS", topOrgId);
        modelMap.put("itemStatus", itemStatus);

        return new ModelAndView("ui-jsp/aplanmis/item/service_window_item_index");
    }

    @RequestMapping("/viewWindow.do")
    public ModelAndView viewWindow() {

        return new ModelAndView("ui-jsp/aplanmis/item/view_item_service_window_index");
    }

    @RequestMapping("/listAeaServiceWindowByPage.do")
    @ApiOperation("列举阶段或环节")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaServiceWindow", value = "服务窗口对象", required = true, dataType = "键值对"),
            @ApiImplicitParam(name = "page", value = "分页", required = true, dataType = "键值对"),
    })
    public EasyuiPageInfo<AeaServiceWindow> listAeaParStagePage(AeaServiceWindow aeaServiceWindow, Page page) {

        aeaServiceWindow.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindow> pageList = windowService.listAeaServiceWindowByPage(aeaServiceWindow, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/listAeaServiceWindow.do")
    @ApiOperation("查询窗口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaServiceWindow", value = "服务窗口对象", required = true, dataType = "键值对"),
    })
    public ResultForm listAeaParStagePage(AeaServiceWindow aeaServiceWindow) {

        try {
            List<AeaServiceWindow> list = windowService.listAeaServiceWindow(aeaServiceWindow);
            log.info("查询窗口");
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "查询窗口异常");
    }

    @RequestMapping("/listAgencyWin.do")
    @ApiOperation("查询代办中心")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaServiceWindow", value = "代办中心对象", required = true, dataType = "AeaServiceWindow"),
    })
    public ResultForm listAgencyWin(AeaServiceWindow aeaServiceWindow) {
        try {
            List<AeaServiceWindow> list = windowService.listAeaServiceWindow(aeaServiceWindow);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "查询代办中心异常");
    }

    @RequestMapping("/saveAeaServiceWindow.do")
    @ApiOperation("保存服务窗口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaServiceWindow", value = "服务窗口对象", required = true, dataType = "键值对"),
    })
    public ResultForm saveAeaServiceWindow(AeaServiceWindow window, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(window.getRootOrgId())) {
            window.setRootOrgId(getCurrentOrgId());
        }
        if (StringUtils.isNotBlank(window.getWindowId())) {
            ResultForm form = isCurrentOrgId(window);
            if (!form.isSuccess()) {
                return form;
            }
            windowService.updateAeaServiceWindow(window);
        } else {
            window.setWindowId(UuidUtil.generateUuid());
            windowService.saveAeaServiceWindow(window);
        }
        windowService.saveMapAttFile(window, request);
        return new ContentResultForm(true, window);
    }

    @RequestMapping("/getAeaServiceWindowById.do")
    @ApiOperation("根据ID获取服务窗口")
    @ApiImplicitParam(name = "windowId", value = "服务窗口ID", required = true, dataType = "String")
    public AeaServiceWindow getAeaServiceWindowById(String windowId) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        AeaServiceWindow window = windowService.getAeaServiceWindowById(windowId);
        if (window.getRootOrgId().equalsIgnoreCase(getCurrentOrgId())) {
            return window;
        }
        log.info("getAeaServiceWindowById.do未找到资源！");
        return null;
    }

    @RequestMapping("/deleteAeaServiceWindowById.do")
    @ApiOperation("根据ID删除服务窗口")
    @ApiImplicitParam(name = "windowId", value = "服务窗口ID", required = true, dataType = "String")
    public ResultForm deleteAeaServiceWindowById(String windowId) throws Exception {
        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "删除的服务窗口不存在");
        }
        windowService.deleteAeaServiceWindowById(windowId, getCurrentOrgId());
        log.info("成功执行单个删除！");
        return new ResultForm(true);
    }

    @RequestMapping("/batchDeleteAeaServiceWindow.do")
    @ApiOperation("批量删除服务窗口")
    @ApiImplicitParam(name = "windowIds", value = "服务窗口ID数组", required = true, dataType = "String[]")
    public ResultForm batchDeleteAeaServiceWindow(String[] windowIds) throws Exception {

        if (windowIds != null && windowIds.length > 0) {
            windowService.batchDeleteAeaServiceWindow(windowIds, getCurrentOrgId());
            log.info("成功执行批量删除！");
            return new ResultForm(true);
        }
        return new ResultForm(false, "请选择需要删除的选项！");
    }

    @RequestMapping("/downloadFile.do")
    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "detailId", value = "附件详情ID", required = true, dataType = "String")
    public void downloadFile(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String[] detailIds = {detailId};
        fileUtilsService.downloadAttachment(detailIds, response, request, false);
//        attachmentAdminService.downloadFileStrategy(detailId, response);
    }

    @RequestMapping("/listWindowMapAtt.do")
    @ApiOperation("查询地图附件")
    @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String")
    public ResultForm listWindowMapAtt(String windowId) throws Exception {
        try {
            List<BscAttForm> windowMapAtt = windowService.findWindowMapAtt(windowId);
            return new ContentResultForm<>(true, windowMapAtt);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("查询地图附件异常");
        }
        return new ResultForm(false, "查询地图附件异常!");
    }

    @GetMapping("/windowMapAtt/preview")
    @ApiOperation(value = "预览电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @RequestMapping("/downloadServiceWindowMapAtt.do")
    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "detailId", value = "附件详情ID", required = true, dataType = "String")
    public ResultForm downloadServiceWindowMapAtt(String detailId, HttpServletResponse response, HttpServletRequest request) {
        if (StringUtils.isBlank(detailId)) {
            return new ResultForm(false, "找不到文件!");
        }
        try {
            String[] detailIds = {detailId};
            boolean flag = fileUtilsService.downloadAttachment(detailIds, response, request, false);
            if (flag) {
                log.debug("执行附件下载成功。");
            }
            return new ResultForm(true);
        } catch (Exception e) {
            return new ResultForm(false, "找不到文件!");
        }
    }

    @RequestMapping("/deleteServiceWindowMapAtt.do")
    @ApiOperation("删除窗口附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "detailId", value = "附件详情ID", required = true, dataType = "String")
    })
    public ResultForm deleteServiceWindowMapAtt(String windowId, String detailId) throws Exception {

        if (StringUtils.isBlank(windowId) || StringUtils.isBlank(detailId)) {
            return new ContentResultForm(false);
        }
        AeaServiceWindow aeaServiceWindow = windowService.getAeaServiceWindowById(windowId);
        if (aeaServiceWindow != null && (!aeaServiceWindow.getRootOrgId().equalsIgnoreCase(getCurrentOrgId()))) {
            return new ResultForm(false, "非法操作资源！");
        }

        if (aeaServiceWindow != null && aeaServiceWindow.getRootOrgId().equalsIgnoreCase(getCurrentOrgId())) {
            aeaServiceWindow.setMapAtt("");
            fileUtilsService.deleteAttachment(detailId);
            windowService.updateAeaServiceWindow(aeaServiceWindow);
            log.info("删除附件成功！");
        }

        return new ContentResultForm(true);
    }

    @ApiOperation("查找未选择的服务窗口")
    @ApiImplicitParam(name = "windowItem", value = "事项服务窗口", required = true, dataType = "String")
    @RequestMapping("/listItemUnselectedWindowByPage.do")
    public EasyuiPageInfo<AeaServiceWindow> listItemUnselectedWindowByPage(AeaServiceWindowItem windowItem, Page page) {

        windowItem.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindow> pageList = windowService.listItemUnselectedWindowByPage(windowItem, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/listStageUnselectedWindowByPage.do")
    public EasyuiPageInfo<AeaServiceWindow> listStageUnselectedWindowByPage(AeaServiceWindowStage windowStage, Page page) {

        windowStage.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindow> pageList = windowService.listStageUnselectedWindowByPage(windowStage, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "查询事项未选择的窗口列表,带分页", notes = "查询事项未选择的窗口列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "windItem", value = "必填" , dataType = "AeaServiceWindowItem" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/listItemUnselectedWindowByPageForEui.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaServiceWindow> listItemUnselectedWindowByPageForEui(AeaServiceWindowItem windItem, @ModelAttribute PageParam page) {

        windItem.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindow> pageList = windowService.listItemUnselectedWindowByPage(windItem, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "查询阶段未选择的窗口列表,带分页", notes = "查询阶段未选择的窗口列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "windStage", value = "必填" , dataType = "AeaServiceWindowStage" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/listStageUnselectedWindowByPageForEui.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaServiceWindow> listStageUnselectedWindowByPageForEui(AeaServiceWindowStage windStage, @ModelAttribute PageParam page) {

        windStage.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindow> pageList = windowService.listStageUnselectedWindowByPage(windStage, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/delServiceWindowAtt.do")
    @ApiOperation("删除窗口附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "detailId", value = "附件详情ID", required = true, dataType = "String")
    })
    public ResultForm delServiceWindowAtt(String windowId, String detailId) throws Exception {

        if (StringUtils.isBlank(windowId) || StringUtils.isBlank(detailId)) {
            return new ResultForm(false);
        }
        windowService.delServiceWindowAtt(windowId, detailId, getCurrentOrgId());
        return new ResultForm(true);
    }

    @RequestMapping("/listAeaServiceWindowItemByPage.do")
    @ApiOperation("删除窗口附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaServiceWindowitem", value = "窗口事项", required = true, dataType = "Object"),
            @ApiImplicitParam(name = "page", value = "分页", required = true, dataType = "Object")
    })
    public EasyuiPageInfo<AeaServiceWindowItem> listAeaServiceWindowItemByPage(AeaServiceWindowItem aeaServiceWindowitem, Page page) {

        aeaServiceWindowitem.setRootOrgId(getCurrentOrgId());
        PageInfo<AeaServiceWindowItem> pageList = windowService.listAeaServiceWindowItemByPage(aeaServiceWindowitem, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "保存窗口阶段", notes = "保存窗口阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "itemVerIds", value = "事项ID数组", required = true, dataType = "String[]")
    })
    @RequestMapping("/saveAeaServiceWindowItem.do")
    public ResultForm saveAeaServiceWindowItem(String windowId, String[] itemVerIds) throws Exception {

        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "参数windowId为空!");
        }
        windowService.saveAeaServiceWindowItem(windowId, itemVerIds);
        return new ResultForm(true);
    }

    @ApiOperation(value = "保存窗口阶段", notes = "保存窗口阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "stageIds", value = "阶段ids集合", required = true, allowMultiple = true, dataType = "String")
    })
    @RequestMapping("/saveAeaServiceWindowStage.do")
    public ResultForm saveAeaServiceWindowStage(String windowId, String[] stageIds) throws Exception {

        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "参数windowId为空!");
        }
        windowService.saveAeaServiceWindowStage(windowId, stageIds);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDeleteAeaServiceWindowItem.do")
    @ApiOperation("批量删除窗口事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windItemIds", value = "事项ids", required = true, allowMultiple = true, dataType = "String")
    })
    public ResultForm batchDeleteAeaServiceWindowItem(String[] windItemIds) {

        if (windItemIds != null && windItemIds.length > 0) {
            windowItemService.batchDelAeaServiceWindowItemByIds(windItemIds);
            log.info("成功执行批量删除！");
            return new ResultForm(true);
        }
        return new ResultForm(false, "请选择需要删除的选项！");
    }

    @RequestMapping("/getMaxSortNo.do")
    @ApiOperation("获取最大排序号")
    public Long getMaxSortNo() {
        return windowService.getMaxSortNo(getCurrentOrgId());
    }

    @RequestMapping("/listSelfAndChildRegionByOrgId.do")
    @ApiOperation("获取当下组织的窗口")
    public List<ZtreeNode> listSelfAndChildRegionByOrgId() {

        String orgId = SecurityContext.getCurrentOrgId();
        return windowService.listSelfAndChildRegionByOrgId(orgId);
    }

    @RequestMapping("/listRegionTreeByOrgId.do")
    @ApiOperation("获取子区域")
    public ElementUiRsTreeNode listRegionTreeByOrgId() {
        String orgId = SecurityContext.getCurrentOrgId();
        return windowService.listRegionTreeByOrgId(orgId);
    }

    @RequestMapping("/listAllUserByOrgId.do")
    @ApiOperation("获取当下组织所有用户")
    public List<ElementUiRsTreeNode> listAllUserByOrgId() {

        String orgId = SecurityContext.getCurrentOrgId();
        return windowService.listAllUserByOrgId(orgId);
    }

    @RequestMapping("listWindowStageByWindowId.do")
    @ApiOperation("通过关键字查询窗口事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true, dataType = "String"),
    })
    public List<AeaParStage> listWindowStageByWindowId(String windowId, String keyword) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        return windowStageService.findWindowStageByKeyword(windowId, keyword);
    }

    @RequestMapping("listWindowItemByWindowId.do")
    @ApiOperation("通过关键字查询窗口事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true, dataType = "String"),
    })
    public List<AeaServiceWindowItem> listWindowItemByWindowId(String windowId, String keyword) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }

        AeaServiceWindow window = windowService.getAeaServiceWindowById(windowId);
        if (window != null && !window.getRootOrgId().equalsIgnoreCase(getCurrentOrgId())) {
            return null;
        }

        return windowItemService.listWindowItemByWindowId(windowId, keyword);
    }

    @RequestMapping("listWindowUserByWindowId.do")
    @ApiOperation("通过关键字查询窗口人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true, dataType = "String"),
    })
    public List<AeaServiceWindowUser> listWindowUserByWindowId(String windowId, String keyword) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        AeaServiceWindow window = windowService.getAeaServiceWindowById(windowId);
        if (window != null && !window.getRootOrgId().equalsIgnoreCase(getCurrentOrgId())) {
            return null;
        }

        return windowUserService.listWindowUserByWindowId(windowId, keyword);
    }

    @RequestMapping("/saveAeaServiceWindowUser.do")
    @ApiOperation("保存窗口人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userIds", value = "人员ID", required = true, dataType = "String[]"),
    })
    public ResultForm saveAeaServiceWindowUser(String windowId, String[] userIds) throws Exception {

        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "参数windowId为空!");
        }
        AeaServiceWindow window = windowService.getAeaServiceWindowById(windowId);
        if (window != null && !window.getRootOrgId().equalsIgnoreCase(getCurrentOrgId())) {
            return null;
        }
        windowService.saveAeaServiceWindowUsers(windowId, userIds);
        return new ResultForm(true);
    }

    @RequestMapping("/saveAgencyUser.do")
    @ApiOperation("保存代办中心人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userIds", value = "人员ID", required = true, dataType = "String[]"),
    })
    public ResultForm saveAgencyUser(String windowId, String[] userIds) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "参数windowId为空!");
        }
        try {
            windowService.saveAgencyUsers(windowId, userIds);
            resultForm.setMessage("保存成功。");
            resultForm.setSuccess(true);
        }catch (Exception e){
            resultForm.setMessage("保存失败。"+e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    @RequestMapping("/deleteAgencyUser.do")
    @ApiOperation("删除代办中心人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "userIds", value = "人员ID", required = true, dataType = "String[]"),
    })
    public ResultForm deleteAgencyUser(String windowId, String[] userIds) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if (StringUtils.isBlank(windowId)) {
            return new ResultForm(false, "参数windowId为空!");
        }
        try {
            windowService.deleteAgencyUser(windowId, userIds);
            resultForm.setMessage("删除成功。");
            resultForm.setSuccess(true);
        }catch (Exception e){
            resultForm.setMessage("删除失败。"+e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    private String getCurrentOrgId() {

        return SecurityContext.getCurrentOrgId();
    }

    /**
     * 是否当前资源
     *
     * @param window
     * @return
     */
    private ResultForm isCurrentOrgId(AeaServiceWindow window) {

        ResultForm form = new ResultForm(false);
        AeaServiceWindow newWindow = windowService.getAeaServiceWindowById(window.getWindowId());
        if (newWindow != null) {
            if (!window.getRootOrgId().equalsIgnoreCase(newWindow.getRootOrgId())) {
                form.setMessage("您操作的不是本组织下的资源！");
                return form;
            }
        }
        if (newWindow == null) {
            form.setMessage("未找到资源！");
            return form;
        }
        form.setSuccess(true);
        return form;
    }

    @GetMapping("/listAllStageTree")
    @ApiOperation("查询主题阶段树")
    public ResultForm listAllStageTree() {
        try {
            List<ElementUiRsTreeNode> list = windowStageService.listAllStageTree();
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            log.error("查询主题阶段树异常");
            e.printStackTrace();
        }
        return new ResultForm(false, "查询主题阶段树异常");
    }

    @GetMapping("/stage")
    @ApiOperation("查询窗口可办理阶段")
    @ApiImplicitParam(name = "windowId", value = "窗口ID", dataType = "string", required = true)
    public ResultForm findWindowStageByWindowId(String windowId) {
        List<AeaParStage> list = windowStageService.findWindowStageByWindowId(windowId);
        return new ContentResultForm<>(true, list);
    }

    @GetMapping("/findAeaServiceWindowStage")
    @ApiOperation("查询窗口可办理阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "windowId", value = "窗口ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true, dataType = "String"),
    })
    public ResultForm findAeaServiceWindowStageByWindowId(String windowId,String keyword) {
        List<AeaServiceWindowStage> list = windowStageService.findAeaServiceWindowStageByWindowId(windowId, keyword);
        return new ContentResultForm<>(true, list);
    }

    @PostMapping("/saveWindowStage")
    @ApiOperation("保存窗口阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaWindowStageVo", value = "窗口ID", dataType = "AeaWindowStageVo"),
    })
    public ResultForm saveWindowStage(@RequestBody AeaWindowStageVo aeaWindowStageVo) {

        try {
            windowStageService.saveWindowStage(aeaWindowStageVo.getWindowId(),aeaWindowStageVo.getAeaServiceWindowStageList());
            return new ResultForm(true);
        } catch (Exception e) {
            log.error("删除窗口阶段异常");
            e.printStackTrace();
        }
        return new ResultForm(false);

    }

}

