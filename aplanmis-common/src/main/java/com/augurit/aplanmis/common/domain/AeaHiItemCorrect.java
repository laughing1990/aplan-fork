package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.dto.MatCorrectDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 事项输入输出实例表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-08-03 10:26:15</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:26:15</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiItemCorrect implements Serializable {

    private static final long serialVersionUID = 1L;
    private String correctId; // (主键ID)
    private String iteminstId; // (事项实例ID)
    private String applyinstId; // (申报实例ID)
    private String projInfoId; // (项目ID)
    private String isFlowTrigger; // (是否流程流转过程中触发，0表示非流程触发，1表示流程触发)
    private String appinstId; // (所属流程实例ID)
    @Size(max = 10)
    private Long correctDueDays; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date correctDueTime; // ()
    private String matinstIds; // (材料实例ID列表，以半角逗号分隔)
    private String correctDesc; // (补正说明)
    private String correctMemo; // (备注说明)
    private String chargeOrgId; // (负责部门ID)
    private String chargeOrgName; // (负责部门名称)
    @Size(max = 10)
    private String windowUserId; // (综窗负责人ID)
    private String windowUserName; // (综窗负责人姓名)
    private String windowPhone; // (综窗联系方式)
    private String opsUserId; // (经办人ID)
    private String opsUserName; // (经办人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date opsTime; // (经办日期)
    private String correctUserName; // (补正结束人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date correctEndTime; // (补正结束时间)
    private String printUserId; // (打印人ID)
    private String printUserName; // (打印人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date printTime; // (打印时间)
    private String correctState; // (补正状态，6表示待补正，7表示已补正（材料齐全），5表示不予受理)
    private String isDeleted; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID

    //扩展字段
    private String projInfoName;
    private String localCode;
    private String applyinstCode;
    private String owner;
    private String iteminstName;
    private String approveOrgName;
    private String applySubject;
    private String linkmanInfoId;
    private String linkman;
    private String linkmanPhone;
    private List<MatCorrectDto> matCorrectDtos;
    private String regionalism;//行政区划
    private String regionName;//行政名称

}
