package com.augurit.aplanmis.common.apply.item;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaHiGuideDetailMapper;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemMapper;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgMapper;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门辅导事项换算handler
 */
public final class GuideItemPrivilegeComputationHandler extends ItemPrivilegeComputationHandler<GuideComputedItem> {

    public static Logger log = LoggerFactory.getLogger(GuideItemPrivilegeComputationHandler.class);

    private Map<String, GuideComputedItem> guideComputedItemMap;

    // 行政区划与序列map
    private Map<String, String> regionIdRegionSeqMap;

    private AeaHiGuide aeaHiGuide;

    private List<AeaHiGuideDetail> aeaHiGuideDetails;

    // 是否审批部门
    private boolean approveDept = false;

    public GuideItemPrivilegeComputationHandler(AeaHiGuide aeaHiGuide, String rootOrgId, AeaParStage aeaParStage, @Nullable List<AeaItemBasic> originalItems, boolean needFilterStateItem) {
        super(aeaHiGuide.getProjInfoId(), rootOrgId, aeaParStage, originalItems, needFilterStateItem);
        guideComputedItemMap = new HashMap<>();
        this.aeaHiGuide = aeaHiGuide;
        validate();
    }

    protected void validate() {
        super.validate();
        Assert.notNull(aeaHiGuide, "没有找到部门辅导记录");
    }

    protected void doCompute(List<GuideComputedItem> computedItems) {
        Assert.state(computedItems.size() > 0, "没有可用于计算的事项");

        // 判断是否牵头部门
        int leaderDeptCnt = SpringContextHolder.getBean(AeaSolicitOrgMapper.class).countLeaderDeptByUserId(SecurityContext.getCurrentUserId());
        // 判断是否审批部门
        int approvedeptCnt = SpringContextHolder.getBean(AeaSolicitItemMapper.class).countApproveDeptByUserId(SecurityContext.getCurrentUserId());
        if (leaderDeptCnt > 0) {
            // 牵头部门可以看所有实施事项
        } else if (approvedeptCnt > 0) {
            approveDept = true;
            if (itemVerIdRegionIdMap != null && itemVerIdRegionIdMap.size() > 0) {
                List<String> regionIds = new ArrayList<>(itemVerIdRegionIdMap.values());
                regionIdRegionSeqMap = SpringContextHolder.getBean(CustomAeaDicRegionMapper.class).listRegionVo(regionIds)
                        .stream().collect(Collectors.toMap(CustomAeaDicRegionMapper.RegionVo::getRegionId, CustomAeaDicRegionMapper.RegionVo::getRegionSeq));
            }
        } else {
            // 基于项目属地换算实施事项
            projRangeFilter(computedItems);
        }

        // 事项选择信息填充: 包括是否智能引导、申请人选择、牵头部门、审批部门
        fillInfoComplemention();
    }

    private void fillInfoComplemention() {
        this.aeaHiGuideDetails.forEach(detail -> {
            // 标准事项 或者 实施事项 匹配
            GuideComputedItem guideComputedItem = guideComputedItemMap.get(detail.getCatalogItemVerId()) != null ? guideComputedItemMap.get(detail.getCatalogItemVerId())
                    : guideComputedItemMap.get(detail.getItemVerId());
            if (guideComputedItem != null) {
                guideComputedItem.fillInfo(detail);

                // 标准事项需要重新匹配实施事项
                if (Status.ON.equals(guideComputedItem.getIsCatalog())) {
                    for (ComputedItem.CarryOutItem co : guideComputedItem.getCarryOutItems()) {
                        if (co.getItemVerId().equals(detail.getItemVerId())) {
                            guideComputedItem.setCurrentCarryOutItem(co);
                            break;
                        }
                    }
                }
                if (approveDept) {
                    // 审批部门可以看当前行政区划 以及 子级行政区划 实施事项
                    projRangeAndChildRegionFilter(guideComputedItem);
                }
            }
        });
    }

    // 获取所有的实施事项
    public List<GuideComputedItem> preHandle() {
        // 部门辅导详情
        aeaHiGuideDetails = SpringContextHolder.getBean(AeaHiGuideDetailMapper.class).listAeaHiGuideDetailByGuideIdOrderByCreateTimeAsc(aeaHiGuide.getGuideId());

        List<GuideComputedItem> computedItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.originalItems)) {
            originalItems.forEach(origin -> {
                GuideComputedItem computedItem = new GuideComputedItem();
                complementCarryoutItems(computedItem, origin);
                computedItems.add(computedItem);

                guideComputedItemMap.put(computedItem.getItemVerId(), computedItem);
            });
        }
        return computedItems;
    }

    // 基于项目属地换算实施事项
    private void projRangeFilter(List<GuideComputedItem> computedItems) {
        List<String> projectAddressRegionIds = SpringContextHolder.getBean(AeaProjInfoService.class).getProjAddressRegion(projInfoId);
        AeaProjInfo aeaProjInfo = SpringContextHolder.getBean(AeaProjInfoService.class).getAeaProjInfoByProjInfoId(projInfoId);
        String regionalism = aeaProjInfo.getRegionalism();
        computedItems.forEach(item -> {
            if (Status.ON.equals(item.isCatalog)) {
                List<String> regionIds = new ArrayList<>();
                regionIds.add(regionalism);
                // 根据事项的换算方式过滤实施事项
                if (Status.OFF.equals(item.getItemExchangeWay())) {
                    regionIds.addAll(projectAddressRegionIds);
                }
                List<ComputedItem.CarryOutItem> finalCarryOutItems = item.getCarryOutItems().stream().filter(co -> regionIds.contains(co.getRegionId())).collect(Collectors.toList());
                item.setCarryOutItems(finalCarryOutItems);
                if (CollectionUtils.isNotEmpty(finalCarryOutItems)) {
                    item.setCurrentCarryOutItem(finalCarryOutItems.get(0));
                }
            }
        });
    }

    // 审批部门可以看当前行政区划 以及 子级行政区划 实施事项
    private void projRangeAndChildRegionFilter(GuideComputedItem computedItem) {
        if (Status.ON.equals(computedItem.getIsCatalog()) && computedItem.getCurrentCarryOutItem() != null) {
            String currentRegionId = itemVerIdRegionIdMap.get(computedItem.getCurrentCarryOutItem().getItemVerId());
            String currentCarryOutItemRegionSeq = regionIdRegionSeqMap.get(currentRegionId);
            List<ComputedItem.CarryOutItem> finalCarryOutItems = computedItem.getCarryOutItems()
                    .stream()
                    .filter(co -> {
                        String regionId = itemVerIdRegionIdMap.get(co.getItemVerId());
                        if (StringUtils.isNotBlank(regionId)) {
                            String carryOutItemRegionSeq = regionIdRegionSeqMap.get(regionId);
                            return carryOutItemRegionSeq.contains(currentCarryOutItemRegionSeq);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            computedItem.setCarryOutItems(finalCarryOutItems);
        }
    }
}
