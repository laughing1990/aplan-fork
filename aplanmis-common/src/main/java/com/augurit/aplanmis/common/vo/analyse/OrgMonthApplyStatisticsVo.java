package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

@Data
public class OrgMonthApplyStatisticsVo {
    private String month;
    private Long shouliCount;
    private Long buyushouliCount;
    private Long banjieCount;
    private Long yuqiCount;
}
