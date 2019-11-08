package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/19 19:34
 */
@Data
public class WinYearCountVo {

    private String windowId;
    private String applySource;
    private String applyRecordId;
    private String isParallel;

    private Long yearApplyCount;
    private Long allInSupplementCount;
    private Long allSupplementedCount;
    private Long yearPreAcceptanceCount;
    private Long yearOutScopeCount;
    private Long yearCompletedCount;
    private Long yearOverTimeCount;

    private Long lastYearApplyCount;
    private Long lastYearPreAcceptacneCount;
    private Long lastYearOutScopeCount;
    private Long lastYearCompletedCount;
    private Long lastYearOverTimeCount;
    private Long lastYearInSupplementCount;
    private Long lastYearSupplementedCount;

}
