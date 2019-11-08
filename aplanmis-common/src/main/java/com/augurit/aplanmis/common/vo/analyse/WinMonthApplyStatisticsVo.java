package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 10:45
 */
@Data
public class WinMonthApplyStatisticsVo {

    private String month;
    private Long shouliCount;
    private Long buyushouliCount;
    private Long banjieCount;
    private Long yuqiCount;
}
