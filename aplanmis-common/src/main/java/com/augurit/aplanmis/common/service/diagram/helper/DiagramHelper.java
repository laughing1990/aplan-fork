package com.augurit.aplanmis.common.service.diagram.helper;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.diagram.component.BasicItemDiagramItem;
import com.augurit.aplanmis.common.service.diagram.component.DiagrameStage;
import com.augurit.aplanmis.common.service.diagram.component.OptionStageDiagramItem;
import com.augurit.aplanmis.common.service.diagram.component.ParallelStageDiagramItem;
import com.augurit.aplanmis.common.service.diagram.component.RequiredStageDiagramItem;
import com.augurit.aplanmis.common.service.diagram.component.StateStageDiagramItem;
import com.augurit.aplanmis.common.service.diagram.dto.DiagramStatusDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiagramHelper {

    private String themeVerId;

    // 全景图json
    private String diagramJson;

    private List<AeaParStage> aeaParStages;

    // 事项实例对应的事项定义
    private Map<String, AeaItemBasic> itemBasicMap;

    // 实施事项对应的标准事项
    private Map<String, AeaItemBasic> catalogItemBasicMap;

    // 情形事项
    private Map<String, AeaItemBasic> stateItemBasicMap;

    // 可选事项(并行事项)
    private Map<String, AeaItemBasic> optionItemBasicMap;

    // 阶段实例
    private List<AeaHiParStageinst> aeaHiParStageinstList;

    // stageId -> aeaHiParStageinst
    // 阶段实例map
    // 一个阶段可能有多个阶段实例
    private Map<String, List<AeaHiParStageinst>> stageinstMap;

    private Map<String, AeaHiApplyinst> stageApplyinstMap;

    // 并联申报实例
    private List<AeaHiApplyinst> stageApplyinstList;

    // 单项申报实例
    private List<AeaHiApplyinst> seriesApplyinstList;

    // stageinstId -> aeaHiIteminst
    // 阶段实例对应的事项实例
    private Map<String, List<AeaHiIteminst>> stageinstIdIteminstMap;

    // 单项对应的事项实例
    private List<AeaHiIteminst> seriesIteminstList;

    // applyinstId -> aeaHiApplyisnt
    // 所有申报实例
    private Map<String, AeaHiApplyinst> allApplyinstMap;

    // aeaItemVerId -> aeaHiIteminst
    // 所有事项实例
    private Map<String, AeaHiIteminst> allIteminstMap;

    private DiagramHelper() {
        aeaParStages = new ArrayList<>();
    }

    public static DiagramHelper newOne() {
        return new DiagramHelper();
    }

    public DiagramHelper themeVerId(String themeVerId) {
        this.themeVerId = themeVerId;
        return this;
    }

    public DiagramHelper diagramJson(String diagramJson) {
        this.diagramJson = diagramJson;
        return this;
    }

    public DiagramHelper aeaHiParStageinstList(List<AeaHiParStageinst> aeaHiParStageinstList) {
        if (aeaHiParStageinstList != null) {
            this.aeaHiParStageinstList = aeaHiParStageinstList;
            this.stageinstMap = new HashMap<>(aeaHiParStageinstList.size());
            aeaHiParStageinstList.forEach(stageinst -> {
                if (stageinstMap.get(stageinst.getStageId()) != null) {
                    stageinstMap.get(stageinst.getStageId()).add(stageinst);
                } else {
                    List<AeaHiParStageinst> aeaHiParStageinsts = new ArrayList<>();
                    aeaHiParStageinsts.add(stageinst);
                    stageinstMap.put(stageinst.getStageId(), aeaHiParStageinsts);
                }
            });
        } else {
            this.aeaHiParStageinstList = new ArrayList<>();
            this.stageinstMap = new HashMap<>();
        }
        return this;
    }

    public DiagramHelper stageApplyinstList(List<AeaHiApplyinst> stageApplyinstList) {
        if (allApplyinstMap == null) {
            this.allApplyinstMap = new HashMap<>();
        }
        if (stageApplyinstList != null) {
            this.stageApplyinstList = stageApplyinstList;
            stageApplyinstList.forEach(a -> allApplyinstMap.put(a.getApplyinstId(), a));
        } else {
            this.stageApplyinstList = new ArrayList<>();
        }
        return this;
    }

    public DiagramHelper seriesApplyinstList(List<AeaHiApplyinst> seriesApplyinstList) {
        if (allApplyinstMap == null) {
            this.allApplyinstMap = new HashMap<>();
        }
        if (seriesApplyinstList != null) {
            this.seriesApplyinstList = seriesApplyinstList;
            seriesApplyinstList.forEach(a -> allApplyinstMap.put(a.getApplyinstId(), a));
        } else {
            this.seriesApplyinstList = new ArrayList<>();
        }
        return this;
    }

    public DiagramHelper stageinstIteminstMap(Map<String, List<AeaHiIteminst>> stageinstIdIteminstMap) {
        if (allIteminstMap == null) {
            allIteminstMap = new HashMap<>();
        }
        if (stageinstIdIteminstMap != null) {
            this.stageinstIdIteminstMap = stageinstIdIteminstMap;
            for (Map.Entry<String, List<AeaHiIteminst>> entry : stageinstIdIteminstMap.entrySet()) {
                entry.getValue().forEach(aeaHiIteminst -> allIteminstMap.put(aeaHiIteminst.getItemVerId(), aeaHiIteminst));
            }
        } else {
            this.stageinstIdIteminstMap = new HashMap<>();
        }
        return this;
    }

    public DiagramHelper seriesIteminstList(List<AeaHiIteminst> seriesIteminstList) {
        if (allIteminstMap == null) {
            allIteminstMap = new HashMap<>();
        }
        if (seriesIteminstList != null) {
            this.seriesIteminstList = seriesIteminstList;
            for (AeaHiIteminst aeaHiIteminst : seriesIteminstList) {
                allIteminstMap.put(aeaHiIteminst.getItemVerId(), aeaHiIteminst);
            }
        } else {
            this.seriesIteminstList = new ArrayList<>();
        }
        return this;
    }

    public DiagramHelper aeaParStages(List<AeaParStage> aeaParStages) {
        if (aeaParStages != null) {
            this.aeaParStages = aeaParStages;
        } else {
            this.aeaParStages = new ArrayList<>();
        }
        return this;
    }

    public DiagramHelper itemBasicMap(Map<String, AeaItemBasic> itemBasicMap) {
        if (itemBasicMap != null) {
            this.itemBasicMap = itemBasicMap;
        } else {
            this.itemBasicMap = new HashMap<>();
        }
        return this;
    }

    public DiagramHelper catalogItemBasicMap(Map<String, AeaItemBasic> catalogItemBasicMap) {
        if (catalogItemBasicMap != null) {
            this.catalogItemBasicMap = catalogItemBasicMap;
        } else {
            this.catalogItemBasicMap = new HashMap<>();
        }
        return this;
    }

    public DiagramHelper stateItemBasicMap(Map<String, AeaItemBasic> stateItemBasicMap) {
        if (stateItemBasicMap != null) {
            this.stateItemBasicMap = stateItemBasicMap;
        } else {
            this.stateItemBasicMap = new HashMap<>();
        }
        return this;
    }

    public DiagramHelper optionItemBasicMap(Map<String, AeaItemBasic> optionItemBasicMap) {
        if (optionItemBasicMap != null) {
            this.optionItemBasicMap = optionItemBasicMap;
        } else {
            this.optionItemBasicMap = new HashMap<>();
        }
        return this;
    }

    public DiagramStatusDto build() throws Exception {
        validate();

        List<DiagrameStage> diagrameStages = new ArrayList<>(aeaParStages.size());
        aeaParStages.forEach(currentStage -> {
            String currentStageId = currentStage.getStageId();
            List<AeaHiParStageinst> currentParStageinsts = stageinstMap.get(currentStageId);
            buildStageApplyinstMap();

            // 情形事项
            List<StateStageDiagramItem> stateStageDiagramItems = new ArrayList<>();
            // 必选事项
            List<RequiredStageDiagramItem> requiredStageDiagramItems = new ArrayList<>();

            if (currentParStageinsts != null && currentParStageinsts.size() > 0) {
                for (AeaHiParStageinst currentParStageinst : currentParStageinsts) {
                    List<AeaHiIteminst> currentIteminsts = stageinstIdIteminstMap.get(currentParStageinst.getStageinstId());
                    if (currentIteminsts != null) {
                        currentIteminsts.forEach(iteminst -> {
                            AeaItemBasic currentItemBasic = itemBasicMap.get(iteminst.getItemVerId());
                            AeaItemBasic currentCatalogItemBasic = catalogItemBasicMap.get(iteminst.getItemVerId());
                            // 是情形事项
                            if (stateItem(iteminst.getItemVerId(), currentCatalogItemBasic == null ? "" : currentCatalogItemBasic.getItemVerId())) {
                                StateStageDiagramItem stateStageDiagramItem = buildStateItem(currentItemBasic, currentCatalogItemBasic, currentParStageinst, iteminst);
                                stateStageDiagramItems.add(stateStageDiagramItem);
                            }
                            // 必选事项
                            else {
                                RequiredStageDiagramItem requiredStageDiagramItem = buildRequiredItem(currentItemBasic, currentCatalogItemBasic, currentParStageinst, iteminst);
                                requiredStageDiagramItems.add(requiredStageDiagramItem);
                            }
                        });
                    }
                }
            }
            // 并联事项
            ParallelStageDiagramItem parallelStageDiagramItem = new ParallelStageDiagramItem(stateStageDiagramItems, requiredStageDiagramItems);

            // 并行事项
            List<BasicItemDiagramItem> basicItemDiagramItems = new ArrayList<>();
            seriesIteminstList.forEach(iteminst -> {
                AeaItemBasic currentCatalogItemBasic = catalogItemBasicMap.get(iteminst.getItemVerId());
                AeaItemBasic currentItemBasic = itemBasicMap.get(iteminst.getItemVerId());
                if(currentCatalogItemBasic != null){
                    if (optionItem(iteminst.getItemVerId(), currentCatalogItemBasic.getItemVerId())) {
                        BasicItemDiagramItem basicItemDiagramItem = buildOptionItem(currentItemBasic, currentCatalogItemBasic, iteminst);
                        basicItemDiagramItems.add(basicItemDiagramItem);
                    }
                }
            });
            OptionStageDiagramItem optionStageDiagramItem = new OptionStageDiagramItem(basicItemDiagramItems);

            DiagrameStage diagrameStage = new DiagrameStage(currentStage, parallelStageDiagramItem, optionStageDiagramItem);
//            diagrameStage.acquireHandleStatus();

            diagrameStages.add(diagrameStage);
        });

        DiagramStatusDto diagramStatusDto = buildDiagramStatusDto();
        if (diagrameStages.size() > 0) {
            diagramStatusDto.getDiagramStageStatusList().addAll(diagrameStages.stream().map(DiagramStatusDto::build).collect(Collectors.toList()));
        }
        return diagramStatusDto;
    }

    private DiagramStatusDto buildDiagramStatusDto() {
        DiagramStatusDto diagramStatusDto = new DiagramStatusDto();
        diagramStatusDto.setThemeVerId(this.themeVerId);
        diagramStatusDto.setDiagramJson(this.diagramJson);
        return diagramStatusDto;
    }

    private BasicItemDiagramItem buildOptionItem(AeaItemBasic currentItemBasic, AeaItemBasic currentCatalogItemBasic, AeaHiIteminst iteminst) {
        BasicItemDiagramItem basicItemDiagramItem = new BasicItemDiagramItem(currentItemBasic, iteminst);
        basicItemDiagramItem.fillCatalogInfo(currentCatalogItemBasic);
        basicItemDiagramItem.acquireHandleStatus();
        return basicItemDiagramItem;
    }

    private RequiredStageDiagramItem buildRequiredItem(AeaItemBasic currentItemBasic, AeaItemBasic currentCatalogItemBasic, AeaHiParStageinst currentParStageinst, AeaHiIteminst iteminst) {
        RequiredStageDiagramItem requiredStageDiagramItem = new RequiredStageDiagramItem(currentItemBasic, currentParStageinst, iteminst);
        requiredStageDiagramItem.fillCatalogInfo(currentCatalogItemBasic);
        requiredStageDiagramItem.acquireHandleStatus();
        return requiredStageDiagramItem;
    }

    private StateStageDiagramItem buildStateItem(AeaItemBasic currentItemBasic, AeaItemBasic currentCatalogItemBasic, AeaHiParStageinst currentParStageinst, AeaHiIteminst iteminst) {
        StateStageDiagramItem stateStageDiagramItem = new StateStageDiagramItem(currentItemBasic, currentParStageinst, iteminst);
        stateStageDiagramItem.fillCatalogInfo(currentCatalogItemBasic);
        stateStageDiagramItem.acquireHandleStatus();
        return stateStageDiagramItem;
    }

    /**
     * 判断是否并行事项
     * 实施事项和标准事项都要判断
     *
     * @param itemVerId 事项版本id
     */
    private boolean optionItem(String itemVerId, String catalogItemVerId) {
        return optionItemBasicMap.containsKey(itemVerId) || optionItemBasicMap.containsKey(catalogItemVerId);
    }

    /**
     * 判断是否情形事项
     *
     * @param itemVerId 事项版本id
     */
    private boolean stateItem(String itemVerId, String catalogItemVerId) {
        return stateItemBasicMap.containsKey(itemVerId) || stateItemBasicMap.containsKey(catalogItemVerId);
    }

    /**
     * 建立阶段实例与申报实例关联
     */
    private void buildStageApplyinstMap() {
        stageApplyinstMap = new HashMap<>();
        aeaHiParStageinstList.forEach(stageinst -> {
            String applyinstId = stageinst.getApplyinstId();
            AeaHiApplyinst aeaHiApplyinst = allApplyinstMap.get(applyinstId);
            if (aeaHiApplyinst != null) {
                stageApplyinstMap.put(stageinst.getStageinstId(), aeaHiApplyinst);
            }
        });
    }

    private void validate() throws Exception {
        if (StringUtils.isBlank(themeVerId)) {
            throw new Exception("无法找到有效的主题版本");
        }
        if (StringUtils.isBlank(diagramJson)) {
            throw new Exception("项目流程图json为空");
        }
    }
}
