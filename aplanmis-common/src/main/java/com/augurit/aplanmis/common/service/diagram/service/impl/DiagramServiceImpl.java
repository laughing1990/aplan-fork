package com.augurit.aplanmis.common.service.diagram.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaProjStageConstants;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaProjStage;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSeriesinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaProjStageMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import com.augurit.aplanmis.common.service.diagram.dto.DiagramStatusDto;
import com.augurit.aplanmis.common.service.diagram.dto.MatAttachDto;
import com.augurit.aplanmis.common.service.diagram.helper.DiagramHelper;
import com.augurit.aplanmis.common.service.diagram.qo.MatAttachQo;
import com.augurit.aplanmis.common.service.diagram.service.DiagramService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjStageService;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DiagramServiceImpl implements DiagramService {

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;
    @Autowired
    private AeaProjStageService aeaProjStageService;
    @Autowired
    private AeaProjStageMapper aeaProjStageMapper;

    @Override
    public DiagramStatusDto getGlobalDiagramStatus(String projInfoId, String themeId) throws Exception {
        Assert.state(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        Assert.state(StringUtils.isNotBlank(themeId), "themeId is null");

        log.info("Query project diagram, projectId: {}, themeId: {}", projInfoId, themeId);

        AeaParThemeVer aeaParThemeVer = queryLatestAeaParThemeVer(themeId);
        if (aeaParThemeVer == null || StringUtils.isBlank(aeaParThemeVer.getThemeVerDiagram())) {
//            throw new Exception("aeaParThemeVer not found or diagramJson is empty");
            log.info("aeaParThemeVer not found or diagramJson is empty");
            return new DiagramStatusDto();
        }
        String themeVerId = aeaParThemeVer.getThemeVerId();
        DiagramHelper diagramHelper = DiagramHelper.newOne()
                .themeVerId(themeVerId)
                .diagramJson(aeaParThemeVer.getThemeVerDiagram());

        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        // 项目中已绑定的主题id
        if (StringUtils.isNotBlank(aeaProjInfo.getThemeId())) {
            log.info("获取项目历史办理信息");

            // 申报实例id
            AeaApplyinstProj param = new AeaApplyinstProj();
            param.setProjInfoId(projInfoId);
            Set<String> applyinstIds = aeaApplyinstProjMapper.listAeaApplyinstProj(param).stream().map(AeaApplyinstProj::getApplyinstId).collect(Collectors.toSet());
            if (applyinstIds.size() > 0) {
                List<AeaHiApplyinst> aeaHiApplyinsts = aeaHiApplyinstMapper.listAeaHiApplyinstByIds(Lists.newArrayList(applyinstIds));
                Map<String, List<AeaHiApplyinst>> groupByIsSeries = aeaHiApplyinsts.stream().collect(Collectors.groupingBy(AeaHiApplyinst::getIsSeriesApprove));
                // 并联与单项申报实例
                List<AeaHiApplyinst> stageApplyinst = groupByIsSeries.get("0");
                List<AeaHiApplyinst> seriesApplyinst = groupByIsSeries.get("1");

                List<AeaHiIteminst> allIteminstList = new ArrayList<>();
                Set<String> allStageIds = new HashSet<>();

                // 单项申报
                List<AeaHiIteminst> distinctedSimpleIteminsts = null;
                if (seriesApplyinst != null && seriesApplyinst.size() > 0) {
                    List<String> itemApplyinstIds = seriesApplyinst.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());

                    List<String> seriesinstIds = aeaHiSeriesinstMapper.listAeaHiSeriesinstByApplyinstIds(itemApplyinstIds).stream()
                            .peek(s -> {
                                if (StringUtils.isNotBlank(s.getStageId())) {
                                    allStageIds.add(s.getStageId());
                                }
                            })
                            .map(AeaHiSeriesinst::getSeriesinstId).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(seriesinstIds)) {
                        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.listAeaHiiteminstListBySeriesinstIds(seriesinstIds);

                        distinctedSimpleIteminsts = distinctByItemIdAndCreateTime(aeaHiIteminsts);
                        allIteminstList.addAll(distinctedSimpleIteminsts);
                    }
                }

                // 并联申报
                List<AeaHiParStageinst> aeaHiParStageinsts = null;
                List<AeaParStage> aeaParStages = null;
                final Map<String, List<AeaHiIteminst>> stageinstIteminstMap = new HashMap<>();
                if (stageApplyinst != null && stageApplyinst.size() > 0) {
                    List<String> stageApplyinstIds = stageApplyinst.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());
                    aeaHiParStageinsts = aeaHiParStageinstMapper.listAeaHiParStageinstByApplyinstIds(stageApplyinstIds);
                    if (CollectionUtils.isNotEmpty(aeaHiParStageinsts)) {
                        allStageIds.addAll(aeaHiParStageinsts.stream().map(AeaHiParStageinst::getStageId).collect(Collectors.toSet()));

                        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.listAeaHiIteminstListByStageinstIds(aeaHiParStageinsts.stream().map(AeaHiParStageinst::getStageinstId).collect(Collectors.toList()));

                        List<AeaHiIteminst> distinctedIteminsts = distinctByItemIdAndCreateTime(aeaHiIteminsts);
                        Map<String, AeaHiParStageinst> stageinstMap = new HashMap<>();
                        aeaHiParStageinsts.forEach(inst -> stageinstMap.put(inst.getStageinstId(), inst));
                        distinctedIteminsts.forEach(iteminst -> {
                            AeaHiParStageinst aeaHiParStageinst = stageinstMap.get(iteminst.getStageinstId());
                            String stageinstId = aeaHiParStageinst.getStageinstId();
                            if (stageinstIteminstMap.get(stageinstId) != null) {
                                stageinstIteminstMap.get(stageinstId).add(iteminst);
                            } else {
                                List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
                                aeaHiIteminstList.add(iteminst);
                                stageinstIteminstMap.put(stageinstId, aeaHiIteminstList);
                            }
                        });
                        allIteminstList.addAll(distinctedIteminsts);
                    }
                }
                Map<String, AeaItemBasic> optionItemBasicMap = null;
                Map<String, AeaItemBasic> stateItemBasicMap = null;
                if (CollectionUtils.isNotEmpty(allStageIds)) {
                    List<String> stageIdList = Lists.newArrayList(allStageIds);
                    stateItemBasicMap = queryAllStateItems(stageIdList);

                    optionItemBasicMap = queryAllItems(stageIdList);
                    aeaParStages = aeaParStageMapper.listAeaParStageByIds(stageIdList);
                }

                // 所有事项定义
                Map<String, AeaItemBasic> itemBasicMap = new HashMap<>();
                // 标准事项
                Map<String, AeaItemBasic> catalogItemBasicMap = new HashMap<>();
                if (allIteminstList.size() > 0) {
                    List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByItemVerIds(allIteminstList.stream().map(AeaHiIteminst::getItemVerId).toArray(String[]::new));
                    aeaItemBasics.stream()
                            .peek(item -> {
                                // 理论上只要是 aea_hi_iteminst 都是实施事项
                                if ("1".equals(item.getIsCatalog())) {
                                    catalogItemBasicMap.put(item.getItemVerId(), item);
                                } else {
                                    AeaItemBasic catalogItem = aeaItemBasicService.getCatalogItemByCarryOutItemId(item.getItemId());
                                    catalogItemBasicMap.put(item.getItemVerId(), catalogItem);
                                }
                            })
                            .forEach(item -> itemBasicMap.put(item.getItemVerId(), item))
                    ;
                }

                diagramHelper.stageApplyinstList(stageApplyinst)
                        .seriesApplyinstList(seriesApplyinst)
                        .aeaHiParStageinstList(aeaHiParStageinsts)
                        .stageinstIteminstMap(stageinstIteminstMap)
                        .aeaParStages(aeaParStages)
                        .seriesIteminstList(distinctedSimpleIteminsts)
                        .itemBasicMap(itemBasicMap)
                        .catalogItemBasicMap(catalogItemBasicMap)
                        .stateItemBasicMap(stateItemBasicMap)
                        .optionItemBasicMap(optionItemBasicMap)
                ;
            }
        }

        DiagramStatusDto statusDto = diagramHelper.build();

        //查处所有的父级
        if(statusDto != null && statusDto.getDiagramStageStatusList() != null && statusDto.getDiagramStageStatusList().size() > 0){
            List<DiagramStatusDto.DiagramStageDto> stageList = statusDto.getDiagramStageStatusList();
            for(DiagramStatusDto.DiagramStageDto stageDto : stageList){

                populateStageTimeInfo(stageDto, projInfoId);

                Set<DiagramStatusDto.DiagramItemDto> itemList = stageDto.getDiagramItemList();
                if(itemList != null && itemList.size() > 0){
                    for(DiagramStatusDto.DiagramItemDto dto: itemList) {
                        Set<String> sets = new HashSet<>();
                        AeaItem item = aeaItemAdminService.getAeaItemById(dto.getItemId());
                        sets.add(dto.getItemId());
                        if(item != null && item.getParentItemId() != null && !item.getParentItemId().equalsIgnoreCase("root")){
                            aeaItemAdminService.getItemParentIdsByItemId(item.getParentItemId(), sets);
                        }
                        dto.setIteminstRunTime(0d);
                        if (StringUtils.isNotBlank(dto.getIteminstId())){
                            List<ActStoTimeruleInst> actStoTimeruleInsts = actStoTimeruleInstMapper.listProcessinstTimeruleInst(new String[]{dto.getIteminstId()}, item.getRootOrgId());
                            if (actStoTimeruleInsts != null && actStoTimeruleInsts.size() > 0) {
                                dto.setIteminstRunTime(actStoTimeruleInsts.get(0).getUseLimitTime());
                            }
                        }

                        dto.setItemIds(String.join(",", sets));
                    }
                }
            }
        }
        return statusDto;
    }

    private void populateStageTimeInfo(DiagramStatusDto.DiagramStageDto stageDto, String projInfoId) {
        // 获取项目第一次申报某阶段时间
        stageDto.setStageStartTime(aeaProjStageService.getProjFirstApplyStageTime(stageDto.getStageId(), projInfoId));
        if (StringUtils.isNotBlank(stageDto.getDybzspjdxh())) {
            AeaProjStage aeaProjStage = aeaProjStageMapper.getAeaParStageByProjInfoIdAndStandardSortNo(projInfoId, stageDto.getDybzspjdxh());
            if (aeaProjStage != null) {
                stageDto.setStageEndTime(aeaProjStage.getPassTime());
                stageDto.setDuringTime(aeaProjStage.getPassWorkingDays());

                String stageState = aeaProjStage.getStageState();
                if (AeaProjStageConstants.STAGE_STATE_NONAPPLY.equals(stageState)) {
                    stageDto.setStatusName(HandleStatus.UN_FINISHED.getName());
                    stageDto.setStatusValue(HandleStatus.UN_FINISHED.getValue());
                } else if (AeaProjStageConstants.STAGE_STATE_APPROVAL.equals(stageState)) {
                    stageDto.setStatusName(HandleStatus.HANDLING.getName());
                    stageDto.setStatusValue(HandleStatus.HANDLING.getValue());
                } else if (AeaProjStageConstants.STAGE_STATE_COMPLETED.equals(stageState)) {
                    stageDto.setStatusName(HandleStatus.FINISHED.getName());
                    stageDto.setStatusValue(HandleStatus.FINISHED.getValue());
                }
            }
        }
    }

    /*
    根据itemId和createTime去重， 因为阶段下的事项可以重复申报多次， 以最新的为准
     */
    private List<AeaHiIteminst> distinctByItemIdAndCreateTime(List<AeaHiIteminst> aeaHiIteminsts) {
        List<AeaHiIteminst> distinctedItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(aeaHiIteminsts)) {
            Map<String, AeaHiIteminst> latestItemMap = new HashMap<>(aeaHiIteminsts.size());
            Set<String> addedItemId = new HashSet<>(aeaHiIteminsts.size());
            aeaHiIteminsts.forEach(iteminst -> {
                if (!addedItemId.contains(iteminst.getItemId())) {
                    addedItemId.add(iteminst.getItemId());
                    latestItemMap.put(iteminst.getItemId(), iteminst);
                } else {
                    AeaHiIteminst newlyItem = latestItemMap.get(iteminst.getItemId());
                    // 比较createTime 将最新的放进去
                    if (newlyItem.getCreateTime().before(iteminst.getCreateTime())) {
                        latestItemMap.put(iteminst.getItemId(), iteminst);
                    }
                }
            });
            distinctedItems.addAll(latestItemMap.values());
        }
        return distinctedItems;
    }

    @Override
    public MatAttachDto getAllAttachmentsByIteminstId(MatAttachQo matAttachQo) {
        return null;
    }

    @Autowired
    private AeaItemAdminService aeaItemAdminService;

    /**
     * 阶段的可选事项
     *
     * @param stageIds 阶段id集合
     */
    private Map<String, AeaItemBasic> queryAllItems(List<String> stageIds) {
        Map<String, AeaItemBasic> optionItemBasicMap = new HashMap<>();
        List<AeaItemBasic> allItems = aeaItemBasicMapper.listOptionAeaItemBasicListByStageIds(stageIds, null, SecurityContext.getCurrentOrgId());
        if (allItems.size() > 0) {
            allItems.forEach(item -> optionItemBasicMap.put(item.getItemVerId(), item));
        }
        return optionItemBasicMap;
    }

    /**
     * 查询阶段所有的情形事项
     *
     * @param stageIds 阶段id集合
     */
    private Map<String, AeaItemBasic> queryAllStateItems(List<String> stageIds) {
        Map<String, AeaItemBasic> stateItemBasicMap = new HashMap<>();
        List<AeaItemBasic> stateItemBasic = aeaItemBasicMapper.listAeaItemBasicListByStageIds(stageIds, SecurityContext.getCurrentOrgId());
        if (stateItemBasic.size() > 0) {
            stateItemBasic.forEach(item -> stateItemBasicMap.put(item.getItemVerId(), item));
        }
        return stateItemBasicMap;
    }

    /**
     * 根据主题id查找最新的已发布或者试运行版本
     *
     * @param themeId 主题id
     */
    private AeaParThemeVer queryLatestAeaParThemeVer(String themeId) {
        AeaParThemeVer aeaParThemeVer = new AeaParThemeVer();
        aeaParThemeVer.setThemeId(themeId);
        List<AeaParThemeVer> aeaParThemeVers = aeaParThemeVerMapper.listAeaParThemeVer(aeaParThemeVer);
        if (aeaParThemeVers.size() > 0) {
            for (AeaParThemeVer ver : aeaParThemeVers) {
                if ("1".equals(ver.getIsPublish()) || "2".equals(ver.getIsPublish())) {
                    return ver;
                }
            }
        }
        return null;
    }
}
