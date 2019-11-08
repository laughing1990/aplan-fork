package com.augurit.aplanmis.common.domain;

import java.io.Serializable;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
@Data
public class AeaLogItemStateHist implements Serializable {

    private static final long serialVersionUID = 1L;
    private String stateHistId; // (主键)
    private String iteminstId; // (事项实例（办件）ID)
    private String isFlowTrigger; // (是否流程流转过程中触发，0表示非流程触发，1表示流程触发)
    private String appinstId; // (所属流程实例ID)
    private String taskinstId; // (所属任务实例ID)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date triggerTime; // (状态变更发生时间)
    private String oldState; // (旧事项状态)
    private String newState; // (新事项状态)
    /**
     * 办件所属委办局组织ID
     */
    private String opsOrgId;
    private String opsUserId; // (操作用户ID)
    private String opsUserName; // (操作用户名称)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date opsFillTime; // (意见填写时间)
    private String opsUserOpinion; // (填写意见内容)
    private String opsAction; // (操作动作)
    private String opsMemo; // (操作备注说明)
    private String busTableName; // (导致状态变更的业务表表名)
    private String busPkName; // (导致状态变更的业务表主键名)
    private String busRecordId; // (导致状态变更的业务表记录ID)
    private String rootOrgId;//根组织ID
}
