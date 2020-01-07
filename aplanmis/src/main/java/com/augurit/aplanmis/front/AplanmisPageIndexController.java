package com.augurit.aplanmis.front;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
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
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 并联申报首页
     *
     * @return
     */
    @GetMapping("/stageApplyIndex")
    @ApiOperation("菜单-并联申报页")
    public ModelAndView stageApplyIndex() {
        return new ModelAndView("apply/index");
    }

    /**
     * 单项申报首页
     *
     * @return
     */
    @GetMapping("/singleApplyIndex/{itemVerId}")
    @ApiOperation("菜单-单项申报页")
    public ModelAndView singleApplyIndex(@PathVariable String itemVerId) {
        ModelAndView modelAndView = new ModelAndView("apply/index-single");
        modelAndView.addObject("itemVerId", itemVerId);
        return modelAndView;
    }

    /*@GetMapping("/singleApplyIndex1/{itemVerId}")
    @ApiOperation("菜单-单项申报页")
    public ModelAndView singleApplyIndex1(@PathVariable String itemVerId) {
        ModelAndView modelAndView = new ModelAndView("apply/singleIndex");
        modelAndView.addObject("itemVerId", itemVerId);
        return modelAndView;
    }*/

    /**
     * 并联申报首页
     *
     * @return
     */
    @GetMapping("/stageApproveIndex")
    @ApiOperation("菜单-并联申报页")
    public ModelAndView stageApproveIndex() {
        return new ModelAndView("approve/index");
    }

    /**
     * 申请表
     *
     * @return
     */
    @GetMapping("/applyForm.html")
    @ApiOperation("iframe申请表")
    public ModelAndView applyForm() {
        return new ModelAndView("approve/applyForm");
    }

    /**
     * 材料附件
     *
     * @return
     */
    @GetMapping("/materialAnnex.html")
    @ApiOperation("iframe材料附件")
    public ModelAndView materialAnnex() {
        return new ModelAndView("approve/materialAnnex");
    }

    /**
     * 审批意见
     *
     * @return
     */
    @GetMapping("/approvalOpinions.html")
    @ApiOperation("iframe审批意见")
    public ModelAndView approvalOpinions() {
        ModelAndView modelAndView = new ModelAndView("approve/approvalOpinions");
        OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        modelAndView.addObject("currentUserName", userinfo.getUserName());
        return modelAndView;
    }

    /**
     * 意见汇总表
     *
     * @return
     */
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
    public ResultForm urlStageOneForm(String masterEntityKey, boolean enableParamItem, String itemId, String projInfoId, HttpServletRequest request, HttpServletResponse response) {
//        String realUrl = request.getContextPath() + "/rest/oneform/common/renderHtmlFormContainer?";
        String realUrl = request.getContextPath() + "/rest/oneform/common/getListForm4StageOneForm?";
        String params = "";
        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(masterEntityKey);
            if (aeaHiApplyinst != null) {
                params += "applyinstId=" + aeaHiApplyinst.getApplyinstId();
                AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(aeaHiApplyinst.getApplyinstId());
                if (aeaHiParStageinst != null) {
                    params += "&stageId=" + aeaHiParStageinst.getStageId();
                }

                if (enableParamItem) {
                    params += "&itemids=" + itemId;
                } else {
                    List<AeaHiIteminst> aeaHiIteminstList = null;
                    aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(aeaHiApplyinst.getApplyinstId());
                    for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
                        params += "&itemids=" + aeaHiIteminst.getItemId();
                    }
                }
            }
            if (StringUtils.isNotBlank(projInfoId)) {
                params += "&projInfoId=" + projInfoId;
            }
            params += "&showBasicButton=false";
            params += "&includePlatformResource=false";
            realUrl += params;
            return new ContentResultForm(true, realUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "一张表单参数转换失败！");
        }
    }


    /**
     * 特殊程序发起窗口
     *
     * @return
     */
    @GetMapping("/specialProcedures.html")
    @ApiOperation("特殊程序发起窗口")
    public ModelAndView specialProcedures() {
        return new ModelAndView("approve/specialProcedures");
    }

    /**
     * 所有办件查询（查看所有）
     *
     * @return
     */
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

    /**
     * 承诺制办件查询（查看所有）
     *
     * @return
     */
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

    /**
     * 所有办件查询（查看自己办过的件）
     *
     * @return
     */
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

    /**
     * 所有办件查询（查看当前委办局办过的件）
     *
     * @return
     */
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

    /**
     * 申报查询（查看所在综合窗口的数据）
     *
     * @return
     */
    @GetMapping("/queryWindowApplyIndex.html")
    @ApiOperation("菜单-综窗申报")
    public ModelAndView queryWindowApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", true);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    /**
     * 申报查询（查看当前用户经办的记录）
     *
     * @return
     */
    @GetMapping("/queryHandlerApplyIndex.html")
    @ApiOperation("菜单-经办申报")
    public ModelAndView queryHandlerApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", true);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    /**
     * 申报查询（查看所有）
     *
     * @return
     */
    @GetMapping("/queryApplyIndex.html")
    @ApiOperation("菜单-所有申报")
    public ModelAndView applyInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryApplyIndex");
        modelAndView.addObject("entrust", false);
        modelAndView.addObject("handler", false);
        modelAndView.addObject("defaultInstState", "");
        return modelAndView;
    }

    /**
     * 待办任务
     *
     * @return
     */
    @GetMapping("/queryWaitDoTasksIndex.html")
    @ApiOperation("菜单-待办任务")
    public ModelAndView queryWaitDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitDoTasksIndex");
        return modelAndView;
    }

    /**
     * 窗口撤件待办件
     *
     * @return
     */
    @GetMapping("/queryWaitCancelTasksByCkIndex.html")
    @ApiOperation("菜单-撤件待办件")
    public ModelAndView queryWaitCancelTasksIndexByCk() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByCkIndex");
        modelAndView.addObject("state", 0);
        return modelAndView;
    }

    /**
     * 窗口撤件经办件
     *
     * @return
     */
    @GetMapping("/queryDoneCancelTasksByCkIndex.html")
    @ApiOperation("菜单-撤件经办件")
    public ModelAndView queryDoneCancelTasksIndexByCk() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByCkIndex");
        modelAndView.addObject("state", 1);
        return modelAndView;
    }

    /**
     * 部门撤件经办件
     *
     * @return
     */
    @GetMapping("/queryDoneCancelTasksByBmIndex.html")
    @ApiOperation("菜单-撤件经办件")
    public ModelAndView queryDoneCancelTasksIndexByBm() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByBmIndex");
        modelAndView.addObject("state", 1);
        return modelAndView;
    }

    /**
     * 部门撤件待办件
     *
     * @return
     */
    @GetMapping("/queryWaitCancelTasksByBmIndex.html")
    @ApiOperation("菜单-撤件待办件")
    public ModelAndView queryWaitCancelTasksByBmIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryWaitCancelTasksByBmIndex");
        modelAndView.addObject("state", 0);
        return modelAndView;
    }

    /**
     * 已办任务
     *
     * @return
     */
    @GetMapping("/queryDoneTasksIndex.html")
    @ApiOperation("菜单-已办任务")
    public ModelAndView queryDoneTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryDoneTasksIndex");
        return modelAndView;
    }

    /**
     * 办结任务
     *
     * @return
     */
    @GetMapping("/queryConcludedTasksIndex.html")
    @ApiOperation("菜单-办结任务")
    public ModelAndView queryFinishTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryConcludedTasksIndex");
        return modelAndView;
    }

    /**
     * 网上待预审
     *
     * @return
     */
    @GetMapping("/queryPreliminaryTasksIndex.html")
    @ApiOperation("菜单-网上待预审")
    public ModelAndView queryPreliminaryTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPreliminaryTasksIndex");
        return modelAndView;
    }

    /**
     * 不予受理申报
     *
     * @return
     */
    @GetMapping("/queryDismissedApplyIndex.html")
    @ApiOperation("菜单-不予受理申报")
    public ModelAndView queryDismissedApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryDismissedApplyIndex");
        return modelAndView;
    }

    /**
     * 待补正办件
     *
     * @return
     */
    @GetMapping("/queryNeedCorrectTasksIndex.html")
    @ApiOperation("菜单-待补正办件")
    public ModelAndView queryNeedCorrectTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryNeedCorrectTasksIndex");
        return modelAndView;
    }


    /**
     * 待补全申报
     *
     * @return
     */
    @GetMapping("/queryNeedCompletedApplyIndex.html")
    @ApiOperation("菜单-待补全申报")
    public ModelAndView queryNeedCorrectApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryNeedCompletedApplyIndex");
        return modelAndView;
    }

    /**
     * 补全待确认申报
     *
     * @return
     */
    @GetMapping("/queryNeedConfirmCompletedApplyIndex.html")
    @ApiOperation("菜单-补全待确认申报")
    public ModelAndView queryNeedConfirmCompletedApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryNeedConfirmCompletedApplyIndex");
        return modelAndView;
    }

    /**
     * 取件登记
     *
     * @return
     */
    @GetMapping("/queryPickupCheck.html")
    @ApiOperation("菜单-取件登记")
    public ModelAndView queryPickupCheckIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPickupCheck");
        return modelAndView;
    }

    /**
     * 取件登记-窗口取件
     * 唐山需求
     *
     * @return
     */
    @GetMapping("/queryPickupCheckWin.html")
    @ApiOperation("菜单-取件登记")
    public ModelAndView queryPickupCheckWinIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPickupCheckWin");
        return modelAndView;
    }

    /**
     * 取件登记-快递取件
     * 唐山需求
     *
     * @return
     */
    @GetMapping("/queryPickupCheckExpress.html")
    @ApiOperation("菜单-取件登记")
    public ModelAndView queryPickupCheckExpressIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPickupCheckExpress");
        return modelAndView;
    }

    /**
     * 取件登记-已取件
     *
     * @return
     */
    @GetMapping("/queryPickupCheckFinish.html")
    @ApiOperation("菜单-取件登记-已取件")
    public ModelAndView queryPickupCheckFinishIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryPickupCheckFinish");
        return modelAndView;
    }

    /**
     * 网上已预审
     *
     * @return
     */
    @GetMapping("/queryDoneliminaryTasksIndex.html")
    @ApiOperation("菜单-网上已预审")
    public ModelAndView queryDoneliminaryTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryDoneliminaryTasksIndex");
        return modelAndView;
    }

    /**
     * 特殊程序件
     *
     * @return
     */
    @GetMapping("/querySpecialProcedureTasksIndex.html")
    @ApiOperation("菜单-特殊程序件")
    public ModelAndView querySpecialProcedureTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySpecialProcedureTasksIndex");
        return modelAndView;
    }


    /**
     * 全局项目库
     *
     * @return
     */
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

        ModelAndView modelAndView = new ModelAndView("view/queryItemDisgreeIndex");
        return modelAndView;
    }

    @GetMapping("/queryItemOutScopeIndex.html")
    @ApiOperation("菜单-不予受理(部门)")
    public ModelAndView itemOutScopeIndex() {

        ModelAndView modelAndView = new ModelAndView("view/queryItemOutScopeIndex");
        return modelAndView;
    }

    @GetMapping("/queryItemAgreeToleranceIndex.html")
    @ApiOperation("菜单-容缺通过件")
    public ModelAndView itemAgreeToleranceIndex() {

        ModelAndView modelAndView = new ModelAndView("view/queryItemAgreeToleranceIndex");
        return modelAndView;
    }

    @GetMapping("/queryItemToleranceAcceptIndex.html")
    @ApiOperation("菜单-容缺受理件")
    public ModelAndView itemToleranceAcceptIndex() {

        ModelAndView modelAndView = new ModelAndView("view/queryItemToleranceAcceptIndex");
        return modelAndView;
    }

    @GetMapping("/queryUnCompleteFlowIndex.html")
    @ApiOperation("菜单-未完成流程")
    public ModelAndView queryUnCompleteFlowIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryUnCompleteFlowIndex");
        return modelAndView;
    }

    @GetMapping("/queryGlobalUnitInfoIndex.html")
    @ApiOperation("全局库-全局单位库")
    public ModelAndView queryGlobalUnitInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalUnitInfoIndex");
        return modelAndView;
    }

    @GetMapping("/queryUnConfirmItemCorrectTasksIndex.html")
    @ApiOperation("菜单-补正待确认件")
    public ModelAndView queryUnConfirmItemCorrectTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryUnConfirmItemCorrectTasksIndex");
        return modelAndView;
    }

    @GetMapping("/queryGlobalLinkmanInfoIndex.html")
    @ApiOperation("全局库-全局人员库")
    public ModelAndView queryGlobalLinkmanInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalLinkmanInfoIndex");
        return modelAndView;
    }


    @GetMapping("/querySeriesApplyIndex.html")
    @ApiOperation("菜单-单项申报")
    public ModelAndView querySeriesApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/querySeriesApplyIndex");
        return modelAndView;
    }

    @GetMapping("/queryAgentApplyIndex.html")
    @ApiOperation("菜单-中介事项列表视图")
    public ModelAndView queryAgentApplyIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgentApplyIndex");
        return modelAndView;
    }

    @GetMapping("/queryMyDraftsIndex.html")
    @ApiOperation("菜单-我的草稿箱")
    public ModelAndView queryMyDraftsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryMyDraftsIndex");
        return modelAndView;
    }

    @GetMapping("/queryWindowDraftsIndex.html")
    @ApiOperation("菜单-窗口草稿箱")
    public ModelAndView queryWindowDraftsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryWindowDraftsIndex");
        return modelAndView;
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

    /**
     * 窗口经办办件
     *
     * @return
     */
    @GetMapping("/queryWindowPartsIndex.html")
    @ApiOperation("菜单-窗口经办办件")
    public ModelAndView queryWindowPartsIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryWindowPartsIndex");
        return modelAndView;
    }


    /**
     * 行政区划项目
     *
     * @return
     */
    @GetMapping("/queryRegionProjectInfoIndex.html")
    @ApiOperation("菜单-行政区划项目")
    public ModelAndView queryRegionProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", true);
        modelAndView.addObject("onlyOrg", false);
        modelAndView.addObject("handler", false);
        return modelAndView;
    }

    /**
     * 部门经办项目
     *
     * @return
     */
    @GetMapping("/queryOrgProjectInfoIndex.html")
    @ApiOperation("菜单-部门经办项目")
    public ModelAndView queryOrgProjectInfoIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryGlobalProjectInfoIndex");
        modelAndView.addObject("onlyRegion", false);
        modelAndView.addObject("onlyOrg", true);
        modelAndView.addObject("handler", false);
        return modelAndView;
    }

    /**
     * 我经办项目库
     *
     * @return
     */
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
        ModelAndView modelAndView = new ModelAndView("view/queryWindowItemOutScopeIndex");
        return modelAndView;
    }

    /**
     * 阶段已办结的工程项目
     *
     * @return
     */
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


    /**
     * 菜单-上传服务结果
     * 中介超市服务结果上传列表页
     *
     * @return
     */
    @GetMapping("/queryUploadServiceResultIndex.html")
    @ApiOperation("菜单-中介事项上传服务结果")
    public ModelAndView queryuUloadServiceResultIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryUploadServiceResultIndex");
        return modelAndView;
    }

    /**
     * 菜单-意见征求列表
     *
     * @return
     */
    @GetMapping("/solicitOpinionIndex.html")
    @ApiOperation("菜单-意见征求")
    public ModelAndView SolicitOpinionIndex() {
        ModelAndView modelAndView = new ModelAndView("view/solicitOptionIndex.html");
        return modelAndView;
    }

    /**
     * 菜单-一次征询列表
     *
     * @return onceConsultIndex.html
     */
    @GetMapping("/onceConsultIndex.html")
    @ApiOperation("菜单-一次征询列表")
    public ModelAndView onceConsultIndex() {
        ModelAndView modelAndView = new ModelAndView("view/onceConsultIndex.html");
        return modelAndView;
    }

    /**
     * 菜单-联合评审列表
     *
     * @return jointReviewIndex.html
     */
    @GetMapping("/jointReviewIndex.html")
    @ApiOperation("菜单-联合评审列表")
    public ModelAndView jointReviewIndex() {
        ModelAndView modelAndView = new ModelAndView("view/jointReviewIndex.html");
        return modelAndView;
    }

    /**
     * 菜单-部门辅导列表
     *
     * @return departGuideIndex.html
     */
    @GetMapping("/departGuideIndex.html")
    @ApiOperation("菜单-联合评审列表")
    public ModelAndView departGuideReviewIndex() {
        ModelAndView modelAndView = new ModelAndView("view/departGuideIndex.html");
        return modelAndView;
    }

    /**
     * 项目代办
     * @return
     */
    @GetMapping("/queryAllAgencyDoTasksIndex.html")
    @ApiOperation("菜单-项目代办-所有")
    public ModelAndView queryAllAgencyDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgencyDoTasksIndex");
        modelAndView.addObject("viewDataCtrl", "0");
        return modelAndView;
    }
    /**
     * 项目代办
     * @return
     */
    @GetMapping("/queryWindowAgencyDoTasksIndex.html")
    @ApiOperation("菜单-项目代办-代办中心")
    public ModelAndView queryWindowAgencyDoTasksIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryAgencyDoTasksIndex");
        modelAndView.addObject("viewDataCtrl", "1");
        return modelAndView;
    }
    /**
     * 项目代办
     * @return
     */
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
