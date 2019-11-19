package com.augurit.aplanmis.front.queryView.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.BpmDestTaskConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendConfig;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.handler.BaseEnum;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import com.augurit.aplanmis.common.utils.ExcelUtils;
import com.augurit.aplanmis.common.vo.conditional.ApplyInfo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.common.vo.conditional.PartsInfo;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import com.augurit.aplanmis.front.queryView.service.ConditionalJumpService;
import com.augurit.aplanmis.front.queryView.vo.ConditionalQueryDic;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tiantian
 * @date 2019/9/4
 */
@Service
@Transactional
public class ConditionalJumpServiceImpl implements ConditionalJumpService {

    @Autowired
    private AplanmisOpuOmOrgAdminService opuOmOrgService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private ConditionalJumpMapper conditionalJumpMapper;

    @Autowired
    private AeaServiceWindowUserMapper aeaServiceWindowUserMapper;

    @Autowired
    private BpmTaskService bpmTaskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    private static String waitDoViewId;

    private static String doneViewId;

    private static String concludedViewId;

    private static String partsViewId;


    @Override
    public ConditionalQueryDic applyConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());

        if (topOrg != null) {

            String orgId = topOrg.getOrgId();

            loadBaseConditionalQueryDic(conditionalQueryDic, orgId);

            //行政分区
            List<BscDicCodeItem> regionList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_XZFQ, orgId);
            conditionalQueryDic.setRegionList(regionList);

            //立项类型
            List<BscDicCodeItem> projTypeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, orgId);
            conditionalQueryDic.setProjTypeList(projTypeList);

            //投资类型
            List<BscDicCodeItem> investCategoryList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_TZLX, orgId);
            conditionalQueryDic.setInvestCategoryList(investCategoryList);

            //建设性质
            List<BscDicCodeItem> buildNatureList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, orgId);
            conditionalQueryDic.setBuildNatureList(buildNatureList);

            //项目级别
            List<BscDicCodeItem> projLevelList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, orgId);
            conditionalQueryDic.setProjLevelList(projLevelList);
        }
        return conditionalQueryDic;
    }


    @Override
    public ConditionalQueryDic parsConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());

        if (topOrg != null) {

            String orgId = topOrg.getOrgId();

            loadBaseConditionalQueryDic(conditionalQueryDic, orgId);

            //事项状态
            List<BscDicCodeItem> iteminstStateList = getBscDicCodeItemList(ItemStatus.values());
            conditionalQueryDic.setIteminstStateList(iteminstStateList);

        }
        return conditionalQueryDic;
    }

    @Override
    public ConditionalQueryDic taskConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());

        if (topOrg != null) {

            String orgId = topOrg.getOrgId();

            loadBaseConditionalQueryDic(conditionalQueryDic, orgId);

        }
        return conditionalQueryDic;
    }


    private void loadBaseConditionalQueryDic(ConditionalQueryDic conditionalQueryDic, String orgId) {
        //主题
        AeaParTheme aeaParTheme = new AeaParTheme();
        aeaParTheme.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaParTheme> parThemeList = aeaParThemeMapper.getTestRunOrPublishedVerAeaParTheme(null, orgId);
        conditionalQueryDic.setThemeList(parThemeList);

        //申报来源
        List<BscDicCodeItem> applySourceList = getBscDicCodeItemList(ApplySource.values());
        conditionalQueryDic.setApplySourceList(applySourceList);

        //申报类型
        List<BscDicCodeItem> applyTypeList = getBscDicCodeItemList(ApplyType.values());
        conditionalQueryDic.setApplyTypeList(applyTypeList);

        //申报状态
        List<BscDicCodeItem> applyStateList = getBscDicCodeItemList(ApplyState.values());
        conditionalQueryDic.setApplyStateList(applyStateList);

        //单位类型
        List<BscDicCodeItem> unitTypeList = getBscDicCodeItemList(UnitType.values());
        conditionalQueryDic.setUnitTypeList(unitTypeList);

        //业务状态
        List<BscDicCodeItem> instStateList = getBscDicCodeItemList(TimeruleInstState.values());
        conditionalQueryDic.setInstStateList(instStateList);
    }

    private List<BscDicCodeItem> getBscDicCodeItemList(BaseEnum[] baseEnums) {
        List<BscDicCodeItem> bscDicCodeItemList = new ArrayList<>();
        for (BaseEnum baseEnum : baseEnums) {
            BscDicCodeItem instState = new BscDicCodeItem();
            instState.setItemName(baseEnum.getName());
            instState.setItemCode(baseEnum.getValue().toString());
            instState.setItemId(baseEnum.getValue().toString());
            bscDicCodeItemList.add(instState);
        }
        return bscDicCodeItemList;
    }

    @Override
    public List<AeaParStage> getApprovalStageByThemeId(String themeId) {
        if (StringUtils.isNotBlank(themeId)) {
            AeaParThemeVer themeVer = new AeaParThemeVer();
            themeVer.setThemeId(themeId);
            themeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
            themeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaParThemeVer> themeVerList = aeaParThemeVerMapper.listAeaParThemeVer(themeVer);
            if (themeVerList != null && themeVerList.size() > 0) {
                AeaParStage stage = new AeaParStage();
                stage.setThemeVerId(Collections.max(themeVerList, Comparator.comparing(AeaParThemeVer::getVerNum)).getThemeVerId());
                stage.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                return aeaParStageMapper.listAeaParStage(stage);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public PageInfo listApplyinfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        loadConditionalQueryRequest(conditionalQueryRequest, true);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalJumpMapper.listApplyinfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    private Page changeOrderBySql(Page page) {

        if (page != null && StringUtils.isNotBlank(page.getOrderBy())) {
            String[] sqls = page.getOrderBy().split(" ");
            if (sqls != null && sqls.length == 2) {
                String fields = sqls[0];
                String orderBy = sqls[1];

                if (fields.contains(",")) {
                    String replaceSql = " " + orderBy + ",";
                    page.setOrderBy(page.getOrderBy().replaceAll(",", replaceSql));
                }
            }
        }

        return page;
    }

    private void loadApplyinfo(List<ApplyInfo> applyInfoList) {

        if (applyInfoList != null && applyInfoList.size() > 0) {

            //申报状态
            List<BscDicCodeItem> applyStateList = getBscDicCodeItemList(ApplyState.values());

            for (ApplyInfo applyInfo : applyInfoList) {

                if (ApplySource.NET.getValue().equals(applyInfo.getApplySource())) {
                    applyInfo.setApplySource("网厅");
                } else if (ApplySource.WIN.getValue().equals(applyInfo.getApplySource())) {
                    applyInfo.setApplySource("窗口");
                }

                for (BscDicCodeItem bscDicCodeItem : applyStateList) {
                    if (bscDicCodeItem.getItemCode().equals(applyInfo.getApplyState())) {
                        applyInfo.setApplyState(bscDicCodeItem.getItemName());
                        break;
                    }
                }

                applyInfo.setRemainingOrOverTimeText(loadRemainingOrOverTimeText(applyInfo.getInstState(), applyInfo.getTimeruleUnit(), applyInfo.getRemainingTime(), applyInfo.getOverdueTime()));

                applyInfo.setDueNumText("-天");

                applyInfo.setDueNumText(loadDueNumText(applyInfo.getDueNumText(), applyInfo.getTimeruleUnit(), applyInfo.getDueNum()));

            }
        }
    }

    private String getRemainingOrOverTimeText(String instState, String timeruleUnit, Double remainingTime, Double overdueTime) {
        Double time = remainingTime;
        if (TimeruleInstState.OVERDUE.getValue().equals(instState)) {
            time = overdueTime;
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return sb.toString();
        }

        return null;
    }

    @Override
    public void exportApplyinfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        loadConditionalQueryRequest(conditionalQueryRequest, true);

        List<ApplyInfo> applyInfoList = conditionalJumpMapper.listApplyinfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        for (ApplyInfo applyInfo : applyInfoList) {
            TimeruleInstState timeruleInstState = TimeruleInstState.getTimeruleInstState(applyInfo.getInstState());
            if (timeruleInstState != null) {
                applyInfo.setInstState(timeruleInstState.getName());
            }
        }

        ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
        excelParam.setDataList(applyInfoList);
        excelParam.setFileName("申报数据导出.xls");
        excelParam.setSheetName("申报数据");
        excelParam.setTitleName("申报数据");
        List<Integer> columnWidthList = Arrays.asList(2500, 3000, 6000, 6500, 3000, 7000, 7000, 6000, 3000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("类型", "申报来源", "申请编号", "项目名称", "联系人", "阶段名称", "事项名称", "所属主题", "申报状态", "业务状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applyType", "applySource", "applyinstCode", "projName", "linkmanName", "stageName", "itemName", "themeName", "applyState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    private ConditionalQueryRequest loadConditionalQueryRequest(ConditionalQueryRequest conditionalQueryRequest, boolean isApply) {
//        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());

        if (conditionalQueryRequest.isHandler()) {
            String loginName = SecurityContext.getOpusLoginUser().getUser().getLoginName();
            conditionalQueryRequest.setLoginName(loginName);
            conditionalQueryRequest.setUserId(SecurityContext.getCurrentUserId());
        }

        if (isApply && StringUtils.isNotBlank(conditionalQueryRequest.getIndustry())) {
            conditionalQueryRequest.setIndustries(conditionalQueryRequest.getIndustry().trim().split(","));
        }

        if (conditionalQueryRequest.isEntrust()) {

            if (isApply) {
                List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
                if (list.size() == 0) {
                    throw new RuntimeException("当前用户没有绑定所在服务窗口");
                }
                //多个窗口取第一个
                conditionalQueryRequest.setWindowId(list.get(0).getWindowId());
            }

            conditionalQueryRequest.setCurrentOrgId(getCurrentOrgId());
        }

        return conditionalQueryRequest;
    }

    @Override
    public PageInfo listPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<PartsInfo> partsList = conditionalJumpMapper.listPartsInfo(conditionalQueryRequest);

        loadPartsinfo(partsList);

        return new PageInfo<>(partsList);
    }


    private void loadPartsinfo(List<PartsInfo> partsList) {
        if (partsList != null && partsList.size() > 0) {

            //事项状态
            List<BscDicCodeItem> iteminstStateList = getBscDicCodeItemList(ItemStatus.values());

            for (PartsInfo partsInfo : partsList) {
                for (BscDicCodeItem bscDicCodeItem : iteminstStateList) {
                    if (bscDicCodeItem.getItemCode().equals(partsInfo.getIteminstState())) {
                        partsInfo.setIteminstState(bscDicCodeItem.getItemName());
                        break;
                    }
                }


                if (ApplySource.NET.getValue().equals(partsInfo.getApplySource())) {
                    partsInfo.setApplySource("网厅");
                } else if (ApplySource.WIN.getValue().equals(partsInfo.getApplySource())) {
                    partsInfo.setApplySource("窗口");
                }

                partsInfo.setRemainingOrOverTimeText(loadRemainingOrOverTimeText(partsInfo.getInstState(), partsInfo.getTimeruleUnit(), partsInfo.getRemainingTime(), partsInfo.getOverdueTime()));

                partsInfo.setDueNumText("-天");

                partsInfo.setDueNumText(loadDueNumText(partsInfo.getDueNumText(), partsInfo.getTimeruleUnit(), partsInfo.getDueNum()));


            }
        }
    }

    @Override
    public void exportPartsInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        List<PartsInfo> partsList = conditionalJumpMapper.listPartsInfo(conditionalQueryRequest);

        loadPartsinfo(partsList);

        for (PartsInfo partsInfo : partsList) {
            TimeruleInstState timeruleInstState = TimeruleInstState.getTimeruleInstState(partsInfo.getInstState());
            if (timeruleInstState != null) {
                partsInfo.setInstState(timeruleInstState.getName());
            }
        }

        ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
        excelParam.setDataList(partsList);
        excelParam.setFileName("办件数据导出.xls");
        excelParam.setSheetName("办件数据");
        excelParam.setTitleName("办件数据");
        List<Integer> columnWidthList = Arrays.asList(2500, 3000, 6000, 6500, 7000, 7000, 3000, 5000, 4000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("类型", "申报来源", "申报流水号", "项目名称", "事项名称", "阶段名称", "申请人", "所属主题", "申报状态", "业务状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applyType", "applySource", "applyinstCode", "projName", "itemName", "stageName", "linkmanName", "themeName", "iteminstState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    @Override
    public List<BscDicCodeItem> listIndustryTree() {
        List<BscDicCodeItem> nodeList = new ArrayList<>();
        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());

        if (topOrg != null) {
            String orgId = topOrg.getOrgId();
            BscDicCodeType bscDicCodeType = bscDicCodeService.getTypeByTypeCode(DicConstants.XM_GBHY, orgId);

            if (bscDicCodeType != null) {
                return bscDicCodeService.getItemTreeByTypeId(bscDicCodeType.getTypeId());
            }

        }

        return nodeList;

    }

    @Override
    public String queryApplyInfoTaskId(String applyinstId) {
        List<String> taskIdList = conditionalJumpMapper.queryApplyInfoTaskId(applyinstId);

        if (taskIdList != null && taskIdList.size() > 0) {
            return taskIdList.get(0);
        }

        return null;
    }

    @Override
    public String queryItemInfoTaskId(String iteminstId, boolean handler) {
        String loginName = null;
        if (handler) {
            loginName = SecurityContext.getOpusLoginUser().getUser().getLoginName();
        }
        List<String> taskIdList = conditionalJumpMapper.queryItemInfoTaskId(iteminstId, loginName);

        if (taskIdList != null && taskIdList.size() > 0) {
            return taskIdList.get(0);
        }

        return null;
    }

    @Override
    public String queryViewId(String keyword) {
        List<String> viewIdList = conditionalJumpMapper.queryViewId(keyword);

        if (viewIdList != null && viewIdList.size() > 0) {
            return viewIdList.get(0);
        }

        return null;
    }

    @Override
    public PageInfo listWaitDoTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listWaitDoTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待办");

        return new PageInfo<>(taskList);
    }

    private void loadTaskInfo(List<TaskInfo> taskList, String viewName) {
        if (taskList != null && taskList.size() > 0) {

            String viewId = queryViewId(viewName);

            if (StringUtils.isBlank(viewId)) {
                viewId = queryViewId("所有办件");
            }

            for (TaskInfo taskInfo : taskList) {

                taskInfo.setViewId(viewId);

                if (ApplySource.NET.getValue().equals(taskInfo.getApplySource())) {
                    taskInfo.setApplySource("网厅");
                } else if (ApplySource.WIN.getValue().equals(taskInfo.getApplySource())) {
                    taskInfo.setApplySource("窗口");
                }

                taskInfo.setRemainingOrOverTimeText(loadRemainingOrOverTimeText(taskInfo.getInstState(), taskInfo.getTimeruleUnit(), taskInfo.getRemainingTime(), taskInfo.getOverdueTime()));

                taskInfo.setDueNumText("-天");
                taskInfo.setUseLimitTimeText("-天");

                taskInfo.setDueNumText(loadDueNumText(taskInfo.getDueNumText(), taskInfo.getTimeruleUnit(), taskInfo.getDueNum()));

                if (taskInfo.getUseLimitTime() != null) {
                    loadUseLimitTimeText(taskInfo);
                }
            }

        }
    }

    private void loadUseLimitTimeText(TaskInfo taskInfo) {
        if (StringUtils.isBlank(taskInfo.getTimeruleUnit())) {
            return;
        }

        String timeruleUnit = taskInfo.getTimeruleUnit();

        Double time = taskInfo.getUseLimitTime();
        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            taskInfo.setUseLimitTimeText(sb.toString());
        }
    }

    private String loadDueNumText(String dueNumText, String timeruleUnit, Double time) {
        if (StringUtils.isBlank(timeruleUnit)) {
            timeruleUnit = TimeruleUnit.WD.getValue();
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return sb.toString();
        }

        return dueNumText;

    }


    /**
     * 计算要显示的剩余/逾期时间
     *
     * @param instState
     * @param timeruleUnit
     * @param remainingTime
     * @param overdueTime
     * @return
     */
    private String loadRemainingOrOverTimeText(String instState, String timeruleUnit, Double remainingTime, Double overdueTime) {
        Double time = remainingTime;
        if (TimeruleInstState.OVERDUE.getValue().equals(instState)) {
            time = overdueTime;
        }

        if (time != null) {
            StringBuffer sb = new StringBuffer();
            if (time > time.intValue()) {
                sb.append(BigDecimal.valueOf(time).setScale(1, BigDecimal.ROUND_HALF_UP));
            } else {
                sb.append(time.intValue());
            }
            if (TimeruleUnit.isDayUnit(timeruleUnit)) {
                sb.append("天");
            } else if (TimeruleUnit.isHourUnit(timeruleUnit)) {
                sb.append("小时");
            }

            return (TimeruleInstState.OVERDUE.getValue().equals(instState) ? "逾期" : "剩余") + sb.toString();
        }

        return null;
    }


    @Override
    public PageInfo listDoneTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listDoneTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "已办");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listConcludedTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listConcludedTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "办结");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listPreliminaryTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待预审");

        return new PageInfo<>(taskList);
    }

    private ConditionalQueryRequest getWindowUsers(ConditionalQueryRequest conditionalQueryRequest) {
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());

        conditionalQueryRequest.setEntrust(true);
        String loginName = SecurityContext.getOpusLoginUser().getUser().getLoginName();
        conditionalQueryRequest.setLoginName(loginName);
        conditionalQueryRequest.setUserId(SecurityContext.getCurrentUserId());

        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
        if (list.size() == 0) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        //多个窗口取第一个
        String windowId = list.get(0).getWindowId();
        conditionalQueryRequest.setWindowId(windowId);

        List<OpuOmUser> opuOmUsers = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, null, "1");

        if (opuOmUsers != null && opuOmUsers.size() > 0) {
            List<String> userList = new ArrayList<>();
            for (OpuOmUser opuOmUser : opuOmUsers) {
                userList.add(opuOmUser.getLoginName());
            }

            conditionalQueryRequest.setUserList(userList);
        } else {
            throw new RuntimeException("查询不到所在窗口的用户信息");
        }


        return conditionalQueryRequest;
    }


    @Override
    public PageInfo listDismissedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listDismissedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "不予受理");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedCorrectTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
//        getWindowUsers(conditionalQueryRequest);
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" correctDueTime desc" );
        }

        changeOrderBySql(page);

        if(conditionalQueryRequest.isEntrust()){
            String currentOrgId = getCurrentOrgId();
            conditionalQueryRequest.setCurrentOrgId(currentOrgId);
        }

        PageHelper.startPage(page);
        List<TaskInfo> taskList = conditionalJumpMapper.listNeedCorrectTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待补正");

        return new PageInfo<>(taskList);

    }

    private String getCurrentOrgId() {
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
        if (!opuOmOrgs.isEmpty()) {
            OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
            return opuOmOrg.getOrgId();
        }

        return null;
    }

    @Override
    public PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listNeedCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "待补全申报");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedConfirmCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listNeedConfirmCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "补全待确认");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listPickupCheckTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listPickupCheckTasksByPage(conditionalQueryRequest);

        loadTaskInfo(taskList, "取件登记");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listPickupCheckTasksByPageFinish(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listPickupCheckTasksFinishByPage(conditionalQueryRequest);

        loadTaskInfo(taskList, "已取件");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listDoneliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listDoneliminaryTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "已预审");

        return new PageInfo<>(taskList);
    }

    //特别程序件-todo
    @Override
    public PageInfo lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" applyTime desc" );
        }

        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.lisSpecialProcedureTasks(conditionalQueryRequest);
        taskList = taskList.stream().filter(info -> StringUtils.isNotBlank(info.getTaskId())).collect(Collectors.toList());
        loadTaskInfo(taskList, "特殊程序件");

        return new PageInfo<>(taskList);
    }

    @Override
    public List<OpuOmOrg> listNeedCorrectTasksOrganizer() throws Exception {
        String windowId = null;
        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
        if (list.size()!=0) {
            windowId = list.get(0).getWindowId();
        }
        return conditionalJumpMapper.listNeedCorrectTasksOrganizer(windowId);
    }

    /**
     * 办结（不通过）-部门-事项列表
     * todo
     *
     * @param conditionalQueryRequest 查询参数
     * @param page                    分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo listItemDisgree(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" dismissedTime desc" );
        }

        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listItemDisgree(conditionalQueryRequest);

        loadTaskInfo(taskList, "不通过件");

        return new PageInfo<>(taskList);
    }

    /**
     * 不予受理(部门)-事项列表
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     */
    @Override
    public PageInfo listItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" dismissedTime desc" );
        }
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listItemOutScope(conditionalQueryRequest);

        loadTaskInfo(taskList, "不予受理件");

        return new PageInfo<>(taskList);
    }

    /**
     * 容缺受理件-部门
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo listItemToleranceAccept(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" dismissedTime desc" );
        }
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listItemToleranceAccept(conditionalQueryRequest);

        loadTaskInfo(taskList, "容缺受理件");

        return new PageInfo<>(taskList);
    }

    /**
     * 容缺通过件-部门
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo listItemAgreeTolerance(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" dismissedTime desc" );
        }
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listItemAgreeTolerance(conditionalQueryRequest);

        loadTaskInfo(taskList, "容缺通过件");

        return new PageInfo<>(taskList);
    }


    /**
     * 补正待确认件
     *
     * @param conditionalQueryRequest
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo listUnConfirmItemCorrect(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        if(StringUtils.isBlank(page.getOrderBy())){
            page.setOrderBy(" correctDueTime desc" );
        }
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listUnConfirmItemCorrect(conditionalQueryRequest);

        loadTaskInfo(taskList, "补正待确认件");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listUnCompleteFlow(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalJumpMapper.listUnCompleteFlow(conditionalQueryRequest);

        return new PageInfo<>(taskList);
    }

    @Override
    public void doCompleteFlow(String taskId, String desActId) throws Exception {
        BpmTaskSendObject sendObject = new BpmTaskSendObject();
        sendObject.setTaskId(taskId);
        if (StringUtils.isNotBlank(desActId)) {
            BpmTaskSendConfig config = new BpmTaskSendConfig();
            config.setDestActId(desActId);
            List temp = new ArrayList<>();
            temp.add(config);
            sendObject.setSendConfigs(temp);
        }
        bpmTaskService.completeTask(sendObject);
    }

    @Override
    public ResultForm checkDoCompleteFlow(String taskId) throws Exception {
        Task task = bpmTaskService.getTaskByTaskId(taskId);
        if (task != null) {
            ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowByTriggerTaskinstId(taskId);
            if (actStoAppinstSubflow != null) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(actStoAppinstSubflow.getSubflowProcinstId()).singleResult();
                if (processInstance != null) {
                    //子流程未结束
                    return new ResultForm(false, "当前流程还有子流程未执行结束，请先结束子流程！");
                }
            }
            List<BpmDestTaskConfig> list = bpmTaskService.getBpmDestTaskConfigByCurrTaskId(taskId);
            if (list.size() > 0) {
                return new ContentResultForm<>(true, list);
            } else {
                return new ResultForm(false, "当前节点没有可发送的下一环节节点，请检查流程是否有效！");
            }
        } else {
            return new ResultForm(false, "任务实例不存在，请检查！");
        }
    }
}
