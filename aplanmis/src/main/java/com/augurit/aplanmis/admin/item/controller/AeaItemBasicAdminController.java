package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.item.vo.CarryOutItemVo;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.bsc.domain.MindResponse;
import com.augurit.aplanmis.common.constants.AeaMindConst;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemVer;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemCondAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemVerAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 行政审批事项表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/basic")
public class AeaItemBasicAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemBasicAdminController.class);

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @Autowired
    private AeaItemCondAdminService aeaItemCondAdminService;


    /**
     *  行政事项等等四库使用
     *
     * @param aeaItemBasic
     * @param page
     * @return
     */
    @RequestMapping("/listLatestAeaItemBasicTreeByPage.do")
    public EasyuiPageInfo<AeaItemBasic> listLatestAeaItemBasicTreeByPage(AeaItemBasic aeaItemBasic, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemBasic);
        PageInfo<AeaItemBasic> pageInfo = aeaItemBasicAdminService.latestAeaItemBasicTree(aeaItemBasic, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 保存更新事项
     *
     * @param aeaItemBasic
     * @param docTemplateFile
     * @param applyTableDemoFile
     * @param applyTableTemplateFile
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAeaItemBasic.do")
    public ResultForm saveAeaItemBasic(AeaItemBasic aeaItemBasic, MultipartFile docTemplateFile,
                                       MultipartFile applyTableDemoFile,
                                       MultipartFile applyTableTemplateFile) throws Exception {

        if (docTemplateFile != null && docTemplateFile.getBytes().length > 0) {
            byte[] fileBytes = docTemplateFile.getBytes();
            aeaItemBasic.setDocTemplate(fileBytes);
        }
        if (applyTableDemoFile != null && applyTableDemoFile.getBytes().length > 0) {
            byte[] fileBytes = applyTableDemoFile.getBytes();
            aeaItemBasic.setApplyTableDemo(fileBytes);
        }

        if (applyTableTemplateFile != null && applyTableTemplateFile.getBytes().length > 0) {
            byte[] fileBytes = applyTableTemplateFile.getBytes();
            aeaItemBasic.setApplyTableTemplate(fileBytes);
        }
        if (StringUtils.isNotBlank(aeaItemBasic.getItemBasicId())) {
            aeaItemBasicAdminService.updateAeaItemBasic(aeaItemBasic);
        } else {
            aeaItemBasic.setItemBasicId(UUID.randomUUID().toString());
            aeaItemBasicAdminService.saveAeaItemBasic(aeaItemBasic);
        }
        return new ContentResultForm<>(true, aeaItemBasic);
    }

    /**
     * 获取基本信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/getAeaItemBasic.do")
    public AeaItemBasic getAeaItemBasic(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaItem对象，ID为：{}", id);
            return aeaItemBasicAdminService.getAeaItemBasicById(id);
        } else {
            logger.debug("构建新的AeaItem对象");
            return new AeaItemBasic();
        }
    }

    @RequestMapping("/delItemBasicAtt.do")
    public ResultForm delItemBasicAtt(String itemBasicId, String type) throws Exception {

        if(StringUtils.isBlank(itemBasicId)){
            throw new InvalidParameterException("参数itemBasicId为空!");
        }
        if(StringUtils.isBlank(type)){
            throw new InvalidParameterException("参数type为空!");
        }
        AeaItemBasic basic = new AeaItemBasic();
        basic.setItemBasicId(itemBasicId);
        byte[] attContent = new byte[]{};
        if(type.equals("docTemplate")){
            basic.setDocTemplate(attContent);
        }else if(type.equals("applyTableDemo")){
            basic.setApplyTableDemo(attContent);
        }else{
            basic.setApplyTableTemplate(attContent);
        }
        aeaItemBasicAdminService.updateAeaItemBasic(basic);
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaItemBasicById.do")
    public ResultForm deleteAeaBasicItemById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除行政审批事项表Form对象，对象id为：{}", id);
            aeaItemBasicAdminService.deleteAeaItemBasicById(id);
        }
        return new ResultForm(true);
    }

    /**
     * 查看附件
     *
     * @param fileTypeName
     * @param itemBasicId
     * @param fileType
     * @return
     */
    @RequestMapping("/indexItemDocOrTemplateFile.do")
    public ModelAndView indexItemDocOrTemplateFile(String fileTypeName, String itemBasicId, String fileType) {

        ModelAndView modelAndView = new ModelAndView("ui-jsp/aplanmis/item/item_doc_template_view");
        modelAndView.addObject("itemBasicId", itemBasicId);
        modelAndView.addObject("fileType", fileType);
        modelAndView.addObject("fileTypeName", fileTypeName);
        return modelAndView;
    }

    /**
     * 预览事项文件
     *
     * @param itemBasicId
     * @param fileType
     * @return
     */
    @RequestMapping("/getItemDocOrTemplateFile.do")
    public byte[] getItemDocTemplate(String itemBasicId, String fileType) {

        if (StringUtils.isNotBlank(itemBasicId) && StringUtils.isNotBlank(fileType)) {
            AeaItemBasic item = aeaItemBasicAdminService.getAeaItemBasicById(itemBasicId);
            if ("docTemplate".equals(fileType)) {
                return item.getDocTemplate();
            } else if ("applyTableDemo".equals(fileType)) {
                return item.getApplyTableDemo();
            } else if ("applyTableTemplate".equals(fileType)) {
                return item.getApplyTableTemplate();
            }
        }
        return null;
    }

    @RequestMapping("/dept/listAeaItemOfOrgByPage.do")
    public EasyuiPageInfo<AeaItemBasic> listAeaDeptItemByPage(AeaItemBasic aeaItemBasic, Page page) {

        PageInfo<AeaItemBasic> pageInfo = aeaItemBasicAdminService.listAeaItemBasicByOrgIds(aeaItemBasic, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 事项前置条件Mind
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/preConditionMind/index.do")
    public ModelAndView preConditionMind(String busiType, String busiId,String stateVerId, ModelMap modelMap) {

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(busiId);
        Assert.notNull(ver,"busiId="+ busiId +"事项版本不存在!");
        if(ver!=null&&ver.isEditable()){
            curIsEditable = true;
        }
        AeaItemBasic aeaItemBasic = aeaItemBasicAdminService.getOneByItemVerId(busiId, SecurityContext.getCurrentOrgId());
        modelMap.put("currentBusiType", busiType);
        modelMap.put("currentBusiId", busiId);
        modelMap.put("currentStateVerId", stateVerId);
        modelMap.put("isNeedState", aeaItemBasic.getIsNeedState());
        modelMap.put("curIsEditable", curIsEditable);
        modelMap.put("handWay", Status.ON);
        modelMap.put("useOneForm", Status.OFF);
        return new ModelAndView("ui-jsp/kitymind/item/detail/preConditionMind");
    }

    /**
     *  获取树状数据接口
     *
     * @param busiType
     * @param busiId
     * @return
     * @throws Exception
     */
    @RequestMapping("/preConditionMind/list.do")
    public MindResponse preConditionMindList(String busiType, String busiId) throws Exception {

        MindResponse result = new MindResponse();
        MindBaseNode mindBaseNode = null;
        if (StringUtils.isNotBlank(busiType)) {
            if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE)) {

            } else if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)) {
                mindBaseNode = aeaItemCondAdminService.listAeaItemCondMindView(busiId);
            }
        }
        MindBaseNode[] arr = new MindBaseNode[]{mindBaseNode};
        result.setCode(1);
        result.setMsg("操作成功");
        result.setData(arr);
        return result;
    }

    /**
     * 保存数据接口
     *
     * @param mindExportObj
     * @return
     * @throws Exception
     */
    @RequestMapping("/preConditionMind/save.do")
    public MindResponse preConditionMindSave(@RequestBody MindExportObj mindExportObj) throws Exception {

        MindResponse result = new MindResponse();
        result.setCode(1);
        result.setMsg("保存成功");
        result.setData(null);
        if (mindExportObj == null || mindExportObj.getData() == null || StringUtils.isBlank(mindExportObj.getData().getNodeTypeCode())) {
            return result;
        }
        if (AeaMindConst.MIND_NODE_TYPE_CODE_STAGE.equals(mindExportObj.getData().getNodeTypeCode())) {

        } else if (MindType.ITEM.getValue().equals(mindExportObj.getData().getNodeTypeCode())) {
            aeaItemCondAdminService.saveOrUpdateAeaItemCondMindView(mindExportObj);
        }
        return result;
    }

    /**
     * 根据标准事项查询所有事项事项
     *
     * @param itemId 标准事项id
     * @return 实施事项列表
     */
    @GetMapping("/carry/out/items")
    @ApiOperation("根据标准事项id查询对应的实施事项")
    @ApiImplicitParam(value = "标准事项id", paramType = "query", dataType = "string")
    public ContentResultForm<List<CarryOutItemVo>> findCarryOutItems(@RequestParam String itemId) {
        List<AeaItemBasic> itemBasics = aeaItemBasicAdminService.findCarryOutItemsByItemId(itemId, SecurityContext.getCurrentOrgId());
        List<CarryOutItemVo> carryOutItems = itemBasics.stream().map(CarryOutItemVo::build).collect(Collectors.toList());
        return new ContentResultForm<>(true, carryOutItems, "Query carry out items success");
    }

    @ApiOperation(value = "查询编号是否唯一存在", notes = "查询编号是否唯一存在")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "itemId", value = "事项itemId", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "itemCode", value = "事项itemCode", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/checkUniqueItemCode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkUniqueItemCode(String itemId, String itemCode) {

        if (StringUtils.isBlank(itemCode)) {
            return "false";
        }
        return aeaItemBasicAdminService.checkUniqueItemCode(itemId, itemCode, SecurityContext.getCurrentOrgId()) + "";
    }

    @ApiOperation(value = "查询唯一分类编号是否唯一存在", notes = "查询唯一分类编号是否唯一存在")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "itemId", value = "事项itemId", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "isCatalog", value = "是否标准事项isCatalog", dataType = "String", required = true, paramType = "query"),
        @ApiImplicitParam(name = "itemCategoryMark", value = "唯一分类编号", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/checkUniqueItemCategoryMark.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkUniqueItemCategoryMark(String itemId, String isCatalog, String itemCategoryMark) {

        if(StringUtils.isBlank(isCatalog)){
            return "false";
        }
        if (StringUtils.isBlank(itemCategoryMark)) {
            return "false";
        }
        // 不检查实施事项
        if(isCatalog.equals("0")){
            return "true";
        }
        return aeaItemBasicAdminService.checkUniqueItemCategoryMark(itemId, isCatalog, itemCategoryMark, SecurityContext.getCurrentOrgId()) + "";
    }

    /**
     * 中介服务事项查询已关联的行政事项
     * @param itemId
     * @return
     */
    @PostMapping("/listAeaItemBasicForSupermaketNoPage.do")
    public List<AeaItemBasic> listAeaItemBasicForSupermaketNoPage(String itemId) throws Exception{
        List<AeaItemBasic> list = aeaItemBasicAdminService.listAeaItemBasicForSupermaketNoPage(itemId);
        return list;
    }

    /**
     * 中介服务事项配置查询行政事项列表方法
     * @param aeaItemBasic （itemNature=0）
     * @param page
     * @return
     */
    @RequestMapping("/listAeaItemBasicForSupermaket.do")
    public EasyuiPageInfo<AeaItemBasic> listAeaItemBasicForSupermaket(AeaItemBasic aeaItemBasic, Page page) throws Exception{
        logger.debug("分页查询，过滤条件为{}", JsonUtils.toJson(aeaItemBasic));
        aeaItemBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        EasyuiPageInfo<AeaItemBasic> pageInfo = aeaItemBasicAdminService.listAeaItemBasicForSupermaket(aeaItemBasic, page);
        return pageInfo;
    }

    /**
     * 保存中介事项关联行政事项
     * @param itemId          中介事项ID
     * @param saveItemIds    要保存的事项ID数组
     * @param cancelItemIds  要取消的事项ID数组
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveConfigItem.do")
    public ResultForm saveConfigItem(String itemId,String[] saveItemIds,String[] cancelItemIds) throws Exception{
        ResultForm resultForm = aeaItemBasicAdminService.saveConfigItem(itemId,saveItemIds,cancelItemIds);
        return resultForm;
    }
}
