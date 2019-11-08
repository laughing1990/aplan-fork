package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 主题周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-09-05 10:19:04</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:19:04</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaAnaThemeWeekStatistics implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String themeWeekStatisticsId; // (主键)
    private String statisticsRecordId; // (数据生成记录表ID)
    private String themeId; // (主题ID)
    private String themeName; // (主题名称)
    private String applyRecordId; // (阶段/辅线/并行推进事项ID)
    private String applyRecordName; // (阶段/辅线/并行推进事项名称)
    private String dybzspjdxh;//对应国家标准审批阶段，多选，1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进
    private String isNode;//1表示主线，2表示辅线 3技术审查
    private String isParallel;//是否并行推进事项。0表示否，1表示是
    private String applyinstSource;//申报来源，net表示网上申报，win表示窗口申报
    @Size(max = 10)
    private Long weekApplyCount; // (周接件数)
    @Size(max = 10)
    private Long allApplyCount; // (总接件数)
    @Size(max = 4)
    private Double applyLrr; // (周接件数环比)
    @Size(max = 10)
    private Long allInSupplementCount; // (待补全数)
    @Size(max = 10)
    private Long allSupplementedCount; // (补全待确认数)
    @Size(max = 10)
    private Long weekPreAcceptanceCount; // (周预受理数)
    @Size(max = 10)
    private Long allPreAcceptanceCount; // (总预受理数)
    @Size(max = 4)
    private Double preAcceptanceLrr; // (周预受理数环比)
    @Size(max = 10)
    private Long weekOutScopeCount; // (周不予受理数)
    @Size(max = 10)
    private Long allOutScopeCount; // (总不予受理数)
    @Size(max = 4)
    private Double outScopeLrr; // (周不予受理数环比)
    @Size(max = 10)
    private Long weekCompletedCount; // (周办结数)
    @Size(max = 10)
    private Long allCompletedCount; // (总办结数)
    @Size(max = 4)
    private Double completedLrr; // (周办结数环比)
    @Size(max = 10)
    private Long weekOverTimeCount; // (周逾期数)
    @Size(max = 10)
    private Long allOverTimeCount; // (总逾期数)
    @Size(max = 4)
    private Double overTimeLrr; // (周逾期数环比)
    @Size(max = 4)
    private Double allPreAcceptanceRate; // (总预受理率（预受理数/接件数）)
    @Size(max = 4)
    private Double allOutScopeRate; // (总不予受理率（不予受理数/接件数）)
    @Size(max = 4)
    private Double allOverTimeRate; // (总逾期率（逾期数/预受理数）)
    @Size(max = 4)
    private Double allCompletedRate; // (总办结率（办结数/预受理数）)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date statisticsStartDate; // (统计数据开始日期（yyyy-MM-dd 00:00:00）)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date statisticsEndDate; // (统计数据结束日期（yyyy-MM-dd 23:59:59）)
    @Size(max = 10)
    private Long statisticsYear; // (统计数据年份)
    @Size(max = 10)
    private Long weekNum; // (第几周。表示统计数据开始日期和统计数据结束日期是对应年份中第几周。)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (所属顶级组织ID)

}
