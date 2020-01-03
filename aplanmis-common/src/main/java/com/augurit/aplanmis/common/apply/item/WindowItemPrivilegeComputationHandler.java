package com.augurit.aplanmis.common.apply.item;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 窗口事项权限换算handler
 */
public final class WindowItemPrivilegeComputationHandler extends ItemPrivilegeComputationHandler<WindowComputedItem> {

    public static Logger log = LoggerFactory.getLogger(WindowItemPrivilegeComputationHandler.class);

    /*
    当前申报窗口
     */
    private AeaServiceWindow currentWindow;

    public WindowItemPrivilegeComputationHandler(AeaServiceWindow currentWindow, String projInfoId, String rootOrgId, AeaParStage aeaParStage, @Nullable List<AeaItemBasic> originalItems, boolean needFilterStateItem) {
        super(projInfoId, rootOrgId, aeaParStage, originalItems, needFilterStateItem);
        this.currentWindow = Optional.ofNullable(currentWindow).orElse(new AeaServiceWindow());
        validate();
    }

    protected void validate() {
        super.validate();
        Assert.notNull(currentWindow, "currentWinsow must not null.");
    }

    protected void doCompute(List<WindowComputedItem> computedItems) {
        Assert.state(computedItems.size() > 0, "没有可用于计算的事项");

        // 办理范围过滤
        regionRangeFilter(computedItems);

        //阶段事项过滤
        stageItemFilter(computedItems);

        // 基于项目属地换算实施事项
        projRangeFilter(computedItems);

    }

    // 获取所有的实施事项
    public List<WindowComputedItem> preHandle() {
        List<WindowComputedItem> computedItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.originalItems)) {
            originalItems.forEach(origin -> {
                WindowComputedItem computedItem = new WindowComputedItem();
                computedItem.mergeCommonField(origin);
                complementCarryoutItems(computedItem, origin);
                computedItems.add(computedItem);
            });
        }
        return computedItems;
    }

    // 办理范围， 阶段办理范围覆盖窗口办理范围
    private void regionRangeFilter(List<WindowComputedItem> computedItems) {
        String regionRange = SpringContextHolder.getBean(AeaServiceWindowStageService.class).findCurrentUserAeaServiceWindowStage(currentStage.getStageId());
        computedItems.forEach(item -> {
            item.setRegionRange(regionRange);
            // 属地办理
            if (Status.ON.equals(regionRange) && Status.ON.equals(item.getIsCatalog())) {
                item.getCarryOutItems().forEach(co -> {
                    if (StringUtils.isNotBlank(co.getRegionId()) && co.getRegionId().equals(currentWindow.getRegionId())) {
                        item.setCurrentCarryOutItem(co);
                    }
                });
            }
        });
    }

    // 需要阶段事项过滤 且 不是所有事项通办
    private void stageItemFilter(List<? extends ComputedItem> computedItems) {
        if (Status.ON.equals(currentWindow.getStageItemFilter()) && Status.OFF.equals(currentWindow.getIsAllItem())) {
            // 窗口事项
            Set<String> privItemVerIds = Optional.ofNullable(SpringContextHolder.getBean(AeaServiceWindowItemService.class)
                    .findCurrentUserWindowItem()).orElse(new ArrayList<>()).stream()
                    .map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
            computedItems.forEach(item -> {
                // 过滤掉窗口事项中没有配置的实施事项
                if (Status.ON.equals(item.getIsCatalog())) {
                    if (item.getCurrentCarryOutItem() != null && !privItemVerIds.contains(item.getCurrentCarryOutItem().getItemVerId())) {
                        item.setCurrentCarryOutItem(null);
                    }
                    item.setCarryOutItems(item.getCarryOutItems().stream().filter(co -> privItemVerIds.contains(co.getItemVerId())).collect(Collectors.toList()));
                }
            });
        }
    }

    // 基于项目属地换算实施事项
    private void projRangeFilter(List<WindowComputedItem> computedItems) {
        List<String> projectAddressRegionIds = SpringContextHolder.getBean(AeaProjInfoService.class).getProjAddressRegion(projInfoId);
        computedItems.forEach(item -> {
            if (Status.ON.equals(item.getIsCatalog())
                    && item.getCurrentCarryOutItem() == null
                    && item.getCarryOutItems() != null
                    && item.getCarryOutItems().size() > 0
                    && "0".equals(item.getItemExchangeWay())
                    && projectAddressRegionIds.size() > 0) {
                for (ComputedItem.CarryOutItem co : item.getCarryOutItems()) {
                    if (projectAddressRegionIds.contains(co.getRegionId())) {
                        item.setCurrentCarryOutItem(co);
                        break;
                    }
                }
            }
        });
    }
}
