package com.augurit.aplanmis.front.approve.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("审批详情页 --> 申报方式和状态")
public class BpmApproveStateVo {
    @ApiModelProperty(value = "申报状态：0:已接件未审核（适用于网厅）、1:已接件并审核、" +
            "2:已受理、3:待补全、4:已补全、5:不予受理、6:审批中、7:已办结", required = true,
            dataType="string",allowableValues ="0,1,2,3,4,5,6,7" )
    private String applyStyle;
    @ApiModelProperty(value = "当前申报状态：0:已接件未审核（适用于网厅）、1:已接件并审核、" +
            "2:已受理、3:待补全、4:已补全、5:不予受理、6:审批中、7:已办结", required = true,
            dataType="string",allowableValues ="0,1,2,3,4,5,6,7" )
    private String currentState;
    private String currentStateValue;
    @ApiModelProperty(value = "事项实例（办件）名称", required = true, dataType="string")
    private String stageOrItemName;
    @ApiModelProperty(value = "项目名称", required = true, dataType="string" )
    private String projName;
    @ApiModelProperty(value = "项目编号", required = true, dataType = "string")
    private String projCode;
    @ApiModelProperty(value = "是否串联审批，0:表示并联审批，1:表示串联审批", required = true, dataType="string",allowableValues = "0,1" )
    private String isSeriesinst;
    @ApiModelProperty(value = "是否是审批人：0：窗口人员；1：审批人", required = true, dataType="string" ,allowableValues = "0,1")
    private String isApprover;
    @ApiModelProperty(value = "项目ID", required = true, dataType="string" )
    private String projId;
    @ApiModelProperty(value = "阶段定义id", required = true, dataType="string" )
    private String stageId;

    @ApiModelProperty(value = "服务协同状态", required = true, dataType="string" )
    private String coordinationState;
    @ApiModelProperty(value = "是否显示发起特殊程序按钮：1是 0否", required = true, dataType = "string")
    private String showSpecialBtn = "1";
    @ApiModelProperty(value = "是否发起过特殊程序：1是 0否", required = true, dataType = "string")
    private String hasSpecial = "0";
    @ApiModelProperty(value = "是否发起过补正：1是 0否", required = true, dataType = "string")
    private String hasSupply = "0";
    @ApiModelProperty(value = "是否显示一张表单：1是 0否", required = true, dataType = "string")
    private String isShowOneForm = "0";
    @ApiModelProperty(value = "是否有撤件申请：1是 0否", required = true, dataType = "string")
    private String ishasApplyinstCancel = "0";
    @ApiModelProperty(value = "是否有意见征求：1是 0否", required = true, dataType = "string")
    private String hasYJZQ = "0";
    @ApiModelProperty(value = "是否有联合评审：1是 0否", required = true, dataType = "string")
    private String hasLHPS = "0";
    @ApiModelProperty(value = "是否联合评审通过，是的话显示通过按钮，否则显示不通过按钮：1是 0否", required = true, dataType = "string")
    private String isPassLHPS;
    @ApiModelProperty(value = "是否有一次征询：1是 0否", required = true, dataType = "string")
    private String hasYCZX = "0";


    @ApiModelProperty(value = "事项版本ID", required = true, dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "事项ID", required = true, dataType = "string")
    private String itemId;
}
