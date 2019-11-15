package com.augurit.aplanmis.front.queryView.service.impl;

import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.mapper.*;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.vo.ApplyEventVo;
import com.augurit.aplanmis.common.event.vo.IteminstEventVo;
import com.augurit.aplanmis.common.handler.BaseEnum;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.shortMessage.AplanmisSmsConfigProperties;
import com.augurit.aplanmis.common.utils.ExcelUtils;
import com.augurit.aplanmis.common.vo.conditional.*;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
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
public class ConditionalQueryServiceImpl implements ConditionalQueryService {

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private ConditionalQueryMapper conditionalQueryMapper;

    @Autowired
    private AeaServiceWindowUserMapper aeaServiceWindowUserMapper;

    @Autowired
    private BpmTaskService bpmTaskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;

    @Autowired
    private ActStoRemindReceiverMapper actStoRemindReceiverMapper;

    @Autowired
    private AplanmisSmsConfigProperties smsConfigProperties;

    @Autowired
    AplanmisEventPublisher aplanmisEventPublisher;

    @Autowired
    AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    OpuOmOrgMapper opuOmOrgMapper;


    @Autowired
    private ActStoTimegroupActMapper actStoTimegroupActMapper;

    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Autowired
    private ActStoTimegroupMapper actStoTimegroupMapper;

    @Autowired
    private ActStoAppinstMapper actStoAppinstMapper;

    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Autowired
    private ActStoTimeruleService actStoTimeruleService;

    @Override
    public ConditionalQueryDic applyConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        String orgId = SecurityContext.getCurrentOrgId();

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

        return conditionalQueryDic;
    }


    @Override
    public ConditionalQueryDic parsConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        String orgId = SecurityContext.getCurrentOrgId();

        loadBaseConditionalQueryDic(conditionalQueryDic, orgId);

        //事项状态
        List<BscDicCodeItem> iteminstStateList = getBscDicCodeItemList(ItemStatus.values());
        conditionalQueryDic.setIteminstStateList(iteminstStateList);

        return conditionalQueryDic;
    }

    @Override
    public ConditionalQueryDic taskConditionalQueryDic() {
        ConditionalQueryDic conditionalQueryDic = new ConditionalQueryDic();

        String orgId = SecurityContext.getCurrentOrgId();

        loadBaseConditionalQueryDic(conditionalQueryDic, orgId);

        return conditionalQueryDic;
    }

    /**
     * 通用查询条件数据
     * @param conditionalQueryDic
     * @param orgId
     */
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

        //时限状态
        List<BscDicCodeItem> instStateList = getBscDicCodeItemList(TimeruleInstState.values());
        conditionalQueryDic.setInstStateList(instStateList);
    }

    /**
     * 枚举类转换为数据字典
     * @param baseEnums
     * @return
     */
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

        loadApplyConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    /**
     * 组装申报查询参数
     * @param conditionalQueryRequest
     * @return
     */
    private void loadApplyConditionalQueryRequest(ConditionalQueryRequest conditionalQueryRequest) {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();

        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());

        if (conditionalQueryRequest.isHandler()) {
            String loginName = opusLoginUser.getUser().getLoginName();
            conditionalQueryRequest.setLoginName(loginName);
            conditionalQueryRequest.setUserId(opusLoginUser.getUser().getUserId());
        }

        if (StringUtils.isNotBlank(conditionalQueryRequest.getIndustry())) {
            conditionalQueryRequest.setIndustries(conditionalQueryRequest.getIndustry().trim().split(","));
        }

        if (conditionalQueryRequest.isEntrust()) {
            List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(opusLoginUser.getUser().getUserId());
            if (list.size() == 0) {
                throw new RuntimeException("当前用户没有绑定所在服务窗口");
            }
            conditionalQueryRequest.setWindowId(list.get(0).getWindowId());
        }
    }

    /**
     * 处理多字段排序
     * @param page
     * @return
     */
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

    /**
     * 处理显示数据
     * @param applyInfoList
     */
    private void loadApplyinfo(List<ApplyInfo> applyInfoList) {

        if (applyInfoList != null && applyInfoList.size() > 0) {

            //申报状态
            List<BscDicCodeItem> applyStateList = getBscDicCodeItemList(ApplyState.values());

            for (ApplyInfo applyInfo : applyInfoList) {

                loadApplySource(applyInfo);

                for (BscDicCodeItem bscDicCodeItem : applyStateList) {
                    if (bscDicCodeItem.getItemCode().equals(applyInfo.getApplyState())) {
                        applyInfo.setApplyState(bscDicCodeItem.getItemName());
                        break;
                    }
                }

                loadInstInfo(applyInfo);

            }
        }
    }

    /**
     * 设置申报来源显示
     * @param info
     */
    private void loadApplySource(BaseInfo info){
        if (ApplySource.NET.getValue().equals(info.getApplySource())) {
            info.setApplySource("网厅");
        } else if (ApplySource.WIN.getValue().equals(info.getApplySource())) {
            info.setApplySource("窗口");
        }
    }

    private void loadInstInfo(BaseInfo info){
        loadRemainingOrOverTimeText(info);
        loadDueNumText(info);
    }

    @Override
    public void exportApplyinfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        loadApplyConditionalQueryRequest(conditionalQueryRequest);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listApplyinfo(conditionalQueryRequest);

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
        List<Integer> columnWidthList = Arrays.asList(2500, 3000, 6000, 6500, 7000, 7000, 6000, 3000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("类型", "申报来源", "申请编号", "项目名称", "阶段名称", "事项名称", "所属主题", "申报状态", "时限状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applyType", "applySource", "applyinstCode", "projName", "stageName", "itemName", "themeName", "applyState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    private ConditionalQueryRequest loadConditionalQueryRequest(ConditionalQueryRequest conditionalQueryRequest, boolean isApply) {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();

        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());

        if (conditionalQueryRequest.isHandler()) {
            String loginName = opusLoginUser.getUser().getLoginName();
            conditionalQueryRequest.setLoginName(loginName);
            conditionalQueryRequest.setUserId(opusLoginUser.getUser().getUserId());
        }

        if (isApply && StringUtils.isNotBlank(conditionalQueryRequest.getIndustry())) {
            conditionalQueryRequest.setIndustries(conditionalQueryRequest.getIndustry().trim().split(","));
        }

        if (conditionalQueryRequest.isEntrust()) {

            if (isApply) {
                List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(opusLoginUser.getUser().getUserId());
                if (list.size() == 0) {
                    throw new RuntimeException("当前用户没有绑定所在服务窗口");
                }
                conditionalQueryRequest.setWindowId(list.get(0).getWindowId());
            }
        }

        return conditionalQueryRequest;
    }

    /**
     * 组装查询参数
     * @param conditionalQueryRequest
     */
    private void loadConditionalQueryRequest(ConditionalQueryRequest conditionalQueryRequest) {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();

        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());

        if (conditionalQueryRequest.isHandler()) {
            String loginName = opusLoginUser.getUser().getLoginName();
            conditionalQueryRequest.setLoginName(loginName);
            conditionalQueryRequest.setUserId(opusLoginUser.getUser().getUserId());
        }
    }

    @Override
    public PageInfo listPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        loadPartsConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<PartsInfo> partsList = conditionalQueryMapper.listPartsInfo(conditionalQueryRequest);

        loadPartsinfo(partsList);

        return new PageInfo<>(partsList);
    }

    /**
     * 组装办件查询参数
     * @param conditionalQueryRequest
     * @return
     */
    private void loadPartsConditionalQueryRequest(ConditionalQueryRequest conditionalQueryRequest) {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());

        if(conditionalQueryRequest.isEntrust()){
            List<OpuOmOrg> opuOmOrgList = opuOmOrgMapper.listBelongOrgByUserId(opusLoginUser.getUser().getUserId());

            Set<String> currentUserOrgIdList = new HashSet<>();
            Set<String> selfAndParentOrgIdList = new HashSet<>();
            for(OpuOmOrg opuOmOrg: opuOmOrgList){
                currentUserOrgIdList.add(opuOmOrg.getOrgId());
                selfAndParentOrgIdList.add(opuOmOrg.getOrgId());
                if(StringUtils.isNotBlank(opuOmOrg.getOrgSeq())){
                    String[] orgIds = opuOmOrg.getOrgSeq().split(".");
                    for(String id:orgIds){
                        if(StringUtils.isNotBlank(id)){
                            selfAndParentOrgIdList.add(id);
                        }
                    }
                }
            }
            conditionalQueryRequest.setCurrentUserOrgIdList(currentUserOrgIdList);
            conditionalQueryRequest.setSelfAndParentOrgIdList(selfAndParentOrgIdList);
        }

        if (conditionalQueryRequest.isHandler()) {
            String loginName = opusLoginUser.getUser().getLoginName();
            conditionalQueryRequest.setLoginName(loginName);
            conditionalQueryRequest.setUserId(opusLoginUser.getUser().getUserId());
        }
    }

    /**
     * 组装办件查询返回
     * @param partsList
     */
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

                loadApplySource(partsInfo);

                loadInstInfo(partsInfo);
            }
        }
    }

    @Override
    public void exportPartsInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        loadPartsConditionalQueryRequest(conditionalQueryRequest);

        List<PartsInfo> partsList = conditionalQueryMapper.listPartsInfo(conditionalQueryRequest);

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
        List<Integer> columnWidthList = Arrays.asList(2500, 3000, 6000, 6500, 7000, 7000, 5000, 4000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("类型", "申报来源", "申报流水号", "项目名称", "事项名称", "阶段名称",  "所属主题", "申报状态", "业务状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applyType", "applySource", "applyinstCode", "projName", "itemName", "stageName", "themeName", "iteminstState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    @Override
    public List<BscDicCodeItem> listIndustryTree() {
        List<BscDicCodeItem> nodeList = new ArrayList<>();

        String orgId = SecurityContext.getCurrentOrgId();
        BscDicCodeType bscDicCodeType = bscDicCodeService.getTypeByTypeCode(DicConstants.XM_GBHY, orgId);

        if (bscDicCodeType != null) {
            return bscDicCodeService.getItemTreeByTypeId(bscDicCodeType.getTypeId());
        }

        return nodeList;

    }

    @Override
    public String queryApplyInfoTaskId(String applyinstId) {
        List<String> taskIdList = conditionalQueryMapper.queryApplyInfoTaskId(applyinstId);

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
        List<String> taskIdList = conditionalQueryMapper.queryItemInfoTaskId(iteminstId, loginName);

        if (taskIdList != null && taskIdList.size() > 0) {
            return taskIdList.get(0);
        }

        return null;
    }

    @Override
    public String queryViewId(String keyword) {
        List<String> viewIdList = conditionalQueryMapper.queryViewId(keyword);

        if (viewIdList != null && viewIdList.size() > 0) {
            return viewIdList.get(0);
        }

        return null;
    }

    @Override
    public PageInfo listWaitDoTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listWaitDoTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待办");

        loadRemindInfo(taskList);

        return new PageInfo<>(taskList);
    }

    /**
     * 查询催办信息
     * @param taskList
     */
    private void loadRemindInfo(List<TaskInfo> taskList){
        if(!taskList.isEmpty()) {
            List<String>  taskIds = new ArrayList<>();
            for(TaskInfo taskInfo:taskList){
                if(StringUtils.isNotBlank(taskInfo.getTaskId())){
                    taskIds.add(taskInfo.getTaskId());
                }
            }
            List<ActStoRemindAndReceiver> actStoRemindAndReceivers = conditionalQueryMapper.listTaskRemindInfo(SecurityContext.getCurrentUserId(),taskIds);

            for(ActStoRemindAndReceiver actStoRemindAndReceiver:actStoRemindAndReceivers){
                for(TaskInfo taskInfo:taskList){
                    if(StringUtils.isNotBlank(taskInfo.getTaskId())){
                        if(taskInfo.getTaskId().equals(actStoRemindAndReceiver.getTaskInstId())){
                            if(taskInfo.getRemindList()==null){
                                List<ActStoRemindAndReceiver> remindList  = new ArrayList<>();
                                taskInfo.setRemindList(remindList);
                            }
                            taskInfo.getRemindList().add(actStoRemindAndReceiver);
                        }
                    }
                }
            }

        }
    }

    private void loadTaskInfo(List<TaskInfo> taskList, String viewName) {
        if (taskList != null && taskList.size() > 0) {

            String viewId = queryViewId(viewName);

            if (StringUtils.isBlank(viewId)) {
                viewId = queryViewId("所有办件");
            }

            for (TaskInfo taskInfo : taskList) {

                taskInfo.setViewId(viewId);

                loadApplySource(taskInfo);

                loadInstInfo(taskInfo);

                loadUseLimitTimeText(taskInfo);

            }

        }
    }

    /**
     * 处理已用时显示
     * @param taskInfo
     */
    private void loadUseLimitTimeText(TaskInfo taskInfo) {

        taskInfo.setUseLimitTimeText("-天");

        if (taskInfo.getUseLimitTime() == null) {
            return;
        }
        if (StringUtils.isBlank(taskInfo.getTimeruleUnit())) {
            return;
        }

        String timeruleUnit = taskInfo.getTimeruleUnit();

        Double time = taskInfo.getUseLimitTime();

        String timeText = getTimeText(timeruleUnit,time);

        if(StringUtils.isNotBlank(timeText)){
            taskInfo.setUseLimitTimeText(timeText);
        }

    }

    /**
     * 处理承诺办结时限显示
     * @param info
     */
    private void loadDueNumText(BaseInfo info) {
        if(info==null){
            return;
        }

        String timeText = getTimeText(info.getTimeruleUnit(),info.getDueNum());
        if(StringUtils.isBlank(timeText)){
            timeText = "-天";
        }

        info.setDueNumText(timeText);

    }

    /**
     * 获取时限的显示文本
     * @param timeruleUnit
     * @param time
     * @return
     */
    private String getTimeText(String timeruleUnit,Double time){
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

        return null;
    }

    /**
     * 处理时限天数的显示文本
     * @param info
     */
    private void loadRemainingOrOverTimeText(BaseInfo info) {

        if (StringUtils.isBlank(info.getTimeruleUnit())) {
            return;
        }

        Double time = info.getRemainingTime();
        if (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState())) {
            time = info.getOverdueTime();
        }

        String timeText = getTimeText(info.getTimeruleUnit(),time);

        if(StringUtils.isNotBlank(timeText)){
            timeText = (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState()) ? "逾期" : "剩余") + timeText;
            info.setRemainingOrOverTimeText(timeText);
        }
        if(info instanceof TaskInfo){
            TaskInfo info1 = (TaskInfo) info;
            loadTaskRemainingOrOverTimeText(info1);
        }
    }

    /**
     * 加载节点的时限列表
     * @param info
     */
    private void loadTaskRemainingOrOverTimeText(TaskInfo info){
        try {
            List nodeTimelimitList = new ArrayList();
            List hasInst = new ArrayList();
            //获取节点时限实例信息
            ActStoTimeruleInst query1 = new ActStoTimeruleInst();
            query1.setProcInstId(info.getProcInstId());
            query1.setTaskinstId(info.getTaskId());
            List<ActStoTimeruleInst> actStoTimeruleInsts = actStoTimeruleInstMapper.listActStoTimeruleInst(query1);
            if(actStoTimeruleInsts.size() > 0){
                for(int i=0; i<actStoTimeruleInsts.size(); i++){
                    ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInsts.get(i);
                    String timeruleInstTimeText = getTimeruleInstTimeText(actStoTimeruleInst);
                    if("2".equals(actStoTimeruleInst.getTimeruleInstType())){
                        if(StringUtils.isNotBlank(timeruleInstTimeText)){
                            nodeTimelimitList.add(createVo("当前环节时限",timeruleInstTimeText,actStoTimeruleInst.getInstState()));
                        }
                        hasInst.add(actStoTimeruleInst.getTimegroupActId());

                    }else if("3".equals(actStoTimeruleInst.getTimeruleInstType())){
                        if(StringUtils.isNotBlank(timeruleInstTimeText)){
                            String name = "时限组时限";
                            ActStoTimegroup actStoTimegroup = actStoTimegroupMapper.getActStoTimegroupById(actStoTimeruleInst.getTimegroupId());
                            if(actStoTimegroup != null){
                                name = actStoTimegroup.getTimegroupName() + "时限";
                            }
                            nodeTimelimitList.add(createVo(name,timeruleInstTimeText,actStoTimeruleInst.getInstState()));
                        }
                        hasInst.add(actStoTimeruleInst.getTimegroupId());
                    }
                }
            }
            //获取节点时限定义信息
            String appFlowdefId = null;
            //查询appFlowdefId 用于关联出节点时限配置
            ActStoAppinst actStoAppinst = actStoAppinstMapper.getActStoAppinstByProcInstId(info.getProcInstId());
            //查的到则是一级流程节点
            if(actStoAppinst != null){
                appFlowdefId = actStoAppinst.getAppFlowdefId();
            }else{
                //查询子流程
                ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(info.getProcInstId());
                if(actStoAppinstSubflow != null){
                    ActTplAppTrigger trigger = actTplAppTriggerMapper.getAllActTplAppTriggerById(actStoAppinstSubflow.getTriggerId());
                    if(trigger != null){
                        appFlowdefId = trigger.getTriggerAppFlowdefId();
                    }
                }
            }
            if(StringUtils.isNotBlank(appFlowdefId)) {
                ActStoTimegroupAct query = new ActStoTimegroupAct();
                query.setAppFlowdefId(appFlowdefId);
                List<ActStoTimegroupAct> actStoTimegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(query);
                if (actStoTimegroupActs.size() > 0) {
                    List<ActStoTimerule> actStoTimerules = actStoTimeruleService.listActStoTimerule(new ActStoTimerule());
                    if (actStoTimerules.size() == 0) return;
                    for(int i=0,len=actStoTimegroupActs.size(); i<len; i++){
                        ActStoTimegroupAct actStoTimegroupAct = actStoTimegroupActs.get(i);
                        String unit = "WD";
                        for(int j=0,lenj=actStoTimerules.size(); j<lenj; j++){
                            if(actStoTimegroupAct.getTimeruleId().equals(actStoTimerules.get(j).getTimeruleId())){
                                unit = actStoTimerules.get(j).getTimeruleUnit();
                                break;
                            }
                        }
                        String state = "1";
                        if(actStoTimegroupAct.getTimeLimit() <= 2 ){
                            state = "2";
                        }
                        if(StringUtils.isNotBlank(actStoTimegroupAct.getTimegroupId())){
                            if(hasInst.contains(actStoTimegroupAct.getTimegroupId())) continue;
                            hasInst.add(actStoTimegroupAct.getTimegroupId());
                            String name = "时限组时限";
                            ActStoTimegroup actStoTimegroup = actStoTimegroupMapper.getActStoTimegroupById(actStoTimegroupAct.getTimegroupId());
                            if(actStoTimegroup != null){
                                name = actStoTimegroup.getTimegroupName() + "时限";
                            }
                            String timeText = getTimeText(unit,actStoTimegroupAct.getTimeLimit().doubleValue());
                            if(StringUtils.isNotBlank(timeText)){
                                timeText = "剩余" + timeText;
                            }
                            nodeTimelimitList.add(createVo(name,timeText,state));

                        }else if(StringUtils.isBlank(actStoTimegroupAct.getTimegroupId())){
                            if(hasInst.contains(actStoTimegroupAct.getTimegroupActId())) continue;

                            String timeText = getTimeText(unit,actStoTimegroupAct.getTimeLimit().doubleValue());
                            if(StringUtils.isNotBlank(timeText)){
                                timeText = "剩余" + timeText;
                            }
                            nodeTimelimitList.add(createVo("当前环节时限",timeText,state));
                        }
                    }
                }
            }
            if(nodeTimelimitList.size() > 0){
                nodeTimelimitList.add(createVo("当前办件时限",info.getRemainingOrOverTimeText(),info.getInstState()));
                info.setNodeTimelimitList(nodeTimelimitList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map createVo(String name,String text,String state){
        Map nodeTimelimit = new HashMap();
        nodeTimelimit.put("name",name);
        nodeTimelimit.put("text",text);
        nodeTimelimit.put("instState",state);
        return nodeTimelimit;
    }

    /**
     * 获取时限实例的时间值
     * @param info
     * @return
     */
    private String getTimeruleInstTimeText(ActStoTimeruleInst info){
        if (StringUtils.isBlank(info.getTimeruleUnit())) {
            return null;
        }
        Double time = info.getRemainingTime();
        if (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState())) {
            time = info.getOverdueTime();
        }
        String timeText = getTimeText(info.getTimeruleUnit(),time);
        if(StringUtils.isNotBlank(timeText)){
            timeText = (TimeruleInstState.OVERDUE.getValue().equals(info.getInstState()) ? "逾期" : "剩余") + timeText;
        }
        return timeText;
    }


    @Override
    public PageInfo listDoneTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listDoneTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "已办");

        loadRemindInfo(taskList);

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listConcludedTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listConcludedTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "办结");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listPreliminaryTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listPreliminaryTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待预审");

        return new PageInfo<>(taskList);
    }

    /**
     * 获取窗口信息
     * @param conditionalQueryRequest
     * @return
     */
    private ConditionalQueryRequest getWindowUsers(ConditionalQueryRequest conditionalQueryRequest) {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();

        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());

        conditionalQueryRequest.setEntrust(true);

        String loginName = opusLoginUser.getUser().getLoginName();
        conditionalQueryRequest.setLoginName(loginName);
        conditionalQueryRequest.setUserId(opusLoginUser.getUser().getUserId());

        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(opusLoginUser.getUser().getUserId());
        if (list.size() == 0) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
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

        List<TaskInfo> taskList = conditionalQueryMapper.listDismissedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "不予受理");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedCorrectTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listNeedCorrectTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "待补正");

        return new PageInfo<>(taskList);

    }

    @Override
    public PageInfo listNeedCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listNeedCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "待补全申报");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listNeedConfirmCompletedApplyByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listNeedConfirmCompletedApply(conditionalQueryRequest);

        loadTaskInfo(taskList, "补全待确认");

        return new PageInfo<>(taskList);
    }


    @Override
    public PageInfo listPickupCheckTasksByPageState(ConditionalQueryRequest conditionalQueryRequest, Page page,String type) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);
        conditionalQueryRequest.setReceiveMode(type);
        List<TaskInfo> taskList = conditionalQueryMapper.listPickupCheckTasksByPageState(conditionalQueryRequest);

        loadTaskInfo(taskList, "取件登记");

        return new PageInfo<>(taskList);
    }
    @Override
    public PageInfo listPickupCheckTasksByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listPickupCheckTasksByPage(conditionalQueryRequest);

        loadTaskInfo(taskList, "取件登记");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listPickupCheckTasksByPageFinish(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);

        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listPickupCheckTasksFinishByPage(conditionalQueryRequest);

        loadTaskInfo(taskList, "已取件");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listDoneliminaryTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        getWindowUsers(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listDoneliminaryTasks(conditionalQueryRequest);

        loadTaskInfo(taskList, "已预审");

        return new PageInfo<>(taskList);
    }

    //特别程序件-todo
    @Override
    public PageInfo lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.lisSpecialProcedureTasks(conditionalQueryRequest);
        taskList = taskList.stream().filter(info -> StringUtils.isNotBlank(info.getTaskId())).collect(Collectors.toList());
        loadTaskInfo(taskList, "特殊程序件");
//        loadTaskInfo(taskList, "待办");
        return new PageInfo<>(taskList);
    }

    @Override
    public List<OpuOmOrg> listNeedCorrectTasksOrganizer() throws Exception {
        String windowId = null;
        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(SecurityContext.getCurrentUserId());
        if (list.size() != 0) {
            windowId = list.get(0).getWindowId();
        }
        return conditionalQueryMapper.listNeedCorrectTasksOrganizer(windowId);
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
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listItemDisgree(conditionalQueryRequest);

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
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listItemOutScope(conditionalQueryRequest);

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
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listItemToleranceAccept(conditionalQueryRequest);

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
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listItemAgreeTolerance(conditionalQueryRequest);

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
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listUnConfirmItemCorrect(conditionalQueryRequest);

        loadTaskInfo(taskList, "补正待确认件");

        return new PageInfo<>(taskList);
    }

    @Override
    public PageInfo listUnCompleteFlow(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        conditionalQueryRequest.setHandler(true);
        loadConditionalQueryRequest(conditionalQueryRequest, false);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listUnCompleteFlow(conditionalQueryRequest);

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

    @Override
    public PageInfo listSeriesApplyItem(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception{

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
        String loginName = opusLoginUser.getUser().getLoginName();
        conditionalQueryRequest.setLoginName(loginName);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<AeaItemBasic> aeaItemBasics = conditionalQueryMapper.listSeriesApplyItem(conditionalQueryRequest);

        loadAeaItemBasic(aeaItemBasics,opusLoginUser.getCurrentOrgId());

        return new PageInfo<>(aeaItemBasics);
    }

    private void loadAeaItemBasic(List<AeaItemBasic> aeaItemBasics,String orgId) {
        if (aeaItemBasics != null && aeaItemBasics.size() > 0) {
            // 服务对象
            List<BscDicCodeItem> itemFwjgxzs = bscDicCodeService.getActiveItemsByTypeCode("ITEM_FWJGXZ", orgId);

            // 办件类型
            List<BscDicCodeItem> itemPropertys = bscDicCodeService.getActiveItemsByTypeCode("ITEM_PROPERTY", orgId);

            for (AeaItemBasic aeaItemBasic : aeaItemBasics) {

                if(StringUtils.isBlank(aeaItemBasic.getXkdx())){
                    aeaItemBasic.setXkdx("5");
                }

                for(BscDicCodeItem bscDicCodeItem:itemPropertys){
                    if(bscDicCodeItem.getItemCode().equals(aeaItemBasic.getItemProperty())){
                        aeaItemBasic.setItemProperty(bscDicCodeItem.getItemName());
                        break;
                    }
                }

                for(BscDicCodeItem bscDicCodeItem:itemFwjgxzs){
                    if(bscDicCodeItem.getItemCode().equals(aeaItemBasic.getXkdx())){
                        aeaItemBasic.setXkdx(bscDicCodeItem.getItemName());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void updateRemindRead(String remindReceiverIds)throws Exception{

        if(StringUtils.isBlank(remindReceiverIds)){
            throw new RuntimeException("缺少参数");
        }
        String[] remindReceiverIdArr = remindReceiverIds.split(",");

        if(remindReceiverIdArr!=null && remindReceiverIdArr.length>0){
            for(String remindReceiverId:remindReceiverIdArr){
                ActStoRemindReceiver actStoRemindReceiver = new ActStoRemindReceiver();
                actStoRemindReceiver.setIsRead("1");
                actStoRemindReceiver.setRemindReceiverId(remindReceiverId);
                actStoRemindReceiver.setReadedTime(new Date());
                actStoRemindReceiverMapper.updateActStoRemindReceiver(actStoRemindReceiver);
            }
        }


    }

    @Override
    public PageInfo listMyDrafts(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception{

        String loginName = SecurityContext.getOpusLoginUser().getUser().getLoginName();
        conditionalQueryRequest.setLoginName(loginName);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listMyDrafts(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    @Override
    public void sendSms(String applyinstId,String iteminstId) throws Exception{
        if(!smsConfigProperties.isEnable()){
            throw new RuntimeException("系统未开启短信提醒功能");
        }

       if(StringUtils.isNotBlank(iteminstId)){
           AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
           if(aeaHiIteminst==null){
               throw new RuntimeException("参数错误");
           }
           aplanmisEventPublisher.conditionalPublishEvent4Iteminst(new IteminstEventVo(iteminstId, aeaHiIteminst.getIteminstState(),true));
       }else if(StringUtils.isNotBlank(applyinstId)){
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
           if(aeaHiApplyinst==null){
               throw new RuntimeException("参数错误");
           }
           aplanmisEventPublisher.conditionalPublishEvent(new ApplyEventVo(applyinstId, null, null, aeaHiApplyinst.getApplyinstState(), null, null,true));
       }else{
           throw new RuntimeException("缺少参数");
       }


    }


    @Override
    public PageInfo listWindowPartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {
        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());
        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(opusLoginUser.getUser().getUserId());
        if (list.size() == 0) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        String windowId = list.get(0).getWindowId();
        conditionalQueryRequest.setWindowId(windowId);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<PartsInfo> partsList = conditionalQueryMapper.listWindowParts(conditionalQueryRequest);

        loadPartsinfo(partsList);

        return new PageInfo<>(partsList);
    }


    @Override
    public void exportWindowParts(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
        conditionalQueryRequest.setCurrentOrgId(opusLoginUser.getCurrentOrgId());
        List<AeaServiceWindowUser> list = aeaServiceWindowUserMapper.getAeaServiceWindowUserByUserId(opusLoginUser.getUser().getUserId());
        if (list.size() == 0) {
            throw new RuntimeException("当前用户没有绑定所在服务窗口");
        }
        String windowId = list.get(0).getWindowId();
        conditionalQueryRequest.setWindowId(windowId);

        List<PartsInfo> partsList = conditionalQueryMapper.listWindowParts(conditionalQueryRequest);

        loadPartsinfo(partsList);

        for (PartsInfo partsInfo : partsList) {
            TimeruleInstState timeruleInstState = TimeruleInstState.getTimeruleInstState(partsInfo.getInstState());
            if (timeruleInstState != null) {
                partsInfo.setInstState(timeruleInstState.getName());
            }
        }

        ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
        excelParam.setDataList(partsList);
        excelParam.setFileName("窗口经办办件数据导出.xls");
        excelParam.setSheetName("窗口经办办件数据");
        excelParam.setTitleName("窗口经办办件数据");
        List<Integer> columnWidthList = Arrays.asList(2500, 3000, 6000, 6500, 7000, 7000, 5000, 4000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("类型", "申报来源", "申报流水号", "项目名称", "事项名称", "阶段名称", "所属主题", "申报状态", "业务状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applyType", "applySource", "applyinstCode", "projName", "itemName", "stageName", "themeName", "iteminstState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    @Override
    public PageInfo listSublineApplyInfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        conditionalQueryRequest.setEntrust(true);
        loadApplyConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listSublineApplyInfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }

    @Override
    public void exportSublineApplyInfo(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        conditionalQueryRequest.setEntrust(true);
        loadApplyConditionalQueryRequest(conditionalQueryRequest);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listSublineApplyInfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        for (ApplyInfo applyInfo : applyInfoList) {
            TimeruleInstState timeruleInstState = TimeruleInstState.getTimeruleInstState(applyInfo.getInstState());
            if (timeruleInstState != null) {
                applyInfo.setInstState(timeruleInstState.getName());
            }
        }

        ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
        excelParam.setDataList(applyInfoList);
        excelParam.setFileName("辅线申报数据导出.xls");
        excelParam.setSheetName("辅线申报数据");
        excelParam.setTitleName("辅线申报数据");
        List<Integer> columnWidthList = Arrays.asList(3000, 6000, 6500, 7000, 7000, 6000, 3000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("申报来源", "申请编号", "项目名称",  "阶段名称", "事项名称", "所属主题", "申报状态", "时限状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applySource", "applyinstCode", "projName",  "stageName", "itemName", "themeName", "applyState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    @Override
    public PageInfo listSublinePartsByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        conditionalQueryRequest.setEntrust(true);
        loadPartsConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<PartsInfo> partsList = conditionalQueryMapper.listSublinePartsInfo(conditionalQueryRequest);

        loadPartsinfo(partsList);

        return new PageInfo<>(partsList);
    }

    @Override
    public void exportSublineParts(ConditionalQueryRequest conditionalQueryRequest, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        conditionalQueryRequest.setEntrust(true);
        loadPartsConditionalQueryRequest(conditionalQueryRequest);

        List<PartsInfo> partsList = conditionalQueryMapper.listSublinePartsInfo(conditionalQueryRequest);

        loadPartsinfo(partsList);

        for (PartsInfo partsInfo : partsList) {
            TimeruleInstState timeruleInstState = TimeruleInstState.getTimeruleInstState(partsInfo.getInstState());
            if (timeruleInstState != null) {
                partsInfo.setInstState(timeruleInstState.getName());
            }
        }

        ExcelUtils.ExcelParam excelParam = new ExcelUtils.ExcelParam();
        excelParam.setDataList(partsList);
        excelParam.setFileName("辅线办件数据导出.xls");
        excelParam.setSheetName("辅线办件数据");
        excelParam.setTitleName("辅线办件数据");
        List<Integer> columnWidthList = Arrays.asList(3000, 6000, 6500, 7000, 7000,5000, 4000, 3000, 3000, 4000);
        List<String> rowTitleList = Arrays.asList("申报来源", "申报流水号", "项目名称", "事项名称", "阶段名称", "所属主题", "申报状态", "业务状态", "剩余/逾期用时", "申报时间");
        List<String> fieldList = Arrays.asList("applySource", "applyinstCode", "projName", "itemName", "stageName", "themeName", "iteminstState", "instState", "remainingOrOverTimeText", "applyTime");
        excelParam.setColumnWidthList(columnWidthList);
        excelParam.setRowTitleList(rowTitleList);
        excelParam.setFieldList(fieldList);

        ExcelUtils.exportExcel(excelParam, req, resp);
    }

    @Override
    public PageInfo listWindowItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception {

        conditionalQueryRequest.setEntrust(false);

        loadApplyConditionalQueryRequest(conditionalQueryRequest);

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<TaskInfo> taskList = conditionalQueryMapper.listWindowItemOutScope(conditionalQueryRequest);

        loadTaskInfo(taskList, "所有办件");

        return new PageInfo<>(taskList);
    }


    @Override
    public PageInfo listStageConcludedApplyInfoByPage(ConditionalQueryRequest conditionalQueryRequest, Page page) throws Exception{
        if (StringUtils.isNotBlank(conditionalQueryRequest.getIndustry())) {
            conditionalQueryRequest.setIndustries(conditionalQueryRequest.getIndustry().trim().split(","));
        }

        changeOrderBySql(page);

        PageHelper.startPage(page);

        List<ApplyInfo> applyInfoList = conditionalQueryMapper.listStageConcludedApplyInfo(conditionalQueryRequest);

        loadApplyinfo(applyInfoList);

        return new PageInfo<>(applyInfoList);
    }


}
