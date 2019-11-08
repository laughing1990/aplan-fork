package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 事项输入输出实例表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-08-03 10:33:44</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:33:44</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
@ApiModel(value = "特殊程序实体")
public class AeaHiItemSpecial implements Serializable {

    private static final long serialVersionUID = 1L;
    private String specialId; // (主键ID)
    @ApiModelProperty(name = "iteminstId", value = "事项实例ID")
    private String iteminstId; // (事项实例ID)
    @ApiModelProperty(name = "applyinstId", value = "申报实例ID")
    private String applyinstId; // (申报实例ID)
    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId; // (项目ID)
    @ApiModelProperty(name = "isFlowTrigger", value = "是否流程流转过程中触发，0表示非流程触发，1表示流程触发")
    private String isFlowTrigger; // (是否流程流转过程中触发，0表示非流程触发，1表示流程触发)
    @ApiModelProperty(name = "appinstId", value = "所属流程实例ID")
    private String appinstId; // (所属流程实例ID)
    @ApiModelProperty(name = "specialType", value = "特别程序类型")
    private String specialType; // (特别程序类型，来自于数据字典，包括现场听证、现场勘察、招投标、专家评审、环评公示等)
    @Size(max = 10)
    @ApiModelProperty(name = "specialDueDays", value = "特殊程序办理天数")
    private Long specialDueDays; // (特殊程序办理天数)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "specialDueTime", value = "特殊程序办理期限")
    private java.util.Date specialDueTime; // (特殊程序办理期限)
    @ApiModelProperty(name = "specialReason", value = "原因")
    private String specialReason; // (原因)
    @ApiModelProperty(name = "specialMemo", value = "备注说明")
    private String specialMemo; // (备注说明)
    @ApiModelProperty(name = "approveResult", value = "批准结果0表示不批准，1表示批准")
    private String approveResult; // (批准结果，0表示不批准，1表示批准)
    @ApiModelProperty(name = "approveUserId", value = "批准人ID")
    private String approveUserId; // (批准人ID)
    @ApiModelProperty(name = "approveUserName", value = "批准人姓名")
    private String approveUserName; // (批准人姓名)
    @ApiModelProperty(name = "approveOpinion", value = "批准意见")
    private String approveOpinion; // (批准意见)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "approveTime", value = "批准时间")
    private java.util.Date approveTime; // (批准时间)
    @ApiModelProperty(name = "chargeOrgId", value = "负责部门ID")
    private String chargeOrgId; // (负责部门ID)
    @ApiModelProperty(name = "chargeOrgName", value = "负责部门名称")
    private String chargeOrgName; // (负责部门名称)
    @ApiModelProperty(name = "windowUserId", value = "综窗负责人ID")
    private String windowUserId; // (综窗负责人ID)
    @ApiModelProperty(name = "windowUserName", value = "综窗负责人姓名")
    private String windowUserName; // (综窗负责人姓名)
    @ApiModelProperty(name = "windowPhone", value = "综窗联系方式")
    private String windowPhone; // (综窗联系方式)
    @ApiModelProperty(name = "opsUserId", value = "经办人ID")
    private String opsUserId; // (经办人ID)
    @ApiModelProperty(name = "opsUserName", value = "经办人姓名")
    private String opsUserName; // (经办人姓名)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "opsTime", value = "经办日期")
    private java.util.Date opsTime; // (经办日期)
    @ApiModelProperty(name = "printUserId", value = "打印人ID")
    private String printUserId; // (打印人ID)
    @ApiModelProperty(name = "printUserName", value = "打印人姓名")
    private String printUserName; // (打印人姓名)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "printTime", value = "负责部门名称")
    private java.util.Date printTime; // (打印时间)
    @ApiModelProperty(name = "specialState", value = "特殊程序状态，9表示开始，10表示结束")
    private String specialState; // (特殊程序状态，9表示开始，10表示结束)
    @ApiModelProperty(name = "creater", value = "创建人")
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private java.util.Date createTime; // (创建时间)
    @ApiModelProperty(name = "modifier", value = "修改人")
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "modifyTime", value = "修改时间")
    private java.util.Date modifyTime; // (修改时间)

    @ApiModelProperty(name = "rootOrgId", value = "根组织ID")
    private String rootOrgId;//根组织ID
    @ApiModelProperty(name = "specialDueWay", value = "办理方式")
    private String specialDueWay;//办理方式
    @ApiModelProperty(name = "specialStartMatId", value = "发起特殊程序时用于关联附件的id")
    private String specialStartMatId;//发起特殊程序时用于关联附件的id
    @ApiModelProperty(name = "specialEndMatId", value = "结束特殊程序时用于关联附件的id")
    private String specialEndMatId;//结束特殊程序时用于关联附件的id
    @ApiModelProperty(name = "specialResult", value = "特殊程序结果")
    private String specialResult;//特殊程序结果
    @ApiModelProperty(name = "money", value = "收费金额，单位：元")
    private Double money;//收费金额，单位：元
}
