package com.augurit.aplanmis.common.handler;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
 * 事项权限换算handler
 */
public final class ItemPrivilegeComputationHandler {

    public static Logger log = LoggerFactory.getLogger(ItemPrivilegeComputationHandler.class);

    /*
    当前申报窗口
     */
    private AeaServiceWindow currentWindow;

    /*
    当前申报阶段
     */
    private AeaParStage currentStage;

    /*
    是否需要过滤情形事项
     */
    private boolean needFilterStateItem;

    /*
    申报项目id
     */
    private String projInfoId;

    /*
    根组织id
     */
    private String rootOrgId;

    /*
    在思维导图中配置的原生事项范围
     */
    private List<AeaItemBasic> originalItems;

    /*
    权限换算后的事项
     */
    private List<ComputedItem> computedItems;


    public ItemPrivilegeComputationHandler(AeaServiceWindow currentWindow, String projInfoId, String rootOrgId, AeaParStage aeaParStage, @Nullable List<AeaItemBasic> originalItems, boolean needFilterStateItem) {
        this.currentWindow = Optional.ofNullable(currentWindow).orElse(new AeaServiceWindow());
        this.currentStage = aeaParStage;
        this.needFilterStateItem = needFilterStateItem && Status.ON.equals(aeaParStage.getIsNeedState());
        this.projInfoId = projInfoId;
        this.rootOrgId = rootOrgId;
        this.originalItems = originalItems;
        validate();
    }

    private void validate() {
        Assert.notNull(currentWindow, "currentWinsow must not null.");
        Assert.notNull(currentStage, "currentStage must not null.");
        Assert.notNull(projInfoId, "projInfoId must not null.");
        Assert.notNull(rootOrgId, "rootOrgId must not null.");
    }

    /**
     * 根据后台的窗口事项权限配置，对阶段配置的标准事项或者实施事项进行换算
     *
     * @return 换算后的事项
     */
    public List<ComputedItem> compute() {

        if (needFilterStateItem) {
            filterStateItems();
        }

        // 先找到标准事项下的实施事项
        preHandle();

        // 存在极端情况，一个事项也没有
        if (computedItems.size() == 0) {
            return computedItems;
        }

        // 换算开始
        doCompute();

        postHandle();

        return this.computedItems;
    }

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

    private void preHandle() {
        computedItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.originalItems)) {
            originalItems.forEach(origin -> {
                ComputedItem computedItem = ComputedItem.mergeCommonField(origin);
                // 标准
                if (Status.ON.equals(origin.getIsCatalog())) {
                    List<AeaItemBasic> carryOutItems = SpringContextHolder.getBean(AeaItemBasicAdminService.class).findCarryOutItemsByItemId(origin.getItemId(), rootOrgId);
                    if (CollectionUtils.isNotEmpty(carryOutItems)) {
                        computedItem.getCarryOutItems().addAll(carryOutItems.stream().map(CarryOutItem::buildCarryOutItem).collect(Collectors.toList()));
                    }
                }
                // 实施
                else if (Status.OFF.equals(origin.getIsCatalog())) {
                    CarryOutItem currentCarryOutItem = CarryOutItem.buildCarryOutItem(origin);
                    computedItem.setCurrentCarryOutItem(currentCarryOutItem);
                    computedItem.getCarryOutItems().add(currentCarryOutItem);
                }
                // error
                else {
                    log.error("该事项既不是标准事项也不是实施事项: itemVerId: {}, itemName: {}", origin.getItemVerId(), origin.getItemName());
                }
                computedItems.add(computedItem);
            });
        }
    }
    private void doCompute() {
        Assert.state(computedItems.size() > 0, "没有可用于计算的事项");

        // 办理范围过滤
        regionRangeFilter();

        //阶段事项过滤
        stageItemFilter();

        // 基于项目属地换算实施事项
        projRangeFilter();

    }

    private void postHandle() {
        if (CollectionUtils.isNotEmpty(computedItems)) {
            computedItems.forEach(item -> {
                // 当前实施不为空 但实施事项列表为空 这种数据不应该存在
                if (item.currentCarryOutItem != null && CollectionUtils.isEmpty(item.carryOutItems)) {
                    item.setCurrentCarryOutItem(null);
                }
            });
        }
    }

    // 办理范围， 阶段办理范围覆盖窗口办理范围
    private void regionRangeFilter() {
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
    private void stageItemFilter() {
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
                    item.setCarryOutItems(item.getCarryOutItems().stream().filter(co -> privItemVerIds.contains(item.getItemVerId())).collect(Collectors.toList()));
                }
            });
        }
    }

    // 基于项目属地换算实施事项
    private void projRangeFilter() {
        List<String> projectAddressRegionIds = SpringContextHolder.getBean(AeaProjInfoService.class).getProjAddressRegion(projInfoId);
        computedItems.forEach(item -> {
            if (Status.ON.equals(item.getIsCatalog())
                    && item.getCurrentCarryOutItem() == null
                    && item.getCarryOutItems() != null
                    && item.getCarryOutItems().size() > 0
                    && "0".equals(item.getItemExchangeWay())
                    && projectAddressRegionIds.size() > 0) {
                for (CarryOutItem co : item.getCarryOutItems()) {
                    if (projectAddressRegionIds.contains(item.getRegionId())) {
                        item.setCurrentCarryOutItem(co);
                        break;
                    }
                }
            }
        });
    }


    /**
     * 换算后的事项
     */
    @Getter
    @Setter
    public static class ComputedItem {

        @ApiModelProperty(value = "阶段情形id")
        private String parStateId;

        @ApiModelProperty(name = "itemVerId", value = "事项版本id", dataType = "string")
        private String itemVerId;

        @ApiModelProperty(name = "itemBasicId", value = "事项基本信息id", dataType = "string")
        private String itemBasicId;

        @ApiModelProperty(value = "事项id", dataType = "string")
        private String itemId;

        @ApiModelProperty(name = "itemName", value = "事项名称", dataType = "string")
        private String itemName;

        @ApiModelProperty(name = "orgName", value = "实施主体", dataType = "string")
        private String orgName;

        @ApiModelProperty(name = "orgId", value = "实施主体id", dataType = "string")
        private String orgId;

        @ApiModelProperty(value = "行政区划ID", dataType = "string")
        private String regionId;

        @ApiModelProperty(value = "行政区划名称", dataType = "string")
        private String regionName;

        @ApiModelProperty(name = "sortNo", value = "排序字段", dataType = "string")
        private String sortNo;

        @ApiModelProperty(name = "isDone", value = "是否已办事项", dataType = "string")
        private String isDone;

        @ApiModelProperty(name = "isRecommend", value = "是否推荐", dataType = "string")
        private String isRecommend;

        @ApiModelProperty(name = "allowManual", value = "是否允许人工选择下级承办组织，0表示禁止，1表示允许", allowableValues = "0, 1")
        private String allowManual;

        @ApiModelProperty(value = "是否标准事项", dataType = "string", notes = "1: 标准事项; 0: 实施事项")
        private String isCatalog;

        @ApiModelProperty(value = "当前窗口归属区划", dataType = "string")
        private String currentRegionId;

        @ApiModelProperty(value = "当前实施事项")
        private CarryOutItem currentCarryOutItem;

        @ApiModelProperty(value = "办理范围", dataType = "string", notes = "0表示全市通办，1表示属地办理")
        private String regionRange;

        @ApiModelProperty(value = "实施事项换算方式", dataType = "string", notes = " 0:按照审批行政区划和属地行政区划换算, 1: 仅按照审批行政区划换算")
        private String itemExchangeWay;

        @ApiModelProperty(value = "实施事项", notes = "列表, 当isCatalog=1时，该列表不为空，否则给出提示，说明标准事项下无法找到对应的实施事项")
        private List<CarryOutItem> carryOutItems;

        ComputedItem() {
            carryOutItems = new ArrayList<>();
        }

        static ComputedItem mergeCommonField(AeaItemBasic origin) {
            ComputedItem computedItem = new ComputedItem();
            computedItem.setItemVerId(origin.getItemVerId());
            computedItem.setItemBasicId(origin.getItemBasicId());
            computedItem.setItemId(origin.getItemId());
            computedItem.setItemName(origin.getItemName());
            computedItem.setOrgName(origin.getOrgName());
            computedItem.setOrgId(origin.getOrgId());
            computedItem.setRegionId(origin.getRegionId());
            computedItem.setRegionName(origin.getRegionName());
            computedItem.setSortNo(origin.getSortNo());
            computedItem.setIsDone(origin.getIsDone());
            computedItem.setIsRecommend(origin.getIsRecommended());
            computedItem.setIsCatalog(origin.getIsCatalog());
            computedItem.setItemExchangeWay(origin.getItemExchangeWay());
            return computedItem;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ApiModel("实施事项")
    public static class CarryOutItem {
        private String itemVerId;

        @ApiModelProperty(value = "实施主体", dataType = "string")
        private String orgName;

        @ApiModelProperty(value = "实施主体id", dataType = "string")
        private String orgId;

        @ApiModelProperty(value = "行政区划ID", dataType = "string")
        private String regionId;

        @ApiModelProperty(value = "行政区划名称", dataType = "string")
        private String regionName;

        @ApiModelProperty(value = "前置事项检查是否通过", dataType = "string", notes = "false: 不通过，此时并行事项不可选")
        private boolean preItemCheckPassed = true;

        static CarryOutItem buildCarryOutItem(AeaItemBasic origin) {
            CarryOutItem carryOutItem = new CarryOutItem();
            carryOutItem.setItemVerId(origin.getItemVerId());
            carryOutItem.setOrgName(origin.getOrgName());
            carryOutItem.setOrgId(origin.getOrgId());
            carryOutItem.setRegionId(origin.getRegionId());
            carryOutItem.setRegionName(origin.getRegionName());
            return carryOutItem;
        }
    }

}
