package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 部门日办件统计表-模型
*/
@Data
public class AeaAnaOrgDayStatistics implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键 **/
    private String orgDayStatisticsId;
    /** 数据生成记录表ID **/
    private String statisticsRecordId;
    /** 部门ID **/
    private String orgId;
    /** 部门名称 **/
    private String orgName;
    /** 行政区划ID（区县级以下的需要换算成区县级） **/
    private String regionId;
    /** 行政区划名称 **/
    private String regionName;
    /** 事项ID **/
    private String itemId;
    /** 事项名称 **/
    private String itemName;
    /** 申报来源 **/
    private String applyinstSource;
    /** 日接件数（需查询历史表统计） **/
    private Integer dayApplyCount;
    /** 总接件数 **/
    private Integer allApplyCount;
    /** 日接件数环比 **/
    private Double applyLrr;
    /** 待补正数（不需查询历史表统计） **/
    private Integer allInSupplementCount;
    /** 补正待确认数（不需查询历史表统计） **/
    private Integer allSupplementedCount;
    /** 日受理数（需查询历史表统计） **/
    private Integer dayAcceptanceCount;
    /** 总受理数 **/
    private Integer allAcceptanceCount;
    /** 日受理数环比 **/
    private Double acceptanceLrr;
    /** 日不予受理数 **/
    private Integer dayOutScopeCount;
    /** 总不予受理数 **/
    private Integer allOutScopeCount;
    /** 日不予受理数环比 **/
    private Double outScopeLrr;
    /** 日办结数 **/
    private Integer dayCompletedCount;
    /** 总办结数 **/
    private Integer allCompletedCount;
    /** 日办结数环比 **/
    private Double completedLrr;
    /** 日特殊程序数 **/
    private Integer daySpecificRoutineCount;
    /** 总特殊程序数 **/
    private Integer allSpecificRoutineCount;
    /** 日特殊程序数环比 **/
    private Double specificRoutineLrr;
    /** 预警数 **/
    private Integer allWariningCount;
    /** 日逾期数 **/
    private Integer dayOverTimeCount;
    /** 总逾期数 **/
    private Integer allOverTimeCount;
    /** 日逾期数环比 **/
    private Double overTimeLrr;
    /** 总受理率（受理数/接件数） **/
    private Double allAcceptanceRate;
    /** 总不予受理率（不予受理数/接件数） **/
    private Double allOutScopeRate;
    /** 总逾期率（逾期数/受理数） **/
    private Double allOverTimeRate;
    /** 总办结率（办结数/受理数） **/
    private Double allCompletedRate;
    /** 统计数据日期，不是创建日期（yyyy-MM-dd） **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date statisticsDate;
    /** 所属顶级组织ID **/
    private String rootOrgId;


}
