package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.aplanmis.common.service.analyse.DeclarationStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeclarationStatisticsServiceImpl implements DeclarationStatisticsService {/*

    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;
    @Autowired
    private AeaHiApplyinstMapper applyinstMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaAnaThemeDayStatisticsMapper themeDayStatisticsMapper;
    @Autowired
    private AeaLogApplyStateHistMapper logApplyStateHistMapper;

    *//**
     * 获取当天 主题-阶段 分类的统计数据
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     *//*

    public List<AeaAnaThemeDayStatistics> doThemeStatistics(String startTime,String endTime) throws Exception{
        if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)){
            startTime = DateUtils.convertDateToString(new Date(),"yyyy-MM-dd");
            endTime = DateUtils.convertDateToString(new Date(),"yyyy-MM-dd");

        }
        List<AeaAnaThemeDayStatistics> result= new ArrayList<>();
        //找到全部主题
        AeaParTheme search = new AeaParTheme();
        search.setIsActive("1");
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParTheme> themes = aeaParThemeMapper.listAeaParTheme(search);
        for(int i=0,len=themes.size();i<len;i++){
            List<Map<String,Object>> objs = getThemeStageIds(themes.get(i));
            for(int j =0,len2=objs.size();j<len2;j++){
                AeaAnaThemeDayStatistics dayData = packageDayStastics(objs.get(j).get("stageId").toString(),startTime,endTime,themes.get(i).getThemeId());
                dayData.setApplyRecordName(objs.get(j).get("stageName").toString());
                dayData.setThemeId(themes.get(i).getThemeId());
                dayData.setThemeName(themes.get(i).getThemeName());
                result.add(dayData);
            }
        }
        return result;
    }

    *//***
     * 封装一条数据
     * @param
     * @param startTime
     * @param endTime
     * @return
     *//*
    private AeaAnaThemeDayStatistics packageDayStastics(String stageId, String startTime, String endTime,String themeId) throws Exception{
//        根据stageid查询，state ,starttime,endtime ,orgid 申请实例

        List<AeaHiApplyinst> list = applyinstMapper.getAeaHiApplyinstByStageIdAndStates(stageId,null,startTime,endTime,SecurityContext.getCurrentOrgId(), isParallel);
        long dayApplyCount=list.size(),accept=0,inSupplementCount=0,supplementedCount=0,dayPreAcceptanceCount=0,dayOutScopeCount=0,dayCompletedCount=0,allOverTimeCount=0;
        if(list.size() >0){
            Map<String, List<AeaHiApplyinst>> collect = list.stream().collect(Collectors.groupingBy(AeaHiApplyinst::getApplyinstState));
            for(String state :collect.keySet()){
                if(ApplyState.ACCEPT_DEAL.getValue().equals(state)){
                    accept+= collect.get(state).size();
                }else if(ApplyState.IN_THE_SUPPLEMENT.getValue().equals(state)){
                    inSupplementCount+= collect.get(state).size();
                }else if(ApplyState.SUPPLEMENTARY.getValue().equals(state)){
                    supplementedCount+= collect.get(state).size();
                }else if(ApplyState.OUT_SCOPE.getValue().equals(state)){
                    dayOutScopeCount+= collect.get(state).size();
                }else if(ApplyState.COMPLETED.getValue().equals(state)){
                    dayCompletedCount += collect.get(state).size();
                }
            }


        }

        //查询所有逾期的东西
        List<ActStoTimeruleInst> actStoTimeruleInsts = themeDayStatisticsMapper.getStageStoTimeruleInst(stageId, "3", SecurityContext.getCurrentOrgId(), isParallel);
        allOverTimeCount = actStoTimeruleInsts.size();

        // 根据历史状态计算一下
        List<AeaLogApplyStateHist> changeHis =  logApplyStateHistMapper.getApplyChangeHis(stageId,startTime,endTime);
        long hisAccept=0,hisInsupplement=0,hisSupplementary=0,hisOutScope=0,hisCompleted=0;
        if(changeHis.size() > 0){
            Map<String, List<AeaLogApplyStateHist>> collect = changeHis.stream().collect(Collectors.groupingBy(AeaLogApplyStateHist::getNewState));
                for(String state :collect.keySet()){
                    if(ApplyState.ACCEPT_DEAL.getValue().equals(state)){
                        hisAccept+= collect.get(state).size();
                    }else if(ApplyState.IN_THE_SUPPLEMENT.getValue().equals(state)){
                        hisInsupplement+= collect.get(state).size();
                    }else if(ApplyState.SUPPLEMENTARY.getValue().equals(state)){
                        hisSupplementary+= collect.get(state).size();
                    }else if(ApplyState.OUT_SCOPE.getValue().equals(state)){
                        hisOutScope+= collect.get(state).size();
                    }else if(ApplyState.COMPLETED.getValue().equals(state)){
                        hisCompleted += collect.get(state).size();
                    }
                }
        }


        dayPreAcceptanceCount = accept+inSupplementCount+supplementedCount+dayCompletedCount;//预受理 +
        long hisPreacceptanceCount =  hisAccept+ hisInsupplement +hisSupplementary +hisOutScope +hisCompleted;// 历史状态转换过来的
        //从数据库获取前一天记录
        String oneDayBefore = DateUtils.getMinusDay(startTime,1);
        AeaAnaThemeDayStatistics onedayBeforStastics = themeDayStatisticsMapper.getAeaAnaThemeDayStatisticsBySatgeIdAndThemeId(themeId,stageId,oneDayBefore,SecurityContext.getCurrentOrgId());
        long dayOverTimeCount = allOverTimeCount - onedayBeforStastics.getDayOverTimeCount();
        double applyLrr = AnalyseUtils.calculateGrowRate(onedayBeforStastics.getDayApplyCount(),dayApplyCount);
        double preAcceptanceLrr = AnalyseUtils.calculateGrowRate(onedayBeforStastics.getDayPreAcceptanceCount(),dayPreAcceptanceCount+hisPreacceptanceCount);
        double outScopeLrr = AnalyseUtils.calculateGrowRate(onedayBeforStastics.getDayOutScopeCount(),dayOutScopeCount+hisOutScope);
        double completedLrr = AnalyseUtils.calculateGrowRate(onedayBeforStastics.getDayCompletedCount(),dayCompletedCount +hisCompleted);
        double overTimeLrr = AnalyseUtils.calculateGrowRate(onedayBeforStastics.getDayOverTimeCount(),dayOverTimeCount  );

        //縂比率统计
        long allPreAcceptanceCount = dayPreAcceptanceCount +onedayBeforStastics.getAllPreAcceptanceCount();
        long allApplyCount = dayApplyCount +onedayBeforStastics.getAllApplyCount();
        long allOutScopeCount = hisOutScope+ dayOutScopeCount +onedayBeforStastics.getAllOutScopeCount();
        long allCompletedCount = hisCompleted + dayCompletedCount + onedayBeforStastics.getAllCompletedCount();

        double allPreAcceptanceRate = AnalyseUtils.calculateRate(allPreAcceptanceCount,allApplyCount);//总预受理率（预受理数/接件数）
        double allOutScopeRate = AnalyseUtils.calculateRate(allOutScopeCount,allApplyCount);//总不予受理率（不予受理数/接件数）
        double allOverTimeRate = AnalyseUtils.calculateRate(allOverTimeCount,allPreAcceptanceCount);//总逾期率（逾期数/预受理数）
        double allCompletedRate = AnalyseUtils.calculateRate(allCompletedCount,allPreAcceptanceCount);//总办结率（办结数/预受理数）


        //从数据库查询全部已补全和待补全的
        long allInSupplementCount = getAllInSupplementCount(stageId,null,null);
        long allSupplementedCount =getAllSupplementedCount(stageId,null,null);
        //封裝數據
        AeaAnaThemeDayStatistics  result = new AeaAnaThemeDayStatistics();
        result.setThemeDayStatisticsId(UUID.randomUUID().toString());
        result.setApplyRecordId(stageId);
        result.setApplyRecordName("");
        result.setDayApplyCount(dayApplyCount);
        result.setAllApplyCount(allApplyCount);
        result.setApplyLrr(applyLrr);
        result.setAllInSupplementCount(allInSupplementCount);
        result.setAllSupplementedCount(allSupplementedCount);
        result.setDayPreAcceptanceCount(dayPreAcceptanceCount);
        result.setAllPreAcceptanceCount(allPreAcceptanceCount);
        result.setPreAcceptanceLrr(preAcceptanceLrr);
        result.setDayOutScopeCount(dayOutScopeCount);
        result.setAllOutScopeCount(allOutScopeCount);
        result.setOutScopeLrr(outScopeLrr);
        result.setDayCompletedCount(dayCompletedCount);
        result.setAllCompletedCount(allCompletedCount);
        result.setCompletedLrr(completedLrr);
        result.setDayOverTimeCount(dayOverTimeCount);
        result.setAllOverTimeCount(allOverTimeCount);
        result.setOverTimeLrr(overTimeLrr);
        result.setAllPreAcceptanceRate(allPreAcceptanceRate);
        result.setAllOutScopeRate(allOutScopeRate);
        result.setAllOverTimeRate(allOverTimeRate);
        result.setAllCompletedRate(allCompletedRate);
        result.setStatisticsDate(new Date());
        result.setRootOrgId(SecurityContext.getCurrentOrgId());
        return result;


    }


    *//** 获取时间段内的逾期数**//*
    private long getOverTimeCount(String stageId, String startTime, String endTime)  throws Exception{
        List<AeaHiApplyinst> list = applyinstMapper.getAeaHiApplyinstByStageIdAndStates(stageId,null,startTime,endTime,SecurityContext.getCurrentOrgId(), isParallel);
        int count = 0;
        for(int i=0,len=list.size();i<len; i++){
            AeaHiApplyinst applyinst = list.get(i);
            boolean boo = checkApplyTimeState(applyinst,"3");
            if(boo){
                count++;
            }
        }
        return count;
    }

    *//** 获取时间段内的办结数**//*
    private long getCompletedCount(String stageId, String startTime, String endTime) {
        String[] states = {ApplyState.COMPLETED.getValue()};
        return getCount(stageId,states,startTime,endTime);
    }

    *//** 获取时间段内的不受理数**//*
    private long getOutScopeCount(String stageId, String startTime, String endTime) {
        String[] states = {ApplyState.OUT_SCOPE.getValue()};
        return getCount(stageId,states,startTime,endTime);
    }

    *//** 获取时间段内预受理**//*
    private long getPreAcceptanceCount(String stageId, String startTime, String endTime) {
        String[] states = {ApplyState.ACCEPT_DEAL.getValue(),ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue()
        ,ApplyState.COMPLETED.getValue()};
        return getCount(stageId,states,startTime,endTime);
    }


    *//** 获取所有已补全的数量**//*
    private long getAllSupplementedCount(String stageId, String startTime, String endTime) {
        String[] states= {ApplyState.SUPPLEMENTARY.getValue()};
        return getCount(stageId,states,startTime,endTime);
    }

    *//** 获取所有待补全数量**//*
    private long getAllInSupplementCount(String stageId, String startTime, String endTime) {
        String[] states = {ApplyState.IN_THE_SUPPLEMENT.getValue()};
        return getCount(stageId,states,startTime,endTime);
    }

    *//** 获取时间段内接件数**//*
    private long getApplyCount(String stageId, String startTime, String endTime) {
       return getCount(stageId,null,startTime,endTime);
    }



    private long getCount(String stageId,String[] states,String startTime,String endTime){
        List<AeaHiApplyinst> list = applyinstMapper.getAeaHiApplyinstByStageIdAndStates(stageId,states,startTime,endTime,SecurityContext.getCurrentOrgId(), isParallel);
        return list.size();
    }
    *//**
     * 获取主题下的所有阶段id
     * @param theme
     * @return
     * @throws Exception
     *//*
    private List<Map<String,Object>> getThemeStageIds(AeaParTheme theme) throws Exception{
        List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByThemeIds("'"+theme.getThemeId()+"'");
        String queryThemeVerIds = themeVers.stream().map(AeaParThemeVer::getThemeVerId).collect(Collectors.joining("','"));
        queryThemeVerIds = "'"+queryThemeVerIds +"'";
        List<Map<String,Object>> list = themeDayStatisticsMapper.queryThemeStageIds(queryThemeVerIds,SecurityContext.getCurrentOrgId());
        return  list;
    }



    *//**
     * 查询申报时限状态
     * @param applyinst 申请实例
     * @param state     查询的状态值 1表示正常，2表示预警，3表示逾期
     * @return
     *//*
    private boolean checkApplyTimeState(AeaHiApplyinst applyinst, String state) throws Exception{
        String applyinstId = applyinst.getApplyinstId();
        boolean boo = false;
        ActStoTimeruleInst timeruleInst;
        if("0".equals(applyinst.getIsSeriesApprove())){
            //并联申报
            AeaHiParStageinst parStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(parStageinst.getStageinstId());
        }else{
            //单项申报
            AeaHiIteminst iteminst = aeaHiIteminstMapper.queryAeaHiIteminstBySeriesApplyinstId(applyinstId);
            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId());
        }
        if(timeruleInst != null){
            boo = state.equals(timeruleInst.getInstState());
        }
        return boo;
    }

    //===以上是新的方式,思路好看一點=
    //下面是直接统计当天有的申报的


    *//**
     * 获取当天主题-阶段基准下的各类统计
     * @param startTime
     * @param endTime
     * @return
     *//*
    @Override
    public List<AeaAnaThemeDayStatistics> queryThemeStatisticsDay(String startTime, String endTime) throws Exception{
        if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)){
            startTime = DateUtils.convertDateToString(new Date(),"yyyy-MM-dd");
            endTime = DateUtils.convertDateToString(new Date(),"yyyy-MM-dd");

        }

        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        //找到全部主题
        AeaParTheme search = new AeaParTheme();
        search.setIsActive("1");
        search.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParTheme> themes = aeaParThemeMapper.listAeaParTheme(search);

        //遍历每个主题下的所有阶段实例
        for(int i =0,len = themes.size();i<len;i++){
            AeaParTheme theme = themes.get(i);
            List<AeaAnaThemeDayStatistics> dayStatistics = themeDayStatistics(theme,startTime,endTime);
            result.addAll(dayStatistics);
        }
        return result;
    }

    private List<AeaAnaThemeDayStatistics> themeDayStatistics(AeaParTheme theme, String startTime, String endTime) throws Exception{
        List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByThemeIds("'"+theme.getThemeId()+"'");
        String queryThemeVerIds ="'"+ themeVers.stream().map(AeaParThemeVer::getThemeVerId).collect(Collectors.joining("','")) + "'";

        //得到当天个状态统计
        List<AeaAnaThemeDayStatistics> allCount = doStatistics(queryThemeVerIds,startTime,endTime,null);
        if(allCount.size() >0){

            //根据themeid和stageid查询总量
            caculateionAllCount(allCount);
            calculationRate(allCount,startTime);
        }

        return allCount;
    }

    *//**
     * 计算个状态的总量
     *
     * @param allCount
     *//*
    private void caculateionAllCount(List<AeaAnaThemeDayStatistics> allCount) throws Exception{
        if(allCount.size() > 0 ){
            for(AeaAnaThemeDayStatistics dayStatistics :allCount){
               long  applyAll = getStateAllCount(dayStatistics.getApplyRecordId(),null);
                long  allInSupplementCount = getStateAllCount(dayStatistics.getApplyRecordId(),new String[]{ApplyState.IN_THE_SUPPLEMENT.getValue()});
                long  allSupplementedCount = getStateAllCount(dayStatistics.getApplyRecordId(),new String[]{ApplyState.SUPPLEMENTARY.getValue()});
                long  allPreAcceptanceCount = getStateAllCount(dayStatistics.getApplyRecordId(),new String[]{ApplyState.ACCEPT_DEAL.getValue(),
               ApplyState.IN_THE_SUPPLEMENT.getValue(),ApplyState.SUPPLEMENTARY.getValue(),ApplyState.COMPLETED.getValue()});
                long  allOutScopeCount = getStateAllCount(dayStatistics.getApplyRecordId(),new String[]{ApplyState.OUT_SCOPE.getValue()});
                long  allCompletedCount = getStateAllCount(dayStatistics.getApplyRecordId(),new String[]{ApplyState.COMPLETED.getValue()});
                long  allOverTimeCount = getOverTimeAllCount(dayStatistics.getApplyRecordId());


                dayStatistics.setAllApplyCount(applyAll);
                dayStatistics.setAllInSupplementCount(allInSupplementCount);
                dayStatistics.setAllSupplementedCount(allSupplementedCount);
                dayStatistics.setAllPreAcceptanceCount(allPreAcceptanceCount);
                dayStatistics.setAllOutScopeCount(allOutScopeCount);
                dayStatistics.setAllCompletedCount(allCompletedCount);
                dayStatistics.setAllOverTimeCount(allOverTimeCount);

                //计算各总比率
                dayStatistics.setAllPreAcceptanceRate(AnalyseUtils.calculateRate(allPreAcceptanceCount,applyAll));
                dayStatistics.setAllOutScopeRate(AnalyseUtils.calculateRate(allOutScopeCount,applyAll));
                dayStatistics.setAllOverTimeRate(AnalyseUtils.calculateRate(allOverTimeCount,allPreAcceptanceCount));
                dayStatistics.setAllCompletedRate(AnalyseUtils.calculateRate(allCompletedCount,allPreAcceptanceCount));

            }
        }
    }

    *//**
     * 获取stage所有的逾期数
     * @param applyRecordId
     * @return
     *//*
    private long getOverTimeAllCount(String applyRecordId) throws Exception{
       List<AeaHiApplyinst> list =  applyinstMapper.listAeaHiApplyinstByStageId(applyRecordId,SecurityContext.getCurrentOrgId());
        int count = 0;
        for(int i=0,len=list.size();i<len; i++){
            AeaHiApplyinst applyinst = list.get(i);
            boolean boo = checkApplyTimeState(applyinst,"3");
            if(boo){
                count++;
            }
        }
        return count;
    }

    private long getStateAllCount(String applyRecordId, String[] states) {

        return themeDayStatisticsMapper.getStateAllCount(applyRecordId,states,SecurityContext.getCurrentOrgId());
    }

    *//**
     * 计算同比增长比率
     * @param statisticList
     *//*
    private void calculationRate(List<AeaAnaThemeDayStatistics> statisticList,String startTime) {
        //获取前一天所有数据
        String yesterday = DateUtils.getMinusDay(startTime,1);
        for(int i=0,len =statisticList.size();i<len;i++){
            List<AeaAnaThemeDayStatistics> list = themeDayStatisticsMapper.getAeaAnaThemeDayStatistics(statisticList.get(i).getThemeId(),statisticList.get(i).getStatisticsRecordId(),
                    SecurityContext.getCurrentOrgId(),yesterday,yesterday);
            if (list.size() > 0){
                AeaAnaThemeDayStatistics yesterdayStatistics = list.get(0);
                statisticList.get(i).setApplyLrr(AnalyseUtils.calculateGrowRate(yesterdayStatistics.getDayApplyCount(),statisticList.get(i).getDayApplyCount()));
                statisticList.get(i).setPreAcceptanceLrr(AnalyseUtils.calculateGrowRate(yesterdayStatistics.getDayPreAcceptanceCount(),statisticList.get(i).getDayPreAcceptanceCount()));
                statisticList.get(i).setOutScopeLrr(AnalyseUtils.calculateGrowRate(yesterdayStatistics.getDayOutScopeCount(),statisticList.get(i).getDayOutScopeCount()));
                statisticList.get(i).setCompletedLrr(AnalyseUtils.calculateGrowRate(yesterdayStatistics.getDayCompletedCount(),statisticList.get(i).getDayCompletedCount()));
                statisticList.get(i).setOverTimeLrr(AnalyseUtils.calculateGrowRate(yesterdayStatistics.getDayOverTimeCount(),statisticList.get(i).getDayOverTimeCount()));
            }else{
                statisticList.get(i).setApplyLrr(AnalyseUtils.calculateGrowRate(0l,statisticList.get(i).getDayApplyCount()));
                statisticList.get(i).setPreAcceptanceLrr(AnalyseUtils.calculateGrowRate(0l,statisticList.get(i).getDayPreAcceptanceCount()));
                statisticList.get(i).setOutScopeLrr(AnalyseUtils.calculateGrowRate(0l,statisticList.get(i).getDayOutScopeCount()));
                statisticList.get(i).setCompletedLrr(AnalyseUtils.calculateGrowRate(0l,statisticList.get(i).getDayCompletedCount()));
                statisticList.get(i).setOverTimeLrr(AnalyseUtils.calculateGrowRate(0l,statisticList.get(i).getDayOverTimeCount()));

            }

           *//* //计算总数
            List<AeaAnaThemeDayStatistics> totalCount = themeDayStatisticsMapper.getAeaAnaThemeDayStatistics(statisticList.get(i).getThemeId(),statisticList.get(i).getStatisticsRecordId(),
                    SecurityContext.getCurrentOrgId(),null,null);*//*

        }

    }



    *//**
     * 统计各主题-阶段下 某状态的数量
     * @param queryThemeVerIds
     * @param startTime
     * @param endTime
     * @param queryStates
     *//*
    private List<AeaAnaThemeDayStatistics> doStatistics(String queryThemeVerIds, String startTime, String endTime, String queryStates) throws Exception{
        //对应时间段内申报的所属主题阶段实例数量
        List<AeaAnaThemeDayStatistics> result = new ArrayList<>();
        Set<String> stageIdSet = new HashSet<>();
        List<AeaHiParStageinst> stageinsts = aeaHiParStageinstMapper.queryThemeAeaHiParStageinsts(queryThemeVerIds,SecurityContext.getCurrentOrgId() ,startTime, endTime,queryStates);
        if(stageinsts.size() > 0){
            Map<String, List<AeaHiParStageinst>> collect = stageinsts.stream().collect(Collectors.groupingBy(AeaHiParStageinst::getStageId));
            for(String stageId :collect.keySet()){
                stageIdSet.add(stageId);
              AeaAnaThemeDayStatistics dayStatistics = new AeaAnaThemeDayStatistics();
                List<AeaHiParStageinst> aeaHiParStageinsts = collect.get(stageId);
                long yiShouLi =0,daiBuQuan =0,yiBuQuan =0,buYuShouLi =0,banJie = 0,yuqi=0;
                for(int i=0,len = aeaHiParStageinsts.size();i<len;i++){
                    String _state = aeaHiParStageinsts.get(i).getApplyinstState();
                    if(ApplyState.ACCEPT_DEAL.getValue().equals(_state)){
                        yiShouLi+=1;
                    }else if(ApplyState.IN_THE_SUPPLEMENT.getValue().equals(_state)){
                        daiBuQuan += 1;
                    }else if(ApplyState.SUPPLEMENTARY.getValue().equals(_state)){
                        yiBuQuan += 1;
                    }else if(ApplyState.OUT_SCOPE.getValue().equals(_state)){
                        buYuShouLi += 1;
                    }else if (ApplyState.COMPLETED.getValue().equals(_state)){
                        banJie +=1;
                    }

                    if(checkIsDue(aeaHiParStageinsts.get(i).getStageinstId())){
                        yuqi +=1;
                    }

                }
                dayStatistics.setApplyRecordId(stageId);
                dayStatistics.setApplyRecordName(aeaHiParStageinsts.get(0).getStageName());
                dayStatistics.setDayApplyCount((long)aeaHiParStageinsts.size());
                dayStatistics.setDayPreAcceptanceCount(yiShouLi+daiBuQuan+yiBuQuan+banJie);
                dayStatistics.setDayOutScopeCount(buYuShouLi);
                dayStatistics.setDayCompletedCount(banJie);
                dayStatistics.setDayOverTimeCount(yuqi);

                result.add(dayStatistics);
            }


        }
        //查询对应时间段内申报的所属主题并行推进事项实例数量
        List<AeaHiSeriesinst> seriesinsts = aeaHiSeriesinstMapper.queryThemeAeaHiSeriesinsts(queryThemeVerIds,SecurityContext.getCurrentOrgId(), startTime, endTime,queryStates);
        if(seriesinsts.size() > 0){
            Map<String, List<AeaHiSeriesinst>> collect = seriesinsts.stream().collect(Collectors.groupingBy(AeaHiSeriesinst::getStageId));
            for(String stageId :collect.keySet()){
                List<AeaHiSeriesinst> aeaHiSeriesinsts = collect.get(stageId);
                AeaAnaThemeDayStatistics dayStatistics = new AeaAnaThemeDayStatistics();
                long yiShouLi =0,daiBuQuan =0,yiBuQuan =0,buYuShouLi =0,banJie = 0,yuqi=0;
                for(int i =0,len = aeaHiSeriesinsts.size(); i<len;i++){
                    String _state = aeaHiSeriesinsts.get(i).getApplyinstState();
                    if(ApplyState.ACCEPT_DEAL.getValue().equals(_state)){
                        yiShouLi+=1;
                    }else if(ApplyState.IN_THE_SUPPLEMENT.getValue().equals(_state)){
                        daiBuQuan += 1;
                    }else if(ApplyState.SUPPLEMENTARY.getValue().equals(_state)){
                        yiBuQuan += 1;
                    }else if(ApplyState.OUT_SCOPE.getValue().equals(_state)){
                        buYuShouLi += 1;
                    }else if (ApplyState.COMPLETED.getValue().equals(_state)){
                        banJie +=1;
                    }

                    if(checkIsDueByApplyinstid(aeaHiSeriesinsts.get(i).getApplyinstId(),"3")){
                        yuqi +=1;
                    }

                }
                if(stageIdSet.contains(stageId)){//如果上面已用同一阶段的，加到里面去
                    for(AeaAnaThemeDayStatistics tmp :result){
                        if(tmp.getApplyRecordId().equals(stageId)){
                            tmp.setDayApplyCount((long)aeaHiSeriesinsts.size()+tmp.getDayApplyCount());
                            tmp.setDayPreAcceptanceCount(yiShouLi+daiBuQuan+yiBuQuan+banJie +tmp.getDayPreAcceptanceCount());
                            tmp.setDayOutScopeCount(buYuShouLi+tmp.getDayOutScopeCount());
                            tmp.setDayCompletedCount(banJie+tmp.getDayCompletedCount());
                            tmp.setDayOverTimeCount(yuqi+tmp.getDayOverTimeCount());
                        }
                    }
                }else{
                    dayStatistics.setApplyRecordId(stageId);
                    dayStatistics.setApplyRecordName(aeaHiSeriesinsts.get(0).getStageName());
                    dayStatistics.setDayApplyCount((long)aeaHiSeriesinsts.size());
                    dayStatistics.setDayPreAcceptanceCount(yiShouLi+daiBuQuan+yiBuQuan+banJie);
                    dayStatistics.setDayOutScopeCount(buYuShouLi);
                    dayStatistics.setDayCompletedCount(banJie);
                    dayStatistics.setDayOverTimeCount(yuqi);

                    result.add(dayStatistics);
                }
            }
        }
        return result;
    }

    *//**
     *
     * @param applyinstId
     * @param state  查询的状态值 1表示正常，2表示预警，3表示逾期
     * @return
     * @throws Exception
     *//*
    private boolean checkIsDueByApplyinstid(String applyinstId,String state) throws Exception{
        AeaHiApplyinst applyinst = applyinstMapper.getAeaHiApplyinstById(applyinstId);
        boolean boo = false;
        ActStoTimeruleInst timeruleInst;
        if("0".equals(applyinst.getIsSeriesApprove())){
            //并联申报
            AeaHiParStageinst parStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(parStageinst.getStageinstId());
        }else{
            //单项申报
            AeaHiIteminst iteminst = aeaHiIteminstMapper.queryAeaHiIteminstBySeriesApplyinstId(applyinstId);
            timeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId());
        }
        if(timeruleInst != null){
            boo = state.equals(timeruleInst.getInstState());
        }
        return boo;
    }

    private boolean checkIsDue(String stageinstId) throws Exception{
        boolean result =false;
        ActStoTimeruleInst timeruleInst =actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(stageinstId);
        if(timeruleInst != null){
            result = "3".equals(timeruleInst.getInstState());
        }
        return result;
    }*/

}
