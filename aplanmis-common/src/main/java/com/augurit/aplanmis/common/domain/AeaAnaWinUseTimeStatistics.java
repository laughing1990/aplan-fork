package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 申报用时统计表（统计办理时限延期最长和办理时限压缩最短的申报）-模型
 */
@Data
public class AeaAnaWinUseTimeStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     **/
    private String useTimeStatisticsId;

    /**
     * 数据生成记录表ID
     **/
    private String statisticsRecordId;

    /**
     * 申请实例ID
     **/
    private String applyinstId;

    /**
     * 时限计算实例ID
     **/
    private String timeruleInstId;

    /**
     * 统计用时类型，0表示最短用时，1表示最长用时
     **/
    private String useTimeType;

    /**
     * 是否已办结，USE_TIME_TYPE为1时使用。0表示未办结，1表示已办结
     **/
    private String isCompleted;

    /**
     * 是否串联审批，0表示并联审批，1表示串联审批
     **/
    private String isSeriesApprove;

    /**
     * 阶段名称。IS_SERIES_APPROVE为0时必填
     **/
    private String stageName;

    /**
     * 主题名称。IS_SERIES_APPROVE为0时必填
     **/
    private String themeName;

    /**
     * 事项名称。IS_SERIES_APPROVE为1时必填
     **/
    private String itemName;

    /**
     * 办件类型。IS_SERIES_APPROVE为1时必填
     **/
    private String itemProperty;

    /**
     * 服务窗口ID
     **/
    private String windowId;

    /**
     * 窗口名称
     **/
    private String windowName;

    /**
     * 项目名称
     **/
    private String projName;

    /**
     * 行政区划ID（区县级以下的需要换算成区县级）
     **/
    private String regionId;

    /**
     * 行政区划名称
     **/
    private String regionName;

    /**
     * 申报来源。net表示网厅申报，win表示现场申报
     **/
    private String applySource;

    /**
     * 时限数
     **/
    @Size(max = 30)
    private Long timeLimit;

    /**
     * 【计算规则时间单位】ND：自然日，WD：工作日，NH：小时（自然日），WH：小时（工作日）
     **/
    private String timeruleUnit;

    /**
     * 已用时
     **/
    @Size(max = 30)
    private Double useLimitTime;

    /**
     * 剩余用时
     **/
    @Size(max = 30)
    private Double remainingTime;

    /**
     * 逾期用时
     **/
    @Size(max = 30)
    private Double overdueTime;

    /**
     * 用时率（已用时/时限数）
     **/
    @Size(max = 4)
    private Double useTimeRate;

    /**
     * 是否全量统计，0表示否，1表示全量统计
     **/
    private String isAllCount;

    /**
     * IS_ALL_COUNT为0时必填，统计数据开始日期（yyyy-MM-dd 00:00:00）
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsStartDate;

    /**
     * IS_ALL_COUNT为0时必填，统计数据结束日期（yyyy-MM-dd 23:59:59）
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsEndDate;

    /**
     * 备注
     **/
    private String memo;

    /**
     * 创建人
     **/
    private String creater;

    /**
     * 创建时间
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 所属顶级组织ID
     **/
    private String rootOrgId;


}
