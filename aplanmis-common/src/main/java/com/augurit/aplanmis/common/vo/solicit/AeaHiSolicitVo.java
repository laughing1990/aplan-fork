package com.augurit.aplanmis.common.vo.solicit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;

@Data
@ApiModel(value = "意见征求vo", description = "意见征求vo")
public class AeaHiSolicitVo {
    // aea_hi_solicit
    private String solicitId; // (主键)
    @ApiModelProperty(value = "")
    private String solicitType; // (征求意见类型：i表示按事项征求，d表示按部门征求)
    @ApiModelProperty(value = "")
    private String solicitOrgId; // (按组织征求配置ID【当SOLICIT_TYPE=d】)
    @ApiModelProperty(value = "")
    private String solicitItemId; // (按事项征求配置ID【当SOLICIT_TYPE=i】)
    @ApiModelProperty(value = "")
    private String applyinstId; // (发起征求的申报ID)
    @ApiModelProperty(value = "")
    private String procinstId; // (发起征求的流程实例ID)
    @ApiModelProperty(value = "")
    private String hiTaskinstId; // (发起征求的历史任务实例ID)
    @ApiModelProperty(value = "")
    private String solicitCode; // (意见征求流水号)
    @ApiModelProperty(value = "")
    private String solicitTopic; // (意见征求主题)
    @ApiModelProperty(value = "")
    private String solicitContent; // (征求意见内容)
    @ApiModelProperty(value = "")
    private Long solicitDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)

    @ApiModelProperty(value = "")
    private Long solicitRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    @ApiModelProperty(value = "")
    private String solicitDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @ApiModelProperty(value = "")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitStartTime; // (意见征求开始时间)
    @ApiModelProperty(value = "")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitEndTime; // (意见征求结束时间)
    @ApiModelProperty(value = "")
    private String isCalcTimerule; // (是否纳入计时)
    @ApiModelProperty(value = "")
    private String solicitTimeruleId; // (意见征求计算策略表ID)

    @ApiModelProperty(value = "")
    private String conclusionFlag; // (办结结论标志位，0表示不通过，1表示通过)

    @ApiModelProperty(value = "")
    private String conclusionDesc; // (办结结论描述)

    @ApiModelProperty(value = "")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date conclusionTime; // (填写结论时间)

    @ApiModelProperty(value = "")
    private String conclusionUserId; // (填写结论用户ID)

    @ApiModelProperty(value = "")
    private String conclusionUserName; // (填写结论用户姓名)

    @ApiModelProperty(value = "")
    private String initiatorOrgId; // (意见征求发起组织ID)

    @ApiModelProperty(value = "")
    private String initiatorOrgName; // (意见征求发起组织名称)

    @ApiModelProperty(value = "")
    private String initiatorUserId; // (意见征求发起用户ID)

    @ApiModelProperty(value = "")
    private String initiatorUserName; // (意见征求发起用户姓名)

    @ApiModelProperty(value = "")
    private Long countLimit; // (办理次数限制，0表示无限制，1或以上表示限制次数)
    @ApiModelProperty(value = "")
    private String solicitState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)
    @ApiModelProperty(value = "")
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "")
    private java.util.Date createTime; // (创建时间)

    //aea_hi_solicit_detail
    private String solicitDetailId; // (主键)
    private String detailOrgId; // (征求意见部门ID)
    private String detailOrgName; // (征求意见部门名称)
    private String itemId; // (事项ID【当SOLICIT_TYPE=i】)
    private String itemVerId; // (事项版本ID【当SOLICIT_TYPE=i】)
    @Size(max = 10)
    private Long detailDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)
    @Size(max = 10)
    private Long detailRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    private String detailDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailStartTime; // (意见征求开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailEndTime; // (意见征求结束时间)
    private String detailState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)

    // aea_hi_solicit_detail_user

    private String detailTaskId; // (主键)
    private String userId; // (用户ID)
    private String userConclusion; // (办理结论，0表示不通过或不同意，1表示通过或同意，2表示不涉及)
    private String userOpinion; // (填写意见)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime; // (签收时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillTime; // (填写意见时间)
    private String taskAction; // (任务动作，0表示正常办理，1表示转交给同一委办局的其他人员，2表示添加同一委办局的其他人员进来)
    private String parentId; // (父ID【当TASK_ACTION=1或2时必填】)

}
