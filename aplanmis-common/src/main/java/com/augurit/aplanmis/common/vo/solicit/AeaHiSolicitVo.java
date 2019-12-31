package com.augurit.aplanmis.common.vo.solicit;

import com.augurit.agcloud.bpm.common.domain.ActStoRemindAndReceiver;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "意见征求vo", description = "意见征求vo")
public class AeaHiSolicitVo {
    // aea_hi_solicit
    private String solicitId; // (主键)
    @ApiModelProperty(value = "征求意见类型：i表示按事项征求，d表示按部门征求")
    private String solicitType;
    @ApiModelProperty(value = "业务类型，来源数据字典")
    private String busType;

    @ApiModelProperty(value = "按组织征求配置ID【当SOLICIT_TYPE=d】")
    private String solicitOrgId;

    @ApiModelProperty(value = "按事项征求配置ID【当SOLICIT_TYPE=i】")
    private String solicitItemId;

    @ApiModelProperty(value = "发起征求的申报ID")
    private String applyinstId;

    @ApiModelProperty(value = "发起征求的流程实例ID")
    private String procinstId;

    @ApiModelProperty(value = "发起征求的历史任务实例ID")
    private String hiTaskinstId;

    @ApiModelProperty(value = "意见征求流水号")
    private String solicitCode;

    @ApiModelProperty(value = "意见征求主题")
    private String solicitTopic;

    @ApiModelProperty(value = "征求意见内容")
    private String solicitContent;

    @ApiModelProperty(value = "意见征求承诺时限，例如：5个工作日，那该字段为5")
    private Double solicitDueDays;

    @ApiModelProperty(value = "意见征求实际时限，例如：5个工作日，那该字段为5")
    private Long solicitRealDays;

    @ApiModelProperty(value = "意见征求时限单位，z表示自然日，g表示工作日")
    private String solicitDaysUnit;
    @ApiModelProperty(value = "意见征求开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitStartTime;

    @ApiModelProperty(value = "意见征求结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitEndTime;

    @ApiModelProperty(value = "是否纳入计时")
    private String isCalcTimerule;

    @ApiModelProperty(value = "意见征求计算策略表ID")
    private String solicitTimeruleId;

    @ApiModelProperty(value = "办结结论标志位，0表示不通过，1表示通过")
    private String conclusionFlag;

    @ApiModelProperty(value = "办结结论描述")
    private String conclusionDesc;

    @ApiModelProperty(value = "填写结论时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date conclusionTime;

    @ApiModelProperty(value = "填写结论用户ID")
    private String conclusionUserId;

    @ApiModelProperty(value = "填写结论用户姓名")
    private String conclusionUserName;

    @ApiModelProperty(value = "意见征求发起组织ID")
    private String initiatorOrgId;

    @ApiModelProperty(value = "意见征求发起组织名称")
    private String initiatorOrgName;

    @ApiModelProperty(value = "意见征求发起用户ID")
    private String initiatorUserId;

    @ApiModelProperty(value = "意见征求发起用户姓名")
    private String initiatorUserName;

    @ApiModelProperty(value = "办理次数限制，0表示无限制，1或以上表示限制次数")
    private Long countLimit;

    @ApiModelProperty(value = "征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止")
    private String solicitState;

    @ApiModelProperty(value = "联系人姓名")
    private String solicitLinkmanName;

    @ApiModelProperty(value = "联系人手机号码")
    private String solicitLinkmanPhone;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "受理时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "意见征求创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //aea_hi_solicit_detail
    @ApiModelProperty(value = "主键")
    private String solicitDetailId;

    @ApiModelProperty(value = "征求意见部门ID")
    private String detailOrgId;

    @ApiModelProperty(value = "征求意见部门名称")
    private String detailOrgName;

    @ApiModelProperty(value = "事项ID【当SOLICIT_TYPE=i】")
    private String itemId;

    @ApiModelProperty(value = "事项版本ID【当SOLICIT_TYPE=i】")
    private String itemVerId;

    @ApiModelProperty(value = "意见征求承诺时限，例如：5个工作日，那该字段为5")
    private Long detailDueDays;

    @ApiModelProperty(value = "意见征求实际时限，例如：5个工作日，那该字段为5")
    private Long detailRealDays;

    @ApiModelProperty(value = "意见征求时限单位，z表示自然日，g表示工作日")
    private String detailDaysUnit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "意见征求开始时间")
    private Date detailStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "意见征求结束时间")
    private Date detailEndTime;

    @ApiModelProperty(value = "征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止")
    private String detailState;

    // aea_hi_solicit_detail_user
    @ApiModelProperty(value = "主键")
    private String detailUserId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "办理结论，0表示不通过或不同意，1表示通过或同意，2表示不涉及")
    private String userConclusion;

    @ApiModelProperty(value = "填写意见")
    private String userOpinion;

    @ApiModelProperty(value = "签收时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    @ApiModelProperty(value = "填写意见时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fillTime;

    @ApiModelProperty(value = "任务动作，0表示正常办理，1表示转交给同一委办局的其他人员，2表示添加同一委办局的其他人员进来")
    private String taskAction;

    @ApiModelProperty(value = "父ID【当TASK_ACTION=1或2时必填】")
    private String parentId;

    //时限相关
//    private Double dueNum;
    @ApiModelProperty(value = "时限状态")
    protected String instState;
    @ApiModelProperty(value = "")
    private Double overdueTime;

    @ApiModelProperty(value = "")
    private Double remainingTime;

    @ApiModelProperty(value = "")
    private String timeruleUnit;
    @ApiModelProperty(value = "节点时限列表")
    private List nodeTimelimitList;

    //申报实例相关
    @ApiModelProperty(value = "")
    private String applyinstCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "申报时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ApiModelProperty(value = "")
    private String isSeriesApprove;

    @ApiModelProperty(value = "")
    private String itemName;

    @ApiModelProperty(value = "")
    private String stageName;

    @ApiModelProperty(value = "主题ID")
    private String themeId;
    @ApiModelProperty(value = "主题名称")
    private String themeName;

    @ApiModelProperty(value = "项目工程名称")
    private String projName;

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目代码")
    private String localCode;

    @ApiModelProperty(value = "提醒信息列表")
    private List<ActStoRemindAndReceiver> remindList;

    private String taskId;
    private String taskName;
    private String taskDefKey;

    @ApiModelProperty(value = "申报来源")
    private String applyinstSource;
    @ApiModelProperty(value = "申报来源：网厅，窗口")
    private String applySource;
    @ApiModelProperty(value = "申报类型，单项 并联")
    private String applyType;


    @ApiModelProperty(value = "剩余/逾期用时显示文本")
    private String remainingOrOverTimeText;

    @ApiModelProperty(value = "法定办结时限显示")
    private String dueNumText;

    @ApiModelProperty(value = "一次征询回复进度或状态")
    private String rateProgress;

    @ApiModelProperty(value = "所有被征询部门数量")
    private int allProgressNum;

    @ApiModelProperty(value = "已完成征询部门数量")
    private int finishProgressNum;

    @ApiModelProperty(value = "待完成征询部门数量")
    private int pendingProgressNum;

    private String viewId;
    @ApiModelProperty(value = "是否发起人，true 是，false 否")
    private boolean promoter;
    @ApiModelProperty(value = "当前征集的次数")
    private int solicitIndex;

}
