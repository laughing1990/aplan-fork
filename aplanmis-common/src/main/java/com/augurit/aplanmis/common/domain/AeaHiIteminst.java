package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:16</li>
 * </ul>
 */
@Data
public class AeaHiIteminst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String iteminstId; // (ID)
    private String itemId; // (事项实例（办件）ID)
    private String iteminstCode; // (事项实例（办件）编号)
    private String iteminstName; // (事项实例（办件）名称)
    private String approveOrgId; // (审批部门ID)
    private String isLackIteminst; // (是否为容缺事项实例（办件），0表示否，1表示是)
    private String isSeriesApprove; // (是否并行审批事项，0表示不是，1表示是)
    private String themeVerId; // (所属主题实例ID【当IS_PAR_ITEM=1】)
    private String stageinstId; // (所属阶段实例ID【当IS_PAR_ITEM=1】)
    private String seriesinstId; // (所属串联实例ID【当IS_PAR_ITEM=0】)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime; // (开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date registerTime; // (登记时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime; // (受理时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime; // (结束时间)
    private String publicMode; // (公开方式)
    private String iteminstState; // (实例状态，1表示已接件，2表示已撤件，3已受理，4不受理，5不予受理，6补正（开始），7补正（结束），8部门开始办理，9特别程序（开始），10特别程序（结束），11办结（通过），12办结（容缺通过），13办结（不通过），14.撤回，15.撤销)

    // 部门受理时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date acceptTime;

    private String innerAppinstId; // (（事项审批部门）内部业务流程模板实例ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String iteminstLevel; // (办理层级，0表示区级，1表示市级)
    private String businessState; // (业务状态）已废除
    private String itemVerId; // (事项版本ID)
    private String isJoinApproval; // (是否参与业务协同审批：1 是，0 否)
    //private String isMatFull;//办件材料是否齐全(0表示必要材料不全（即不予受理），1表示必要材料齐全但有容缺，2表示所有材料齐全)
    private String itemApplyCode;//事项申报流水号
    private String rootOrgId;//根组织ID
    private String isDeleted;
    private String isToleranceAccept;//是否容缺受理，1表示是，0表示否

    private String isSmsSend; //是否已出件，0表示未出件，1表示已出件
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date smsSendTime;//最新出件时间
    private String procinstId; //流程实例ID
    private String iteminstType; //事项实例类型： a 行政审批，p 告知承诺制
    private Double toleranceTime; //办件容缺办结时的时限
    private String timeruleId; //办件容缺办结时的时限计算规则id
    private String isSuspendedBefore;// 撤销之前是否有挂起：1 是，0 否（如果不同意撤销，将恢复流程实例之前的状态）

    //非表字段
    private String approveOrgName;  //审批部门
    private String orgShortName;// 部门简称
    private Double dueNum;//办理时限
    private String dueNumUnit;//办理时限单位
    private String itemProperty;//办件类型
    private String applyinstCode;//申报流水号
    private String stageName;//阶段名称
    private String toleranceTimelimitUnit;//办件容缺办结时的时限单位
    private String toleranceIsCompleted; // (容缺补正是否完成：1 是，0 否)
    private List<Map<String, String>> itemStateinsts;//事项情形列表

    //取证登记参数
    private String hasOutCertinst; //是否有输出证照实例，0无1有

    //analyse查询参数
    private String queryIteminstStates;//查询事项实例状态。多种状态用单引号和逗号拼接成字符串
    private String queryApproveOrgIds; //查询审批部门ID。多个ID用单引号和逗号拼接成字符串
    private String queryThemeVerIds;   //查询主题版本ID。多个ID用单引号和逗号拼接成字符串
    private String querySeriesinstIds; //查询串联实例ID。多个ID用单引号和逗号拼接成字符串
    private String queryStageinstIds;  //查询阶段实例ID。多个ID用单引号和逗号拼接成字符串
    private String createTimeMin;//查询满足创建时间>=该字段值的数据
    private String createTimeMax;//查询满足创建时间<=该字段值的数据
    private String endTimeMin;  //查询满足结束时间>=该字段值的数据
    private String endTimeMax;  //查询满足结束时间<=该字段值的数据

    private String bjType;
    private Date dueDate;
    private String isTiming;

    //清空analyse查询参数
    public void clearQueryParam() {
        this.queryIteminstStates = null;
        this.queryApproveOrgIds = null;
        this.queryThemeVerIds = null;
        this.querySeriesinstIds = null;
        this.queryStageinstIds = null;
        this.createTimeMin = null;
        this.createTimeMax = null;
        this.endTimeMin = null;
        this.endTimeMax = null;
    }

    private String applySource; //申报来源

}
