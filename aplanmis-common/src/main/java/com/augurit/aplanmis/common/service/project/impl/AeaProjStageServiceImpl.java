package com.augurit.aplanmis.common.service.project.impl;

import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.aplanmis.common.constants.AeaItemBasicContants;
import com.augurit.aplanmis.common.constants.AeaParStageConstants;
import com.augurit.aplanmis.common.constants.AeaProjStageConstants;
import com.augurit.aplanmis.common.constants.StandardStageCode;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.project.AeaProjStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinlf
 * @Date 2019/9/5
 */
@Service
@Transactional
public class AeaProjStageServiceImpl implements AeaProjStageService {

    @Autowired
    AeaProjStageMapper aeaProjStageMapper;

    @Autowired
    AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemMapper aeaItemMapper;

    @Autowired
    private WorkdayHolidayService workdayHolidayService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Override
    public Map<String, String> projAllStageState(String projInfoId) {
        return null;
    }

    @Override
    public String projStageState(String projInfoId, String standardSortNo) {
        AeaProjStage aeaProjStage = aeaProjStageMapper.getAeaParStageByProjInfoIdAndStandardSortNo(projInfoId, standardSortNo);
        if (aeaProjStage != null) {
            return aeaProjStage.getStageState();
        } else {
            return AeaProjStageConstants.STAGE_STATE_NONAPPLY;
        }
    }

    @Override
    public void stageApplyUpdateAeaProjStageState(String applyinstId, String userId) {
        //是否是并联申报。非并联申报不计算
        ProjStageForm projStageForm = aeaProjStageMapper.getProjInfoIdAndStageIdByApplyinstId(applyinstId);
        if (projStageForm == null) {
            return;
        }
        String projInfoId = projStageForm.getProjInfoId();
        String standardSortNo = projStageForm.getStandardSortNo();
        saveOrUpdateProjStage(userId, projInfoId, standardSortNo);
    }

    private void saveOrUpdateProjStage(String userId, String projInfoId, String standardSortNo) {
        //查询项目申报阶段记录是否存在
        AeaProjStage aeaProjStage = aeaProjStageMapper.getAeaParStageByProjInfoIdAndStandardSortNo(projInfoId, standardSortNo);
        if (aeaProjStage != null) {
            if (AeaProjStageConstants.STAGE_STATE_NONAPPLY.equals(aeaProjStage.getStageState())) {
                AeaProjStage newAeaProjStage = new AeaProjStage();
                newAeaProjStage.setProjStageId(aeaProjStage.getProjStageId());
                newAeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_APPROVAL);
                aeaProjStageMapper.updateAeaProjStage(newAeaProjStage);
            }
        } else {
            AeaProjStage newAeaProjStage = new AeaProjStage();
            newAeaProjStage.setProjStageId(UUID.randomUUID().toString());
            newAeaProjStage.setProjInfoId(projInfoId);
            newAeaProjStage.setStandardSortNo(standardSortNo);
            Date now = new Date();
            //如果日志表没有记录到预受理状态可能导致记录时间错误
            Date firstApplyTime = this.getProjFirstApplyStageTime(projInfoId, standardSortNo);
            if (firstApplyTime == null) {
                firstApplyTime = now;
            }
            newAeaProjStage.setFirstApplyTime(firstApplyTime);
            newAeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_APPROVAL);
            newAeaProjStage.setCreater(userId);
            newAeaProjStage.setCreateTime(now);
            aeaProjStageMapper.insertAeaProjStage(newAeaProjStage);
        }
    }

    @Override
    public void stageApplyUpdateAeaProjStageState(String stageId, String projInfoId, String userId) {
        AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
        if (stage == null) {
            return;
        }
        saveOrUpdateProjStage(userId, projInfoId, stage.getDybzspjdxh());
    }

    @Override
    public void itemCompletedUpdateProjStageState(String iteminstId, String orgId, String userId) {
        //是否是并联申报的事项。非并联申报的事项不触发计算。
        ProjStageForm projStageForm = aeaProjStageMapper.getProjInfoIdAndStageIdByIteminstId(iteminstId);
        if (projStageForm == null) {
            return;
        }
        String projInfoId = projStageForm.getProjInfoId();
        String standardSortNo = projStageForm.getStandardSortNo();
        //判断事项是否是里程碑事项。非里程碑事项不影响阶段办结状态。
        AeaHiIteminst aeaHiIteminst;
        try {
            aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(aeaHiIteminst.getItemVerId(), orgId);
            if (aeaItemBasic != null && AeaItemBasicContants.IS_MILESTONE_ITEM_NO.equals(aeaItemBasic.getIsMilestoneItem())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        //判断项目的阶段是否已经办结
        AeaProjStage aeaProjStage = aeaProjStageMapper.getAeaParStageByProjInfoIdAndStandardSortNo(projInfoId, standardSortNo);
        if (aeaProjStage != null) {
            if (AeaProjStageConstants.STAGE_STATE_COMPLETED.equals(aeaProjStage.getStageState())) {
                //阶段已办结，不进行是否办结判断
                return;
            }
        } else {
            //已发起申报未触发保存的
            aeaProjStage = new AeaProjStage();
            aeaProjStage.setProjStageId(UUID.randomUUID().toString());
            aeaProjStage.setProjInfoId(projInfoId);
            aeaProjStage.setStandardSortNo(standardSortNo);
            Date now = new Date();
            Date firstApplyTime = this.getProjFirstApplyStageTime(projInfoId, standardSortNo);
            aeaProjStage.setFirstApplyTime(firstApplyTime);
            aeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_APPROVAL);
            aeaProjStage.setCreater(userId);
            aeaProjStage.setCreateTime(now);
            aeaProjStageMapper.insertAeaProjStage(aeaProjStage);
        }
        //计算阶段是否办结：获取项目最新申报的阶段版本办结条件，查询所有阶段版本申报已经办结的事项，满足办结条件则办结。\
        boolean stageEnd = false;
        List<AeaHiParStageinst> aeaHiParStageinstList = aeaProjStageMapper.getStageinstByProjInfoIdAndStanrdSortNo(projInfoId, standardSortNo);
        if (aeaHiParStageinstList == null || aeaHiParStageinstList.size() <= 0) {
            //不可能进来
            return;
        }
        String lastStageId = aeaHiParStageinstList.get(0).getStageId();
        //获取阶段里程碑事项类型
        AeaParStage stage = aeaParStageMapper.getAeaParStageById(lastStageId);
        String lcbsxlx = stage.getLcbsxlx();
        //查询阶段下所有并联里程碑事项
        List<AeaItemBasic> milestoneItemList = aeaProjStageMapper.getAllMilestoneItemByStageId(lastStageId);
        if (milestoneItemList == null || milestoneItemList.size() <= 0) {
            //没有配置里程碑事项不办结。
            return;
        }
        //阶段有一个里程碑事项办结阶段办结，并且当前办结的事项是在最后申报阶段的里程碑事项里面。
        if (AeaParStageConstants.LCBSXLX_ONE.equals(lcbsxlx)) {
            for (AeaItemBasic item : milestoneItemList) {
                String isCatalog = item.getIsCatalog();
                if (AeaItemBasicContants.IS_CATALOG_YES.equals(isCatalog)) {
                    List<AeaItem> doItemList = aeaItemMapper.getAeaItemByParentItemId(item.getItemId());
                    List<String> itemIds = doItemList.stream().map(AeaItem::getItemId).collect(Collectors.toList());
                    if (itemIds.contains(aeaHiIteminst.getItemId())) {
                        stageEnd = true;
                        break;
                    } else {
                        List<AeaHiIteminst> endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, itemIds.toArray(new String[itemIds.size()]));
                        stageEnd = endIteminstList != null && endIteminstList.size() > 0;
                        if (stageEnd) {
                            break;
                        }
                    }
                } else {
                    if (item.getItemId().equals(aeaHiIteminst.getItemId())) {
                        stageEnd = true;
                        break;
                    } else {
                        List<AeaHiIteminst> endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, new String[]{item.getItemId()});
                        stageEnd = endIteminstList != null && endIteminstList.size() > 0;
                        if (stageEnd) {
                            break;
                        }
                    }
                }
            }
        } else {
            //阶段所有里程碑事项办结才阶段办结
            stageEnd = true;
            for (AeaItemBasic item : milestoneItemList) {
                String isCatalog = item.getIsCatalog();
                if (AeaItemBasicContants.IS_CATALOG_YES.equals(isCatalog)) {
                    List<AeaItem> doItemList = aeaItemMapper.getAeaItemByParentItemId(item.getItemId());
                    List<String> itemIds = doItemList.stream().map(AeaItem::getItemId).collect(Collectors.toList());
                    //去除当前办结通过事项
                    if (itemIds.contains(aeaHiIteminst.getItemId())) {
                        continue;
                    }
                    List<AeaHiIteminst> endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, itemIds.toArray(new String[itemIds.size()]));
                    boolean notEnd = endIteminstList == null || endIteminstList.isEmpty();
                    if (notEnd) {
                        //有里程碑事项未办结，阶段未办结
                        stageEnd = false;
                        break;
                    }
                } else {
                    //去除当前办结通过事项
                    if (item.getItemId().equals(aeaHiIteminst.getItemId())) {
                        continue;
                    }
                    List<AeaHiIteminst> endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, new String[]{item.getItemId()});
                    boolean notEnd = endIteminstList == null || endIteminstList.isEmpty();
                    if (notEnd) {
                        //有里程碑事项未办结，阶段未办结
                        stageEnd = false;
                        break;
                    }
                }
            }

        }
        //阶段办结
        if (stageEnd) {
            updateAeaProjStageCompleted(aeaProjStage, orgId);
        }
    }

    private void updateAeaProjStageCompleted(AeaProjStage aeaProjStage, String orgId) {
        AeaProjStage newAeaProjStage = new AeaProjStage();
        newAeaProjStage.setProjStageId(aeaProjStage.getProjStageId());
        newAeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_COMPLETED);
        //计算阶段审批用时和跨度用时。目前公式为：审批用时=跨度用时-非工作日。以后需要调整
        Date now = new Date();
        newAeaProjStage.setPassTime(now);
        Integer daysBetween = DateUtils.daysBetween(now, aeaProjStage.getFirstApplyTime());
        Integer workDays = workdayHolidayService.calNumOfWorkDays(aeaProjStage.getFirstApplyTime(), now, orgId);
        newAeaProjStage.setPassNatureDays(daysBetween.toString());
        newAeaProjStage.setPassWorkingDays(workDays.toString());
        aeaProjStageMapper.updateAeaProjStage(newAeaProjStage);
    }

    @Override
    public Date getProjFirstApplyStageTime(String projInfoId, String standardSortNo) {

        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getProjFirstApplyStageLog(projInfoId, standardSortNo);
        if (aeaLogApplyStateHist != null) {
            return aeaLogApplyStateHist.getTriggerTime();
        }
        return null;
    }

    @Override
    public List<AeaProjStage> findProjStageByTimeRange(Date startTime, Date endTime) {
        return aeaProjStageMapper.findProjStageByTimeRange(startTime, endTime);
    }

    @Override
    public Date getPassTimeByProjInfoId(String projInfoId) {
        AeaProjStage condi = new AeaProjStage();
        condi.setProjInfoId(projInfoId);
        condi.setStageState(AeaProjStageConstants.STAGE_STATE_COMPLETED);
        //查询已经办结的阶段
        List<AeaProjStage> list = aeaProjStageMapper.listAeaProjStage(condi);
        if (list == null || list.size() == 0) {
            return null;
        }
        //阶段标准序号按‘,’分割后去重
        Set<String> standardSortNoSet = new HashSet<>();
        list.stream().forEach(aeaProjStage -> {
            String standardSortNo = aeaProjStage.getStandardSortNo();
            String[] split = standardSortNo.trim().split(",");
            for (String sortNo : split) {
                standardSortNoSet.add(sortNo);
            }
        });
        //判断是否保护了所有阶段1，2，3，4
        for (StandardStageCode standardStageCode : StandardStageCode.values()) {
            if (!standardSortNoSet.contains(standardStageCode.getValue())) {
                return null;
            }
        }
        //返回最新阶段的办结时间作为项目的办结时间
        return list.get(0).getPassTime();
    }

    @Override
    public void calculateAllProjStageState() {
        //查询所有项目的申报阶段
        List<ProjStageApplyForm> projStageApplyForm = aeaProjStageMapper.findAllAeaProjStageApply();
        List<AeaProjStage> AeaProjStages = this.convert2AeaProjStage(projStageApplyForm);
        for (AeaProjStage aeaProjStage : AeaProjStages) {
            String projInfoId = aeaProjStage.getProjInfoId();
            String standardSortNo = aeaProjStage.getStandardSortNo();
            //申报时间
            AeaProjStage aps = aeaProjStageMapper.getAeaParStageByProjInfoIdAndStandardSortNo(projInfoId, standardSortNo);
            if (aps == null) {
                aeaProjStage.setProjStageId(UUID.randomUUID().toString());
                Date now = new Date();
                aeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_APPROVAL);
                //htry
                aeaProjStage.setCreater("4df31dde-9ed6-4f7c-968b-aa22fdd9ab7f");
                aeaProjStage.setCreateTime(now);
                aeaProjStageMapper.insertAeaProjStage(aeaProjStage);
            } else {
                aeaProjStage.setProjStageId(aps.getProjStageId());
            }
            //办结时间
            //查询阶段的里程碑事项
            ProjStageApplyForm lastApplyStageinst = aeaProjStage.getLastApplyStageinst();
            String lcbsxlx = lastApplyStageinst.getLcbsxlx();
            //查询阶段下所有并联里程碑事项
            List<AeaItemBasic> milestoneItemList = aeaProjStageMapper.getAllMilestoneItemByStageId(lastApplyStageinst.getStageId());
            if (milestoneItemList == null || milestoneItemList.size() <= 0) {
                //没有配置里程碑事项不办结。
                continue;
            }
            //查询事项有没有办结
            boolean stageEnd = false;
            Date passTime = null;
            //阶段有一个里程碑事项办结阶段办结，并且当前办结的事项是在最后申报阶段的里程碑事项里面。
            //实施事项
            List<String> carryOut = new ArrayList<>();
            if (AeaParStageConstants.LCBSXLX_ONE.equals(lcbsxlx)) {
                for (AeaItemBasic item : milestoneItemList) {
                    String isCatalog = item.getIsCatalog();
                    if (AeaItemBasicContants.IS_CATALOG_YES.equals(isCatalog)) {
                        List<AeaItem> doItemList = aeaItemMapper.getAeaItemByParentItemId(item.getItemId());
                        List<String> itemIds = doItemList.stream().map(AeaItem::getItemId).collect(Collectors.toList());
                        carryOut.addAll(itemIds);
                    } else {
                        carryOut.add(item.getItemId());
                    }
                }
                List<AeaHiIteminst> endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, carryOut.toArray(new String[carryOut.size()]));
                stageEnd = endIteminstList != null && endIteminstList.size() > 0;
                if (stageEnd) {
                    //取事项实例状态历史
                    List<String> iteminstIds = endIteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());
                    List<AeaLogItemStateHist> aeaLogItemStateHistList = aeaLogItemStateHistService.findItemComplateByIteminstId(iteminstIds.toArray(new String[iteminstIds.size()]));
                    if (aeaLogItemStateHistList != null && aeaLogItemStateHistList.size() > 0) {
                        //最早的里程碑事项办结时间作为阶段办结时间
                        passTime = aeaLogItemStateHistList.get(0).getTriggerTime();
                    }
                    //取事项实例的END_TIME
                   /* List<Date> dates = endIteminstList.stream().map(AeaHiIteminst::getEndTime).sorted().collect(Collectors.toList());
                    passTime = dates.get(0);*/
                }
            } else {
                //阶段所有里程碑事项办结才阶段办结
                stageEnd = true;
                //各里程碑事项最早办结时间
                List<Date> passTimes = new ArrayList<>();
                List<AeaHiIteminst> endIteminstList = new ArrayList<>();
                for (AeaItemBasic item : milestoneItemList) {
                    String isCatalog = item.getIsCatalog();
                    if (AeaItemBasicContants.IS_CATALOG_YES.equals(isCatalog)) {
                        List<AeaItem> doItemList = aeaItemMapper.getAeaItemByParentItemId(item.getItemId());
                        List<String> itemIds = doItemList.stream().map(AeaItem::getItemId).collect(Collectors.toList());
                        endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, itemIds.toArray(new String[itemIds.size()]));
                    } else {
                        endIteminstList = aeaProjStageMapper.findEndIteminstByProjInfoAndItemIds(projInfoId, new String[]{item.getItemId()});
                    }
                    boolean notEnd = endIteminstList == null || endIteminstList.isEmpty();
                    if (notEnd) {
                        //有里程碑事项未办结，阶段未办结
                        stageEnd = false;
                        break;
                    }
                    //取事项实例状态历史
                    List<String> iteminstIds = endIteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());
                    List<AeaLogItemStateHist> aeaLogItemStateHistList = aeaLogItemStateHistService.findItemComplateByIteminstId(iteminstIds.toArray(new String[iteminstIds.size()]));
                    if (aeaLogItemStateHistList != null && aeaLogItemStateHistList.size() > 0) {
                        //最早的事项办结时间作为事项办结时间
                        passTimes.add(aeaLogItemStateHistList.get(0).getTriggerTime());
                    }
                    //取事项实例的END_TIME
                    /*List<Date> dates = endIteminstList.stream().map(AeaHiIteminst::getEndTime).sorted().collect(Collectors.toList());
                    passTimes.add(dates.get(0));*/
                }
                if (stageEnd) {
                    if (passTimes.size() > 0) {
                        List<Date> sortPassTimes = passTimes.stream().sorted().collect(Collectors.toList());
                        passTime = sortPassTimes.get(sortPassTimes.size() - 1);
                    }
                }
            }
            //阶段办结
            if (stageEnd) {
                AeaProjStage newAeaProjStage = new AeaProjStage();
                newAeaProjStage.setProjStageId(aeaProjStage.getProjStageId());
                newAeaProjStage.setStageState(AeaProjStageConstants.STAGE_STATE_COMPLETED);
                //办结时间
                if (passTime != null) {
                    newAeaProjStage.setPassTime(passTime);
                    //计算阶段审批用时和跨度用时。目前公式为：审批用时=跨度用时-非工作日。以后需要调整
                    Integer daysBetween = DateUtils.daysBetween(passTime, aeaProjStage.getFirstApplyTime());
                    Integer workDays = workdayHolidayService.calNumOfWorkDays(aeaProjStage.getFirstApplyTime(), passTime, "0368948a-1cdf-4bf8-a828-71d796ba89f6");
                    newAeaProjStage.setPassNatureDays(daysBetween.toString());
                    newAeaProjStage.setPassWorkingDays(workDays.toString());
                }
                aeaProjStageMapper.updateAeaProjStage(newAeaProjStage);
            }
        }
    }

    private List<AeaProjStage> convert2AeaProjStage(List<ProjStageApplyForm> projStageApplyFormList) {
        List<AeaProjStage> dtos = new ArrayList<>();
        Map<String, AeaProjStage> unimportantMap = new HashMap();
        for (ProjStageApplyForm form : projStageApplyFormList) {
            String unionKey = form.getProjInfoId() + ":" + form.getDybzspjdxh();
            AeaProjStage aeaProjStage = unimportantMap.get(unionKey);
            if (aeaProjStage == null) {
                aeaProjStage = new AeaProjStage();
                aeaProjStage.setProjInfoId(form.getProjInfoId());
                aeaProjStage.setStandardSortNo(form.getDybzspjdxh());
                aeaProjStage.setFirstApplyTime(form.getAcceptTime());
                aeaProjStage.setLastApplyStageinst(form);
                unimportantMap.put(unionKey, aeaProjStage);
            } else {
                if (form.getAcceptTime().before(aeaProjStage.getFirstApplyTime())) {
                    aeaProjStage.setFirstApplyTime(form.getAcceptTime());
                } else {
                    aeaProjStage.setLastApplyStageinst(form);
                }
            }
            aeaProjStage.addForm(form);
            dtos.add(aeaProjStage);
        }
        return dtos;
    }
}
