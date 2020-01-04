package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty(value = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "阶段id")
    private String stageId;

    @ApiModelProperty(value = "发起辅导单位ID")
    private String applyUnitInfoId;

    @ApiModelProperty(value = "发起辅导用户ID")
    private String applyLinkmanInfoId;

    @ApiModelProperty(value = "部门辅导状态：0表示申请人未发起；1牵头部门待签收；2牵头部门处理中；3所有部门征求处理中；4申请人待确认；5结束")
    private String applyState;

    @ApiModelProperty(value = "部门辅导发起时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date guideStartTime;

    @ApiModelProperty(value = "部门辅导结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "是否智能引导，1是0否")
    private String isItGuide;

    // 扩展字段

    // 项目类型id
    private String themeId;
    // 项目类型
    private String themeName;
    // 主辅线类别
    private String themeCategory;
    // 项目代码/工程编码
    private String gcbm;
    // 项目名称
    private String projName;
    // 申报来源
    private String applySource;
    // 阶段名称
    private String stageName;
    // 部门辅导状态
    private String applyStateName;
    // 搜索关键字
    private String keyword;
    // 当前用户id，用于列表数据权限控制
    private String currentUserId;

}