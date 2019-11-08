package com.augurit.aplanmis.admin;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideExtendAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemVerAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * AplanmisAdminIndexController
 *
 * @author jjt
 * @date 2019/8/28
 *
 */
@Api(description = "前缀：/apanmis/admin",tags={"工改后端菜单入口"})
@RestController
@RequestMapping("/apanmis/admin")
public class AplanmisAdminIndexController {

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaParStageAdminService aeaParStageAdminService;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private AeaItemGuideExtendAdminService aeaItemGuideExtendAdminService;

    /**
     * 主题管理
     *
     * @return
     */
    @RequestMapping("/themeIndex.html")
    public ModelAndView themeIndex() {

        return new ModelAndView("admin/theme/index/index");
    }

    /**
     * 行政事项、中介服务事项、服务协同事项、公共服务事项管理
     *
     * @return
     */
    @RequestMapping("/itemIndex.html")
    public ModelAndView itemIndex() {

        return new ModelAndView("admin/item/list/index");
    }

    /**
     * 部门事项管理
     *
     * @return
     */
    @RequestMapping("/dept/itemIndex.html")
    public ModelAndView deptItemIndex() {

        return new ModelAndView("admin/item/dept/index");
    }

    /**
     * 电子证照定义管理 -- 开发中，待优化
     *
     * @return
     */
    @RequestMapping("/certIndex.html")
    public ModelAndView certIndex() {

        return new ModelAndView("admin/cert/certIndex");
    }

    /**
     * 材料类型 -- 开发中，待优化
     *
     * @return
     */
    @RequestMapping("/matTypeIndex.html")
    public ModelAndView matTypeIndex() {

        return new ModelAndView("admin/mat/matTypeIndex");
    }

    /**
     * 材料标准清单
     *
     * @return
     */
    @RequestMapping("/matIndex.html")
    public ModelAndView matIndex() {

        return new ModelAndView("admin/mat/matIndex");
    }

    /**
     * 服务窗口
     *
     * @return
     */
    @GetMapping("/windowIndex.html")
    public ModelAndView windowIndex(){

        return new ModelAndView("window/index");
    }

    /**
     * 服务窗口（过时）
     *
     * @return
     */
    @GetMapping("/windowOldIndex.html")
    public ModelAndView windowOldIndex(){

        return new ModelAndView("window/indexOld");
    }

    /**
     * 法律法规管理 -- 前后端分离中
     *
     * @return
     */
    @RequestMapping("/legalIndex.html")
    public ModelAndView legalIndex() {

        return new ModelAndView("admin/legal/legalIndex");
    }

    /**
     * 总表管理 -- 前后端分离成功
     *
     * @return
     */
    @RequestMapping("/oneformIndex.html")
    public ModelAndView oneformIndex() {

        return new ModelAndView("admin/oneform/oneformIndex");
    }

    /**
     * 主题导航定义
     *
     * @return
     */
    @RequestMapping("/theme/navIndex.html")
    public ModelAndView themeNavIndex() {

        return new ModelAndView("admin/theme/nav/index");
    }

    /**
     * 主题样本
     *
     * @return
     */
    @RequestMapping("/theme/sampleIndex.html")
    public ModelAndView sampleIndex() {

        return new ModelAndView("admin/theme/sample/index");
    }

    /**
     * 信用管理-红黑管理
     *
     * @return
     */
    @RequestMapping("/credit/redblack.html")
    public ModelAndView redblackIndex() {

        return new ModelAndView("admin/credit/redblack");
    }

    /**
     * 信用管理-企业管理
     */
    @RequestMapping("/credit/unit/index.html")
    public ModelAndView unitIndex() {

        return new ModelAndView("admin/credit/unitIndex");
    }

    /**
     * 信用管理-信用详情
     */
    @RequestMapping("/credit/summary/index.html")
    public ModelAndView summaryIndex() {

        return new ModelAndView("admin/credit/summaryIndex");
    }

    // =============  事项操作指南部分 ========

    /**
     * 基本信息
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/basicInfo.html")
    public ModelAndView itemBasicInfo(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        return new ModelAndView("admin/item/guide/basicInfoIndex");
    }

    /**
     * 前置条件
     *
     * @param busiType
     * @param busiId
     * @param stateVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/preCond.html")
    public ModelAndView itempreCond(String busiType, String busiId, String stateVerId, ModelMap map) {

        handItemVerEditable(busiId, map);
        AeaItemBasic aeaItemBasic = aeaItemBasicAdminService.getOneByItemVerId(busiId, SecurityContext.getCurrentOrgId());
        map.put("currentBusiType", busiType);
        map.put("currentBusiId", busiId);
        map.put("currentStateVerId", stateVerId);
        map.put("isNeedState", (aeaItemBasic!=null&&StringUtils.isNotBlank(aeaItemBasic.getIsNeedState()))?aeaItemBasic.getIsNeedState():Status.OFF);
        map.put("handWay", Status.ON);
        map.put("useOneForm", Status.OFF);
        return new ModelAndView("admin/item/guide/preCondIndex");
    }

    /**
     * 设立依据
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/baseOnInfo.html")
    public ModelAndView itemBaseOnInfo(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        return new ModelAndView("admin/item/guide/baseOnInfoIndex");
    }

    /**
     * 服务窗口
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/windowIndex.html")
    public ModelAndView itemWindow(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        return new ModelAndView("admin/item/guide/windowIndex");
    }

    /**
     * 扩展信息
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/extendInfo.html")
    public ModelAndView itemExtendInfo(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemGuideExtend aeaItemGuideExtend = new AeaItemGuideExtend();
        aeaItemGuideExtend.setItemVerId(itemVerId);
        aeaItemGuideExtend.setRootOrgId(rootOrgId);
        List<AeaItemGuideExtend> aeaItemGuideExtends = aeaItemGuideExtendAdminService.listAeaItemGuideExtend(aeaItemGuideExtend);
        if (aeaItemGuideExtends != null && aeaItemGuideExtends.size() > 0) {
            aeaItemGuideExtend = aeaItemGuideExtends.get(0);
            List<BscAttForm> zzzResultGuidList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_GUIDE_EXTEND","ZZZ_RESULT_GUID", aeaItemGuideExtend.getId(), null, rootOrgId, null);
            aeaItemGuideExtend.setZzzResultGuidNum(zzzResultGuidList == null ? 0L : zzzResultGuidList.size());
        }
        map.addAttribute("aeaItemGuideExtend", aeaItemGuideExtend);
        // 主题分类
        List<BscDicCodeItem> themeTypes = bscDicCodeService.getActiveItemsByTypeCode("THEME_TYPE", rootOrgId);
        map.addAttribute("themeTypes", themeTypes);
        return new ModelAndView("admin/item/guide/extendInfoIndex");
    }

    /**
     * 收费信息 -- 已经完成
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/charge.html")
    public ModelAndView itemChargeIndex(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        return new ModelAndView("admin/item/guide/chargeIndex");
    }

    /**
     * 常见问题问答 -- 已经完成
     *
     * @param itemVerId
     * @param map
     * @return
     */
    @RequestMapping("/item/guide/question.html")
    public ModelAndView itemQuestionIndex(String itemVerId, ModelMap map) {

        handItemVerEditable(itemVerId, map);
        return new ModelAndView("admin/item/guide/questionIndex");
    }

    /**
     * 统一处理事项版本是否可以编辑
     *
     * @param itemVerId
     * @param map
     */
    private void handItemVerEditable(String itemVerId, ModelMap map){

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(itemVerId);
        Assert.notNull(ver, "itemVerId=" + itemVerId + "事项版本不存在!");
        if (ver != null && ver.isEditable()) {
            curIsEditable = true;
        }
        map.put("itemVerId", itemVerId);
        map.put("curIsEditable", curIsEditable);
    }

    private void handItemVerEditableForMind(String busiType, String busiId, String stateVerId, Boolean isNeedItemInfo, ModelMap modelMap){

        boolean curIsEditable = false;
        AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(busiId);
        Assert.notNull(ver,"busiId="+ busiId +"事项版本不存在!");
        if(ver!=null&&ver.isEditable()){
            curIsEditable = true;
        }
        String isNeedState = Status.OFF;
        String useOneForm = Status.OFF;
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(busiId, SecurityContext.getCurrentOrgId());
        if (aeaItemBasic != null){
            isNeedState = StringUtils.isNotBlank(aeaItemBasic.getIsNeedState())?aeaItemBasic.getIsNeedState():Status.OFF;
            useOneForm = StringUtils.isNotBlank(aeaItemBasic.getUseOneForm())?aeaItemBasic.getUseOneForm():Status.OFF;
        }
        modelMap.put("currentBusiType", busiType);
        modelMap.put("currentBusiId", busiId);
        modelMap.put("currentStateVerId", stateVerId);
        modelMap.put("isNeedState", isNeedState);
        modelMap.put("curIsEditable", curIsEditable);
        modelMap.put("handWay", Status.ON);
        modelMap.put("useOneForm", useOneForm);
        if(isNeedItemInfo){
            modelMap.put("itemBasic", aeaItemBasic);
        }
    }

    // =============  阶段操作指南部分 ========

    /**
     * 基本信息
     * 
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/stage/guide/basicInfo.html")
    public ModelAndView itemBasicInfo(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("admin/theme/guide/basicInfoIndex");
    }

    /**
     * 设立依据
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/stage/guide/baseOnInfo.html")
    public ModelAndView stageBaseOnInfo(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("admin/theme/guide/baseOnInfoIndex");
    }

    /**
     * 服务窗口
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/stage/guide/serviceWindow.html")
    public ModelAndView stageServiceWindow(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("admin/theme/guide/serviceWindow");
    }

    /**
     * 阶段收费信息 -- 已经完成
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/stage/guide/charge.html")
    public ModelAndView stageChargeIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("admin/theme/guide/chargeIndex");
    }

    /**
     * 阶段常见问题问答 -- 已经完成
     *
     * @param modelMap
     * @param stageId
     * @return
     */
    @RequestMapping("/stage/guide/question.html")
    public ModelAndView itemQuestionIndex(ModelMap modelMap, String stageId) {

        handThemeVerIsEditable(modelMap, stageId);
        return new ModelAndView("admin/theme/guide/questionIndex");
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

    /**
     * 系统跟踪日志日志
     *
     * @return
     */
    @RequestMapping("/sys/traceLog.html")
    public ModelAndView sysTraceLog() {

        return new ModelAndView("admin/log/sysTraceLog");
    }
}
