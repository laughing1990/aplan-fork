package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.efficiency.service.AeaAnaWinUseTimeStatisticService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/9/24 024 14:02
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaAnaWinUseTimeStatisticServiceImpl implements AeaAnaWinUseTimeStatisticService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaWinUseTimeStatisticServiceImpl.class);

    @Autowired
    private AeaLogApplyStateHistMapper aeaLogApplyStateHistMapper;

    @Autowired
    private AeaServiceWindowMapper aeaServiceWindowMapper;

    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;

    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Autowired
    private AeaAnaStatisticsRecordMapper aeaAnaStatisticsRecordMapper;

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private AeaAnaWinUseTimeStatisticsMapper aeaAnaWinUseTimeStatisticsMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Override
    public void doWinUseTimeStatistics(String rootOrgId, String creator, Date startTime, Date endTime) throws Exception {

        List<AeaLogApplyStateHist> latestApplyStateHists = aeaLogApplyStateHistMapper.listLatestLogApplyStateHist(rootOrgId);
        AeaServiceWindow searchEntity = new AeaServiceWindow();
        searchEntity.setRootOrgId(rootOrgId);
        List<AeaServiceWindow> windows = aeaServiceWindowMapper.listAeaServiceWindow(searchEntity);
        String statisticRecId = UuidUtil.generateUuid();
        List<String> preStatisticRecordIds = new ArrayList<>();
        boolean hasRecord = false;
        for (AeaServiceWindow window : windows) {
            List<AeaLogApplyStateHist> hisListForeachWindow = getApplyStateHistByWindow(latestApplyStateHists,
                                                                                        window.getWindowId());
            List<WinUseTimeStatisticDo> resList = new ArrayList<>();
            for (AeaLogApplyStateHist applyStateHist : hisListForeachWindow) {
                try {
                    WinUseTimeStatisticDo res = new WinUseTimeStatisticDo();
                    AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyStateHist.getApplyinstId());
                    if (aeaHiApplyinst.getIsSeriesApprove().equals("0")) {
                        AeaHiParStageinst stageInst = getStageinstByApplyinstId(applyStateHist.getApplyinstId(), startTime,
                                                                                endTime);
                        if (stageInst == null) {
                            continue;
                        }
                        ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInstMapper
                                .getProcessinstTimeruleInstByStageinstId(stageInst.getStageinstId(), rootOrgId);
                        res.setAeaHiParStageinst(stageInst);
                        res.setActStoTimeruleInst(actStoTimeruleInst);
                    } else {
                        AeaHiSeriesinst aeaHiSeriesinst = getSeriesinstByApplyinstId(aeaHiApplyinst.getApplyinstId(), startTime,
                                                                                     endTime);
                        if (aeaHiSeriesinst == null) {
                            continue;
                        }
                        AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper
                                .getAeaHiIteminstBySeriesinstId(aeaHiSeriesinst.getSeriesinstId());
                        AeaItemBasic aeaItemBasic = aeaItemBasicMapper
                                .getAeaItemBasicByItemVerId(aeaHiIteminst.getItemVerId(), rootOrgId);
                        ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInstMapper
                                .getProcessinstTimeruleInstByIteminstId(aeaHiIteminst.getIteminstId(), rootOrgId);
                        res.setActStoTimeruleInst(actStoTimeruleInst);
                        res.setAeaItemBasic(aeaItemBasic);

                    }
                    res.setAeaHiApplyinst(aeaHiApplyinst);
                    if(res.getActStoTimeruleInst() != null){
                        resList.add(res);
                    }
                } catch (Exception e) {
                    logger.debug("======处理窗口（" + window.getWindowId() + "）申报统计有误=======");
                    logger.error(e.getMessage());
                    continue;
                }
            }
            if (resList.size() == 0) {
                logger.debug("======窗口（" + window.getWindowId() + "）没有对应的申办数据======");
                continue;
            }
            WinUseTimeStatisticDo max = getMaxAngMin(resList)[0];
            WinUseTimeStatisticDo min = getMaxAngMin(resList)[1];
            AeaAnaWinUseTimeStatistics maxStatistic = parseWinUseTimeStatistic(window, max, statisticRecId, rootOrgId, startTime,
                                                                               endTime, creator, true);
            AeaAnaWinUseTimeStatistics minStatistic = parseWinUseTimeStatistic(window, min, statisticRecId, rootOrgId, startTime,
                                                                               endTime, creator, false);
            List<AeaAnaWinUseTimeStatistics> preStatistic = getPreStatisticByWindowId(window.getWindowId(), rootOrgId);
            if (!CollectionUtils.isEmpty(preStatistic)) {
                if(!preStatisticRecordIds.contains(preStatistic.get(0).getStatisticsRecordId())){
                    preStatisticRecordIds.add(preStatistic.get(0).getStatisticsRecordId());
                }
                aeaAnaWinUseTimeStatisticsMapper.deleteAeaAnaWinUseTimeStatisticsByWindow(window.getWindowId(), rootOrgId);
            }
            aeaAnaWinUseTimeStatisticsMapper.insertAeaAnaWinUseTimeStatistics(maxStatistic);
            aeaAnaWinUseTimeStatisticsMapper.insertAeaAnaWinUseTimeStatistics(minStatistic);
            hasRecord = true;
        }
        if (hasRecord) {
            for (String preStatisticRecordId : preStatisticRecordIds) {
                aeaAnaStatisticsRecordMapper.deleteAeaAnaStatisticsRecord(preStatisticRecordId);
            }
            AeaAnaStatisticsRecord aeaAnaStatisticsRecord = initAeaAnaStatisticsRecord(statisticRecId, creator, rootOrgId,
                                                                                       startTime, endTime);
            aeaAnaStatisticsRecordMapper.insertAeaAnaStatisticsRecord(aeaAnaStatisticsRecord);
        }

    }

    private List<AeaAnaWinUseTimeStatistics> getPreStatisticByWindowId(String windowId, String rootOrgId) {
        AeaAnaWinUseTimeStatistics temp = new AeaAnaWinUseTimeStatistics();
        temp.setRootOrgId(rootOrgId);
        temp.setWindowId(windowId);
        return aeaAnaWinUseTimeStatisticsMapper.listAeaAnaWinUseTimeStatistics(temp);
    }

    private AeaAnaStatisticsRecord initAeaAnaStatisticsRecord(String statisticRecordId,
                                                              String creator,
                                                              String rootOrgId,
                                                              Date startTime,
                                                              Date endTime) throws ParseException {
        AeaAnaStatisticsRecord aeaAnaStatisticsRecord = new AeaAnaStatisticsRecord();
        aeaAnaStatisticsRecord.setStatisticsRecordId(statisticRecordId);
        aeaAnaStatisticsRecord.setCreater(creator);
        aeaAnaStatisticsRecord.setCreateTime(new Date());
        aeaAnaStatisticsRecord.setReportId("0006");
        aeaAnaStatisticsRecord.setReportName("申报用时统计表");
        aeaAnaStatisticsRecord.setOperateSource("1");
        aeaAnaStatisticsRecord.setRootOrgId(rootOrgId);
        aeaAnaStatisticsRecord
                .setStatisticsStartDate(startTime == null ? new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-30") : startTime);
        aeaAnaStatisticsRecord.setStatisticsEndDate(endTime == null ? new Date() : endTime);
        aeaAnaStatisticsRecord.setStatisticsType("s");
        return aeaAnaStatisticsRecord;
    }

    private AeaAnaWinUseTimeStatistics parseWinUseTimeStatistic(AeaServiceWindow window,
                                                                WinUseTimeStatisticDo winUseTimeStatisticDo,
                                                                String statisticRecId, String rootOrgId, Date startTime,
                                                                Date endTime, String creator, boolean isMax) throws Exception {

        AeaAnaWinUseTimeStatistics statistic = new AeaAnaWinUseTimeStatistics();
        statistic.setUseTimeStatisticsId(UuidUtil.generateUuid());
        statistic.setCreater(creator);
        statistic.setCreateTime(new Date());
        statistic.setRootOrgId(rootOrgId);
        statistic.setStatisticsRecordId(statisticRecId);
        statistic.setWindowId(window.getWindowId());
        statistic.setWindowName(window.getWindowName());
        statistic.setRegionId(window.getRegionId());
        statistic.setRegionName(window.getRegionName());
        statistic.setUseTimeType(isMax ? "1" : "0");

        statistic.setApplyinstId(winUseTimeStatisticDo.getAeaHiApplyinst().getApplyinstId());
        statistic.setApplySource(winUseTimeStatisticDo.getAeaHiApplyinst().getApplyinstSource());
        statistic.setIsSeriesApprove(winUseTimeStatisticDo.getAeaHiApplyinst().getIsSeriesApprove());
        statistic.setIsCompleted(winUseTimeStatisticDo.getAeaHiApplyinst().getApplyinstState()
                                                      .equals(ApplyState.COMPLETED.getValue()) ? "1" : "0");
        AeaProjInfo proj = getStageProjByApplyinstId(statistic.getApplyinstId());
        if (proj == null) {
            throw new Exception("======该申报实例（" + statistic.getApplyinstId() + "）没有对应项目======");
        }
        statistic.setProjName(proj.getProjName());
        if (startTime != null && endTime != null) {
            statistic.setIsAllCount("0");
            statistic.setStatisticsStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-30"));
            statistic.setStatisticsEndDate(new Date());
        } else {
            statistic.setIsAllCount("1");
            statistic.setStatisticsStartDate(startTime);
            statistic.setStatisticsEndDate(endTime);
        }

        if (statistic.getIsSeriesApprove().equals("0")) {
            statistic.setStageName(winUseTimeStatisticDo.getAeaHiParStageinst().getStageName());
            statistic.setThemeName(winUseTimeStatisticDo.getAeaHiParStageinst().getThemeName());
        } else {
            statistic.setItemName(winUseTimeStatisticDo.getAeaItemBasic().getItemName());
            statistic.setItemProperty(winUseTimeStatisticDo.getAeaItemBasic().getItemProperty());
        }


        statistic.setOverdueTime(winUseTimeStatisticDo.getActStoTimeruleInst().getOverdueTime());
        statistic.setTimeLimit(winUseTimeStatisticDo.getActStoTimeruleInst().getTimeLimit());
        statistic.setRemainingTime(winUseTimeStatisticDo.getActStoTimeruleInst().getRemainingTime());
        statistic.setTimeruleUnit(winUseTimeStatisticDo.getActStoTimeruleInst().getTimeruleUnit());
        statistic.setTimeruleInstId(winUseTimeStatisticDo.getActStoTimeruleInst().getTimeruleInstId());
        statistic.setUseLimitTime(winUseTimeStatisticDo.getActStoTimeruleInst().getUseLimitTime());
        statistic.setUseTimeRate(statistic.getUseLimitTime() / statistic.getTimeLimit());
        return statistic;

    }

    private AeaProjInfo getStageProjByApplyinstId(String applyinstId) {
        if (StringUtils.isBlank(applyinstId)) {
            return null;
        }
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoByApplyinstId(applyinstId);
        return aeaProjInfo;
    }

    private WinUseTimeStatisticDo[] getMaxAngMin(List<WinUseTimeStatisticDo> resList) {

        WinUseTimeStatisticDo max = null;
        WinUseTimeStatisticDo min = null;
        for (WinUseTimeStatisticDo res : resList) {
            if (max == null) {
                max = new WinUseTimeStatisticDo();
                max.setActStoTimeruleInst(res.getActStoTimeruleInst());
                max.setAeaHiApplyinst(res.getAeaHiApplyinst());
                max.setAeaHiParStageinst(res.getAeaHiParStageinst());
                max.setAeaItemBasic(res.getAeaItemBasic());
            } else {
                if (res.getActStoTimeruleInst().getOverdueTime() > max.getActStoTimeruleInst().getOverdueTime()) {
                    max.setActStoTimeruleInst(res.getActStoTimeruleInst());
                    max.setAeaHiApplyinst(res.getAeaHiApplyinst());
                    max.setAeaHiParStageinst(res.getAeaHiParStageinst());
                    max.setAeaItemBasic(res.getAeaItemBasic());
                }
            }
            if (min == null) {
                min = new WinUseTimeStatisticDo();
                min.setActStoTimeruleInst(res.getActStoTimeruleInst());
                min.setAeaHiApplyinst(res.getAeaHiApplyinst());
                min.setAeaHiParStageinst(res.getAeaHiParStageinst());
                min.setAeaItemBasic(res.getAeaItemBasic());
            } else {
                if (res.getActStoTimeruleInst().getUseLimitTime() < min.getActStoTimeruleInst().getUseLimitTime()) {
                    min.setActStoTimeruleInst(res.getActStoTimeruleInst());
                    min.setAeaHiApplyinst(res.getAeaHiApplyinst());
                    min.setAeaHiParStageinst(res.getAeaHiParStageinst());
                    min.setAeaItemBasic(res.getAeaItemBasic());
                }
            }
        }
        return new WinUseTimeStatisticDo[]{max, min};
    }

    private AeaHiParStageinst getStageinstByApplyinstId(String applyinstId, Date startTime, Date endTime) {

        AeaHiParStageinst stageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
        if (startTime == null || endTime == null) {
            return stageinst;
        }
        if (stageinst != null && startTime.before(stageinst.getStartTime()) && endTime.after(stageinst.getEndTime())) {
            return stageinst;
        } else {
            return null;
        }

    }

    private AeaHiSeriesinst getSeriesinstByApplyinstId(String applyinstId, Date startTime, Date endTime) throws Exception {

        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstByApplyinstId(applyinstId);
        if (startTime == null || endTime == null) {
            return aeaHiSeriesinst;
        }
        if (aeaHiSeriesinst != null && startTime.before(aeaHiSeriesinst.getStartTime()) && endTime
                .after(aeaHiSeriesinst.getEndTime())) {
            return aeaHiSeriesinst;
        } else {
            return null;
        }

    }

    private List<AeaLogApplyStateHist> getApplyStateHistByWindow(List<AeaLogApplyStateHist> latestApplyStateHists,
                                                                 String windowId) {
        List<AeaLogApplyStateHist> list = new ArrayList<>();
        latestApplyStateHists.forEach((his) -> {
            if (windowId.equals(his.getOpsWindowId())) {
                list.add(his);
            }
        });
        return list;
    }

    @Data
    private class WinUseTimeStatisticDo {
        AeaHiApplyinst aeaHiApplyinst;
        ActStoTimeruleInst actStoTimeruleInst;
        AeaHiParStageinst aeaHiParStageinst;
        AeaItemBasic aeaItemBasic;

    }

}
