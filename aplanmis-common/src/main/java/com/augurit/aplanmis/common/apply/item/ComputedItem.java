package com.augurit.aplanmis.common.apply.item;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 换算后的事项
 */
@Getter
@Setter
public class ComputedItem {

    @ApiModelProperty(name = "itemVerId", value = "事项版本id", dataType = "string")
    protected String itemVerId;

    @ApiModelProperty(name = "itemBasicId", value = "事项基本信息id", dataType = "string")
    protected String itemBasicId;

    @ApiModelProperty(value = "事项id", dataType = "string")
    protected String itemId;

    @ApiModelProperty(name = "itemName", value = "事项名称", dataType = "string")
    protected String itemName;

    @ApiModelProperty(name = "orgName", value = "实施主体", dataType = "string")
    protected String orgName;

    @ApiModelProperty(name = "orgId", value = "实施主体id", dataType = "string")
    protected String orgId;

    @ApiModelProperty(value = "行政区划ID", dataType = "string")
    protected String regionId;

    @ApiModelProperty(value = "行政区划名称", dataType = "string")
    protected String regionName;

    @ApiModelProperty(name = "sortNo", value = "排序字段", dataType = "string")
    protected String sortNo;

    @ApiModelProperty(name = "isDone", value = "是否已办事项", dataType = "string")
    protected String isDone;

    @ApiModelProperty(name = "isRecommend", value = "是否推荐", dataType = "string")
    protected String isRecommend;

    @ApiModelProperty(name = "allowManual", value = "是否允许人工选择下级承办组织，0表示禁止，1表示允许", allowableValues = "0, 1")
    protected String allowManual;

    @ApiModelProperty(value = "是否标准事项", dataType = "string", notes = "1: 标准事项; 0: 实施事项")
    protected String isCatalog;

    @ApiModelProperty(value = "当前归属区划", dataType = "string")
    protected String currentRegionId;

    @ApiModelProperty(value = "当前实施事项")
    protected CarryOutItem currentCarryOutItem;

    @ApiModelProperty(value = "是否必选事项", notes = "1: 是, 0: 否")
    protected String isOptionItem;

    @ApiModelProperty(value = "是否必办事项", notes = "1: 是, 0: 否")
    protected String isDoneItem;

    @ApiModelProperty(value = "实施事项", notes = "列表, 当isCatalog=1时，该列表不为空，否则给出提示，说明标准事项下无法找到对应的实施事项")
    protected List<CarryOutItem> carryOutItems;

    public ComputedItem() {
        carryOutItems = new ArrayList<>();
    }

    protected void mergeCommonField(AeaItemBasic origin) {
        this.setItemVerId(origin.getItemVerId());
        this.setItemBasicId(origin.getItemBasicId());
        this.setItemId(origin.getItemId());
        this.setItemName(origin.getItemName());
        this.setOrgName(origin.getOrgName());
        this.setOrgId(origin.getOrgId());
        this.setRegionId(origin.getRegionId());
        this.setRegionName(origin.getRegionName());
        this.setSortNo(origin.getSortNo());
        this.setIsDone(origin.getIsDone());
        this.setIsRecommend(origin.getIsRecommended());
        this.setIsCatalog(origin.getIsCatalog());
        this.setIsOptionItem(origin.getIsOptionItem());
        this.setIsDoneItem(origin.getIsDoneItem());
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ApiModel("实施事项")
    public static class CarryOutItem {
        private String itemVerId;

        private String itemId;

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
            carryOutItem.setItemId(origin.getItemId());
            carryOutItem.setOrgName(origin.getOrgName());
            carryOutItem.setOrgId(origin.getOrgId());
            carryOutItem.setRegionId(origin.getRegionId());
            carryOutItem.setRegionName(origin.getRegionName());
            return carryOutItem;
        }
    }
}

