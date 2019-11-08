package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/19 16:19
 */
@Data
public class WinWeekCountVo {

    private String windowId;
    private String applySource;
    private String applyRecordId;
    private String isParallel;

    private Long weekApplyCount;
    private Long allInSupplementCount;
    private Long allSupplementedCount;
    private Long weekPreAcceptanceCount;
    private Long weekOutScopeCount;
    private Long weekCompletedCount;
    private Long weekOverTimeCount;

    private Long lastWeekApplyCount;
    private Long lastWeekPreAcceptacneCount;
    private Long lastWeekOutScopeCount;
    private Long lastWeekCompletedCount;
    private Long lastWeekOverTimeCount;

}
