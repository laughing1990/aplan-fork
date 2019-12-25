package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.commonInterface.FlowObject;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 项目申报实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:08</li>
 * </ul>
 */
@Data
public class AeaHiApplyinst implements Serializable, FlowObject {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String applyinstId; // (主键)
    private String applyinstCode; // (申请编号)
    private String applyinstSource; // (申报来源，net表示网上申报，win表示窗口申报)
    private String queueNo; // (排队号【当APPLY_SOURCE=win】)
    private String isSeriesApprove; // (是否串联审批，0表示并联审批，1表示串联审批)
    private String applyinstMemo; // (备注说明)
    private String applyinstState; // (申请状态)申报状态：0已接件未审核（适用于网厅）、1已接件并审核、2已受理、3待补全、4已补全、5不予受理、6已办结
    private String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String linkmanInfoId; // (联系人ID)
    private String branchOrg; // (分局承办组织ID)
    private String applySubject; // (申办主体：1 单位，0 个人)
    private String isServiceCooperative; // (【当IS_SERIES_APPROVE=1】是否关联服务协同：1 是，0 否)
    private java.lang.String isSmsSend; // (是否已出件，0表示未出件，1表示已出件)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date smsSendTime; // (最新出件时间)
    private String rootOrgId;//根组织ID
    private String isTemporarySubmit; //是否临时提交  0否  1是
    private String parentApplyinstId;//父申报实例ID【用于并联申报时，并行事项的单项申报与并联申报之间的关系】

    // 是否绿色通道 1: 是, 0: 否
    private String isGreenWay;

    // 窗口受理时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date acceptTime;
    // 窗口办结时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date endTime;

    //**************** 拓展字段 *****************************************
    private Double dueNum;//办理时限
    private String dueUnit;//办理时限单位

    private Map<String, Boolean> approvalOrgCode;
    private Map<String, Boolean> isBranchHandle;
    private Map<String, Boolean> iteminsts;
    //private Map<String, Boolean> iteminsts;
    private String projInfoId;

    //  扩展字段
    private String createrCN;
    private String stageId;
    private String stageItemIds;
    private String itemId;
    private String itemVerId;
    private String starParStageId;
    private String starIteminstId;

    private String stageName;
    private Integer num;

    private String startProcItemStateId;

    private String taskId;
    private String assignee;//受理人

    private String linkManName;
    private String nodeName;  //节点名称
    private String state;   //当前状态

    private List<AeaProjInfo> projInfoList;//申请实例下的项目集合

    private String localCode;//本地项目代码
    private String centralCode;//国家项目代码
    private String approveOrgId;//审批组织ID
    private String businessState;//业务状态
    private String iteminstState;//事项实例状态
    private String iteminstId;//事项实例ID
    private String itemName;//事项名称
    private String itemCode;//事项编码
    private String itemProperty;//办件类别

    //
    private String projName;//项目名称
    private String themeName;//主题名称
    private String regionId;//所属区域id

    private String stageInstId;//阶段实例ID

    private String isParallel;//是否（是否有）并行推进事项。0表示否，1表示是',(主題階段申报用到)

    private String appinstId;// 流程模板实例
    private String appId;// 流程模板

    private Map<String, Boolean> stateinsts;//所有情形map集合，格式为，存在的情形id:true
    @Override
    public String getMasterRecordId() {
        return this.getApplyinstId();
    }
}
