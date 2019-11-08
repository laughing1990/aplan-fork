package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 部门周办件统计表（在日办件统计表的基础上统计。历史周数据不变。
* 本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）
*/
@Data
public class AeaAnaOrgWeekStatistics implements Serializable{
    private static final Long serialVersionUID = 1L;

    /** 主键 **/
    private String orgWeekStatisticsId;
    /** 数据生成记录表ID **/
    private String statisticsRecordId;
    /** 部门ID **/
    private String orgId;
    /** 部门名称 **/
    private String orgName;
    /** 行政区划ID（市级和区县级以下的需要换算成市级和区县级） **/
    private String regionId;
    /** 行政区划名称 **/
    private String regionName;
    /** 事项ID **/
    private String itemId;
    /** 事项名称 **/
    private String itemName;
    /** 申报来源 **/
    private String applyinstSource;
    /** 周接件数 **/
    private Integer weekApplyCount;
    /** 总接件数 **/
    private Integer allApplyCount;
    /** 周接件数环比 **/
    private Double applyLrr;
    /** 待补正数 **/
    private Integer allInSupplementCount;
    /** 补正待确认数 **/
    private Integer allSupplementedCount;
    /** 周受理数 **/
    private Integer weekAcceptanceCount;
    /** 总受理数 **/
    private Integer allAcceptanceCount;
    /** 周受理数环比 **/
    private Double acceptanceLrr;
    /** 周不予受理数 **/
    private Integer weekOutScopeCount;
    /** 总不予受理数 **/
    private Integer allOutScopeCount;
    /** 周不予受理数环比 **/
    private Double outScopeLrr;
    /** 周办结数 **/
    private Integer weekCompletedCount;
    /** 总办结数 **/
    private Integer allCompletedCount;
    /** 周办结数环比 **/
    private Double completedLrr;
    /** 周特殊程序数 **/
    private Integer weekSpecificRoutineCount;
    /** 总特殊程序数 **/
    private Integer allSpecificRoutineCount;
    /** 特殊程序数环比 **/
    private Double specificRoutineLrr;
    /** 总预警数 **/
    private Integer allWariningCount;
    /** 周逾期数 **/
    private Integer weekOverTimeCount;
    /** 总逾期数 **/
    private Integer allOverTimeCount;
    /** 周逾期数环比 **/
    private Double overTimeLrr;
    /** 总受理率（受理数/接件数） **/
    private Double allAcceptanceRate;
    /** 总不予受理率（不予受理数/接件数） **/
    private Double allOutScopeRate;
    /** 总逾期率（逾期数/受理数） **/
    private Double allOverTimeRate;
    /** 总办结率（办结数/受理数） **/
    private Double allCompletedRate;
    /** 统计数据开始日期（yyyy-MM-dd 00:00:00） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsStartDate;
    /** 统计数据结束日期（yyyy-MM-dd 23:59:59） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsEndDate;
    /** 统计数据年份 **/
    private Integer statisticsYear;
    /** 第几周。表示统计数据开始日期和统计数据结束日期是对应年份中第几周。 **/
    private Integer weekNum;
    /** 所属顶级组织ID **/
    private String rootOrgId;

}
