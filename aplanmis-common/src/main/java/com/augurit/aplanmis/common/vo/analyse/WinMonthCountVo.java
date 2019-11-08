package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/19 18:55
 */
@Data
public class WinMonthCountVo {

    private String windowId;
    private String applySource;
    private String applyRecordId;
    private String isParallel;

    private Long monthApplyCount;
    private Long allInSupplementCount;
    private Long allSupplementedCount;
    private Long monthPreAcceptanceCount;
    private Long monthOutScopeCount;
    private Long monthCompletedCount;
    private Long monthOverTimeCount;

    private Long lastMonthApplyCount;
    private Long lastMonthInSupplementCount;
    private Long lastMonthSupplementedCount;
    private Long lastMonthPreAcceptacneCount;
    private Long lastMonthOutScopeCount;
    private Long lastMonthCompletedCount;
    private Long lastMonthOverTimeCount;

    private Long lastYearApplyCount;
    private Long lastYearInSupplementCount;
    private Long lastYearSupplementedCount;
    private Long lastYearPreAcceptacneCount;
    private Long lastYearOutScopeCount;
    private Long lastYearCompletedCount;
    private Long lastYearOverTimeCount;

}
