package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ApiModel(value = "部门辅导")
@Getter
@Setter
@ToString
public class AeaHiGuide {

    @ApiModelProperty(value = "主键")
    private String guideId;

    @ApiModelProperty(value = "申报实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "申请辅导用户类型：p表示个人，u表示企业")
    private String guideType;

    @ApiModelProperty(value = "发起辅导单位ID")
    private String applyUnitInfoId;

    @ApiModelProperty(value = "发起辅导用户ID")
    private String applyLinkmanInfoId;

    @ApiModelProperty(value = "部门辅导状态：0表示申请人未发起；1牵头部门待签收；2牵头部门处理中；3所有部门征求处理中；4申请人待确认；5结束")
    private String applyState;

    @ApiModelProperty(value = "部门辅导发起时间")
    private Date guideStartTime;

    @ApiModelProperty(value = "部门辅导结束时间")
    private Date guideEndTime;

    @ApiModelProperty(value = "部门辅导时限定义（单位工作日小时）")
    private Double dueTimeLimit;

    @ApiModelProperty(value = "实际用时（单位工作日小时）")
    private Double realTimeLimit;

    @ApiModelProperty(value = "牵头部门组织ID")
    private String leaderOrgId;

    @ApiModelProperty(value = "牵头部门用户ID")
    private String leaderUserId;

    @ApiModelProperty(value = "牵头部门汇总意见")
    private String leaderOrgOpinion;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "是否智能引导，1是0否")
    private String isItGuide;
}