package com.augurit.aplanmis.front.approve.vo;

import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("审批意见树")
public class BpmHistoryCommentFormVo {

    @ApiModelProperty(value = "处理中的任务", required = true, dataType = "boolean")
    private boolean dealingTask;

    @ApiModelProperty(value = "第一个组织简称", required = true, dataType="string")
    private String firstOrgShortName;

    @ApiModelProperty(value = "第二个组织简称", required = true, dataType="string")
    private String secondOrgShortName;

    @ApiModelProperty(value = "是否当前审批人", required = true, dataType = "string")
    private String isApprover;

    @ApiModelProperty(value = "附件个数", required = true, dataType = "int")
    private Integer attDetailNum;

    @ApiModelProperty(value = "任务ID", required = true, dataType="string")
    private String taskId;

    @ApiModelProperty(value = "节点名称", required = true, dataType="string")
    private String nodeName;

    @ApiModelProperty(value = "审批意见", required = true, dataType = "string")
    private String commentMessage;

    @ApiModelProperty(value = "审批结果", required = true, dataType = "string")
    private String approveResult;

    @ApiModelProperty(value = "任务办理人", required = true, dataType = "string")
    private String taskAssignee;

    @ApiModelProperty(value = "开始日期", required = true, dataType = "date")
    private Date sigeInDate;

    @ApiModelProperty(value = "结束日期", required = true, dataType="date")
    private Date endDate;

    @ApiModelProperty(value = "任务状态", required = true, dataType="int")
    private Integer taskState;

    @ApiModelProperty(value = "组织id", required = true, dataType="string")
    private String orgId;

    @ApiModelProperty(value = "组织名称", required = true, dataType="string")
    private String orgName;

    @ApiModelProperty(value = "用户手机", required = true, dataType="string")
    private String userMobile;

    @ApiModelProperty(value = "是否多工作项节点，1是0否", required = true, dataType = "string")
    private String isMultiTaskNode = "0";

    @ApiModelProperty(value = "是否事项节点，1是0否", required = true, dataType = "string")
    private String isItemNode = "0";

    @ApiModelProperty(value = "事项实例id，是事项节点时则不为空", required = true, dataType = "string")
    private String iteminstId;

    @ApiModelProperty(value = "事项审批过程中，补正次数，是事项节点时则不为空", required = true, dataType = "int")
    private Integer bzNum;

    @ApiModelProperty(value = "事项审批过程中，特殊程序次数，是事项节点时则不为空", required = true, dataType = "int")
    private Integer tscxNum;

    @ApiModelProperty(value = "子节点", required = true, dataType="java.utl.List")
    private List<BpmHistoryCommentFormVo> childNode;

    @ApiModelProperty(value = "补正，补全，特殊程序意见列表", required = true, dataType="java.utl.List")
    private List<SupplyOrSpacialCommentVo> otherCommentList;
}
