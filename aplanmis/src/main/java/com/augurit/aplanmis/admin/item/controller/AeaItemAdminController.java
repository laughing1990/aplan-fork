package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.bpm.common.sfengine.config.SFProperties;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemVer;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemVerAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/2 002 16:38
 * @desc
 **/
@RestController
@RequestMapping("/aea/item")
public class AeaItemAdminController {

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AeaItemAdminService aeaItemAdminService;

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private SFProperties sFProperties;

    /**
     * 实施清单
     */
    @RequestMapping("/index2.do")
    public ModelAndView indexAeaItem2(ModelMap modelMap, String itemNature) {

        if(StringUtils.isBlank(itemNature)){
            throw new InvalidParameterException("参数itemNature为空!");
        }
        modelMap.addAttribute("itemNature", itemNature);
        // 0-行政事项，8-中介服务事项，9-服务协同，6-市政公用服务
        switch (itemNature) {
            case "0": {
                modelMap.addAttribute("itemModelName", "行政事项库");
                break;
            }
            case "6":{
                modelMap.addAttribute("itemModelName", "市政公用服务事项库");
                break;
            }
            case "8":{
                modelMap.addAttribute("itemModelName", "中介服务事项库");
                break;
            }
            case "9":{
                modelMap.addAttribute("itemModelName", "服务协同事项库");
                break;
            }
            default: {
                break;
            }
        }
        loadBscItemData(modelMap);
        return new ModelAndView("ui-jsp/item/list/item_index2");
    }

    /**
     * 部门事项
     */
    @RequestMapping("/dept/index.do")
    public ModelAndView index(ModelMap modelMap) {

        OpusLoginUser login = SecurityContext.getOpusLoginUser();
        String topOrgId = login.getCurrentOrgId();
        String userId = login.getUser().getUserId();
        List<OpuOmOrg> userOrgs = opuOmOrgMapper.listOpuOmUserOrgByUserId(userId);
        List<OpuOmOrg> needOrgs = new ArrayList<>();
        if(userOrgs!=null && userOrgs.size()>=0){
            for(OpuOmOrg org : userOrgs){
                String orgSeq = org.getOrgSeq();
                if(StringUtils.isNotBlank(orgSeq)&&orgSeq.contains("."+ topOrgId +".")){
                    needOrgs.add(org);
                }
            }
        }else{
            throw new InvalidParameterException("无法获取当前用户所在组织!");
        }
        if(needOrgs!=null && needOrgs.size()>=0){
            modelMap.put("userOrgs", needOrgs);
        }else{
            throw new InvalidParameterException("无法获取当前用户所在组织!");
        }
        loadBscItemData(modelMap);
        return new ModelAndView("ui-jsp/item/dept/item_dept_index");
    }

    /**
     * 用户事项管理
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/item/user.do")
    public ModelAndView indexItemUser(ModelMap modelMap){

        loadBscItemData(modelMap);
        return new ModelAndView("ui-jsp/item/user/item_user");
    }

    /**
     * 事项下放
     *
     * @return
     */
    @RequestMapping("/delegateIndex.do")
    public ModelAndView delegateIndex() {

        return new ModelAndView("ui-jsp/item/list/delegateIndex");
    }

    /**
     * 数据字典值
     *
     * @param modelMap
     */
    private void loadBscItemData(ModelMap modelMap) {

        String topOrgId = SecurityContext.getCurrentOrgId();
        // 数据字段承诺办结时限单位
        List<BscDicCodeItem> dueUnits = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrgId);
        modelMap.put("dueUnits", dueUnits);

        // 事项类型
        List<BscDicCodeItem> itemTypes = bscDicCodeService.getActiveItemsByTypeCode("DEPT_ITEM_TYPE", topOrgId);
        modelMap.put("itemTypes", itemTypes);

        // 处理结果发送方式
        List<BscDicCodeItem> sendResultModes = bscDicCodeService.getActiveItemsByTypeCode("SEND_RESULT_MODE", topOrgId);
        modelMap.put("sendResultModes", sendResultModes);

        // 办件类型
        List<BscDicCodeItem> itemPropertys = bscDicCodeService.getActiveItemsByTypeCode("ITEM_PROPERTY", topOrgId);
        modelMap.put("itemPropertys", itemPropertys);

        // 事项状态
        List<BscDicCodeItem> itemStatus = bscDicCodeService.getActiveItemsByTypeCode("ITEM_STATUS", topOrgId);
        modelMap.put("itemStatus", itemStatus);

        // 受理方式
        List<BscDicCodeItem> slfss = bscDicCodeService.getActiveItemsByTypeCode("ITEM_SLFS", topOrgId);
        modelMap.put("slfss", slfss);

        // 服务对象
        List<BscDicCodeItem> itemFwjgxzs = bscDicCodeService.getActiveItemsByTypeCode("ITEM_FWJGXZ", topOrgId);
        modelMap.put("itemFwjgxzs", itemFwjgxzs);
    }

    /**
     * 修复版本序列
     *
     * @param basic
     * @return
     */
    @RequestMapping("/initItemVerSeq.do")
    public ResultForm initItemVerSeq(AeaItemBasic basic){

        aeaItemAdminService.initItemVerSeq(basic);
        return new ResultForm(true);
    }

    /**
     *
     * 创建标准事项
     *
     * @param basic
     * @return
     */
    @RequestMapping("/initStandItem.do")
    public ResultForm initStandItem(AeaItemBasic basic){

        aeaItemAdminService.initStandItem(basic);
        return new ResultForm(true);
    }

    @RequestMapping("/syncItemRegion.do")
    public ResultForm syncItemRegion(AeaItemBasic basic){

        aeaItemAdminService.syncItemRegion(basic);
        return new ResultForm(true);
    }

    /**
     * 事项前置检查--事项检查
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/frontCheckItem.do")
    public ModelAndView frontCheckItem(String busiType, String busiId, String stateVerId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, stateVerId, false, modelMap);
        return new ModelAndView("ui-jsp/kitymind/item/front/set_front_check_item");
    }

    /**
     * 事项前置检查
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/frontCheckManage.do")
    public ModelAndView frontCheckManage(String busiType, String busiId, String stateVerId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, stateVerId, false, modelMap);
//        return new ModelAndView("ui-jsp/kitymind/item/front/set_front_check_manage");
        return new ModelAndView("ui-jsp/kitymind/item/frontCheck/front_check_manage_index");
    }

    /**
     * 事项输出材料
     *
     * @param busiType
     * @param busiId
     * @param modelMap
     * @return
     */
    @RequestMapping("/indexSetItemOut.do")
    public ModelAndView indexSetItemOut(String busiType, String busiId,String stateVerId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, stateVerId, false, modelMap);
        return new ModelAndView("ui-jsp/kitymind/item/out/set_item_out_index");
    }

    /**
     * 事项一张表单
     *
     * @param busiType
     * @param busiId
     * @param modelMap
     * @return
     */
    @RequestMapping("/oneFormManage.do")
    public ModelAndView oneFormManage(String busiType, String busiId,String stateVerId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, stateVerId, false, modelMap);
        return new ModelAndView("ui-jsp/kitymind/item/oneForm/set_item_oneForm_index");
    }

    /**
     * 中介超市事项输出材料
     *
     * @param busiType
     * @param busiId
     * @param modelMap
     * @return
     */
    @RequestMapping("/indexSetAgentServiceItemOut.do")
    public ModelAndView indexSetAgentServiceItemOut(String busiType, String busiId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, "", false, modelMap);
        return new ModelAndView("ui-jsp/item/list/item_out_index");

    }

    /**
     * 编辑事项目录
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param modelMap
     * @return
     */
    @RequestMapping("/editItemDirectory.do")
    public ModelAndView editItemDirectory(String busiType, String busiId, String stateVerId, ModelMap modelMap) {

        handleItemVerIsEditable(busiType, busiId, stateVerId, true, modelMap);
        return new ModelAndView("ui-jsp/kitymind/item/detail/set_item_dir_index");
    }

    /**
     * 处理事项版本信息
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param isNeedItemInfo
     * @param modelMap
     */
    private void handleItemVerIsEditable(String busiType, String busiId, String stateVerId,
                                         Boolean isNeedItemInfo, ModelMap modelMap){

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(busiId);
        Assert.notNull(ver,"busiId="+ busiId +"事项版本不存在!");
        if(ver!=null&&ver.isEditable()){
            curIsEditable = true;
        }
        String isNeedState = Status.OFF;
        String useOneForm = Status.OFF;
        String isCheckItem = Status.OFF;
        String isCheckItemform = Status.OFF;
        String isCheckPartform = Status.OFF;
        String isCheckProj = Status.OFF;
        String isCheckStage = Status.OFF;
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(busiId, SecurityContext.getCurrentOrgId());
        if (aeaItemBasic != null){
            isNeedState = StringUtils.isNotBlank(aeaItemBasic.getIsNeedState())?aeaItemBasic.getIsNeedState():Status.OFF;
            useOneForm = StringUtils.isNotBlank(aeaItemBasic.getUseOneForm())?aeaItemBasic.getUseOneForm():Status.OFF;
            isCheckItem = StringUtils.isNotBlank(aeaItemBasic.getIsCheckItem())?aeaItemBasic.getIsCheckItem():Status.OFF;
            isCheckPartform = StringUtils.isNotBlank(aeaItemBasic.getIsCheckPartform())?aeaItemBasic.getIsCheckPartform():Status.OFF;
            isCheckProj = StringUtils.isNotBlank(aeaItemBasic.getIsCheckProj())?aeaItemBasic.getIsCheckProj():Status.OFF;
        }
        if (StringUtils.isNotBlank(this.sFProperties.getRestWebApp())) {
            modelMap.put("restWebApp", this.sFProperties.getRestWebApp());
        } else {
            modelMap.put("restWebApp", "http://localhost:7071/bpm-admin/");
        }
        modelMap.put("currentBusiType", busiType);
        modelMap.put("currentBusiId", busiId);
        modelMap.put("currentStateVerId", stateVerId);
        modelMap.put("isNeedState", isNeedState);
        modelMap.put("curIsEditable", curIsEditable);
        modelMap.put("handWay", Status.ON);
        modelMap.put("useOneForm", useOneForm);
        modelMap.put("isCheckItem", isCheckItem);
        modelMap.put("isCheckItemform", isCheckItemform);
        modelMap.put("isCheckPartform", isCheckPartform);
        modelMap.put("isCheckProj", isCheckProj);
        modelMap.put("isCheckStage", isCheckStage);
        if(isNeedItemInfo){
            modelMap.put("itemBasic", aeaItemBasic);
        }
    }

    @RequestMapping("/gtreeItem.do")
    private List<AeaItem> gtreeItem() {

        com.github.pagehelper.PageHelper.clearPage();
        List<AeaItem> list = aeaItemAdminService.gtreeItem();
        return list;
    }

    /**
     * 是否可以选择标注事项
     *
     * @param basic
     * @return
     */
    @RequestMapping("/gtreeTestRunOrPublishedItem.do")
    public List<AeaItem> gtreeTestRunOrPublishedItem(AeaItemBasic basic) {

        com.github.pagehelper.PageHelper.clearPage();
        List<AeaItem> list = aeaItemAdminService.gtreeTestRunOrPublishedItem(basic);
        return list;
    }

    @RequestMapping("/gtreeLatestItem.do")
    public List<AeaItem> gtreeLatestItem(AeaItemBasic basic) {

        com.github.pagehelper.PageHelper.clearPage();
        List<AeaItem> list = aeaItemAdminService.gtreeLatestItem(basic);
        return list;
    }

    @RequestMapping("/gtreeOkVerItemNoRelSelf.do")
    public List<AeaItem> gtreeOkVerItemNoRelSelf(AeaItemBasic basic) {

        com.github.pagehelper.PageHelper.clearPage();
        List<AeaItem> list = aeaItemAdminService.gtreeOkVerItemNoRelSelf(basic);
        return list;
    }

    /**
     * 组织事项异步加载
     *
     * @param id
     * @param name
     * @param type
     * @return
     */
    @RequestMapping("/gtreeItemAsyncZTree.do")
    public List<AeaItem> gtreeItemAsyncZTree(String id, String name, String type) {

        return aeaItemAdminService.gtreeItemAsyncZTree(id, name, type);
    }

    @RequestMapping("/gtreeItemCond.do")
    public List<AeaItemCond> gtreeItemCond(String itemId) {

        return aeaItemAdminService.gtreeItemCond(itemId);
    }

    /**
     * 异步获取表和字段数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/gtreeTableColumnAsyncZTree.do")
    public List<ZtreeNode> gtreeTableColumnAsyncZTree(String id) {

        return aeaItemAdminService.gtreeTableColumnAsyncZTree(id);
    }

    /**
     * 同步获取表和字段数据
     *
     * @return
     */
    @RequestMapping("/gtreeTableColumnSyncZTree.do")
    public List<ZtreeNode> gtreeTableColumnSyncZTree() {

        return aeaItemAdminService.gtreeTableColumnSyncZTree();
    }


    /**
     * 跳转中介事项配置信息页面
     * @param itemVerId
     * @return
     */
    @GetMapping("/itemService.do")
    public ModelAndView itemService(String itemVerId){
        ModelAndView modelAndView = new ModelAndView("ui-jsp/supermarket_manage/item/item_cfg_index");
        if(StringUtils.isNotBlank(itemVerId)){
            boolean curIsEditable = false;
            AeaItemVer aeaItemVer = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
            if(aeaItemVer != null){
                modelAndView.addObject("itemId",aeaItemVer.getItemId());
                modelAndView.addObject("itemVerId",itemVerId);
                modelAndView.addObject("itemName",aeaItemVer.getItemName());
                if(aeaItemVer.isEditable()){
                    curIsEditable = true;
                }
                modelAndView.addObject("curIsEditable",curIsEditable);
            }
        }
        return modelAndView;
    }

    @RequestMapping("/getUnSelectedParFrontItemTree.do")
    public List<AeaItem> getUnSelectedParFrontItemTree(String stageId,String frontItemId) {
        List<AeaItem> list = aeaItemAdminService.getUnSelectedParFrontItemTree(stageId,frontItemId);
        return list;
    }

    @RequestMapping("/getUnSelectedItemFrontItemTree.do")
    public List<AeaItem> getUnSelectedItemFrontItemTree(String itemVerId,String frontItemId) {
        List<AeaItem> list = aeaItemAdminService.getUnSelectedItemFrontItemTree(itemVerId,frontItemId);
        return list;
    }
}
