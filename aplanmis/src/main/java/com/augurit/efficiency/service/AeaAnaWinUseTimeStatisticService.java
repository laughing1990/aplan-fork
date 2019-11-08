package com.augurit.efficiency.service;

import java.util.Date;

/**
 * @author ZhangXinhui
 * @date 2019/9/24 024 14:02
 * @desc
 **/
public interface AeaAnaWinUseTimeStatisticService {
    /**
     * 统计窗口申报最长延期及最短用时
     * @param rootOrgId
     * @param creator
     * @param startTime 以 aea_hi_par_stageinst.start_time 或 aea_hi_seriesinst.start_time为判断依据，为空时全量统计
     * @param endTime 以 aea_hi_par_stageinst.end_time 或 aea_hi_seriesinst.end_time为判断依据，为空时全量统计
     * @throws Exception
     */
    void doWinUseTimeStatistics(String rootOrgId, String creator, Date startTime, Date endTime) throws Exception;
}
