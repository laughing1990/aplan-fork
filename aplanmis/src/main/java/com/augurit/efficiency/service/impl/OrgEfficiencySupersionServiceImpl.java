package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
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
import com.augurit.efficiency.service.OrgEfficiencySupersionService;
import com.augurit.efficiency.utils.CalculateUtil;
import lombok.extern.slf4j.Slf4j;
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
        res.removeIf(it -> (it.getNotAcceptRate() == null && it.getOverdueRate() == null));
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
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        for (int i = 0, len = orgInfoList.size(); i < len; i++) {
            AeaItemBasic basic = orgInfoList.get(i);
            String orgId = basic.getOrgId();
            String orgName = basic.getOrgName();
            ItemDetailFormVo vo = new ItemDetailFormVo();
            vo.setOrgId(orgId);
            vo.setOrgName(orgName);
            //统计startTime至endTime（不包含今天）
            List<AeaAnaOrgDayStatistics> list = aeaAnaOrgDayStatisticsMapper.queryAeaAnaOrgDayStatisticsGreaterThanStartTime(orgId, null, startTime, endTime);
            if (list != null && list.size() > 0) {
                for (int k = 0, length = list.size(); k < length; k++) {
                    AeaAnaOrgDayStatistics orgDayStatistics = list.get(k);
                    covertItemDetailFormVo(vo, orgDayStatistics);
                }
            }
            if (StringUtils.isNotBlank(vo.getOrgName())) {
                setItemDetailFormVorRate(vo);
                result.add(vo);
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
            Integer receiptCount = vo.getReceiptCount();
            Integer acceptCount = vo.getAcceptCount();
            Integer notAcceptCount = vo.getNotAcceptCount();
            Integer completedCount = vo.getCompletedCount();
            Integer overdueCount = vo.getOverdueCount();
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
     * @param type D表示今日，W表示本周，M表示本月
     * @return
     */
    public List<ItemDetailFormVo> queryOrgHandleItemStatisticsToNow(String type) {
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
            if ("W".equals(type)) {
                int weekNum = DateUtils.getThisWeekNum(new Date());
                AeaAnaOrgWeekStatistics weekStatistics = aeaAnaOrgWeekStatisticsMapper.sumWeekStatistics(weekNum, rootOrgId, orgId);
                if (weekStatistics != null) {
                    vo.setReceiptCount(weekStatistics.getWeekApplyCount());
                    vo.setAcceptCount(weekStatistics.getWeekAcceptanceCount());
                    vo.setNotAcceptCount(weekStatistics.getWeekOutScopeCount());
                    vo.setCompletedCount(weekStatistics.getWeekCompletedCount());
                    vo.setOverdueCount(weekStatistics.getWeekOverTimeCount());
                }
            } else if ("M".equals(type)) {
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

        if (isCurrent == true) {
            List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
            if (!opuOmOrgs.isEmpty()) {
                OpuOmOrg opuOmOrg = opuOmOrgs.get(0);
                orgId = opuOmOrg.getOrgId();
            }
        }

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<ItemDetailFormVo> list = new ArrayList<>();
        if("D".equals(type)){
            String yesterday = DateUtils.convertDateToString(DateUtils.getPreDateByDate(new Date()), "yyyy-MM-dd");
            startDate = yesterday;
            endDate = yesterday ;
            list = aeaAnaOrgDayStatisticsMapper.getOrgDayStatistics(startDate,endDate,orgId,rootOrgId);
        }else if("W".equals(type)){
            String thisYear = DateUtils.convertDateToString(new Date(), "yyyy");
            int thisWeekNum = DateUtils.getThisWeekNum(new Date());
            list = aeaAnaOrgWeekStatisticsMapper.getOrgWeekStatistics(thisYear,thisWeekNum,thisWeekNum,orgId,rootOrgId);
        }else if("M".equals(type)){
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

        if ("D".equals(type)) {//昨日日统计
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
        } else if ("W".equals(type)) {//本周统计
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
        } else if ("M".equals(type)) {//本月统计
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

        if ("D".equals(type)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date preDay = DateUtils.getPreDateByDate(new Date());
            return getDayApplyDataByOrgItem(orgId, itemId, preDay, preDay, rootOrgId);
        } else if ("W".equals(type)) {//本周统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date thisWeekMonday = DateUtils.getThisWeekMonday(new Date());
            Date thisWeekSunday = DateUtils.getThisWeekSunday(new Date());
            return getDayApplyDataByOrgItem(orgId, itemId, thisWeekMonday, thisWeekSunday, rootOrgId);
        } else if ("M".equals(type)) {//本月统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date firstDay = DateUtils.firstDayOfMonth(new Date());
            Date lastDay = DateUtils.lastDayOfMonth(new Date());
            return getDayApplyDataByOrgItem(orgId, itemId, firstDay, lastDay, rootOrgId);
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {//按时段统计
            String rootOrgId = SecurityContext.getCurrentOrgId();
            Date startDate = DateUtils.convertStringToDate(startTime, "yyyy-MM-dd");
            Date endDate = DateUtils.convertStringToDate(endTime, "yyyy-MM-dd");
            return getDayApplyDataByOrgItem(orgId, itemId, startDate, endDate, rootOrgId);
        }
        return null;
    }

    private List<Map<String, Object>> getDayApplyDataByOrgItem(String orgId, String itemId, Date startDate, Date endDate, String rootOrgId) throws Exception {
        String startTime = DateUtils.convertDateToString(startDate, "yyyy-MM-dd") + " 00:00:00";
        String endTime = DateUtils.convertDateToString(endDate, "yyyy-MM-dd") + " 23:59:59";

        long millisecond = endDate.getTime() - startDate.getTime();
        int day = (int) (millisecond / DateUtils.MILLIS_PER_DAY);

        //日期统计
        List<OrgItemStatisticsVo> winAcceptStatisticsByDay = aeaAnaOrgDayStatisticsMapper.getOrgItemAcceptStatisticsByDay(orgId, itemId, startTime, endTime, rootOrgId);
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

}
