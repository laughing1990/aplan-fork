package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.efficiency.service.AeaAnaOrgUseTimeStatisticService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/9/18 018 16:16
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaAnaOrgUseTimeStatisticServiceImpl implements AeaAnaOrgUseTimeStatisticService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaOrgUseTimeStatisticServiceImpl.class);

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaAnaOrgUseTimeStatisticsMapper aeaAnaOrgUseTimeStatisticsMapper;

    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Autowired
    private AeaAnaStatisticsRecordMapper aeaAnaStatisticsRecordMapper;


    @Override
    public void doUseTimeStatistics(String rootOrgId, String creator, Date startTime, Date endTime) throws Exception {
        if (StringUtils.isBlank(rootOrgId) || StringUtils.isBlank(creator)) {
            throw new Exception("缺少必要参数");
        }
        logger.info("========统计机构办件最长和最短用时========");
        String statisticRecordId = UuidUtil.generateUuid();
        List<String> preStatisticRecordIds = new ArrayList<>();
        List<AeaItemBasic> orgInfoList = aeaItemBasicMapper.listOrgInfoByRootOrgId(rootOrgId);
        boolean hasRec = false;

        for (AeaItemBasic orgInfo : orgInfoList) {
            List<AeaItemBasic> aeaItemBasicsForeachOrg = aeaItemBasicMapper
                    .listLatestVerAeaItemBasicByOrgId(orgInfo.getOrgId(), rootOrgId);
            if (CollectionUtils.isEmpty(aeaItemBasicsForeachOrg)) {
                continue;
            }
            List<UseTimeStatisticDo> useTimeStatisticDos = new ArrayList<>();
            for (AeaItemBasic itemInSameOrg : aeaItemBasicsForeachOrg) {
                itemInSameOrg.setRegionName(orgInfo.getRegionName());
                itemInSameOrg.setOrgName(orgInfo.getOrgName());
                AeaHiIteminst temp = new AeaHiIteminst();
                temp.setRootOrgId(rootOrgId);
                temp.setItemId(itemInSameOrg.getItemId());
                temp.setItemVerId(itemInSameOrg.getItemVerId());
                temp.setStartTime(startTime);
                temp.setEndTime(endTime);
                temp.setIsSeriesApprove("1");
                List<AeaHiIteminst> iteminsts = aeaHiIteminstMapper.listAeaHiIteminst(temp);
                for (AeaHiIteminst iteminst : iteminsts) {
                    ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInstMapper
                            .getProcessinstTimeruleInstByIteminstId(iteminst.getIteminstId(), rootOrgId);
                    if (actStoTimeruleInst == null) {
                        continue;
                    }
                    UseTimeStatisticDo utsd = new UseTimeStatisticDo();
                    utsd.init(itemInSameOrg, iteminst, actStoTimeruleInst);
                    useTimeStatisticDos.add(utsd);
                }
            }
            if (useTimeStatisticDos.size() <= 0) {
                continue;
            }
            UseTimeStatisticDo[] maxAndMin = getMaxAndMin(useTimeStatisticDos);
            AeaAnaOrgUseTimeStatistics max = convertToStatistic(maxAndMin[0], statisticRecordId, creator, rootOrgId, startTime,
                                                                endTime, true);
            List<AeaAnaOrgUseTimeStatistics> preStatistic = getPreStatisticByOrgId(max.getOrgId(), rootOrgId);
            if(!CollectionUtils.isEmpty(preStatistic)){
                if(!preStatisticRecordIds.contains(preStatistic.get(0).getStatisticsRecordId())){
                    preStatisticRecordIds.add(preStatistic.get(0).getStatisticsRecordId());
                }
                aeaAnaOrgUseTimeStatisticsMapper.deleteAeaAnaOrgUseTimeStatisticsByOrgId(max.getOrgId(), rootOrgId);
            }

            aeaAnaOrgUseTimeStatisticsMapper.insertAeaAnaOrgUseTimeStatistics(max);
            if (maxAndMin[1] != null) {
                AeaAnaOrgUseTimeStatistics min = convertToStatistic(maxAndMin[1], statisticRecordId, creator, rootOrgId,
                                                                    startTime, endTime, false);
                aeaAnaOrgUseTimeStatisticsMapper.insertAeaAnaOrgUseTimeStatistics(min);
            }
            hasRec = true;
        }
        if (hasRec) {
            for (String preStatisticRecordId : preStatisticRecordIds) {
                aeaAnaStatisticsRecordMapper.deleteAeaAnaStatisticsRecord(preStatisticRecordId);
            }
            AeaAnaStatisticsRecord statisticsRecord = initAeaAnaStatisticsRecord(statisticRecordId, creator, rootOrgId, startTime,
                                                                                 endTime);
            aeaAnaStatisticsRecordMapper.insertAeaAnaStatisticsRecord(statisticsRecord);
        }
        return;
    }

    private List<AeaAnaOrgUseTimeStatistics> getPreStatisticByOrgId(String orgId,String rootOrgId){
        AeaAnaOrgUseTimeStatistics temp = new AeaAnaOrgUseTimeStatistics();
        temp.setRootOrgId(rootOrgId);
        temp.setOrgId(orgId);
        return  aeaAnaOrgUseTimeStatisticsMapper.listAeaAnaOrgUseTimeStatistics(temp);
    }

    private AeaAnaOrgUseTimeStatistics convertToStatistic(UseTimeStatisticDo utsd, String statisticRecordId, String creator,
                                                          String rootOrgId, Date startTime, Date endTime,
                                                          boolean isMax) throws Exception {
        AeaAnaOrgUseTimeStatistics statistics = new AeaAnaOrgUseTimeStatistics();
        statistics.setUseTimeStatisticsId(UuidUtil.generateUuid());
        statistics.setStatisticsRecordId(statisticRecordId);
        statistics.setCreater(creator);
        statistics.setCreateTime(new Date());
        statistics.setRootOrgId(rootOrgId);
        statistics.setItemName(utsd.getAeaItemBasic().getItemName());
        statistics.setItemProperty(utsd.getAeaItemBasic().getItemProperty());
        statistics.setOrgId(utsd.getAeaItemBasic().getOrgId());
        statistics.setOrgName(utsd.getAeaItemBasic().getOrgName());
        statistics.setRegionId(utsd.getAeaItemBasic().getRegionId());
        statistics.setRegionName(utsd.getAeaItemBasic().getRegionName());
        statistics.setIteminstId(utsd.getAeaHiIteminst().getIteminstId());
        statistics.setItemId(utsd.getAeaHiIteminst().getItemId());
        statistics.setTimeruleInstId(utsd.getActStoTimeruleInst().getTimeruleInstId());
        statistics.setTimeLimit(utsd.getActStoTimeruleInst().getTimeLimit());
        statistics.setTimeruleUnit(utsd.getActStoTimeruleInst().getTimeruleUnit());
        statistics.setUseLimitTime(utsd.getActStoTimeruleInst().getUseLimitTime());
        statistics.setRemainingTime(utsd.getActStoTimeruleInst().getRemainingTime());
        statistics.setOverdueTime(utsd.getActStoTimeruleInst().getOverdueTime());
        statistics.setUseTimeRate(utsd.getActStoTimeruleInst().getUseLimitTime() / utsd.getActStoTimeruleInst().getTimeLimit());
        AeaProjInfo aeaProjInfo = getProjBySeriesinstId(utsd.getAeaHiIteminst().getSeriesinstId());
        if (aeaProjInfo == null) {
            throw new Exception("======没有关联项目信息======");

        } else {
            statistics.setProjName(aeaProjInfo.getProjName());
        }
        if (isMax) {
            statistics.setIsCompleted(isCompleted(utsd.getAeaHiIteminst().getIteminstState()) ? "1" : "0");
        }
        statistics.setUseTimeType(isMax ? "1" : "0");
        if (startTime == null || endTime == null) {
            statistics.setIsAllCount("1");
        } else {
            statistics.setIsAllCount("0");
            statistics.setStatisticsStartDate(startTime);
            statistics.setStatisticsEndDate(endTime);
        }
        return statistics;
    }

    private AeaProjInfo getProjBySeriesinstId(String seriesinstId) throws Exception {
        if (StringUtils.isBlank(seriesinstId)) {
            return null;
        }
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstById(seriesinstId);
        if (aeaHiSeriesinst == null) {
            return null;
        }
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoByApplyinstId(aeaHiSeriesinst.getApplyinstId());
        return aeaProjInfo;
    }

    private UseTimeStatisticDo[] getMaxAndMin(List<UseTimeStatisticDo> useTimeStatisticDos) {
        UseTimeStatisticDo max = null;
        UseTimeStatisticDo min = null;
        for (UseTimeStatisticDo utsd : useTimeStatisticDos) {
            if (max == null) {
                max = utsd;
            } else if (max.getActStoTimeruleInst().getUseLimitTime() < utsd.getActStoTimeruleInst().getUseLimitTime()) {
                max = utsd;
            }
            if (!isCompleted(utsd.getAeaHiIteminst().getIteminstState())) {
                continue;
            }
            if (min == null) {
                min = utsd;
            } else if (min.getActStoTimeruleInst().getUseLimitTime() > utsd.getActStoTimeruleInst().getUseLimitTime()) {
                min = utsd;
            }
        }
        return new UseTimeStatisticDo[]{max, min};
    }

    private boolean isCompleted(String state) {
        return state.equals(ItemStatus.AGREE.getValue()) || state.equals(ItemStatus.AGREE_TOLERANCE.getValue()) || state
                .equals(ItemStatus.DISAGREE.getValue());
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
        aeaAnaStatisticsRecord.setReportId("0005");
        aeaAnaStatisticsRecord.setReportName("办件用时统计表");
        aeaAnaStatisticsRecord.setOperateSource("1");
        aeaAnaStatisticsRecord.setRootOrgId(rootOrgId);
        aeaAnaStatisticsRecord
                .setStatisticsStartDate(startTime == null ? new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-30") : startTime);
        aeaAnaStatisticsRecord.setStatisticsEndDate(endTime == null ? new Date() : endTime);
        aeaAnaStatisticsRecord.setStatisticsType("b");
        return aeaAnaStatisticsRecord;
    }

    @Data
    private class UseTimeStatisticDo {

        AeaItemBasic aeaItemBasic;
        AeaHiIteminst aeaHiIteminst;
        ActStoTimeruleInst actStoTimeruleInst;

        public void init(AeaItemBasic aeaItemBasicsForeachOrg, AeaHiIteminst aeaHiIteminst,
                         ActStoTimeruleInst actStoTimeruleInst) {
            this.setActStoTimeruleInst(actStoTimeruleInst);
            this.setAeaItemBasic(aeaItemBasicsForeachOrg);
            this.setAeaHiIteminst(aeaHiIteminst);
        }
    }


}
