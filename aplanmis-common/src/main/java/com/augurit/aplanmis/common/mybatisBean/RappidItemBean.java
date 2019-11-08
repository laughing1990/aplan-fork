package com.augurit.aplanmis.common.mybatisBean;

public class RappidItemBean {
    private String itemId;
    private String itemName;
    private String itemCode;
    private String itemNum;
    private String isCatalog;
    private String stageItemId;
    private String stageId;
    private String itemVerId;
    private String isOptionItem;
    private Double dueNum;

    public Double getDueNum() {
        return dueNum;
    }

    public void setDueNum(Double dueNum) {
        this.dueNum = dueNum;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }


    public String getIsCatalog() {
        return isCatalog;
    }

    public void setIsCatalog(String isCatalog) {
        this.isCatalog = isCatalog;
    }

    public String getStageItemId() {
        return stageItemId;
    }

    public void setStageItemId(String stageItemId) {
        this.stageItemId = stageItemId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getItemVerId() {
        return itemVerId;
    }

    public void setItemVerId(String itemVerId) {
        this.itemVerId = itemVerId;
    }

    public String getIsOptionItem() {
        return isOptionItem;
    }

    public void setIsOptionItem(String isOptionItem) {
        this.isOptionItem = isOptionItem;
    }
}
