package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 部门年办件统计表（在月办件统计表的基础上统计。历史年数据不变。
 * 本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-模型
*/
@Data
public class AeaAnaOrgYearStatistics implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键 **/
    private String orgYearStatisticsId;

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

    /** 年接件数 **/
    private Integer yearApplyCount;

    /** 总接件数 **/
    private Integer allApplyCount;

    /** 年接件数环比 **/
    private Double applyLrr;

    /** 待补正数 **/
    private Integer allInSupplementCount;

    /** 补正待确认数 **/
    private Integer allSupplementedCount;

    /** 年受理数 **/
    private Integer yearAcceptanceCount;

    /** 总受理数 **/
    private Integer allAcceptanceCount;

    /** 年受理数环比 **/
    private Double acceptanceLrr;

    /** 年不予受理数 **/
    private Integer yearOutScopeCount;

    /** 总不予受理数 **/
    private Integer allOutScopeCount;

    /** 年不予受理数环比 **/
    private Double outScopeLrr;

    /** 年办结数 **/
    private Integer yearCompletedCount;

    /** 总办结数 **/
    private Integer allCompletedCount;

    /** 年办结数环比 **/
    private Double completedLrr;

    /** 年特殊程序数 **/
    private Integer yearSpecificRoutineCount;

    /** 总特殊程序数 **/
    private Integer allSpecificRoutineCount;

    /** 年特殊程序数环比 **/
    private Double specificRoutineLrr;

    /** 预警数 **/
    private Integer allWariningCount;

    /** 年逾期数 **/
    private Integer yearOverTimeCount;

    /** 总逾期数 **/
    private Integer allOverTimeCount;

    /** 年逾期数环比 **/
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

    /** 统计数据年份（yyyy） **/
    private Integer statisticsYear;

    /** 所属顶级组织ID **/
    private String rootOrgId;

}
