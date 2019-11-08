package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/11 11:04
 */
@Data
public class ThemeMonthCountVo {

    private String themeId;
    private String themeName;
    private String applyRecordId;
    private String applyRecordName;

    private String dybzspjdxh;
    private String isNode;
    private String isParallel;
    private String applyinstSource;

    private Long monthApplyCount;
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

    private Long allApplyCount;
    private Long allInSupplementCount;
    private Long allSupplementedCount;
    private Long allPreAcceptanceCount;
    private Long allOutScopeCount;
    private Long allCompletedCount;
    private Long allOverTimeCount;

    private Double allPreAcceptanceRate;
    private Double allOutScopeRate;
    private Double allOverTimeRate;
    private Double allCompletedRate;

}
