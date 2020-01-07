package com.augurit.aplanmis.front;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局页面跳转controller
 */
@RequestMapping("/apanmis/page")
@RestController
@Api(tags = "菜单入口")
public class AplanmisPageIndexController {

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;

    @GetMapping("/stageApplyIndex")
    @ApiOperation("菜单-并联申报页")
    public ModelAndView stageApplyIndex() {
        return new ModelAndView("apply/index");
    }

    @GetMapping("/singleApplyIndex/{itemVerId}")
    @ApiOperation("菜单-单项申报页")
    public ModelAndView singleApplyIndex(@PathVariable String itemVerId) {
        ModelAndView modelAndView = new ModelAndView("apply/index-single");
        modelAndView.addObject("itemVerId", itemVerId);
        return modelAndView;
    }

    @GetMapping("/stageApproveIndex")
    @ApiOperation("菜单-并联申报页")
    public ModelAndView stageApproveIndex() {
        return new ModelAndView("approve/index");
    }

    @GetMapping("/applyForm.html")
    @ApiOperation("iframe申请表")
    public ModelAndView applyForm() {
        return new ModelAndView("approve/applyForm");
    }

    @GetMapping("/materialAnnex.html")
    @ApiOperation("iframe材料附件")
    public ModelAndView materialAnnex() {
        return new ModelAndView("approve/materialAnnex");
    }

    @GetMapping("/approvalOpinions.html")
    @ApiOperation("iframe审批意见")
    public ModelAndView approvalOpinions() {
        ModelAndView modelAndView = new ModelAndView("approve/approvalOpinions");
        OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        modelAndView.addObject("currentUserName", userinfo.getUserName());
        return modelAndView;
    }

    @GetMapping("/opinionForm.html")
    @ApiOperation("iframe意见汇总表")
    public ModelAndView opinionForm() {
        return new ModelAndView("approve/opinionForm");
    }

    /**
     * 并联申报 一张表单url构造
     * enableParamItem 是否启用参数 itemId，用于只查看指定事项的情况
     * itemId 指定事项的id
     */
    @GetMapping("/urlStageOneForm.html")
    public ContentResultForm<?> urlStageOneForm(String masterEntityKey, boolean enableParamItem, String itemId, String projInfoId, HttpServletRequest request) {
        String realUrl = request.getContextPath() + "/rest/oneform/common/getListForm4StageOneForm?";
        StringBuilder params = new StringBuilder();
        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(masterEntityKey);
            if (aeaHiApplyinst != null) {
                params.append("applyinstId=").append(aeaHiApplyinst.getApplyinstId());
                AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
                if (aeaHiParStageinst != null) {
                    params.append("&stageId=").append(aeaHiParStageinst.getStageId());
                }

                if (enableParamItem) {
                    params.append("&itemids=").append(itemId);
                } else {
                    List<AeaHiIteminst> aeaHiIteminstList;
                    aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
                    for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
                        params.append("&itemids=").append(aeaHiIteminst.getItemId());
                    }
                }
            }
            if (StringUtils.isNotBlank(projInfoId)) {
                params.append("&projInfoId=").append(projInfoId);
            }
            params.append("&showBasicButton=false");
            params.append("&includePlatformResource=false");
            realUrl += params;
            return new ContentResultForm<>(true, realUrl, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "一张表单参数转换失败！");
        }
    }


    @GetMapping("/specialProcedures.html")
    @ApiOperation("特殊程序发起窗口")
    public ModelAndView specialProcedures() {
        return new ModelAndView("approve/specialProcedures");
    }

    @GetMapping("/queryPartsIndex.html")
    @ApiOperation("菜单-所有办件")
    public ModelAndView queryPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        modelAndView.addObject("isInformCommit", "0");
        return modelAndView;
    }

    @GetMapping("/queryCommitPartsIndex.html")
    @ApiOperation("菜单-承诺制办件")
    public ModelAndView queryCommitPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        modelAndView.addObject("isInformCommit", "1");
        return modelAndView;
    }

    @GetMapping("/queryHandlerPartsIndex.html")
    @ApiOperation("菜单-经办办件")
    public ModelAndView queryHandlerPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", true);
        modelAndView.addObject("defaultInstState", "");
        modelAndView.addObject("isInformCommit", "0");
        return modelAndView;
    }

    @GetMapping("/queryEntrustPartsIndex.html")
    @ApiOperation("菜单-单位办件")
    public ModelAndView queryEntrustPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        modelAndView.addObject("isInformCommit", "0");
        return modelAndView;
    }

    @GetMapping("/queryWindowApplyIndex.html")
    @ApiOperation("菜单-综窗申报")
    public ModelAndView queryWindowApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    @GetMapping("/queryHandlerApplyIndex.html")
    @ApiOperation("菜单-经办申报")
    public ModelAndView queryHandlerApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", true);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    @GetMapping("/queryApplyIndex.html")
    @ApiOperation("菜单-所有申报")
    public ModelAndView applyInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    @GetMapping("/queryWaitDoTasksIndex.html")
    @ApiOperation("菜单-待办任务")
    public ModelAndView queryWaitDoTasksIndex() {
        return new ModelAndView("view/queryWaitDoTasksIndex");
    }

    @GetMapping("/queryWaitCancelTasksByCkIndex.html")
    @ApiOperation("菜单-撤件待办件")
    public ModelAndView queryWaitCancelTasksIndexByCk() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByCkIndex");
        modelAndView.addObject("state", 0);
        return modelAndView;
    }

    @GetMapping("/queryDoneCancelTasksByCkIndex.html")
    @ApiOperation("菜单-撤件经办件")
    public ModelAndView queryDoneCancelTasksIndexByCk() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByCkIndex");
        modelAndView.addObject("state", 1);
        return modelAndView;
    }

    @GetMapping("/queryDoneCancelTasksByBmIndex.html")
    @ApiOperation("菜单-撤件经办件")
    public ModelAndView queryDoneCancelTasksIndexByBm() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByBmIndex");
        modelAndView.addObject("state", 1);
        return modelAndView;
    }

    @GetMapping("/queryWaitCancelTasksByBmIndex.html")
    @ApiOperation("菜单-撤件待办件")
    public ModelAndView queryWaitCancelTasksByBmIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByBmIndex");
        modelAndView.addObject("state", 0);
        return modelAndView;
    }

    @GetMapping("/queryDoneTasksIndex.html")
    @ApiOperation("菜单-已办任务")
    public ModelAndView queryDoneTasksIndex() {
        return new ModelAndView("view/queryDoneTasksIndex");
    }

    @GetMapping("/queryConcludedTasksIndex.html")
    @ApiOperation("菜单-办结任务")
    public ModelAndView queryFinishTasksIndex() {
        return new ModelAndView("view/queryConcludedTasksIndex");
    }

    @GetMapping("/queryPreliminaryTasksIndex.html")
    @ApiOperation("菜单-网上待预审")
    public ModelAndView queryPreliminaryTasksIndex() {
        return new ModelAndView("view/queryPreliminaryTasksIndex");
    }

    @GetMapping("/queryDismissedApplyIndex.html")
    @ApiOperation("菜单-不予受理申报")
    public ModelAndView queryDismissedApplyIndex() {
        return new ModelAndView("view/queryDismissedApplyIndex");
    }

    @GetMapping("/queryNeedCorrectTasksIndex.html")
    @ApiOperation("菜单-待补正办件")
    public ModelAndView queryNeedCorrectTasksIndex() {
        return new ModelAndView("view/queryNeedCorrectTasksIndex");
    }


    @GetMapping("/queryNeedCompletedApplyIndex.html")
    @ApiOperation("菜单-待补全申报")
    public ModelAndView queryNeedCorrectApplyIndex() {
        return new ModelAndView("view/queryNeedCompletedApplyIndex");
    }

    @GetMapping("/queryNeedConfirmCompletedApplyIndex.html")
    @ApiOperation("菜单-补全待确认申报")
    public ModelAndView queryNeedConfirmCompletedApplyIndex() {
        return new ModelAndView("view/queryNeedConfirmCompletedApplyIndex");
    }

    @GetMapping("/queryPickupCheck.html")
    @ApiOperation("菜单-取件登记")
    public ModelAndView queryPickupCheckIndex() {
        return new ModelAndView("view/queryPickupCheck");
    }

    @GetMapping("/queryPickupCheckWin.html")
    @ApiOperation("菜单-窗口取件")
    public ModelAndView queryPickupCheckWinIndex() {
        return new ModelAndView("view/queryPickupCheckWin");
    }

    @GetMapping("/queryPickupCheckExpress.html")
    @ApiOperation("菜单-邮寄取件")
    public ModelAndView queryPickupCheckExpressIndex() {
        return new ModelAndView("view/queryPickupCheckExpress");
    }

    @GetMapping("/queryPickupCheckFinish.html")
    @ApiOperation("菜单-取件登记-已取件")
    public ModelAndView queryPickupCheckFinishIndex() {
        return new ModelAndView("view/queryPickupCheckFinish");
    }

    @GetMapping("/queryDoneliminaryTasksIndex.html")
    @ApiOperation("菜单-网上已预审")
    public ModelAndView queryDoneliminaryTasksIndex() {
        return new ModelAndView("view/queryDoneliminaryTasksIndex");
    }

    @GetMapping("/querySpecialProcedureTasksIndex.html")
    @ApiOperation("菜单-特殊程序件")
    public ModelAndView querySpecialProcedureTasksIndex() {
        return new ModelAndView("view/querySpecialProcedureTasksIndex");
    }


    @GetMapping("/queryGlobalProjectInfoIndex.html")
    @ApiOperation("菜单-全局项目库")
    public ModelAndView queryGlobalProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", false);
        modelAndView.addObject("onlyOrg", false);
        modelAndView.addObject("handler", false);
        return modelAndView;
    }

    @GetMapping("/queryItemDisgreeIndex.html")
    @ApiOperation("菜单-办结（不通过）-部门")
    public ModelAndView itemDisgreeIndex() {
        return new ModelAndView("view/queryItemDisgreeIndex");
    }

    @GetMapping("/queryItemOutScopeIndex.html")
    @ApiOperation("菜单-不予受理(部门)")
    public ModelAndView itemOutScopeIndex() {
        return new ModelAndView("view/queryItemOutScopeIndex");
    }

    @GetMapping("/queryItemAgreeToleranceIndex.html")
    @ApiOperation("菜单-容缺通过件")
    public ModelAndView itemAgreeToleranceIndex() {
        return new ModelAndView("view/queryItemAgreeToleranceIndex");
    }

    @GetMapping("/queryItemToleranceAcceptIndex.html")
    @ApiOperation("菜单-容缺受理件")
    public ModelAndView itemToleranceAcceptIndex() {
        return new ModelAndView("view/queryItemToleranceAcceptIndex");
    }

    @GetMapping("/queryUnCompleteFlowIndex.html")
    @ApiOperation("菜单-未完成流程")
    public ModelAndView queryUnCompleteFlowIndex() {
        return new ModelAndView("view/queryUnCompleteFlowIndex");
    }

    @GetMapping("/queryGlobalUnitInfoIndex.html")
    @ApiOperation("全局库-全局单位库")
    public ModelAndView queryGlobalUnitInfoIndex() {
        return new ModelAndView("view/queryGlobalUnitInfoIndex");
    }

    @GetMapping("/queryUnConfirmItemCorrectTasksIndex.html")
    @ApiOperation("菜单-补正待确认件")
    public ModelAndView queryUnConfirmItemCorrectTasksIndex() {
        return new ModelAndView("view/queryUnConfirmItemCorrectTasksIndex");
    }

    @GetMapping("/queryGlobalLinkmanInfoIndex.html")
    @ApiOperation("全局库-全局人员库")
    public ModelAndView queryGlobalLinkmanInfoIndex() {
        return new ModelAndView("view/queryGlobalLinkmanInfoIndex");
    }


    @GetMapping("/querySeriesApplyIndex.html")
    @ApiOperation("菜单-单项申报")
    public ModelAndView querySeriesApplyIndex() {
        return new ModelAndView("view/querySeriesApplyIndex");
    }

    @GetMapping("/queryAgentApplyIndex.html")
    @ApiOperation("菜单-中介事项列表视图")
    public ModelAndView queryAgentApplyIndex() {
        return new ModelAndView("view/queryAgentApplyIndex");
    }

    @GetMapping("/queryMyDraftsIndex.html")
    @ApiOperation("菜单-我的草稿箱")
    public ModelAndView queryMyDraftsIndex() {
        return new ModelAndView("view/queryMyDraftsIndex");
    }

    @GetMapping("/queryWindowDraftsIndex.html")
    @ApiOperation("菜单-窗口草稿箱")
    public ModelAndView queryWindowDraftsIndex() {
        return new ModelAndView("view/queryWindowDraftsIndex");
    }

    @GetMapping("/queryWindowWarnApplyIndex.html")
    @ApiOperation("菜单-窗口预警申报")
    public ModelAndView queryWindowWarnApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "2");
        return modelAndView;
    }

    @GetMapping("/queryWindowOverdueApplyIndex.html")
    @ApiOperation("菜单-窗口逾期申报")
    public ModelAndView queryWindowOverdueApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "3");
        return modelAndView;
    }


    @GetMapping("/queryEntrustWarnPartsIndex.html")
    @ApiOperation("菜单-部门预警办件")
    public ModelAndView queryEntrustWarnPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "2");
        modelAndView.addObject("isInformCommit", "0");
        return modelAndView;
    }

    @GetMapping("/queryEntrustOverduePartsIndex.html")
    @ApiOperation("菜单-部门逾期办件")
    public ModelAndView queryEntrustOverduePartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPartsIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "3");
        modelAndView.addObject("isInformCommit", "0");
        return modelAndView;
    }

    @GetMapping("/queryWindowPartsIndex.html")
    @ApiOperation("菜单-窗口经办办件")
    public ModelAndView queryWindowPartsIndex() {
        return new ModelAndView("view/queryWindowPartsIndex");
    }


    @GetMapping("/queryRegionProjectInfoIndex.html")
    @ApiOperation("菜单-行政区划项目")
    public ModelAndView queryRegionProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", true);
        modelAndView.addObject("onlyOrg", false);
        modelAndView.addObject("handler", false);
        return modelAndView;
    }

    @GetMapping("/queryOrgProjectInfoIndex.html")
    @ApiOperation("菜单-部门经办项目")
    public ModelAndView queryOrgProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", false);
        modelAndView.addObject("onlyOrg", true);
        modelAndView.addObject("handler", false);
        return modelAndView;
    }

    @GetMapping("/queryHandlerProjectInfoIndex.html")
    @ApiOperation("菜单-我经办项目")
    public ModelAndView queryHandlerProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", false);
        modelAndView.addObject("onlyOrg", false);
        modelAndView.addObject("handler", true);
        return modelAndView;
    }

    @GetMapping("/querySublineApply/index.html")
    @ApiOperation("菜单-辅线申报")
    public ModelAndView querySublineApply() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "");
        return modelAndView;
    }

    @GetMapping("/querySublineApply/51/index.html")
    @ApiOperation("菜单-多评合一申报")
    public ModelAndView querySublineApply51Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "51");
        return modelAndView;
    }

    @GetMapping("/querySublineApplyIndex/52/index.html")
    @ApiOperation("菜单-方案联审申报")
    public ModelAndView querySublineApply52Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "52");
        return modelAndView;
    }

    @GetMapping("/querySublineApplyIndex/53/index.html")
    @ApiOperation("菜单-联合审图申报")
    public ModelAndView querySublineApply53Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "53");
        return modelAndView;
    }

    @GetMapping("/querySublineApplyIndex/54C/index.html")
    @ApiOperation("菜单-联合测绘申报")
    public ModelAndView querySublineApply54CIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "54C");
        return modelAndView;
    }

    @GetMapping("/querySublineApplyIndex/54Y/index.html")
    @ApiOperation("菜单-联合验收申报")
    public ModelAndView querySublineApply54YIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySublineApplyIndex");
        modelAndView.addObject("sublineType", "54Y");
        return modelAndView;
    }

    @GetMapping("/querySublineParts/index.html")
    @ApiOperation("菜单-辅线办件")
    public ModelAndView querySublineParts() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "");
        return modelAndView;
    }

    @GetMapping("/querySublineParts/51/index.html")
    @ApiOperation("菜单-多评合一办件")
    public ModelAndView querySublineParts51Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "51");
        return modelAndView;
    }

    @GetMapping("/querySublinePartsIndex/52/index.html")
    @ApiOperation("菜单-方案联审办件")
    public ModelAndView querySublineParts52Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "52");
        return modelAndView;
    }

    @GetMapping("/querySublinePartsIndex/53/index.html")
    @ApiOperation("菜单-联合审图办件")
    public ModelAndView querySublineParts53Index() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "53");
        return modelAndView;
    }

    @GetMapping("/querySublinePartsIndex/54C/index.html")
    @ApiOperation("菜单-联合测绘办件")
    public ModelAndView querySublineParts54CIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "54C");
        return modelAndView;
    }

    @GetMapping("/querySublinePartsIndex/54Y/index.html")
    @ApiOperation("菜单-联合验收办件")
    public ModelAndView querySublineParts54YIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySublinePartsIndex");
        modelAndView.addObject("sublineType", "54Y");
        return modelAndView;
    }

    @GetMapping("/queryWindowItemOutScopeIndex.html")
    @ApiOperation("菜单-不予受理办件(窗口)")
    public ModelAndView queryWindowItemOutScopeIndex() {
        return new ModelAndView("view/queryWindowItemOutScopeIndex");
    }

    @GetMapping("/queryStageConcludedApplyInfo/{stageIndex}/index.html")
    @ApiOperation("菜单-阶段已办结的工程项目")
    public ModelAndView queryStageConcludedApplyInfo(@PathVariable("stageIndex") int stageIndex) {
        if (stageIndex <= 0 || stageIndex > 4) {
            throw new InvalidParameterException("地址有误!");
        }
        ModelAndView modelAndView = new ModelAndView("view/queryStageConcludedApplyInfoIndex");
        modelAndView.addObject("stageIndex", stageIndex);
        return modelAndView;
    }


    @GetMapping("/queryUploadServiceResultIndex.html")
    @ApiOperation("菜单-中介事项上传服务结果")
    public ModelAndView queryuUloadServiceResultIndex() {
        return new ModelAndView("view/queryUploadServiceResultIndex");
    }

    @GetMapping("/solicitOpinionIndex.html")
    @ApiOperation("菜单-意见征求")
    public ModelAndView SolicitOpinionIndex() {
        return new ModelAndView("view/solicitOptionIndex.html");
    }

    @GetMapping("/onceConsultIndex.html")
    @ApiOperation("菜单-一次征询列表")
    public ModelAndView onceConsultIndex() {
        return new ModelAndView("view/onceConsultIndex.html");
    }

    @GetMapping("/jointReviewIndex.html")
    @ApiOperation("菜单-联合评审列表")
    public ModelAndView jointReviewIndex() {
        return new ModelAndView("view/jointReviewIndex.html");
    }

    @GetMapping("/departGuideIndex.html")
    @ApiOperation("菜单-联合评审列表")
    public ModelAndView departGuideReviewIndex() {
        return new ModelAndView("view/departGuideIndex.html");
    }

    @GetMapping("/queryAllAgencyDoTasksIndex.html")
    @ApiOperation("菜单-项目代办-所有")
    public ModelAndView queryAllAgencyDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgencyDoTasksIndex");
        modelAndView.addObject("viewDataCtrl", "0");
        return modelAndView;
    }

    @GetMapping("/queryWindowAgencyDoTasksIndex.html")
    @ApiOperation("菜单-项目代办-代办中心")
    public ModelAndView queryWindowAgencyDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgencyDoTasksIndex");
        modelAndView.addObject("viewDataCtrl", "1");
        return modelAndView;
    }

    @GetMapping("/queryUserAgencyDoTasksIndex.html")
    @ApiOperation("菜单-项目代办-代办人员")
    public ModelAndView queryUserAgencyDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgencyDoTasksIndex");
        modelAndView.addObject("viewDataCtrl", "2");
        return modelAndView;
    }

    @GetMapping("/dept/guide.html")
    @ApiOperation("菜单-部门辅导")
    public ModelAndView deptGuide() {
        return new ModelAndView("view/departGuideIndex");
    }

}
