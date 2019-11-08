package com.augurit.aplanmis.front.portal.vo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
public class EchartsBarVo {
    private List<String> legendData ;
    private List<String> xAxisData;
    private List<List<Integer>> seriesData;

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<String> getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(List<String> xAxisData) {
        this.xAxisData = xAxisData;
    }

    public List<List<Integer>> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<List<Integer>> seriesData) {
        this.seriesData = seriesData;
    }
}
