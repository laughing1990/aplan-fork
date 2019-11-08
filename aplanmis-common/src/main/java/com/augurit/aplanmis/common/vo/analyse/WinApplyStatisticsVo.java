package com.augurit.aplanmis.common.vo.analyse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 9:54
 */
@Data
public class WinApplyStatisticsVo {
    @JsonIgnore
    private String windowId;
    private String windowName;
    @JsonIgnore
    private String applySource;
    private Long applyCount;
    private Long shouliCount;
    private Long buyushouliCount;
    private Long banjieCount;
    private Long yuqiCount;
    private Long caiLiaoBuquanCount;

    private Long winApplyCount;
    private Long netApplyCount;

    @JsonIgnore
    private Double yuqiRate;
    @JsonIgnore
    private Double shouliRate;

    private String yuqiRatio;
    private String shouliRatio;
    private Integer sortNo;

    private String day;

    private Double winJieJianRate;//窗口总量占全市总量比

    private String echartsColor;//窗口颜色，效能督查统计图表使用
}
