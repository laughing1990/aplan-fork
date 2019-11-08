package com.augurit.aplanmis.admin.mind.controller;

import com.augurit.agcloud.bpm.admin.tpl.vo.AppProcCaseDefVo;
import com.augurit.agcloud.bpm.common.domain.ActTplApp;
import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.service.ActTplAppService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.bsc.domain.MindResponse;
import com.augurit.aplanmis.common.constants.AeaMindConst;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateVerMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.admin.tpl.DgActTplAppAdminService;
import com.augurit.aplanmis.common.vo.ActTplAppTriggerAdminVo;
import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.augurit.aplanmis.common.vo.AppProcCaseDefTreeVo;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.augurit.aplanmis.common.service.admin.item.impl.AeaItemStateVersionAdminService.StateVersionStrategy.MAX_VERSION;

/**
 * @author ZhangXinhui
 * @date 2019/8/8 008 15:32
 * @desc
 **/
@RestController
@RequestMapping("/rest/mind")
@Slf4j
public class MindAdminController {

    private static final String BACKEND_KITYMIND_PROCESS = "ui-jsp/kitymind/process/processModeler";
    private static final String BACKEND_KITYMIND_PROCESS_STAGE = "ui-jsp/kitymind/process/processModeler_stage";

    @Autowired
    private AeaItemStateVersionAdminService aeaItemStateVersionAdminService;
    @Autowired
    private AeaParStageAdminService aeaParStageAdminService;
    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaItemStateVerMapper aeaItemStateVersionMapper;
    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;
    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;
    @Autowired
    private AeaParStateAdminService aeaParStateAdminService;
    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;
    @Autowired
    private DgActTplAppAdminService dgActTplAppAdminService;
    @Autowired
    private AeaItemAdminService aeaItemAdminService;
    @Autowired
    private AeaParStateXmindAdminServiceImpl aeaParStateXmindAdminService;
    @Autowired
    private AeaItemStateXmindAdminServiceImpl aeaItemStateXmindAdminService;
    @Autowired
    private ActTplAppService actTplAppService;

    /**
     * 阶段/事项--情形配置-统一入口
     *
     * @param busiType         阶段/事项
     * @param busiId           阶段ID/事项ID
     * @param isSetState       是否设置 分情形状态
     * @param paramIsNeedState 参数值-分情形状态
     */
    @RequestMapping("/mindIndex.do")
    @Transactional
    public ModelAndView configSituationEntry(String busiType, String busiId,
                                             String stateVerId, String isSetState,
                                             String paramIsNeedState) {

        if (StringUtils.isBlank(busiType)) {
            throw new IllegalArgumentException("参数busiType为空!");
        }
        if (StringUtils.isBlank(busiId)) {
            throw new IllegalArgumentException("参数事项版本busiId为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currentBusiType", busiType);
        modelAndView.addObject("currentBusiId", busiId);

        //是否分情形
        String isNeedState;
        String handWay = Status.ON;
        String useOneForm = Status.OFF;
        boolean curIsEditable = false;
        boolean itemVerIsEditable = false;
        if (AeaMindConst.MIND_NODE_TYPE_CODE_STAGE.equals(busiType)) {

            AeaParStage aeaParStage = aeaParStageAdminService.getAeaParStageById(busiId);
            if (aeaParStage != null && StringUtils.isNotBlank(aeaParStage.getIsNeedState())) {
                isNeedState = aeaParStage.getIsNeedState();
                handWay = aeaParStage.getHandWay();
                useOneForm = aeaParStage.getUseOneForm();
                if (StringUtils.isNotBlank(isSetState) && isSetState.equals(Status.ON)) {
                    isNeedState = paramIsNeedState;
                    aeaParStage.setIsNeedState(isNeedState);
                    aeaParStageAdminService.updateAeaParStage(aeaParStage);
                }
                AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(aeaParStage.getThemeVerId());
                if (ver != null && ver.isEditable()) {
                    curIsEditable = true;
                }
                modelAndView.addObject("curIsEditable", curIsEditable);
                modelAndView.addObject("itemVerIsEditable", itemVerIsEditable);
                modelAndView.addObject("isNeedState", isNeedState);
                modelAndView.addObject("handWay", StringUtils.isBlank(handWay) ? Status.ON : handWay);
                modelAndView.addObject("useOneForm", StringUtils.isBlank(useOneForm)? Status.OFF: useOneForm);
                if (Status.ON.equals(isNeedState)) {
                    modelAndView.setViewName("ui-jsp/agcloud/bsc/mind/mindIndex");
                } else if (Status.OFF.equals(isNeedState) && Status.ON.equals(handWay)) {
                    modelAndView.setViewName("ui-jsp/kitymind/stage/state/noSituationStage");
                } else if (Status.OFF.equals(isNeedState) && Status.OFF.equals(handWay)) {
                    modelAndView.setViewName("ui-jsp/kitymind/stage/items/show_stage_item_index");
                }
            }
        } else if (AeaMindConst.MIND_NODE_TYPE_CODE_ITEM.equals(busiType)) {

            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(busiId, rootOrgId);
            if (aeaItemBasic != null && StringUtils.isNotBlank(aeaItemBasic.getIsNeedState())) {
                isNeedState = aeaItemBasic.getIsNeedState();
                useOneForm = aeaItemBasic.getUseOneForm();
                if (StringUtils.isNotBlank(isSetState) && isSetState.equals(Status.ON)) {
                    isNeedState = paramIsNeedState;
                    aeaItemBasic.setIsNeedState(isNeedState);
                    aeaItemBasicAdminService.updateAeaItemBasic(aeaItemBasic);
                }
                AeaItemVer ver = aeaItemVerAdminService.getAeaItemVerById(busiId);
                Assert.notNull(ver, "busiId=" + busiId + "事项版本不存在!");
                if (ver != null && ver.isEditable()) {
                    itemVerIsEditable = true;
                }
                modelAndView.addObject("itemVerIsEditable", itemVerIsEditable);
                modelAndView.addObject("isNeedState", isNeedState);
                modelAndView.addObject("handWay", StringUtils.isBlank(handWay) ? Status.ON : handWay);
                modelAndView.addObject("useOneForm", StringUtils.isBlank(useOneForm)? Status.OFF: useOneForm);
                AeaItemStateVer maxVer;
                if (StringUtils.isNotBlank(stateVerId)) {
                    maxVer = aeaItemStateVersionMapper.getAeaItemStateVerById(stateVerId);
                } else {
                    maxVer = aeaItemStateVersionAdminService
                            .getSpecificVersion(MAX_VERSION, busiId, rootOrgId);
                }
                Assert.notNull(maxVer, "stateVerId=" + stateVerId + "情形版本不存在!");
                modelAndView.addObject("stateVerId", maxVer == null ? null : maxVer.getItemStateVerId());

                if (Status.ON.equals(isNeedState)) {
                    if (ver != null && maxVer != null && ver.isEditable() && maxVer.isEditable()) {
                        curIsEditable = true;
                    }
                    modelAndView.addObject("curIsEditable", curIsEditable);
                    modelAndView.setViewName("ui-jsp/agcloud/bsc/mind/mindIndex");
                } else if (Status.OFF.equals(isNeedState)) {

                    if (ver != null && ver.isEditable()) {
                        curIsEditable = true;
                    }
                    modelAndView.addObject("curIsEditable", curIsEditable);
                    modelAndView.setViewName("ui-jsp/agcloud/bsc/mind/noSituationItem");
                }
            }
        }
        return modelAndView;
    }

    /**
     * 获取树状数据接口
     *
     * @param busiType 当前业务类型 stage阶段  item事项
     * @param busiId  当前业务id
     * @param stateVerId 情形版本id
     * @param aeaMindUi 当前控制思维导图展示字段
     * @return
     * @throws Exception
     */
    @RequestMapping("/affairItemSituation/v3/datajson/view/list.do")
    public MindResponse affairItemSituation(String busiType, String busiId, String stateVerId, AeaMindUi aeaMindUi) throws Exception {

        MindResponse result = new MindResponse();
        MindBaseNode mindBaseNode = null;
        if (StringUtils.isNotBlank(busiType)) {

            // 初始化查询参数
            if (aeaMindUi == null) {
                aeaMindUi = new AeaMindUi();
                aeaMindUi.setShowMat(true);
                aeaMindUi.setShowCert(true);
                aeaMindUi.setShowSituationLinkItem(true);
                aeaMindUi.setShowForm(true);
            }

            String rootOrgId = SecurityContext.getCurrentOrgId();

            // 阶段思维导图获取数据
            if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE)) {

                mindBaseNode = aeaParStateAdminService.listAeaStageStateMindView(busiId, aeaMindUi, rootOrgId);

                // 事项思维导图获取数据
            } else if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)) {

                mindBaseNode = aeaItemStateAdminService.listAeaItemStateTreeByItemVerId(busiId, stateVerId, rootOrgId, aeaMindUi);
            }
        }
        MindBaseNode[] arr = new MindBaseNode[]{mindBaseNode};
        result.setCode(1);
        result.setMsg("操作成功");
        result.setAeaMindUi(aeaMindUi);
        result.setData(arr);
        return result;
    }

    /**
     * 保存数据接口
     *
     * @param mindExportObj
     * @param stateVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/affairItemSituation/v3/save.do")
    public MindResponse save(@RequestBody MindExportObj mindExportObj, String stateVerId) throws Exception {

        MindResponse result = new MindResponse();
        result.setCode(1);
        result.setMsg("保存成功");
        result.setData(null);
        if (mindExportObj == null || mindExportObj.getData() == null || StringUtils
                .isBlank(mindExportObj.getData().getNodeTypeCode())) {
            return result;
        }
        try {
            String userId =  SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            // 阶段思维导图保存
            if (AeaMindConst.MIND_NODE_TYPE_CODE_STAGE.equals(mindExportObj.getData().getNodeTypeCode())) {

                aeaParStateAdminService.saveOrUpdateAeaStageStateMindView(mindExportObj, rootOrgId);

                // 事项思维导图保存
            } else if (MindType.ITEM.getValue().equals(mindExportObj.getData().getNodeTypeCode())) {

                aeaItemStateAdminService.refreshItemTree(mindExportObj, mindExportObj.getData().getId(), stateVerId, rootOrgId, userId);
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/stage/processModeler.do")
    public ModelAndView stageProcessModeler(String busiType, String busiId) throws Exception {

        ModelAndView modelAndView = new ModelAndView(BACKEND_KITYMIND_PROCESS_STAGE);
        modelAndView.addObject("currentBusiType", busiType);
        modelAndView.addObject("currentBusiId", busiId);
        AeaParStage aeaParStage = aeaParStageAdminService.getAeaParStageById(busiId);
        if (aeaParStage != null) {
            if (StringUtils.isBlank(aeaParStage.getAppId()) || actTplAppService.getActTplAppById(aeaParStage.getAppId()) == null) {
                //20190909 修改，阶段模板名称为 阶段主题+阶段名称
                String appName = aeaParStage.getStageName();
                AeaParThemeVer aeaParThemeVer = aeaParThemeVerAdminService.getAeaParThemeVerById(aeaParStage.getThemeVerId());
                if(aeaParThemeVer != null){
                    appName = aeaParThemeVer.getThemeName() + "—" + appName;
                }
                ActTplApp actTplAppAllInfo = dgActTplAppAdminService
                        .createActTplAppAllInfo(appName, Status.OFF);
                aeaParStage.setAppId(actTplAppAllInfo.getAppId());
                aeaParStageAdminService.updateAeaParStage(aeaParStage);
            }
            aeaItemAdminService.syncActTplAppDefLimitTime(aeaParStage.getAppId(), aeaParStage.getDueNum().longValue(), aeaParStage
                    .getDueUnit());
            modelAndView.addObject("appId", aeaParStage.getAppId());
            modelAndView.addObject("currentBusiCode", aeaParStage.getStageCode());
            modelAndView.addObject("isNeedState", StringUtils.isNotBlank(aeaParStage.getIsNeedState())?aeaParStage.getIsNeedState():Status.OFF);
            modelAndView.addObject("handWay", StringUtils.isBlank(aeaParStage.getHandWay()) ? Status.ON : aeaParStage.getHandWay());
            modelAndView.addObject("useOneForm", StringUtils.isBlank(aeaParStage.getUseOneForm())?Status.OFF : aeaParStage.getUseOneForm());
        }
        return modelAndView;
    }

    /**
     * 查看某个情形版本的数据
     *
     * @param itemVerId  事项版本id
     * @param stateVerId 情形版本id
     */
    @GetMapping("/view/state/version")
    public MindResponse getItemTreeData4SpecificStateVer(String itemVerId, String stateVerId, AeaMindUi aeaMindUi) {

        Assert.notNull(itemVerId, "itemVerId is null");
        Assert.notNull(stateVerId, "stateVerId is null");

        if (aeaMindUi == null) {
            aeaMindUi = new AeaMindUi();
            aeaMindUi.setShowMat(true);
            aeaMindUi.setShowCert(true);
            aeaMindUi.setShowSituationLinkItem(true);
            aeaMindUi.setShowForm(true);
        }

        MindBaseNode res = aeaItemStateAdminService
                .listAeaItemStateTreeByItemVerId(itemVerId, stateVerId, SecurityContext.getCurrentOrgId(), aeaMindUi);

        return new MindResponse(1, "操作成功", new MindBaseNode[]{res});
    }

    @RequestMapping("/count/unpublished/state/num.do")
    public ResultForm countUnpublishedStateNum(String itemVerId) {

        if (StringUtils.isBlank(itemVerId)) {
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        AeaItemStateVer version = aeaItemStateVersionAdminService
                .getSpecificVersion(AeaItemStateVersionAdminService.StateVersionStrategy.UN_PUBLISHED, itemVerId, SecurityContext
                        .getCurrentOrgId());
        if (version == null) {
            return new ResultForm(true, "不存在最新未发布情形版本");
        } else {
            return new ResultForm(false, "存在最新未发布情形版本!");
        }
    }

    /**
     * 复制事项版本下最新的试运行或者已发布版本
     *
     * @param itemVerId 事项版本id
     */
    @RequestMapping("/copy/max/state/version")
    public ResultForm copyMaxPublishedOrTestRunStateVer(String itemVerId) {

        if (StringUtils.isBlank(itemVerId)) {
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        String newStateVerId = aeaItemStateAdminService.copyMaxPublishedOrTestRunStateVer(itemVerId);
        log.info("复制成功，newStateVerId: {}", newStateVerId);
        return new ResultForm(true, "复制成功!");
    }


    /**
     * 基于某个版本复制为一个新的版本
     *
     * @param itemVerId 事项版本id
     */
    @RequestMapping("/copy/state/version")
    public ResultForm copyOneStateVer(String itemVerId, String stateVerId) {

        if (StringUtils.isBlank(itemVerId)) {
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        if (StringUtils.isBlank(stateVerId)) {
            throw new IllegalArgumentException("情形版本stateVerId为空!");
        }
        String newStateVerId = aeaItemStateAdminService.copyItemStateVerRelData(itemVerId, null, stateVerId, null);
        log.info("复制成功，newStateVerId: {}", newStateVerId);
        return new ResultForm(true, "复制成功!");
    }

    @RequestMapping("/itemCondition.do")
    public ModelAndView itemCondition(String busiType, String busiId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("agcloud/bsc/mind/itemCondition");
        modelAndView.addObject("currentBusiType", busiType);
        modelAndView.addObject("currentBusiId", busiId);
        return modelAndView;
    }

    @RequestMapping("/mindStage.do")
    public ModelAndView mindStage() {
        return new ModelAndView("ui-jsp/aplanmis/mind/stage/mind_stage_index");
    }

    @RequestMapping("/processDefs.do")
    public List<AppProcCaseDefPlusAdminVo> processDefs(String appId, String stateVerId, String keyword) throws Exception {

        List<AppProcCaseDefPlusAdminVo> appProcCaseDefList = dgActTplAppAdminService.getAppProcCaseDefVo(appId, keyword);
        if (appProcCaseDefList != null) {
            appProcCaseDefList.sort((Comparator<AppProcCaseDefVo>) (arg0, arg1) -> {
                if (arg0.getIsMasterDef() == null) {
                    return 1;
                } else if (arg1.getIsMasterDef() == null) {
                    return -1;
                } else if ("1".equals(arg0.getIsMasterDef())) {
                    return -1;
                } else if ("1".equals(arg1.getIsMasterDef())) {
                    return 1;
                } else {
                    return (arg1.getIsMasterDef()).compareTo((arg0.getIsMasterDef()));
                }
            });

            AeaItemState itemState = new AeaItemState();
            itemState.setStateVerId(stateVerId);
            itemState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaItemState> itemStateList = aeaItemStateAdminService.listAeaItemState(itemState);
            if (itemStateList != null && itemStateList.size() > 0) {
                for (AppProcCaseDefVo procCaseDefVo : appProcCaseDefList) {
                    for (AeaItemState state : itemStateList) {
                        if (procCaseDefVo.getStartEl() != null && procCaseDefVo.getStartEl().contains(state.getItemStateId())) {
                            procCaseDefVo.setStartElName(state.getStateName());
                        } else if (procCaseDefVo.getStartEl() != null && !procCaseDefVo.getStartEl().contains(state.getItemStateId())
                                && procCaseDefVo.getStartElName() == null) {
                            procCaseDefVo.setStartElName(procCaseDefVo.getStartEl());
                        }
                    }
                }
            }

        }
        return appProcCaseDefList;
    }

    @RequestMapping("/processDefTree.do")
    public List<AppProcCaseDefTreeVo> processDefTree(String appId,boolean isPid, String stateVerId, String keyword) throws Exception {
        List<AppProcCaseDefTreeVo> appProcCaseDefTreeVo = dgActTplAppAdminService.getAppProcCaseDefTreeVo(appId, stateVerId, keyword,isPid);
        return appProcCaseDefTreeVo;
    }

    @RequestMapping("/subProcessDefs.do")
    public List<ActTplAppTriggerAdminVo> subProcessDefs(String appFlowdefId, String appId, String defKey, String nodeId,String keyword) {

        return aeaItemBasicAdminService.getActTplAppTriggerByAppFlowDefIds(appFlowdefId, appId, nodeId,keyword);
    }

    @RequestMapping("/exportAsXmindFile.do")
    public void exportAsXmindFile(String busiType, String busiId, String stateVerId, HttpServletResponse response, HttpServletRequest request) throws Exception {

        if(StringUtils.isBlank(busiType)){
            throw new InvalidParameterException("参数busiType为空！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)&&StringUtils.isBlank(busiId)) {
            throw new InvalidParameterException("对应的事项版本不存在！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE)&&StringUtils.isBlank(busiId)) {
            throw new InvalidParameterException("对应的阶段不存在！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)&&StringUtils.isBlank(stateVerId)){
            throw new InvalidParameterException("对应事项情形版本不存在！");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE)) {

            AeaParStage aoaParStage = aeaParStageAdminService.getAeaParStageById(busiId);
            aeaParStateXmindAdminService.downloadXmindFileByParStage(aoaParStage, request, response);

        } else if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)) {

            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(busiId, rootOrgId);
            aeaItemStateXmindAdminService.downloadXmindFileByItem(aeaItemBasic, stateVerId, rootOrgId, request, response);
        }
    }

    /**
     * 导入情形思维图
     * @param file
     * @param busiType
     * @param busiId
     * @param stateVerId 20190317 新增情形版本字段 第一次导入或新增时改字段为null
     * @return
     */
    @RequestMapping("/importXmindFile.do")
    public ResultForm importXmindFile(@RequestParam("file") MultipartFile file, String busiType, String busiId, String stateVerId) {

        if(file==null){
            throw new InvalidParameterException("文件内容为空！");
        }
        if(StringUtils.isBlank(busiType)){
            throw new InvalidParameterException("参数busiType为空！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)&&StringUtils.isBlank(busiId)) {
            throw new InvalidParameterException("对应的事项版本不存在！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE)&&StringUtils.isBlank(busiId)) {
            throw new InvalidParameterException("对应的阶段不存在！");
        }
        if (busiType.equals(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM)&&StringUtils.isBlank(stateVerId)){
            throw new InvalidParameterException("对应事项情形版本不存在！");
        }
        try {
            if (StringUtils.endsWith(file.getOriginalFilename(), ".xmind")) {
                if (AeaMindConst.MIND_NODE_TYPE_CODE_ITEM.equals(busiType)) {

                    log.info("添加的情形位于{}事项下", busiId);
                    aeaItemStateXmindAdminService.loadXmindStateFileToPojo(file.getInputStream(), busiId, stateVerId);
                    return new ResultForm(true);
                }
                if (AeaMindConst.MIND_NODE_TYPE_CODE_STAGE.equals(busiType)) {

                    log.info("导入的阶段情形对应的阶段ID: {}mindIndex.do", busiId);
                    aeaParStateXmindAdminService.loadXmindFileToParSate(file.getInputStream(), busiId);
                    return new ResultForm(true);
                }
                return new ResultForm(false, "对应的节点类型非事项或阶段！");
            } else {
                return new ResultForm(false, "文件上传类型只支持.xmind");
            }
        } catch (Exception e) {
            return new ResultForm(false, "文件上传失败！");
        }
    }

    @RequestMapping("/item/processModeler.do")
    @Transactional
    public ModelAndView itemProcessModeler(String busiType, String busiId, String stateVerId) {

        ModelAndView modelAndView = new ModelAndView(BACKEND_KITYMIND_PROCESS);
        modelAndView.addObject("currentBusiType", busiType);
        modelAndView.addObject("currentBusiId", busiId);
        modelAndView.addObject("currentStateVerId", stateVerId);
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(busiId,SecurityContext.getCurrentOrgId());
        if (aeaItemBasic != null) {
            if (StringUtils.isBlank(aeaItemBasic.getAppId()) || actTplAppService.getActTplAppById(aeaItemBasic.getAppId()) == null) {
                try {
                    ActTplApp actTplAppAllInfo = dgActTplAppAdminService.createActTplAppAllInfo(aeaItemBasic.getItemName(), "1");
                    aeaItemBasic.setAppId(actTplAppAllInfo.getAppId());
                    aeaItemBasicAdminService.updateAeaItemBasic(aeaItemBasic);
                }catch (Exception e){
                    log.warn("为事项创建流程模板时报错, itemId: {}, itemVerId: {}", aeaItemBasic.getItemId(), aeaItemBasic.getItemVerId());
                    e.printStackTrace();
                    throw new RuntimeException("为事项创建流程模板时报错", e);
                }

            }
            aeaItemAdminService.syncActTplAppDefLimitTime(aeaItemBasic.getAppId(),aeaItemBasic.getDueNum().longValue(),aeaItemBasic.getBjType());

            modelAndView.addObject("appId", aeaItemBasic.getAppId());
            modelAndView.addObject("currentBusiCode", aeaItemBasic.getItemCode());
            modelAndView.addObject("isNeedState", StringUtils.isNotBlank(aeaItemBasic.getIsNeedState())?aeaItemBasic.getIsNeedState():Status.OFF);
            modelAndView.addObject("handWay", Status.ON);
            modelAndView.addObject("useOneForm", StringUtils.isNotBlank(aeaItemBasic.getUseOneForm())?aeaItemBasic.getUseOneForm():Status.OFF);
        }
        return modelAndView;
    }

    @RequestMapping("/getTaskNodeList.do")
    public Collection<FlowElement> getTaskNodeList(String defKey) {
        try {
            return aeaItemAdminService.getTaskNodeList(defKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/saveOrUpdateSubTrigger.do")
    public ResultForm saveOrUpdateSubTrigger(ActTplAppTrigger actTplAppTrigger) {
        try {
            boolean had = aeaItemAdminService.checkHadTrigger(actTplAppTrigger);
            if(had){
                return new ResultForm(false, "当前节点已经配置了此流程，请勿重复配置！");
            }
            aeaItemAdminService.saveOrUpdateTrigger(actTplAppTrigger);
            return new ResultForm(true, "保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存失败！");
        }
    }

    @RequestMapping("/batchDelSubTrigger.do")
    public ResultForm batchDelSubTrigger(String[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                log.debug("删除触发配置，对象id为：{}", Arrays.asList(ids));
                aeaItemAdminService.batchDelSubTrigger(ids);
                return new ResultForm(true);
            } else {
                return new ResultForm(false, "操作id为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/delSubTrigger.do")
    public ResultForm delSubTrigger(String id) {
        try {
            if (id != null) {
                log.debug("删除触发配置，对象id为：{}", id);
                aeaItemAdminService.delSubTrigger(id);
                return new ResultForm(true);
            } else {
                return new ResultForm(false, "操作id为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getSubTriggerById.do")
    public ActTplAppTrigger getSubTriggerById(String id) {
        try {
            return aeaItemAdminService.getSubTriggerById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
