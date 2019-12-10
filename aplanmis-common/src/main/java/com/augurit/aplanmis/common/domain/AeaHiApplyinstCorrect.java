package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.dto.MatCorrectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 材料补全实例表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-08-28 17:33:44</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:33:44</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiApplyinstCorrect implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "applyinstCorrectId", value = "补全实例ID")
    private String applyinstCorrectId; // (主键ID)

    @ApiModelProperty(name = "applyinstId", value = "申报实例ID")
    private String applyinstId; // (申报实例ID)

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId; // (项目ID)

    @ApiModelProperty(name = "isFlowTrigger", value = "(是否流程流转过程中触发，0表示非流程触发，1表示流程触发)")
    private String isFlowTrigger; // (是否流程流转过程中触发，0表示非流程触发，1表示流程触发)

    @ApiModelProperty(name = "appinstId", value = "所属流程实例ID")
    private String appinstId; // (所属流程实例ID)

    @Size(max = 10)
    @ApiModelProperty(name = "correctDueDays", value = "补全期限")
    private Long correctDueDays; // ()

    @ApiModelProperty(name = "correctDueTime", value = "补全限定时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date correctDueTime; // ()

    @ApiModelProperty(name = "correctMemo", value = "备注说明")
    private String correctMemo; // (备注说明)

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

    @ApiModelProperty(name = "opsTime", value = "经办日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date opsTime; // (经办日期)

    @ApiModelProperty(name = "correctUserName", value = "补正结束人姓名")
    private String correctUserName; // (补正结束人姓名)

    @ApiModelProperty(name = "correctEndTime", value = "补正结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date correctEndTime; // (补正结束时间)

    @ApiModelProperty(name = "printUserId", value = "打印人ID")
    private String printUserId; // (打印人ID)

    @ApiModelProperty(name = "printUserName", value = "打印人姓名")
    private String printUserName; // (打印人姓名)

    @ApiModelProperty(name = "printTime", value = "打印时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date printTime; // (打印时间)

    @ApiModelProperty(name = "correctState", value = "补正状态，6表示待补正，7表示已补正（待确认），8表示已补正（已确认，材料齐全），5表示不予受理")
    private String correctState; // (补正状态，6表示待补正，7表示已补正（待确认），8表示已补正（已确认，材料齐全），5表示不予受理)

    private String isDeleted; // ()

    private String creater; // (创建人)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)

    private String modifier; // (修改人)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)

    private String rootOrgId; // ()

    //扩展字段
    @ApiModelProperty(name = "projInfoName", value = "项目名称")
    private String projInfoName;

    @ApiModelProperty(name = "localCode", value = "项目代码")
    private String localCode;

    @ApiModelProperty(name = "applyinstCode", value = "申请流水号")
    private String applyinstCode;

    @ApiModelProperty(name = "owner", value = "业主名称")
    private String owner;

    @ApiModelProperty(name = "iteminstName", value = "事项名称")
    private String iteminstName;

    private String applySubject;

    private String linkmanInfoId;

    @ApiModelProperty(name = "matCorrectDtos", value = "补全材料清单")
    private List<MatCorrectDto> matCorrectDtos;

    @ApiModelProperty(name = "chargeOrgName", value = "审批部门")
    private String chargeOrgName;

    //扩展字段
    private String applyinstSource;
    private String regionalism;//行政区划
    private String regionName;//行政名称
    private List<AeaUnitInfo> unitInfos;//申报主体list（用于证照库选择申报主体）
    private String itemVerIds;//事项版本id list（用于证照库查询证照使用）
}
