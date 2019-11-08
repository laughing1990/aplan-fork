//package com.augurit.aplanmis.common.service.analyse.impl;
//
//import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
//import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
//import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
//import com.augurit.agcloud.framework.security.SecurityContext;
//import com.augurit.agcloud.framework.ui.result.ContentResultForm;
//import com.augurit.agcloud.framework.util.CollectionUtils;
//import com.augurit.agcloud.framework.util.StringUtils;
//import com.augurit.aplanmis.common.constants.ApplyState;
//import com.augurit.aplanmis.common.constants.DeletedStatus;
//import com.augurit.aplanmis.common.constants.ItemStatus;
//import com.augurit.aplanmis.common.constants.StandardStageCode;
//import com.augurit.aplanmis.common.domain.*;
//import com.augurit.aplanmis.common.mapper.*;
//import com.augurit.aplanmis.common.service.analyse.EfficiencySupervisionService;
//import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
//import com.augurit.aplanmis.common.utils.DateUtils;
//import com.augurit.aplanmis.common.vo.analyse.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service("efficiencySupervisionServiceImpl2")
//@Transactional(rollbackFor = RuntimeException.class)
//public class EfficiencySupervisionServiceImpl implements EfficiencySupervisionService{
//    private static Logger logger = LoggerFactory.getLogger(EfficiencySupervisionServiceImpl.class);
//
//    @Autowired
//    private AeaHiIteminstMapper aeaHiIteminstMapper;
//    @Autowired
//    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
//    @Autowired
//    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
//    @Autowired
//    private ActStoTimeruleInstService actStoTimeruleInstService;
//    @Autowired
//    private AeaParThemeMapper aeaParThemeMapper;
//    @Autowired
//    private AeaParThemeVerMapper aeaParThemeVerMapper;
//    @Autowired
//    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
//    @Autowired
//    private EfficiencySupervisionMapper efficiencySupervisionMapper;
//    @Autowired
//    private BscDicCodeItemService bscDicCodeItemService;
//    @Autowired
//    AeaServiceWindowUserMapper aeaServiceWindowUserMapper;
//
//
//    /**
//     * 办件数统计
//     * @return
//     */
//    @Override
//    public ItemStatusCountVo listItemStatusCount(){
//        ItemStatusCountVo vo = new ItemStatusCountVo();
//        AeaHiIteminst search = new AeaHiIteminst();
//        //办结数
//        queryCompletedCount(vo,search);
//        //待补正数
//        queryCorrectStartCount(vo,search);
//        //补正待确认数
//        queryCorrectEndCount(vo,search);
//        //已受理数
//        queryAcceptedCount(vo,search);
//        //预警数
//        queryWarningCount(vo,search);
//        //逾期数
//        queryOverTimeCount(vo,search);
//        return vo;
//    }
//
//    /**
//     * 申报统计
//     * @return
//     */
//    @Override
//    public ContentResultForm queryApplyStatistics(){
//        ContentResultForm resultForm = new ContentResultForm(false);
//        List<ApplyStatisticsVo> list = new ArrayList<>();
//        try {
//            //查询网上待预审
//            ApplyStatisticsVo v1 = queryWangshangdaiyushen();
//            //查询申报待补全
//            ApplyStatisticsVo v2 = queryShenbaodaibuquan();
//            //查询申报已补全
//            ApplyStatisticsVo v3 = queryShenbaoyibuquan();
//            //查询不予受理
//            ApplyStatisticsVo v4 = queryBuyushouli();
//            //查询申报时限预警
//            ApplyStatisticsVo v5 = queryShenbaoxianshiyujing();
//            //查询申报时限逾期
//            ApplyStatisticsVo v6 = queryShenbaoxianshiyuqi();
//            list.add(v1);
//            list.add(v2);
//            list.add(v3);
//            list.add(v4);
//            list.add(v5);
//            list.add(v6);
//            resultForm.setSuccess(true);
//            resultForm.setContent(list);
//        }catch (Exception e){
//            e.printStackTrace();
//            resultForm.setMessage(e.getMessage());
//            return resultForm;
//        }
//        return resultForm;
//    }
//
//    /**
//     * 主题申报统计
//     * 根据主题ID，找到所有的主题版本号，
//     * 找到阶段实例表对应主题版本的数量 + 串联实例表的并行申报事项对应的阶段的主题版本号的数量。
//     * 阶段实例和串联实例对应的申请实例的创建时间在查询时间区间内
//     * @return
//     */
//    @Override
//    public ContentResultForm queryThemeApplyStatistics(String startTime, String endTime){
//        ContentResultForm resultForm = new ContentResultForm(false);
//        boolean boo = checkDate(startTime,endTime);
//        if(boo){
//            StatisticsVo content = getStatisticsVo(startTime, endTime);
//            resultForm.setSuccess(true);
//            resultForm.setContent(content);
//        }else {
//            resultForm.setMessage("时间参数不正确");
//        }
//        return resultForm;
//    }
//
//    private StatisticsVo getStatisticsVo(String startTime, String endTime) {
//        //找到全部主题
//        AeaParTheme search = new AeaParTheme();
//        search.setIsActive("1");
//        search.setRootOrgId(SecurityContext.getCurrentOrgId());
//        List<AeaParTheme> themes = aeaParThemeMapper.listAeaParTheme(search);
//        StatisticsVo content = new StatisticsVo();
//        int count = 0;
//        List<ApplyStatisticsVo> applyStatistics = new ArrayList<>();
//        for(int i=0,len=themes.size();i<len;i++){
//            AeaParTheme theme = themes.get(i);
//            List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByThemeIds("'"+theme.getThemeId()+"'");
//            String queryThemeVerIds = getqueryThemeVerIds(themeVers);
//            //对应时间段内申报的所属主题阶段实例数量
//            List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryThemeAeaHiParStageinsts(queryThemeVerIds,SecurityContext.getCurrentOrgId() ,startTime, endTime,null);
//            //查询对应时间段内申报的所属主题并行推进事项实例数量
//            List<AeaHiSeriesinst> seriesinsts = aeaHiSeriesinstMapper.queryThemeAeaHiSeriesinsts(queryThemeVerIds,SecurityContext.getCurrentOrgId(), startTime, endTime,null);
//            int num = stageinsts.size() + seriesinsts.size();
//            ApplyStatisticsVo v1 = new ApplyStatisticsVo();
//            v1.setName(theme.getThemeName());
//            v1.setCount(num);
//            v1.setType(theme.getThemeType());
//            applyStatistics.add(v1);
//            count += num;
//        }
//        //计算百分比
//        calcuPercent(count, applyStatistics);
//        content.setTotal(count);
//        content.setApplyStatistics(applyStatistics);
//        content.setStartTime(startTime);
//        content.setEndTime(endTime);
//        return content;
//    }
//
//    /**
//     * 主题分类申报统计
//     * 在主题申报统计的基础上再进行一层分类统计
//     * 主题分类数据字典：THEME_TYPE
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    @Override
//    public ContentResultForm queryThemeTypeApplyStatistics(String startTime, String endTime) {
//        ContentResultForm resultForm = new ContentResultForm(false);
//        StatisticsVo statistics = this.getStatisticsVo(startTime, endTime);
//        try {
//            StatisticsVo content = new StatisticsVo();
//            List<ApplyStatisticsVo> applyStatistics = statistics.getApplyStatistics();
//            List<BscDicCodeItem> themeType = bscDicCodeItemService.getActiveItemsByTypeCode("THEME_TYPE", SecurityContext.getCurrentOrgId());
//            List<ApplyStatisticsVo> result = new ArrayList<>();
//            int count = 0;
//            for(int k=0,len=themeType.size();k<len;k++){
//                BscDicCodeItem item = themeType.get(k);
//                String itemName = item.getItemName();
//                String type = item.getItemCode();
//                int num = 0;
//                for(int i=0,length=applyStatistics.size();i<length;i++){
//                    ApplyStatisticsVo statisticsVo = applyStatistics.get(i);
//                    if(type.equals(statisticsVo.getType())){
//                        num ++;
//                    }
//                }
//                count += num;
//                ApplyStatisticsVo vo = new ApplyStatisticsVo();
//                vo.setName(itemName);
//                vo.setCount(num);
//                result.add(vo);
//            }
//            calcuPercent(count, result);
//            content.setTotal(count);
//            content.setApplyStatistics(result);
//            content.setStartTime(startTime);
//            content.setEndTime(endTime);
//            resultForm.setSuccess(true);
//            resultForm.setContent(content);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return resultForm;
//    }
//
//    /**
//     * 阶段申报统计
//     * 根据四大阶段的标准编码StandardStageCode
//     * 找到阶段实例表对应阶段数量 + 串联实例表的并行申报事项对应阶段的数量。
//     * 阶段实例和串联实例对应的申请实例的创建时间在查询时间区间内
//     * @return
//     */
//    @Override
//    public ContentResultForm queryStageApplyStatistics(String startTime, String endTime){
//        ContentResultForm resultForm = new ContentResultForm(false);
//        boolean boo = checkDate(startTime,endTime);
//        if(boo){
//            StatisticsVo content = new StatisticsVo();
//            int count = 0;  //总数
//            int bx = 0;     //并行推进申报数
//            List<ApplyStatisticsVo> applyStatistics = new ArrayList<>();
//            for (StandardStageCode standardStageCode : StandardStageCode.values()) {
//                String value = standardStageCode.getValue();
//                String name = standardStageCode.getName();
//                //查询对应时间段内申报的对应阶段的阶段实例
//                List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryStageAeaHiParStageinsts(value,SecurityContext.getCurrentOrgId(), startTime, endTime,null);
//                //查询对应时间段内申报的对应阶段的并行推进事项实例
//                List<AeaHiSeriesinst> seriesinsts = aeaHiSeriesinstMapper.queryStageAeaHiSeriesinsts(value,SecurityContext.getCurrentOrgId(), startTime, endTime,null);
//                int num = stageinsts.size() + seriesinsts.size();
//                ApplyStatisticsVo v1 = new ApplyStatisticsVo();
//                v1.setName(name);
//                v1.setCount(stageinsts.size());
//                applyStatistics.add(v1);
//                count += num;
//                bx += seriesinsts.size();
//            }
//            ApplyStatisticsVo bxVo = new ApplyStatisticsVo();
//            bxVo.setName("并行推进");
//            bxVo.setCount(bx);
//            applyStatistics.add(bxVo);
//            //计算百分比
//            calcuPercent(count, applyStatistics);
//            content.setTotal(count);
//            content.setApplyStatistics(applyStatistics);
//            content.setStartTime(startTime);
//            content.setEndTime(endTime);
//            resultForm.setSuccess(true);
//            resultForm.setContent(content);
//        }else {
//            resultForm.setMessage("时间参数不正确");
//        }
//        return resultForm;
//    }
//
//    /**
//     * 统计对应月份区间的：
//     * 受理申报数（申请实例状态为：2已受理、3待补全、4已补全、6已办结）
//     * 办结申报数（申请实例状态为：6已办结）
//     * 逾期申报数（申请实例状态为：0已接件未审核、1已接件并审核、2已受理、3待补全、4已补全、6已办结）
//     * 不予受理申报数（申请实例状态为：5不予受理）
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    @Override
//    public ContentResultForm queryMonthlyApplyStatistics(String startTime, String endTime) {
//        ContentResultForm resultForm = new ContentResultForm(false,null,"查询出错");
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            List<MonthlyStatisticsVo> content = new ArrayList<>();
//            List<String> list = DateUtils.calculateMonthly(startTime, endTime);
//            if(list.size() > 0){
//                for(String date:list){
//                    MonthlyStatisticsVo vo = new MonthlyStatisticsVo();
//                    Date m = dateFormat.parse(date);
//                    Date start = DateUtils.firstDayOfMonth(m);
//                    Date end = DateUtils.lastDayOfMonth(m);
//                    String firstTime = dateFormat2.format(start);
//                    String lastTime = dateFormat2.format(end);
//                    //受理申报数
//                    String[] accept ={ApplyState.ACCEPT_DEAL.getValue(),ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),ApplyState.COMPLETED.getValue()};
//                    List<AeaHiApplyinst> acceptList = aeaHiApplyinstMapper.queryAeaHiApplyinst(Arrays.asList(accept), SecurityContext.getCurrentOrgId(), firstTime, lastTime);
//                    //办结申报数
//                    String[] complete ={ApplyState.COMPLETED.getValue()};
//                    List<AeaHiApplyinst> completeList = aeaHiApplyinstMapper.queryAeaHiApplyinst(Arrays.asList(complete), SecurityContext.getCurrentOrgId(), firstTime, lastTime);
//                    //逾期申报数
//                    String[] overtime ={ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),ApplyState.RECEIVE_APPROVED_APPLY.getValue(),ApplyState.ACCEPT_DEAL.getValue(),
//                            ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),ApplyState.COMPLETED.getValue()};
//                    List<AeaHiApplyinst> overtimeList = aeaHiApplyinstMapper.queryAeaHiApplyinst(Arrays.asList(overtime), SecurityContext.getCurrentOrgId(), firstTime, lastTime);
//                    //不予受理申报数
//                    String[] outScope ={ApplyState.OUT_SCOPE.getValue()};
//                    List<AeaHiApplyinst> outScopeList = aeaHiApplyinstMapper.queryAeaHiApplyinst(Arrays.asList(outScope), SecurityContext.getCurrentOrgId(), firstTime, lastTime);
//                    vo.setMonthly(date);
//                    vo.setAcceptApplyCount(acceptList.size());
//                    vo.setCompletedApplyCount(completeList.size());
//                    vo.setOverTimeApplyCount(getCount(overtimeList));
//                    vo.setOverTimeApplyCount(outScopeList.size());
//                    content.add(vo);
//                }
//                resultForm.setSuccess(true);
//                resultForm.setContent(content);
//                resultForm.setMessage("查询成功");
//            }
//        }catch (Exception e){
//            return resultForm;
//        }
//        return resultForm;
//    }
//
//    @Override
//    public Map<String, Object> queryApplyStatisticsByTime(String startTime, String endTime) throws Exception{
//        Map<String,Object> result = new HashMap<>();
//        if(!checkDate(startTime,endTime)){
//            throw new Exception("日期格式不对！开始日期不能大于结束日期！");
//        }
//
//        List<ApplyFormVo> list = efficiencySupervisionMapper.getApplyStatisticsByTIme(startTime,endTime,SecurityContext.getCurrentOrgId());
//        List<String> title = new ArrayList<>();
//        List<Long> acceptCount = new ArrayList<>();
//        List<Long> notAcceptCount = new ArrayList<>();
//
//        List<EfficacyWinResultVo> tableDataList = new ArrayList<>();
//
//        //先按窗口名称分组
//        if(list.size()>0){
//            Map<String, List<ApplyFormVo>> winCollect = list.stream().collect(Collectors.groupingBy(ApplyFormVo::getWindowName));
//            for(String key: winCollect.keySet()){
//                title.add(key);
//                List<ApplyFormVo> tmp = winCollect.get(key);
//                if(tmp !=null && tmp.size() > 0 ){
//                    List<ApplyFormVo> accList = new ArrayList<>();
//                    List<ApplyFormVo> notAccList = new ArrayList<>();
//                    //按申请状态分组
//                    Map<String, List<ApplyFormVo>> stateCollect = tmp.stream().collect(Collectors.groupingBy(ApplyFormVo::getApplyinstState));
//                    long acc= 0 ,notAcc = 0,beforeAcc=0;
//                    for(String stateKey :stateCollect.keySet()){
//                        List<ApplyFormVo> efficacyWindowVos = stateCollect.get(stateKey);
//                        if("5".equals(stateKey)){//不受理的
//                            notAcc += efficacyWindowVos.size();
//                            notAccList.addAll(efficacyWindowVos);
//                        }else if("1,2,3,4,6".indexOf(stateKey) != -1){//已受理的：0已接件未审核（适用于网厅）、1已接件并审核、2已受理、3待补全、4已补全、5不予受理、6已办结'
//                            acc += efficacyWindowVos.size();
//                            accList.addAll(efficacyWindowVos);
//                        }else if("0".equals(stateKey)){
//                            beforeAcc += efficacyWindowVos.size();
//                            accList.addAll(efficacyWindowVos);
//                        }
//                    }
//                    acceptCount.add(acc+beforeAcc+notAcc);
//                    notAcceptCount.add(notAcc);
//                    //=end
//
//                    //算窗口还是网上
//                    Map<String ,Long> sourceCount = getSourceCount(accList);
//                    //就是逾期的
//                    long overdueCount = getOverDueCount(accList);
//                    //封装返回表格数据
//                    EfficacyWinResultVo oneVo =  compostOneData(key,acc+beforeAcc,notAcc,sourceCount.get("win"),sourceCount.get("net"),overdueCount);
//                    tableDataList.add(oneVo);
//                }
//            }
//
//            List<EfficacyWinResultVo> sortList = tableDataList.stream().sorted(Comparator.comparing(EfficacyWinResultVo::getAcceptCount).reversed()).collect(Collectors.toList());
//            for(int i =0,len = sortList.size();i<len; i++){
//                sortList.get(i).setSortNo(i+1);
//            }
//
//
//            //封装返回结果
//            Map<String,Object> left = new HashMap<>();
//            left.put("title",title);
//            left.put("acceptCount",acceptCount);
//            left.put("notAcceptCount",notAcceptCount);
//            result.put("leftData",left);
//            result.put("rightData",sortList);
//
//        }
//        return result;
//    }
//
//    @Override
//    public List<Map<String, Object>> staticsticsByApplySource() throws Exception {
//        String[] stateArr = {ApplyState.RECEIVE_APPROVED_APPLY.getValue(),ApplyState.ACCEPT_DEAL.getValue(),
//                ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),
//                ApplyState.COMPLETED.getValue()};
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setApplyinstSource("win");
//        List<AeaHiApplyinst> winList = getApplyinstByStatusAndCondition(null,search);//null查询所有
//
//        search.setApplyinstSource("net");
//        List<AeaHiApplyinst> netList = getApplyinstByStatusAndCondition(stateArr,search);
//        List<Map<String, Object>> result = new ArrayList<>();
//        result.add(new HashMap<String, Object>(){{put("name","网上大厅");put("value",netList.size());}});
//        result.add(new HashMap<String, Object>(){{put("name","现场大厅");put("value",winList.size());}});
//        return result;
//    }
//
//    @Override
//    public List<Map<String, Object>> staticsticsByApplyType() throws Exception {
//        //申报成功的状态
//        List<Map<String, Object>> result = new ArrayList<>();
//        String[] stateArr = {ApplyState.RECEIVE_APPROVED_APPLY.getValue(),ApplyState.ACCEPT_DEAL.getValue(),
//                ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),
//                ApplyState.COMPLETED.getValue()};
//        List<ApplyPorjVo> list = efficiencySupervisionMapper.getApplyProjVoByState(null,SecurityContext.getCurrentOrgId());
//        int _single =0,_parllel =0,_partParllel=0;
//        if(list.size() > 0){
//            Map<String, List<ApplyPorjVo>> collect = list.stream().collect(Collectors.groupingBy(ApplyPorjVo::getProjInfoId));
//            for(String projInfoId: collect.keySet()){
//                int single =0,parallel=0;
//                for(ApplyPorjVo vo : collect.get(projInfoId)){
//                    if("0".equals(vo.getIsApprove())){
//                        parallel += 1;
//                    }else {
//                        single +=1;
//                    }
//                }
//                if(single ==0 ){
//                    _parllel +=1;
//                }
//                if(parallel ==0 ){
//                    _single +=1;
//                }
//                if(parallel>0 && single >0){
//                    _partParllel +=1;
//                }
//            }
//        }
//        HashMap<String, Object> parlMap = new HashMap<>();
//        parlMap.put("name","全并联项目");
//        parlMap.put("value",_parllel);
//
//        HashMap<String, Object> singleMap = new HashMap<>();
//        singleMap.put("name","全单项审批项目");
//        singleMap.put("value",_single);
//        HashMap<String, Object> partParlMap = new HashMap<>();
//        partParlMap.put("name","部分并联项目");
//        partParlMap.put("value",_partParllel);
//        result.add(singleMap);
//        result.add(parlMap);
//        result.add(partParlMap);
//
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> queryApplyStatisticsByRegion(String startTime,String endTime) throws Exception {
//        if(!checkDate(startTime,endTime)){
//            throw new Exception("日期格式不对！开始日期不能大于结束日期！");
//        }
//        Map<String,Object> result = new HashMap<>();
//        List<ApplyFormVo>  list = efficiencySupervisionMapper.queryApplyWithRegion(startTime,endTime,SecurityContext.getCurrentOrgId());
//        List<String> titleList = new ArrayList<>();
//        List<Integer> countList = new ArrayList<>();
//        if(list.size() > 0){
//            Map<String, List<ApplyFormVo>> collect = list.stream().collect(Collectors.groupingBy(ApplyFormVo::getRegionName));
//            for(String regionName : collect.keySet()){
//                titleList.add(regionName);
//                countList.add(collect.get(regionName).size());
//            }
//            result.put("title",titleList);
//            result.put("value",countList);
//        }
//        return result;
//    }
//
//    /**
//     * 查询主题阶段异常排名
//     * 阶段-主题 ，分组后统计其中的不受理数和逾期数
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public List<ApplyThemeExceptionVo> queryApplyThemeExceptionStatistics() throws Exception {
//        //不写新的mapper有点复杂
//        List<ApplyThemeExceptionVo> result = new ArrayList<>();
//        List<ApplyStatisticsVo> applyStatistics = new ArrayList<>();
//        for (StandardStageCode standardStageCode : StandardStageCode.values()) {
//            String value = standardStageCode.getValue();
//            String name = standardStageCode.getName();
//            //查询对应时间段内申报的对应阶段的阶段实例
//            List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryStageAeaHiParStageinsts(value, SecurityContext.getCurrentOrgId(),null, null,null);
//            if(stageinsts.size() >0){
//                //安主题分组(一个阶段下的个钟主题)
//                Map<String, List<AeaHiParStageinst>> collect = stageinsts.stream().collect(Collectors.groupingBy(AeaHiParStageinst::getThemeVerId));
//                for(String key :collect.keySet()){
//                    int notAccept=0,overdue=0;
//                    List<AeaHiParStageinst> aeaHiParStageinsts = collect.get(key);
//
//                    //计算逾期数量和不受理数
//                    for(AeaHiParStageinst inst : aeaHiParStageinsts){
//                        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(inst.getApplyinstId());
//
//                        if(checkApplyTimeState(applyinst,"3")){
//                            overdue++;
//                        }
//                        if("5".equals(applyinst.getApplyinstState())){
//                            notAccept++;
//                        }
//                    }
//                    ApplyThemeExceptionVo exceptionVo = new ApplyThemeExceptionVo();
//                    exceptionVo.setNotAcceptCount(notAccept);
//                    exceptionVo.setOverdueCount(overdue);
//                    exceptionVo.setStageName(aeaHiParStageinsts.get(0).getStageName());
//                    exceptionVo.setThemeName(aeaHiParStageinsts.get(0).getThemeName());
//                    result.add(exceptionVo);
//                }
//            }
//
//        }
//        //排序设置排名，
//        if(result.size()>0){
//            List<ApplyThemeExceptionVo> sortList = result.stream().sorted(Comparator.comparing(ApplyThemeExceptionVo::getNotAcceptCount).reversed()).collect(Collectors.toList());
//            for(int i=0 ,len = sortList.size();i<len;i++){
//                sortList.get(i).setSortNo(i+1);
//            }
//            return sortList;
//        }
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> queryItemStatisticsByTime(String startTime, String endTime) throws Exception {
//        if(!checkDate(startTime,endTime)){
//            throw new Exception("日期格式不对！开始日期不能大于结束日期！");
//        }
//        List<String> title = new ArrayList<>();
//        List<Integer> receiptCountList = new ArrayList<>();
//        List<Integer> notAcceptCountList = new ArrayList<>();
//        List<ItemDetailFormVo> dataList = new ArrayList<>();
//
//        //查询所有状态下的事项实例,在程序里分组，不知大数据量效率如何
//        String[] stateIds = null;
//        List<ItemDetailFormVo> list = efficiencySupervisionMapper.queryItemStatisticsByTime(stateIds,startTime,endTime,SecurityContext.getCurrentOrgId());
//
//        if(list.size() >0 ){
//            //按部门分组
//            Map<String, List<ItemDetailFormVo>> collect = list.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getOrgId));
//            for(String orgId : collect.keySet()){
//                List<ItemDetailFormVo> itemDetailVos = collect.get(orgId);
//                String orgName = itemDetailVos.get(0).getOrgName();
//                title.add(orgName);
//                int receiptSize = itemDetailVos.size();
//                receiptCountList.add(receiptSize);
//                //按事项实例状态分组
//                Map<String, List<ItemDetailFormVo>> statecollect = itemDetailVos.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getIteminstState));
//                int notAcceptCount=0,completedCount=0,overdueCount=0;
//                for(String state :statecollect.keySet()){
//                    int tmp = statecollect.get(state).size();
//                    if(ItemStatus.OUT_SCOPE.getValue().equals(state)){
//                        notAcceptCount += tmp;
//
//                    }else if(ItemStatus.AGREE.getValue().equals(state) || ItemStatus.DISAGREE.getValue().equals(state)){//办件通过和不通过
//                        completedCount += tmp;
//                    }
//                    //统计逾期shu
//                    for(ItemDetailFormVo vo :statecollect.get(state)){
//                        if(checkItemTimeState(vo.getIteminstId(),"3")){
//                            overdueCount++;
//                        }
//                    }
//
//                }
//                notAcceptCountList.add(notAcceptCount);
//                //封装表格一行数据
//                ItemDetailFormVo formVo = compostOneOrgData(orgName,receiptSize,notAcceptCount,completedCount,overdueCount);
//                dataList.add(formVo);
//            }
//            //数据排序设置排名,按接件量
//            List<ItemDetailFormVo> _tmp = dataList.stream().sorted(Comparator.comparing(ItemDetailFormVo::getReceiptCount).reversed()).collect(Collectors.toList());
//            for(int i = 0,len = _tmp.size();i<len;i++){
//                _tmp.get(i).setSortNo(i+1);
//            }
//
//            //封装返回结果
//            Map<String,Object> result = new HashMap<>();
//            Map<String,Object> left = new HashMap<>();
//            left.put("title",title);
//            left.put("receiptCount",receiptCountList);
//            left.put("notAcceptCount",notAcceptCountList);
//            result.put("leftData",left);
//            result.put("rightData",_tmp);
//            return result;
//        }
//        return null;
//    }
//
//    /**
//     * 根据开始结束时间，统计各地区的受理事项数量
//     * @param startTime
//     * @param endTime
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public Map<String, Object> queryItemStatisticsByRegion(String startTime, String endTime) throws Exception {
//        if(!checkDate(startTime,endTime)){
//            throw new Exception("日期格式不对！开始日期不能大于结束日期！");
//        }
//        Map<String,Object> result = new HashMap<>();
//        String[] states = {ItemStatus.ACCEPT_DEAL.getValue(),ItemStatus.CORRECT_MATERIAL_START.getValue(),
//        ItemStatus.CORRECT_MATERIAL_END.getValue(),ItemStatus.DEPARTMENT_DEAL_START.getValue(),ItemStatus.SPECIFIC_PROC_START.getValue(),
//        ItemStatus.SPECIFIC_PROC_END.getValue(),ItemStatus.AGREE.getValue(),ItemStatus.AGREE_TOLERANCE.getValue(),
//        ItemStatus.DISAGREE.getValue(),ItemStatus.RECALL.getValue(),ItemStatus.REVOKE.getValue()};
//        List<ItemDetailFormVo> list = efficiencySupervisionMapper.queryItemStatisticsByTime(states,startTime,endTime,SecurityContext.getCurrentOrgId());
//        //按地区分组
//        List<String> titleList = new ArrayList<>();
//        List<Integer> countList = new ArrayList<>();
//        if(list.size() > 0){
//            Map<String, List<ItemDetailFormVo>> collect = list.stream().collect(Collectors.groupingBy(ItemDetailFormVo::getRegionId));
//            for(String regionId :collect.keySet()){
//                titleList.add(collect.get(regionId).get(0).getRegionName());
//                countList.add(collect.get(regionId).size());
//            }
//            result.put("title",titleList);
//            result.put("value",countList);
//        }
//
//        return result;
//    }
//
//    /**
//     * 事项办理异常排名统计
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public List<ItemExceptionCountVo> queryItemExceptionStatistics() throws Exception {
//        List<ItemExceptionCountVo> itemExceptionCountVos = efficiencySupervisionMapper.queryItemExceptionCountVo(SecurityContext.getCurrentOrgId());
//        if (CollectionUtils.isNotEmpty(itemExceptionCountVos)) {
//            itemExceptionCountVos.forEach(vo ->{
//                int noAcceptCount = vo.getNoAcceptCount();
//                int overdueCount = vo.getOverdueCount();
//                int totalCount = vo.getTotalCount();
//                int exceptionCount = noAcceptCount + overdueCount;
//
//                vo.setExceptionCount(exceptionCount);
//                double v1 = new BigDecimal(noAcceptCount).divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP).doubleValue();
//                double noAcceptRate = formatDoubleValue(v1 * 100) ;
//                vo.setNoAcceptRate(noAcceptRate);
//                double v2 = new BigDecimal(overdueCount).divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP).doubleValue();
//                double overdueNumRate = formatDoubleValue(v2 * 100) ;
//                vo.setOverdueRate(overdueNumRate);
//                double v3 = new BigDecimal(exceptionCount).divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP).doubleValue();
//                double exceptionRate = formatDoubleValue(v3 * 100);
//                vo.setExceptionRate(exceptionRate);
//            });
//            List<ItemExceptionCountVo> sortList = itemExceptionCountVos.stream().sorted(
//                    Comparator.comparing(ItemExceptionCountVo::getExceptionRate, Comparator.reverseOrder())
//                            .thenComparing(ItemExceptionCountVo::getExceptionCount, Comparator.reverseOrder())
//            ).collect(Collectors.toList());
//            for (int i = 0, len = sortList.size(); i < len; i++) {
//                sortList.get(i).setSortNo(i+1);
//            }
//            return sortList;
//        }
//        return Collections.emptyList();
//    }
//
//    /**
//     * 事项办理串并联统计
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public List<Map<String, Object>> staticsticsIteminstByApplyType() throws Exception {
//        List<Map<String, Object>> result = new ArrayList<>();
//        int serIteminstCount = aeaHiIteminstMapper.countTotalItemByApplyType("1", SecurityContext.getCurrentOrgId());
//        int parIteminstCount = aeaHiIteminstMapper.countTotalItemByApplyType("0", SecurityContext.getCurrentOrgId());
//
//        HashMap<String, Object> singleMap = new HashMap<>();
//        singleMap.put("name","一般单项");
//        singleMap.put("value",serIteminstCount);
//
//        HashMap<String, Object> parlMap = new HashMap<>();
//        parlMap.put("name","并联单项");
//        parlMap.put("value",parIteminstCount);
//        result.add(singleMap);
//        result.add(parlMap);
//
//        return result;
//    }
//
//
//    //===================================================================↓非接口实现方法↓====================================================================
//
//    /**
//     * 统计网上待预审
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryWangshangdaiyushen() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("网上待预审");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        search.setApplyinstState(ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue());
//        search.setApplyinstSource("net");
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
//        vo.setCount(list.size());
//        return vo;
//    }
//    /**
//     * 校验时间字符串
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    private boolean checkDate(String startTime,String endTime){
//        boolean result = false;
//        if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date startDate = dateFormat.parse(startTime);
//                Date endDate = dateFormat.parse(startTime);
//                if(endDate.getTime()>=startDate.getTime()){
//                    result = true;
//                }
//            }catch (ParseException e){
//                return result;
//            }
//        }
//        return result;
//    }
//    /**
//     * 保留两位小数
//     * @param f
//     * @return
//     */
//    private static double formatDoubleValue(double f){
//        BigDecimal bg = new BigDecimal(f);
//        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }
//
//    private String getqueryThemeVerIds(List<AeaParThemeVer> themeVers){
//        StringBuilder sb = new StringBuilder();
//        for(int i=0,len=themeVers.size();i<len;i++){
//            String verId = themeVers.get(i).getThemeVerId();
//            sb.append(",").append("'").append(verId).append("'");
//        }
//        String s = sb.toString().substring(1);
//        return s;
//    }
//
//    /**
//     * 计算百分比
//     * @param count  总数
//     * @param applyStatistics
//     */
//    private void calcuPercent(float count, List<ApplyStatisticsVo> applyStatistics) {
//        if(applyStatistics != null && applyStatistics.size() >0 && count > 0){
//            DecimalFormat df = new DecimalFormat("##.00%");
//            for(int i=0,len=applyStatistics.size();i<len;i++){
//                ApplyStatisticsVo v2 = applyStatistics.get(i);
//                double value = v2.getCount() / count;
//                String percent = df.format(value);
//                v2.setPercent(percent);
//            }
//        }
//    }
//
//    /**
//     * 将double2位小数转换成百分比
//     * @param value
//     * @return
//     */
//    private String formatPercent(double value){
//        return value * 100 + "%";
//    }
//
//    /**
//     * 统计申报待补全
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryShenbaodaibuquan() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("申报待补全");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        search.setApplyinstState(ApplyState.IN_THE_SUPPLEMENT.getValue());
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
//        vo.setCount(list.size());
//        return vo;
//    }
//
//    /**
//     * 统计申报已补全
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryShenbaoyibuquan() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("申报已补全");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        search.setApplyinstState(ApplyState.SUPPLEMENTARY.getValue());
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
//        vo.setCount(list.size());
//        return vo;
//    }
//
//    /**
//     * 统计不予受理
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryBuyushouli() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("不予受理");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        search.setApplyinstState(ApplyState.OUT_SCOPE.getValue());
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
//        vo.setCount(list.size());
//        return vo;
//    }
//    /**
//     * 统计申报时限预警
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryShenbaoxianshiyujing() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("申报时限预警");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        String[] states ={ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),ApplyState.RECEIVE_APPROVED_APPLY.getValue(),
//                ApplyState.ACCEPT_DEAL.getValue(),ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue()};
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinstByStates(Arrays.asList(states), SecurityContext.getCurrentOrgId());
//        int count = 0;
//        for(int i=0,len=list.size();i<len; i++){
//            AeaHiApplyinst applyinst = list.get(i);
//            boolean boo = checkApplyTimeState(applyinst,"2");
//            if(boo){
//                count++;
//            }
//        }
//        vo.setCount(count);
//        return vo;
//    }
//
//    /**
//     * 统计申报时限逾期
//     * @return
//     * @throws Exception
//     */
//    private ApplyStatisticsVo queryShenbaoxianshiyuqi() throws Exception {
//        ApplyStatisticsVo vo = new ApplyStatisticsVo();
//        vo.setName("申报时限逾期");
//        AeaHiApplyinst search = new AeaHiApplyinst();
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        String[] states ={ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),ApplyState.RECEIVE_APPROVED_APPLY.getValue(),ApplyState.ACCEPT_DEAL.getValue(),
//                          ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),ApplyState.COMPLETED.getValue()};
//        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinstByStates(Arrays.asList(states), SecurityContext.getCurrentOrgId());
//        vo.setCount(getCount(list));
//        return vo;
//    }
//
//    /**
//     * 根据申请实例集合返回逾期申报数量
//     * @param list
//     * @return
//     * @throws Exception
//     */
//    public int getCount(List<AeaHiApplyinst> list) throws Exception {
//        int count = 0;
//        for(int i=0,len=list.size();i<len; i++){
//            AeaHiApplyinst applyinst = list.get(i);
//            boolean boo = checkApplyTimeState(applyinst,"3");
//            if(boo){
//                count++;
//            }
//        }
//        return count;
//    }
//
//    /**
//     * 查询办结数量
//     * @param vo
//     */
//    private void queryCompletedCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//        search.clearQueryParam();
//        String[] state = {ItemStatus.AGREE.getValue(),ItemStatus.AGREE_TOLERANCE.getValue(),ItemStatus.DISAGREE.getValue()};
//        search.setQueryIteminstStates(getQueryFieldValue(state));
//        List<AeaHiIteminst> list = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
//        vo.setCompletedCount(list.size());
//    }
//
//    /**
//     * 查询待补正数量
//     * @param vo
//     */
//    private void queryCorrectStartCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//        search.clearQueryParam();
//        String[] state = {ItemStatus.CORRECT_MATERIAL_START.getValue()};
//        search.setQueryIteminstStates(getQueryFieldValue(state));
//        List<AeaHiIteminst> list = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
//        vo.setCorrectStartCount(list.size());
//    }
//
//    /**
//     * 查询补正待确认数量
//     * @param vo
//     */
//    private void queryCorrectEndCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//        search.clearQueryParam();
//        String[] state = {ItemStatus.CORRECT_MATERIAL_END.getValue()};
//        search.setQueryIteminstStates(getQueryFieldValue(state));
//        List<AeaHiIteminst> list = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
//        vo.setCorrectStartCount(list.size());
//    }
//
//
//    /**
//     * 查询预警数量
//     * @param vo
//     */
//    private void queryWarningCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//
//    }
//
//    /**
//     * 查询逾期数量
//     * @param vo
//     */
//    private void queryOverTimeCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//
//    }
//
//    /**
//     * 查询申报时限状态
//     * @param applyinst 申请实例
//     * @param state     查询的状态值 1表示正常，2表示预警，3表示逾期
//     * @return
//     */
//    private boolean checkApplyTimeState(AeaHiApplyinst applyinst, String state) throws Exception{
//        String applyinstId = applyinst.getApplyinstId();
//        boolean boo = false;
//        ActStoTimeruleInst timeruleInst;
//        if("0".equals(applyinst.getIsSeriesApprove())){
//            //并联申报
//            AeaHiParStageinst parStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
//            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(parStageinst.getStageinstId());
//        }else{
//            //单项申报
//            AeaHiIteminst iteminst = aeaHiIteminstMapper.queryAeaHiIteminstBySeriesApplyinstId(applyinstId);
//            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId());
//        }
//        if(timeruleInst != null){
//            boo = state.equals(timeruleInst.getInstState());
//        }
//        return boo;
//    }
//
//    /**
//     * 查询已受理数量
//     * @param vo
//     */
//    private void queryAcceptedCount(ItemStatusCountVo vo,AeaHiIteminst search) {
//        search.clearQueryParam();
//        String[] state = {ItemStatus.ACCEPT_DEAL.getValue(),ItemStatus.CORRECT_MATERIAL_START.getValue(),ItemStatus.CORRECT_MATERIAL_END.getValue(),
//                ItemStatus.DEPARTMENT_DEAL_START.getValue(),ItemStatus.SPECIFIC_PROC_START.getValue(),ItemStatus.SPECIFIC_PROC_END.getValue(),
//                ItemStatus.AGREE.getValue(),ItemStatus.AGREE_TOLERANCE.getValue(),ItemStatus.DISAGREE.getValue(),
//                ItemStatus.RECALL.getValue(),ItemStatus.REVOKE.getValue()};
//        search.setQueryIteminstStates(getQueryFieldValue(state));
//        List<AeaHiIteminst> list = aeaHiIteminstMapper.queryAeaHiIteminstList(search);
//        vo.setCorrectStartCount(list.size());
//    }
//
//
//    /**
//     * 拼接查询字段
//     * @return
//     */
//    private String getQueryFieldValue(String[] params){
//        String result = null;
//        if(params != null && params.length>0){
//            StringBuilder sb = new StringBuilder();
//            for(int i=0,len=params.length;i<len;i++){
//                String param = params[i];
//                if(StringUtils.isNotBlank(param)){
//                    sb.append(",").append("'").append(param).append("'");
//                }
//            }
//            if(sb.length()>0){
//                result = sb.toString().substring(1);
//            }
//        }
//        return result;
//    }
//
//    private Map<String, Long> getSourceCount(List<ApplyFormVo> accList) {
//        if(accList!=null && accList.size() >0){
//            Map<String, Long> collect = accList.stream().collect(Collectors.groupingBy(ApplyFormVo::getApplyinstSource, Collectors.counting()));
//            return collect;
//        }
//        return new HashMap<String, Long>(){{put("net",0l);put("win",0l);}};
//    }
//    /**
//     * 计算逾期数量
//     * @param accList
//     * @return
//     * @throws Exception
//     */
//    private long getOverDueCount(List<ApplyFormVo> accList) throws Exception{
//        long count = 0;
//        if(accList != null && accList.size() >0){
//            for(ApplyFormVo vo :accList){
//                boolean bol =  checkApplyTimeState(vo.getApplyinstId(),vo.getIsApprove(),"3");
//                if(bol){
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//    /**
//     *
//     * @param applyinstId
//     * @param isApprove 0并联1单向
//     * @param state 查询的状态值 1表示正常，2表示预警，3表示逾期
//     * @return
//     * @throws Exception
//     */
//    private boolean checkApplyTimeState(String applyinstId,String isApprove, String state) throws Exception{
//
//        boolean boo = false;
//        ActStoTimeruleInst timeruleInst;
//        if("0".equals(isApprove)){
//            //并联申报
//            AeaHiParStageinst parStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
//            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(parStageinst.getStageinstId());
//        }else{
//            //单项申报
//            AeaHiIteminst iteminst = aeaHiIteminstMapper.queryAeaHiIteminstBySeriesApplyinstId(applyinstId);
//            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId());
//        }
//        if(timeruleInst != null){
//            boo = state.equals(timeruleInst.getInstState());
//        }
//        return boo;
//    }
//    /**
//     * 封装表格数据
//     * @param windowName
//     * @param acc
//     * @param notAcc
//     * @param win
//     * @param net
//     * @param overdueCount
//     * @return
//     */
//
//    private EfficacyWinResultVo compostOneData(String windowName, long acc, long notAcc, Long win, Long net, long overdueCount) {
//        win = win==null ? 0l : win;
//        net = net == null ?0l : net;
//        DecimalFormat df = new DecimalFormat("##.00%");
//        EfficacyWinResultVo vo = new EfficacyWinResultVo();
//        vo.setWindowName(windowName);
//        vo.setAcceptCount(acc);
//        vo.setWinAcceptCount(win);
//        vo.setNetAcceptCount(net);
//        vo.setNotAcceptCount(notAcc);
//
//        if(acc ==0 || (acc+notAcc)==0){
//            vo.setAcceptRate("0.0%");
//            vo.setOverdueRate("0.0%");
//        }else{
//            double d = acc * 1.0/(acc+notAcc);
//            vo.setAcceptRate(df.format(d));
//            vo.setOverdueRate(overdueCount==0?"0.0%":df.format(overdueCount *1.0 /acc));
//        }
//
//        vo.setOverdueCount(overdueCount);
//
//        return vo;
//
//    }
//    /**
//     * 查询某种状态下申请实例
//     * @param stateArr
//     * @return
//     */
//    private List<AeaHiApplyinst> getApplyinstByStatusAndCondition(String[] stateArr,AeaHiApplyinst search)throws Exception {
//        search.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//        search.setRootOrgId(SecurityContext.getCurrentOrgId());
//        List<AeaHiApplyinst> applyinsts = aeaHiApplyinstMapper.listAeaHiApplyinst(search);
//        if(stateArr == null){
//            return applyinsts;
//        }
//        String join = StringUtils.join(stateArr, ",");
//        if(applyinsts.size() > 0){
//            List<AeaHiApplyinst> collect = applyinsts.stream().filter(obj -> join.indexOf(obj.getApplyinstState()) >= 0).collect(Collectors.toList());
//            return collect;
//        }
//        return new ArrayList<AeaHiApplyinst>();
//    }
//
//    /**
//     * 根据事项实例和需要判断的状态
//     * @param iteminstId
//     * 查询的状态值 1表示正常，2表示预警，3表示逾期
//     * @return
//     */
//    private boolean checkItemTimeState(String iteminstId,String state) throws Exception{
//        boolean result = false;
//        ActStoTimeruleInst timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminstId);
//        if(timeruleInst!=null){
//            result = state.equals(timeruleInst.getInstState());
//        }
//
//        return result;
//    }
//
//    /**
//     * 封装成一行按部门统计的详细信息
//     * @param orgName
//     * @param receiptSize
//     * @param notAcceptCount
//     * @param completedCount
//     * @param overdueCount
//     * @return
//     */
//    private ItemDetailFormVo compostOneOrgData(String orgName, int receiptSize, int notAcceptCount, int completedCount, int overdueCount) {
//        ItemDetailFormVo result = new ItemDetailFormVo();
//        result.setOrgName(orgName);
//        result.setReceiptCount(receiptSize);
//        int accCount = receiptSize-notAcceptCount;
//        result.setAcceptCount(accCount);//受理数
//        result.setNotAcceptCount(notAcceptCount);
//        result.setCompletedCount(completedCount);
//        double complate = accCount== 0 ? 0 : completedCount *1.0 / accCount;
//        result.setCompletedRate(formatPercent(formatDoubleValue(complate)));
//        result.setOverdueCount(overdueCount);
//        double voerdue = receiptSize == 0 ? 0 : overdueCount *1.0 / receiptSize;
//        result.setOverdueRate(formatPercent(formatDoubleValue(voerdue)));
//        return result;
//    }
//
//}
