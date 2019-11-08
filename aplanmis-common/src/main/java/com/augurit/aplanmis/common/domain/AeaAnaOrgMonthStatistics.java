package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 部门月申报统计表（在日办件统计表的基础上统计。历史月数据不变。
 * 本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-模型
*/
@Data
public class AeaAnaOrgMonthStatistics implements Serializable{
    private static final Long serialVersionUID = 1L;

    /** 主键 **/
    private String orgMonthStatisticsId;

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

    /** 月接件数 **/
    private Integer monthApplyCount;

    /** 总接件数 **/
    private Integer allApplyCount;

    /** 月接件数同比 **/
    private Double applyOnYoyBasis;

    /** 月接件数环比 **/
    private Double applyLrr;

    /** 待补正数 **/
    private Integer allInSupplementCount;

    /** 待补正数同比 **/
    private Double inSupplementOnYoyBasis;

    /** 待补正数环比 **/
    private Double inSupplementLrr;

    /** 补正待确认数 **/
    private Integer allSupplementedCount;

    /** 补正待确认数同比 **/
    private Double supplementedOnYoyBasis;

    /** 补正待确认数环比 **/
    private Double supplementedLrr;

    /** 月受理数 **/
    private Integer monthAcceptanceCount;

    /** 总受理数 **/
    private Integer allAcceptanceCount;

    /** 月受理数同比 **/
    private Double acceptanceOnYoyBasis;

    /** 月受理数环比 **/
    private Double acceptanceLrr;

    /** 月不予受理数 **/
    private Integer monthOutScopeCount;

    /** 总不予受理数 **/
    private Integer allOutScopeCount;

    /** 月不予受理数同比 **/
    private Double outScopeOnYoyBasis;

    /** 月不予受理数环比 **/
    private Double outScopeLrr;

    /** 月办结数 **/
    private Integer monthCompletedCount;

    /** 总办结数 **/
    private Integer allCompletedCount;

    /** 月办结数同比 **/
    private Double completedOnYoyBasis;

    /** 月办结数环比 **/
    private Double completedLrr;

    /** 月特殊程序数 **/
    private Integer monthSpecificRoutineCount;

    /** 总特殊程序数 **/
    private Integer allSpecificRoutineCount;

    /** 特殊程序数同比 **/
    private Double specificRoutineOnYoyBasis;

    /** 特殊程序数环比 **/
    private Double specificRoutineLrr;

    /** 预警数 **/
    private Integer allWariningCount;

    /** 月逾期数 **/
    private Integer monthOverTimeCount;

    /** 总逾期数 **/
    private Integer allOverTimeCount;

    /** 月逾期数同比 **/
    private Double overTimeOnYoyBasis;

    /** 月逾期数环比 **/
    private Double overTimeLrr;

    /** 总受理率（受理数/接件数） **/
    private Double allAcceptanceRate;

    /** 总不予受理率（不予受理数/接件数） **/
    private Double allOutScopeRate;

    /** 总逾期率（逾期数/预受理数） **/
    private Double allOverTimeRate;

    /** 总办结率（办结数/预受理数） **/
    private Double allCompletedRate;

    /** 统计数据开始日期（yyyy-MM-dd 00:00:00） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsStartDate;

    /** 统计数据结束日期（yyyy-MM-dd 23:59:59） **/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsEndDate;

    /** 统计数据月份(yyyy-MM) **/
    private String statisticsMonth;

    /** 修改人 **/
    private String modifier;

    /** 修改时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date modifyTime;

    /** 所属顶级组织ID **/
    private String rootOrgId;

}
