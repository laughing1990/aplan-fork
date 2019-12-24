package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 征求意见主表-模型
 */
@Data
public class AeaHiSolicit implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String solicitId; // (主键)
    private String solicitType; // (征求意见类型：i表示按事项征求，d表示按部门征求)
    private String solicitOrgId; // (按组织征求配置ID【当SOLICIT_TYPE=d】)
    private String solicitItemId; // (按事项征求配置ID【当SOLICIT_TYPE=i】)
    private String applyinstId; // (发起征求的申报ID)
    private String procinstId; // (发起征求的流程实例ID)
    private String hiTaskinstId; // (发起征求的历史任务实例ID)
    private String solicitCode; // (意见征求流水号)
    private String solicitTopic; // (意见征求主题)
    private String solicitContent; // (征求意见内容)
    @Size(max = 10)
    private Long solicitDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)
    @Size(max = 10)
    private Long solicitRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    private String solicitDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitStartTime; // (意见征求开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitEndTime; // (意见征求结束时间)
    private String isCalcTimerule; // (是否纳入计时)
    private String solicitTimeruleId; // (意见征求计算策略表ID)
    private String conclusionFlag; // (办结结论标志位，0表示不通过，1表示通过)
    private String conclusionDesc; // (办结结论描述)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date conclusionTime; // (填写结论时间)
    private String conclusionUserId; // (填写结论用户ID)
    private String conclusionUserName; // (填写结论用户姓名)
    private String initiatorOrgId; // (意见征求发起组织ID)
    private String initiatorOrgName; // (意见征求发起组织名称)
    private String initiatorUserId; // (意见征求发起用户ID)
    private String initiatorUserName; // (意见征求发起用户姓名)
    @Size(max = 10)
    private Long countLimit; // (办理次数限制，0表示无限制，1或以上表示限制次数)
    private String solicitState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)

}
