package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 窗口日统计表
 */
@Data
public class AeaAnaWinDayStatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    private String windowDayStatisticsId; // (主键)
    private String statisticsRecordId; // (数据生成记录表ID)
    private String windowId; // (服务窗口ID)
    private String windowName; // (窗口名称)
    private String regionId; // (行政区划ID（区县级以下的需要换算成区县级）)
    private String regionName; // (行政区划名称)
    private String applySource; // (申报来源，统计维度。net表示网厅申报，win表示现场申报)
    private String applyRecordId; // (阶段/辅线/并行推进事项ID)
    private String applyRecordName; // (阶段/辅线/并行推进事项名称)
    private String dybzspjdxh;//对应国家标准审批阶段，多选，1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进
    private String isNode;//1表示主线，2表示辅线 3技术审查
    private String isParallel;//是否并行推进事项。0表示否，1表示是
    @Size(max = 10)
    private Long dayApplyCount; // (日接件数)
    @Size(max = 10)
    private Long allApplyCount; // (总接件数)
    @Size(max = 4)
    private Double applyLrr; // (日接件数环比)
    @Size(max = 10)
    private Long allInSupplementCount; // (待补全数（不需查询历史表统计）)
    @Size(max = 10)
    private Long allSupplementedCount; // (补全待确认数（不需查询历史表统计）)
    @Size(max = 10)
    private Long dayPreAcceptanceCount; // (日预受理数（需查询历史表统计）)
    @Size(max = 10)
    private Long allPreAcceptanceCount; // (总预受理数)
    @Size(max = 4)
    private Double preAcceptanceLrr; // (日预受理数环比)
    @Size(max = 10)
    private Long dayOutScopeCount; // (日不予受理数)
    @Size(max = 10)
    private Long allOutScopeCount; // (总不予受理数)
    @Size(max = 4)
    private Double outScopeLrr; // (日不予受理数环比)
    @Size(max = 10)
    private Long dayCompletedCount; // (日办结数)
    @Size(max = 10)
    private Long allCompletedCount; // (总办结数)
    @Size(max = 4)
    private Double completedLrr; // (日办结数环比)
    @Size(max = 10)
    private Long dayOverTimeCount; // (日逾期数)
    @Size(max = 10)
    private Long allOverTimeCount; // (总逾期数)
    @Size(max = 4)
    private Double overTimeLrr; // (日逾期数环比)
    @Size(max = 4)
    private Double allPreAcceptanceRate; // (总预受理率（预受理数/接件数）)
    @Size(max = 4)
    private Double allOutScopeRate; // (总不予受理率（不予受理数/接件数）)
    @Size(max = 4)
    private Double allOverTimeRate; // (总逾期率（逾期数/预受理数）)
    @Size(max = 4)
    private Double allCompletedRate; // (总办结率（办结数/预受理数）)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date statisticsDate; // (统计数据日期，不是创建日期（yyyy-MM-dd）)
    private String rootOrgId; // (所属顶级组织ID)

    @Transient
    private String startTime;
    @Transient
    private String endTime;
}
