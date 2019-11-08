package com.augurit.aplanmis.common.domain;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
public class TotalItemForm {

    /**
     * 新增办件
     */
    private Integer newItem;
    /**
     * 逾期办件
     */
    private Integer overdueItem;
    /**
     * 在办件
     */
    private Integer processingItem;
    /**
     * 已办件
     */
    private Integer finishItem;
    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 主题编码
     */
    private String themeCode;

    public Integer getNewItem() {
        return newItem;
    }

    public void setNewItem(Integer newItem) {
        this.newItem = newItem;
    }

    public Integer getOverdueItem() {
        return overdueItem;
    }

    public void setOverdueItem(Integer overdueItem) {
        this.overdueItem = overdueItem;
    }

    public Integer getProcessingItem() {
        return processingItem;
    }

    public void setProcessingItem(Integer processingItem) {
        this.processingItem = processingItem;
    }

    public Integer getFinishItem() {
        return finishItem;
    }

    public void setFinishItem(Integer finishItem) {
        this.finishItem = finishItem;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeCode() {
        return themeCode;
    }

    public void setThemeCode(String themeCode) {
        this.themeCode = themeCode;
    }
}
