package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Ma Yanhao
 * @date 2019/9/5 14:05
 */
@Data
public class ThemePeriodStatistics implements Serializable {
    private String themeId;
    private String applyRecordId;
    private Long periodApplyCount;
    private Long periodPreAcceptanceCount;
    private Long periodOutScopeCount;
    private Long periodCompletedCount;
    private Long periodOverTimeCount;
}
