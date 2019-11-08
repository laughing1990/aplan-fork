package com.augurit.aplanmis.common.mybatisBean;


import java.util.List;

public class RappidStageItemBean {
    private Integer isOptionItem;
    private List<RappidItemBean> aeaParStageItems;

    public Integer getIsOptionItem() {
        return isOptionItem;
    }

    public void setIsOptionItem(Integer isOptionItem) {
        this.isOptionItem = isOptionItem;
    }

    public List<RappidItemBean> getAeaParStageItems() {
        return aeaParStageItems;
    }

    public void setAeaParStageItems(List<RappidItemBean> aeaParStageItems) {
        this.aeaParStageItems = aeaParStageItems;
    }
}
