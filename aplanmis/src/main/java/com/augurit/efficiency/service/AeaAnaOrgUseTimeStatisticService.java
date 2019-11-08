package com.augurit.efficiency.service;

import java.util.Date;

/**
 * @author ZhangXinhui
 * @date 2019/9/18 018 16:16
 * @desc
 **/
public interface AeaAnaOrgUseTimeStatisticService {


    /**
     * 统计部门机构办件最长及最短用时（最短用时只统计已完成）
     * @param rootOrgId
     * @param creator
     * @param startTime 以aea_hi_iteminst表的start_time为判断依据,为空时全量统计
     * @param endTime 以aea_hi_iteminst表的end_time为判断依据,为空时全量统计
     * @throws Exception
     */
    void doUseTimeStatistics(String rootOrgId, String creator, Date startTime, Date endTime) throws Exception;
}
