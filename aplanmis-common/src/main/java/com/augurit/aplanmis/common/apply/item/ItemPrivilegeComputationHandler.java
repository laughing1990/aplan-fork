package com.augurit.aplanmis.common.apply.item;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 事项权限换算handler
 */
public abstract class ItemPrivilegeComputationHandler<ITEM extends ComputedItem> {

    public static Logger log = LoggerFactory.getLogger(ItemPrivilegeComputationHandler.class);

    /*
    当前申报阶段
     */
    AeaParStage currentStage;

    /*
    是否需要过滤情形事项
     */
    private boolean needFilterStateItem;

    /*
    申报项目id
     */
    protected String projInfoId;

    /*
    根组织id
     */
    protected String rootOrgId;

    /*
    在思维导图中配置的原生事项范围
     */
    List<AeaItemBasic> originalItems;

    // 事项事项的行政区划regionSeq, 用于匹配子级区划
    protected Map<String, String> itemVerIdRegionIdMap = new HashMap<>();

    ItemPrivilegeComputationHandler(String projInfoId, String rootOrgId, AeaParStage aeaParStage, @Nullable List<AeaItemBasic> originalItems, boolean needFilterStateItem) {
        this.currentStage = aeaParStage;
        this.needFilterStateItem = needFilterStateItem && Status.ON.equals(aeaParStage.getIsNeedState());
        this.projInfoId = projInfoId;
        this.rootOrgId = rootOrgId;
        this.originalItems = originalItems;
    }

    /**
     * 根据后台的窗口事项权限配置，对阶段配置的标准事项或者实施事项进行换算
     *
     * @return 换算后的事项
     */
    public List<ITEM> compute() {

        if (needFilterStateItem) {
            filterStateItems();
        }

        // 先找到标准事项下的实施事项
        List<ITEM> computedItems = preHandle();

        // 存在极端情况，一个事项也没有
        if (computedItems.size() == 0) {
            return computedItems;
        }

        // 换算开始
        doCompute(computedItems);

        postHandle(computedItems);

        return computedItems;
    }

    protected void validate() {
        Assert.notNull(currentStage, "currentStage must not null.");
        Assert.notNull(projInfoId, "projInfoId must not null.");
        Assert.notNull(rootOrgId, "rootOrgId must not null.");
    }

    abstract void doCompute(List<ITEM> computedItems);

    // 过滤情形事项
    private void filterStateItems() {
        try {
            List<AeaItemBasic> resultItems = new ArrayList<>();
            // 情形事项
            Set<String> stateItemVerIds = SpringContextHolder.getBean(AeaItemBasicService.class).getAeaItemBasicListByStageIdAndStateId(currentStage.getStageId(), null, null, rootOrgId)
                    .stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
            // 过滤情形事项
            originalItems.forEach(item -> {
                if (!stateItemVerIds.contains(item.getItemVerId())) {
                    resultItems.add(item);
                }
            });
            originalItems = resultItems;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("过滤情形事项报错!" + e.getMessage());
        }
    }

    // 获取所有的实施事项
    public abstract List<ITEM> preHandle();

    private void postHandle(List<ITEM> computedItems) {
        if (CollectionUtils.isNotEmpty(computedItems)) {
            computedItems.forEach(item -> {
                // 当前实施不为空 但实施事项列表为空 这种数据不应该存在
                if (item.getCurrentCarryOutItem() != null && CollectionUtils.isEmpty(item.getCarryOutItems())) {
                    item.setCurrentCarryOutItem(null);
                }
                // 根据行政区划id自然序排序
                if (CollectionUtils.isNotEmpty(item.getCarryOutItems())) {
                    item.getCarryOutItems().sort(Comparator.comparing(ComputedItem.CarryOutItem::getRegionId));
                }
            });
        }
    }

    /*
    找到标准事项的实施事项
     */
    void complementCarryoutItems(ComputedItem computedItem, AeaItemBasic origin) {
        computedItem.mergeCommonField(origin);
        // 标准
        if (Status.ON.equals(origin.getIsCatalog())) {
            List<AeaItemBasic> carryOutItems = SpringContextHolder.getBean(AeaItemBasicAdminService.class).findCarryOutItemsByItemId(origin.getItemId(), rootOrgId);
            if (CollectionUtils.isNotEmpty(carryOutItems)) {
                computedItem.getCarryOutItems().addAll(carryOutItems
                        .stream()
                        .peek(item -> itemVerIdRegionIdMap.put(item.getItemVerId(), item.getRegionId()))
                        .map(ComputedItem.CarryOutItem::buildCarryOutItem).collect(Collectors.toList()));
            }
        }
        // 实施
        else if (Status.OFF.equals(origin.getIsCatalog())) {
            ComputedItem.CarryOutItem currentCarryOutItem = ComputedItem.CarryOutItem.buildCarryOutItem(origin);
            computedItem.setCurrentCarryOutItem(currentCarryOutItem);
            computedItem.getCarryOutItems().add(currentCarryOutItem);
        }
        // error
        else {
            log.error("该事项既不是标准事项也不是实施事项: itemVerId: {}, itemName: {}", origin.getItemVerId(), origin.getItemName());
        }
    }
}
