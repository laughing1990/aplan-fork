package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/11 11:04
 */
@Data
public class ThemeWeekCountVo {

    private String themeId;
    private String themeName;
    private String applyRecordId;
    private String applyRecordName;

    private String dybzspjdxh;
    private String isNode;
    private String isParallel;
    private String applyinstSource;

    private Long weekApplyCount;
    private Long weekPreAcceptanceCount;
    private Long weekOutScopeCount;
    private Long weekCompletedCount;
    private Long weekOverTimeCount;

    private Long lastWeekApplyCount;
    private Long lastWeekPreAcceptacneCount;
    private Long lastWeekOutScopeCount;
    private Long lastWeekCompletedCount;
    private Long lastWeekOverTimeCount;

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
