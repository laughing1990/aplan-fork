package com.augurit.aplanmis.common.apply.item;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaHiGuideDetailMapper;
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

    private AeaHiGuide aeaHiGuide;

    private List<AeaHiGuideDetail> aeaHiGuideDetails;

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

        // 基于项目属地换算实施事项
        projRangeFilter(computedItems);

        // 事项选择信息填充: 包括是否智能引导、申请人选择、牵头部门、审批部门
        fillInfoComplemention();
    }

    private void fillInfoComplemention() {
        this.aeaHiGuideDetails.forEach(detail -> {
            // 标准事项 或者 事项事项 匹配
            GuideComputedItem guideComputedItem = guideComputedItemMap.get(detail.getCatalogItemVerId()) != null ? guideComputedItemMap.get(detail.getCatalogItemVerId())
                    : guideComputedItemMap.get(detail.getItemVerId());
            if (guideComputedItem != null) {
                guideComputedItem.fillInfo(detail);
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
                computedItem.mergeCommonField(origin);
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
        computedItems.forEach(item -> {
            List<String> regionIds = new ArrayList<>();
            regionIds.add(aeaProjInfo.getRegionalism());
            // 根据事项的换算方式过滤实施事项
            if (Status.OFF.equals(item.getItemExchangeWay())) {
                regionIds.addAll(projectAddressRegionIds);
            }
            List<ComputedItem.CarryOutItem> finalCarryOutItems = item.getCarryOutItems().stream().filter(co -> regionIds.contains(co.getRegionId())).collect(Collectors.toList());
            item.setCarryOutItems(finalCarryOutItems);
            if (CollectionUtils.isNotEmpty(finalCarryOutItems)) {
                item.setCurrentCarryOutItem(finalCarryOutItems.get(0));
            }
        });
    }
}
