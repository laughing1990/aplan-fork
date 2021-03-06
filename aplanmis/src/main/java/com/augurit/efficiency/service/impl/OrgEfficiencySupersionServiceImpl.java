package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.domain.AeaAnaOrgDayStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaOrgMonthStatistics;
import com.augurit.aplanmis.common.domain.AeaAnaOrgWeekStatistics;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.augurit.aplanmis.common.vo.analyse.*;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import com.augurit.efficiency.constant.DateType;
import com.augurit.efficiency.service.OrgEfficiencySupersionService;
import com.augurit.efficiency.utils.CalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrgEfficiencySupersionServiceImpl implements OrgEfficiencySupersionService {

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaAnaOrgMonthStatisticsMapper aeaAnaOrgMonthStatisticsMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;
    @Autowired
    private AeaAnaOrgDayStatisticsMapper aeaAnaOrgDayStatisticsMapper;
    @Autowired
    private AeaAnaOrgWeekStatisticsMapper aeaAnaOrgWeekStatisticsMapper;
    @Autowired
    private EfficiencySupervisionMapper efficiencySupervisionMapper;
    @Autowired
    private ConditionalJumpMapper conditionalJumpMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;


    @Override
    public List<Map> countItemByApplyinstSource(String rootOrgId) {
        Long net = aeaHiIteminstMapper.countIteminstByApplySource(ApplySource.NET.getValue(), rootOrgId);
        Long win = aeaHiIteminstMapper.countIteminstByApplySource(ApplySource.WIN.getValue(), rootOrgId);
        List<Map> res = new ArrayList<>();
        res.add(new HashMap(2) {{
            put("name", "网上申报");
            put("value", net);
        }});
        res.add(new HashMap(2) {{
            put("name", "窗口申报");
            put("value", win);
        }});
        return res;
    }

    @Override
    public List<Map> countItemForSeriesAndStage(String rootOrgId) {
        Long series = aeaHiIteminstMapper.countSeriesIteminst(rootOrgId);
        Long stage = aeaHiIteminstMapper.countStageIteminst(rootOrgId);
        List<Map> res = new ArrayList<>();
        res.add(new HashMap(2) {{
            put("name", "一般单项");
            put("value", series);
        }});
        res.add(new HashMap(2) {{
            put("name", "并联单项");
            put("value", stage);
        }});
        return res;
    }

    @Override
    public List<ApplyUnusualStatisticVo> queryItemExceptionStatistics(String rootOrgId) {
        List<ApplyUnusualStatisticVo> res = aeaAnaOrgDayStatisticsMapper.listApplyUnusualStatistic(rootOrgId);
        //res.removeIf(it -> (it.getNotAcceptRate() == null && it.getOverdueRate() == null));
        return res;
    }


    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaAnaWinDayStatisticsMapper aeaAnaWinDayStatisticsMapper;

    @Override
    public List<ItemStatisticsVo> getItemStateStatistics(String orgId, boolean isCurrent) throws Exception {
        if(isCurrent){
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                orgId = opuOmOrg.getOrgId();
            }else{
                throw new Exception("找不到当前用户所属部门orgId");
            }
        }

        ItemStatisticsVo v1 = getRongQueShouLi(orgId);
                /*= new ItemStatisticsVo();
        v1.setName("容缺受理件");
        AeaHiIteminst search = new AeaHiIteminst();
        search.setIsToleranceAccept("1");
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        int total = aeaHiIteminstMapper.listAeaHiIteminst(search).size();
        v1.setCount(total);
*/
        ItemStatisticsVo v2 = getDaiBuZheng(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.CORRECT_MATERIAL_START.getValue()}, "待补证件");
        ItemStatisticsVo v3 = getBuZhengDaiQueRen(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.CORRECT_MATERIAL_END.getValue()}, "补正待确认件");
        ItemStatisticsVo v4 = getTeShuChengXu(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.SPECIFIC_PROC_START.getValue(), ItemStatus.SPECIFIC_PROC_END.getValue()}, "特殊程序件");
        ItemStatisticsVo v5 = getRongQueTongGuo(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.AGREE_TOLERANCE.getValue()}, "容缺通过件");
        ItemStatisticsVo v6 = getBanLiBuTongGuo(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.DISAGREE.getValue()}, "办理不通过件");
        ItemStatisticsVo v7 = getBuYuShouLi(orgId);//calculateItemStateStatistics(new String[]{ItemStatus.OUT_SCOPE.getValue()}, "不予受理件");

        List<ItemStatisticsVo> list = new ArrayList<>();
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        list.add(v6);
        list.add(v7);
        return list;
    }

    /**
     * 容缺受理件
     * @param orgId
     * @return
     */
    private ItemStatisticsVo getRongQueShouLi(String orgId) throws Exception{
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(true);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listItemToleranceAccept(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("容缺受理件");
        return statisticsVo;
    }

    /**
     * 不予受理
     * @param orgId
     * @return
     */
    private ItemStatisticsVo getBuYuShouLi(String orgId) throws Exception{
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listItemOutScope(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("不予受理件");
        return statisticsVo;
    }

    /**
     * 办理不通过件
     * @param orgId
     * @return
     */
    private ItemStatisticsVo getBanLiBuTongGuo(String orgId) throws Exception{
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listItemDisgree(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("办理不通过件");
        return statisticsVo;
    }

    /**
     * 获取容缺通过
     * @param orgId
     * @return
     */
    private ItemStatisticsVo getRongQueTongGuo(String orgId) throws Exception{
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listItemAgreeTolerance(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("容缺通过件");
        return statisticsVo;
    }

    /**
     * 获取待补证
     *
     * @return
     * @throws Exception
     * @param orgId
     */
    private ItemStatisticsVo getDaiBuZheng(String orgId) throws Exception {
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listNeedCorrectTasks(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("待补正件");
        return statisticsVo;
    }

    /**
     * 获取特殊程序统计
     *
     * @return
     * @param orgId
     */
    private ItemStatisticsVo getTeShuChengXu(String orgId) {
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.lisSpecialProcedureTasks(conditionalQueryRequest);
        taskList = taskList.stream().filter(info -> StringUtils.isNotBlank(info.getTaskId())).collect(Collectors.toList());
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("特殊程序件");
        return statisticsVo;
    }

    /**
     * 获取补正待确认
     *
     * @return
     * @param orgId
     */
    private ItemStatisticsVo getBuZhengDaiQueRen(String orgId) throws Exception {
        ItemStatisticsVo statisticsVo = new ItemStatisticsVo();
        ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
        conditionalQueryRequest.setCurrentOrgId(SecurityContext.getCurrentOrgId());
        conditionalQueryRequest.setEntrust(false);
        conditionalQueryRequest.setHandler(true);
        conditionalQueryRequest.setCurrentOrgId(orgId);
        List<TaskInfo> taskList = conditionalJumpMapper.listUnConfirmItemCorrect(conditionalQueryRequest);
        statisticsVo.setCount(taskList.size());
        statisticsVo.setName("补正待确认件");
        return statisticsVo;
    }


    @Override
    public List<ItemDetailFormVo> queryOrgHandleItemStatistics(String startTime, String endTime) throws Exception {
        List<ItemDetailFormVo> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime);
        Date endDate = sdf.parse(endTime);
        long start = startDate.getTime();
        long end = endDate.getTime();
        if (start > end) {
            throw new RuntimeException("查询开始时间不能大于结束时间。");
        }
        Date now = new Date();
        Date today = DateUtils.fullDate(now);
        long todayEnd = today.getTime();
        if (start > todayEnd) {
            throw new RuntimeException("查询开始时间不能大于今天。");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<String> orgIdList = new ArrayList<>();
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        for (int i = 0, len = orgInfoList.size(); i < len; i++) {
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            ItemDetailFormVo vo = new ItemDetailFormVo();
            vo.setOrgId(orgId);
            vo.setOrgName(orgName);
            orgIdList.add(orgId);
            if(!result.contains(vo)){
                result.add(vo);
            }
        }
        String[] orgIds = orgIdList.toArray(new String[orgIdList.size()]);
        List<AeaAnaOrgDayStatistics> list = aeaAnaOrgDayStatisticsMapper.queryAeaAnaOrgDayStatisticsGreaterThanStartTime(orgIds, null, startTime, endTime);
        if (list != null && list.size() > 0) {
            for(int i=0,len=result.size();i<len;i++){
                ItemDetailFormVo vo = result.get(i);
                for(int j=0;j<list.size();j++){
                    AeaAnaOrgDayStatistics orgDayStatistics = list.get(j);
                    if(vo.getOrgId().equals(orgDayStatistics.getOrgId())){
                        covertItemDetailFormVo(vo, orgDayStatistics);
                        list.remove(orgDayStatistics);
                        j--;
                    }
                }
                setItemDetailFormVorRate(vo);
            }
        }
        return result;
    }

    /**
     * 累加统计数据
     *
     * @param vo
     * @param oneOrg
     */
    private void covertItemDetailFormVo(ItemDetailFormVo vo, AeaAnaOrgDayStatistics oneOrg) {
        if (vo != null && oneOrg != null) {
            vo.setOrgId(oneOrg.getOrgId());
            vo.setOrgName(oneOrg.getOrgName());
            int receiptCount = vo.getReceiptCount();
            int acceptCount = vo.getAcceptCount();
            int notAcceptCount = vo.getNotAcceptCount();
            int completedCount = vo.getCompletedCount();
            int overdueCount = vo.getOverdueCount();
            vo.setReceiptCount(receiptCount + oneOrg.getDayApplyCount());
            vo.setAcceptCount(acceptCount + oneOrg.getDayAcceptanceCount());
            vo.setNotAcceptCount(notAcceptCount + oneOrg.getDayOutScopeCount());
            vo.setCompletedCount(completedCount + oneOrg.getDayCompletedCount());
            vo.setOverdueCount(overdueCount + oneOrg.getDayOverTimeCount());
        }
    }

    /**
     * 计算办结率和逾期率
     *
     * @param vo
     */
    private void setItemDetailFormVorRate(ItemDetailFormVo vo) {
        if (vo != null) {
            Integer acceptCount = vo.getAcceptCount();
            Integer completedCount = vo.getCompletedCount();
            Integer overdueCount = vo.getOverdueCount();
            //办结率
            Double completedRate = (acceptCount == 0 || completedCount == 0) ? 0 : CalculateUtil.formatDoubleValue((double) completedCount / acceptCount);
            vo.setCompletedRate(completedRate);
            //逾期率
            Double overTimeRate = (acceptCount == 0 || overdueCount == 0) ? 0 : CalculateUtil.formatDoubleValue((double) overdueCount / acceptCount);
            vo.setOverdueRate(overTimeRate);
        }
    }

    /**
     * 根据不同类型，查询今日、本周、本月的部门办理事项统计
     *
     * @param type W表示本周，M表示本月
     * @return
     */
    @Override
    public List<ItemDetailFormVo> queryOrgHandleItemStatisticsToYesterday(String type) {
        List<ItemDetailFormVo> result = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        for (int i = 0, len = orgInfoList.size(); i < len; i++) {
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            ItemDetailFormVo vo = new ItemDetailFormVo();
            vo.setOrgId(orgId);
            vo.setOrgName(orgName);
            if (DateType.CUR_WEEK.equals(type)) {
                int weekNum = DateUtils.getThisWeekNum(new Date());
                AeaAnaOrgWeekStatistics weekStatistics = aeaAnaOrgWeekStatisticsMapper.sumWeekStatistics(weekNum, rootOrgId, orgId);
                if (weekStatistics != null) {
                    vo.setReceiptCount(weekStatistics.getWeekApplyCount());
                    vo.setAcceptCount(weekStatistics.getWeekAcceptanceCount());
                    vo.setNotAcceptCount(weekStatistics.getWeekOutScopeCount());
                    vo.setCompletedCount(weekStatistics.getWeekCompletedCount());
                    vo.setOverdueCount(weekStatistics.getWeekOverTimeCount());
                }
            } else if (DateType.CUR_MONTH.equals(type)) {
                String yearMonth = DateUtils.getDateString("yyyy-MM");
                AeaAnaOrgMonthStatistics monthStatistics = aeaAnaOrgMonthStatisticsMapper.sumMonthStatistics(yearMonth, rootOrgId, orgId);
                if (monthStatistics != null) {
                    vo.setReceiptCount(monthStatistics.getMonthApplyCount());
                    vo.setAcceptCount(monthStatistics.getMonthAcceptanceCount());
                    vo.setNotAcceptCount(monthStatistics.getMonthOutScopeCount());
                    vo.setCompletedCount(monthStatistics.getMonthCompletedCount());
                    vo.setOverdueCount(monthStatistics.getMonthOverTimeCount());
                }
            }
            setItemDetailFormVorRate(vo);
            result.add(vo);
        }
        return result;
    }

    /**
     * 月度事项受理统计
     *
     * @param startMonth
     * @param endMonth
     */
    @Override
    public List<OrgMonthApplyStatisticsVo> queryOrgApplyStatisticsByMonth(String startMonth, String endMonth) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<OrgMonthApplyStatisticsVo> applyStatisticsByMonth = aeaAnaOrgMonthStatisticsMapper.getOrgApplyStatisticsByMonth(startMonth, endMonth, rootOrgId);

        return applyStatisticsByMonth;
    }

    /**
     * 地区办件统计
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<OrgAreaStatisticsVo> queryStatisticsCompletedByArea(String startTime, String endTime) {
        List<OrgAreaStatisticsVo> areaStatisticsList = aeaAnaOrgDayStatisticsMapper.getDayStatisticsCompletedByArea(startTime, endTime);
        return areaStatisticsList;
    }

    @Override
    public Map<String, Object> getOrgItemStatistics(String startDate, String endDate, String type, boolean isCurrent, String orgId) throws Exception{

        if (isCurrent) {
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                orgId = opuOmOrg.getOrgId();
            }else {
                throw new Exception("找不到当前部门的orgId");
            }
        }

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<ItemDetailFormVo> list = new ArrayList<>();
        if (DateType.YESTERDAY.equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startDate = yesterday;
            endDate = yesterday ;
            list = aeaAnaOrgDayStatisticsMapper.getOrgDayStatistics(startDate,endDate,orgId,rootOrgId);
        } else if (DateType.CUR_WEEK.equals(type)) {
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            list = aeaAnaOrgWeekStatisticsMapper.getOrgWeekStatistics(thisYear,thisWeekNum,thisWeekNum,orgId,rootOrgId);
        } else if (DateType.CUR_MONTH.equals(type)) {
            String yearMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            list = aeaAnaOrgMonthStatisticsMapper.getOrgMonthStatistics(yearMonth,yearMonth,orgId,rootOrgId);
        }else{
            if (!DateUtils.checkTimeParam(startDate, endDate, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate _endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(_endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endDate = yesterday ;
            }
            list = aeaAnaOrgDayStatisticsMapper.getOrgDayStatistics(startDate,endDate,orgId,rootOrgId);
        }

        //分组计算统计各情况
        List<ItemDetailFormVo> itemStatisticsList  = new ArrayList<>();
        int total = 0;
        if(list.size() >0){
            //按事项id来分组
            Map<String, List<ItemDetailFormVo>> collect = list.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getItemId));
            Set<Map.Entry<String, List<ItemDetailFormVo>>> itemEntris = collect.entrySet();
            for(Map.Entry<String, List<ItemDetailFormVo>> entry :itemEntris){
                List<ItemDetailFormVo> value = entry.getValue();
                ItemDetailFormVo record = new ItemDetailFormVo();
                //统计每个事项对应状态的数量
                int receiptCount=0,acceptCount=0,overdueCount=0,winRece=0,netRece=0;
                for(ItemDetailFormVo obj:value){
                    total += obj.getReceiptCount();
                    receiptCount+= obj.getReceiptCount();
                    acceptCount += obj.getAcceptCount();
                    overdueCount += obj.getOverdueCount();
                    if(ApplySource.NET.getValue().equals(obj.getApplyinstSource())){
                        netRece += obj.getReceiptCount();
                    }else{
                        winRece += obj.getReceiptCount();
                    }

                }
                //计算比率
                double acceptanceRate = CalculateUtil.formatDoubleValue(CalculateUtil.calculRate(acceptCount,receiptCount));
                double overTimeRate =CalculateUtil.formatDoubleValue( CalculateUtil.calculRate(overdueCount,acceptCount));

                record.setAcceptanceRate(acceptanceRate);
                record.setOverdueRate(overTimeRate);
                ItemDetailFormVo tmp = value.get(0);
                record.setItemName(tmp.getItemName());
                record.setReceiptCount(receiptCount);
                record.setAcceptCount(acceptCount);
                record.setOverdueCount(overdueCount);
                record.setWinApplyCount(winRece);
                record.setNetApplyCount(netRece);
                itemStatisticsList.add(record);
            }
        }
        Map<String ,Object > result = new HashMap<>();
        result.put("total" ,total);
        result.put("data",itemStatisticsList);
        return result;
    }

    @Override
    public List<Map<String, Object>> getOrgItemAcceptStatistics(String startTime, String endTime, String type, boolean isCurrent) throws Exception {
        String orgId = null;
        if (isCurrent) {
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                orgId = opuOmOrg.getOrgId();
            }
        }

        if (DateType.YESTERDAY.equals(type)) {//昨日日统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDate = DateUtils.getPreDateByDate(new Date());
            String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");

            List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getOrgItemAcceptStatistics(orgId, preDateStr, preDateStr, rootOrgId);
            List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().sorted(Comparator.comparing(AeaAnaOrgDayStatistics::getDayApplyCount).reversed()).map(statistics -> {
                Map<String, Object> data = new HashMap<>();
                data.put("itemId", statistics.getItemId());
                data.put("itemName", statistics.getItemName());
                data.put("apply", statistics.getDayApplyCount());
                data.put("acceptDeal", statistics.getDayAcceptanceCount());
                return data;
            }).collect(Collectors.toList());
            return collect;
        } else if (DateType.CUR_WEEK.equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());

            List<AeaAnaOrgWeekStatistics> orgItemAcceptStatistics = aeaAnaOrgWeekStatisticsMapper.getOrgItemAcceptStatistics(orgId, thisYear, thisWeekNum, rootOrgId);
            List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().sorted(Comparator.comparing(AeaAnaOrgWeekStatistics::getWeekApplyCount).reversed()).map(statistics -> {
                Map<String, Object> data = new HashMap<>();
                data.put("itemId", statistics.getItemId());
                data.put("itemName", statistics.getItemName());
                data.put("apply", statistics.getWeekApplyCount());
                data.put("acceptDeal", statistics.getWeekAcceptanceCount());
                return data;
            }).collect(Collectors.toList());
            return collect;
        } else if (DateType.CUR_MONTH.equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String thisMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");

            List<AeaAnaOrgMonthStatistics> orgItemAcceptStatistics = aeaAnaOrgMonthStatisticsMapper.getOrgItemAcceptStatistics(orgId, thisMonth, rootOrgId);
            List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().sorted(Comparator.comparing(AeaAnaOrgMonthStatistics::getMonthApplyCount).reversed()).map(statistics -> {
                Map<String, Object> data = new HashMap<>();
                data.put("itemId", statistics.getItemId());
                data.put("itemName", statistics.getItemName());
                data.put("apply", statistics.getMonthApplyCount());
                data.put("acceptDeal", statistics.getMonthAcceptanceCount());
                return data;
            }).collect(Collectors.toList());
            return collect;
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();

            List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getOrgItemAcceptStatistics(orgId, startTime, endTime, rootOrgId);
            List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().sorted(Comparator.comparing(AeaAnaOrgDayStatistics::getDayApplyCount).reversed()).map(statistics -> {
                Map<String, Object> data = new HashMap<>();
                data.put("itemId", statistics.getItemId());
                data.put("itemName", statistics.getItemName());
                data.put("apply", statistics.getDayApplyCount());
                data.put("acceptDeal", statistics.getDayAcceptanceCount());
                return data;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getOrgItemAcceptHistoryStatistics(String itemId, String startTime, String endTime, String type, boolean isCurrent) throws Exception {
        String orgId = null;
        if (isCurrent) {
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                orgId = opuOmOrg.getOrgId();
            }
        }

        if (DateType.YESTERDAY.equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDay = DateUtils.getPreDateByDate(new Date());
            return getDayApplyDataByRegionOrgItem(null, orgId, itemId, preDay, preDay, rootOrgId);
        } else if (DateType.CUR_WEEK.equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date thisWeekMonday = DateUtils.getThisWeekMonday(new Date());
            Date thisWeekSunday = DateUtils.getThisWeekSunday(new Date());
            return getDayApplyDataByRegionOrgItem(null, orgId, itemId, thisWeekMonday, thisWeekSunday, rootOrgId);
        } else if (DateType.CUR_MONTH.equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date firstDay = DateUtils.firstDayOfMonth(new Date());
            Date lastDay = DateUtils.lastDayOfMonth(new Date());
            return getDayApplyDataByRegionOrgItem(null, orgId, itemId, firstDay, lastDay, rootOrgId);
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
            return getDayApplyDataByRegionOrgItem(null, orgId, itemId, startDate, endDate, rootOrgId);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getRegionOrgAcceptStatistics(String regionId, String startTime, String endTime, String type) throws Exception {
        if (StringUtils.isBlank(regionId)) {
            if (DateType.YESTERDAY.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                Date preDate = DateUtils.getPreDateByDate(new Date());
                String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");

                List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getRegionAcceptStatistics(preDateStr, preDateStr, rootOrgId);
                List<Map<String, Object>> allRegion = getRegionOrgData(null);

                Map<String, List<AeaAnaOrgDayStatistics>> collect = null;
                if (CollectionUtils.isNotEmpty(orgItemAcceptStatistics)) {
                    collect = orgItemAcceptStatistics.stream().collect(Collectors.groupingBy(AeaAnaOrgDayStatistics::getRegionId));
                }

                List<Map<String, Object>> result = new ArrayList<>();
                for (Map<String, Object> region : allRegion) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("regionId", region.get("regionId"));
                    data.put("regionName", region.get("regionName"));
                    data.put("orgId", "");
                    data.put("orgName", "");
                    data.put("apply", 0);
                    data.put("acceptDeal", 0);
                    if (collect != null && collect.get(region.get("regionId")) != null) {
                        List<AeaAnaOrgDayStatistics> statistics = collect.get(region.get("regionId"));
                        data.put("apply", statistics.get(0).getDayApplyCount());
                        data.put("acceptDeal", statistics.get(0).getDayAcceptanceCount());
                    }
                    result.add(data);
                }
                return result;
            } else if (DateType.CUR_WEEK.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
                int thisWeekNum = DateUtils.getThisWeekNum(new Date());

                List<AeaAnaOrgWeekStatistics> orgItemAcceptStatistics = aeaAnaOrgWeekStatisticsMapper.getRegionAcceptStatistics(thisYear, thisWeekNum, rootOrgId);
                List<Map<String, Object>> allRegion = getRegionOrgData(null);

                Map<String, List<AeaAnaOrgWeekStatistics>> collect = null;
                if (CollectionUtils.isNotEmpty(orgItemAcceptStatistics)) {
                    collect = orgItemAcceptStatistics.stream().collect(Collectors.groupingBy(AeaAnaOrgWeekStatistics::getRegionId));
                }

                List<Map<String, Object>> result = new ArrayList<>();
                for (Map<String, Object> region : allRegion) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("regionId", region.get("regionId"));
                    data.put("regionName", region.get("regionName"));
                    data.put("orgId", "");
                    data.put("orgName", "");
                    data.put("apply", 0);
                    data.put("acceptDeal", 0);
                    if (collect != null && collect.get(region.get("regionId")) != null) {
                        List<AeaAnaOrgWeekStatistics> statistics = collect.get(region.get("regionId"));
                        data.put("apply", statistics.get(0).getWeekApplyCount());
                        data.put("acceptDeal", statistics.get(0).getWeekAcceptanceCount());
                    }
                    result.add(data);
                }
                return result;
            } else if (DateType.CUR_MONTH.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                String thisMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");

                List<AeaAnaOrgMonthStatistics> orgItemAcceptStatistics = aeaAnaOrgMonthStatisticsMapper.getRegionAcceptStatistics(thisMonth, rootOrgId);
                List<Map<String, Object>> allRegion = getRegionOrgData(null);

                Map<String, List<AeaAnaOrgMonthStatistics>> collect = null;
                if (CollectionUtils.isNotEmpty(orgItemAcceptStatistics)) {
                    collect = orgItemAcceptStatistics.stream().collect(Collectors.groupingBy(AeaAnaOrgMonthStatistics::getRegionId));
                }

                List<Map<String, Object>> result = new ArrayList<>();
                for (Map<String, Object> region : allRegion) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("regionId", region.get("regionId"));
                    data.put("regionName", region.get("regionName"));
                    data.put("orgId", "");
                    data.put("orgName", "");
                    data.put("apply", 0);
                    data.put("acceptDeal", 0);
                    if (collect != null && collect.get(region.get("regionId")) != null) {
                        List<AeaAnaOrgMonthStatistics> statistics = collect.get(region.get("regionId"));
                        data.put("apply", statistics.get(0).getMonthApplyCount());
                        data.put("acceptDeal", statistics.get(0).getMonthAcceptanceCount());
                    }
                    result.add(data);
                }
                return result;
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();

                List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getRegionAcceptStatistics(startTime, endTime, rootOrgId);
                List<Map<String, Object>> allRegion = getRegionOrgData(null);

                Map<String, List<AeaAnaOrgDayStatistics>> collect = null;
                if (CollectionUtils.isNotEmpty(orgItemAcceptStatistics)) {
                    collect = orgItemAcceptStatistics.stream().collect(Collectors.groupingBy(AeaAnaOrgDayStatistics::getRegionId));
                }

                List<Map<String, Object>> result = new ArrayList<>();
                for (Map<String, Object> region : allRegion) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("regionId", region.get("regionId"));
                    data.put("regionName", region.get("regionName"));
                    data.put("orgId", "");
                    data.put("orgName", "");
                    data.put("apply", 0);
                    data.put("acceptDeal", 0);
                    if (collect != null && collect.get(region.get("regionId")) != null) {
                        List<AeaAnaOrgDayStatistics> statistics = collect.get(region.get("regionId"));
                        data.put("apply", statistics.get(0).getDayApplyCount());
                        data.put("acceptDeal", statistics.get(0).getDayAcceptanceCount());
                    }
                    result.add(data);
                }
                return result;
            }
        } else {
            if (DateType.YESTERDAY.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                Date preDate = DateUtils.getPreDateByDate(new Date());
                String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");

                List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getOrgAcceptStatistics(regionId, preDateStr, preDateStr, rootOrgId);
                List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().map(statistics -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", statistics.getOrgId());
                    data.put("orgName", statistics.getOrgName());
                    data.put("regionId", "");
                    data.put("regionName", "");
                    data.put("apply", statistics.getDayApplyCount());
                    data.put("acceptDeal", statistics.getDayAcceptanceCount());
                    return data;
                }).collect(Collectors.toList());
                return collect;
            } else if (DateType.CUR_WEEK.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
                int thisWeekNum = DateUtils.getThisWeekNum(new Date());

                List<AeaAnaOrgWeekStatistics> orgItemAcceptStatistics = aeaAnaOrgWeekStatisticsMapper.getOrgAcceptStatistics(regionId, thisYear, thisWeekNum, rootOrgId);
                List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().map(statistics -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", statistics.getOrgId());
                    data.put("orgName", statistics.getOrgName());
                    data.put("regionId", "");
                    data.put("regionName", "");
                    data.put("apply", statistics.getWeekApplyCount());
                    data.put("acceptDeal", statistics.getWeekAcceptanceCount());
                    return data;
                }).collect(Collectors.toList());
                return collect;
            } else if (DateType.CUR_MONTH.equals(type)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                String thisMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");

                List<AeaAnaOrgMonthStatistics> orgItemAcceptStatistics = aeaAnaOrgMonthStatisticsMapper.getOrgAcceptStatistics(regionId, thisMonth, rootOrgId);
                List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().map(statistics -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", statistics.getOrgId());
                    data.put("orgName", statistics.getOrgName());
                    data.put("regionId", "");
                    data.put("regionName", "");
                    data.put("apply", statistics.getMonthApplyCount());
                    data.put("acceptDeal", statistics.getMonthAcceptanceCount());
                    return data;
                }).collect(Collectors.toList());
                return collect;
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                String rootOrgId = SecurityContext.getCurrentOrgId();

                List<AeaAnaOrgDayStatistics> orgItemAcceptStatistics = aeaAnaOrgDayStatisticsMapper.getOrgAcceptStatistics(regionId, startTime, endTime, rootOrgId);
                List<Map<String, Object>> collect = orgItemAcceptStatistics.stream().map(statistics -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", statistics.getOrgId());
                    data.put("orgName", statistics.getOrgName());
                    data.put("regionId", "");
                    data.put("regionName", "");
                    data.put("apply", statistics.getDayApplyCount());
                    data.put("acceptDeal", statistics.getDayAcceptanceCount());
                    return data;
                }).collect(Collectors.toList());
                return collect;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getRegionOrgAcceptHistoryStatistics(String regionId, String orgId, String startTime, String endTime, String type) throws Exception {
        if (DateType.YESTERDAY.equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDay = DateUtils.getPreDateByDate(new Date());
            return getDayApplyDataByRegionOrgItem(regionId, orgId, null, preDay, preDay, rootOrgId);
        } else if (DateType.CUR_WEEK.equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date thisWeekMonday = DateUtils.getThisWeekMonday(new Date());
            Date thisWeekSunday = DateUtils.getThisWeekSunday(new Date());
            return getDayApplyDataByRegionOrgItem(regionId, orgId, null, thisWeekMonday, thisWeekSunday, rootOrgId);
        } else if (DateType.CUR_MONTH.equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date firstDay = DateUtils.firstDayOfMonth(new Date());
            Date lastDay = DateUtils.lastDayOfMonth(new Date());
            return getDayApplyDataByRegionOrgItem(regionId, orgId, null, firstDay, lastDay, rootOrgId);
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
            return getDayApplyDataByRegionOrgItem(regionId, orgId, null, startDate, endDate, rootOrgId);
        }
        return null;
    }

    private List<Map<String, Object>> getDayApplyDataByRegionOrgItem(String regionId, String orgId, String itemId, Date startDate, Date endDate, String rootOrgId) throws Exception {
        String startTime = DateUtils.convertDateToString(startDate, "yyyy-MM-dd");
        String endTime = DateUtils.convertDateToString(endDate, "yyyy-MM-dd");

        long millisecond = endDate.getTime() - startDate.getTime();
        int day = (int) (millisecond / DateUtils.MILLIS_PER_DAY);

        //日期统计
        List<OrgItemStatisticsVo> winAcceptStatisticsByDay = aeaAnaOrgDayStatisticsMapper.getAcceptStatisticsByDay(regionId, orgId, itemId, startTime, endTime, rootOrgId);
        if (winAcceptStatisticsByDay == null) {
            winAcceptStatisticsByDay = new ArrayList<>();
        }
        Map<String, List<OrgItemStatisticsVo>> collect = winAcceptStatisticsByDay.stream().collect(Collectors.groupingBy(OrgItemStatisticsVo::getDay));
        for (int i = 0; i <= day; i++) {
            Date date = DateUtils.addDays(startDate, i);
            String dateStr = DateUtils.convertDateToString(date, "yyyy-MM-dd");
            if (collect.get(dateStr) == null) {
                OrgItemStatisticsVo vo = new OrgItemStatisticsVo();
                vo.setApply(0L);
                vo.setAcceptDeal(0L);
                vo.setDay(dateStr);
                winAcceptStatisticsByDay.add(vo);

            }
        }
        List<Map<String, Object>> dayDatas = winAcceptStatisticsByDay.stream().sorted(Comparator.comparing(OrgItemStatisticsVo::getDay)).map(vo -> {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", vo.getDay());
            dayData.put("apply", vo.getApply());
            dayData.put("acceptDeal", vo.getAcceptDeal());
            return dayData;
        }).collect(Collectors.toList());
        return dayDatas;
    }

    @Override
    public List<BscDicRegion> getCurrentRegionList() throws Exception{

        List<BscDicRegion> childrenRegion = new ArrayList<>();
        List<BscDicRegion> tmp = getChildRegion();
        if(tmp.size()==0){
            throw new Exception("region爲空");
        }

        //拼接 下拉列表返回数据
        BscDicRegion top = tmp.get(0);
        BscDicRegion cityOrg = new BscDicRegion();
        String id =  top.getRegionId();
        cityOrg.setRegionId(id);
        cityOrg.setRegionName("市级部门");
        top.setRegionId("");
        childrenRegion.add(top);
        childrenRegion.add(cityOrg);
        tmp.remove(0);
        childrenRegion.addAll(tmp);

        return childrenRegion;
    }

    /**
     * 获取当前城市及下级行政区划列表，市级和县级
     * @return
     * @throws Exception
     */
    private List<BscDicRegion> getChildRegion() throws Exception{
        BscDicRegion region = efficiencySupervisionMapper.getCurrentRegionByUserId(SecurityContext.getCurrentUserId());
        String regionSeq = region.getRegionSeq();
        if(StringUtils.isBlank(regionSeq)){
            throw new Exception("区域regionSeq查询异常“");
        }

        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for(Character c : regionSeq.toCharArray()){
            sb.append(c);
            if(c=='.'){
                idx++;
            }
            if(idx==5){break;}
        }

        regionSeq = regionSeq.substring(0,sb.length());


        List<BscDicRegion> tmp = efficiencySupervisionMapper.getChildrenReggion(regionSeq, null);
        return tmp;
    }

    @Override
    public Map<String, Object> getOrgReceiveStatistics(String startDate, String endDate, String type, String regionId) throws Exception {
        if(StringUtils.isBlank(regionId)){
            regionId = null;
        }

        String rootOrgId = SecurityContext.getCurrentOrgId();
        String startTime = null,endTime = null;
        List<ItemDetailFormVo> list = new ArrayList<>();
        if("D".equals(type)){
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startDate = yesterday;
            endDate = yesterday ;
            startTime += yesterday+" 00:00:00";
            endTime += yesterday+" 23:59:59";
            list = aeaAnaOrgDayStatisticsMapper.getRegionDayStatistics(startDate,endDate,regionId,rootOrgId);
        }else if("W".equals(type)){
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            startTime = DateUtils.firstDayOfWeekStr();
            endTime = DateUtils.lastDayOfWeekStr();
            list = aeaAnaOrgWeekStatisticsMapper.getRegionWeekStatistics(thisYear,thisWeekNum,thisWeekNum,regionId,rootOrgId);
        }else if("M".equals(type)){
            String yearMonth = DateUtils.convertDateToString(new Date(), "yyyy-MM");
            startTime = DateUtils.firstDayOfMonthStr();
            endTime = DateUtils.lastDayOfMonthStr();
            list = aeaAnaOrgMonthStatisticsMapper.getRegionMonthStatistics(yearMonth,yearMonth,regionId,rootOrgId);
        }else{
            if (!DateUtils.checkTimeParam(startDate, endDate, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数欧问题。。。");
            }
            LocalDate _endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(_endDate)) {//结束日期只能统计到昨天
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endDate = yesterday ;
            }
            startTime = startDate + " 00:00:00";
            endTime = endDate +" 23:59:59";
            list = aeaAnaOrgDayStatisticsMapper.getRegionDayStatistics(startDate,endDate,regionId,rootOrgId);
        }

        //另外查询材料补证的的
        List<ItemDetailFormVo> cortMatlist = efficiencySupervisionMapper.getOrgReceiveStateStatistics(regionId,"6",startTime,endTime,rootOrgId);


        //封装已有的数据
        Map<String,Object> result = new HashMap<>();
        if(cortMatlist.size()>list.size()){
            result= packageHadData(cortMatlist,list,regionId,true);
        }else{
            result=  packageHadData(list,cortMatlist,regionId,true);
        }
        return result;


    }


    /**
     *
     * @param list
     * @param cortMatlist
     * @param regionId
     * @param showAll 是否显示所有记录
     *
     * @return
     * @throws Exception
     */
    private Map<String,Object> packageHadData(List<ItemDetailFormVo> list, List<ItemDetailFormVo> cortMatlist, String regionId, boolean showAll) throws Exception{
        Map<String,Object> result = new HashMap<>();
        List<List<Object>> dataList= new ArrayList<>();
        int total = 0;
        List<Map<String,Object>> leftlist = new ArrayList<>();
        boolean bol = StringUtils.isBlank(regionId);
        //
        List<ItemDetailFormVo> _tmp = new ArrayList<>();
        _tmp.addAll(list);

        for(int i=0,len = cortMatlist.size();i<len;i++){
            ItemDetailFormVo out = cortMatlist.get(i);
            String name1 = bol?out.getRegionName():out.getOrgName();
            boolean had = false;
            for(int j=0,len2=list.size();j<len2;j++){
                ItemDetailFormVo in = list.get(j);
                String name2 = bol?in.getRegionName():in.getOrgName();
                if(name1.equals(name2)){
                    had = true;
                    ItemDetailFormVo tmpVo = _tmp.get(j);
                    tmpVo.setCorrectMaterialCount(out.getCorrectMaterialCount());
                    break;
                }
            }
            if(!had){
                _tmp.add(out);
            }
        }
        //
            for(int k=0,len =_tmp.size();k<len;k++){
                ItemDetailFormVo obj = _tmp.get(k);
                String name1 = bol?obj.getRegionName():obj.getOrgName();
                total+= obj.getReceiptCount();
                dataList.add(Arrays.asList(name1,obj.getAcceptCount(),obj.getCorrectMaterialCount(),obj.getNotAcceptCount()));
                Map<String,Object> tmp = new HashMap<>();
                tmp.put("name",name1);tmp.put("value",obj.getReceiptCount());
                leftlist.add(tmp);

            }

        Map<String,Object> leftData = new HashMap<>();
        if(showAll){
            List<String> showData = getShowData(regionId);
            List<Map<String,Object>> _leftList = new ArrayList<>();
            List<List<Object>> _rightList = new ArrayList<>();
            for(String name :showData){
                boolean had = false;
                boolean had2= false;
                for(Map<String,Object> map :leftlist){
                    if(name.equals(map.get("name"))){
                        had = true;
                        _leftList.add(map);
                        break;
                    }
                }
                if(!had){
                    _leftList.add(new HashMap<String,Object>(){{put("name",name);put("value",0);}});
                }
                for(List<Object> lstr :dataList){
                    if(name.equals(lstr.get(0))){
                        had2 = true;
                        _rightList.add(lstr);
                        break;
                    }
                }
                if(!had2){
                    _rightList.add(Arrays.asList(name,0,0,0));
                }
            }
            leftData.put("data",_leftList);
            result.put("rightData",_rightList);
        }
        else{
            leftData.put("data",leftlist);
            result.put("rightData",dataList);
        }
        leftData.put("total",total);
        result.put("leftData",leftData);

        return result;
    }

    private List<String> getShowData(String regionId) throws Exception{
        List<String> list = new ArrayList<>();
        if(StringUtils.isBlank(regionId)){
            List<BscDicRegion> childRegion = getChildRegion();
            if(childRegion.size()==0) throw new Exception("查询区域出现异常");
            list = childRegion.stream().map(BscDicRegion::getRegionName).collect(Collectors.toList());
        }else{
            OpuOmOrg search = new OpuOmOrg();
            BscDicRegion currentRegion = bscDicRegionMapper.getBscDicRegionById(regionId);
            if (currentRegion.getRegionalLevel() == 4) {
                search.setOrgLevel(2);//市级部门
            } else if (currentRegion.getRegionalLevel() == 5) {
                search.setOrgLevel(3);
            }
            search.setOrgProperty("d");
            search.setIsPublic("1");
            search.setIsActive("1");
            search.setIsLeaf("1");
            search.setRegionId(regionId);
            //原来的opuomOrgmapper里面缺失了regionid 字段所以写到这里
            List<OpuOmOrg> orgList = efficiencySupervisionMapper.listOpuOmOrg(search);
            if(orgList.size()==0) throw new Exception("该区域下没有配置所属部门。");
            list = orgList.stream().map(OpuOmOrg::getOrgName).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public Map<String,Object> getOrgReceiveLimitTimeStatistics(String startTime, String endTime, String type, String regionId) throws Exception {

       List<List<Object>> result = new ArrayList<>();
        if ("D".equals(type)) {
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startTime = yesterday + " 00:00:00";
            endTime = yesterday + " 23:59:59";
        } else if ("W".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.getThisWeekMonday(new Date()), "yyyy-MM-dd") + " 00:00:00";
            endTime = DateUtils.convertDateToString(DateUtils.getThisWeekSunday(new Date()), "yyyy-MM-dd") + " 23:59:59";

        } else if ("M".equals(type)) {
            startTime = DateUtils.convertDateToString(DateUtils.firstDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.convertDateToString(DateUtils.lastDayOfMonth(new Date()), "yyyy-MM-dd HH:mm:ss");

        } else {
            if (!DateUtils.checkTimeParam(startTime, endTime, "yyyy-MM-dd")) {
                throw new Exception("传入的时间参数有问题。。。");
            }
            LocalDate endDate = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!LocalDate.now().isAfter(endDate)) {//结束日期只能统计到昨天
                startTime  = startTime + " 00:00:00";
                String yesterday = DateUtils.getYesterdayByFormat("yyyy-MM-dd");
                endTime = yesterday + " 23:59:59";
            }
        }

        List<ItemDetailFormVo> list = efficiencySupervisionMapper.getOrgReceiveLimitTimeStatistics(startTime,endTime,regionId,SecurityContext.getCurrentOrgId());

        boolean bol = StringUtils.isBlank(regionId);//不为空的事项显示部门的，否则显示区县的

        int allNormal =0,allWarn =0,allOverdue =0,total=0;
        if(list.size()>0){
            Map<String, List<ItemDetailFormVo>> collect;
            if(bol){
                collect = list.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getRegionId));
            }else {
                collect = list.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getOrgId));
            }
            Set<Map.Entry<String, List<ItemDetailFormVo>>> entries = collect.entrySet();
            for(Map.Entry<String, List<ItemDetailFormVo>> entry: entries){
                List<ItemDetailFormVo> value = entry.getValue();
                ItemDetailFormVo obj = value.get(0);
                int normal=0,warn=0,overdue=0;
                String name = "";
                for(ItemDetailFormVo vo :value){
                    if("1".equals(vo.getInstState())){
                        normal += vo.getReceiptCount();
                        allNormal += vo.getReceiptCount();
                    }else if("2".equals(vo.getInstState())){
                        warn += vo.getReceiptCount();
                        allWarn += vo.getReceiptCount();
                    }else if("3".equals(vo.getInstState())){
                        overdue += vo.getReceiptCount();
                        allOverdue += vo.getReceiptCount();
                    }
                }
                 /*if(bol){
                     name = obj.getRegionName();
                 }else {
                     name = obj.getOrgName();
                 }*/
                name = bol ? obj.getRegionName():obj.getOrgName();

                List<Object> record = Arrays.asList(name,normal,warn,overdue);
                 result.add(record);
            }
        }

        //显示全部部门或区县数据
        List<List<Object>> showResult = new ArrayList<>();
        List<String> showData = getShowData(regionId);
        for(int i=0,len = showData.size();i < len; i++){
            String _name = showData.get(i);
            boolean had = false;
            for(int j=0,len2 = result.size();j<len2; j++){
                if(_name.equals(result.get(j).get(0))){
                    had = true;
                    showResult.add(result.get(j));
                    break;
                }
            }
            if(!had){
                List<Object> tmp = Arrays.asList(_name,0,0,0);
                showResult.add(tmp);
            }
        }

        //封装左右两个图的数据
        Map<String,Object> rMap = new HashMap<>();
        Map<String,Object> left = new HashMap<>();

        List<Map<String,Object>> leftDataList = new ArrayList<>();
        Map<String,Object> m1 = new HashMap<>();
        m1.put("name","正常");m1.put("value",allNormal);
        Map<String,Object> m2 = new HashMap<>();
        m2.put("name","预警");m2.put("value",allWarn);
        Map<String,Object> m3 = new HashMap<>();
        m3.put("name","逾期");m3.put("value",allOverdue);
        total = allNormal+allWarn+allOverdue;
        left.put("total",total);
        leftDataList.add(m1);
        leftDataList.add(m2);
        leftDataList.add(m3);
        left.put("data",leftDataList);
        rMap.put("leftData",left);
        rMap.put("rightData",showResult);
        return rMap;
    }

    private List<Map<String, Object>> getRegionOrgData(String regionId) throws Exception {
        List<Map<String, Object>> list = null;
        if (StringUtils.isBlank(regionId)) {
            List<BscDicRegion> childRegion = getChildRegion();
            if (childRegion.size() == 0) throw new Exception("查询区域出现异常");
            list = childRegion.stream().map(data -> {
                Map<String, Object> region = new HashMap<>();
                region.put("regionId", data.getRegionId());
                region.put("regionName", data.getRegionName());
                return region;
            }).collect(Collectors.toList());
        } else {
            OpuOmOrg search = new OpuOmOrg();
            BscDicRegion currentRegion = bscDicRegionMapper.getBscDicRegionById(regionId);
            if (currentRegion.getRegionalLevel() == 4) {
                //市级部门
                search.setOrgLevel(2);
            } else if (currentRegion.getRegionalLevel() == 5) {
                search.setOrgLevel(3);
            }
            search.setOrgProperty("d");
            search.setIsPublic("1");
            search.setIsActive("1");
            search.setIsLeaf("1");
            search.setRegionId(regionId);
            //原来的opuomOrgmapper里面缺失了regionid 字段所以写到这里
            List<OpuOmOrg> orgList = efficiencySupervisionMapper.listOpuOmOrg(search);
            if (orgList.size() == 0) throw new Exception("该区域下没有配置所属部门");
            list = orgList.stream().map(data -> {
                Map<String, Object> org = new HashMap<>();
                org.put("orgId", data.getOrgId());
                org.put("orgName", data.getOrgName());
                return org;
            }).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<UseTimeStatisticsVo> getItemUseTimeStatistics(String startTime, String endTime, String type, String regionId, String orgId) throws Exception {

        if (DateType.YESTERDAY.equals(type)) {
            Date preDate = DateUtils.getPreDateByDate(new Date());
            String preDateStr = DateUtils.convertDateToString(preDate, "yyyy-MM-dd");
            String preDateStart = preDateStr + " 00:00:00";
            String preDateEnd = preDateStr + " 23:59:59";
            return getItemUseTimeStatisticsByCondition(preDateStart, preDateEnd, regionId, orgId, SecurityContext.getCurrentOrgId());
        } else if (DateType.CUR_WEEK.equals(type)) {
            Date thisWeekMonday = DateUtils.getThisWeekMonday(new Date());
            Date thisWeekSunday = DateUtils.getThisWeekSunday(new Date());
            String monday = DateUtils.convertDateToString(thisWeekMonday, "yyyy-MM-dd") + " 00:00:00";
            String sunday = DateUtils.convertDateToString(thisWeekSunday, "yyyy-MM-dd") + " 23:59:59";
            return getItemUseTimeStatisticsByCondition(monday, sunday, regionId, orgId, SecurityContext.getCurrentOrgId());
        } else if (DateType.CUR_MONTH.equals(type)) {
            Date firstDay = DateUtils.firstDayOfMonth(new Date());
            Date lastDay = DateUtils.lastDayOfMonth(new Date());
            String firstDayStart = DateUtils.convertDateToString(firstDay, "yyyy-MM-dd") + " 00:00:00";
            String lastDayEnd = DateUtils.convertDateToString(lastDay, "yyyy-MM-dd") + " 23:59:59";
            return getItemUseTimeStatisticsByCondition(firstDayStart, lastDayEnd, regionId, orgId, SecurityContext.getCurrentOrgId());
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            return getItemUseTimeStatisticsByCondition(startTime, endTime, regionId, orgId, SecurityContext.getCurrentOrgId());
        }

        return null;
    }

    private List<UseTimeStatisticsVo> getItemUseTimeStatisticsByCondition(String startTime, String endTime, String regionId, String orgId, String rootOrgId) throws Exception {
        if (StringUtils.isBlank(regionId) && StringUtils.isBlank(orgId)) {
            //查询各地区用时
            List<UseTimeStatisticsVo> useTimeData = efficiencySupervisionMapper.getItemUseTimeByRegion(startTime, endTime, rootOrgId);
            List<Map<String, Object>> allRegion = getRegionOrgData(null);

            Map<String, List<UseTimeStatisticsVo>> collect = null;
            if (CollectionUtils.isNotEmpty(useTimeData)) {
                collect = useTimeData.stream().collect(Collectors.groupingBy(UseTimeStatisticsVo::getRegionId));
            }

            List<UseTimeStatisticsVo> result = new ArrayList<>();
            for (Map<String, Object> region : allRegion) {
                UseTimeStatisticsVo vo = new UseTimeStatisticsVo();
                vo.setRegionId(region.get("regionId").toString());
                vo.setRegionName(region.get("regionName").toString());
                vo.setMaxUseTime(0.0);
                vo.setMinUseTime(0.0);
                vo.setAvgUseTime(0.0);
                if (collect != null && collect.get(region.get("regionId")) != null) {
                    List<UseTimeStatisticsVo> useTimeStatisticsVos = collect.get(region.get("regionId"));
                    if (CollectionUtils.isNotEmpty(useTimeStatisticsVos)) {
                        vo.setMaxUseTime(useTimeStatisticsVos.get(0).getMaxUseTime());
                        vo.setMinUseTime(useTimeStatisticsVos.get(0).getMinUseTime());
                        vo.setAvgUseTime(useTimeStatisticsVos.get(0).getAvgUseTime());
                    }
                }
                result.add(vo);
            }
            return result;
        } else if (StringUtils.isNotBlank(regionId)) {
            //查询某部门各部门用时
            List<UseTimeStatisticsVo> useTimeData = efficiencySupervisionMapper.getItemUseTimeByOrg(startTime, endTime, regionId, rootOrgId);
            List<Map<String, Object>> allOrg = getRegionOrgData(regionId);

            Map<String, List<UseTimeStatisticsVo>> collect = null;
            if (CollectionUtils.isNotEmpty(useTimeData)) {
                collect = useTimeData.stream().collect(Collectors.groupingBy(UseTimeStatisticsVo::getOrgId));
            }

            List<UseTimeStatisticsVo> result = new ArrayList<>();
            for (Map<String, Object> org : allOrg) {
                UseTimeStatisticsVo vo = new UseTimeStatisticsVo();
                vo.setOrgId(org.get("orgId").toString());
                vo.setOrgName(org.get("orgName").toString());
                vo.setMaxUseTime(0.0);
                vo.setMinUseTime(0.0);
                vo.setAvgUseTime(0.0);
                if (collect != null && collect.get(org.get("orgId")) != null) {
                    List<UseTimeStatisticsVo> useTimeStatisticsVos = collect.get(org.get("orgId"));
                    if (CollectionUtils.isNotEmpty(useTimeStatisticsVos)) {
                        vo.setMaxUseTime(useTimeStatisticsVos.get(0).getMaxUseTime());
                        vo.setMinUseTime(useTimeStatisticsVos.get(0).getMinUseTime());
                        vo.setAvgUseTime(useTimeStatisticsVos.get(0).getAvgUseTime());
                    }
                }
                result.add(vo);
            }
            return result;
        } else if (StringUtils.isNotBlank(orgId)) {
            //查询某部门各事项用时
            List<UseTimeStatisticsVo> useTimeData = efficiencySupervisionMapper.getItemUseTimeByItem(startTime, endTime, orgId, rootOrgId);
            List<AeaItemBasic> items = aeaItemBasicMapper.listAeaItemBasicByOrgId(orgId);

            Map<String, List<UseTimeStatisticsVo>> collect = null;
            if (CollectionUtils.isNotEmpty(useTimeData)) {
                collect = useTimeData.stream().collect(Collectors.groupingBy(UseTimeStatisticsVo::getItemId));
            }


            List<UseTimeStatisticsVo> result = new ArrayList<>();
            for (AeaItemBasic item : items) {
                UseTimeStatisticsVo vo = new UseTimeStatisticsVo();
                vo.setItemId(item.getItemId());
                vo.setItemName(item.getItemName());
                vo.setMaxUseTime(0.0);
                vo.setMinUseTime(0.0);
                vo.setAvgUseTime(0.0);
                if (collect != null && collect.get(item.getItemId()) != null) {
                    List<UseTimeStatisticsVo> useTimeStatisticsVos = collect.get(item.getItemId());
                    if (CollectionUtils.isNotEmpty(useTimeStatisticsVos)) {
                        vo.setMaxUseTime(useTimeStatisticsVos.get(0).getMaxUseTime());
                        vo.setMinUseTime(useTimeStatisticsVos.get(0).getMinUseTime());
                        vo.setAvgUseTime(useTimeStatisticsVos.get(0).getAvgUseTime());
                    }
                }
                result.add(vo);
            }
            return result;
        }
        return null;
    }
}
